/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import com.mipyykko.muistipeli.ui.UI;
import java.awt.Point;
import java.util.Set;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
    private StatusHBox status;
    private Scene scene;
    private boolean firstrun;
    private Point[] siirto;
    private Node[] siirtoNodet;

    /**
     * JavaFXUI:n konstruktori.
     * 
     * @param peli Peli-objekti. Voi olla tässä vaiheessa vielä null.
     */
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
        paaIkkuna = new StackPane();
        paaIkkuna.setBackground(null);
        alkuIkkuna = new GridPane();
        alkuIkkuna.setAlignment(Pos.CENTER);
        alkuIkkuna.setPadding(new Insets(15));
        alkuIkkuna.setHgap(16);
        alkuIkkuna.setVgap(8);
        
        GridPane valikko = new GridPane();
        Text otsikko = new Text("Muistipeliö");
        otsikko.setFont(Font.loadFont(getClass().getClassLoader().getResourceAsStream("fontit/GoodDog.otf"), 48));
        otsikko.setTextAlignment(TextAlignment.CENTER);
        Button aloitusNappi = new Button("Aloita peli");
        aloitusNappi.setAlignment(Pos.CENTER);
        aloitusNappi.setOnAction((ActionEvent event) -> aloitusNappiKlikattu(event));
        valikko.add(otsikko, 1, 0);
        GridPane.setHalignment(otsikko, HPos.CENTER);
        GridPane.setHalignment(aloitusNappi, HPos.CENTER);
        valikko.add(aloitusNappi, 1, 1);
        alkuIkkuna.add(valikko, 1, 1);
        peliIkkuna = new BorderPane();
        status = new StatusHBox();
        peliIkkuna.setTop(status);
        
        ruudukko = new RuudukkoPane(paaIkkuna, peli.getPelilauta());
        ruudukko.setOnMouseClicked((MouseEvent event) -> klikattuRuutua(event));
        peliIkkuna.setCenter(ruudukko);
        peliIkkuna.setVisible(false);
        peli.setTila(Pelitila.VALIKKO); 
        
        paaIkkuna.getChildren().addAll(alkuIkkuna, peliIkkuna);
        
        scene = new Scene(paaIkkuna, ikkunaleveys, ikkunakorkeus);
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

    private void aloitusNappiKlikattu(ActionEvent event) {
        System.out.println("kää");
        if (peli.getTila() == Pelitila.VALIKKO) {
            int leveys = peli.getPelilauta().getLeveys();
            int korkeus = peli.getPelilauta().getKorkeus();
            Set<Kuva> kuvat = peli.getPelilauta().getKuvasarja();
            Set<Tausta> taustat = peli.getPelilauta().getTaustasarja();
            peli = new Peli(Korttityyppi.JAVAFX);
            try {
                peli.uusiPeli(leveys, korkeus, kuvat, taustat);
                alkuIkkuna.setVisible(false);
                peliIkkuna.setVisible(true);
            } catch (Exception e) {
                System.out.println("ääk!"); // debug
            }
        }
    }
}
