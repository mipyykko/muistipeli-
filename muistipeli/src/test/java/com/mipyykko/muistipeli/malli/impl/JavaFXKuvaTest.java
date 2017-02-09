/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.util.TestApplication;
import javafx.application.Application;
import javafx.scene.image.Image;
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
public class JavaFXKuvaTest {
    
    private Image testikuvaImage, isoTestikuvaImage;
    private JavaFXKuva testikuva, isoTestikuva;
    private static Thread thread;
    
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
        String tk = getClass().getClassLoader().getResource("kuvat/testi.png").toString();
        testikuvaImage = new Image(tk, 100, 100, false, false);
        isoTestikuvaImage = new Image(tk, 200, 200, false, false);
        testikuva = new JavaFXKuva("testikuva", testikuvaImage);
        isoTestikuva = new JavaFXKuva("isotestikuva", isoTestikuvaImage);

    }
    
    @After
    public void tearDown() {
        thread.interrupt();
    }

    @Test
    public void kuvanMitatOikein() {
        assertEquals("Testikuvan leveys väärin", 100, Math.round(testikuva.getLeveys()));
        assertEquals("Testikuvan korkeus väärin", 100, Math.round(testikuva.getKorkeus()));
        assertEquals("Ison testikuvan leveys väärin", 200, Math.round(isoTestikuva.getLeveys()));
        assertEquals("Ison testikuvan korkeus väärin", 200, Math.round(isoTestikuva.getKorkeus()));
    }
}
