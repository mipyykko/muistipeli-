/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kortti;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pyykkomi
 */
public class TekstiKorttiTest {
    
    private Kortti kortti, kortti2, kortti3;
    
    @Before
    public void setUp() {
        kortti = new TekstiKortti(new TekstiKuva("testikuva"), new TekstiTausta("testitausta"));
        kortti2 = new TekstiKortti(new TekstiKuva("testikuva"), new TekstiTausta("testitausta"));
        kortti3 = new TekstiKortti(new TekstiKuva("testikuva2"), new TekstiTausta("testitausta2"));
    }
    
    @Test
    public void kaannaKortti() {
        kortti.kaanna();
        assertTrue("Kääntämätön kortti ei käänny", kortti.kaannetty());
    }
    
    @Test
    public void kaannaKaannettyKortti() {
        kortti.kaanna();
        kortti.kaanna();
        assertTrue("Käännetty kortti ei käänny", !kortti.kaannetty());
    }
    
    @Test
    public void kortitSamanlaiset() {
        assertTrue("Korttien sisältö sama mutta vertailu ei toimi", kortti.equals(kortti2));
    }
    
    @Test
    public void korttiKaantyy() {
        assertTrue("Kortti ei käänny", kortti.kaanna() == true);
        assertTrue("Kortti ei käänny toisen kerran", kortti.kaanna() == false);
    }
    
    @Test
    public void korttiPalauttaaOikeanToStringin() {
        assertEquals("Kortti ei palatua oikeaa toStringiä", kortti.toString(), "testitausta");
    }
    
    @Test
    public void korttiKaantyyJaPalauttaaOikeanToStringin() {
        kortti.kaanna();
        assertEquals("Kortti ei kääntämisen jälkeen palauta oikeaa toStringiä", kortti.toString(), "testikuva");
        kortti.kaanna();
        assertEquals("Kortti ei toisen kääntämisen jälkeen palauta oikeaa toStringiä", kortti.toString(), "testitausta");
    }
    
    @Test
    public void korttiCompareTo() {
        assertEquals("Korttien compareTo ei palauta oikein", kortti.compareTo(kortti2), 0);
        assertTrue("Korttien compareTo ei palauta oikein kun kortit erilaiset", kortti.compareTo(kortti3) != 0);
    }
    
    @Test
    public void korttiHashCode() {
        assertEquals("Korttien hashCode ei palauta oikein", kortti.hashCode(), kortti2.hashCode());
        assertNotEquals("Korttien hashCode ei palauta oikein kun kortit erilaiset", kortti.hashCode(), kortti3.hashCode());
    }
}
