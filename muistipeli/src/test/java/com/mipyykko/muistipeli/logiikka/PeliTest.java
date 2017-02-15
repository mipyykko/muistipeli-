/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.logiikka;

import com.mipyykko.muistipeli.malli.impl.TekstiKuva;
import com.mipyykko.muistipeli.malli.impl.TekstiTausta;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Pelilauta;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import com.mipyykko.muistipeli.ui.TestUI;
import com.mipyykko.muistipeli.ui.UI;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pyykkomi
 */
public class PeliTest {
    
    private Peli peli;
    private Pelilauta pelilauta;
    private Set<Kuva> testikuvat;
    private Set<Tausta> testitaustat;
    
    private int LEVEYS = 4, KORKEUS = 4;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    private void luoTestikuvat() {
        testikuvat = new HashSet<>();
        for (int i = 1; i <= (LEVEYS * KORKEUS) / 2; i++) {
            testikuvat.add(new TekstiKuva(Integer.toString(i)));
        }
    }
    
    private void luoTestitaustat() {
        testitaustat = new HashSet<>();
        for (int i = 1; i <= (LEVEYS * KORKEUS) / 2; i++) {
            testitaustat.add(new TekstiTausta("*"));
            testitaustat.add(new TekstiTausta("*"));
        }
    }
    
    private void luoTestipelilauta() {
        pelilauta = new Pelilauta(LEVEYS, KORKEUS, null, null);
        luoTestikuvat();
        luoTestitaustat();
        pelilauta.setKuvasarja(testikuvat);
        pelilauta.setTaustasarja(testitaustat);
        try {
            pelilauta.luoPelilauta(Korttityyppi.TEKSTI, false); // ainoa ero pelilautatestiin: ei sekoiteta
        } catch (Exception ex) {
            System.err.println("Pelilaudan luominen epäonnistui, " + ex.getMessage());
        }
    }
    
    @Before
    public void setUp() {
        peli = new Peli(null, Korttityyppi.TEKSTI);
        luoTestipelilauta();
        peli.setPelilauta(pelilauta);
    }
    
    @Test
    public void testaaUusiPeli() {
        assertTrue("Pelilaudan luomisessa jotain häikkää", peli.getPelilauta().getKortit().length > 0);
        assertTrue("Pelilaudan luomisessa häikkää", peli.getPelilauta().getKortti(new Point(0, 0)) != null);
        try {    
            peli.uusiPeli(LEVEYS, KORKEUS, testikuvat, testitaustat);
            Object o = peli.getPelilauta().getKortti(new Point(0, 0)).getKuva().getSisalto();
        } catch (Exception e) {
            fail("Pelin luomisen jälkeen ei kuvia korteissa");
        }
        assertTrue("Pelin luomisen jälkeen ei kuvia korteissa #2", peli.getPelilauta().getKortti(new Point(0,0)).getKuva() != null);
        assertTrue("Pelin luomisen jälkeen ei siirtoluetteloa", peli.getSiirrot() != null);
        Peli tPeli = new Peli(null, null);
        try {
            tPeli.uusiPeli(LEVEYS, KORKEUS, testikuvat, testitaustat);
        } catch (Exception e) {
            assertEquals("Ei virheilmoitusta ilman korttityyppiä luodessa", "Pelilaudan luominen epäonnistui, korttityyppi puuttuu", e.getMessage());
        }
        assertTrue("Pelin luominen onnistui ilman korttityyppiä", tPeli.getPelilauta().getKortti(new Point(0, 0)) == null);
        try {
            Object o = tPeli.getPelilauta().getKortti(new Point(0, 0)).getKuva().getSisalto();
            fail("Pelin luominen onnistui ilman korttityyppiä #2");
            o = tPeli.getPelilauta().getKortti(new Point(0, 0)).getTausta().getSisalto();
            fail("Pelin luominen onnistui ilman korttityyppiä #3");
        } catch (Exception e) {
            System.err.println("Jotain hämmentävää tapahtui pelitestissä");
        }
        tPeli = new Peli(null, Korttityyppi.TEKSTI);
        try {
            tPeli.uusiPeli(LEVEYS, KORKEUS, null, testitaustat);
        } catch (Exception e) {
            assertEquals("Ei virheilmoitusta ilman kuvia luodessa", 
                    "Pelilaudan luominen epäonnistui, kuvia odotettu " + (LEVEYS * KORKEUS) / 2 + ", saatu 0", e.getMessage());
        }
        assertTrue("Pelin luominen onnistui ilman kuvia", tPeli.getPelilauta().getKortti(new Point(0, 0)) == null);
        try {
            tPeli.uusiPeli(LEVEYS, KORKEUS, null, testitaustat);
            Object o = tPeli.getPelilauta().getKortti(new Point(0, 0)).getKuva().getSisalto();
            fail("Pelin luominen onnistui ilman kuvia #2");
        } catch (Exception e) {
            System.err.println("Jotain hämmentävää tapahtui pelitestissä");
        }
        tPeli = new Peli(null, Korttityyppi.TEKSTI);
        try {
            tPeli.uusiPeli(LEVEYS, KORKEUS, testikuvat, null);
        } catch (Exception e) {
            assertEquals("Ei virheilmoitusta ilman taustoja luodessa", 
                    "Pelilaudan luominen epäonnistui, taustoja odotettu " + LEVEYS * KORKEUS + ", saatu 0", e.getMessage());
        }
        assertTrue("Pelin luominen onnistui ilman taustoja", tPeli.getPelilauta().getKortti(new Point(0, 0)) == null);
        tPeli = new Peli(null, Korttityyppi.TEKSTI);
        try {
            tPeli.uusiPeli(LEVEYS, KORKEUS, testikuvat, null);
            Object o = tPeli.getPelilauta().getKortti(new Point(0, 0)).getTausta().getSisalto();
            fail("Pelin luominen onnistui ilman taustoja #2");
        } catch (Exception e) {
            System.err.println("Jotain hämmentävää tapahtui pelitestissä");
        }
    }
    
     
    @Test
    public void testaaPeli() {
        List<Point> siirrot = new ArrayList<>();
        for (int y = 0; y < KORKEUS; y++) {
            for (int x = 0; x < LEVEYS; x++) {
                siirrot.add(new Point(x, y));
            }
        }
        UI ui = new TestUI(siirrot);
        ui.setPeli(peli);
        peli.setUI(ui);
        assertEquals("Kortteja ei käännetty mutta peli on loppu", false, peli.peliLoppu());
        Iterator<Point> it = siirrot.iterator();
        while (it.hasNext()) {
            Point s[] = new Point[]{it.next(), it.next()};
            peli.kaannaPari(s);
            assertTrue("Kortit eivät käänny #1", peli.getPelilauta().getKortti(s[0]).kaannetty());
            assertTrue("Kortit eivät käänny #2", peli.getPelilauta().getKortti(s[1]).kaannetty());
            assertTrue("Parin tarkistus väärin", peli.tarkistaPari(s));
            int n = peli.getSiirrotLkm();
            peli.lisaaSiirto();
            assertEquals("Siirrot eivät kasva", n + 1, peli.getSiirrotLkm());
        }
        /// TODO, hm, tää testi ei varsinaisesti testaa muuta kuin että peli menee tosiaan läpi 
        // koska jos tuo siirtosarja ei menis läpi niin se jumisi pelaa-luuppiin...
        // assertiin ei siis ikinä edes päädytä jos näin käy.
        assertEquals("Kaikki kortit käännetty mutta peli ei loppu", true, peli.peliLoppu());
        assertEquals("Siirtojen määrä väärin", siirrot.size() / 2, peli.getSiirrotLkm());
    }
    
    @Test
    public void tarkistaPari() {
        assertTrue("Pari ei ole pari", peli.tarkistaPari(new Point[]{new Point(0, 0), new Point(1, 0)}));
        assertTrue("Ei-pari on pari", !peli.tarkistaPari(new Point[]{new Point(1, 0), new Point(2, 0)}));
        assertTrue("Virheellinen pari hyväksytty", !peli.tarkistaPari(null));
        assertTrue("Virheellinen pari hyväksytty #2", !peli.tarkistaPari(new Point[]{new Point(0,0)}));
        assertTrue("Virheellinen pari hyväksytty #3", !peli.tarkistaPari(new Point[]{
            new Point(0, 0), new Point(1, 0), new Point(2, 0)}));
    }

    @Test
    public void okSiirrot() {
        assertTrue("Null-siirto hyväksytty", !peli.okSiirto(null));
        assertTrue("Pelilaudan ulkopuolinen siirto hyväksytty #1", !peli.okSiirto(new Point(4, 3)));
        assertTrue("Pelilaudan ulkopuolinen siirto hyväksytty #2", !peli.okSiirto(new Point(3, 4)));
        assertTrue("Pelilaudan ulkopuolinen siirto hyväksytty #3", !peli.okSiirto(new Point(-1, 0)));
        assertTrue("Pelilaudan ulkopuolinen siirto hyväksytty #4", !peli.okSiirto(new Point(0, -1)));
        assertTrue("Kääntämätön kortti ei ole ok-siirto", peli.okSiirto(new Point(0, 0)));
        peli.getPelilauta().getKortti(new Point(0, 0)).kaanna();
        assertTrue("Käännetty kortti on ok-siirto", !peli.okSiirto(new Point(0, 0)));
    }
    
    @Test
    public void UIPalautuu() {
        assertTrue("getUI ei palauta nullia vaikka se puuttuu", peli.getUI() == null);
        peli.setUI(new TestUI(new ArrayList<Point>()));
        assertTrue("getUI ei palauta UIta oikein", peli.getUI() instanceof UI);
    }
    
    @Test
    public void TilaPalautuuJaMuuttuu() {
        assertEquals("Tila ei alkutilanteessa palaudu oikein", Pelitila.EI_KAYNNISSA, peli.getTila());
        peli.setTila(Pelitila.PELI_LOPPU);
        assertEquals("Tila ei palaudu muutoksen jälkeen", Pelitila.PELI_LOPPU, peli.getTila());
    }
    
}
