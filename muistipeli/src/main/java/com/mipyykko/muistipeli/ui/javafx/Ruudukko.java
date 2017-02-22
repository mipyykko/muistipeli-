/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.*;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import com.mipyykko.muistipeli.malli.impl.JavaFXKortti;
import java.awt.Point;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.GridPane.setColumnIndex;
import static javafx.scene.layout.GridPane.setRowIndex;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Korttien ImageViewit sisältävä ruudukko.
 *
 * @author pyykkomi
 */
public class Ruudukko extends GridPane {

    private Pane ikkuna;
    private ImageView[][] ivRuudukko;
    private Peli peli;
    
    /**
     * Konstruktori.
     *
     * @param ikkuna Peli-ikkuna. TODO: tämän saanee parentilla myös?
     */
    public Ruudukko(Pane ikkuna, Peli peli) {
        super();
        this.ikkuna = ikkuna;
        this.peli = peli;
        setPadding(new Insets(5, 0, 5, 0));
        setVgap(4);
        setHgap(4);

        setBackground(null);
    }

    /**
     * Alustaa ruudukon ja laittaa oikeat ImageViewit oikeisiin kohtiin.
     *
     * @param pelilauta Pelilauta.
     */
    public void alustaRuudukko() {
        ivRuudukko = new ImageView[peli.getPelilauta().getLeveys()][peli.getPelilauta().getKorkeus()];

        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            for (int x = 0; x < peli.getPelilauta().getLeveys(); x++) {
                Kortti k = peli.getPelilauta().getKortti(new Point(x, y));
                ivRuudukko[x][y] = new ImageView((Image) k.getSisalto());
                sijoitaJaSkaalaaIv(ivRuudukko[x][y], x, y);
                
                add(ivRuudukko[x][y], x, y);
            }
        }
    }

    /**
     * Palauttaa ImageViewin pisteessä p.
     *
     * @param p koordinaatit Point-muodossa.
     * @return ImageView
     */
    public ImageView getIv(Point p) {
        return ivRuudukko[p.x][p.y];
    }

    /**
     * Asettaa ImageViewin pisteessä p.
     *
     * @param iv ImageView
     * @param p koordinaatit Point-muodossa.
     */
    public void setIv(ImageView iv, Point p) {
        ivRuudukko[p.x][p.y] = iv;
    }

    /**
     * Sijoittaa annetun ImageView-objektin ruudukkoon oikeaan kohtaan.
     *
     * @param iv ImageView-objekti.
     * @param x X-koordinaatti.
     * @param y Y-koordinaatti.
     */
    public void sijoitaJaSkaalaaIv(ImageView iv, int x, int y) {
        /* note to self:
         for file in *.png; do convert -resize 256x256 $file -background none -gravity center -extent 256x256 $file; done
         */
        // TODO: magic numbers, binding, centering?
        iv.setPreserveRatio(true);
        iv.fitWidthProperty().bind(ikkuna.widthProperty().divide(peli.getPelilauta().getLeveys()));
        iv.fitHeightProperty().bind(ikkuna.heightProperty().subtract(70).divide(peli.getPelilauta().getKorkeus()));
        setColumnIndex(iv, x);
        setRowIndex(iv, y);
    }

    /**
     * Animoi kortin kääntämisen.
     *
     * @param p Kortin koordinaatit.
     */
    public void kaannaKortti(Point p) {
        JavaFXKortti kortti = (JavaFXKortti) peli.getPelilauta().getKortti(p);
        ImageView ivAlku = getIv(p);
        ScaleTransition stPiilota = new ScaleTransition(Duration.millis(150), ivAlku);
        stPiilota.setFromX(1);
        stPiilota.setToX(0);

        kortti.kaanna();

        ImageView ivLoppu = new ImageView((Image) kortti.getSisalto());
        sijoitaJaSkaalaaIv(ivLoppu, p.x, p.y);
        ivLoppu.setScaleX(0);
        ScaleTransition stNayta = new ScaleTransition(Duration.millis(150), ivLoppu);
        stNayta.setFromX(0);
        stNayta.setToX(1);

        stPiilota.setOnFinished((ActionEvent t) -> {
            getChildren().remove(ivAlku);
            getChildren().add(ivLoppu);
            setIv(ivLoppu, p);
            stNayta.play();
        });
        //peli.setTila(Pelitila.ANIM_ALKU);
        stPiilota.play();
        stNayta.setOnFinished((ActionEvent t) -> {
            // TODO: pelitila anim_loppu?
            peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
            //j.oikeaKuva();
        });
    }

}
