/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.GeneerinenKuva;
import com.mipyykko.muistipeli.malli.GeneerinenTausta;
import com.mipyykko.muistipeli.malli.Kortti;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Pelilauta;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.ui.TekstiUI;
import com.mipyykko.muistipeli.ui.UI;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author pyykkomi
 */
public class Main {

    public static void main(String[] args) {
        // testi
        Set<Kuva> testikuvat = new HashSet<>();
        Set<Tausta> testitaustat = new HashSet<>();
        
        Kortti k = new Kortti(new GeneerinenKuva("testi"), null);
        Kortti k2 = new Kortti(new GeneerinenKuva("testi"), null);
        
        System.out.println(k == k2);
        int leveys = 8, korkeus = 4;

        for (int i = 0; i < 16; i++) {
            testikuvat.add(new GeneerinenKuva(Integer.toString(i + 1)));
            testitaustat.add(new GeneerinenTausta("*"));
            testitaustat.add(new GeneerinenTausta("*"));
        }

        Pelilauta p = new Pelilauta(leveys, korkeus, testikuvat, testitaustat);
        Peli peli = new Peli(p);
        p.luoPelilauta();
        UI ui = new TekstiUI(peli, new Scanner(System.in));

        while(true) {
            ui.nayta();
            int siirto[] = ui.siirto();
            if (siirto != null) {
                int x = siirto[0];
                int y = siirto[1];
                if (!p.getKortit()[x][y].kaannetty()) {
                    p.getKortit()[x][y].kaanna();
                }
            }
        }    
    }

}
