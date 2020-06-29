package org.foi.nwtis.lukkristi.konfiguracije;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

public class KonfiguracijaBIN extends KonfiguracijaApstraktna {

    public KonfiguracijaBIN(String nazivDatoteke) {
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
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                Object o = ois.readObject();
                if(o instanceof Properties)
                    this.postavke = (Properties) o;
                ois.close();
            } catch (Exception ex) {
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
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(this.postavke);
                oos.close();
            } catch (IOException ex) {
                throw new NeispravnaKonfiguracija("Problem kod spremanja konfiguracije!");
            }
        } else {
            throw new NemaKonfiguracije("Datoteka pod nazivom "+datoteka+" ne postoji ili nije datoteka!");
        }
        
    }
    
}
