/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.lukkristi.ejb.eb.Myairports;

/**
 *
 * @author lukkristi
 */
@Stateless
public class MyairportsFacade extends AbstractFacade<Myairports> implements MyairportsFacadeLocal {

    @PersistenceContext(unitName = "NWTIS_PROJEKT_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyairportsFacade() {
        super(Myairports.class);
    }
    
}
