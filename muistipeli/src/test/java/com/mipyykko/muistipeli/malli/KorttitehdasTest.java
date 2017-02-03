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
public class KorttitehdasTest {
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void testaaVaaraParametri() {
        Korttitehdas kt = new Korttitehdas("kissa");
        assertTrue("Korttitehdas toimii väärillä parametreilla", kt.uusiKortti(null, null) == null);
    }
    
    @Test
    public void testaaOikeaParametri() {
        Korttitehdas kt = new Korttitehdas("Geneerinen");
        assertTrue("Korttitehdas toimii väärin oikeilla parametreilla", 
                    kt.uusiKortti(null, null).getClass() == GeneerinenKortti.class);
        kt = new Korttitehdas("JavaFX");
        assertTrue("Korttitehdas toimii väärin oikeilla parametreilla #2", 
                    kt.uusiKortti(null, null).getClass() == JavaFXKortti.class);
        
    }
}
