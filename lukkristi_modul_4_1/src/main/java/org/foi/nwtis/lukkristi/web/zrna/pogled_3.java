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
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.ejb.eb.Korisnici;
import org.foi.nwtis.lukkristi.ejb.eb.Myairports;
import org.foi.nwtis.lukkristi.ejb.sb.KorisniciFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.MyairportslogFacadeLocal;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.lukkristi.ws.klijent.Aerodrom_WS;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author lukkristi
 */
@Named(value = "pogled_3")
@SessionScoped
public class pogled_3 implements Serializable {

    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;
    @EJB
    MyairportslogFacadeLocal myairportslogFacadeLocal;
    @Getter
    @Setter
    String korisnik = "";
    @Getter
    @Setter
    String lozinka = "";
    @Getter
    List<Myairports> aerodromiKorisnika = new ArrayList<>();
    @Getter
    @Setter
    private String aerodromOdabrani;
    @Getter
    @Setter
    String drzava;

    @Getter
    @Setter
    String naziv;

    @Getter
    List<org.foi.nwtis.lukkristi.ws.serveri.Airports> aerodromi = new ArrayList<>();
    @Getter
    @Setter
    Myairports aerodrom;
    @Inject
    ServletContext context;
    @Getter
    @Setter
    private MeteoPodaci meteoPodaci;
    @Getter
    @Setter
    Lokacija lokacija;
    

    /**
     * Creates a new instance of pogled_3
     */
    public pogled_3() {

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
        Korisnici k = korisniciFacadeLocal.find(korisnik);
        aerodromiKorisnika.addAll(k.getMyairportsList());
        return "";
    }

    private void getSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        korisnik = (String) session.getAttribute("korisnik");
        lozinka = (String) session.getAttribute("lozinka");
    }
//    
//    /**
//     * Jos jedan pokusaj dohvacanja vlage ali zbog isti gresaka  kao prethodna metoda nije ostavljena u pogledu
//     * @param ident
//     * @return
//     */
//    public String DohvatiTemp(String ident){
//        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
//        MeteoPodaci meteoPodaci=meteorologLocal.dohvatiGeoIMeteoPodatke(ident,bpk);       
//        temperatura=meteoPodaci.getTemperatureValue().toString();
//        if(temperatura==null)
//             System.out.println("org.foi.nwtis.lukkristi.web.zrna.Aktivnost2.DohvatiVlagu()");
//        return temperatura;               
//    }

    /**
     * Metoda koja pziva REST servis i dohvaca aerodrome prema nazivu ili drzavi
     *
     * @param aerodrom
     * @return
     */
    public void DohvatiMeteoPodatke(Myairports aerodrom) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        String LiqKlijentToken = bpk.getKonfig().dajPostavku("LocationIQ.token");
        String OWMKlijentApi = bpk.getKonfig().dajPostavku("OpenWeatherMap.apikey");
        LIQKlijent lIQKlijent = new LIQKlijent(LiqKlijentToken);
        OWMKlijent oWMKlijent = new OWMKlijent(OWMKlijentApi);                
        lokacija = lIQKlijent.getGeoLocation(aerodrom.getIdent().getIdent());
        meteoPodaci = oWMKlijent.getRealTimeWeather(lokacija.getLatitude(), lokacija.getLongitude());

    }

    public void onRowSelect(SelectEvent event) {
        aerodrom = (Myairports) event.getObject();
        DohvatiMeteoPodatke(aerodrom);        
        RequestContext.getCurrentInstance().addCallbackParam("latitude", lokacija.getLatitude());
        RequestContext.getCurrentInstance().addCallbackParam("longitude", lokacija.getLongitude());
        RequestContext.getCurrentInstance().addCallbackParam("meteoPodaci", meteoPodaci);
    }

    public String dajAerodromeDrzavaNaziv() {
        Aerodrom_WS soap = new Aerodrom_WS();
        if (!aerodromi.isEmpty()) {
            aerodromi.clear();
        }
        if (drzava != null && !drzava.isEmpty()) {
            aerodromi = soap.dajAerodomePoDrzavi(korisnik, lozinka, drzava);
        } else if (naziv != null && !naziv.isEmpty()) {
            aerodromi = soap.dajAerodomePoNazivu(korisnik, lozinka, naziv);
        } else {
            naziv = "";
            aerodromi = soap.dajAerodomePoNazivu(korisnik, lozinka, naziv);

        }
        return "";
    }

}
