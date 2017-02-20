/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.*;
import com.mipyykko.muistipeli.malli.*;
import com.mipyykko.muistipeli.malli.enums.*;
import com.mipyykko.muistipeli.ui.UI;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
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
    private GridPane alkuIkkuna;
    private StackPane paaIkkuna;
    private RuudukkoPane ruudukko; 
    private BorderPane peliIkkuna;
    private Valikko valikko;
    private StatusHBox status;
    private Scene scene;
    private boolean firstrun;
    private Point[] siirto;
    private Node[] siirtoNodet;

    /**
     * JavaFXUI:n konstruktori.
     * 
     */
    public JavaFXUI() {
        this.peli = null;
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
    private void alusta() throws Exception {
        paaIkkuna = new StackPane();
        paaIkkuna.setBackground(null);
        alkuIkkuna = new GridPane();
        alkuIkkuna.setAlignment(Pos.CENTER);
        alkuIkkuna.setPadding(new Insets(15));
        alkuIkkuna.setHgap(16);
        alkuIkkuna.setVgap(8);
        
        valikko = new Valikko();
        valikko.getAloitusnappi().setOnAction((ActionEvent event) -> {
            try {
                aloitusNappiKlikattu(event);
            } catch (Exception ex) {
                System.err.println("Jotain tapahtui aloitusnappia painettassa :x " + ex.getMessage()); // DEBUG
            }
        });
       
        alkuIkkuna.add(valikko.getSisalto(), 1, 1);
        alkuIkkuna.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
        peliIkkuna = new BorderPane();
        peliIkkuna.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));

        status = new StatusHBox();
        peliIkkuna.setTop(status);
        
        ruudukko = new RuudukkoPane(peliIkkuna/*paaIkkuna*/);
        ruudukko.setOnMouseClicked((MouseEvent event) -> klikattuRuutua(event));
        peliIkkuna.setCenter(ruudukko);
        
        RuudukkoPane.setHalignment(ruudukko, HPos.CENTER);
        if (peli != null) {
            peli.setTila(Pelitila.VALIKKO);
        } 
        
        //paaIkkuna.getChildren().addAll(alkuIkkuna, peliIkkuna);
        
        scene = new Scene(alkuIkkuna/*paaIkkuna*/, ikkunaleveys, ikkunakorkeus);
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
    public void nayta() throws Exception {
        // TODO: jotain!
        if (firstrun) {
            firstrun = false;
            try { 
                alusta();
            } catch (Exception e) {
                throw new Exception("UI:n alustus ei onnistunut: " + e.getMessage());
            }
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
        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(new EventHandler<ActionEvent>() {
            Node[] nodes = siirtoNodet;
            
            @Override
            public void handle(ActionEvent t) {
                for (Node n : nodes) {
                    ruudukko.kaannaKortti(n, 
                            new Point(RuudukkoPane.getColumnIndex(n), RuudukkoPane.getRowIndex(n)));
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
        if (peli.getTila() != Pelitila.ANIM_KAYNNISSA) {
            return;
        }
        ruudukko.kaannaKortti(n, p);
        if (siirto[0] != null) {
            siirto[1] = p;
            siirtoNodet[1] = n;
            if (!peli.tarkistaPari(siirto)) {
                // sekunnin tauko korttien kääntämisen jälkeen
                odotaEnnenParinKaantoa(1);
            }
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

        if (peli.getTila() == Pelitila.ANIM_KAYNNISSA) {
            return;
        }
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
            peli.getPelilauta().getKortti(p).getKaannetty()) {
            return;
        }

        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        hoidaSiirto(n, p);
        if (peli.getTila() == Pelitila.PELI_LOPPU) {
            System.out.println("lölz"); //TODO
        }
    }

    private void aloitusNappiKlikattu(ActionEvent event) throws Exception {
        if (peli == null || peli.getTila() == Pelitila.VALIKKO) {
            int leveys = 4;
            int korkeus = 5;
            JavaFXInit jfi = new JavaFXInit();
            String setti = valikko.getKuvavalikkoValittu();
            if (setti == null || setti.isEmpty()) {
                return;
            }
            jfi.lueKuvalista(setti);
            jfi.lueTaustalista("debug");
            Set<Kuva> kuvat = jfi.luoKuvat(leveys, korkeus);
            Set<Tausta> taustat = jfi.luoTaustat(leveys, korkeus);
            // debug pelin käynnistys?
            peli = new Peli(Korttityyppi.JAVAFX);
            try {
                peli.uusiPeli(leveys, korkeus, kuvat, taustat);
                ruudukko.alustaRuudukko(peli.getPelilauta());
                //peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
                primaryStage.setScene(new Scene(peliIkkuna, ikkunaleveys, ikkunakorkeus));
//                alkuIkkuna.setVisible(false);
//                peliIkkuna.setVisible(true);
            } catch (Exception e) {
                System.out.println("Pelin luominen epäonnistui! " + e.getMessage()); // debug
            }
        }
    }
}
