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
 * Muistipeliö, simppeliäkin simppelimpi javalabra-projekti.
 * 
 * @author pyykkomi
 */
public class Main {

    private static Thread thread;
    
    /**
     * Pääluokka. JavaFX haarautuu tästä omaansa.
     * @param args komentoriviparametrit, joita ei juuri nyt käytetä mihinkään
     */
    public static void main(String[] args) {

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

                Peli peli = new Peli(korttityyppi);
                try {
                    peli.uusiPeli(leveys, korkeus, testikuvat, testitaustat);
                } catch (Exception ex) {
                    System.out.println("hetkinen?"); //TODO
                }
                UI ui = new TekstiUI(peli, new Scanner(System.in));
                try {
                    ui.nayta();
                } catch (Exception e) {
                }
                break;
            case JAVAFX:
                Application.launch(JavaFXMain.class, args);
                break;
            default:
                throw new AssertionError();
        }
    }
}
