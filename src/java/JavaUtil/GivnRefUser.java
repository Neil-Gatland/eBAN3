/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaUtil;

import java.io.Serializable;

/**
 *
 * @author taitchison
 */
public class GivnRefUser implements Serializable {
    private String display;
    private String logonId;
    
    public GivnRefUser(String display, String logonId) {
        this.display = display;
        this.logonId = logonId;
    }
    
    public String getDisplay() {
        return display;
    }
    
    public String getLogonId() {
        return logonId;
    }
}
