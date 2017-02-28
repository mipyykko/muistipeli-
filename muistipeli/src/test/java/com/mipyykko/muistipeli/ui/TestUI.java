/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.util.Point;
import java.util.Iterator;
import java.util.List;

/**
 * Testausluokka jolla voidaan syöttää pelin siirrot etukäteen.
 * 
 * @author pyykkomi
 */
public class TestUI implements UI {

    private Peli peli;
    private List<Point> syotetytSiirrot;
    private Iterator<Point> seuraavaSiirto;
    
    public TestUI(List<Point> syotetytSiirrot) {
        this.syotetytSiirrot = syotetytSiirrot;
        this.seuraavaSiirto = this.syotetytSiirrot.iterator();
    }
    
    @Override
    public void setPeli(Peli peli) {
        this.peli = peli;
    }

    @Override
    public void nayta() {
        // ei näytetä mitään
    }

    public Point siirto() {
        if (seuraavaSiirto.hasNext()) {
            return seuraavaSiirto.next();
        }
        return null;
    }
    
}
