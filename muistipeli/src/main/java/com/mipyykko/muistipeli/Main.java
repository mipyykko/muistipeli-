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
import com.mipyykko.muistipeli.ui.teksti.TekstiUI;
import com.mipyykko.muistipeli.ui.UI;
import com.mipyykko.muistipeli.ui.teksti.TekstiInit;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Application;

/** 
 * Muistipeliö, simppeliäkin simppelimpi javalabra-projekti.
 * 
 * @author pyykkomi
 */
public class Main {

    /**
     * Pääluokka. JavaFX haarautuu tästä omaansa.
     * @param args komentoriviparametrit - ainoa toistaiseksi toimiva on
     * "teksti" joka käynnistää pelin tekstimuoodissa.
     */
    public static void main(String[] args) {

        UITyyppi uiTyyppi = UITyyppi.JAVAFX;
        Korttityyppi korttityyppi = Korttityyppi.JAVAFX;

        if (args.length > 0) {
            String s = args[0].toLowerCase();
            if (s.equals("teksti")) {
                uiTyyppi = UITyyppi.TEKSTI;
                korttityyppi = Korttityyppi.TEKSTI;
            }
        }

        switch (uiTyyppi) {
            case TEKSTI:

                int leveys = 4;
                int korkeus = 4;

                TekstiInit ti = new TekstiInit();
                
                Set<Kuva> kuvat = ti.luoKuvat(leveys, korkeus);
                Set<Tausta> taustat = ti.luoTaustat(leveys, korkeus);

                Peli peli = new Peli(korttityyppi);
                try {
                    peli.uusiPeli(leveys, korkeus, kuvat, taustat);
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
