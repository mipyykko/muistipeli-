package com.mipyykko.muistipeli.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mipyykko.muistipeli.util.Point;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pyykkomi
 */
public class PointTest {
    
    @Test
    public void pointTestit() {
        Point p = new Point();
        assertEquals("Point ei alustu oikein ilman parametreja", 0, p.x);
        assertEquals("Point ei alustu oikein ilman parametreja #2", 0, p.y);
        p = new Point(2, 2);
        assertEquals("Point ei alustu oikein parametrien kanssa", 2, p.x);
        assertEquals("Point ei alustu oikein parametrien kanssa #2", 2, p.y);
        assertTrue("Vertailu ei toimi oikein", new Point(2, 2).equals(p));
        assertTrue("Vertailu ei toimi oikein väärillä tyypeillä", !p.equals("kissa"));
        assertEquals("hashCode väärin", 290917, p.hashCode());
    }
}
