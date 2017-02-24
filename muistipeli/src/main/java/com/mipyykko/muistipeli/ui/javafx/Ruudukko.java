/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.Animaatiotila;
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
import javafx.scene.layout.HBox;
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
    private HBox[][] hbRuudukko;
    private Peli peli;

    /**
     * Konstruktori.
     *
     * @param ikkuna Peli-ikkuna. TODO: tämän saanee parentilla myös?
     * @param peli Peli-objekti.
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
     */
    public void alustaRuudukko() {
        ivRuudukko = new ImageView[peli.getPelilauta().getLeveys()][peli.getPelilauta().getKorkeus()];
        hbRuudukko = new HBox[peli.getPelilauta().getLeveys()][peli.getPelilauta().getKorkeus()];
        
        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            for (int x = 0; x < peli.getPelilauta().getLeveys(); x++) {
                JavaFXKortti k = (JavaFXKortti) peli.getPelilauta().getKortti(new Point(x, y));
                k.setOsaParia(false);
                ivRuudukko[x][y] = new ImageView((Image) k.getSisalto());
                hbRuudukko[x][y] = new HBox();
                hbRuudukko[x][y].getChildren().add(ivRuudukko[x][y]);
                sijoitaJaSkaalaaIv(ivRuudukko[x][y], x, y);
                add(/*iv*/hbRuudukko[x][y], x, y);
            }
        }
    }

    /**
     * Merkataan pari ja animaatiot päättyneiksi.
     * .
     * @param siirto Point[] joka sisältää siirrot.
     */
    public void merkkaaPari(Point[] siirto) {
        for (Point p : siirto) {
            JavaFXKortti k = (JavaFXKortti) peli.getPelilauta().getKortti(p);
            k.setAnimTila(Animaatiotila.EI_KAYNNISSA);
            k.setOsaParia(true);
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
     * Palauttaa HBoxin pisteessä p.
     *
     * @param p koordinaatit Point-muodossa.
     * @return HBox
     */
    public HBox getHB(Point p) {
        return hbRuudukko[p.x][p.y];
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
    
//    public void setHB(HBox hb, Point p) {
//        hbRuudukko[p.x][p.y] = hb;
//    }
//
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
    
    private int[] korttejaAnimOdotusTilassa() {
        int[] animOdotus = new int[2];
        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            for (int x = 0; x < peli.getPelilauta().getLeveys(); x++) {
                Animaatiotila a = ((JavaFXKortti) peli.getPelilauta().getKortti(new Point(x, y))).getAnimTila();
                animOdotus[0] += (a == Animaatiotila.KAANTYMASSA_AUKI
                        || a == Animaatiotila.KAANTYMASSA_KIINNI) ? 1 : 0;
                animOdotus[1] += (a == Animaatiotila.ODOTTAA) ? 1 : 0;
            }
        }
        return animOdotus;
    }

    /**
     * Animoi kortin kääntämisen.
     *
     * @param p Kortin koordinaatit.
     * @param kaikki Ollaanko kääntämässä kaikkia kortteja vai sallitaanko vain
     * kaksi auki samaan aikaan?
     */
    public void kaannaKortti(Point p, boolean kaikki) {
//        if (animaatioLkm > 2 && !kaikki) {
//            return;
//        }
        JavaFXKortti kortti = (JavaFXKortti) peli.getPelilauta().getKortti(p);
        if (kortti.getAnimTila() != Animaatiotila.EI_KAYNNISSA
                && kortti.getAnimTila() != Animaatiotila.ODOTTAA) {
            return;
        }
        peli.setTila(Pelitila.ANIM_KAYNNISSA);
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
            getHB(p).getChildren().remove(ivAlku);
            getHB(p).getChildren().add(ivLoppu);
            setIv(ivLoppu, p);
            stNayta.play();
        });
        if (kortti.getKaannetty()) {
            kortti.setAnimTila(Animaatiotila.KAANTYMASSA_AUKI);
        } else {
            kortti.setAnimTila(Animaatiotila.KAANTYMASSA_KIINNI);
        }
        stPiilota.play();
        stNayta.setOnFinished((ActionEvent t) -> {
            if (kortti.getKaannetty() && !kortti.getOsaParia()) {
                kortti.setAnimTila(Animaatiotila.ODOTTAA);
            } else {
                kortti.setAnimTila(Animaatiotila.EI_KAYNNISSA);
            }
            int[] ao = korttejaAnimOdotusTilassa();
            if (ao[0] == 0 && ao[1] < 2) {
                peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
            }
        });
    }
    
}
