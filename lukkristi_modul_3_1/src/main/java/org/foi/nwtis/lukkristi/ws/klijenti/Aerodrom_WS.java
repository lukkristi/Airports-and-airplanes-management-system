/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ws.klijenti;

import java.util.List;
import org.foi.nwtis.lukkristi.ws.serveri.Airports;


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
     
    
}
