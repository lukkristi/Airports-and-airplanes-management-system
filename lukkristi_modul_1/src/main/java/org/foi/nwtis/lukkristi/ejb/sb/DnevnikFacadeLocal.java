/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import java.util.List;
import javax.ejb.Local;
import org.foi.nwtis.lukkristi.ejb.eb.Dnevnik;

/**
 *
 * @author lukkristi
 */
@Local
public interface DnevnikFacadeLocal {

    void create(Dnevnik dnevnik);

    void edit(Dnevnik dnevnik);

    void remove(Dnevnik dnevnik);

    Dnevnik find(Object id);

    List<Dnevnik> findAll();

    List<Dnevnik> findRange(int[] range);

    int count();
    
}
