/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import com.mipyykko.muistipeli.malli.impl.TekstiKuva;
import com.mipyykko.muistipeli.malli.impl.TekstiTausta;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
    int LEVEYS = 4, KORKEUS = 4;
    
    @Before
    public void setUp() {
        pelilauta = new Pelilauta(LEVEYS, KORKEUS, null, null);
    }
    
    private void luoTestikuvat() {
        this.testikuvat = new HashSet<>();
        for (int i = 1; i <= (LEVEYS * KORKEUS) / 2; i++) {
            testikuvat.add(new TekstiKuva(Integer.toString(i)));
        }
    }
    
    private void luoTestitaustat() {
        this.testitaustat = new HashSet<>();
        for (int i = 1; i <= LEVEYS * KORKEUS; i++) {
            testitaustat.add(new TekstiTausta("*"));
        }
    }
    
    private void luoTestipelilauta() {
        luoTestikuvat();
        luoTestitaustat();
        pelilauta.setKuvasarja(testikuvat);
        pelilauta.setTaustasarja(testitaustat);
        try {
            pelilauta.luoPelilauta(Korttityyppi.TEKSTI, true);
        } catch (Exception ex) {
            fail("Pelilaudan luominen epäonnistui");
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    
    @Test
    public void luontiEiOnnistuIlmanKuvasarjaa() {
        luoTestitaustat();
        Pelilauta pl = new Pelilauta(LEVEYS, KORKEUS, null, testitaustat);
        try {
            pl.luoPelilauta(Korttityyppi.TEKSTI, true);
        } catch (Exception ex) {
            assertEquals("Pelilaudan luominen onnistuu ilman kuvasarjaa", "kuvia odotettu " + (LEVEYS * KORKEUS) / 2 + ", saatu 0", ex.getMessage());
        }
        assertTrue("Pelilaudan luominen onnistuu ilman kuvasarjaa #2", pl.getKortit()[0][0] == null);
    }
    
    @Test
    public void luontiEiOnnistuLiianPienelläKuvasarjalla() {
        luoTestikuvat();
        luoTestitaustat();
        Pelilauta pl = new Pelilauta(LEVEYS + 2, KORKEUS + 2, testikuvat, testitaustat);
        try {
            pl.luoPelilauta(Korttityyppi.TEKSTI, true);
        } catch (Exception ex) {
            assertEquals("Pelilaudan luominen onnistuu liian pienellä kuvasarjalla", "kuvia odotettu " + ((LEVEYS + 2) * (KORKEUS + 2)) / 2 + ", saatu " + (LEVEYS * KORKEUS) / 2, ex.getMessage());
        }
        assertTrue("Pelilaudan luominen onnistuu liian pienellä kuvasarjalla #2", pl.getKortit()[0][0] == null);
    }
    
    @Test
    public void luontiEiOnnistuIlmanTaustasarjaa() {
        luoTestikuvat();
        Pelilauta pl = new Pelilauta(LEVEYS, KORKEUS, testikuvat, null);
        //pelilauta.setTaustasarja(new HashSet<Tausta>());
        try {
            pl.luoPelilauta(Korttityyppi.TEKSTI, true);
        } catch (Exception ex) {
            assertEquals("Pelilaudan luonti onnistuu ilman taustasarjaa", "taustoja odotettu " + LEVEYS * KORKEUS + ", saatu 0", ex.getMessage());
        }
        assertTrue("Pelilaudan luonti onnistuu ilman taustasarjaa #2", pl.getKortit()[0][0] == null);
    }
    
    public void luontiEiOnnistuLiianPienelläTaustasarjalla() {
        luoTestikuvat();
        luoTestitaustat();
        Pelilauta pl = new Pelilauta(LEVEYS + 2, KORKEUS + 2, testikuvat, testitaustat);
        try {
            pl.luoPelilauta(Korttityyppi.TEKSTI, true);
        } catch (Exception ex) {
            assertEquals("Pelilaudan luonti onnistuu liian pienellä taustasarjalla", "taustoja odotettu " + (LEVEYS + 2) * (KORKEUS + 2) + ", saatu " + LEVEYS * KORKEUS, ex.getMessage());
        }
        assertTrue("Pelilaudan luonti onnistuu liian pienellä taustasarjalla #2", pl.getKortit()[0][0] == null);
    }

    @Test
    public void eiTaustaaAsetettu() {
//        luoTestikuvat();
//        luoTestitaustat();
        luoTestipelilauta();
        assertEquals("Korteille ei ole asetettu taustaa pelilaudan luomisen jälkeen", "*",
                (String) pelilauta.getKortti(new Point(0, 0)).getTausta().getSisalto());
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
    
    @Test
    public void getteritJaSetteritToimii() {
        luoTestipelilauta();
        pelilauta.setPelilauta(null);
        assertTrue("get/setKortit ei toimi oikein", pelilauta.getKortit() == null);
        assertEquals("getLeveys ei toimi oikein", LEVEYS, pelilauta.getLeveys());
        assertEquals("getKorkeus ei toimi oikein", KORKEUS, pelilauta.getKorkeus());
        assertEquals("getKuvasarja ei toimi oikein", (LEVEYS * KORKEUS) / 2, pelilauta.getKuvasarja().size());
        pelilauta.setKuvasarja(null);
        assertTrue("setKuvasarja ei toimi oikein", pelilauta.getKuvasarja() == null);
        assertEquals("getTaustasarja ei toimi oikein", LEVEYS * KORKEUS, pelilauta.getTaustasarja().size());
        pelilauta.setTaustasarja(null);
        assertTrue("setTaustasarja ei toimi oikein", pelilauta.getTaustasarja() == null);
    }
}
