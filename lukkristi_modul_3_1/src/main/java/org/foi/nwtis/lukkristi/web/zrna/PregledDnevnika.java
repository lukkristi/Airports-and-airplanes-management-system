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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.ejb.eb.Dnevnik;
import org.foi.nwtis.lukkristi.ejb.sb.DnevnikFacadeLocal;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author lukkristi
 */
@Named(value = "pregledDnevnika")
@SessionScoped
public class PregledDnevnika implements Serializable {

    @Inject
    PrijavaKorisnika prijavaKorisnika;
    @Inject
    ServletContext context;
    @EJB
    DnevnikFacadeLocal dnevnikFacadeLocal;
    private BP_Konfiguracija bpk;
    @Getter
    @Setter
    String korisnik = "";
    @Getter
    @Setter
    String lozinka = "";
    @Getter
    List<Dnevnik> dnevnik = new ArrayList<>();
    @Getter
    List<Dnevnik> mojDnevnik = new ArrayList<>();
    @Getter
    @Setter
    String brojRedova;

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
    }

    public String autentificiraj() throws IOException {
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (sesija == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("pogled_1.xhtml");
            return"";
        } else {
            if (sesija.getAttribute("korisnik") == null) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().redirect("pogled_1.xhtml");
                return"";
            }
        }
      
       return"";       
    }

    private void getSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        korisnik = (String) session.getAttribute("korisnik");
        lozinka = (String) session.getAttribute("lozinka");               
        brojRedova=prijavaKorisnika.getBpk().getKonfig().dajPostavku("brojLinija");
    }   
    public void dohavtiDnevnikKorisnika() {
        getSession();
        dnevnik = dnevnikFacadeLocal.findAll();
        if (korisnik != "") {
            for (Dnevnik d : dnevnik) {
                if (d.getKorisnik().equals(korisnik)) {
                    mojDnevnik.add(d);
                }
            }
        }

    }
}
