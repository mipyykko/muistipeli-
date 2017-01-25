/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.logiikka;

import com.mipyykko.muistipeli.malli.GeneerinenKuva;
import com.mipyykko.muistipeli.malli.Kortti;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Pelilauta;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author pyykkomi
 */
public class PelilautaTest {
    
    Pelilauta pelilauta;
    Set<Kuva> testikuvat;
    int LEVEYS = 10, KORKEUS = 5;
    
    @Before
    public void setUp() {
        this.testikuvat = new HashSet<>();
        pelilauta = new Pelilauta(LEVEYS, KORKEUS, null, null);
    }
    
    @After
    public void tearDown() {
    }

    private void luoTestikuvat() {
        for (int i = 1; i <= (LEVEYS * KORKEUS) / 2; i++) {
            testikuvat.add(new GeneerinenKuva(Integer.toString(i)));
        }
    }
    
    @Test
    public void kaikillaPari() {
        luoTestikuvat();
        pelilauta.setKuvasarja(testikuvat);
        pelilauta.luoPelilauta();
        Map<Kuva, Kuva> parit = new HashMap<>();
        
        for (int y = 0; y < KORKEUS; y++) {
            for (int x = 0; x < LEVEYS; x++) {
                Kuva k = pelilauta.getKortit()[x][y].getKuva();
                if (parit.containsKey(k)) {
                    assertTrue("Useampi kuin kaksi samaa korttia", parit.get(k) == null);
                    parit.put(k, k);
                } else {
                    parit.put(k, null);
                }
            }
        }
    }
    
    @Test
    public void onkoSekoitettu() {
        luoTestikuvat();
        pelilauta.setKuvasarja(testikuvat);
        pelilauta.luoPelilauta();
        
        Kuva ed = pelilauta.getKortit()[0][0].getKuva();
        int perattaiset = 0;
        int idx = 0;
        
        for (int y = 0; y < KORKEUS; y++) {
            for (int x = 0; x < LEVEYS; x++) {
                Kuva k = pelilauta.getKortit()[x][y].getKuva();
                if (idx > 0 && idx % 2 != 0 && k.toString().equals(ed.toString())) perattaiset++;
                if (idx %2 == 0) ed = k;
                idx++;
            }
        }
        /* testataan, ettei laudalla kaikki parit ole vierekkäin. Toki tämäkin voi feilata, jos käy
           satumainen tuuri...
        */
        
        assertTrue("Kortteja ei ole sekoitettu", perattaiset < (KORKEUS * LEVEYS) / 2);
    } 
}
