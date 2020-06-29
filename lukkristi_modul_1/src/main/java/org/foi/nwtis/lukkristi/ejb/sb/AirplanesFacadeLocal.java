/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import java.util.List;
import javax.ejb.Local;
import org.foi.nwtis.lukkristi.ejb.eb.Airplanes;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 *
 * @author lukkristi
 */
@Local
public interface AirplanesFacadeLocal {

    void create(Airplanes airplanes);

    void edit(Airplanes airplanes);

    void remove(Airplanes airplanes);

    Airplanes find(Object id);

    List<Airplanes> findAll();

    List<Airplanes> findRange(int[] range);
    
    List<AvionLeti> dajSveAvioneAerodromaOdDo(String korIme, String lozinka, String ident, long od, long doDrugi);
    
    List<AvionLeti> dajSveLetoveAvionaOdDo(String korIme, String lozinka, String ICAO24, long od, long doDrugi);

    int count();
    
}
