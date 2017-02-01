/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.GeneerinenKuva;
import com.mipyykko.muistipeli.malli.GeneerinenTausta;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.ui.TekstiUI;
import com.mipyykko.muistipeli.ui.UI;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author pyykkomi
 */
public class Main extends Application {

    private static UI ui;
    private static Peli peli;
    
    public static void main(String[] args) {
        //launch(args);
        // testi, nämä kyllä muuttavat täältä pois jossain vaiheessa
        Set<Kuva> testikuvat = new HashSet<>();
        Set<Tausta> testitaustat = new HashSet<>();
        
        int leveys = 4, korkeus = 4;

        for (int i = 0; i < (leveys * korkeus) / 2; i++) {
            testikuvat.add(new GeneerinenKuva(Integer.toString(i + 1)));
            testitaustat.add(new GeneerinenTausta("*"));
            testitaustat.add(new GeneerinenTausta("*"));
        }

        Peli peli = new Peli(leveys, korkeus, testikuvat, testitaustat);
        UI ui = new TekstiUI(peli, new Scanner(System.in));
        ui.setPeli(peli);
        //UI ui = new JavaFXUI(peli);
        //JavaFXUI ui = new JavaFXUI(peli);
        peli.setUI(ui);
        peli.pelaa();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*JavaFXUI ui = new JavaFXUI(null);
        ui.startUI(primaryStage);*/
    }

}
