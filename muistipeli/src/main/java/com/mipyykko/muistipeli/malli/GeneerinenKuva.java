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
public class GeneerinenKuva implements Kuva {

    private String teksti;
    
    public GeneerinenKuva(String teksti) {
        this.teksti = teksti;
    }

    public String getTeksti() {
        return teksti;
    }

    public void setTeksti(String teksti) {
        this.teksti = teksti;
    }

    @Override
    public Object getSisalto() {
        return this;
        //TODO hmmhmh
    }
    
    @Override
    public String toString() {
        return teksti;
    }

    @Override
    public void setSisalto(Object o) {
        //TODO and to think over
    }
    
    
    
}
