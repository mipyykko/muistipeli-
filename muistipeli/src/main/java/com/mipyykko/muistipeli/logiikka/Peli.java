/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.logiikka;

import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Pelilauta;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.ui.UI;
import java.awt.Point;
import java.util.Set;

/**
 * Pelin päälogiikka.
 * 
 * @author pyykkomi
 */
public class Peli {

    private Pelilauta pelilauta;
    private UI ui;
    private int siirrot;
    private String korttityyppi;
    
    public Peli(UI ui, String korttityyppi) {
        this.ui = ui;
        this.korttityyppi = korttityyppi;
    }

    /**
     * 
     * Luo uuden pelin annetuilla parametreilla.
     * 
     * @param leveys
     * @param korkeus
     * @param kuvasarja
     * @param taustasarja 
     */
    public void uusiPeli(int leveys, int korkeus, Set<Kuva> kuvasarja, Set<Tausta> taustasarja) {
        this.pelilauta = new Pelilauta(leveys, korkeus, kuvasarja, taustasarja);
        try {
            pelilauta.luoPelilauta(korttityyppi, true); // TODO: tälle jotain
        } catch (Exception ex) {
            System.err.println("Pelilaudan luominen epäonnistui, " + ex.getMessage());
        }
        this.siirrot = 0;
    }
    
    /**
     * Kasvattaa sirtojen määrää.
     */
    public void lisaaSiirto() {
        siirrot++;
    }
    
    public int getSiirrot() {
        return siirrot;
    }
    
    public void setUI(UI ui) {
        this.ui = ui;
    }

    public UI getUI() {
        return ui;
    }

    public Pelilauta getPelilauta() {
        return pelilauta;
    }

    public void setPelilauta(Pelilauta pelilauta) {
        this.pelilauta = pelilauta;
    }

    /**
     * Onko kaikki kortit käännetty eli onko peli loppu?
     * 
     * @return boolean-arvo
     */
    
    public boolean peliLoppu() {
        return pelilauta.kaikkiKaannetty();
    }

    /**
     * Onko siirto laillinen?
     * 
     * @param p siirto Point-muodossa
     * @return boolean-arvo
     */
    private boolean okSiirto(Point p) {
        return (p != null && p.x < pelilauta.getLeveys() && p.y < pelilauta.getKorkeus()
                && p.x >= 0 && p.y >= 0 && !pelilauta.getKortti(p).kaannetty());
    }

    private Point[] haeSiirtopari() {
        Point[] siirrot = new Point[2];
        for (int i = 0; i < 2; i++) {
            boolean oksiirto = false;
            while (!oksiirto) {
                ui.nayta();
                siirrot[i] = ui.siirto();
                if (okSiirto(siirrot[i])) {
                    kaannaKortti(siirrot[i]);
                    oksiirto = true;
                }
            }
        }
        return siirrot;
    }

    private boolean tarkistaPari(Point[] siirrot) {
        return pelilauta.getKortti(siirrot[0]).equals(pelilauta.getKortti(siirrot[1]));
    }
    
    private void kaannaKortti(Point p) {
        pelilauta.getKortti(p).kaanna();
    }
    
    private void kaannaPari(Point[] p) {
        kaannaKortti(p[0]);
        kaannaKortti(p[1]);
    }
    
    public void pelaa() {
        while (!peliLoppu()) {
            Point[] pari = haeSiirtopari();
            ui.nayta();
            if (!tarkistaPari(pari)) {
                kaannaPari(pari);
            }
            lisaaSiirto();
        }
    }

}
