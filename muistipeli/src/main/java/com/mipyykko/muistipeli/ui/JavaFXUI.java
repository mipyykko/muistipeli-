/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import com.mipyykko.muistipeli.malli.impl.JavaFXKortti;
import java.awt.Point;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    private GridPane ruudukko;
    private BorderPane ikkuna;
    private Text score;
    private HBox status;
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
        status = new HBox();
        status.setPadding(new Insets(15, 15, 15, 15));
        status.setSpacing(10);
        status.setStyle("-fx-background-color: #800000");
        score = new Text("Siirrot: 0");
        score.setFont(Font.loadFont(getClass().getClassLoader().getResource("fontit/GoodDog.otf").toExternalForm(), 24));
        score.setFill(Color.YELLOW);
        status.getChildren().add(score);
        ikkuna.setTop(status);
        
        ruudukko = new GridPane();
        //ruudukko.prefHeightProperty().bind(ikkuna.heightProperty().subtract(status.heightProperty()));
        //ruudukko.prefWidthProperty().bind(ikkuna.widthProperty().subtract(status.widthProperty()));
        ruudukko.setPadding(new Insets(5, 0, 5, 0));
        ruudukko.setVgap(4);
        ruudukko.setHgap(4);

        ikkuna.setCenter(ruudukko);
        
        kortit = new Group();

        kortit.getChildren().clear();

        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            for (int x = 0; x < peli.getPelilauta().getLeveys(); x++) {
                JavaFXKortti k = (JavaFXKortti) peli.getPelilauta().getKortti(new Point(x, y));
                kortit.getChildren().remove(k);
                ImageView iv = new ImageView((Image) k.getSisalto());
                sijoitaJaSkaalaaIV(iv, x, y);
                ruudukko.getChildren().add(iv);
            }
        }
        ruudukko.getChildren().add(kortit);
        ruudukko.setOnMouseClicked((MouseEvent event) -> klikattuRuutua(event));
        scene = new Scene(ikkuna, ikkunaleveys, ikkunakorkeus);
        ruudukko.setBackground(null);
        scene.setFill(Color.YELLOW);

        primaryStage.setTitle("Muistipeliö");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
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

    private void sijoitaJaSkaalaaIV(ImageView iv, int x, int y) {
        /* note to self:
            for file in *.png; do convert -resize 256x256 $file -background none -gravity center -extent 256x256 $file; done
        */
        iv.setPreserveRatio(true);
        iv.fitWidthProperty().bind(ikkuna.widthProperty().divide(peli.getPelilauta().getLeveys()));
        iv.fitHeightProperty().bind(ikkuna.heightProperty().subtract(70).divide(peli.getPelilauta().getKorkeus()));
        ruudukko.setColumnIndex(iv, x);
        ruudukko.setRowIndex(iv, y);
    }

    private void kaannaKortti(Node n, int x, int y) {
        JavaFXKortti j = (JavaFXKortti) peli.getPelilauta().getKortti(new Point(x, y));
        //root.getChildren().remove(n);
        ImageView ivAlku = (ImageView) n;
        ScaleTransition stPiilota = new ScaleTransition(Duration.millis(150), ivAlku);
        stPiilota.setFromX(1);
        stPiilota.setToX(0);
        System.out.println("ennen kääntämistä " + j.toString());
        // tuleeko tähän tuplakääntö
        j.kaanna();

        ImageView ivLoppu = new ImageView((Image) j.getSisalto());
        sijoitaJaSkaalaaIV(ivLoppu, x, y);
        ivLoppu.setScaleX(0);
        ScaleTransition stNayta = new ScaleTransition(Duration.millis(150), ivLoppu);
        stNayta.setFromX(0);
        stNayta.setToX(1);

        stPiilota.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stNayta.play();
            }
        });
        ((ImageView) n).setImage((Image) ivAlku.getImage());
        //peli.setTila(Pelitila.ANIM_ALKU);
        stPiilota.play();
        ruudukko.getChildren().add(ivLoppu);
        //root.getChildren().remove(ivAlku);
        //root.getChildren().addAll(ivAlku, ivLoppu);
        stNayta.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //j.oikeaKuva();
            }
        });
    }

    private void luoTauko(int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(new EventHandler<ActionEvent>() {
            Node n0 = siirtoNodet[0], n1 = siirtoNodet[1];

            @Override
            public void handle(ActionEvent t) {
                kaannaKortti(n0, ruudukko.getColumnIndex(n0), ruudukko.getRowIndex(n0));
                kaannaKortti(n1, ruudukko.getColumnIndex(n1), ruudukko.getRowIndex(n1));
                peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
            }
        });
        pause.play();
    }

    private void hoidaSiirto(Node n, int x, int y) {
        kaannaKortti(n, x, y);
        if (siirto[0] != null) {
            siirto[1] = new Point(x, y);
            siirtoNodet[1] = n;
            if (!peli.tarkistaPari(siirto)) {
                // sekunnin tauko korttien kääntämisen jälkeen
                luoTauko(1);
            } else {
                // pari
                if (peli.peliLoppu()) {
                    peli.setTila(Pelitila.PELI_LOPPU);
                } else {
                    peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
                }
            }
            peli.lisaaSiirto();
            score.setText("Siirrot: " + peli.getSiirrotLkm());
            siirto = new Point[2];
            siirtoNodet = new Node[2];
        } else {
            siirto[0] = new Point(x, y);
            siirtoNodet[0] = n;
            peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
        }
    }
    
    private void klikattuRuutua(MouseEvent event) {

        Node n = (Node) event.getTarget();
        if (n == null || !(n instanceof GridPane) && !(n instanceof ImageView)) {
            return;
        }
        Integer ex = ruudukko.getColumnIndex(n), ey = ruudukko.getRowIndex(n);
        if (ex == null || ey == null) {
            return;
        }
        if (peli.getTila() != Pelitila.ODOTTAA_SIIRTOA || 
            peli.getPelilauta().getKortti(new Point(ex, ey)).kaannetty()) {
            return;
        }

        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        hoidaSiirto(n, ex, ey);
        if (peli.getTila() == Pelitila.PELI_LOPPU) {
            System.out.println("lölz"); //TODO
        }
    }
}
