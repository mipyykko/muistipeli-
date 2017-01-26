/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

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
    Set<Tausta> testitaustat;
    int LEVEYS = 10, KORKEUS = 5;
    
    @Before
    public void setUp() {
        this.testikuvat = new HashSet<>();
        this.testitaustat = new HashSet<>();
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
    
    private void luoTestitaustat() {
        for (int i = 1; i <= (LEVEYS * KORKEUS) / 2; i++) {
            testitaustat.add(new GeneerinenTausta("*"));
            testitaustat.add(new GeneerinenTausta("*"));
        }
    }
    
    private void luoTestipelilauta() {
        luoTestikuvat();
        luoTestitaustat();
        pelilauta.setKuvasarja(testikuvat);
        pelilauta.setTaustasarja(testitaustat);
        pelilauta.luoPelilauta();
    }
    
    @Test
    public void kaikillaPari() {
        luoTestipelilauta();
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
        luoTestipelilauta();
        
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
    
    @Test
    public void kaikkiKaannetty() {
        luoTestipelilauta();
        
        for (int y = 0; y < KORKEUS; y++) {
            for (int x = 0; x < LEVEYS; x++) {
                pelilauta.getKortit()[x][y].kaanna();
            }
        }
        assertTrue("Kaikki kortit käännetty mutta kaikkiKaannetty = false", pelilauta.kaikkiKaannetty());
    }
}
