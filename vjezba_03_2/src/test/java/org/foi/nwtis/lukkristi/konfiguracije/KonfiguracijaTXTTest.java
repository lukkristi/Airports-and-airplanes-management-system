/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.konfiguracije;

import java.io.File;
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
public class KonfiguracijaTXTTest {
    
    private Konfiguracija kofiguracija = null;
    private Properties postavke = null;
    private String datoteka = "NWTiS_matnovak_test.txt";
    
    public KonfiguracijaTXTTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws NemaKonfiguracije, NeispravnaKonfiguracija {
        postavke = new Properties();
        postavke.setProperty("1", "prva");
        postavke.setProperty("2", "druga");
        postavke.setProperty("3", "treca");
        postavke.setProperty("4", "cetvrta");
        postavke.setProperty("5", "peta");
        postavke.setProperty("6", "šesta");
        
        kofiguracija = KonfiguracijaApstraktna.kreirajKonfiguraciju(datoteka);
    }
    
    @After
    public void tearDown() {
        kofiguracija.obrisiSvePostavke();
        postavke.clear();
        
        File f=new File(datoteka);
        if(f.exists())
            f.delete();
    }

    /**
     * Test of ucitajKonfiguraciju method, of class KonfiguracijaTXT.
     */
    @Test
    public void testUcitajKonfiguraciju() throws Exception {
        System.out.println("ucitajKonfiguraciju");
        
        File f=new File(datoteka);
        if(!f.exists())
           fail("Problem kod kreiranja datoteke!");
        
        kofiguracija.ucitajKonfiguraciju();
        
        assertEquals(new Properties(), this.kofiguracija.dajSvePostavke());
        
        this.kofiguracija.dodajKonfiguraciju(postavke);
        this.kofiguracija.spremiKonfiguraciju();
        this.kofiguracija.obrisiSvePostavke();
        
        assertTrue(this.kofiguracija instanceof KonfiguracijaTXT);
        assertEquals(new Properties(), this.kofiguracija.dajSvePostavke());
        
        kofiguracija.ucitajKonfiguraciju();
        assertEquals(this.postavke, this.kofiguracija.dajSvePostavke());
        assertArrayEquals(this.postavke.keySet().toArray(),this.kofiguracija.dajSvePostavke().keySet().toArray());
        assertArrayEquals(this.postavke.entrySet().toArray(),this.kofiguracija.dajSvePostavke().entrySet().toArray());
    }
    
    @Test(expected = NeispravnaKonfiguracija.class)
    public void testUcitajKonfiguracijuSaKrivomDatotekom() throws Exception {
        kofiguracija = KonfiguracijaApstraktna.kreirajKonfiguraciju("datoteka");
    }
    
    @Test
    public void testUcitajKonfiguracijuSaKrivomDatotekom2() {
        try {
            kofiguracija = KonfiguracijaApstraktna.kreirajKonfiguraciju("datoteka");
            fail("Nije se bacila iznimka!");
        } catch (NemaKonfiguracije ex) {
            fail("Kriva iznimka!");
        } catch (NeispravnaKonfiguracija ex) {
            System.out.println("Iznimka ispravna bačena!");
        }
        
    }
    
    /**
     * Test of ucitajKonfiguraciju method, of class KonfiguracijaTXT.
     */
    @Test
    @Ignore
    public void testUcitajKonfiguraciju_String() throws Exception {
        System.out.println("ucitajKonfiguraciju");
        String datoteka = "";
        KonfiguracijaTXT instance = null;
        instance.ucitajKonfiguraciju(datoteka);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of spremiKonfiguraciju method, of class KonfiguracijaTXT.
     */
    @Test
    @Ignore
    public void testSpremiKonfiguraciju() throws Exception {
        System.out.println("spremiKonfiguraciju");
        KonfiguracijaTXT instance = null;
        instance.spremiKonfiguraciju();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of spremiKonfiguraciju method, of class KonfiguracijaTXT.
     */
    @Test
    @Ignore
    public void testSpremiKonfiguraciju_String() throws Exception {
        System.out.println("spremiKonfiguraciju");
        String datoteka = "";
        KonfiguracijaTXT instance = null;
        instance.spremiKonfiguraciju(datoteka);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}