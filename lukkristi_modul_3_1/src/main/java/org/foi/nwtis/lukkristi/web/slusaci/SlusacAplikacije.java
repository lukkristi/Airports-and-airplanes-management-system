/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.lukkristi.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.lukkristi.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;

/**
 * Web application lifecycle listener.
 *
 * @author lukkristi
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String datoteke = sc.getInitParameter("konfiguracija");
        String putanja = sc.getRealPath("WEB-INF") + File.separator + datoteke;
        try {
            BP_Konfiguracija konf = new BP_Konfiguracija(putanja);
            sc.setAttribute("BP_Konfig", konf);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Aplikacija 3 je pokrenuta!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {        
        System.out.println("Aplikacija 3 je  zaustavljena!");
    }
}
