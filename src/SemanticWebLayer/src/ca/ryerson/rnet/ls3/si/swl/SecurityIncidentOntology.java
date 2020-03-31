/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ryerson.rnet.ls3.si.swl;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Hossein Fani
 * <https://sites.google.com/site/hosseinfani at ls3.rnet.ryerson.ca>
 */
/*internal*/class SecurityIncidentOntology {
    
    private String OntologyIri;
    /*internal*/OntModel ontology;
    
    private SecurityIncidentOntology(){}
    
    private static SecurityIncidentOntology singletonInstance;
    public static SecurityIncidentOntology getInstance() throws IOException{
        if(singletonInstance == null ){
            loadSecurityIncidentOntology();
        }
        return singletonInstance;
    }
    
    private static void loadSecurityIncidentOntology() throws FileNotFoundException, IOException {
        singletonInstance = new SecurityIncidentOntology();
        Properties ps = new Properties();
        ps.load(SecurityIncidentOntology.class.getResourceAsStream("configuration.properties"));
        singletonInstance.OntologyIri = ps.getProperty("OntologyIri");
        OntModel o = ModelFactory.createOntologyModel();
        FileManager.get().readModel(o, singletonInstance.OntologyIri);
        singletonInstance.ontology = o;
    }

}