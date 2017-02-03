/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author pyykkomi
 */
public class JavaFXTausta extends ImageView implements Tausta {

    private Image imgTausta;
    
    public JavaFXTausta(Image imgTausta) {
        this.imgTausta = imgTausta;
    }
    
    @Override
    public Object getSisalto() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSisalto(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
