/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.snapshotdownloader.Biotic;

import java.net.URI;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BioticAPIException extends Exception {

    Integer responsecode;
    String responsemessage;
    URI uri;
    
    public BioticAPIException(Integer response, URI uri, String msg) {
        this.responsecode = response;
        this.uri = uri;
        this.responsemessage = msg;
    }
    
    /**
     * Get API repsonse code causing this exception.
     * Documentation on: https://confluence.imr.no/display/API/Biotic+V3+API+documentation
     * @return 
     */
    public Integer getResponse(){
        return this.responsecode;
    }
    
    public String getResponseMessage(){
        return this.responsemessage;
    }
    
    /**
     * Get URI that produced this response.
     * @return 
     */
    public URI getURI(){
        return this.uri;
    }
    
    @Override
    public String toString(){
        String stringrep = "" + this.responsecode + ", " + this.responsemessage + " ("+this.uri.toString()+")";
        System.out.println(stringrep);
        return stringrep;
    }
}
