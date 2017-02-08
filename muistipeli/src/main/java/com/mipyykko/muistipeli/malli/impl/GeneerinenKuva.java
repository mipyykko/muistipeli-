/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kuva;

/**
 *
 * @author pyykkomi
 */
public class GeneerinenKuva implements Kuva {

    private String teksti;
    
    public GeneerinenKuva(String teksti) {
        this.teksti = teksti;
    }

    public String getTeksti() {
        return teksti;
    }

    public void setTeksti(String teksti) {
        this.teksti = teksti;
    }

    
    @Override
    public String toString() {
        return teksti;
    }

    @Override
    public int getLeveys() {
        return 0;
    }

    @Override
    public int getKorkeus() {
        return 0;
    }

    @Override
    public Object getSisalto() {
        return teksti;
    }
    
    
    
}
