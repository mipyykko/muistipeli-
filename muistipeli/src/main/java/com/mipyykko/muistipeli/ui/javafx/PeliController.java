/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.Animaatiotila;
import com.mipyykko.muistipeli.malli.enums.JavaFXIkkuna;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import com.mipyykko.muistipeli.malli.impl.JavaFXKortti;
import com.mipyykko.muistipeli.util.Point;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author pyykkomi
 */
public class PeliController implements Initializable, ControlledRuutu {

    private IkkunaController ikkunaController;
    @FXML private Text score;
    private Ruudukko ruudukko;
    @FXML private StackPane pelialueStackPane;
    @FXML private HBox ruudukkoWrapper;
    @FXML private BorderPane peliIkkuna;
    @FXML private HBox status;
    @FXML private Button valikkoButton;
    
    private Point[] siirto, edellinenSiirto;

    private int animX, animKerrat;
    private Timeline naytaTimeline;
    private Timeline korttiAnimTimeline;
    private AnimationTimer aikaTimer;
    private DoubleProperty aikaProperty;
    private BooleanProperty aikaKaynnissaProperty;
    
    private Peli peli;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        peliIkkuna.setStyle("");
        ruudukkoWrapper.minWidthProperty().bind(peliIkkuna.widthProperty());
        ruudukkoWrapper.minHeightProperty().bind(peliIkkuna.heightProperty().subtract(status.heightProperty()));
    }
    
    public void alustaRuudukko() {
        peli = ikkunaController.getPeli();
        
        ruudukkoWrapper.getChildren().clear();

        peliIkkuna.prefWidthProperty().bind(ikkunaController.widthProperty());
        peliIkkuna.prefHeightProperty().bind(ikkunaController.heightProperty().subtract(status.heightProperty()));
        ruudukko = new Ruudukko(ruudukkoWrapper, peli);
        ruudukko.alustaRuudukko();
        ruudukkoWrapper.getChildren().add(ruudukko);
        ruudukkoWrapper.setFillHeight(true);
        ruudukkoWrapper.prefHeightProperty().bind(ruudukko.heightProperty());
        ruudukkoWrapper.prefWidthProperty().bind(ruudukko.widthProperty());
        ruudukko.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        animX = 0;
        animKerrat = 0;
        if (ikkunaController.getCurTransition() != null) {
            ikkunaController.getCurTransition().setOnFinished(e -> {
                ikkunaController.setCurTransition(null);
                naytaKaikki();
        });
        } else {
            naytaKaikki();
        }
        
        valikkoButton.setOnAction(e -> {
            ruudukko.setOnMouseClicked(null);
            ikkunaController.asetaIkkuna(JavaFXIkkuna.VALIKKO);
        });
        
        aikaProperty = new SimpleDoubleProperty(0.0);
        aikaKaynnissaProperty = new SimpleBooleanProperty(Boolean.FALSE);
        paivitaScore();
        
        siirto = new Point[2];
        edellinenSiirto = new Point[2]; // onko tää käytössä?
    }

    /**
     * Käynnistää animaation, joka näyttää kortit pystyrivi kerrallaan.
     */
    public void naytaKaikki() {
        if (peli == null || peli.getPelilauta() == null) {
            return;
        }
        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        int koko = peli.getPelilauta().getKorkeus() * peli.getPelilauta().getLeveys();
        // TODO vieläkin magic numbers mutta nyt ainakin riippuu pelilaudan koosta
        double viive = 1500 * ((double) koko / 24);
        naytaTimeline = new Timeline();
        naytaTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(100), (ActionEvent event) -> kaannaPystyRivi(event, viive)));
        naytaTimeline.setCycleCount(peli.getPelilauta().getLeveys());
        naytaTimeline.setCycleCount(Animation.INDEFINITE);
        naytaTimeline.play();
    }

    private void kaannaPystyRivi(ActionEvent event, double viive) {
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
                PauseTransition p = new PauseTransition(Duration.millis(viive));
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
                alustaTimer();
                aikaTimer.start();
                //peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
            }
        }
    }

    private void alustaTimer() {
        aikaTimer = new AnimationTimer() {
            private long alku;
            
            @Override
            public void start() {
                alku = System.currentTimeMillis();
                aikaKaynnissaProperty.set(true);
                super.start();
            }
            
            @Override
            public void stop() {
                aikaKaynnissaProperty.set(false);
                super.stop();
            }
            
            @Override
            public void handle(long now) {
                long nyt = System.currentTimeMillis();
                aikaProperty.set((nyt - alku) / 1000.0);
                paivitaScore();
                peli.setAika(aikaProperty.intValue());
            }
        };
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

    private void animoiPari(Point[] siirto) {
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
        peli.setTila(Pelitila.EI_KAYNNISSA);
        ruudukko.setOnMouseClicked(null);
        DropShadow ds = new DropShadow();
        ds.setColor(Color.RED);
        ds.setSpread(0.75);
        Animation a = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(ds.radiusProperty(), 0d)),
                        new KeyFrame(Duration.seconds(0.3), new KeyValue(ds.radiusProperty(), 20d)));
        a.setAutoReverse(true);
        a.setCycleCount(10);
        ruudukko.setEffect(ds);
        
        a.setOnFinished(e -> ikkunaController.asetaIkkuna(JavaFXIkkuna.TULOS));
        a.play();
    }

    private void paivitaScore() {
        score.setText("Siirrot: "+ peli.getSiirrotLkm() + " Parit: " + peli.getParitLkm() + " Aika: " + peli.getAikaAsString());
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
        if (x == null || y == null || 
            x > peli.getPelilauta().getLeveys() || y > peli.getPelilauta().getKorkeus()) {
            return;
        }
        Point p = new Point(x, y);
        JavaFXKortti k = (JavaFXKortti) peli.getPelilauta().getKortti(p);

        if (k.getKaannetty() || k.getAnimTila() != Animaatiotila.EI_KAYNNISSA) {
            return;
        }

        hoidaSiirto(p);
        
        if (peli.getTila() == Pelitila.PELI_LOPPU) {
            aikaTimer.stop();
            animoiVoitto();
        }
    }
    
    @Override
    public void asetaParent(IkkunaController ikkuna) {
        ikkunaController = ikkuna;
    }
}
