/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;

/**
 *
 * @author pyykkomi
 */
public class TekstiUI implements UI {

    private Peli peli;
    
    public TekstiUI(Peli peli) {
        this.peli = peli;
    }
    
    @Override
    public void nayta() {
        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            for (int x = 0; x < peli.getPelilauta().getLeveys(); x++) {
                System.out.print(peli.getPelilauta().getKortit()[x][y].getKuva() + " ");
            }
            System.out.println("");
        }    
    }
    
}
