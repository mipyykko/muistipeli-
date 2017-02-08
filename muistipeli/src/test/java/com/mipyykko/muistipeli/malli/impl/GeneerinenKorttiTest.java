/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kortti;
import com.mipyykko.muistipeli.malli.impl.GeneerinenKuva;
import com.mipyykko.muistipeli.malli.impl.GeneerinenKortti;
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
public class GeneerinenKorttiTest {
    
    private Kortti kortti;
    
    @Before
    public void setUp() {
        kortti = new GeneerinenKortti(new GeneerinenKuva("testi"), null);
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
        Kortti kortti2 = new GeneerinenKortti(new GeneerinenKuva("testi"), null);
        assertTrue("Korttien sisältö sama mutta vertailu ei toimi", kortti.equals(kortti2));
    }
}
