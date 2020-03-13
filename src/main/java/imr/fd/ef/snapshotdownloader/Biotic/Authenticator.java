/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.snapshotdownloader.Biotic;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Authentication provided through static methods to avoid being a member of serializable objects
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class Authenticator {
    
    protected static Map<String,String> tokens = new HashMap<>();
    
    /**
     * Sets token for Basic Authentication scheme to be used for given url pointing to a BioticV3 API
     * @param url
     * @param token 
     */
    public static void setToken(String url, String token){
        Authenticator.tokens.put(url, token);
    }
    
    /**
     * Gets token for Basic Authentication scheme to be used for given url pointing to a BioticV3 API
     * @param url
     * @return 
     */
    public static String getToken(String url){
        if (!Authenticator.tokens.containsKey(url)){
            throw new RuntimeException("token not provided for" + url);
        }
        try {
            return Base64.getEncoder().encodeToString(Authenticator.tokens.get(url).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Unsupported encoding");
        }
    }
    
    /**
     * Checks if token for given url is registered
     * @param url
     * @return 
     */
    public static boolean hasToken(String url){
        return Authenticator.tokens.containsKey(url);
    }

    /**
     * Provide dialog box for getting token for a url
     * @param url 
     */
    public static void prompt(String url) {
        String token= JOptionPane.showInputDialog("Provide Authentication token (<user>:<passwd>) for:" + url);
        Authenticator.tokens.put(url, token);
    }
    
}
