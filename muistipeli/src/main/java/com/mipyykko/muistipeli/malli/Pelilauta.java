/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

import java.awt.Point;
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

    public void luoPelilauta(String korttiTyyppi, boolean sekoita) throws Exception {
        if (kuvasarja == null || kuvasarja.isEmpty() || kuvasarja.size() * 2 < leveys * korkeus) {
            throw new Exception("Kuvasarjassa on liian vähän kuvia!");
        }
        if (taustasarja == null || taustasarja.isEmpty() || taustasarja.size() < leveys * korkeus) {
            throw new Exception("Taustasarjassa on liian vähän taustoja!");
        }
        List<Kortti> arvottavat = new ArrayList<>();
        
        Korttitehdas kt = new Korttitehdas(korttiTyyppi);
        
        for (Kuva k : kuvasarja) {
            arvottavat.add(kt.uusiKortti(k, null));
            arvottavat.add(kt.uusiKortti(k, null)); // kaksi jokaista
        }
 
        if (sekoita) { // pääasiassa testejä varten
            Collections.shuffle(arvottavat);
        }
        Iterator<Kortti> kortit = arvottavat.iterator();
        Iterator<Tausta> taustat = taustasarja.iterator();
        
        for (int y = 0; y < korkeus; y++) {
            for (int x = 0; x < leveys; x++) {
                pelilauta[x][y] = kortit.next();
                pelilauta[x][y].setTausta(taustat.next());
            }
        }
    }
 
    public boolean kaikkiKaannetty() {
        for (int y = 0; y < korkeus; y++) {
            for (int x = 0; x < leveys; x++) {
                if (!pelilauta[x][y].kaannetty()) {
                    return false;
                }
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

    public Kortti getKortti(Point p) {
        return getKortit()[p.x][p.y];
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
