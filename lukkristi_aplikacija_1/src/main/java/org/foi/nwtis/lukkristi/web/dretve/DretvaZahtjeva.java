/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.dretve;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.dkermek.ws.serveri.Avion;
import org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika;

/**
 *
 * @author lukkristi
 */
public class DretvaZahtjeva extends Thread {

    private Socket socket = null;
    private boolean stop = false;
    private static int brPoruke = 0;
    private long vrijeme;

    public DretvaZahtjeva(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        zaprimanjeKomande();
    }
    
    
    /**
     * Metoda šalje odgovor korisniku
     *
     * @param line odgovor za korisnika
     * @throws IOException exception koji se može pojaviti prilikom slanja poruke na socket
     */
    /**
     * Metoda zaprima komandu od korisnika
     *
     * @return komandu primljnu od korisnika
     * @throws IOException exception koji se može pojaviti prilikom čitanja poruke sa socketa
     */
    public void zaprimanjeKomande() {
        BufferedWriter bw = null;
        String naredba = "";
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                buffer.append((char) znak);
            }
            naredba = buffer.toString();
            System.out.println("Dobivena naredba: " + naredba);
            vrijeme = System.currentTimeMillis();
            String odgovor = provijeriKomandu(naredba);
            os.write(odgovor.getBytes());
            os.flush();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DretvaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DretvaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(DretvaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
     /**
     * Postavlja socket
     *
     * @param socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    
     /**
     * Provjerava komandu i ako je dobra prosljeđuje naredbu metodi za odabir metoda za akciju
     *
     * @param komanda komanda zaprimljena od korisnika
     * @return odabrana akcija ili error
     */
    public String provijeriKomandu(String komanda) {
        String poruka = "";
        String regexPosluzitelj = "KORISNIK\\s+(\\w+);\\s+LOZINKA\\s+(\\w+);\\s+(PAUZA|DODAJ|RADI|KRAJ|STANJE);\\s*";
        Pattern pp = Pattern.compile(regexPosluzitelj);
        Matcher mp = pp.matcher(komanda);
        if (mp.matches()) {
            String korisnik = mp.group(1);
            String lozinka = mp.group(2);
            String naredba = mp.group(3);
            if (korisnik!="") {
                return odaberiAkcijuPosluzitelja(korisnik, naredba);
            } else {
                return "ERR 11;";
            }
        }
        String regexGrupa = "KORISNIK\\s+(\\w+);\\s+LOZINKA\\s+(\\w+);\\s+GRUPA\\s+(PRIJAVI|ODJAVI|AKTIVIRAJ|BLOKIRAJ|STANJE|GRUPA AERODROMI);\\s*";
        Pattern pg = Pattern.compile(regexGrupa);
        Matcher mg = pg.matcher(komanda);
        if (mg.matches()) {
            String korisnik = mg.group(1);
            String lozinka = mg.group(2);
            String naredba = mg.group(3);
            if (korisnik!="") {
                return odaberiAkcijuGrupe(korisnik, lozinka, naredba);
            } else {
                return "ERR 11;";
            }
        }
        String regexKorisnik = "KORISNIK\\s+(\\w+);\\s+LOZINKA\\s+(\\w+);";
        Pattern pk = Pattern.compile(regexKorisnik);
        Matcher mk = pk.matcher(komanda);
        if (mk.matches()) {
            String korisnik = mk.group(1);
            String lozinka = mk.group(2);
            if (korisnik!="") {
                
                return "OK 10;";
            } else {                
                return "ERR 11;";
            }
        } else {
            return null;
        }
    }
    
    /**
     * Odabir i pozivanje ozivanje metode za željenu akciju
     *
     * @param m Metcher za provjeru regex upitima i dohvaćanje grupa
     * @param poruka poruka proslijeđena od ranijih metoda
     * @return poruka poruka izvršene akcije
     * @throws NumberFormatException exception koji se može javiti prilikom parsiranja brojeva
     */
    public String odaberiAkcijuPosluzitelja(String korisnik, String naredba) {
        String poruka = "";
        switch (naredba) {
            case "PAUZA":
                poruka = "";
                break;
            case "RADI":
                poruka = "";
                break;           
            case "DODAJ":
                poruka = "";
                break;
            case "KRAJ":
                poruka = "";
                break;
            case "STANJE":
                poruka = "";
                break;
        }       
        return poruka;
    }
    
    public String odaberiAkcijuGrupe(String korisnik, String lozinka, String naredba) {
        String poruka = "";
        switch (naredba) {
            case "PRIJAVI":
                poruka = prijaviGrupu(korisnik, lozinka);
                break;
            case "ODJAVI":
                poruka = odjaviGrupu(korisnik, lozinka);
                break;
            case "AKTIVIRAJ":
                poruka = aktivirajGrupu(korisnik, lozinka);
                break;
            case "BLOKIRAJ":
                poruka = blokirajGrupu(korisnik, lozinka);
                break;
            case "STANJE":
                poruka = statusGrupe(korisnik, lozinka);
                break;

        }
        return poruka;
    }
    
    private String prijaviGrupu(String korisnik, String lozinka) {
        if (registrirajGrupu("lukkristi", "nwtis_DA")) {            
            return "OK 20;";
        } else {          
            return "ERR 20;";
        }
    }

    private String odjaviGrupu(String korisnik, String lozinka) {
        if (deregistrirajGrupu("lukkristi", "nwtis_DA")) {           
            return "OK 20;";
        } else {          
            return "ERR 21;";
        }
    }

    private String aktivirajGrupu(String korisnik, String lozinka) {
        StatusKorisnika statKorisnika = dajStatusGrupe("lukkristi", "nwtis_NE");
        if (statKorisnika == StatusKorisnika.NEPOSTOJI) {            
            return "ERR 21;";
        } else if (aktivirajGrupu_1("lukkristi", "nwtis_DA")) {           
            return "OK 20;";
        } else {
            
            return "ERR 22;";
        }
    }

    private String blokirajGrupu(String korisnik, String lozinka) {
        StatusKorisnika statKorisnika = dajStatusGrupe("lukkristi", "nwtis_DA");
        if (statKorisnika == StatusKorisnika.NEPOSTOJI) {            
            return "ERR 21;";
        } else if (blokirajGrupu_1("lukkristi", "nwtis_DA")) {
            
            return "OK 20;";
        } else {
           
            return "ERR 23;";
        }
    }

    private String statusGrupe(String korisnik, String lozinka) {
        StatusKorisnika statKorisnika = dajStatusGrupe("lukkristi", "nwtis_DA");       
        String odgovor = "";
        if (statKorisnika == StatusKorisnika.NEPOSTOJI) {
            odgovor = "ERR 21;";
        } else if (statKorisnika == StatusKorisnika.AKTIVAN) {
            odgovor = "OK 21;";
        } else if (statKorisnika == StatusKorisnika.BLOKIRAN) {
            odgovor = "OK 22;";
        }
        return odgovor;
    }
    
    private static Boolean registrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {        
       
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();          
            java.lang.Boolean result = port.registrirajGrupu(korisnickoIme, korisnickaLozinka);                 
        
        return port.registrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    private static Boolean deregistrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        
      
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();         
            
        return port.deregistrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    private static Boolean aktivirajGrupu_1(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
               
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
           
            java.lang.Boolean result = port.aktivirajGrupu(korisnickoIme, korisnickaLozinka);
         

        return port.aktivirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    private static Boolean blokirajGrupu_1(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        
        
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
            

        return port.blokirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    private static StatusKorisnika dajStatusGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();           
        return port.dajStatusGrupe(korisnickoIme, korisnickaLozinka);
    }
    
    private Boolean  PostaviGrupeAerodromi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, List<Avion> aerodromi) {
        
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
            org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
            // TODO initialize WS operation arguments here
           
            boolean result = port.postaviAvioneGrupe(korisnickoIme, korisnickaLozinka, aerodromi);
          

        return result;
    }

}
