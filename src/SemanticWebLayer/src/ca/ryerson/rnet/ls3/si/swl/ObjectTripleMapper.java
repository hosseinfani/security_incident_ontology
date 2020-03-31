/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ryerson.rnet.ls3.si.swl;

import ca.ryerson.rnet.ls3.si.SecurityIncident;
import ca.ryerson.rnet.ls3.si.gal.TriplesManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import java.io.IOException;

/**
 *
 * @author Hossein Fani
 * <https://sites.google.com/site/hosseinfani at ls3.rnet.ryerson.ca>
 */
public class ObjectTripleMapper {
    private ObjectTripleMapper(){};
    
    public static Model populateModelFromObject(SecurityIncident si) throws ClassNotFoundException {
        return si.getRdfResource().getModel();
    }
    public static OntModel getSecurityIncidentOntology() throws IOException{
        return SecurityIncidentOntology.getInstance().ontology;
    }
    public static void save(SecurityIncident si) throws ClassNotFoundException, IOException{
        Model m = populateModelFromObject(si);
        //getSecurityIncidentOntology();
        new TriplesManager().save(m);
    }
}
