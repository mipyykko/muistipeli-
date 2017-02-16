/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import com.mipyykko.muistipeli.ui.UI;
import java.awt.Point;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * JavaFX-käyttöliittymä.
 *
 * @author pyykkomi
 */
public class JavaFXUI implements UI {

    private Peli peli;
    private int ikkunaleveys, ikkunakorkeus;
    private Stage primaryStage;
    private Group kortit;
    private RuudukkoPane ruudukko; // GridPane
    private BorderPane ikkuna;
    private StatusHBox status;
    private Scene scene;
    private boolean firstrun;
    private Point[] siirto;
    private Node[] siirtoNodet;

    public JavaFXUI(Peli peli) {
        this.peli = peli;
        this.ikkunaleveys = 560; // magic numbers!
        this.ikkunakorkeus = 620;
        this.firstrun = true;
        this.siirto = new Point[2];
        this.siirtoNodet = new Node[2];
        //this.kortit = new Group();

    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    @Override
    public void setPeli(Peli peli) {
        this.peli = peli;
    }

    /**
     * Piirtää pelilaudan.
     */
    private void alusta() {
        ikkuna = new BorderPane();
        status = new StatusHBox();
        ikkuna.setTop(status);
        
        ruudukko = new RuudukkoPane(ikkuna, peli.getPelilauta());
        ruudukko.setOnMouseClicked((MouseEvent event) -> klikattuRuutua(event));
        ikkuna.setCenter(ruudukko);

        scene = new Scene(ikkuna, ikkunaleveys, ikkunakorkeus);
        scene.setFill(Color.YELLOW);

        primaryStage.setTitle("Muistipeliö");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
        
        vilautaKortteja(1); // TODO jotain
    }

    /**
     * Näytä pelilauta.
     */
    @Override
    public void nayta() {
        // TODO: jotain!
        if (firstrun) {
            firstrun = false;
            alusta();
        }
    }

    private void sulje() {
        primaryStage.close();
    }
    
    private PauseTransition odotaKortinKaantoa(Point p, int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished((ActionEvent t) -> {
            System.out.println("hello polly"); // debug
            ruudukko.kaannaKortti(ruudukko.getIvRuudukko(p), p);
        });
        return pause;
    }
    
    private void vilautaKortteja(int seconds) {
        // yhyy
    }
    
    private void odotaEnnenParinKaantoa(int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(new EventHandler<ActionEvent>() {
            Node[] nodes = siirtoNodet;
            
            @Override
            public void handle(ActionEvent t) {
                for (Node n : nodes) {
                    ruudukko.kaannaKortti(n, new Point(GridPane.getColumnIndex(n), GridPane.getRowIndex(n)));
                }
                peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
            }
        });
        pause.play();
    }

    private void paivitaScore() {
        status.setScore("Siirrot: " + peli.getSiirrotLkm() + " Parit: " + peli.getParitLkm());
    }
    
    private void hoidaSiirto(Node n, Point p) {
        ruudukko.kaannaKortti(n, p);
        if (siirto[0] != null) {
            siirto[1] = p;
            siirtoNodet[1] = n;
            if (!peli.tarkistaPari(siirto)) {
                // sekunnin tauko korttien kääntämisen jälkeen
                odotaEnnenParinKaantoa(1);
            } else {
                // pari
                peli.lisaaPari();
                if (peli.peliLoppu()) {
                    peli.setTila(Pelitila.PELI_LOPPU);
                } else {
                    peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
                }
            }
            peli.lisaaSiirto();
            paivitaScore();
            siirto = new Point[2];
            siirtoNodet = new Node[2];
        } else {
            siirto[0] = p;
            siirtoNodet[0] = n;
            peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
        }
    }
    
    private void klikattuRuutua(MouseEvent event) {

        Node n = (Node) event.getTarget();
        if (n == null || !(n instanceof GridPane) && !(n instanceof ImageView)) {
            return;
        }
        Integer x = RuudukkoPane.getColumnIndex(n), y = RuudukkoPane.getRowIndex(n);
        if (x == null || y == null) {
            return;
        }
        Point p = new Point(x, y);
        
        if (peli.getTila() != Pelitila.ODOTTAA_SIIRTOA || 
            peli.getPelilauta().getKortti(p).kaannetty()) {
            return;
        }

        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        hoidaSiirto(n, p);
        if (peli.getTila() == Pelitila.PELI_LOPPU) {
            System.out.println("lölz"); //TODO
        }
    }
}
