/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kuva;

/**
 * Geneerinen, vain tekstiä sisältävä kuva.
 * 
 * @author pyykkomi
 */
public class TekstiKuva implements Kuva {

    private String teksti;
    
    public TekstiKuva(String teksti) {
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
        return teksti.length();
    }

    @Override
    public int getKorkeus() {
        return 1;
    }

    @Override
    public Object getSisalto() {
        return teksti;
    }

    @Override
    public int compareTo(Object o) {
        return teksti.compareTo(o.toString());
    }
    
    
    
}
