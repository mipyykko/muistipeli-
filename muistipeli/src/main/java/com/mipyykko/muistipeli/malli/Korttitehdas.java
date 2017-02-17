/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import com.mipyykko.muistipeli.malli.impl.TekstiKortti;
import com.mipyykko.muistipeli.malli.impl.JavaFXKortti;
/**
 * Korttitehdas.
 * 
 * @author pyykkomi
 */
public class Korttitehdas {
    
    private final Korttityyppi korttityyppi;

    /**
    * Konstruktorille annetaan tehtaan haluttu korttityyppi.
    * 
    */
    public Korttitehdas(Korttityyppi korttityyppi) {
        this.korttityyppi = korttityyppi;
    }

    /**
     * Uuden korttityypin kortin luova ja palauttava metodi.
     * 
     * @param params Vaihteleva määrä kortin konstruktorille annettavia parametreja
     * @return korttityypin mukainen Kortti tai null jos korttityyppi on väärä
     */
    public Kortti uusiKortti(Object... params) {
        if (korttityyppi == Korttityyppi.TEKSTI) {
            return new TekstiKortti((Kuva) params[0], (Tausta) params[1]);
        } else if (korttityyppi == Korttityyppi.JAVAFX) {
            return new JavaFXKortti((Kuva) params[0], (Tausta) params[1]);
        }
        return null;
    }
}
