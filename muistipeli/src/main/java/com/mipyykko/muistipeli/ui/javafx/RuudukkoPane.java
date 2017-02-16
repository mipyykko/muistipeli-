/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.malli.Kortti;
import com.mipyykko.muistipeli.malli.Pelilauta;
import com.mipyykko.muistipeli.malli.impl.JavaFXKortti;
import java.awt.Point;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author pyykkomi
 */
public class RuudukkoPane extends GridPane {

    private Pane ikkuna;
    private ImageView[][] ivRuudukko;
    private Pelilauta pelilauta;
    
    public RuudukkoPane(Pane ikkuna, Pelilauta pelilauta) {
        super();
        this.ikkuna = ikkuna;
        this.pelilauta = pelilauta;
        setPadding(new Insets(5, 0, 5, 0));
        setVgap(4);
        setHgap(4);
        
        ivRuudukko = new ImageView[pelilauta.getLeveys()][pelilauta.getKorkeus()];
        
        for (int y = 0; y < pelilauta.getKorkeus(); y++) {
            for (int x = 0; x < pelilauta.getLeveys(); x++) {
                Kortti k = pelilauta.getKortti(new Point(x, y));
                ivRuudukko[x][y] = new ImageView((Image) k.getSisalto());
                sijoitaJaSkaalaaIV(ivRuudukko[x][y], x, y);
                getChildren().add(ivRuudukko[x][y]);
            }
        }
        setBackground(null);
    }
    
    public ImageView getIvRuudukko(Point p) {
        return ivRuudukko[p.x][p.y];
    }
    
    public void setIvRuudukko(ImageView iv, Point p) {
        ivRuudukko[p.x][p.y] = iv;
    }
    
    public void sijoitaJaSkaalaaIV(ImageView iv, int x, int y) {
        /* note to self:
            for file in *.png; do convert -resize 256x256 $file -background none -gravity center -extent 256x256 $file; done
        */
        // TODO: magic numbers!
        iv.setPreserveRatio(true);
        iv.fitWidthProperty().bind(ikkuna.widthProperty().divide(pelilauta.getLeveys()));
        iv.fitHeightProperty().bind(ikkuna.heightProperty().subtract(70).divide(pelilauta.getKorkeus()));
        setColumnIndex(iv, x);
        setRowIndex(iv, y);
    }

    public void kaannaKortti(Node n, Point p) {
        JavaFXKortti j = (JavaFXKortti) pelilauta.getKortti(p);
        ImageView ivAlku = getIvRuudukko(p);
        ScaleTransition stPiilota = new ScaleTransition(Duration.millis(150), ivAlku);
        stPiilota.setFromX(1);
        stPiilota.setToX(0);

        j.kaanna();

        ImageView ivLoppu = new ImageView((Image) j.getSisalto());
        sijoitaJaSkaalaaIV(ivLoppu, p.x, p.y);
        ivLoppu.setScaleX(0);
        ScaleTransition stNayta = new ScaleTransition(Duration.millis(150), ivLoppu);
        stNayta.setFromX(0);
        stNayta.setToX(1);

        stPiilota.setOnFinished((ActionEvent t) -> {
            getChildren().remove(ivAlku);
            getChildren().add(ivLoppu);
            setIvRuudukko(ivLoppu, p);
            stNayta.play();
        });
        //peli.setTila(Pelitila.ANIM_ALKU);
        stPiilota.play();
        stNayta.setOnFinished((ActionEvent t) -> {
            //j.oikeaKuva();
        });
    }

}

