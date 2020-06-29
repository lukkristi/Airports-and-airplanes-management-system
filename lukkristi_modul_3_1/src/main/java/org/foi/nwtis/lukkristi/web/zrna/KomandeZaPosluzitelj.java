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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author lukkristi
 */
@Named(value = "komandeZaPosluzitelj")
@SessionScoped
public class KomandeZaPosluzitelj implements Serializable {
   
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
    
    @Inject
    ServletContext context;   
    @Getter
    @Setter
    String korisnik = "";
    @Getter
    @Setter
    String lozinka = "";
    /**
     * Creates a new instance of KomandeZaPosluzitelj
     */
    public KomandeZaPosluzitelj() {        
        getSession();
    }
    
    public void autentificiraj() throws IOException {
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (sesija == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("pogled_1.xhtml");
        } else {
            if (sesija.getAttribute("korisnik") == null) {
                FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("pogled_1.xhtml");
            }
        }
    }
    
    private void getSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        korisnik = (String) session.getAttribute("korisnik");
        lozinka = (String) session.getAttribute("lozinka");
    }
    
    private void dohvatiPortIAdresuIzPostavki() {        

        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        adresa = bpk.getKonfig().dajPostavku("adresa");
        port = Integer.parseInt(bpk.getKonfig().dajPostavku("port"));
    }
    
    public String saljiKomanduPauza() throws IOException {
        komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; PAUZA;";
        saljiKomandu(komanda);
        return "";
    }

    public String saljiKomanduRadi() throws IOException {
        komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; RADI";
        saljiKomandu(komanda);
        return "";
    }

    public String saljiKomanduKraj() throws IOException {
        komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; KRAJ;";
        saljiKomandu(komanda);
        return "";
    }

    public String saljiKomanduStanje() throws IOException {
        komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; STANJE;";
        saljiKomandu(komanda);
        return "";
    }

    public String saljiKomanduGrupaPrijavi() throws IOException {
        komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA PRIJAVI;";
        saljiKomandu(komanda);
        return "";
    }

    public String saljiKomanduOdjaviGrupu() throws IOException {
        komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA ODJAVI;";
        saljiKomandu(komanda);
        return "";
    }
    public String saljiKomanduGrupaAkriviraj() throws IOException {
        komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA AKTIVIRAJ;";
        saljiKomandu(komanda);
        return "";
    }

    public String saljiKomanduStanjeGrupe() throws IOException {
        komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA STANJE;";
        saljiKomandu(komanda);
        return "";
    }
    public String saljiKomanduGrupaAerodrom() throws IOException {
        komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA AERODROMI;";
        saljiKomandu(komanda);
        return "";
    }

    public String saljiKomanduBlokirajGrupu() throws IOException {
        komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA BLOKIRAJ;";
        saljiKomandu(komanda);
        return "";
    }
    
    
     private void saljiKomandu(String command) {
        dohvatiPortIAdresuIzPostavki();
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
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
            System.out.println(" socket:  " + sb.toString());

        } catch (IOException ex) {
            Logger.getLogger(KomandeZaPosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Došlo je do pogreške!"));
        }
    }
    
}
