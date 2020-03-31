/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ryerson.rnet.ls3.si.gal;

import java.io.*;
import java.util.Properties;
import virtuoso.jena.driver.VirtGraph;

/**
 *
 * @author  Hossein Fani  
 * <https://sites.google.com/site/hosseinfani at ls3.rnet.ryerson.ca>
 */
/*internal*/class SecurityIncidentGraph {

    private String tripleStoreEndpoint;
    private String tripleStoreUsername;
    private String tripleStorePassword;
    private String graphName;
    /*internal*/VirtGraph graph;
    
    private SecurityIncidentGraph(){}
    
    private static SecurityIncidentGraph singletonInstance;
    public static SecurityIncidentGraph getInstance() throws IOException{
        if(singletonInstance == null ){
            loadTripleStoreConnectionSettings();
            singletonInstance.graph = new VirtGraph(singletonInstance.graphName, singletonInstance.tripleStoreEndpoint, singletonInstance.tripleStoreUsername, singletonInstance.tripleStorePassword);
        }
        return singletonInstance;
    }
    
    private static void loadTripleStoreConnectionSettings() throws FileNotFoundException, IOException {
        singletonInstance = new SecurityIncidentGraph();
        Properties tripleStoreConnectionSettings = new Properties();
        tripleStoreConnectionSettings.load(SecurityIncidentGraph.class.getResourceAsStream("configuration.properties"));
        singletonInstance.tripleStoreEndpoint = tripleStoreConnectionSettings.getProperty("TripleStoreEndpoint");
        singletonInstance.tripleStoreUsername = tripleStoreConnectionSettings.getProperty("TripleStoreUsername");
        singletonInstance.tripleStorePassword = tripleStoreConnectionSettings.getProperty("TripleStorePassword");
        singletonInstance.graphName           = tripleStoreConnectionSettings.getProperty("GraphName");
        
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
        singletonInstance.graph.close();
    }
    
    
    
}
