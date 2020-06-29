/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ws.klijent;

import java.util.List;
import org.foi.nwtis.lukkristi.ws.serveri.Airports;
import org.foi.nwtis.lukkristi.ws.serveri.AvionLeti;


/**
 *
 * @author lukkristi
 */
public class Aerodrom_WS {
    
     public List<Airports> dajAerodomeKorisnika(String korisnik, String lozinka){
         List<Airports> aerodromi = null;
         
         try { // Call Web Service Operation
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service service = new org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service();
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS port = service.getAerodromWSPort();            
           
             aerodromi = port.dajAerodomeKorisnika(korisnik, lozinka);            
         } catch (Exception ex) {
             System.out.println("GRESKA kod 3.1 dajAerodromeKorisnika");
             System.err.println("STA TE MUCI RODJO:   " + ex.getMessage());
             ex.printStackTrace();
         }

         return aerodromi;
     }
     
      public List<Airports> dajAerodomePoNazivu(String korisnik, String lozinka, String naziv){
         List<Airports> aerodromi = null;
         
         try { // Call Web Service Operation
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service service = new org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service();
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS port = service.getAerodromWSPort();            
             if(naziv==null)
                 naziv="";
             aerodromi = port.dajAerodromePoNazivu(korisnik, lozinka, naziv);            
         } catch (Exception ex) {
             System.out.println("GRESKA kod 3.1 dajAerodromePoNazivu");
             System.err.println("STA TE MUCI RODJO:   " + ex.getMessage());
             ex.printStackTrace();
         }

         return aerodromi;
     }
      
      public List<Airports> dajAerodomePoDrzavi(String korisnik, String lozinka, String drzava){
         List<Airports> aerodromi = null;
         
         try { // Call Web Service Operation
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service service = new org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service();
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS port = service.getAerodromWSPort();                        
             aerodromi = port.dajAerodromePoDrzavi(korisnik, lozinka, drzava);            
         } catch (Exception ex) {
             System.out.println("GRESKA kod 3.1 dajAerodromePoDrzavi");
             System.err.println("STA TE MUCI RODJO:   " + ex.getMessage());
             ex.printStackTrace();
         }
         return aerodromi;
     }
      
    public List<AvionLeti> dajSveAvioneAerodromaOdDo(String korisnik, String lozinka, String ident,long odDat, long doDat){
         List<AvionLeti> avioniAerodroma = null;
         
         try { // Call Web Service Operation
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service service = new org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service();
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS port = service.getAerodromWSPort();                        
             avioniAerodroma = port.dajSveAvioneAerodromaOdDo(korisnik, lozinka, ident, odDat, doDat);            
         } catch (Exception ex) {
             System.out.println("GRESKA kod 3.1 dajAerodromePoDrzavi");
             System.err.println("STA TE MUCI RODJO:   " + ex.getMessage());
             ex.printStackTrace();
         }
         return avioniAerodroma;
     }
     
    public List<AvionLeti> dajSveAvioneOdDo(String korisnik, String lozinka, String icao24,long odDat, long doDat){
         List<AvionLeti> avioniAerodroma = null;
         
         try { // Call Web Service Operation
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service service = new org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service();
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS port = service.getAerodromWSPort();                        
             avioniAerodroma = port.dajSveAvioneOdDo(korisnik, lozinka, icao24, odDat, doDat);            
         } catch (Exception ex) {
             System.out.println("GRESKA kod 3.1 dajAerodromePoDrzavi");
             System.err.println("STA TE MUCI RODJO:   " + ex.getMessage());
             ex.printStackTrace();
         }
         return avioniAerodroma;
     }
    
     public List<Airports> vratiAerodromeUnutarGranica(String korisnik, String lozinka, String icao, int odKm, int doKm){
         List<Airports> aerodromi = null;
         
         try { // Call Web Service Operation
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service service = new org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service();
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS port = service.getAerodromWSPort();                        
             aerodromi = port.vratiAerodromeUnutarGranica(korisnik, lozinka, icao, odKm, doKm);
         } catch (Exception ex) {
             System.out.println("GRESKA kod 3.1 dajAerodromePoDrzavi");
             System.err.println("STA TE MUCI RODJO:   " + ex.getMessage());
             ex.printStackTrace();
         }
         return aerodromi;
     }
     
     public double izmjeriUdaljenostAerodroma(String korisnik, String lozinka, String icao, String icao2){
         int udaljenost=1;
         double da=0;
          try { // Call Web Service Operation
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service service = new org.foi.nwtis.lukkristi.ws.serveri.AerodromWS_Service();
             org.foi.nwtis.lukkristi.ws.serveri.AerodromWS port = service.getAerodromWSPort();                        
             da = port.izmjeriUdaljenostAerodroma(korisnik, lozinka, icao2, icao2);
         } catch (Exception ex) {
             System.out.println("GRESKA kod 3.1 dajAerodromePoDrzavi");
             System.err.println("STA TE MUCI RODJO:   " + ex.getMessage());
             ex.printStackTrace();
         }
          return da;
     }
    
}
