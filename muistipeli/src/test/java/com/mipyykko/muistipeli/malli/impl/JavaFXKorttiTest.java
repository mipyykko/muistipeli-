/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kortti;
import com.mipyykko.muistipeli.util.TestingApplication;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pyykkomi
 */
public class JavaFXKorttiTest {

    class TestImageView extends ImageView {

        private Object image;

        public TestImageView(Image i) {
            image = i;
        }

        public void setImage(String url) {
            image = url;
        }
    }

    private Kortti kortti;
    private static Thread thread;

    /*
        JavaFX ei anna luoda Imageja ennen applikaation käynnistämistä joten
        testauskaan ei onnistu. Luodaan siis mock-application joka pyörii 
        testien ajan.
    */
    
    @BeforeClass
    public static void appSetup() {
        thread = new Thread() {
            @Override
            public void run() {
                Application.launch(TestingApplication.class);
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
        kortti = new JavaFXKortti(new JavaFXKuva("testikuva",
                new Image(tk)),
                new JavaFXTausta("testitausta",
                        new Image(tk)));
    }

    @Test
    public void kaantyyOikein() {
        assertTrue("Kortti ei käänny oikein", kortti.kaanna());
    }

    @Test
    public void kaannettyPalauttaaOikein() {
        kortti.kaanna();
        assertTrue("Käännetty ei palauta oikein", kortti.kaannetty());
    }

    @AfterClass
    public void tearDown() {
        thread.interrupt();
    }
}
