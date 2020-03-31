package ca.ryerson.rnet.ls3.si.nnl;

import ca.ryerson.rnet.ls3.si.Assault;
import ca.ryerson.rnet.ls3.si.AssaultWithWeapon;
import ca.ryerson.rnet.ls3.si.Color;
import ca.ryerson.rnet.ls3.si.CommunityMember;
import ca.ryerson.rnet.ls3.si.IndecentExposure;
import ca.ryerson.rnet.ls3.si.ProximityAdverb;
import ca.ryerson.rnet.ls3.si.Robbery;
import ca.ryerson.rnet.ls3.si.SecurityIncident;
import ca.ryerson.rnet.ls3.si.SecurityService;
import ca.ryerson.rnet.ls3.si.SexualAssault;
import ca.ryerson.rnet.ls3.si.Subject;
import ca.ryerson.rnet.ls3.si.Victim;
import ca.ryerson.rnet.ls3.si.Voyeurism;
import ca.ryerson.rnet.ls3.si.swl.ObjectTripleMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.DayOfWeek;
import java.util.EnumSet;
import org.jsoup.select.Elements;
import org.w3.www.geo.Point;
import org.w3.www.owl.Thing;
import org.w3.www.time.Instant;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hossein Fani
 * <https://sites.google.com/site/hosseinfani at ls3.rnet.ryerson.ca>
 */
public class NameEntityExtractor {
    private NameEntityExtractor(){};
    public static SecurityIncident populateObjectFromText(Elements pageElements) throws MalformedURLException{
        
        Instant it = new Instant(2014, 1+ (int)(Math.random()*10), 1+ (int)(Math.random()*10), 0, 30, 0, DayOfWeek.WEDNESDAY, ProximityAdverb.Approximitaly);
        Instant ir = new Instant(2014, 1+ (int)(Math.random()*10), 1+ (int)(Math.random()*10), 21, 55,0, DayOfWeek.WEDNESDAY, ProximityAdverb.Approximitaly);      
        Thing t = new Thing("1st floor Atrium area, near to the west entrance");
        
        Point p = new Point(43.659623 + Math.random() * 0.01 * (Math.random() < 0.5 ? +1 : -1), -79.377972 + Math.random() * 0.01 * (Math.random() < 0.5 ? +1 : -1), "325 Church Street, Toronto, ON, CA.");
        t = new Thing("The victim felt the subject grasp her poncho from behind, which she was wearing at the time.  The victim then turned around to discover that the subject had bitten and had her poncho in his mouth.");
        SecurityService s = new SecurityService("Ryerson University Security & Emergency Services", null, null);
        Victim v = new Victim(null, null, null, "Female", null, null, null,null);
        v.getTypes().add(CommunityMember.class);
        
        Subject sj = new Subject(
                null, 
                ProximityAdverb.Approximitaly, 
                "Male", 
                "a navy blue Ryerson hooded sweatshirt/coat with gold embroidery",
                null,
                1.75,
                80.0,
                null,
                "The subject then released the victim’s poncho, left the area though the west entrance, and was last seen walking southbound on the east sidewalk of Church Street",
                EnumSet.of(Color.Blonde, Color.Black)
                );
        sj.getAge().add(20);
        sj.getAge().add(25);
        sj.getDescriptions().add("Light complexion");
        sj.getDescriptions().add("Medium-length hair");
        sj.getImageUrls().add(new URL("http://www.ryerson.ca/content/dam/irm/images/security/Bulletins/1.jpg"));
        sj.getImageUrls().add(new URL("http://www.ryerson.ca/content/dam/irm/images/security/Bulletins/2.jpg"));
        
        SecurityIncident a = null;
        
        double i = Math.random();
        if(i > 0.9){
            a = new AssaultWithWeapon(it, ir, t);
        }
        else if(i > 0.8){
            a = new Assault(it, ir);
        }
        else if(i > 0.7){
            a = new SexualAssault(it, ir);
        }
        else if(i > 0.4){
            a = new Robbery(it, ir);
        }
        else if(i > 0.2){
            a = new IndecentExposure(it, ir);
        }
        else {
            a = new Voyeurism(it, ir);
        }
        
        a.getPlace().add(t);
        a.getPlace().add(p);
        a.getProduct().add(t);
        a.getAgents().add(s);
        a.getAgents().add(s);
        a.getAgents().add(v);
        
        return a;
    } 
    public static void save(Elements pageElements) throws MalformedURLException, ClassNotFoundException, IOException{
        SecurityIncident si = populateObjectFromText(pageElements);
        ObjectTripleMapper.save(si);
    }
}
