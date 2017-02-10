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
import com.mipyykko.muistipeli.malli.enums.UITyyppi;
import com.mipyykko.muistipeli.malli.impl.TekstiKuva;
import com.mipyykko.muistipeli.malli.impl.TekstiTausta;
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

    private static Thread thread;

    public static void main(String[] args) {
        /*
         ooookei, notes to future self:
        
         - JavaFXMainilla tuo toimii, siis Application.launch(JavaFXMain, args)
         mutta JavaFXUI ei pääse inittiin tai jotain vaikka ovat beisik samoja
         */

        UITyyppi uiTyyppi = UITyyppi.JAVAFX;
        Korttityyppi korttityyppi = Korttityyppi.JAVAFX;

        switch (uiTyyppi) {
            case TEKSTI:
                Set<Kuva> testikuvat = new HashSet<>();
                Set<Tausta> testitaustat = new HashSet<>();

                int leveys = 4;
                int korkeus = 4;

                for (int i = 0; i < (leveys * korkeus) / 2; i++) {
                    testikuvat.add(new TekstiKuva(Integer.toString(i + 1)));
                    testitaustat.add(new TekstiTausta("*"));
                    testitaustat.add(new TekstiTausta("*"));
                }

                Peli peli = new Peli(null, korttityyppi);
                peli.uusiPeli(leveys, korkeus, testikuvat, testitaustat);
                UI ui = new TekstiUI(peli, new Scanner(System.in));
                peli.setUI(ui);
                ui.nayta();
                break;
            case JAVAFX:
                Application.launch(JavaFXMain.class, args);
                break;
            default:
                throw new AssertionError();
        }
    }
}
