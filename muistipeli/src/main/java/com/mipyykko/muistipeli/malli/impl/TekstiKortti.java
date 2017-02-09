/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kortti;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;

/**
 * Tekstimuotoinen kortti.
 * 
 * @author pyykkomi
 */
public class TekstiKortti implements Kortti {

    private Kuva kuva;
    private Tausta tausta;
    private boolean kaannetty;
    
    public TekstiKortti(Kuva kuva, Tausta tausta) {
        this.kuva = kuva;
        this.tausta = tausta;
        this.kaannetty = false;
    }

    /**
     * Onko kortti käännetty?
     * 
     * @return boolean-arvo
     */
    @Override
    public boolean kaannetty() {
        return kaannetty;
    }
    
    /**
     * Kääntää kortin.
     * 
     * @return boolean-arvo
     */
    @Override
    public boolean kaanna() {
        kaannetty = !kaannetty; 
        return kaannetty;
    }
    
    @Override
    public Kuva getKuva() {
        return kuva;
    }

    @Override
    public void setKuva(Kuva kuva) {
        this.kuva = kuva;
    }

    @Override
    public Tausta getTausta() {
        return tausta;
    }

    @Override
    public void setTausta(Tausta tausta) {
        this.tausta = tausta;
    }
    
    @Override
    public String toString() {
        return kaannetty ? getKuva().toString() : getTausta().toString();
    }
    
    @Override
    public int compareTo(Object o) {
        return this.kuva.toString().compareTo(((Kortti) o).getKuva().toString());
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Kortti)) {
            return false;
        }
        Kortti k = (Kortti) o;
        return this.kuva.toString().equals(k.getKuva().toString());
        // tän vois kyllä miettiä 
    }
    
    @Override
    public int hashCode() {
        return 42 * (17 + this.kuva.toString().hashCode());
    }

}
