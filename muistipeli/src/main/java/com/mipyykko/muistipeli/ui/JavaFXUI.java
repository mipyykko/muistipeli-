/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.Main;
import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.Kortti;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.impl.JavaFXKortti;
import com.mipyykko.muistipeli.malli.impl.JavaFXKuva;
import com.mipyykko.muistipeli.malli.impl.JavaFXTausta;
import java.awt.Point;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Application;
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

/**
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

    public JavaFXUI(Peli peli) {
        this.peli = peli;
        this.ikkunaleveys = 800;
        this.ikkunakorkeus = 600;
        //this.kortit = new Group();

    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    @Override
    public void setPeli(Peli peli) {
        this.peli = peli;
    }

    @Override
    public void nayta() {
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
                root.setRowIndex(iv, y);
                root.setColumnIndex(iv, x);
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

    @Override
    public Point siirto() {
        // testeilyä
        return new Point(0, 0);
    }

    private void sulje() {
        primaryStage.close();
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
        if (ex == null || ey == null) return;
        
        JavaFXKortti j = (JavaFXKortti) peli.getPelilauta().getKortti(new Point(ex, ey));
        j.kaanna();
        j.oikeaKuva();
        kortit.getChildren().remove(j);
        ImageView iv = new ImageView((Image) j.getSisalto());
        ((ImageView) n).setImage((Image) j.getSisalto()); // tää toimii!

    }
}
