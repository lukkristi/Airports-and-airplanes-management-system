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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.foi.nwtis.lukkristi.ejb.eb.Airplanes;
import org.foi.nwtis.lukkristi.ejb.eb.Airplanes_;
import org.foi.nwtis.lukkristi.ejb.eb.Airports_;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 *
 * @author lukkristi
 */
@Stateless
public class AirplanesFacade extends AbstractFacade<Airplanes> implements AirplanesFacadeLocal {

    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;
    
    @PersistenceContext(unitName = "NWTIS_PROJEKT_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AirplanesFacade() {
        super(Airplanes.class);
    }

    @Override
    public List<AvionLeti> dajSveAvioneAerodromaOdDo(String korIme, String lozinka, String ident, long od, long doDrugi) {
        if(!korisniciFacadeLocal.provjeriKorisnika(korIme, lozinka)){
            return null;
        }
        List<Airplanes> avioni= new ArrayList<>();
        List<AvionLeti> avioniLete=new ArrayList<>();
//        avioni= getEntityManager().createQuery("SELECT a FROM Airplanes a WHERE a.firstseen>=:od AND a.lastseen<=:doDrugi "
//                + "AND a.estdepartureairport.ident=:ident")               
//                .setParameter("od", od)
//                .setParameter("doDrugi", doDrugi)
//                .setParameter("ident", ident)
//                .getResultList();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();       
        Root<Airplanes> sviLetovi = cq.from(Airplanes.class);
        cq.select(sviLetovi);
        List<Predicate> uvjeti = new ArrayList<>();
        uvjeti.add(cb.equal(sviLetovi.get(Airplanes_.estdepartureairport).get(Airports_.ident), ident));
        uvjeti.add(cb.greaterThanOrEqualTo(sviLetovi.get(Airplanes_.firstseen), (int)od));
        uvjeti.add(cb.lessThanOrEqualTo(sviLetovi.get(Airplanes_.lastseen), (int)doDrugi));
        cq.where(uvjeti.toArray(new Predicate[]{}));
        Query q = em.createQuery(cq);
        avioni = q.getResultList();       
        if(avioni.size()>0){
            for(Airplanes av: avioni){
                AvionLeti a = new AvionLeti(av.getIcao24(),av.getFirstseen(), av.getEstdepartureairport().getIdent(), av.getLastseen(), av.getEstarrivalairport(), 
                        av.getCallsign(), av.getEstdepartureairporthorizdistance(), av.getEstarrivalairportvertdistance(), 
                        av.getEstarrivalairporthorizdistance(), av.getEstdepartureairportvertdistance(), 
                        av.getDepartureairportcandidatescount(), av.getArrivalairportcandidatescount());
                avioniLete.add(a);
            }
            return avioniLete;
        }
        return null;
    }

    @Override
    public List<AvionLeti> dajSveLetoveAvionaOdDo(String korIme, String lozinka, String ICAO24, long od, long doDrugi) {
        if(!korisniciFacadeLocal.provjeriKorisnika(korIme, lozinka)){
            return null;
        }
        List<Airplanes> avioni= new ArrayList<>();
        List<AvionLeti> avioniLete=new ArrayList<>();
//        avioni= getEntityManager().createQuery("SELECT a FROM Airplanes a WHERE a.firstseen>=:od AND a.lastseen<=:doDrugi AND a.icao24=:ICAO24")               
//                .setParameter("od", od)
//                .setParameter("doDrugi", doDrugi)
//                .setParameter("ICAO24", ICAO24)
//                .getResultList();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();       
        Root<Airplanes> sviLetovi = cq.from(Airplanes.class);
        cq.select(sviLetovi);
        List<Predicate> uvjeti = new ArrayList<>();
        uvjeti.add(cb.equal(sviLetovi.get(Airplanes_.icao24), ICAO24));
        uvjeti.add(cb.greaterThanOrEqualTo(sviLetovi.get(Airplanes_.firstseen), (int)od));
        uvjeti.add(cb.lessThanOrEqualTo(sviLetovi.get(Airplanes_.lastseen), (int)doDrugi));
        cq.where(uvjeti.toArray(new Predicate[]{}));
        Query q = em.createQuery(cq);
        avioni = q.getResultList();
        if(avioni.size()>0){
            for(Airplanes av: avioni){
                AvionLeti a = new AvionLeti(av.getIcao24(),av.getFirstseen(), av.getEstdepartureairport().getIdent(), av.getLastseen(), av.getEstarrivalairport(), 
                        av.getCallsign(), av.getEstdepartureairporthorizdistance(), av.getEstarrivalairportvertdistance(), 
                        av.getEstarrivalairporthorizdistance(), av.getEstdepartureairportvertdistance(), 
                        av.getDepartureairportcandidatescount(), av.getArrivalairportcandidatescount());
                avioniLete.add(a);
            }
            return avioniLete;
        }
        return null;
    }
    
}
