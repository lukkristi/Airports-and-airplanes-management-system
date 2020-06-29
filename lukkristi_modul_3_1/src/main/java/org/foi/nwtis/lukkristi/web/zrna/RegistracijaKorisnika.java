/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.zrna;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.ejb.eb.Korisnici;
import org.foi.nwtis.lukkristi.ejb.sb.KorisniciFacadeLocal;
import org.foi.nwtis.lukkristi.konfiguracije.Konfiguracija;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author lukkristi
 */
@Named(value = "registracijaKorisnika")
@SessionScoped
public class RegistracijaKorisnika implements Serializable {

    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;
    @Inject
    ServletContext context;   
    
    @Getter
    @Setter
    String korisnik = "";
    @Getter
    @Setter
    String lozinka = "";
    @Getter
    @Setter
    String ime = "";
    @Getter
    @Setter
    String prezime = "";
    @Getter
    @Setter
    String email = "";
    @Getter
    @Setter
    String adresa;
    @Getter
    @Setter
    int port;
    @Getter
    @Setter
    private String komanda;
    @Getter
    @Setter
    private String odgovor;
    private Socket socket;

    /**
     * Creates a new instance of RegistracijaKorisnika
     */
    public RegistracijaKorisnika() {
        
    }

    /**
     * Metoda koja preuzima parametre iz forme za registraciju korisnika
     *
     * @return
     */
    public String registrirajKorisnika() {
        Korisnici novi = new Korisnici();
        novi.setIme(ime);
        novi.setKorIme(korisnik);
        novi.setLozinka(lozinka);
        novi.setEmail(email);
        korisniciFacadeLocal.create(novi);
        return "";
    }

    private void dohvatiPortIAdresuIzPostavki() {

        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        adresa = bpk.getKonfig().dajPostavku("adresa");
        port = Integer.parseInt(bpk.getKonfig().dajPostavku("port"));
    }

    public String saljiKomanduRegistrirajKorisnika() throws IOException {
        komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; DODAJ;";
        dohvatiPortIAdresuIzPostavki();
        saljiKomandu(komanda);
        return "";
    }

    private void saljiKomandu(String command) {
        Socket socket = null;
        InputStream is = null;
        DataInputStream in = null;
        OutputStream os = null;
        DataOutputStream out = null;
        try {
            socket = new Socket(adresa, port);
            is = socket.getInputStream();
            in = new DataInputStream(is);
            os = socket.getOutputStream();
            out = new DataOutputStream(os);
            out.writeUTF(command);
            out.flush();
            socket.shutdownOutput();
            StringBuffer sb = new StringBuffer();
            while (true) {
                try {
                    String odgovor = in.readUTF();
                    sb.append(odgovor);
                } catch (EOFException ex) {
                    break;
                }
            }
            odgovor = sb.toString();
            provjeriOdgovor();
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
            System.out.println(" socket:  " + sb.toString());

        } catch (IOException ex) {
            Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void provjeriOdgovor() {
        if (odgovor == "OK 10;") {
            registrirajKorisnika();
        }
    }
}
