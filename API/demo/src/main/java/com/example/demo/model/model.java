package com.example.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import demo.ConnString;

public class model {
	public static class Book {
		public Integer bookId;
		public String name;
		public String author;
    }

    public static class Author {
    	public Integer authorId;
    	public String name;
    } 
    
    public static class modelgrade {
    	public String name;
    	public int score;
    } 
        
}
