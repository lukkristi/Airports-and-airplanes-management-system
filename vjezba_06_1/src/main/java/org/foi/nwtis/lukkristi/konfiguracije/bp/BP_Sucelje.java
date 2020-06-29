package org.foi.nwtis.lukkristi.konfiguracije.bp;

import java.util.Properties;

/**
 *
 * @author lukkristi
 */
public interface BP_Sucelje {
    String getAdminDatabase();
    String getAdminPassword();
    String getAdminUsername();
    String getDriverDatabase();
    String getDriverDatabase(String bp_url);
    Properties getDriversDatabase();
    String getServerDatabase();
    String getUserDatabase();
    String getUserPassword();
    String getUserUsername();
    String getOpenSkyNetworkKorisnik();
    String getOpenSkyNetworkLozinka();
    int getPreuzimanjeCiklusa();
    int getPreuzimanjePauza();
    String getPreuzimanjePocetak();
    String getPreuzimanjeKraj();
    boolean getPreuzimanjeStatus();
    
}
