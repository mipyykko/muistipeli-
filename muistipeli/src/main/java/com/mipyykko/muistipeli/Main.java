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
import com.mipyykko.muistipeli.malli.Pelilauta;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.ui.TekstiUI;
import com.mipyykko.muistipeli.ui.UI;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author pyykkomi
 */
public class Main extends Application {

    public static void main(String[] args) {
        // seuraava rivi käyttöön jos javaFX kuten myös start-metodissa
        //launch(args); 

        // testi, nämä kyllä muuttavat täältä pois jossain vaiheessa
        // testing testing
        
        Set<Kuva> testikuvat = new HashSet<>();
        Set<Tausta> testitaustat = new HashSet<>();

        int leveys = 4, korkeus = 4;

        for (int i = 0; i < (leveys * korkeus) / 2; i++) {
            testikuvat.add(new GeneerinenKuva(Integer.toString(i + 1)));
            testitaustat.add(new GeneerinenTausta("*"));
            testitaustat.add(new GeneerinenTausta("*"));
        }

        Peli peli = new Peli(null);
        peli.uusiPeli(leveys, korkeus, testikuvat, testitaustat);
        UI ui = new TekstiUI(peli, new Scanner(System.in));
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
