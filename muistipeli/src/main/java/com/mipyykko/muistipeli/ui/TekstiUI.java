/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;
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
    
    public TekstiUI(Peli peli, Scanner lukija) {
        this.peli = peli;
        this.lukija = lukija;
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
    @Override
    public void nayta() {
        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            for (int x = 0; x < peli.getPelilauta().getLeveys(); x++) {
                System.out.format("%2s ", peli.getPelilauta().getKortit()[x][y]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    @Override
    public Point siirto() {
        System.out.format("Siirto %d, x y? ", peli.getSiirrot());
        String syote = lukija.nextLine();
        System.out.println("");
        String s[] = syote.split(" ");
        if (s.length != 2 || !s[0].matches("^\\d+$") || !s[1].matches("^\\d+$")) {
            return null;
        }
        int x = Integer.parseInt(s[0]);
        int y = Integer.parseInt(s[1]);
        
        return new Point(x, y);
    }


}
