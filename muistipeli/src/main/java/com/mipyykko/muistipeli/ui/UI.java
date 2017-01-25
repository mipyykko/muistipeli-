/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;
import java.util.Scanner;

/**
 *
 * @author pyykkomi
 */
public abstract class UI {
    
    private Scanner lukija;
    private Peli peli;
    
    public UI(Peli peli, Scanner lukija) {
        this.peli = peli;
        this.lukija = lukija;
    }
    
    public abstract void nayta();
    public abstract int[] siirto();
}
