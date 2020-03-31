/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.w3.www.time;

import ca.ryerson.rnet.ls3.annotation.OwlIriFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import java.time.DayOfWeek;
import org.w3.www.owl.Thing;

/**
 *
 * @author Hossein Fani
 * <https://sites.google.com/site/hosseinfani at ls3.rnet.ryerson.ca>
 */
public class DateTimeDescription extends Thing{
    
    public DateTimeDescription(int year, int month, int day, int hour, int minute, int second, DayOfWeek dayOfWeek){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.dayOfWeek = dayOfWeek;
    }
    private int year;
    public int getYear(){
        return year;
    }
    private int month;
    public int getMonth(){
        return month;
    }
    
    private int day;
    public int getDay(){
        return day;
    }
    
    private int hour;
    public int getHour(){
        return hour;
    }
    
    private int minute;
    public int getMinute(){
        return minute;
    }
    
    private int second;
    public int getSecond(){
        return second;
    }
    
    private DayOfWeek dayOfWeek;
    public DayOfWeek getDayOfWeek(){
        return dayOfWeek;
    }
    
    @Override
    public Resource getRdfResource() throws ClassNotFoundException {
        Resource r = super.getRdfResource();
        Property p = ResourceFactory.createProperty(OwlIriFactory.getIri(this.getClass(), "hour"));
        r.addLiteral(p, hour);
        p = ResourceFactory.createProperty(OwlIriFactory.getIri(this.getClass(), "minute"));
        r.addLiteral(p, minute);
        p = ResourceFactory.createProperty(OwlIriFactory.getIri(this.getClass(), "second"));
        r.addLiteral(p, second);
        p = ResourceFactory.createProperty(OwlIriFactory.getIri(this.getClass(), "year"));
        r.addLiteral(p, year);
        p = ResourceFactory.createProperty(OwlIriFactory.getIri(this.getClass(), "month"));
        r.addLiteral(p, month);
        p = ResourceFactory.createProperty(OwlIriFactory.getIri(this.getClass(), "day"));
        r.addLiteral(p, day);
        if(dayOfWeek != null){
            p = ResourceFactory.createProperty(OwlIriFactory.getIri(this.getClass(), "dayOfWeek"));
            r.addLiteral(p, dayOfWeek.toString());
        }
        
        return r;
    }
}
