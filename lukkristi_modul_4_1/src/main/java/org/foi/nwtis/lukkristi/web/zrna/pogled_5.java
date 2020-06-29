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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.rest.klijent.ProjektREST_RS;
import org.foi.nwtis.lukkristi.rest.klijent.TestDrugiRS;
import org.foi.nwtis.lukkristi.ws.klijent.Aerodrom_WS;
import org.foi.nwtis.lukkristi.ws.serveri.Airports;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.OdgovorAerodrom;

/**
 *
 * @author lukkristi
 */
@Named(value = "pogled_5")
@SessionScoped
public class pogled_5 implements Serializable {

    @Getter
    @Setter
    String korisnik = "";
    @Getter
    @Setter
    String icao1 = "";
    @Getter
    @Setter
    String icao2 = "";
    @Getter
    @Setter
    String odKm = "";
    @Getter
    @Setter
    String doKm = "";
    @Getter
    @Setter
    String lozinka = "";
    @Getter
    @Setter
    double udaljenost;
    @Getter
    List<Aerodrom> aerodromiKorisnika = new ArrayList<>();
    @Getter
    List<Airports> aerodromiUIntervalu = new ArrayList<>();
    @Inject
    pogled_4 pogled;
    @Inject
    Prijava prijava;
    @Getter
    List<Airports> aerodromiKorisnika1 = new ArrayList<>();

    /**
     * Creates a new instance of pogled_5
     */
    public pogled_5() {
    }

    /**
     * dohvaca korisnikove aerodrome pozivanje servisa REST
     *
     * @return listu korisnikovih aerodroma
     */
    public String dajAerodromeKorisnika() {
        getSession();
        TestDrugiRS zadaca2_2RS = new TestDrugiRS(korisnik, lozinka);
        OdgovorAerodrom odgovor = zadaca2_2RS.dajAerodomeKorisnika(OdgovorAerodrom.class);
        aerodromiKorisnika = new LinkedList<>(Arrays.asList(odgovor.getOdgovor()));
        return "";
    }

    private void getSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        korisnik = (String) session.getAttribute("korisnik");
        lozinka = (String) session.getAttribute("lozinka");
    }

    /**
     * metoda dodaje aerodrom u korisnikovu kolekciju
     *
     * @param icao
     * @return
     */
    public String obrisiMojAerodrom() {
        TestDrugiRS zadaca2_2RS = new TestDrugiRS(korisnik, lozinka);
        Object odg = null;
        Response res = zadaca2_2RS.obrisiMojAerodom(odg, icao1);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(res.toString()));
        return "";
    }

    public String obrisiAvioneAerodroma() {
        TestDrugiRS zadaca2_2RS = new TestDrugiRS(korisnik, lozinka);
        Object odg = null;
        Response res = zadaca2_2RS.obrisiAvioneAerodoma(odg, icao1);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(res.toString()));
        return "";
    }

    public String vratiAerodromeURasponu() {
        Aerodrom_WS aerodromiKlijent = new Aerodrom_WS();
        int odKmint = Integer.parseInt(odKm);
        int doKmint = Integer.parseInt(doKm);
        aerodromiUIntervalu = aerodromiKlijent.vratiAerodromeUnutarGranica(korisnik, lozinka, icao1, odKmint, doKmint);
        return "";
    }

    public String izmjeriUdaljenost() {
        Aerodrom_WS aerodromiKlijent = new Aerodrom_WS();
        udaljenost = aerodromiKlijent.izmjeriUdaljenostAerodroma(korisnik, lozinka, icao1, icao2);
        return "";
    }

    public void localeChanged(AjaxBehaviorEvent e) {
        odKm = ((UIInput) e.getSource()).getValue().toString();

    }

    public void localeChanged2(AjaxBehaviorEvent e) {
        doKm = ((UIInput) e.getSource()).getValue().toString();
    }

    public String dohavtiAeridrime() {
        getSession();
        aerodromiKorisnika1 = prijava.getAerodromiKorisnika();
        return "";
    }

}
