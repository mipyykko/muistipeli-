/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.logiikka;

import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Pelilauta;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import com.mipyykko.muistipeli.util.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Pelin päälogiikka.
 * 
 * @author pyykkomi
 */
public class Peli {

    private Pelilauta pelilauta;
    private int siirrotLkm;
    private int paritLkm;
    private List<Point> siirrot;
    private final Korttityyppi korttityyppi;
    private Pelitila tila;
    
    /**
     * Pelin konstruktori.
     * @param korttityyppi Käytettävä korttityyppi. 
     */
    public Peli(Korttityyppi korttityyppi) {
        this.siirrot = new ArrayList<>();
        this.korttityyppi = korttityyppi;
        this.tila = Pelitila.EI_KAYNNISSA;
    }

    /**
     * 
     * Luo uuden pelin annetuilla parametreilla.
     * 
     * @param leveys Pelilaudan leveys.
     * @param korkeus Pelilaudan korkeus.
     * @param kuvasarja Set Kuva-objekteja.
     * @param taustasarja Set Kuva-objekteja.
     * @throws Exception Virhe pelilaudan luonnissa palauttaa virheilmoituksen selityksen kera.
     */
    public void uusiPeli(int leveys, int korkeus, Set<Kuva> kuvasarja, Set<Tausta> taustasarja) throws Exception {
        // TODO: pelilaudan oikean koon tarkistus
        tila = Pelitila.INIT;
        this.pelilauta = new Pelilauta(leveys, korkeus, kuvasarja, taustasarja);
        try {
            pelilauta.luoPelilauta(korttityyppi, true); // TODO: tälle jotain
        } catch (Exception ex) {
            throw new Exception("Pelilaudan luominen epäonnistui, " + ex.getMessage());
        }
        this.siirrot = new ArrayList<>(); // TODO: tämä esim. käyttöön
        this.siirrotLkm = 0;
        this.paritLkm = 0;
        tila = Pelitila.ODOTTAA_SIIRTOA;
    }
    
    /**
     * Luo uuden pelin nykyisillä parametreilla.
     * 
     * @throws Exception 
     */
    public void uusiPeli() throws Exception {
        if (pelilauta != null) {
            uusiPeli(pelilauta.getLeveys(), pelilauta.getKorkeus(),
                 pelilauta.getKuvasarja(), pelilauta.getTaustasarja());
        } else {
            throw new Exception("Uusi peli vanhoilla parametreilla ei onnistu");
        }
    }
    
    /**
     * Kasvattaa siirtojen määrää.
     */
    public void lisaaSiirto() {
        siirrotLkm++;
    }
    
    public int getSiirrotLkm() {
        return siirrotLkm;
    }
    
    /**
     * Kasvattaa parien määrää ja merkitsee parin.
     
     * @param pari Korttien koordinaatit Point-muodossa.    
     */
    public void lisaaPari(Point[] pari) {
        paritLkm++;
        for (Point p : pari) {
            pelilauta.getKortti(p).setOsaParia(true);
        }
        
    }
    
    public int getParitLkm() {
        return paritLkm;
    }
    
    public List<Point> getSiirrot() {
        return siirrot;
    }
    
    public void setTila(Pelitila tila) {
        this.tila = tila;
    }
    
    public Pelitila getTila() {
        return tila;
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
    public boolean okSiirto(Point p) {
        return (p != null && p.x < pelilauta.getLeveys() && p.y < pelilauta.getKorkeus()
                && p.x >= 0 && p.y >= 0 && !pelilauta.getKortti(p).getKaannetty());
    }

    /**
     * Tarkistaa onko annetuissa koordinaateissa korttipari ja asettaa pelitilan.
     * 
     * 
     * @param siirrot Kaksi Point-objektia sisältävä taulukko.
     * @return boolean-arvo
     */
    public boolean tarkistaPari(Point[] siirrot) {
        // TODO turhan isoja himmeleitä nämä ehtolauseet
        if (siirrot == null || siirrot.length != 2 || siirrot[0] == null || siirrot[1] == null ||
            siirrot[0].equals(siirrot[1])) {
            return false;
        }
        
        lisaaSiirto();
        boolean pari = pelilauta.getKortti(siirrot[0]).equals(pelilauta.getKortti(siirrot[1]));
        if (pari) {
            lisaaPari(siirrot);
            if (peliLoppu()) {
                setTila(Pelitila.PELI_LOPPU);
            } else {
                setTila(Pelitila.ODOTTAA_SIIRTOA);
            }
        } else {
            setTila(Pelitila.ODOTTAA_SIIRTOA);
        }
        return pari;
    }
    
    /**
     * Kääntää annetuissa koordinaateissa olevan kortin.
     * 
     * @param p Point-objekti.
     */
    private void kaannaKortti(Point p) {
        pelilauta.getKortti(p).kaanna();
    }
    
    /**
     * Kääntää annetussa taulukossa olevat kortit.
     * 
     * @param p Point[]-objekti. 
     */
    public void kaannaKortit(Point[] p) {
        for (Point k : p) {
            kaannaKortti(k);
        }
    }
}
