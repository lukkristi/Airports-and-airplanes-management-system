/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.ejb.eb.Myairports;
import org.foi.nwtis.lukkristi.ws.klijent.Aerodrom_WS;
import org.foi.nwtis.lukkristi.ws.serveri.Airports;
import org.foi.nwtis.lukkristi.ws.serveri.AvionLeti;

/**
 *
 * @author lukkristi
 */
@Named(value = "pogled_4")
@SessionScoped
public class pogled_4 implements Serializable {

    @Inject
    Prijava prijava;
    @Getter
    @Setter
    String korisnik = "";
    @Getter
    @Setter
    String lozinka = "";
    @Getter
    List<Airports> aerodromiKorisnika = new ArrayList<>();
    @Getter
    List<AvionLeti> avioniAerodroma = new ArrayList<>();
    @Getter
    List<AvionLeti> letoviAviona = new ArrayList<>();
    @Getter
    @Setter
    boolean prikaz1 = false;
    @Getter
    @Setter
    boolean prikaz2 = false;
    @Getter
    @Setter
    String odDatum;

    @Getter
    @Setter
    String doDatum;

    @Getter
    @Setter
    String icaoOdabrani;

    @Getter
    @Setter
    String icao24Odabrani;

    /**
     * Creates a new instance of pogled_4
     */
    public pogled_4() {

    }

    /**
     * metoda poziva facade korisnika i dohvaca korisnikove aerodrome ali prethodno ako je popunjena ocisti ju te poziva metodu za
     * otkrivanje tablice
     *
     * @return
     */
    public String dohvatiAerodromeKorisnika() {
        getSession();
        if (!aerodromiKorisnika.isEmpty()) {
            aerodromiKorisnika.clear();
        }
        Aerodrom_WS aerodromiKlijent = new Aerodrom_WS();
        aerodromiKorisnika = aerodromiKlijent.dajAerodomeKorisnika(korisnik, lozinka);
        return "";
    }

    private void getSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        korisnik = (String) session.getAttribute("korisnik");
        lozinka = (String) session.getAttribute("lozinka");
    }

    /**
     * metoda poziva soap servis i dohvaca avione od aerodroma
     *
     * @return avioni vraca listu aviona odabranog icao
     */
    public String dajAvioneAerodroma() {

        avioniAerodroma.clear();
        letoviAviona.clear();
        Aerodrom_WS aerodromiKlijent = new Aerodrom_WS();
        SimpleDateFormat dfad = new SimpleDateFormat("dd.mm.yyyy HH:mm");
        try {
            Date datum1 = dfad.parse(odDatum);
            Date datum2 = dfad.parse(doDatum);
            long datumOd = datum1.getTime() / 1000;
            long datumDo = datum2.getTime() / 1000;
            avioniAerodroma = aerodromiKlijent.dajSveAvioneAerodromaOdDo(korisnik, lozinka, icaoOdabrani, datumOd, datumDo);
        } catch (ParseException ex) {
            Logger.getLogger(pogled_4.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Pogreska kog klase PregledAviona metoda dajAvione " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unesite datume!"));
        }
        return "";
    }

    /**
     * metoda poziva soap servis i dohvaca avione
     *
     * @return avioni vraca listu aviona odabranog icao
     */
    public String dajLetoveAviona(String icao24) {
        letoviAviona.clear();
        Aerodrom_WS aerodromiKlijent = new Aerodrom_WS();
        SimpleDateFormat dfad = new SimpleDateFormat("dd.mm.yyyy HH:mm");
        try {
            Date datum1 = dfad.parse(odDatum);
            Date datum2 = dfad.parse(doDatum);
            long datumOd = datum1.getTime() / 1000;
            long datumDo = datum2.getTime() / 1000;
            letoviAviona = aerodromiKlijent.dajSveAvioneOdDo(korisnik, lozinka, icao24, datumOd, datumDo);
        } catch (ParseException ex) {
            Logger.getLogger(pogled_4.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Pogreska kog klase PregledAviona metoda dajAvione " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unesite datume!"));
        }
        return "";
    }

    public void localeChanged(AjaxBehaviorEvent  e) {
        odDatum=((UIInput)e.getSource()).getValue().toString();       

    }
    public void localeChanged2(AjaxBehaviorEvent  e) {       
        doDatum=((UIInput)e.getSource()).getValue().toString();

    }

}
