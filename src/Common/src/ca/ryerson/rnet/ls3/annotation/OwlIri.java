/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ryerson.rnet.ls3.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Hossein Fani
 * <https://sites.google.com/site/hosseinfani at ls3.rnet.ryerson.ca>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.FIELD})
public @interface OwlIri {
    public String value() default "http://ls3.rnet.ryerson.ca/ontologies/sio/";
}

