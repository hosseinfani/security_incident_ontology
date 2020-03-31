/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ryerson.rnet.ls3.annotation;

import ca.ryerson.rnet.ls3.si.SecurityIncident;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 *
 * @author Hossein Fani
 * <https://sites.google.com/site/hosseinfani at ls3.rnet.ryerson.ca>
 */
public class OwlIriFactory {
    private OwlIriFactory(){};
    public static String getIri(UUID id) throws ClassNotFoundException{
        return getPrefix(SecurityIncident.class).concat(id.toString());
    }
    
    public static String getIri(Field f, String owlName){
        String fieldName = f.getName();
        if(owlName != null){
            fieldName = owlName;
        }
        try{
            return f.getAnnotation(OwlIri.class).value().concat(fieldName);
        }
        catch(NullPointerException ex){
            return getPrefix(f.getDeclaringClass()).concat(fieldName);
        }
    }
    
    public static String getIri(Class c){
        return getPrefix(c).concat(c.getSimpleName());
    }
    
    public static String getIri(Class c, String fieldName){
        return getIri(getField(c, fieldName), null);
    }
    public static String getIri(Class c, String fieldName, String owlName){
        return getIri(getField(c, fieldName), owlName);
    }
    
    public static String getPrefix(Class c){
        Package p = Package.getPackage(c.getPackage().getName());
        return p.getAnnotation(OwlIri.class).value();
    }
    
    private static Field getField(Class c, String fieldName) {
        //List<Field> result = new ArrayList<>();
        Field result = null;
        Class i = c;
        while (i != null && i != Object.class) {
            //result.addAll(Arrays.asList(i.getDeclaredFields()));
            try
            {
                return i.getDeclaredField(fieldName);
            }
            catch(NoSuchFieldException ex){
                i = i.getSuperclass();
            }
            
        }
        return result;
    }
    
}
