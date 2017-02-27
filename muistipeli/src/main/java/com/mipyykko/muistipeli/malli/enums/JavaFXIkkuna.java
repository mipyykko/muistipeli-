/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli.enums;

/**
 *
 * @author pyykkomi
 */
public enum JavaFXIkkuna {
    VALIKKO("Valikko.fxml"),
    PELI("Peli.fxml"),
    TULOS("Tulos.fxml");
    
    private final String s;
    
    JavaFXIkkuna(final String s) {
        this.s = s;
    }
    
    public String tiedosto() {
        return s;
    }
}
