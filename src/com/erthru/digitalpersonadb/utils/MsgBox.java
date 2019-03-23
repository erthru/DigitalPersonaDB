/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erthru.digitalpersonadb.utils;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author supriantodjamalu
 */
public class MsgBox {
    
    public static void success(String msg){
        
        JOptionPane.showMessageDialog(null, msg, "Success", 0, new ImageIcon("./src/com/erthru/digitalpersonadb/assets/img_success.png"));
        
    }
    
    public static void error(String msg){
        
        JOptionPane.showMessageDialog(null, msg, "Error", 0, new ImageIcon("./src/com/erthru/digitalpersonadb/assets/img_error.png"));
        
    }
    
    public static void warning(String msg){
        
        JOptionPane.showMessageDialog(null, msg, "Warning", 0, new ImageIcon("./src/com/erthru/digitalpersonadb/assets/img_error.png"));
        
    }
    
    public static int confirm(String msg){
        
        return JOptionPane.showConfirmDialog(null, msg, msg, JOptionPane.YES_NO_OPTION, 0, new ImageIcon("./src/com/erthru/digitalpersonadb/assets/img_warning.png"));
        
    }
    
}
