/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.JavaFXMain;
import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.Animaatiotila;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import com.mipyykko.muistipeli.malli.impl.JavaFXKortti;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import javafx.animation.*;
import javafx.event.*;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Peli-ikkuna joka sisältää ruudukon ja yläreunan statusbarin.
 *
 * @author pyykkomi
 */
public class PeliIkkuna extends BorderPane {

    private StatusHBox status;
    private Ruudukko ruudukko;
    private StackPane sp;
    private HBox ruudukkoWrapper;
    private Peli peli;
    private Point[] siirto, edellinenSiirto;
    private int animX = 0, animKerrat = 0;
    private Timeline naytaTimeline;
    private Timeline korttiAnimTimeline;

    /**
     * Konstruktori.
     *
     * @param peli Saa parametrina peli-objektin.
     */
    public PeliIkkuna(Peli peli) {
        super();
        this.peli = peli;
        setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));

        status = new StatusHBox();
        setTop(status);

        sp = new StackPane();
        ruudukkoWrapper = new HBox();
        ruudukko = new Ruudukko(this, peli);
        ruudukko.alustaRuudukko();
        ruudukkoWrapper.getChildren().add(ruudukko);
        ruudukkoWrapper.setAlignment(Pos.CENTER);
        sp.getChildren().add(ruudukkoWrapper);

        setCenter(sp/*ruudukkoWrapper*/);

        Ruudukko.setHalignment(ruudukko, HPos.CENTER);

        this.siirto = new Point[2];
        this.edellinenSiirto = new Point[2];
        naytaKaikki();
    }

    public void setPeli(Peli peli) {
        this.peli = peli;
    }

    public Ruudukko getRuudukko() {
        return ruudukko;
    }

    /**
     * Käynnistää animaation, joka näyttää kortit pystyrivi kerrallaan.
     */
    public void naytaKaikki() {
        if (peli == null || peli.getPelilauta() == null) {
            return;
        }
        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        naytaTimeline = new Timeline();
        naytaTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(100), (ActionEvent event) -> kaannaPystyRivi(event)));
        naytaTimeline.setCycleCount(peli.getPelilauta().getLeveys());
        naytaTimeline.setCycleCount(Animation.INDEFINITE);
        naytaTimeline.play();
        // debug: pelitilan seuranta
//        Timeline tmpTimeline = new Timeline();
//        tmpTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(10),
//                new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent ae) {
//                int[] ao = ruudukko.korttejaAnimOdotusTilassa();
//                status.setScore(peli.getTila().toString() + " anim: " + ao[0] + " odotus: " + ao[1]);
//            }
//        }));
//        tmpTimeline.setCycleCount(Animation.INDEFINITE);
//        tmpTimeline.play();

    }

    private void kaannaPystyRivi(ActionEvent event) {
        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            ruudukko.kaannaKortti(new Point(animX, y), true);
        }
        animX++;
        if (animX >= peli.getPelilauta().getLeveys()) {
            if (animKerrat < 1) {
                animKerrat++;
                naytaTimeline.stop();
                /* ensimmäisen kääntökerran jälkeen odotetaan
                 jonka jälkeen käännetään uudelleen, sen jälkeen voidaan asettaa
                 pelitila
                 */
                // TODO: magic numbers
                PauseTransition p = new PauseTransition(Duration.seconds(1));
                p.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        naytaKaikki();
                    }
                });
                p.play();
                animX = 0;
            } else {
                naytaTimeline.stop();
                naytaTimeline = null;
                ruudukko.setOnMouseClicked((MouseEvent e) -> klikattuRuutua(e));
                //peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
            }
        }
    }

    private void odotaEnnenParinKaantoa(int seconds) {
        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                for (Point p : edellinenSiirto) {
                    ruudukko.kaannaKortti(p, false);
                }
                siirto = new Point[2];
            }
        });
        pause.play();
    }

    // TODO: esim. tää toimimaan
    private void animoiPari(Point[] siirto) {
        /* tähän vaihtui nyt hbox sisältämään iv:n eli jos sen sais tekemään jotain */
        final Timeline[] a = new Timeline[siirto.length];

        for (int i = 0; i < siirto.length; i++) {
            final int idx = i;
            Glow g = new Glow();
            DropShadow ds = new DropShadow();
            ds.setColor(Color.RED);
            ds.setSpread(0.75);
            a[i] = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(ds.radiusProperty(), 0d)),
                    new KeyFrame(Duration.seconds(0.45), new KeyValue(ds.radiusProperty(), 20d)));
            a[i].setAutoReverse(true);
            a[i].setCycleCount(2);
            ruudukko.getHB(siirto[idx]).setEffect(ds);
            a[i].setOnFinished(e -> ruudukko.getHB(siirto[idx]).setEffect(null));
            a[i].play();
        }
    }

    private void animoiVoitto() {
        DropShadow ds = new DropShadow();
        ds.setColor(Color.RED);
        ds.setSpread(0.75);
        Animation a = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(ds.radiusProperty(), 0d)),
                        new KeyFrame(Duration.seconds(0.3), new KeyValue(ds.radiusProperty(), 20d)));
        a.setAutoReverse(true);
        a.setCycleCount(10);
        ruudukko.setEffect(ds);
        a.setOnFinished(e -> ruudukko.setEffect(null));
        
        TulosIkkuna t = new TulosIkkuna(peli);
        sp.getChildren().add(t);
        t.setTranslateX(-sp.getWidth());

        PauseTransition pt = new PauseTransition(Duration.seconds(2));
        TranslateTransition tt = new TranslateTransition(new Duration(350), t);
        tt.setToX(0);
        a.setOnFinished(e -> tt.play());
        a.play();
    }

    private void paivitaScore() {
        status.setScore("Siirrot: " + peli.getSiirrotLkm() + " Parit: " + peli.getParitLkm());
    }

    private void hoidaSiirto(Point p) {
        if (peli.getTila() != Pelitila.ODOTTAA_SIIRTOA) {
            return;
        }
        if (siirto[0] != null && siirto[0] != p) {
            ruudukko.kaannaKortti(p, false);
            siirto[1] = p;
            if (!peli.tarkistaPari(siirto)) {
                // sekunnin tauko korttien kääntämisen jälkeen
                odotaEnnenParinKaantoa(1);
            } else {
                ruudukko.merkkaaPari(siirto);
                animoiPari(siirto);
                siirto = new Point[2];
            }
            paivitaScore();
            edellinenSiirto = siirto;

        } else if (siirto[0] == null && siirto[1] == null) {
            ruudukko.kaannaKortti(p, false);
            siirto[0] = p;
        }
    }

    private void klikattuRuutua(MouseEvent event) {
        if (peli == null || peli.getTila() != Pelitila.ODOTTAA_SIIRTOA) {
            return;
        }
        Node n = (Node) event.getTarget();
        if (n == null || !(n instanceof GridPane) && !(n instanceof ImageView)) {
            return;
        }
        Integer x = Ruudukko.getColumnIndex(n), y = Ruudukko.getRowIndex(n);
        if (x == null || y == null) {
            return;
        }
        Point p = new Point(x, y);
        JavaFXKortti k = (JavaFXKortti) peli.getPelilauta().getKortti(p);

        if (k.getKaannetty() || k.getAnimTila() != Animaatiotila.EI_KAYNNISSA) {
            return;
        }

        hoidaSiirto(p);

        if (peli.getTila() == Pelitila.PELI_LOPPU) {
            animoiVoitto();
        }
    }

}
