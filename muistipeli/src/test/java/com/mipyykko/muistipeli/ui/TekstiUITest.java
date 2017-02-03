/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;
import java.awt.Point;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
    
    @Before
    public void setUp() {
        this.peli = new Peli(tekstiUI); // TODO: muuta?
        this.tekstiUI = new TekstiUI(peli, null);
    }
    
    @Test
    public void oikeatKoordinaatitSyote() {
        tekstiUI.setLukija(new Scanner("4 4\n"));
        Point paluuarvo = tekstiUI.siirto();
        assertEquals("Oikea koordinaattisyöte, väärä paluuarvo", new Point(4,4), paluuarvo);
    }
    
    @Test
    public void liikaaKoordinaattejaSyote() {
        tekstiUI.setLukija(new Scanner("4 4 4\n"));
        Point paluuarvo = tekstiUI.siirto();
        assertEquals("Liikaa numeroita syötteessä hyväksytty", null, paluuarvo);
    }
    
    @Test
    public void tyhjaSyote() {
        tekstiUI.setLukija(new Scanner("\n"));
        Point paluuarvo = tekstiUI.siirto();
        assertEquals("Tyhjä syöte hyväksytty", null, paluuarvo);
    }
    
    @Test
    public void tekstiaSyotteessa() {
        tekstiUI.setLukija(new Scanner("cat\n"));
        Point paluuarvo = tekstiUI.siirto();
        assertEquals("Virheellinen tekstisyöte hyväksytty", null, paluuarvo);
        tekstiUI.setLukija(new Scanner("x x\n"));
        paluuarvo = tekstiUI.siirto();
        assertEquals("Virheellinen tekstisyöte #2 hyväksytty", null, paluuarvo);
    }
    
}
