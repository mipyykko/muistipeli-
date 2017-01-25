/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

/**
 *
 * @author pyykkomi
 */
public class Kortti implements Comparable<Kuva> {
    private Kuva kuva;
    private Tausta tausta;
    private boolean kaannetty;
    
    public Kortti(Kuva kuva, Tausta tausta) {
        this.kuva = kuva;
        this.tausta = tausta;
        this.kaannetty = false;
    }

    public boolean kaannetty() {
        return kaannetty;
    }
    
    public boolean kaanna() {
        kaannetty = kaannetty ? false : true;
        return kaannetty;
    }
    
    public Kuva getKuva() {
        return kuva;
    }

    public void setKuva(Kuva kuva) {
        this.kuva = kuva;
    }

    public Tausta getTausta() {
        return tausta;
    }

    public void setTausta(Tausta tausta) {
        this.tausta = tausta;
    }
    
    @Override
    public int compareTo(Kuva o) {
        //TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
