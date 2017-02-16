/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kortti;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
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
    
    public JavaFXKortti(Kuva kuva, Tausta tausta) {
        this.kuva = kuva;
        this.tausta = tausta;
        this.kaannetty = false;
        //TODO: hajottaa testit, tosin öö
        //setImage((Image) kuva.getSisalto());
    }

    /**
     * Asettaa kortille oikean kuvan sen mukaan onko kortti käännetty.
     */
    public void oikeaKuva() {
        setImage(kaannetty ? (Image) kuva.getSisalto() : (Image) tausta.getSisalto());
    }
    
    // näitä ei nyt käytetty
    /*public void setXY(int x, int y) {
        setX(x);
        setY(y);
    }
    
    public double getKorttiX() {
        return getX();
    }
    
    public double getKorttiY() {
        return getY();
    }*/
    
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
    public boolean kaannetty() {
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
        if (!(o instanceof JavaFXKortti)) {
            return false;
        }
        // TODO vertailu?
        return ((JavaFXKortti) o).getKuva().equals(this.kuva);
    }
    
//    @Override
//    public int hashCode() {
//        return super.hashCode();
//        //return 42 * (17 + this.kuva.toString().hashCode());
//    }
    
    
}
