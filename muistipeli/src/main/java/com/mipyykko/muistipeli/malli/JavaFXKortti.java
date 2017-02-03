/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

import javafx.scene.image.ImageView;

/**
 *
 * @author pyykkomi
 */
public class JavaFXKortti extends ImageView implements Kortti, Comparable<Kortti> {

    private Kuva kuva;
    private Tausta tausta;
    private boolean kaannetty;
    private int korttileveys, korttikorkeus;
    
    public JavaFXKortti(Kuva kuva, Tausta tausta) {
        this.kuva = kuva;
        this.tausta = tausta;
        this.kaannetty = false;
    }

    public void setXY(int x, int y) {
        setX(x);
        setY(y);
    }
    
    public double getKorttiX() {
        return getX();
    }
    
    public double getKorttiY() {
        return getY();
    }
    
    public int getKorttiLeveys() {
        return korttileveys;
    }
    
    public int getKorttiKorkeus() {
        return korttikorkeus;
    }
    
    @Override
    public boolean kaannetty() {
        return kaannetty;
    }
    
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
    public int compareTo(Kortti k) {
        return this.kuva.toString().compareTo(k.getKuva().toString());
    }
    
    @Override
    public boolean equals(Object o) {
        Kortti k = (Kortti) o;
        return this.kuva.toString().equals(k.getKuva().toString());
        // tän vois kyllä miettiä 
    }
    
    @Override
    public int hashCode() {
        return 42 * (17 + this.kuva.toString().hashCode());
    }
    
    
}
