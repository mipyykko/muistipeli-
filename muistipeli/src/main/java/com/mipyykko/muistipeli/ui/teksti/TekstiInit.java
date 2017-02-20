/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.teksti;

import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.impl.TekstiKuva;
import com.mipyykko.muistipeli.malli.impl.TekstiTausta;
import java.util.HashSet;
import java.util.Set;

/**
 * Teksti-UI:n kuvat ja taustat luova luokka. Hehe, luova luokka.
 * 
 * @author pyykkomi
 */
public class TekstiInit {

    /**
     * Luo numerot tekstimuodossa sisältävät tekstikortit.
     * 
     * @param leveys Pelilaudan leveys.
     * @param korkeus Pelilaudan korkeus.
     * @return Setti kuvia.
     */
    public Set<Kuva> luoKuvat(int leveys, int korkeus) {
        Set<Kuva> kuvat = new HashSet<>();
        
        for (int i = 0; i < (leveys * korkeus) / 2; i++) {
            kuvat.add(new TekstiKuva(Integer.toString(i + 1)));
        }

        return kuvat;
    }

    /**
     * Luo tekstimuotoiset taustat.
     * 
     * @param leveys Pelilaudan leveys.
     * @param korkeus Pelilaudan korkeus.
     * @return Setti taustoja.
     */
    public Set<Tausta> luoTaustat(int leveys, int korkeus) {
        Set<Tausta> taustat = new HashSet<>();
        
        for (int i = 0; i < leveys * korkeus; i++) {
            taustat.add(new TekstiTausta("*"));
        }
        
        return taustat;
    }
}
