/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Kuva;
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
    public int compareTo(Object o) {
        if (!(o instanceof JavaFXKuva)) {
            return -1;
        }
        return key.compareTo(((JavaFXKuva) o).getKey());
    }
}
