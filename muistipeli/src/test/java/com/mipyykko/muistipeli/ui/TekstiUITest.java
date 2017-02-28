/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.ui.teksti.TekstiUI;
import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Pelilauta;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import com.mipyykko.muistipeli.malli.impl.TekstiKuva;
import com.mipyykko.muistipeli.malli.impl.TekstiTausta;
import com.mipyykko.muistipeli.util.Point;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pyykkomi
 */
public class TekstiUITest {
    
    private TekstiUI tekstiUI;
    private Scanner lukija;
    private Peli peli;
    private Pelilauta pelilauta;
    private Set<Kuva> testikuvat;
    private Set<Tausta> testitaustat;
    private int LEVEYS = 8;
    private int KORKEUS = 8;
    
    private void luoTestikuvat() {
        testikuvat = new HashSet<>();
        for (int i = 1; i <= (LEVEYS * KORKEUS) / 2; i++) {
            testikuvat.add(new TekstiKuva(Integer.toString(i)));
        }
    }
    
    private void luoTestitaustat() {
        testitaustat = new HashSet<>();
        for (int i = 1; i <= (LEVEYS * KORKEUS) / 2; i++) {
            testitaustat.add(new TekstiTausta("*"));
            testitaustat.add(new TekstiTausta("*"));
        }
    }
    
    private void luoTestipelilauta() {
        pelilauta = new Pelilauta(LEVEYS, KORKEUS, null, null);
        luoTestikuvat();
        luoTestitaustat();
        pelilauta.setKuvasarja(testikuvat);
        pelilauta.setTaustasarja(testitaustat);
        try {
            pelilauta.luoPelilauta(Korttityyppi.TEKSTI, false); // ainoa ero pelilautatestiin: ei sekoiteta
        } catch (Exception ex) {
            System.err.println("Pelilaudan luominen epäonnistui, " + ex.getMessage());
        }
    }
    
    @Before
    public void setUp() {
        this.peli = new Peli(Korttityyppi.TEKSTI); // TODO: muuta?
        luoTestipelilauta();
        peli.setPelilauta(pelilauta);
        this.tekstiUI = new TekstiUI(peli, null);
    }
    
    @Test
    public void oikeatKoordinaatitSyote() {
        tekstiUI.setLukija(new Scanner("4 4\n"));
        Point paluuarvo = tekstiUI.kysySiirto();
        assertEquals("Oikea koordinaattisyöte, väärä paluuarvo", new Point(4,4), paluuarvo);
    }
    
    @Test
    public void liikaaKoordinaattejaSyote() {
        tekstiUI.setLukija(new Scanner("4 4 4\n"));
        Point paluuarvo = tekstiUI.kysySiirto();
        assertEquals("Liikaa numeroita syötteessä hyväksytty", null, paluuarvo);
    }
    
    @Test
    public void tyhjaSyote() {
        tekstiUI.setLukija(new Scanner("\n"));
        Point paluuarvo = tekstiUI.kysySiirto();
        assertEquals("Tyhjä syöte hyväksytty", null, paluuarvo);
    }
    
    @Test
    public void tekstiaSyotteessa() {
        tekstiUI.setLukija(new Scanner("cat\n"));
        Point paluuarvo = tekstiUI.kysySiirto();
        assertEquals("Virheellinen tekstisyöte hyväksytty", null, paluuarvo);
        tekstiUI.setLukija(new Scanner("x x\n"));
        paluuarvo = tekstiUI.kysySiirto();
        assertEquals("Virheellinen tekstisyöte #2 hyväksytty", null, paluuarvo);
    }
    
}
