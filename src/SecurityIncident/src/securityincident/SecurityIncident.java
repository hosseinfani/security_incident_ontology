/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package securityincident;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author hfani
 */
public class SecurityIncident {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        //File folder = new File("C:\\Users\\hfani\\Desktop\\SecurityIncidentsNew");
//        File[] listOfFiles = folder.listFiles();
//
//        for (File file : listOfFiles) {
//            if (file.isFile()) {
//
        for(Object o : Files.lines(Paths.get(args[0]), StandardCharsets.UTF_8).toArray()){
            String url = o.toString();
            grabPage(url, 5);
        }
    }
    
    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);
        byte[] b = new byte[2048];
        int length;
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();
    }
    
    public static void grabPage(String url, int retryCount) throws IOException{
        try{
            Document d = Jsoup.connect(url).get();
            Elements es = d.select("#page_content");
            Elements images = es.select("img");
            for(Element e : images){
                String imageLink = e.attr("abs:src");
                e.attr("src", imageLink.toLowerCase().replace("http://", "").replace('/', '_').replace('.','_').replace("_jpg", ".jpg"));
                //System.out.println(e.attr("src"));
                saveImage(imageLink, "Result\\" + e.attr("src"));
            }
            String fileName = url.toLowerCase().replace("http://", "").replace('/', '_').replace('.','_').replace("_html", ".html");
            PrintWriter writer = new PrintWriter("Result\\" + fileName, "UTF-8");
            writer.println("<!-- saved from url=" + url + "-->");
            writer.println(es.toString());
            writer.close();
            
        }
        catch(IOException ex)
        {
            retryCount--;
            if(retryCount < 1)
                System.out.println("Failed >> " + ex.toString() + ":" + url);
            else{
                System.out.println("Retry: " + url);
                grabPage(url, retryCount);
            }
            
            
        }
    }
    
}
