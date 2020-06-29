/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import java.util.List;
import javax.ejb.Local;
import org.foi.nwtis.lukkristi.ejb.eb.Grupe;

/**
 *
 * @author lukkristi
 */
@Local
public interface GrupeFacadeLocal {

    void create(Grupe grupe);

    void edit(Grupe grupe);

    void remove(Grupe grupe);

    Grupe find(Object id);

    List<Grupe> findAll();

    List<Grupe> findRange(int[] range);

    int count();
    
}
