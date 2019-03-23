package com.erthru.digitalpersonadb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
        
        String config = "./config";
        
        File file = new File(config);
        
        FileReader fr = new FileReader(file);
        
        BufferedReader br = new BufferedReader(fr);
        
        String line;
        int index = 0;
        
        String server = null;
        String user = null;
        String password = null;
        String database = null;
        
        while ((line = br.readLine()) != null){
            if(index == 0){
                server = line.replace("server=", "").replace("'", "").replace(";", "");
            }else if(index == 1){
                user = line.replace("user=", "").replace("'", "").replace(";", "");
            }else if(index == 2){
                password = line.replace("password=", "").replace("'", "").replace(";", "");
            }else if(index == 3){
                database = line.replace("database_name=", "").replace("'", "").replace(";", "");
            }
            index++;
        }
                
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+database, user, password);
        
        return conn;
        
    }
    
}
