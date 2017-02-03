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
public class Korttitehdas {
    
    private String korttityyppi;
    
    public Korttitehdas(String korttityyppi) {
        this.korttityyppi = korttityyppi;
    }

    public Kortti uusiKortti(Object... params) {
        switch (korttityyppi) {
            case "Geneerinen":
                return new GeneerinenKortti((Kuva) params[0], (Tausta) params[1]);
            case "JavaFX":
                return new JavaFXKortti((Kuva) params[0], (Tausta) params[1]);
            default:
                return null;
        }
    }
}
