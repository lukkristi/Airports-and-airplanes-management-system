/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ws.serveri;

import static java.lang.Math.log;
import static java.rmi.server.LogStream.log;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import javax.validation.ConstraintViolationException;
import org.foi.nwtis.lukkristi.ejb.eb.Airports;
import org.foi.nwtis.lukkristi.ejb.eb.Dnevnik;
import org.foi.nwtis.lukkristi.ejb.sb.AirplanesFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.AirportsFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.DnevnikFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.KorisniciFacadeLocal;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;


/**
 *
 * @author lukkristi
 */
@WebService(serviceName = "AerodromWS")
public class AerodromWS {

    @Inject
    ServletContext context;

    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;

    @EJB
    AirportsFacadeLocal airportsFacadeLocal;

    @EJB
    AirplanesFacadeLocal airplanesFacadeLocal;

    @EJB
    DnevnikFacadeLocal dnevnikFacadeLocal;

    /**
     * This is a sample web service operation
     *
     * @param korIme
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "provjeriKorisnika")
    public Boolean provjeriKorisnika(@WebParam(name = "korIme") String korIme,
            @WebParam(name = "lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("SOAP- dajAerodromePoNazivu ");
        dnevnik.setKorisnik(korIme);
        dnevnik.setIpadresa("asd");
        dnevnik.setTrajanje(3);
        dnevnik.setVriejme(new Timestamp(System.currentTimeMillis()));
        try {
            dnevnikFacadeLocal.create(dnevnik);
        } catch (ConstraintViolationException e) {
            Logger.getLogger(AerodromWS.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("EROR "+ e.getConstraintViolations());
             System.err.println("EROR2 "+ e.getStackTrace());
        }
        return korisniciFacadeLocal.provjeriKorisnika(korIme, lozinka);
    }

    /**
     * Web service operation
     *
     * @param korIme
     * @param lozinka
     * @param naziv
     * @return
     */
    @WebMethod(operationName = "dajAerodromePoNazivu")
    public java.util.List<Airports> dajAerodromePoNazivu(@WebParam(name = "korIme") String korIme,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "naziv") String naziv) {
        List<Airports> aerodromi = airportsFacadeLocal.pretraziPoNazivuJPQL(korIme, lozinka, naziv);
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("SOAP- dajAerodromePoNazivu " + naziv);
        zapisiUDnevnik(dnevnik, korIme);
        if (aerodromi != null) {
            if (aerodromi.size() > 0) {
                return aerodromi;
            }
            return null;
        }
        return null;
    }

    /**
     * Web service operation
     *
     * @param korIme
     * @param lozinka
     * @param drzava
     * @return
     */
    @WebMethod(operationName = "dajAerodromePoDrzavi")
    public java.util.List<Airports> dajAerodromePoDrzavi(@WebParam(name = "korIme") String korIme,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "drzava") String drzava) {
        List<Airports> aerodromi = airportsFacadeLocal.pretraziPoDrzavi(korIme, lozinka, drzava);
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("SOAP- dajAerodromePoDrzavi " + drzava);
        zapisiUDnevnik(dnevnik, korIme);
        if (aerodromi != null) {
            if (aerodromi.size() > 0) {
                return aerodromi;
            }
        }
        return null;
    }

    /**
     * Web service operation
     *
     * @param korIme
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "dajAerodomeKorisnika")
    public java.util.List<Airports> dajAerodomeKorisnika(@WebParam(name = "korIme") String korIme,
            @WebParam(name = "lozinka") String lozinka) {
        List<Airports> aerodromi = airportsFacadeLocal.dajKorisnikoveAerodrome(korIme, lozinka);
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("SOAP- dajAerodomeKorisnika ");
        zapisiUDnevnik(dnevnik, korIme);
        if (aerodromi != null) {
            if (aerodromi.size() > 0) {
                return aerodromi;
            }
        }
        return null;
    }

    /**
     * Web service operation
     *
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @param doDrugi
     * @param od
     * @return
     */
    @WebMethod(operationName = "dajSveAvioneAerodromaOdDo")
    public java.util.List<AvionLeti> dajSveAvioneAerodromaOdDo(@WebParam(name = "korIme") String korIme,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ICAO") String ICAO,
            @WebParam(name = "od") long od, @WebParam(name = "doDrugi") long doDrugi) {
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("SOAP- dajSveAvioneAerodromaOdDo " + ICAO + " od:" + od + " do:" + doDrugi);
        zapisiUDnevnik(dnevnik, korIme);
        return airplanesFacadeLocal.dajSveAvioneAerodromaOdDo(korIme, lozinka, ICAO, od, doDrugi);
    }

    /**
     * Web service operation
     *
     * @param korIme
     * @param lozinka
     * @param ICAO24
     * @param od
     * @param doDatum
     * @return
     */
    @WebMethod(operationName = "dajSveAvioneOdDo")
    public java.util.List<AvionLeti> dajSveAvioneOdDo(@WebParam(name = "korIme") String korIme,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ICAO24") String ICAO24,
            @WebParam(name = "od") long od, @WebParam(name = "doDatum") long doDatum) {
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("SOAP- dajSveAvioneOdDo " + ICAO24 + " od:" + od + " do:" + doDatum);
        zapisiUDnevnik(dnevnik, korIme);
        return airplanesFacadeLocal.dajSveLetoveAvionaOdDo(korIme, lozinka, ICAO24, od, doDatum);
    }

    /**
     * Web service operation
     *
     * @param korIme
     * @param lozinka
     * @param ICAO_1
     * @param ICAO_2
     * @return
     */
    @WebMethod(operationName = "izmjeriUdaljenostAerodroma")
    public double izmjeriUdaljenostAerodroma(@WebParam(name = "korIme") String korIme, @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "ICAO_1") String ICAO_1, @WebParam(name = "ICAO_2") String ICAO_2) {
        if (!korisniciFacadeLocal.provjeriKorisnika(korIme, lozinka)) {
            return -1;
        }
        Lokacija podaciPolazisni = vratiLokacijuAerodroma(ICAO_1);
        Lokacija podaciOdredisni = vratiLokacijuAerodroma(ICAO_2);
        if (podaciPolazisni != null && podaciOdredisni != null) {
            double theta = Double.parseDouble(podaciPolazisni.getLongitude()) - Double.parseDouble(podaciOdredisni.getLongitude());
            double dist = Math.sin(deg2rad(Double.parseDouble(podaciPolazisni.getLatitude())))
                    * Math.sin(deg2rad(Double.parseDouble(podaciOdredisni.getLatitude())))
                    + Math.cos(deg2rad(Double.parseDouble(podaciPolazisni.getLatitude())))
                    * Math.cos(deg2rad(Double.parseDouble(podaciOdredisni.getLatitude()))) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;

            dist = dist * 1.609344; // u kilometrima

//        upisiDnevnik(korisnickoIme, "SOAP#1 - Preuzimanje udaljenosti izmedu dva aerodroma");
            Dnevnik dnevnik = new Dnevnik();
            dnevnik.setUrl("SOAP- izmjeri udaljenost aerodroma " + ICAO_1 + " " + ICAO_2);
            zapisiUDnevnik(dnevnik, korIme);
            return dist;

        }
        return -1;
    }

    public void zapisiUDnevnik(Dnevnik dnevnik, String korIme) {
        dnevnik.setKorisnik(korIme);
        dnevnik.setIpadresa("localhost:8084");
        dnevnik.setTrajanje(1);
        dnevnik.setVriejme(new Timestamp(System.currentTimeMillis()));
        dnevnikFacadeLocal.create(dnevnik);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private Lokacija vratiLokacijuAerodroma(String icao) {
        Lokacija lokacija = null;
        Airports airp = airportsFacadeLocal.find(icao);
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        String LiqKlijentToken = bpk.getKonfig().dajPostavku("LocationIQ.token");
        LIQKlijent lIQKlijent = new LIQKlijent(LiqKlijentToken);
        if (airp != null) {
            String[] koordinate = airp.getCoordinates().split(",\\s+");
            lokacija = new Lokacija();
            lokacija.setLatitude(koordinate[1]);
            lokacija.setLongitude(koordinate[0]);
        }

        return lokacija;
    }

    /**
     * Web service operation
     *
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @param odKm
     * @param doKm
     * @return
     */
    @WebMethod(operationName = "vratiAerodromeUnutarGranica")
    public java.util.List<Airports> vratiAerodromeUnutarGranica(@WebParam(name = "korIme") String korIme,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ICAO") String ICAO,
            @WebParam(name = "odKm") int odKm, @WebParam(name = "doKm") int doKm) {
        if (!korisniciFacadeLocal.provjeriKorisnika(korIme, lozinka)) {
            return null;
        }
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("SOAP- vratiAerodromeUnutarGranica " + ICAO + " od:" + odKm + " do:" + doKm);
        zapisiUDnevnik(dnevnik, korIme);
        List<Airports> aerodromi = new ArrayList<>();
        List<Airports> aerodromiRrasponu = new ArrayList<>();
        aerodromi = dajAerodomeKorisnika(korIme, lozinka);
        Airports airp = airportsFacadeLocal.find(ICAO);
        double udaljenost = 0;
        if (airp != null && aerodromi.size() > 0) {
            for (Airports a : aerodromi) {
                udaljenost = izmjeriUdaljenostAerodroma(korIme, lozinka, ICAO, a.getIdent());
                if (udaljenost >= odKm && udaljenost <= doKm) {
                    aerodromiRrasponu.add(a);
                }
            }

            return aerodromiRrasponu;

        }

        return null;
    }

    /**
     * Web service operation
     *
     * @param korIme
     * @param lozinka
     * @param icao
     * @return
     */
    @WebMethod(operationName = "obrisiMojAerodrom")
    public boolean obrisiMojAerodrom(@WebParam(name = "korIme") String korIme,
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao) {
        if (!korisniciFacadeLocal.provjeriKorisnika(korIme, lozinka)) {
            return false;
        }

        return false;
    }

}
