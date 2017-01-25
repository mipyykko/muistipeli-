/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;
import java.awt.geom.Point2D;
import java.util.Scanner;

/**
 *
 * @author pyykkomi
 */
public class TekstiUI extends UI {

    private Peli peli;
    private Scanner lukija;
    
    public TekstiUI(Peli peli, Scanner lukija) {
        super(peli, lukija);
        this.peli = peli;
        this.lukija = lukija;
    }

    @Override
    public void nayta() {
        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            for (int x = 0; x < peli.getPelilauta().getLeveys(); x++) {
                String s = (peli.getPelilauta().getKortit()[x][y].kaannetty()
                        ? peli.getPelilauta().getKortit()[x][y].getKuva().toString()
                        : peli.getPelilauta().getKortit()[x][y].getTausta().toString());
                System.out.format("%2s ", s);
            }
            System.out.println("");
        }
    }

    @Override
    public int[] siirto() {
        System.out.print("x y? ");
        String syote = lukija.nextLine();
        System.out.println("");
        String s[] = syote.split(" ");
        if (s.length != 2) {
            return null;
        }
        int x = Integer.parseInt(s[0]);
        int y = Integer.parseInt(s[1]);
        
        return new int[]{x, y};
    }

}
