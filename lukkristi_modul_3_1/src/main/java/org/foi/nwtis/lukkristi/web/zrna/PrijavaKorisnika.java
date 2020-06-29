/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.ejb.eb.Korisnici;
import org.foi.nwtis.lukkristi.ejb.sb.DnevnikFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.KorisniciFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.SingletonSB;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author lukkristi
 */
@Named(value = "prijavaKorisnika")
@SessionScoped
public class PrijavaKorisnika implements Serializable {

    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;

    @EJB
    SingletonSB sinletonSB;
    

    @EJB
    DnevnikFacadeLocal dnevnikFacadeLocal;
    
    @Getter
    @Setter
    String korisnik = "";
    @Getter
    @Setter
    String lozinka = "";
    @Getter
    List<Korisnici> korisnici = new ArrayList<>();
    @Getter
    String greska = "";
    @Getter
    @Setter
    boolean prijavljen = false;
    @Inject
    ServletContext context;
    @Getter
    @Setter
    BP_Konfiguracija bpk;
    @Getter
    List<HttpSession> listaSesija;

    /**
     * Creates a new instance of PrijavaKorisnika
     */
    public PrijavaKorisnika() {
    }

    /**
     * metoda koja prijavljuje korisnika
     *
     * @return
     */
    public String prijaviKorisnika() {
        HttpSession sesija1 = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (sesija1 != null) {            
            if (sesija1.getAttribute("korisnik")!=null && sesija1.getAttribute("lozinka")!=null) {               
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Vec ste prijavljeni"));
                return "";
            }
        }        
        if (provjeriKorisnika()) {

            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            sesija.setAttribute("korisnik", korisnik);
            sesija.setAttribute("lozinka", lozinka);
            ispisiKorisnike();
            prijavljen = true;
            sinletonSB.addToSession(sesija);
            bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
//            FacesContext context = FacesContext.getCurrentInstance();
//            context.getExternalContext().redirect("naslovnica.xhtml");
//            return"dodavanjeAerodroma.xhtml?faces-redirect=true";
            return "";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Neuspjela prijava"));
        return "";
    }

    public boolean provjeriKorisnika() {
        Korisnici kor = korisniciFacadeLocal.find(korisnik);
        if (kor != null) {
            if (kor.getLozinka().equals(lozinka)) {
                return true;
            }
        }
        return false;
    }

    public void ispisiKorisnike() {

        korisnici = korisniciFacadeLocal.findAll();

    }
}
