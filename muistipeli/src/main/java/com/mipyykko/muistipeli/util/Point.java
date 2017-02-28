/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.util;

/**
 * Piste. Määritelty uudestaan koska aiemmin käytetty com.mipyykko.muistipeli.util on 
 * vain Swingissä eikä siten toimi Androidin kanssa.
 * 
 * @author pyykkomi
 */
public class Point {
    public int x;
    public int y;
    
    /** 
     * Luo uuden pisteen annetuilla x- ja y-arvoilla.
     * 
     * @param x X-koordinaatti kokonaislukuna.
     * @param y Y-koordinaatti kokonaislukuna.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Luo pisteen (0, 0).
     */
    public Point() {
        this(0, 0);
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point) || o == null) {
            return false;
        }
        Point p = (Point) o;
        return (x == p.x && y == p.y);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.x;
        hash = 701 * hash + this.y;
        return hash;
    }
}
