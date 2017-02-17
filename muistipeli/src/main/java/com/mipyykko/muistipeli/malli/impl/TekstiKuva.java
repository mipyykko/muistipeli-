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

    private String key;
    
    /**
     * TekstiKuvan konstruktori.
     * 
     * @param key Kuvan nimi jonka perusteella vertailu tapahtuu.
     */
    public TekstiKuva(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public int getLeveys() {
        if (key == null) {
            return 0; 
        }
        return key.length();
    }

    @Override
    public int getKorkeus() {
        if (key == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public Object getSisalto() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TekstiKuva)) {
            return false;
        }
        return key.equals(((TekstiKuva) o).getKey());
    }
    
//    @Override
//    public int compareTo(Object o) {
//        return teksti.compareTo(o.toString());
//    }
    
    
    
}
