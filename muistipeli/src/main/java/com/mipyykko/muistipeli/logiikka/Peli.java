/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.logiikka;

import com.mipyykko.muistipeli.malli.Kortti;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Pelilauta;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.ui.UI;
import java.util.Set;

/**
 *
 * @author pyykkomi
 */
public class Peli {
    
    private Pelilauta pelilauta;
    private UI ui;
    
    public Peli(int leveys, int korkeus, Set<Kuva> kuvasarja, Set<Tausta> taustasarja) {
        this.pelilauta = new Pelilauta(leveys, korkeus, kuvasarja, taustasarja);
        this.ui = null;
        
        pelilauta.luoPelilauta();
    }

    public void setUI(UI ui) {
        this.ui = ui;
        ui.setPeli(this);
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

    public boolean peliLoppu() {
        return pelilauta.kaikkiKaannetty();
    }
    
    public boolean okSiirto(int[] siirto) {
        return (siirto != null && siirto[0] < pelilauta.getLeveys() && siirto[1] < pelilauta.getKorkeus() &&
                !pelilauta.getKortti(siirto).kaannetty());
    }
    
    public void pelaa() {
        while (!peliLoppu()) {
            Kortti[] valitut = new Kortti[2];
            int[][] siirrot = new int[2][];
            for (int i = 0; i < 2; i++) {
              boolean oksiirto = false;
              while (!oksiirto) {
                ui.nayta();
                siirrot[i] = ui.siirto();
                if (okSiirto(siirrot[i])) {
                    pelilauta.getKortti(siirrot[i]).kaanna();
                    oksiirto = true;
                }
              }
              valitut[i] = pelilauta.getKortti(siirrot[i]);
            }
            ui.nayta();
            if (!valitut[0].equals(valitut[1])) {
                pelilauta.getKortti(siirrot[0]).kaanna();
                pelilauta.getKortti(siirrot[1]).kaanna();
            }
        }
    }

}
