/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.Pelilauta;
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
    
    public TekstiUITest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.tekstiUI = new TekstiUI(null, null);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void oikeatKoordinaatitSyote() {
        tekstiUI.setLukija(new Scanner("4 4\n"));
        int[] paluuarvo = tekstiUI.siirto();
        assertArrayEquals("Oikea koordinaattisyöte, väärä paluuarvo", new int[]{4, 4}, paluuarvo);
    }
    
    @Test
    public void liikaaKoordinaattejaSyote() {
        tekstiUI.setLukija(new Scanner("4 4 4\n"));
        int[] paluuarvo = tekstiUI.siirto();
        assertArrayEquals("Liikaa numeroita syötteessä hyväksytty", null, paluuarvo);
    }
    
    @Test
    public void tyhjaSyote() {
        tekstiUI.setLukija(new Scanner("\n"));
        int[] paluuarvo = tekstiUI.siirto();
        assertArrayEquals("Tyhjä syöte hyväksytty", null, paluuarvo);
    }
    
    @Test
    public void tekstiaSyotteessa() {
        tekstiUI.setLukija(new Scanner("cat\n"));
        int[] paluuarvo = tekstiUI.siirto();
        assertArrayEquals("Virheellinen tekstisyöte hyväksytty", null, paluuarvo);
    }
    
}
