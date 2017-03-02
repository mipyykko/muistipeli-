/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.util.TestApplication;
import javafx.application.Application;
import javafx.scene.image.Image;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pyykkomi
 */
public class JavaFXTaustaTest {
    
    private static Thread thread;
    private JavaFXTausta testitausta, isoTestitausta;
    private Image testitaustaImage, isoTestitaustaImage;
    
    @BeforeClass
    public static void appSetup() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Application.launch(TestApplication.class);
                } catch (Exception e) {
                    // jo käynnissä
                }    
            }
        };
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(300);
        } catch (Exception e) {
        }
        thread.interrupt();
    }
    
    @Before
    public void setUp() {
        String tt = getClass().getClassLoader().getResource("kuvat/testi2.png").toString();
        testitaustaImage = new Image(tt, 100, 100, false, false);
        isoTestitaustaImage = new Image(tt, 200, 200, false, false);
        testitausta = new JavaFXTausta("testitausta", testitaustaImage);
        isoTestitausta = new JavaFXTausta("testitausta", isoTestitaustaImage);
    }
    
    @Test
    public void taustanMitatOikein() {
        assertEquals("Testitaustan leveys väärin", 100, Math.round(testitausta.getLeveys()));
        assertEquals("Testitaustan korkeus väärin", 100, Math.round(testitausta.getKorkeus()));
        assertEquals("Ison testitaustan leveys väärin", 200, Math.round(isoTestitausta.getLeveys()));
        assertEquals("Ison testitaustan korkeus väärin", 200, Math.round(isoTestitausta.getKorkeus()));
    }
    
    @Test
    public void getteritJaSetteritKunnossa() {
        assertTrue("getImgTausta ei palauta Image-objektia", testitausta.getImgTausta() instanceof Image);
        testitausta.setImgTausta(null);
        assertTrue("getImgTausta ei palauta oikein asettamisen jälkeen", testitausta.getImgTausta() == null);
        assertEquals("getKey ei palauta oikein", "testitausta", testitausta.getKey());
        testitausta.setKey("abcdf");
        assertEquals("getKey ei palauta oikein asettamisen jälkeen", "abcdf", testitausta.getKey());
    } 

    @AfterClass
    public static void tearDown() {
        thread.interrupt();
    }
}
