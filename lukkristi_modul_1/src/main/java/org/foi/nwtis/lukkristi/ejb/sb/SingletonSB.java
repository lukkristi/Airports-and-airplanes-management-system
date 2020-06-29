/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.servlet.http.HttpSession;

/**
 *
 * @author lukkristi
 */
@Singleton
@LocalBean
public class SingletonSB {

    List<HttpSession> listaSesija;

    public SingletonSB() {
        listaSesija = new ArrayList<>();
    }

    public List<HttpSession> getActiveSessions() {
        return listaSesija;
    }
    
    public void addToSession(HttpSession sesija){
        listaSesija.add(sesija);
    }
}
