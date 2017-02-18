/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;
import java.awt.Point;

/**
 * UI:n interface.
 * 
 * @author pyykkomi
 */
public interface UI {
    
    /**
     * Pelin setteri.
     * @param peli Peli-objekti.
     */
    public void setPeli(Peli peli);
    /**
     * Näytä UI.
     */
    public void nayta();
}