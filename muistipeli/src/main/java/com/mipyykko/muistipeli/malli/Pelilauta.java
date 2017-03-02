/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import com.mipyykko.muistipeli.util.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Pelin kortit sisältävä pelilauta-luokka.
 * 
 * @author pyykkomi
 */
public class Pelilauta {
    
    private Kortti[][] kortit;
    private int leveys, korkeus;
    private Set<Kuva> kuvasarja;
    private Set<Tausta> taustasarja;
    
    /**
     * Konstruktori. 
     * @param leveys Pelialueen leveys
     * @param korkeus Pelialueen korkeus
     * @param kuvasarja Voi olla myös null, konstruktori ei oleta sisältävän mitään
     * @param taustasarja Voi olla myös null, konstruktori ei oleta sisältävän mitään
     */
    public Pelilauta(int leveys, int korkeus, Set<Kuva> kuvasarja, Set<Tausta> taustasarja) {
        this.leveys = leveys;
        this.korkeus = korkeus;
        this.kortit = new Kortti[leveys][korkeus];
        this.kuvasarja = kuvasarja;
        this.taustasarja = taustasarja;
    }
    
    /**
     * Luo uuden pelilaudan ja asettaa sille kortit.
     * 
     * @param korttityyppi Kortin tyyppi.
     * @param sekoita Sekoitetaanko kortit? Ilman sekoitusta jokainen pari on laudalla peräkkäin.
     * @throws Exception Virheellinen määrä kuvia tai taustoja.
     */
    public void luoPelilauta(Korttityyppi korttityyppi, boolean sekoita) throws Exception {
        if (kuvasarja == null || kuvasarja.isEmpty() || kuvasarja.size() < (leveys * korkeus) / 2) {
            int s = 0;
            if (kuvasarja != null) {
                s = kuvasarja.size();
            }
            throw new Exception("kuvia odotettu " + (leveys * korkeus) / 2 + ", saatu " + s);
        }
        if (taustasarja == null || taustasarja.isEmpty() || taustasarja.size() < leveys * korkeus) {
            int s = 0;
            if (taustasarja != null) {
                s = taustasarja.size();
            }
            throw new Exception("taustoja odotettu " + leveys * korkeus + ", saatu " + s);
        }
        if (korttityyppi == null) {
            throw new Exception("korttityyppi puuttuu");
        }
        List<Kortti> arvottavat = new ArrayList<>();
        
        Korttitehdas kt = new Korttitehdas(korttityyppi);
        
        for (Kuva k : kuvasarja) {
            arvottavat.add(kt.uusiKortti(k, null));
            arvottavat.add(kt.uusiKortti(k, null));
        }
 
        if (sekoita) { // pääasiassa testejä varten, nyt tää on hajalla kuitenkin taas
            Collections.shuffle(arvottavat);
        }
        
        Iterator<Kortti> kortit = arvottavat.iterator();
        Iterator<Tausta> taustat = taustasarja.iterator();
        
        for (int y = 0; y < korkeus; y++) {
            for (int x = 0; x < leveys; x++) {
                this.kortit[x][y] = kortit.next();
                this.kortit[x][y].setTausta(taustat.next());
            }
        }
    }
 
    /**
     * Onko kaikki kortit käännetty?
     * 
     * @return boolean-arvo
     */
    public boolean kaikkiKaannetty() {
        for (int y = 0; y < korkeus; y++) {
            for (int x = 0; x < leveys; x++) {
                if (!kortit[x][y].getKaannetty()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Palauttaa pelilaudan kortit kaksiulotteisena arrayna.
     * @return kortti-array
     */
    public Kortti[][] getKortit() {
        return kortit;
    }

    public void setKortit(Kortti[][] kortit) {
        this.kortit = kortit;
    }

    /**
     * Palauttaa kortin pisteessä p.
     * @param p Koordinaatit Point-muodossa.
     * @return kortti
     */
    public Kortti getKortti(Point p) {
        if (p.x >= 0 && p.x < leveys &&
            p.y >= 0 && p.y < korkeus) {
            return getKortit()[p.x][p.y];
        } else {
            return null;
        }
    }

    public int getLeveys() {
        return leveys;
    }

    public int getKorkeus() {
        return korkeus;
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
