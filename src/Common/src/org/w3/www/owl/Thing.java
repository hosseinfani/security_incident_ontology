/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.w3.www.owl;

import ca.ryerson.rnet.ls3.annotation.OwlIri;
import ca.ryerson.rnet.ls3.annotation.OwlIriFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Hossein Fani
 * <https://sites.google.com/site/hosseinfani at ls3.rnet.ryerson.ca>
 */
public class Thing {
    public Thing(){
        id = UUID.randomUUID();
        types.add(this.getClass());
    }
    public Thing(Object label){
        this();
        this.label = label;
    }
    
    @OwlIri
    private final UUID id;
    public UUID getId(){
        return id;
    }
    
    @OwlIri
    private Object label;
    public Object getLabel(){
        return label;
    }

    private List<Class> types = new ArrayList<>();
    public List<Class> getTypes(){
        return types;
    }
    
    public Resource getRdfResource() throws ClassNotFoundException {
        Model m = ModelFactory.createDefaultModel();
        Resource r = m.createResource(OwlIriFactory.getIri(this.id));
        for(Class c : types){
            Resource o = m.createResource(OwlIriFactory.getIri(c));
            r.addProperty(RDF.type, o);
        }
        r.addProperty(RDF.type, OWL2.NamedIndividual);
        if(label != null){
            Property p = ResourceFactory.createProperty(OwlIriFactory.getIri(this.getClass(), "label"));
            r.addLiteral(p, m.createTypedLiteral(label));
        }
        return r;
        
    }
}
