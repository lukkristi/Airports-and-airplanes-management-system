/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.rest.klijent;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:ProjektRestAerodromi [aerodromiProjekt]<br>
 * USAGE:
 * <pre>
 *        TestDrugiRS client = new TestDrugiRS();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author lukkristi
 */
public class TestDrugiRS {
    

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8084/lukkristi_modul_3_1/webresources";
    private String korisnik="";
    private String lozinka="";
    private String icao="";
    private String drzava="";
    private String naziv="";

    public TestDrugiRS() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("aerodromiProjekt");
    }
    
     public TestDrugiRS(String korisnik, String lozinka){
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("aerodromi");
        this.korisnik=korisnik;
        this.lozinka=lozinka;     
     }

    public void putJson(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public <T> T dajAerodomKorisnika(Class<T> responseType, String icao) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{icao}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)                 
                .header("lozinka", lozinka)
                .get(responseType);
    }

    public <T> T dajAerodomeDrzavaNaziv(Class<T> responseType, String drzava, String naziv) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (drzava != null && !drzava.isEmpty()) {
            resource = resource.queryParam("drzava", drzava);
        }
        if (naziv != null&& !naziv.isEmpty()) {
            resource = resource.queryParam("naziv", naziv);
        }
        resource = resource.path("svi");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)                 
                .header("lozinka", lozinka)
                .get(responseType);
    }

    public Response obrisiMojAerodom(Object requestEntity, String icao) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{icao})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)                 
                .header("lozinka", lozinka)         
                .delete(Response.class);
    }

    public <T> T dajAerodomeKorisnika(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)                 
                .header("lozinka", lozinka)
                .get(responseType);
    }

    public Response obrisiAvioneAerodoma(Object requestEntity, String icao) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}/avioni", new Object[]{icao})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)                 
                .header("lozinka", lozinka)              
                .delete(Response.class);
    }

    public void close() {
        client.close();
    }
    
}
