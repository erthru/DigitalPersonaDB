package com.erthru.digitalpersonadb.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author supriantodjamalu
 */
public class DB {
    
    public static Connection con()  throws Exception{
        
        Connection conn = null;
        
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost/db_digitalpersona", "root", "");
        
        return conn;
        
    }
    
}
