/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.util;

import com.mipyykko.muistipeli.Main;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.impl.JavaFXKuva;
import com.mipyykko.muistipeli.malli.impl.JavaFXTausta;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import javafx.scene.image.Image;

/**
 *
 * @author pyykkomi
 */
public class JavaFXInit {

    private Map<String, String> kuvalista, taustalista;

    /**
     * Luo kuvalistan jossa oikeat osoitteet resursseihin.
     *
     * @param tileset Hakemiston nimi. Metodi odottaa hakemistossa olevan
     * tileset.txt-nimisen tiedoston.
     */
    public void lueKuvalista(String tileset) {
        // debug: tää vois ihan hyvin vaan lukea hakemiston kaikki .png-tiedostot
        kuvalista = new HashMap<>();

        InputStream is = Main.class.getClassLoader().getResourceAsStream("kuvat/" + tileset + "/tileset.txt");
        try (Scanner s = new Scanner(is)) {
            while (s.hasNextLine()) {
                String[] t = s.nextLine().split(",");
                if (t.length == 2) {
                    kuvalista.put(t[0].replaceAll("^\"|\"$", ""),
                        "kuvat/" + tileset + "/images/" + t[1].replaceAll("^\"|\"$", ""));
                }
            }
        }
    }

    public void lueTaustalista(String bgset) {
        taustalista = new HashMap<>();
        // jotain jotain
    }

    /**
     * Luo Image-objektit korttien kuville.
     *
     * @param leveys Pelilaudan leveys.
     * @param korkeus Pelilaudan korkeus
     * @throws Exception Virhe kuvien luonnissa?
     * @return Palauttaa kuvat sisältävän Setin.
     */
    public Set<Kuva> luoKuvat(int leveys, int korkeus) throws Exception {
        Iterator<String> it = kuvalista.keySet().iterator();
        Set<Kuva> kuvat = new HashSet<>();

        try {
            for (int i = 0; i < (leveys * korkeus) / 2; i++) {
                String key = it.next();
                // TODO: ei hyväksy välejä tiedostonimissä!
                String tiedostonimi = URLDecoder.decode(kuvalista.get(key), "UTF-8");
                InputStream kuvaOsoite = Main.class.getClassLoader().getResourceAsStream(tiedostonimi);
                if (kuvaOsoite != null) {
                    Image kuva = new Image(kuvaOsoite);//, 200, 200, false, false);
                    kuvat.add(new JavaFXKuva(key, kuva));
                }
            }
        } catch (Exception e) {
            throw new Exception("Kuvia ei voitu luoda! " + e.getMessage());
        }

        return kuvat;
    }

    /**
     * Tämä (ehkä) tulevaisuudessa lataa taustat erillään.
     *
     * @param leveys Pelilaudan leveys.
     * @param korkeus Pelilaudan korkeus.
     * @return Palauttaa taustat sisältävän Setin.
     * @throws Exception Virhe taustoja ladatessa?
     */
    public Set<Tausta> luoTaustat(int leveys, int korkeus) throws Exception {
        // debug: ei taustalistaa
        //Iterator<String> it = taustalista.keySet().iterator();
        Set<Tausta> taustat = new HashSet<>();
        taustalista.put("tausta", "debug");
        
        try {
            for (int i = 0; i < leveys * korkeus; i++) {
                // String key = it.next(); // debug
                String key = "tausta";
                String testiTausta = "taustat/basic/tausta.png"; // "taustat/basic/tausta.png"
                InputStream taustaOsoite = Main.class.getClassLoader().getResourceAsStream(testiTausta);
                //InputStream taustaOsoite = Main.class.getClassLoader().getResourceAsStream(taustalista.get(key));
                Image tausta = new Image(taustaOsoite);
                taustat.add(new JavaFXTausta(taustalista.get(key), tausta));
            }
        } catch (Exception e) {
            throw new Exception("Taustoja ei voitu luoda! " + e.getMessage());
        }
        return taustat;
    }

}
