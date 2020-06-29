/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.konfiguracije;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author lukkristi
 */
public class KonfiguracijaApstraktnaTest {
    
    private KonfiguracijaApstraktna instance;
    
   public KonfiguracijaApstraktnaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new KonfiguracijaApstraktnaImpl();
        instance.postavke = new Properties();
        instance.postavke.setProperty("1", "prva");
        instance.postavke.setProperty("2", "druga");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of dodajKonfiguraciju method, of class KonfiguracijaApstraktna.
     */
    @Test
    @Ignore
    public void testDodajKonfiguraciju() {
        System.out.println("dodajKonfiguraciju");
        Properties postavke = null;
        KonfiguracijaApstraktna instance = null;
        instance.dodajKonfiguraciju(postavke);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of kopirajKonfiguraciju method, of class KonfiguracijaApstraktna.
     */
    @Test
    @Ignore
    public void testKopirajKonfiguraciju() {
        System.out.println("kopirajKonfiguraciju");
        Properties postavke = null;
        KonfiguracijaApstraktna instance = null;
        instance.kopirajKonfiguraciju(postavke);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dajSvePostavke method, of class KonfiguracijaApstraktna.
     */
    @Test
    public void testDajSvePostavke() {
        System.out.println("dajSvePostavke");
        Properties expResult = instance.postavke;
        Properties result = instance.dajSvePostavke();
        assertEquals(expResult, result);
    }

    
      /**
     * Test of dajPostavku method, of class KonfiguracijaApstraktna.
     */
    @Test
    public void testDajPostavku() {
        System.out.println("dajPostavku");
        String postavka = "1";
        String expResult = instance.postavke.getProperty(postavka);
        String result = instance.dajPostavku(postavka);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of obrisiSvePostavke method, of class KonfiguracijaApstraktna.
     */
    @Test
    @Ignore
    public void testObrisiSvePostavke() {
        System.out.println("obrisiSvePostavke");
        KonfiguracijaApstraktna instance = null;
        boolean expResult = false;
        boolean result = instance.obrisiSvePostavke();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

  

    /**
     * Test of spremiPostavku method, of class KonfiguracijaApstraktna.
     */
    @Test
    @Ignore
    public void testSpremiPostavku() {
        System.out.println("spremiPostavku");
        String postavka = "";
        String vrijednost = "";
        KonfiguracijaApstraktna instance = null;
        boolean expResult = false;
        boolean result = instance.spremiPostavku(postavka, vrijednost);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of azurirajPostavku method, of class KonfiguracijaApstraktna.
     */
    @Test
    @Ignore
    public void testAzurirajPostavku() {
        System.out.println("azurirajPostavku");
        String postavka = "";
        String vrijednost = "";
        KonfiguracijaApstraktna instance = null;
        boolean expResult = false;
        boolean result = instance.azurirajPostavku(postavka, vrijednost);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of postojiPostavka method, of class KonfiguracijaApstraktna.
     */
    @Test
    @Ignore
    public void testPostojiPostavka() {
        System.out.println("postojiPostavka");
        String postavka = "";
        KonfiguracijaApstraktna instance = null;
        boolean expResult = false;
        boolean result = instance.postojiPostavka(postavka);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of obrisiPostavku method, of class KonfiguracijaApstraktna.
     */
    @Test
    @Ignore
    public void testObrisiPostavku() {
        System.out.println("obrisiPostavku");
        String postavka = "";
        KonfiguracijaApstraktna instance = null;
        boolean expResult = false;
        boolean result = instance.obrisiPostavku(postavka);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of kreirajKonfiguraciju method, of class KonfiguracijaApstraktna.
     */
    @Test
    @Ignore
    public void testKreirajKonfiguraciju() throws Exception {
        System.out.println("kreirajKonfiguraciju");
        String datoteka = "";
        Konfiguracija expResult = null;
        Konfiguracija result = KonfiguracijaApstraktna.kreirajKonfiguraciju(datoteka);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of preuzmiKonfiguraciju method, of class KonfiguracijaApstraktna.
     */
    @Test
    @Ignore
    public void testPreuzmiKonfiguraciju() throws Exception {
        System.out.println("preuzmiKonfiguraciju");
        String datoteka = "";
        Konfiguracija expResult = null;
        Konfiguracija result = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class KonfiguracijaApstraktnaImpl extends KonfiguracijaApstraktna {

        public KonfiguracijaApstraktnaImpl() {
            super("");
        }

        @Override
        public void ucitajKonfiguraciju() throws NemaKonfiguracije, NeispravnaKonfiguracija {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void ucitajKonfiguraciju(String datoteka) throws NemaKonfiguracije, NeispravnaKonfiguracija {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void spremiKonfiguraciju() throws NemaKonfiguracije, NeispravnaKonfiguracija {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void spremiKonfiguraciju(String datoteka) throws NemaKonfiguracije, NeispravnaKonfiguracija {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
}
