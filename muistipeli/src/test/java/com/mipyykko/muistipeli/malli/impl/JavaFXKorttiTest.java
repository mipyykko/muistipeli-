/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.util.TestApplication;
import javafx.application.Application;
import javafx.application.Platform;
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
public class JavaFXKorttiTest {

    private JavaFXKortti kortti;
    private static Thread thread;
    private Image testikuvaImage, testitaustaImage, isoTestikuvaImage, isoTestitaustaImage;
    private JavaFXKuva testikuva, isoTestikuva;
    private JavaFXTausta testitausta, isoTestitausta;

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
        String tt = getClass().getClassLoader().getResource("kuvat/testi2.png").toString();
        testikuvaImage = new Image(tk, 100, 100, false, false);
        isoTestikuvaImage = new Image(tk, 200, 200, false, false);
        testikuva = new JavaFXKuva("testikuva", testikuvaImage);
        isoTestikuva = new JavaFXKuva("isoTestikuva", isoTestikuvaImage);
        testitaustaImage = new Image(tt, 100, 100, false, false);
        isoTestitaustaImage = new Image(tt, 200, 200, false, false);
        testitausta = new JavaFXTausta("testitausta", testitaustaImage);
        isoTestitausta = new JavaFXTausta("isoTestitausta", isoTestitaustaImage);
        kortti = new JavaFXKortti(testikuva, testitausta);

    }

    @Test
    public void kaantyyOikein() {
        assertTrue("Kortti ei käänny oikein", kortti.kaanna());
    }

    @Test
    public void kaannettyPalauttaaOikein() {
        kortti.kaanna();
        assertTrue("Käännetty ei palauta oikein", kortti.getKaannetty());
    }

    @Test
    public void oikeaKuvaKaantamisenJalkeen() {
        kortti.setTausta(isoTestitausta);
        assertEquals("Kortin kuvaa ei aseteta oikein / tiedosto", 200, Math.round(kortti.getImage().getHeight()));
        assertEquals("Kortin kuvaa ei aseteta oikein / key", "isoTestitausta", kortti.toString());
        kortti.kaanna();
        Image tk = (Image) kortti.getSisalto();
        assertEquals("Kortin kuvaa ei aseteta oikein kääntämisen jälkeen # 2 / tiedosto", 100, Math.round(tk.getHeight()));
        assertEquals("Kortin kuvaa ei aseteta oikein kääntämisen jälkeen #2", "testikuva", kortti.toString());
    }

    @Test
    public void kortinLeveysOikeaKuva() {
        kortti.setKuva(isoTestikuva);
        assertEquals("Kortin leveys ei ole oikein / iso kuva", kortti.getKorttiLeveys(), 200);
        kortti.setKuva(testikuva);
        assertEquals("Kortin leveys ei ole oikein / normaali kuva", kortti.getKorttiLeveys(), 100);
        kortti.setTausta(isoTestitausta);
        assertEquals("Kortin leveys ei ole oikein / iso tausta", kortti.getKorttiLeveys(), 200);
        kortti.setTausta(testitausta);
        assertEquals("Kortin leveys ei ole oikein / normaali tausta", kortti.getKorttiLeveys(), 100);
    }
    
    @Test
    public void kortinKorkeusOikeaKuva() {
        kortti.setKuva(isoTestikuva);
        assertEquals("Kortin korkeus ei ole oikein / iso kuva", kortti.getKorttiKorkeus(), 200);
        kortti.setKuva(testikuva);
        assertEquals("Kortin korkeus ei ole oikein / normaali kuva", kortti.getKorttiKorkeus(), 100);
        kortti.setTausta(isoTestitausta);
        assertEquals("Kortin korkeus ei ole oikein / iso tausta", kortti.getKorttiKorkeus(), 200);
        kortti.setTausta(testitausta);
        assertEquals("Kortin korkeus ei ole oikein / normaali tausta", kortti.getKorttiKorkeus(), 100);
        
    }
    
    @Test
    public void oikeaSisalto() {
        kortti.setTausta(isoTestitausta);
        assertTrue("Kortti ei palauta oikeantyyppistä oliota", kortti.getSisalto() instanceof Image);
        Image i = (Image) kortti.getSisalto();
        assertEquals("Kortti ei palauta oikeata oliota", 200, Math.round(i.getHeight()));
        kortti.kaanna();
        i = (Image) kortti.getSisalto();
        assertTrue("Kortti ei palauta käännön jälkeen oikeantyyppistä oliota", kortti.getSisalto() instanceof Image);
        assertEquals("Kortti ei palauta käännön jälkeen oikeata oliota", 100, Math.round(i.getHeight()));
    }
    
    @Test
    public void testaaEquals() {
        JavaFXKortti kortti2 = new JavaFXKortti(testikuva, testitausta);
        assertTrue("Equals ei palauta oikein kun kortit samanlaiset", kortti.equals(kortti2));
        kortti2.setKuva(isoTestikuva);
        assertTrue("Equals ei palauta oikein kun kortit erilaiset", !kortti.equals(kortti2));
        assertTrue("Equals ei palauta oikein kun objektit erityyppiset", !kortti.equals(new String("jee")));
    }
    
    @Test
    public void setKuvaAsettaaImagen() {
        JavaFXKortti kortti2 = new JavaFXKortti(testikuva, testitausta);
        kortti2.setKuva(isoTestikuva);
        assertTrue("setKuva ei aseta kuvaa oikein", kortti2.getSisalto().equals(testitausta.getSisalto()));
        kortti2.kaanna();
        assertTrue("setKuva ei aseta kuvaa oikein", kortti2.getSisalto().equals(isoTestikuva.getSisalto()));
    
    }
    
    @AfterClass
    public static void tearDown() {
        Platform.exit();
    }
}
