package org.foi.nwtis.lukkristi.konfiguracije;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class KonfiguracijaJSON extends KonfiguracijaApstraktna {

    public KonfiguracijaJSON(String nazivDatoteke) {
        super(nazivDatoteke);
    }

     @Override
    public void ucitajKonfiguraciju() throws NemaKonfiguracije, NeispravnaKonfiguracija {
        ucitajKonfiguraciju(this.nazivDatoteke);
    }

    @Override
    public void ucitajKonfiguraciju(String datoteka) throws NemaKonfiguracije, NeispravnaKonfiguracija {
        this.obrisiSvePostavke();
        
        if(datoteka == null || datoteka.length() == 0)
            throw new NemaKonfiguracije("Neispravni naziv datoteke!");
        
        File f = new File(datoteka);
        if(f.exists() && f.isFile()){
            try {
                Gson gson = new Gson();
                BufferedReader br = new BufferedReader(new FileReader(f));
                this.postavke = gson.fromJson(br, Properties.class);
                br.close();
            } catch (IOException ex) {
                throw new NeispravnaKonfiguracija("Problem kod učitavanja konfiguracije!");
            }
        } else {
            throw new NemaKonfiguracije("Datoteka pod nazivom "+datoteka+" ne postoji ili nije datoteka!");
        }
        
    }

    @Override
    public void spremiKonfiguraciju() throws NemaKonfiguracije, NeispravnaKonfiguracija {
        spremiKonfiguraciju(this.nazivDatoteke);
    }

    @Override
    public void spremiKonfiguraciju(String datoteka) throws NemaKonfiguracije, NeispravnaKonfiguracija {
    
         if(datoteka == null || datoteka.length() == 0)
            throw new NemaKonfiguracije("Neispravni naziv datoteke!");
        
        File f = new File(datoteka);
        if((f.exists() && f.isFile()) || !f.exists()){
            try {
                Gson gson = new Gson();
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                gson.toJson(this.postavke,bw);
                bw.close();
            } catch (IOException ex) {
                throw new NeispravnaKonfiguracija("Problem kod spremanja konfiguracije!");
            }
        } else {
            throw new NemaKonfiguracije("Datoteka pod nazivom "+datoteka+" ne postoji ili nije datoteka!");
        }
        
    }
}
