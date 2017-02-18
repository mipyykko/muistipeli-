/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kuva;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * JavaFX-toteutuksen Image-objektin sisältävä kuva.
 * 
 * @author pyykkomi
 */
public class JavaFXKuva extends ImageView implements Kuva {

    private String key;
    private Image imgKuva;

    /**
     * JavaFX-kuvan konstruktori.
     * 
     * @param key Kuvan nimi, jonka perusteella vertailut tehdään.
     * @param imgKuva Image-objekti. Voi olla myös null luontivaiheessa.
     */
    public JavaFXKuva(String key, Image imgKuva) {
        this.key = key; 
        this.imgKuva = imgKuva;
    }

    public Image getImgKuva() {
        return imgKuva;
    }

    public void setImgKuva(Image imgKuva) {
        this.imgKuva = imgKuva;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
   
    @Override
    public Object getSisalto() {
        return imgKuva;
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public int getLeveys() {
        return (int) imgKuva.getWidth();
    }

    @Override
    public int getKorkeus() {
        return (int) imgKuva.getHeight();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Kuva)) {
            return false;
        }
        return key.equals(((Kuva) o).getKey());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.key);
        return hash;
    }
}
