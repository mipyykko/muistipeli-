/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.impl.JavaFXKortti;
import java.awt.Point;
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
    
    public JavaFXUI(Peli peli) {
        this.peli = peli;
        this.ikkunaleveys = 800;
        this.ikkunakorkeus = 600;
        this.firstrun = true;
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
                k.oikeaKuva();
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

    @Override
    public Point siirto() {
        // TODO
        return new Point(0, 0);
    }

    private void sulje() {
        primaryStage.close();
    }
    
    private void sijoitaJaSkaalaaIV(ImageView iv, int x, int y) {
        iv.fitWidthProperty().bind(root.widthProperty().divide(peli.getPelilauta().getLeveys()));
        iv.fitHeightProperty().bind(root.heightProperty().divide(peli.getPelilauta().getKorkeus()));
        root.setColumnIndex(iv, x);
        root.setRowIndex(iv, y);
    }
    
    private void animoiKortinKaanto(Node n, int x, int y) {
        JavaFXKortti j = (JavaFXKortti) peli.getPelilauta().getKortti(new Point(x, y));
        root.getChildren().remove(n);
        ImageView ivAlku = new ImageView((Image) j.getSisalto());
        sijoitaJaSkaalaaIV(ivAlku, x, y);

        n = (ImageView) ivAlku;
        ScaleTransition stPiilota = new ScaleTransition(Duration.millis(150), ivAlku);
        stPiilota.setFromX(1);
        stPiilota.setToX(0);
        
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
                System.out.println("hepsi");
            }
        });
        ((ImageView) n).setImage((Image) ivAlku.getImage());
        stPiilota.play();
        root.getChildren().addAll(ivAlku, ivLoppu);
    }
    
    private void klikattuRuutua(MouseEvent event) {
        // tähän tarkistus että missä pelin tilassa ollaan,
        // tehdään siihen vaikka enum

        Node n = (Node) event.getTarget();
        if (n == null || !(n instanceof GridPane) && !(n instanceof ImageView)) {
            return;
        }
        System.out.println(n);
        Integer ex = root.getColumnIndex(n);
        Integer ey = root.getRowIndex(n);
        if (ex == null || ey == null) {
            return;
        }
        
        animoiKortinKaanto(n, ex, ey);
        //j.oikeaKuva();
        //kortit.getChildren().remove(j);
        //ImageView iv = new ImageView((Image) j.getSisalto());
        //((ImageView) n).setImage((Image) j.getSisalto()); // tää toimii!

    }
}
