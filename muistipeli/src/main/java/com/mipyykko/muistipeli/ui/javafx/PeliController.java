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
import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
    
    private Point[] siirto, edellinenSiirto;

    private int animX, animKerrat;
    private Timeline naytaTimeline;
    private Timeline korttiAnimTimeline;

    private Peli peli;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        score.setFont(Font.loadFont(getClass().getResourceAsStream("/fontit/GoodDog.otf"), 24));
        peliIkkuna.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 50, 0, 0, 0);");
        ruudukkoWrapper.minWidthProperty().bind(peliIkkuna.widthProperty());
        ruudukkoWrapper.minHeightProperty().bind(peliIkkuna.heightProperty().subtract(status.heightProperty()));
    }
    
    public void alustaRuudukko() {
        peli = ikkunaController.getPeli();
        
        ruudukkoWrapper.getChildren().clear();

        peliIkkuna.prefWidthProperty().bind(ikkunaController.widthProperty());
        peliIkkuna.prefHeightProperty().bind(ikkunaController.heightProperty().subtract(status.heightProperty()));
        System.out.println(peliIkkuna.widthProperty() + " x ");
        ruudukko = new Ruudukko(ruudukkoWrapper, peli);
        ruudukko.alustaRuudukko();
        ruudukkoWrapper.getChildren().add(ruudukko);
        //pelialueStackPane.getChildren().add(ruudukkoWrapper);
        //peliIkkuna.setCenter(pelialueStackPane);
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
        peli.setTila(Pelitila.EI_KAYNNISSA);
        ruudukko.setOnMouseClicked(null);
        DropShadow ds = new DropShadow();
        ds.setColor(Color.RED);
        ds.setSpread(0.75);
        Animation a = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(ds.radiusProperty(), 0d)),
                        new KeyFrame(Duration.seconds(0.3), new KeyValue(ds.radiusProperty(), 20d)));
        GaussianBlur gb = new GaussianBlur();
        Animation b = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(gb.radiusProperty(), 0d)),
                        new KeyFrame(Duration.seconds(1), new KeyValue(gb.radiusProperty(), 10d)));
        b.setCycleCount(1);
        a.setAutoReverse(true);
        a.setCycleCount(10);
        ruudukko.setEffect(ds);
        
        TulosIkkuna t = new TulosIkkuna(peli);
        // tää jotenkin
//        sp.getChildren().add(t);
//        t.setTranslateX(-sp.getWidth());

        PauseTransition pt = new PauseTransition(Duration.seconds(2));
        TranslateTransition tt = new TranslateTransition(new Duration(350), t);
        tt.setToX(0);
        a.setOnFinished((ActionEvent ae) -> {
            ruudukko.setEffect(gb);
            b.play();
        });
        b.setOnFinished(e -> ikkunaController.asetaIkkuna(JavaFXIkkuna.TULOS));
        a.play();
    }

    private void paivitaScore() {
        score.setText("Siirrot: " + peli.getSiirrotLkm() + " Parit: " + peli.getParitLkm());
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
        animoiVoitto();

        if (peli.getTila() == Pelitila.PELI_LOPPU) {
            animoiVoitto();
        }
    }
    
    @Override
    public void asetaParent(IkkunaController ikkuna) {
        ikkunaController = ikkuna;
    }
}
