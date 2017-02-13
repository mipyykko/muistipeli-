/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import com.mipyykko.muistipeli.malli.impl.JavaFXKuva;
import com.mipyykko.muistipeli.malli.impl.JavaFXTausta;
import com.mipyykko.muistipeli.ui.JavaFXUI;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * JavaFX-käyttöliittymän käynnistävä pääohjelma.
 * 
 * @author pyykkomi
 */
public class JavaFXMain extends Application {

    private GridPane root;
    private Group kortit;
    private Scene scene;
    private Set<Kuva> kuvat;
    private Set<Tausta> taustat;
    private int ikkunaleveys = 800;
    private int ikkunakorkeus = 600;
    private Map<String, String> kuvalista;
    private int leveys, korkeus;
    
    /**
     * Luo kuvalistan jossa oikeat osoitteet resursseihin.
     * 
     * @param tileset Hakemiston nimi. Metodi odottaa hakemistossa olevan tileset.txt-nimisen tiedoston.
     */
    public void lueKuvalista(String tileset) {
        kuvalista = new HashMap<>();

        InputStream is = Main.class.getClassLoader().getResourceAsStream("kuvat/" + tileset + "/tileset.txt");
        try (Scanner s = new Scanner(is)) {
            while (s.hasNextLine()) {
                String[] t = s.nextLine().split(",");
                kuvalista.put(t[0].replaceAll("^\"|\"$", ""),
                        "kuvat/" + tileset + "/images/" + t[1].replaceAll("^\"|\"$", ""));
            }
        }
    }
    /**
     * Luo Image-objektit korttien kuville.
     */
    public void luoKuvat() {
        Iterator<String> it = kuvalista.keySet().iterator();
        double maxWidth = 0;
        double maxHeight = 0;
        
        for (int i = 0; i < (leveys * korkeus) / 2; i++) {
            String key = it.next();
            // TODO: kuvien skaalaus pois?
            Image kuva = new Image(getClass().getClassLoader().getResource(kuvalista.get(key)).toString(), 200, 200, false, false);
            maxWidth = Math.max(maxWidth, kuva.getWidth());
            maxHeight = Math.max(maxHeight, kuva.getHeight());
            kuvat.add(new JavaFXKuva(key, kuva));
            // test
            Image tausta = new Image(getClass().getClassLoader().getResource("taustat/basic/tausta.png").toString(), 200, 200, true, false);
            maxWidth = Math.max(maxWidth, tausta.getWidth());
            maxHeight = Math.max(maxHeight, tausta.getHeight());
            taustat.add(new JavaFXTausta("tausta", tausta));
            taustat.add(new JavaFXTausta("tausta", tausta));
        }

        
    }
    public void lataaTaustat(String bgset) {
       // toistaiseksi ei mitään 
    }
    
    /**
     * Käyttöliittymän ajava pääohjelma.
     * 
     * @param primaryStage JavaFX:n luoma Stage.
     * @throws Exception -
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        kuvat = new HashSet<>();
        taustat = new HashSet<>();
        
        leveys = 4;
        korkeus = 4;

        lueKuvalista("elukat-scaled"); // test
        luoKuvat();
        
        Peli peli = new Peli(null, Korttityyppi.JAVAFX);
        peli.uusiPeli(leveys, korkeus, kuvat, taustat);
        JavaFXUI ui = new JavaFXUI(peli);
        peli.setUI(ui);
        ui.setStage(primaryStage);
        ui.nayta();
    }
    
}
