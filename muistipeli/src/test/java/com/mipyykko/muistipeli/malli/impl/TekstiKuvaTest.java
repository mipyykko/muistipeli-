/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kuva;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author pyykkomi
 */
public class TekstiKuvaTest {
    
    private Kuva kuva, kuva2, kuva3;
    
    @Before
    public void setUp() {
        kuva = new TekstiKuva("testikuva");
        kuva2 = new TekstiKuva("testikuva");
        kuva3 = new TekstiKuva("testikuva2");
    }
    
    @Test
    public void kuvaCompareTo() {
        assertEquals("Kuvien compareTo ei toimi oikein", kuva.compareTo(kuva2), 0);
        assertTrue("Kuvien compareTo ei toimi oikein kun kuvat erilaiset", kuva.compareTo(kuva3) != 0);
    }
    
}
