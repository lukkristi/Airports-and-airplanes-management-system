/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import java.util.List;
import javax.ejb.Local;
import org.foi.nwtis.lukkristi.ejb.eb.Airports;

/**
 *
 * @author lukkristi
 */
@Local
public interface AirportsFacadeLocal {

    void create(Airports airports);

    void edit(Airports airports);

    void remove(Airports airports);

    Airports find(Object id);

    List<Airports> findAll();

    List<Airports> findRange(int[] range);
    
    List<Airports> pretraziPoNazivuJPQL(String korIme, String lozinka, String naziv);
    
    List<Airports> pretraziPoDrzavi(String korIme, String lozinka, String drzava);
    
    List<Airports> dajKorisnikoveAerodrome(String korIme, String lozinka);

    int count();
    
}
