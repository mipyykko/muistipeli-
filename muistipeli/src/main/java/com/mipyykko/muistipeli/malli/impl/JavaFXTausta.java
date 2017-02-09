/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.impl;

import com.mipyykko.muistipeli.malli.Tausta;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * JavaFX-toteutuksen Image-objektin sisältävä tausta.
 * 
 * @author pyykkomi
 */
public class JavaFXTausta extends ImageView implements Tausta {

    private Image imgTausta;
    private String key;
    
    public JavaFXTausta(String key, Image imgTausta) {
        this.key = key;
        this.imgTausta = imgTausta;
    }

    public Image getImgTausta() {
        return imgTausta;
    }

    public void setImgTausta(Image imgTausta) {
        this.imgTausta = imgTausta;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    
    @Override
    public Object getSisalto() {
        return imgTausta;
    }

    @Override
    public int getKorkeus() {
        return (int) imgTausta.getHeight();
    }

    @Override
    public int getLeveys() {
        return (int) imgTausta.getWidth();
    }
    
    @Override
    public String toString() {
        return key;
    }
}
