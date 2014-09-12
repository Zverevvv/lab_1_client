package sample;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book implements Serializable{
    private String name, publisher;
    private int id, date, pages;
    private boolean smooth;
    Book(int id, String name, String publisher, int date, int pages, boolean smooth){
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.date = date;
        this.pages = pages;
        this.smooth = smooth;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    
    public String getPublisher(){
        return publisher;
    }
    
    public void setDate(int date){
        this.date = date;
    }
    
    public int getDate(){
        return date;
    }
    
    public void setPages(int pages){
        this.pages = pages;
    }
    
    public int getPages(){
        return pages;
    }
    
    public void setSmooth(boolean smooth){
        this.smooth = smooth;
    }
    
    public boolean getSmooth(){
        return smooth;
    }
}