/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.lukkristi.ejb.eb.Korisnici;

/**
 *
 * @author lukkristi
 */
@Stateless
public class KorisniciFacade extends AbstractFacade<Korisnici> implements KorisniciFacadeLocal {

    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;
    
    @PersistenceContext(unitName = "NWTIS_PROJEKT_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KorisniciFacade() {
        super(Korisnici.class);
    }

   @Override
    public boolean provjeriKorisnika(String korIme, String lozinka) {
        Korisnici k= null;
        k = korisniciFacadeLocal.find(korIme);
        if(k!=null){
            if(k.getLozinka().equals(lozinka))
                return true;
        }
        return false;
        
    }
    
}
