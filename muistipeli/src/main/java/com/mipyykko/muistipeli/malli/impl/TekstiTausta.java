/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Tausta;

/**
 * Geneerinen, vain tekstiä sisältävä tausta.
 * 
 * @author pyykkomi
 */
public class TekstiTausta implements Tausta {

    private String teksti;
    
    public TekstiTausta(String teksti) {
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
    public int getKorkeus() {
        return 1;
    }

    @Override
    public int getLeveys() {
        return teksti.length();
    }

    @Override
    public Object getSisalto() {
        return teksti;
    }
    
}
