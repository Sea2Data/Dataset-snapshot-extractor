/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.snapshotdownloader.Biotic;

/**
 * Marks exceptions already reported to user.
 * Main should handle this by exiting with non-zero code.
 * Avoids having exit calls in unit-tested methods
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RunException extends Exception {

    public RunException(String s) {
        super(s);
    }
    
}
