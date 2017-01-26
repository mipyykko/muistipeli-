/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author pyykkomi
 */
public class Pelilauta {
    
    private Kortti[][] pelilauta;
    private int leveys, korkeus;
    private Set<Kuva> kuvasarja;
    private Set<Tausta> taustasarja;
    private Random random;
    
    public Pelilauta(int leveys, int korkeus, Set<Kuva> kuvasarja, Set<Tausta> taustasarja) {
        this.leveys = leveys;
        this.korkeus = korkeus;
        this.pelilauta = new Kortti[leveys][korkeus];
        this.kuvasarja = kuvasarja;
        this.taustasarja = taustasarja;
        // TODO: taustakuva josta lohkotaan taustat korteille?
    }

    public void luoPelilauta() /*throws Exception*/ {
        /*if (kuvasarja.size() * 2 < leveys * korkeus) {
            throw new Exception("Korttisarjassa on liian vähän kortteja!");
        } // tää ei nyt sinänsä oo välttämätöntä mutta
        */
        List<Kortti> arvottavat = new ArrayList<>();
        //TODO: taustan ja kuvan eriytys
        
        
        for (Kuva k : kuvasarja) {
            arvottavat.add(new Kortti(k, null));
            arvottavat.add(new Kortti(k, null)); // kaksi jokaista
        }

        Collections.shuffle(arvottavat);
        Iterator<Kortti> i = arvottavat.iterator();
        Iterator<Tausta> t = taustasarja.iterator();
        
        for (int y = 0; y < korkeus; y++) {
            for (int x = 0; x < leveys; x++) {
                pelilauta[x][y] = i.next();
                pelilauta[x][y].setTausta(t.next());
            }
        }
    }
 
    public boolean kaikkiKaannetty() {
        for (int y = 0; y < korkeus; y++) {
            for (int x = 0; x < leveys; x++) {
                if (!pelilauta[x][y].kaannetty()) return false;
            }
        }
        return true;
    }
    
    public Kortti[][] getKortit() {
        return pelilauta;
        // TODO rakennetta mietittävä
    }

    public void setKortit(Kortti[][] pelilauta) {
        this.pelilauta = pelilauta;
    }

    public int getLeveys() {
        return leveys;
    }

    public void setLeveys(int leveys) {
        this.leveys = leveys;
    }

    public int getKorkeus() {
        return korkeus;
    }

    public void setKorkeus(int korkeus) {
        this.korkeus = korkeus;
    }

    public Set<Kuva> getKuvasarja() {
        return kuvasarja;
    }

    public void setKuvasarja(Set<Kuva> kuvasarja) {
        this.kuvasarja = kuvasarja;
    }

    public Set<Tausta> getTaustasarja() {
        return taustasarja;
    }

    public void setTaustasarja(Set<Tausta> taustasarja) {
        this.taustasarja = taustasarja;
    }
    
    
}
