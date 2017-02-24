/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

/**
 * Kortin interface.
 * 
 * @author pyykkomi
 */
public interface Kortti {

    /**
     * Onko kortti käännetty?
     * @return boolean-arvo
     */
    public boolean getKaannetty();
    
    /**
     * Kääntää kortin.
     * @return boolean-arvo
     */
    public boolean kaanna();
    
    /** Onko kortti osa paria?
     * 
     * @return  boolean-arvo
     */
    public boolean getOsaParia();
    
    /**
     * Asetetaan kortille osaParia-boolean.
     * 
     * @param arvo Haluttu boolean-arvo.
     */
    public void setOsaParia(boolean arvo);
    /**
     * Kuva-getteri.
     * 
     * @return Kuva-objekti.
     */
    public Kuva getKuva();

    /**
     * Kuva-setteri.
     * @param kuva Kuva.
     */
    public void setKuva(Kuva kuva);

    /**
     * Tausta-getteri.
     * 
     * @return Tausta-objekti.
     */
    public Tausta getTausta();

    /**
     * Tausta-setteri.
     * @param tausta Tausta.
     */
    public void setTausta(Tausta tausta);
    
    /**
     * Palauttaa sisällön objektina. Oikea tyypitys on saajan vastuulla.
     * @return Object.
     */
    public Object getSisalto();
    
    /**
     * Equals joka siis vertailee kuvan keyn perusteella.
     * 
     * @param o vertailtava objekti
     * @return boolean
     */
    @Override
    public boolean equals(Object o);
    
    @Override
    public int hashCode();
}
