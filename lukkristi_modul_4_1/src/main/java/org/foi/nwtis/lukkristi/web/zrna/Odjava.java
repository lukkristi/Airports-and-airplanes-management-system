/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.zrna;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

/**
 *
 * @author lukkristi
 */
@Named(value = "odjava")
@SessionScoped
public class Odjava implements Serializable {

    @Inject
    Prijava prijavaKorisnika;
    /**
     * Creates a new instance of Odjava
     */
    public Odjava() {
    }
    
    public void unistiSesiju(){
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if(sesija!=null){
            sesija.invalidate();            
             FacesContext context = FacesContext.getCurrentInstance();
            try {
                context.getExternalContext().redirect("prijava.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(Odjava.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
