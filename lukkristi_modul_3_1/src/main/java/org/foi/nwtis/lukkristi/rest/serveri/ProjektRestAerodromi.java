/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.rest.serveri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.foi.nwtis.lukkristi.ejb.eb.Dnevnik;
import org.foi.nwtis.lukkristi.ejb.eb.Korisnici;
import org.foi.nwtis.lukkristi.ejb.eb.Myairports;
import org.foi.nwtis.lukkristi.ejb.sb.AirplanesFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.AirportsFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.DnevnikFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.KorisniciFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.MyairportsFacadeLocal;
import org.foi.nwtis.lukkristi.ws.klijenti.Aerodrom_WS;
import org.foi.nwtis.lukkristi.ws.serveri.Airports;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Odgovor;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 * REST Web Service
 *
 * @author lukkristi
 */
@Path("aerodromiProjekt")
@RequestScoped
public class ProjektRestAerodromi {

    @Context
    private UriInfo context;

    @EJB
    MyairportsFacadeLocal myairportsFacadeLocal;

    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;

    @EJB
    AirplanesFacadeLocal airplanesFacadeLocal;

    @EJB
    AirportsFacadeLocal airportsFacadeLocal;
    
    @EJB
    DnevnikFacadeLocal dnevnikFacadeLocal;

    /**
     * Creates a new instance of ProjektRestAerodromi
     */
    public ProjektRestAerodromi() {
    }

    /**
     * Retrieves representation of an instance of org.foi.nwtis.lukkristi.rest.serveri.ProjektRestAerodromi
     *
     * @return an instance of Response
     */
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodomeKorisnika(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {
        List<Airports> airports;
        List<Aerodrom> aerodromi = new ArrayList<>();
        Aerodrom_WS zadaca2_1WS = new Aerodrom_WS();
        airports = zadaca2_1WS.dajAerodomeKorisnika(korisnik, lozinka);       
        Odgovor odgovor = new Odgovor();
        if (airports != null && !airports.isEmpty()) {
            formatijaAerodrome(aerodromi,airports);
            odgovor.setStatus("10");
            odgovor.setPoruka("OK");
            odgovor.setOdgovor(aerodromi.toArray());
        } else {
            
            odgovor.setStatus("40");
            odgovor.setPoruka("Korisnik, nema vlastitih aerodroma, ili su pogresni podaci korisnika");
            odgovor.setOdgovor(aerodromi.toArray());
        }
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("REST- dajAerodomKorisnika");
        zapisiUDnevnik(dnevnik, korisnik);
        return Response
                .ok(odgovor)
                .status(200)
                .build();
    }

    @GET
    @Path("{icao}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodomKorisnika(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, @PathParam("icao") String icao) {
        List<Airports> aerodromi;
        List<Aerodrom> aerodromi2 = new ArrayList<>();
        Aerodrom_WS zadaca2_1WS = new Aerodrom_WS();
        aerodromi = zadaca2_1WS.dajAerodomeKorisnika(korisnik, lozinka);
        Odgovor odgovor = new Odgovor();
        Aerodrom mojAero = vratiAerodrom(aerodromi, icao);
        if (mojAero != null && !aerodromi.isEmpty()) {
            aerodromi2.add(mojAero);
            odgovor.setStatus("10");
            odgovor.setPoruka("OK");
            odgovor.setOdgovor(aerodromi2.toArray());
        } else {
            aerodromi = new ArrayList<>();
            odgovor.setStatus("40");
            odgovor.setPoruka("Korisnik, nema trazeni icao u vlastitim aerodromima ili su pogresni podaci korisnika");
            odgovor.setOdgovor(aerodromi.toArray());
        }
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("REST- dajAerodomKorisnika");
        zapisiUDnevnik(dnevnik, korisnik);
        return Response
                .ok(odgovor)
                .status(200)
                .build();
    }

    public Aerodrom vratiAerodromRizniceNwtis(Airports a) {
        Aerodrom pravi = new Aerodrom();
        pravi.setDrzava(a.getIsoCountry());
        pravi.setIcao(a.getIdent());
        pravi.setNaziv(a.getName());
        String[] lonILat = a.getCoordinates().split(",");
        Lokacija l = new Lokacija();
        l.postavi(lonILat[0].trim(), lonILat[1].trim());
        pravi.setLokacija(l);
        return pravi;
    }

    public Aerodrom vratiAerodrom(List<Airports> aerodromi, String icao) {
        if (aerodromi != null) {
            for (Airports a : aerodromi) {
                if (a.getIdent().equals(icao)) {                   
                    return vratiAerodromRizniceNwtis(a);
                }
            }
        }
        return null;
    }

    @DELETE
    @Path("{icao}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response obrisiMojAerodom(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, @PathParam("icao") String icao) {
        Object[] lista = new Object[1];
        Odgovor odgovor = new Odgovor();
        if (provjeriKorisnika(korisnik, lozinka, odgovor, lista)) {
            return Response
                    .ok(odgovor)
                    .status(200)
                    .build();
        }
        Korisnici k = korisniciFacadeLocal.find(korisnik);
        org.foi.nwtis.lukkristi.ejb.eb.Airports a = airportsFacadeLocal.find(icao);
        if (a != null && obrisiAerodrom(k, a)) {
            odgovor.setStatus("10");
            odgovor.setPoruka("OK");
            odgovor.setOdgovor(lista);
        } else {
            odgovor.setStatus("40");
            odgovor.setPoruka("Korisnik nema " + icao + " aerodrom ili aerodrom sadrzi avione");
            odgovor.setOdgovor(lista);
        }
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("REST- obrisiMojAerodom");
        zapisiUDnevnik(dnevnik, korisnik);
        return Response
                .ok(odgovor)
                .status(200)
                .build();
    }

    public boolean obrisiAerodrom(Korisnici k, org.foi.nwtis.lukkristi.ejb.eb.Airports a) {
        for (Myairports my : k.getMyairportsList()) {
            if (my.getIdent().getIdent().equals(a.getIdent())) {
                if (a.getAirplanesList().isEmpty()) {
                    myairportsFacadeLocal.remove(my);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean provjeriKorisnika(String korisnik, String lozinka, Odgovor odgovor, Object[] lista) {
        if (!korisniciFacadeLocal.provjeriKorisnika(korisnik, lozinka)) {
            odgovor.setStatus("40");
            odgovor.setPoruka("pogresni podaci korisnika");
            odgovor.setOdgovor(lista);
            return true;
        }
        return false;
    }

    @DELETE
    @Path("{icao}/avioni")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response obrisiAvioneAerodoma(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, @PathParam("icao") String icao) {
        Object[] lista = new Object[1];
        Odgovor odgovor = new Odgovor();
        if (provjeriKorisnika(korisnik, lozinka, odgovor, lista)) {
            return Response
                    .ok(odgovor)
                    .status(200)
                    .build();
        }
        Korisnici k = korisniciFacadeLocal.find(korisnik);
        org.foi.nwtis.lukkristi.ejb.eb.Airports a = airportsFacadeLocal.find(icao);
        if (a != null && provjeriMojAerodrom(k, a)) {
            odgovor.setStatus("10");
            odgovor.setPoruka("OK");
            odgovor.setOdgovor(lista);
        } else {
            odgovor.setStatus("40");
            odgovor.setPoruka("Korisnik ne prati " + icao + " aerodrom");
            odgovor.setOdgovor(lista);
        }
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("REST- obrisiAvioneAerodoma");
        zapisiUDnevnik(dnevnik, korisnik);
        return Response
                .ok(odgovor)
                .status(200)
                .build();
    }

    public boolean provjeriMojAerodrom(Korisnici k, org.foi.nwtis.lukkristi.ejb.eb.Airports a) {
        
        for (Myairports my : k.getMyairportsList()) {
            if (my.getIdent().getIdent().equals(a.getIdent())) {
                if (!a.getAirplanesList().isEmpty()) {
                    for (org.foi.nwtis.lukkristi.ejb.eb.Airplanes avion : a.getAirplanesList()) {
                        airplanesFacadeLocal.remove(avion);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Path("/svi")
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodomeDrzavaNaziv(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, @QueryParam("naziv") String naziv,
            @QueryParam("drzava") String drzava) {
        List<Airports> aerodromi = null;
        boolean pogreska = true;
        Aerodrom_WS zadaca2_1WS = new Aerodrom_WS();
        Odgovor odgovor = new Odgovor();
        if (naziv == null && drzava == null) {
            aerodromi = zadaca2_1WS.dajAerodomePoNazivu(korisnik, lozinka, naziv);
            if (aerodromi == null || aerodromi.isEmpty()) {
                pogreska = false;
                odgovor.setPoruka("Korisnik je pogresan ili ne postoji pretraga sa nazivom " + naziv);
            }
        } else if (naziv == null && !drzava.isEmpty()) {
            aerodromi = zadaca2_1WS.dajAerodomePoDrzavi(korisnik, lozinka, drzava);
            if (aerodromi == null || aerodromi.isEmpty()) {
                pogreska = false;
                odgovor.setPoruka("Korisnik je pogresan ili ne postoji pretraga sa drzavom " + drzava);

            }
        } else if (drzava == null && !naziv.isEmpty()) {
            aerodromi = zadaca2_1WS.dajAerodomePoNazivu(korisnik, lozinka, naziv);
            if (aerodromi == null || aerodromi.isEmpty()) {
                pogreska = false;
                odgovor.setPoruka("Korisnik je pogresan ili ne postoji pretraga sa nazivom " + naziv);
            }
        }
        if (pogreska) {
            odgovor.setStatus("10");
            odgovor.setPoruka("OK");
            odgovor.setOdgovor(aerodromi.toArray());
        } else {
            aerodromi = new ArrayList<>();
            odgovor.setStatus("40");
            odgovor.setOdgovor(aerodromi.toArray());
        }
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setUrl("REST- dajAerodomeDrzavaNaziv");
        zapisiUDnevnik(dnevnik, korisnik);

        return Response
                .ok(odgovor)
                .status(200)
                .build();
    }

    public void zapisiUDnevnik(Dnevnik dnevnik, String korIme) {
        dnevnik.setKorisnik(korIme);
        dnevnik.setIpadresa("localhost:8084");
        dnevnik.setTrajanje(1);
        Date d = new Date();
        dnevnik.setVriejme(d);
        dnevnikFacadeLocal.create(dnevnik);
    }
    /**
     * PUT method for updating or creating an instance of ProjektRestAerodromi
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }

    private void formatijaAerodrome(List<Aerodrom> aerodromi, List<Airports> airports) {
        for(Airports a: airports){
            Aerodrom aeroNovi = vratiAerodromRizniceNwtis(a);
            aerodromi.add(aeroNovi);
        }
    }
}
