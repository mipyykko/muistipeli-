/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.impl.GeneerinenKuva;
import com.mipyykko.muistipeli.malli.impl.GeneerinenTausta;
import com.mipyykko.muistipeli.ui.TekstiUI;
import com.mipyykko.muistipeli.ui.UI;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Application;

/**
 *
 * @author pyykkomi
 */
public class Main /*extends Application*/ {

    public static void main(String[] args) {
        /*
         ooookei, notes to future self:
        
         - JavaFXMainilla tuo toimii, siis Application.launch(JavaFXMain, args)
         mutta JavaFXUI ei pääse inittiin tai jotain vaikka ovat beisik samoja
         */

        String uiTyyppi = "JavaFX";

        switch (uiTyyppi) {
            case "Geneerinen":
                Set<Kuva> testikuvat = new HashSet<>();
                Set<Tausta> testitaustat = new HashSet<>();

                int leveys = 4;
                int korkeus = 4;

                for (int i = 0; i < (leveys * korkeus) / 2; i++) {
                    testikuvat.add(new GeneerinenKuva(Integer.toString(i + 1)));
                    testitaustat.add(new GeneerinenTausta("*"));
                    testitaustat.add(new GeneerinenTausta("*"));
                }

                Peli peli = new Peli(null, "Geneerinen");
                peli.uusiPeli(leveys, korkeus, testikuvat, testitaustat);
                UI ui = new TekstiUI(peli, new Scanner(System.in));
                peli.setUI(ui);
                peli.pelaa();
                break;
            case "JavaFX":
                Application.launch(JavaFXMain.class, args);
                break;
            default:
                throw new AssertionError();
        }
    }
}
