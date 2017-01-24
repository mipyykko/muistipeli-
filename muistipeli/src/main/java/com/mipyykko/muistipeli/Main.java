/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.GeneerinenKuva;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Pelilauta;
import com.mipyykko.muistipeli.ui.TekstiUI;
import com.mipyykko.muistipeli.ui.UI;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author pyykkomi
 */
public class Main {

    public static void main(String[] args) {
        // testi
        Set<Kuva> testikuvat = new HashSet<>();
        int leveys = 8, korkeus = 4;
        
        for (int i = 0; i < 16; i++) {
            testikuvat.add(new GeneerinenKuva(Integer.toString(i + 1)));
        }
        
        Pelilauta p = new Pelilauta(leveys, korkeus, testikuvat, null);
        Peli peli = new Peli(p);
        p.luoPelilauta();
        UI ui = new TekstiUI(peli);
        
        ui.nayta();

    }
    
}
