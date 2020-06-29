/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.lukkristi.ejb.eb.KorisniciGrupe;

/**
 *
 * @author lukkristi
 */
@Stateless
public class KorisniciGrupeFacade extends AbstractFacade<KorisniciGrupe> implements KorisniciGrupeFacadeLocal {

    @PersistenceContext(unitName = "NWTIS_PROJEKT_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KorisniciGrupeFacade() {
        super(KorisniciGrupe.class);
    }
    
}
