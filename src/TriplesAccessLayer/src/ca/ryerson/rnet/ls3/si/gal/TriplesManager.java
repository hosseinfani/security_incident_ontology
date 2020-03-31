/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ca.ryerson.rnet.ls3.si.gal;

import java.io.IOException;
import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author  Hossein Fani
 * <https://sites.google.com/site/hosseinfani at ls3.rnet.ryerson.ca>
 */
public class TriplesManager {
    private SecurityIncidentGraph dbSecurityIncident;
    
    public TriplesManager() throws IOException {
        this.dbSecurityIncident = SecurityIncidentGraph.getInstance();
    }
    
//    public Object AddDataProperty(Triple t){
//        String str = "INSERT INTO GRAPH <%s> { <%s> <%s> %s}";
//        str = String.format(str, t.getSubject(), t.getPredicate(), StringEscapeUtils.escapeJava(t.getObject().toString()));
//        VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(str, dbSecurityIncident.graph);
//        vur.exec();
//        return null;
//    }
    
    public Object addTriples(List<Triple> ts){
        Iterator iter = ts.iterator();
        dbSecurityIncident.graph.getTransactionHandler().begin();
        for ( ; iter.hasNext() ; ){
            dbSecurityIncident.graph.add((Triple) iter.next());
        }
        dbSecurityIncident.graph.getTransactionHandler().commit();
        return null;
    }
    public Object removeTriples(List<Triple> ts){
        dbSecurityIncident.graph.remove(ts);
        return null;
    }
    public Object containsTriple(Triple t){
        dbSecurityIncident.graph.contains(t);
        return null;
    }
    public List<Triple> findTriples(Node subject, Node predicate, Node object){
        List<Triple> result = new ArrayList</*Triple*/>();
        ExtendedIterator iter = dbSecurityIncident.graph.find(subject, predicate, object);
        for ( ; iter.hasNext() ; ){
            result.add((Triple) iter.next());
        }
        return result;
    }
    
    public void save(Model m){
        StmtIterator iter = m.listStatements();
        List<Triple> ts = new ArrayList<>();
        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement();  // get next statement
            Resource  subject   = stmt.getSubject();     // get the subject
            Property  predicate = stmt.getPredicate();   // get the predicate
            RDFNode   object    = stmt.getObject();      // get the object
            
            
            Node s = Node.createURI(subject.toString());
            Node p = Node.createURI(predicate.toString());
            Node o;
            if (object instanceof Resource) {
                o = Node.createURI(object.toString());
            } 
            else {
                // object is a literal
                o = Node.createLiteral(" \"" + object.toString().replace("^^", "\"^^"));
            }
            Triple t = new Triple(s, p, o);
            ts.add(t);
        }
        addTriples(ts);
    }
}
