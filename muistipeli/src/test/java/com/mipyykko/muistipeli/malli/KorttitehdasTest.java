/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import com.mipyykko.muistipeli.malli.impl.TekstiKortti;
import com.mipyykko.muistipeli.malli.impl.JavaFXKortti;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pyykkomi
 */
public class KorttitehdasTest {
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void testaaVaaraParametri() {
        Korttitehdas kt = new Korttitehdas(null);
        assertTrue("Korttitehdas toimii väärillä parametreilla", kt.uusiKortti(null, null) == null);
    }
    
    @Test
    public void testaaOikeaParametri() {
        Korttitehdas kt = new Korttitehdas(Korttityyppi.TEKSTI);
        assertTrue("Korttitehdas toimii väärin oikeilla parametreilla", 
                    kt.uusiKortti(null, null).getClass() == TekstiKortti.class);
        kt = new Korttitehdas(Korttityyppi.JAVAFX);
        assertTrue("Korttitehdas toimii väärin oikeilla parametreilla #2", 
                    kt.uusiKortti(null, null).getClass() == JavaFXKortti.class);
        kt = new Korttitehdas(null);
        assertTrue("Korttitehdas toimii väärin väärillä parametreilla",
                kt.uusiKortti(null, null) == null);
        
    }
}
