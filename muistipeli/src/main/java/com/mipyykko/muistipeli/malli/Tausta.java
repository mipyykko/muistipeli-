/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

/**
 * Taustan interface.
 * 
 * @author pyykkomi
 */
public interface Tausta {

    /**
     * Korkeus-getteri.
     * @return int-arvo
     */
    public int getKorkeus();
    /**
     * Leveys-getteri.
     * @return int-arvo
     */
    public int getLeveys();
    /**
     * Sisällön getteri. Palauttaa objektin, tyypitys saajan vastuulla.
     * @return objekti
     */ 
    public Object getSisalto();
}
