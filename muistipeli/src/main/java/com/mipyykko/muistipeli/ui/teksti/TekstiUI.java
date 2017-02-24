/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.teksti;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import com.mipyykko.muistipeli.ui.UI;
import java.awt.Point;
import java.util.Scanner;

/**
 * Tekstikäyttöliittymä.
 *
 * @author pyykkomi
 */
public class TekstiUI implements UI {

    private Peli peli;
    private Scanner lukija;
    private Point[] siirto;

    /**
     * TekstiUI:n konstruktori.
     * 
     * @param peli Peli-objekti.
     * @param lukija Scanner-objekti
     */
    public TekstiUI(Peli peli, Scanner lukija) {
        this.peli = peli;
        this.lukija = lukija;
        this.siirto = new Point[2];
    }

    public void setLukija(Scanner lukija) {
        this.lukija = lukija;
    }

    @Override
    public void setPeli(Peli peli) {
        this.peli = peli;
    }

    /**
     * Näytä pelilauta.
     */
    public void naytaPelilauta() {
        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            for (int x = 0; x < peli.getPelilauta().getLeveys(); x++) {
                System.out.format("%2s ", peli.getPelilauta().getKortit()[x][y]);
            }
            System.out.println("");
        }
        System.out.format("Siirtoja %d, pareja %d%n", peli.getSiirrotLkm(), peli.getParitLkm());

    }

    /**
     * Tulosta annettu merkkijono muotoiltuna ja kysy käyttäjältä syöte.
     * 
     * @param s Tulostettava merkkijono.
     * @param params n määrä parametreja format-komennolle.
     * @return saatu syöte
     */
    public String kysy(String s, Object... params) {
        System.out.format(s, params);
        String syote = lukija.nextLine();
        return syote;
    }
    
    /**
     * Kysy käyttäjältä siirto.
     * 
     * @return Koordinaatit Point-muodossa.
     */
    public Point kysySiirto() {
        peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
        Point p = new Point();
        do {
            naytaPelilauta();
            String syote = kysy("Koordinaatit x y? ", peli.getSiirrotLkm());
            System.out.println("");
            String s[] = syote.split(" ");
            if (s.length != 2 || !s[0].matches("^\\d+$") || !s[1].matches("^\\d+$")) {
                return null;
            }
            int x = Integer.parseInt(s[0]);
            int y = Integer.parseInt(s[1]);

            p = new Point(x, y);
        } while (!peli.okSiirto(p));

        peli.setTila(Pelitila.UI_ODOTUS);
        return p;
    }

    @Override
    public void nayta() {
        while (peli.getTila() != Pelitila.PELI_LOPPU) {
            for (int i = 0; i < 2; i++) {
                siirto[i] = kysySiirto();
                peli.getPelilauta().getKortti(siirto[i]).kaanna();
            }
            
            if (!peli.tarkistaPari(siirto)) {
                naytaPelilauta();
                peli.kaannaKortit(siirto);
            }
            if (peli.peliLoppu()) {
                peli.setTila(Pelitila.PELI_LOPPU);
            }
        }
    }

}