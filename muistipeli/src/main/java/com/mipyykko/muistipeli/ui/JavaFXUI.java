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
    private GridPane root;
    private Scene scene;
    private boolean firstrun;
    private Point[] siirto;
    private Node[] siirtoNodet;

    public JavaFXUI(Peli peli) {
        this.peli = peli;
        this.ikkunaleveys = 800;
        this.ikkunakorkeus = 600;
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
        root = new GridPane();
        root.setPadding(new Insets(5, 0, 5, 0));
        root.setVgap(4);
        root.setHgap(4);

        kortit = new Group();

        kortit.getChildren().clear();

        // TODO: tän pitäis ehkä olla vaan jossain first run-jutussa
        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            for (int x = 0; x < peli.getPelilauta().getLeveys(); x++) {
                JavaFXKortti k = (JavaFXKortti) peli.getPelilauta().getKortti(new Point(x, y));
                //k.oikeaKuva();
                kortit.getChildren().remove(k);
                //k.setXY(x * (k.getKorttiLeveys() + 20), y * (k.getKorttiKorkeus() + 20));
                ImageView iv = new ImageView((Image) k.getSisalto());
                // TODO: skaalautuvuus kuntoon
                sijoitaJaSkaalaaIV(iv, x, y);
                root.getChildren().add(iv);
            }
        }
        root.getChildren().add(kortit);
        root.setOnMouseClicked((MouseEvent event) -> klikattuRuutua(event));
        scene = new Scene(root, ikkunaleveys, ikkunakorkeus);
        root.setBackground(null);
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

    private void keskitaKuva(ImageView iv) {
        Image img = iv.getImage();
        double w = 0;
        double h = 0;

        double ratioX = iv.getFitWidth() / img.getWidth();
        double ratioY = iv.getFitHeight() / img.getHeight();

        double reducCoeff = 0;
        if (ratioX >= ratioY) {
            reducCoeff = ratioY;
        } else {
            reducCoeff = ratioX;
        }

        w = img.getWidth() * reducCoeff;
        h = img.getHeight() * reducCoeff;

        iv.setX((iv.getFitWidth() - w) / 2);
        iv.setY((iv.getFitHeight() - h) / 2);
    }

    private void sijoitaJaSkaalaaIV(ImageView iv, int x, int y) {
        //iv.setPreserveRatio(true);
        keskitaKuva(iv);
        iv.fitWidthProperty().bind(root.widthProperty().divide(peli.getPelilauta().getLeveys()));
        iv.fitHeightProperty().bind(root.heightProperty().divide(peli.getPelilauta().getKorkeus()));
        root.setColumnIndex(iv, x);
        root.setRowIndex(iv, y);
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
        root.getChildren().add(ivLoppu);
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
                kaannaKortti(n0, root.getColumnIndex(n0), root.getRowIndex(n0));
                kaannaKortti(n1, root.getColumnIndex(n1), root.getRowIndex(n1));
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
            siirto = new Point[2];
            siirtoNodet = new Node[2];
        } else {
            siirto[0] = new Point(x, y);
            siirtoNodet[0] = n;
            peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
        }
        peli.lisaaSiirto();
    }
    
    private void klikattuRuutua(MouseEvent event) {

        Node n = (Node) event.getTarget();
        if (n == null || !(n instanceof GridPane) && !(n instanceof ImageView)) {
            return;
        }
        Integer ex = root.getColumnIndex(n), ey = root.getRowIndex(n);
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
            // peli loppu, tee jotain!
        }
    }
}
