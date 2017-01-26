/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

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
public class KorttiTest {
    
    private Kortti kortti;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        kortti = new Kortti(new GeneerinenKuva("testi"), null);
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
        Kortti kortti2 = new Kortti(new GeneerinenKuva("testi"), null);
        assertTrue("Korttien sisältö sama mutta vertailu ei toimi", kortti.equals(kortti2));
    }
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
