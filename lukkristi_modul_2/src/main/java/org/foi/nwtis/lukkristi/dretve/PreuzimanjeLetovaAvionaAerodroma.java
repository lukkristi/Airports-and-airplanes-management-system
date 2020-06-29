/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.dretve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.foi.nwtis.lukkristi.ejb.eb.Airplanes;
import org.foi.nwtis.lukkristi.ejb.eb.Airports;
import org.foi.nwtis.lukkristi.ejb.eb.Myairports;
import org.foi.nwtis.lukkristi.ejb.eb.Myairportslog;
import org.foi.nwtis.lukkristi.ejb.eb.MyairportslogPK;
import org.foi.nwtis.lukkristi.ejb.sb.MyairportsFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.AirplanesFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.MyairportslogFacadeLocal;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 *
 * @author lukkristi
 */
public class PreuzimanjeLetovaAvionaAerodroma extends Thread {
    
    @Inject
    ServletContext context;
    
    MyairportsFacadeLocal myairportsFacadeLocal;
    
    
    AirplanesFacadeLocal airplanesFacadeLocal;
    
    
    MyairportslogFacadeLocal myairportslogFacadeLocal;
    

    private BP_Konfiguracija konf;
    int intervalCiklusa = 100 * 1000;
    Boolean preuzimanjeStatus;
    String username;
    String password;
    OSKlijent oSKlijent;
    int inicijalniPocetakIntervala;
    String pocetniDatum;
    String krajDatum;
    Long pocetniInterval;
    Long sljedeceInterval;
    int pauzaDretve;
    int trajanjeCiklusaDretve;   
    List<Myairports> korisnikoviAerodromi = new ArrayList<>();

    public PreuzimanjeLetovaAvionaAerodroma() {
    }

    public PreuzimanjeLetovaAvionaAerodroma(BP_Konfiguracija konf,MyairportsFacadeLocal myairportsFacadeLocal, 
            AirplanesFacadeLocal airplanesFacadeLocal,MyairportslogFacadeLocal myairportslogFacadeLocal) {
        this.konf = konf;
        this.myairportsFacadeLocal=myairportsFacadeLocal;
        this.myairportslogFacadeLocal=myairportslogFacadeLocal;
        this.airplanesFacadeLocal=airplanesFacadeLocal;
    }

    @Override
    public void interrupt() {
        preuzimanjeStatus = false;
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        int brojac = 0;
        while (preuzimanjeStatus) {
            System.out.println("BROJAC: " + brojac++);
            try {                
                korisnikoviAerodromi=myairportsFacadeLocal.findAll();
                java.util.Date myDate = new java.util.Date(pocetniInterval);
                java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
                for (Myairports aerodrom : korisnikoviAerodromi) {
                    if (!provjeriAerodromLog(aerodrom.getIdent().getIdent(),sqlDate)) {
                        List<AvionLeti> avioni = oSKlijent.getDepartures(aerodrom.getIdent().getIdent(), pocetniInterval/1000, sljedeceInterval/1000);
//                        List<AvionLeti> avioni = oSKlijent.getDepartures("EDDF", 1588521744, 1588694544);
                        dodajAvione(avioni,aerodrom.getIdent());
                        spremiUAerodromLog(aerodrom.getIdent().getIdent(),sqlDate,avioni.size());
                        Thread.sleep(pauzaDretve*1000);
                    }
                }
                pocetniInterval = sljedeceInterval;
                sljedeceInterval = pocetniInterval + (24 * 60 * trajanjeCiklusaDretve * 1000);
                if (provjeriTrenutniDatum(pocetniInterval)) {
                    Thread.sleep(24 * 60 * 60 * 1000);
                }
                //TODO üreuzimanje aerodroma i avione od aerodroma za pojedini dan 
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        username = konf.getOpenSkyNetworkKorisnik();
        password = konf.getOpenSkyNetworkLozinka();
        oSKlijent = new OSKlijent(username, password);
        //TODO preuzimanje podataka iz konfiguracije  i pridruzi ih atributima
        pocetniDatum = konf.getPreuzimanjePocetak();
        krajDatum = konf.getPreuzimanjeKraj();
        pauzaDretve = konf.getPreuzimanjePauza();
        trajanjeCiklusaDretve = konf.getPreuzimanjeCiklusa();        
//        airportDAO = AirportDAO.getInstanca(konf);
        preuzimanjeStatus= konf.getPreuzimanjeStatus();
        pretvoriDatumUMilisekunde();

        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * metoda koja pretvara datum u miliskeunde 
     */
    public void pretvoriDatumUMilisekunde() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date datum;
        try {
            datum = df.parse(pocetniDatum);
            pocetniInterval = datum.getTime();
            sljedeceInterval = pocetniInterval + (24 * 60 * trajanjeCiklusaDretve * 1000);
        } catch (ParseException ex) {
            Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * provjerava trenutni datum odnosno danasnji
     * @param datPocetni
     * @return
     */
    public boolean provjeriTrenutniDatum(long datPocetni) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
        Date trenutniDatum = new Date();
        Date datumOdDretva = new Date(datPocetni);
        return fmt.format(trenutniDatum).equals(fmt.format(datumOdDretva));
    }
    
    /**
     *
     * @param avioni
     * @param ident
     */
    public void dodajAvione(List<AvionLeti> avioni, Airports ident){
        for (AvionLeti avion : avioni) {
            if(avion.getEstArrivalAirport()!= null){
                Airplanes avionBP=kreirajAvion(avion,ident);
                airplanesFacadeLocal.create(avionBP);
            }
        }
    }
    
    /**
     *
     * @param avionleti
     * @param ident
     * @return
     */
    public Airplanes kreirajAvion(AvionLeti avionleti, Airports ident){
        Airplanes avion= new Airplanes();
        avion.setArrivalairportcandidatescount(avionleti.getArrivalAirportCandidatesCount());
        avion.setCallsign(avionleti.getCallsign());
        avion.setDepartureairportcandidatescount(avionleti.getDepartureAirportCandidatesCount());
        avion.setEstarrivalairport(avionleti.getEstArrivalAirport());
        avion.setEstarrivalairporthorizdistance(avionleti.getEstArrivalAirportHorizDistance());
        avion.setEstarrivalairportvertdistance(avionleti.getEstArrivalAirportVertDistance());
        avion.setEstdepartureairporthorizdistance(avionleti.getEstDepartureAirportHorizDistance());
        avion.setEstdepartureairportvertdistance(avionleti.getEstDepartureAirportVertDistance());
        avion.setFirstseen(avionleti.getFirstSeen());
        avion.setIcao24(avionleti.getIcao24());
        avion.setLastseen(avionleti.getLastSeen());
        Date d = new Date();
        avion.setStored(d); 
        avion.setEstdepartureairport(ident); 
        return avion;
    }
    
    /**
     *
     * @param ident
     * @param datum
     * @return
     */
    public boolean provjeriAerodromLog(String ident, Date datum){
        MyairportslogPK mpk= new MyairportslogPK(ident, datum);
        Myairportslog m= myairportslogFacadeLocal.find(mpk);        
        if(m!=null)
            return true;
        return false;
    }
    
    /**
     *
     * @param ident
     * @param datum
     * @param brojAviona
     */
    public void spremiUAerodromLog(String ident, Date datum, int brojAviona){
        Myairportslog m= new Myairportslog(ident, datum);
        Date d = new Date();
        m.setStored(d);
        m.setNumberofairplanes(brojAviona);
        myairportslogFacadeLocal.create(m);
    }

}
