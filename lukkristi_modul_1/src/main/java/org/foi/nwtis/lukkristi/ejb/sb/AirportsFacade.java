/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.foi.nwtis.lukkristi.ejb.eb.Airports;
import org.foi.nwtis.lukkristi.ejb.eb.Airports_;
import org.foi.nwtis.lukkristi.ejb.eb.Korisnici;
import org.foi.nwtis.lukkristi.ejb.eb.Myairports;

/**
 *
 * @author lukkristi
 */
@Stateless
public class AirportsFacade extends AbstractFacade<Airports> implements AirportsFacadeLocal {

    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;
    
    @PersistenceContext(unitName = "NWTIS_PROJEKT_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AirportsFacade() {
        super(Airports.class);
    }

   @Override
    public List<Airports> pretraziPoNazivuJPQL(String korIme, String lozinka, String naziv) {
        if(!korisniciFacadeLocal.provjeriKorisnika(korIme, lozinka)){
            return null;
        }
        String naziv2="%"+naziv+"%";
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Airports> aerodromi = cq.from(Airports.class);
        cq.select(aerodromi).where(cb.like(aerodromi.get(Airports_.name), naziv2));
        Query q = em.createQuery(cq);
        return q.getResultList();
    }

    @Override
    public List<Airports> pretraziPoDrzavi(String korIme, String lozinka, String drzava) {
        if(!korisniciFacadeLocal.provjeriKorisnika(korIme, lozinka)){
            return null;
        }        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Airports> aerodromi = cq.from(Airports.class);
        cq.select(aerodromi).where(cb.like(aerodromi.get(Airports_.isoCountry), drzava));
        Query q = em.createQuery(cq);
        return q.getResultList();
    }

    @Override
    public List<Airports> dajKorisnikoveAerodrome(String korIme, String lozinka) {
        if(!korisniciFacadeLocal.provjeriKorisnika(korIme, lozinka)){
            return null;
        }
        List<Airports> korisnikoviAerodromi= new ArrayList<>();
        Korisnici k = korisniciFacadeLocal.find(korIme);
        for(Myairports a: k.getMyairportsList()){
            korisnikoviAerodromi.add(a.getIdent());
        }
        return korisnikoviAerodromi;
    }
    
}
