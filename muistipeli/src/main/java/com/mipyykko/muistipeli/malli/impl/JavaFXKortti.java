/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kortti;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.enums.Animaatiotila;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * JavaFX-toteutuksen Image-objektin sisältävä kortti.
 * 
 * @author pyykkomi
 */
public class JavaFXKortti extends ImageView implements Kortti {

    private Kuva kuva;
    private Tausta tausta;
    private boolean kaannetty;
    private int korttileveys, korttikorkeus;
    private Animaatiotila animTila;
    private boolean osaParia;
    
    /**
     * JavaFX-kortin konstruktori.
     * 
     * @param kuva Voi olla myös null sillä konstruktori ei oleta sisältävän mitään.
     * @param tausta Voi olla myös null sillä konstruktori ei oleta sisältävän mitään.
     */
    public JavaFXKortti(Kuva kuva, Tausta tausta) {
        this.kuva = kuva;
        this.tausta = tausta;
        this.kaannetty = false;
        this.animTila = Animaatiotila.EI_KAYNNISSA;
        this.osaParia = false;
        //TODO: hajottaa testit, tosin öö
        //setImage((Image) kuva.getSisalto());
    }

    /**
     * Asettaa kortille oikean kuvan sen mukaan onko kortti käännetty.
     */
    public void oikeaKuva() {
        setImage(kaannetty ? (Image) kuva.getSisalto() : (Image) tausta.getSisalto());
    }
    
    public int getKorttiLeveys() {
        return Math.max(kuva.getLeveys(), tausta.getLeveys());
    }
    
    public int getKorttiKorkeus() {
        return Math.max(kuva.getKorkeus(), tausta.getKorkeus());
    }
    
    /**
     * Onko kortti käännetty?
     * 
     * @return boolean-arvo
     */
    @Override
    public boolean getKaannetty() {
        return kaannetty;
    }
    
    /**
     * Kääntää kortin.
     * Kutsuu myös oikeaKuva-metodia asettaakseen kuvan oikeaksi.
     * 
     * @return boolean-arvo
     */
    @Override
    public boolean kaanna() {
        kaannetty = !kaannetty;
        //oikeaKuva(); // TODO onko tää oikein?
        return kaannetty;
    }
    
    @Override
    public Object getSisalto() {
        return (kaannetty ? kuva.getSisalto() : tausta.getSisalto());
    }
    
    @Override
    public Kuva getKuva() {
        return kuva;
    }

    @Override
    public void setKuva(Kuva kuva) {
        this.kuva = kuva;
        oikeaKuva();
    }

    @Override
    public Tausta getTausta() {
        return tausta;
    }

    @Override
    public void setTausta(Tausta tausta) {
        this.tausta = tausta;
        oikeaKuva();
    }
    
    public void setAnimTila(Animaatiotila tila) {
        this.animTila = tila;
    }
    
    public Animaatiotila getAnimTila() {
        return animTila;
    }
    
    public void setOsaParia(boolean op) {
        osaParia = op;
    }
    
    public boolean getOsaParia() {
        return osaParia;
    }
    
    @Override
    public String toString() {
        return kaannetty ? getKuva().toString() : getTausta().toString();
    }
    
    /*@Override
    public int compareTo(Object o) {
        if (!(o instanceof Kortti)) {
            return -1;
        }
        return this.kuva.compareTo(((Kortti) o).getKuva());
    }*/
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Kortti)) {
            return false;
        }
        // TODO vertailu?
        return ((Kortti) o).getKuva().equals(this.kuva);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.kuva);
        return hash;
    }
    
    
}
