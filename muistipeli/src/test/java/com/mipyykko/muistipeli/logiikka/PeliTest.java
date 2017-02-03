/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.logiikka;

import com.mipyykko.muistipeli.malli.GeneerinenKuva;
import com.mipyykko.muistipeli.malli.GeneerinenTausta;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Pelilauta;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.ui.TestUI;
import com.mipyykko.muistipeli.ui.UI;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pyykkomi
 */
public class PeliTest {
    
    private Peli peli;
    private Pelilauta pelilauta;
    private Set<Kuva> testikuvat;
    private Set<Tausta> testitaustat;
    
    private int LEVEYS = 4, KORKEUS = 4;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    private void luoTestikuvat() {
        testikuvat = new HashSet<>();
        for (int i = 1; i <= (LEVEYS * KORKEUS) / 2; i++) {
            testikuvat.add(new GeneerinenKuva(Integer.toString(i)));
        }
    }
    
    private void luoTestitaustat() {
        testitaustat = new HashSet<>();
        for (int i = 1; i <= (LEVEYS * KORKEUS) / 2; i++) {
            testitaustat.add(new GeneerinenTausta("*"));
            testitaustat.add(new GeneerinenTausta("*"));
        }
    }
    
    private void luoTestipelilauta() {
        pelilauta = new Pelilauta(LEVEYS, KORKEUS, null, null);
        luoTestikuvat();
        luoTestitaustat();
        pelilauta.setKuvasarja(testikuvat);
        pelilauta.setTaustasarja(testitaustat);
        try {
            pelilauta.luoPelilauta("Geneerinen", false); // ainoa ero pelilautatestiin: ei sekoiteta
        } catch (Exception ex) {
            System.err.println("Pelilaudan luominen epäonnistui");
        }
    }
    
    @Before
    public void setUp() {
        peli = new Peli(null);
        luoTestipelilauta();
        peli.setPelilauta(pelilauta);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testaaPeli() {
        List<Point> siirrot = new ArrayList<>();
        for (int y = 0; y < KORKEUS; y++) {
            for (int x = 0; x < LEVEYS; x++) {
                siirrot.add(new Point(x, y));
            }
        }
        UI ui = new TestUI(siirrot);
        ui.setPeli(peli);
        peli.setUI(ui);
        peli.pelaa();
        /// TODO, hm, tää testi ei varsinaisesti testaa muuta kuin että peli menee tosiaan läpi 
        // koska jos tuo siirtosarja ei menis läpi niin se jumisi pelaa-luuppiin...
        // assertiin ei siis ikinä edes päädytä jos näin käy.
        assertEquals("Kaikki kortit käännetty mutta peli ei loppu", true, peli.peliLoppu());
    }
    
}
