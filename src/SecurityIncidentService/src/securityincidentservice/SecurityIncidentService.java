/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package securityincidentservice;

import ca.ryerson.rnet.ls3.si.nnl.NameEntityExtractor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Properties;
import java.util.TimerTask;

import javax.mail.BodyPart;
import javax.mail.Flags;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 *
 * @author  Hossein Fani
 * <https://sites.google.com/site/hosseinfani at ls3.rnet.ryerson.ca>
 */
public class SecurityIncidentService extends TimerTask{
    
    private static Properties props = new Properties();
    private SecurityIncidentService() throws FileNotFoundException, IOException{
        props.load(getClass().getResourceAsStream("smtp.properties"));
        
    }
    
    public static void main(String[] args) throws IOException {
        TimerTask timerTask = new SecurityIncidentService();
//        Timer timer = new Timer(true);
//        timer.schedule(timerTask, 0, Integer.parseInt(props.getProperty("pollinginterval")) * 1000);
        System.out.println("SecurityIncidentService started");
        while(true) {
            try {
                timerTask.run();
                Thread.sleep(Integer.parseInt(props.getProperty("pollinginterval")) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("SecurityIncidentService stopped");
            }
        }
        
    }
    
    @Override
    public void run() {
        System.out.println("Querying the mailbox, started at:" + new Date());
        fetchSecurityIncidentNotifications();
        System.out.println("Querying the mailbox, finished at:" + new Date());
    }
    private static void fetchSecurityIncidentNotifications() {
        try {
            Session session = Session.getDefaultInstance(props, null);
            
            Store store = session.getStore("imaps");
            store.connect(props.getProperty("mailserver"), props.getProperty("username"), props.getProperty("password"));
            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_WRITE);
            
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);//should be false
            //FlagTerm unseenFlagTerm = new FlagTerm(seen, true);
            
            Message[] messages = inbox.search(unseenFlagTerm);
            System.out.println("New notification count:" + messages.length);
            for (Message m : messages) {
                String s = "";
                if(m.getContent() instanceof Multipart){
                    Multipart mp = (Multipart)m.getContent();
                    for(int i=0;i<mp.getCount();i++) {
                        BodyPart bodyPart = mp.getBodyPart(i);
                        if (bodyPart.isMimeType("text/*")) {
                            s = s.concat((String) bodyPart.getContent());
                        }
                    }
                }
                else{
                    s = (String)m.getContent();
                }
                String s1 = s;
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        //System.out.println("New thread to process is trigered. " + Thread.currentThread().toString());
                        try {
                            fetchSecurityIncidentBody(s1, null, Integer.parseInt(props.getProperty("retrycount")));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } 
                    }
                });
                t.start();
                //break;//should be commented
            }
            inbox.close(false);
            store.close();
            
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void fetchSecurityIncidentBody(String notification, String url, int retryCount) throws ClassNotFoundException, MalformedURLException, IOException {
        Document d = Jsoup.parse(notification);
        Elements es = d.select("a");
        for(Element e : es){
            url = e.attr("href");
            if(url.contains("http://www.ryerson.ca/irm/alerts_reports/alerts/alerts"))
                break;
        }
        System.out.println("Fetching the notification page at :" + url);
        try{
            d = Jsoup.connect(url).get();
        }
        catch(Exception ex)
        {
            retryCount--;
            if(retryCount < 1)
                System.out.println("Failed >> " + ex.toString() + ":" + url);
            else{
                System.out.println("Retry: " + url);
                fetchSecurityIncidentBody(null, url, retryCount);
            }
        }
//            List<URL> imageUrls = new ArrayList<>();
//            for(Element e : images){
//                (new URL(e.attr("abs:src")));
//            }
        es = d.select("#page_content");
        Elements images = es.select("img");
        if(images != null){
            es.addAll(images);
        }
        //System.out.println("Calling OpenNLP to extract information out of " + url);
        NameEntityExtractor.save(es);
        
    }
}
