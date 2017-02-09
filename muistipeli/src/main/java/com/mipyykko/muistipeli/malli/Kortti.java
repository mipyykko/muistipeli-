/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.malli;

import java.util.Objects;

/**
 * Kortin interface.
 * 
 * @author pyykkomi
 */
public interface Kortti {

    public boolean kaannetty();
    
    public boolean kaanna();
    
    public Kuva getKuva();

    public void setKuva(Kuva kuva);

    public Tausta getTausta();

    public void setTausta(Tausta tausta);
    
    public int compareTo(Object o);
    
    @Override
    public int hashCode();
}
