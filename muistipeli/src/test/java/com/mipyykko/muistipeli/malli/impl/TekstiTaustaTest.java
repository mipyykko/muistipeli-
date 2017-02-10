/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author pyykkomi
 */
public class TekstiTaustaTest {
    
    TekstiTausta testitausta, testitausta2, testitausta3;
    
    @Before
    public void setUp() {
        testitausta = new TekstiTausta("testitausta");
        testitausta2 = new TekstiTausta("testitausta2");
        testitausta3 = new TekstiTausta("testitausta3");
    }
    
    @Test
    public void oikeatMitat() {
        testitausta2.setTeksti(null);
        assertEquals("Leveys väärin", 11, testitausta.getLeveys());
        assertEquals("Tekstittömän taustan leveys väärin", 0, testitausta2.getLeveys());
        assertEquals("Korkeus väärin", 1, testitausta.getKorkeus());
        assertEquals("Tekstittömän taustan korkeus väärin", 0, testitausta2.getKorkeus());
    }
}
