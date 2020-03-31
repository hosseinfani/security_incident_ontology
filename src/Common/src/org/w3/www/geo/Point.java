package org.w3.www.geo;

import ca.ryerson.rnet.ls3.annotation.OwlIri;
import ca.ryerson.rnet.ls3.annotation.OwlIriFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
/**
 * 
 * <p>
 * Generated by Protege (http://protege.stanford.edu). <br>
 * Source Class: Point <br>
 * @version generated on Wed Dec 10 20:15:17 EST 2014 by Hossein Fani  <https://sites.google.com/site/hosseinfani at ls3.rnet.ryerson.ca>
 */

public class Point extends SpatialThing {
    
    public Point(double lat, double lng, String text){
        super(text);
        this.lat = lat;
        this.lng = lng;
    } 
    private double lat;
    public double getLat(){
        return lat;
    }
    private double lng;
    public double getLng(){
        return lng;
    }
    
    /* ***************************************************
     * Property http://www.w3.org/2003/01/geo/wgs84_pos#formattedGeo
     */
    @OwlIri
    private String formattedGeo; 
    public String getFormattedGeo(){
        return formattedGeo;
    }
    
    @Override
    public Resource getRdfResource() throws ClassNotFoundException{
        Resource r = super.getRdfResource();
        Property ltd = ResourceFactory.createProperty(OwlIriFactory.getIri(this.getClass(), "lat"));
        r.addLiteral(ltd, lat);
        Property lng = ResourceFactory.createProperty(OwlIriFactory.getIri(this.getClass(), "lng", "long"));
        r.addLiteral(lng, this.lng);
        if(formattedGeo != null){
            Property s = ResourceFactory.createProperty(OwlIriFactory.getIri(this.getClass(), "formattedGeo"));
            r.addProperty(s, formattedGeo);
        }
        return r;
    }
    
}
