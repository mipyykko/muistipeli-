/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
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
    private int ikkunaleveys = 800;
    private int ikkunakorkeus = 600;
    private Map<String, String> kuvat;
    
    /**
     * Luo kuvat-hashmapin jossa oikeat osoitteet reasursseihin.
     * 
     * @param tileset 
     */
    public void lataaKuvat(String tileset) {
        kuvat = new HashMap<>();

        InputStream is = Main.class.getClassLoader().getResourceAsStream("kuvat/"+tileset+"/tileset.txt");
        try (Scanner s = new Scanner(is)) {
            while (s.hasNextLine()) {
                String[] t = s.nextLine().split(",");
                kuvat.put(t[0].replaceAll("^\"|\"$", ""),
                        "kuvat/"+tileset+"/images/" + t[1].replaceAll("^\"|\"$", ""));
            }
        }
    }

    public void lataaTaustat(String bgset) {
       // toistaiseksi ei mitään 
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        //root.getChildren().add(btn);

        // testauksena tässä vaiheessa

        ///////
        
        Set<Kuva> testikuvat = new HashSet<>();
        Set<Tausta> testitaustat = new HashSet<>();

        int leveys = 4, korkeus = 4;

        lataaKuvat("elukat"); // test
        
        Iterator<String> it = kuvat.keySet().iterator();
        for (int i = 0; i < (leveys * korkeus) / 2; i++) {
            String key = it.next();
            Image im = new Image(getClass().getClassLoader().getResource(kuvat.get(key)).toString(), 100, 100, false, false);
            testikuvat.add(new JavaFXKuva(key, im));
            testitaustat.add(new JavaFXTausta("tausta",
                    new Image(getClass().getClassLoader().getResource("taustat/basic/tausta.png").toString(), 100, 100, false, false)));
            testitaustat.add(new JavaFXTausta("tausta",
                    new Image(getClass().getClassLoader().getResource("taustat/basic/tausta.png").toString(), 100, 100, false, false)));
        }

        Peli peli = new Peli(null, "JavaFX");
        peli.uusiPeli(leveys, korkeus, testikuvat, testitaustat);
        JavaFXUI ui = new JavaFXUI(peli);
        peli.setUI(ui);
        ui.setStage(primaryStage);
        ui.nayta();
        
        //peli.pelaa();
    }
    
}
