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
    
    // TODO: nää pitää ehkä 
    @BeforeClass
    public static void appSetup() {
        thread = new Thread() {
            @Override
            public void run() {
                Application.launch(TestApplication.class);
            }
        };
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
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
    public void kuvanMitatOikein() {
        assertEquals("Testikuvan leveys väärin", 100, Math.round(testitausta.getLeveys()));
        assertEquals("Testikuvan korkeus väärin", 100, Math.round(testitausta.getKorkeus()));
        assertEquals("Ison testikuvan leveys väärin", 200, Math.round(isoTestitausta.getLeveys()));
        assertEquals("Ison testikuvan korkeus väärin", 200, Math.round(isoTestitausta.getKorkeus()));
    }
    
    @AfterClass
    public static void tearDown() {
        thread.interrupt();
    }
}
