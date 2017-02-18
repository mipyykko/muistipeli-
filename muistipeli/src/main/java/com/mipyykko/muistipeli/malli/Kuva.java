/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

/**
 * Kuvan interface.
 * 
 * @author pyykkomi
 */
public interface Kuva {
    
    /**
     * Kuvan leveys-getteri.
     * @return int-arvo
     */
    public int getLeveys();
    /**
     * Kuvan korkeus-getteri.
     * @return int-arvo
     */
    public int getKorkeus();
    /**
     * Kuvan sisällön getteri Object-muodossa. Tyypitys saajan vastuulla.
     * @return objekti
     */
    public Object getSisalto();
    
    /**
     * Kuvan avaimen getteri.
     * @return key
     */
    public String getKey();
    
    @Override
    public boolean equals(Object o);
    //public int compareTo(Object o);
    
    
}
