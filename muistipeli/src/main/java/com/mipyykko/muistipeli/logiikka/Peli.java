/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.logiikka;

import com.mipyykko.muistipeli.malli.Kortti;
import com.mipyykko.muistipeli.malli.Pelilauta;

/**
 *
 * @author pyykkomi
 */
public class Peli {
    
    private Pelilauta pelilauta;
    
    public Peli(Pelilauta pelilauta) {
        this.pelilauta = pelilauta;
    }

    public Pelilauta getPelilauta() {
        return pelilauta;
    }

    public void setPelilauta(Pelilauta pelilauta) {
        this.pelilauta = pelilauta;
    }

}
