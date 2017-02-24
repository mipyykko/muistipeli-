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

    @Test
    public void getteritJaSetteritKunnossa() {
        assertTrue("getImgKuva ei palauta Image-objektia", testikuva.getImgKuva() instanceof Image);
        testikuva.setImgKuva(null);
        assertTrue("getImgKuva ei palauta oikein asettamisen jälkeen", testikuva.getImgKuva() == null);
        assertEquals("getKey ei palauta oikein", "testikuva", testikuva.getKey());
        testikuva.setKey("abcdf");
        assertEquals("getKey ei palauta oikein asettamisen jälkeen", "abcdf", testikuva.getKey());
    } 
    
    @Test
    public void equalsToimii() {
        JavaFXKuva t = new JavaFXKuva("testikuva", null);
        assertTrue("Equals ei palauta samanlaisen kuvan kohdalla oikein", testikuva.equals(t));
        t.setKey("abcdf");
        assertTrue("Equals ei palauta erilaisen kuvan kohdalla oikein", !testikuva.equals(t));
        String s = "bläh";
        assertTrue("Equals ei palauta vääränlaisen objektin kuvalla oikein", !testikuva.equals(s));
    }
    
    @Test
    public void hashCodeToimii() {
        JavaFXKuva t = new JavaFXKuva("testikuva", null);
        assertEquals("Hashcode ei palauta oikein kun kuvat samanlaiset", t.hashCode(), testikuva.hashCode());
        assertEquals("Hashcode ei palauta oikeaa arvoa ennen muutosta", -1171139169, t.hashCode());
        t.setKey("kissa");
        assertTrue("Hashcode ei palauta oikein kun kuvat erilaiset", t.hashCode() != testikuva.hashCode());
        assertEquals("Hashcode ei palauta oikeaa arvoa muutoksen jälkeen", 102059350, t.hashCode());
    }

}
