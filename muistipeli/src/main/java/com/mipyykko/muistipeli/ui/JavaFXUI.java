/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.JavaFXKortti;
import java.awt.Point;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author pyykkomi
 */
public class JavaFXUI implements UI {
    
    private Peli peli;
    private int ikkunaleveys, ikkunakorkeus;
    private Group kortit = new Group();
    private StackPane root;
    private Scene scene;
    
    public JavaFXUI(Peli peli) {
        this.peli = peli;
        this.ikkunaleveys = 800;
        this.ikkunakorkeus = 600; 
    }

    public void startUI(Stage primaryStage) {
        // testikoodia javafx:n leipomiseen, ei liity
        /*
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });*/
        
        root = new StackPane();
        //root.getChildren().add(btn);
        
        scene = new Scene(root, ikkunaleveys, ikkunakorkeus);
        root.setBackground(null);
        scene.setFill(Color.DARKBLUE);
        
        primaryStage.setTitle("Muistipeliö");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void setPeli(Peli peli) {
        this.peli = peli;
    }

    @Override
    public void nayta() {
        kortit.getChildren().clear();
        
        // kortin paikan vois jotenkin esim. säilöä sinne korttiin ja laittaa tsekkauksen onko piste sisällä jne
        
        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            for (int x = 0; x < peli.getPelilauta().getLeveys(); x++) {
                JavaFXKortti k = (JavaFXKortti) peli.getPelilauta().getKortti(new Point(x, y));
                k.setXY(x * (k.getKorttiLeveys() + 20), y * (k.getKorttiKorkeus() + 20));
                kortit.getChildren().add(k);
            }
        }
        
        root.getChildren().add(kortit);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point siirto() {
        scene.setOnMouseClicked((MouseEvent event) -> {
            double klikX = event.getSceneX();
            double klikY = event.getSceneY();
            
            // mihin klikattu? tämän eventin paikkaa vois kyllä esim. miettiä
        });
        // testeilyä
        return new Point(0, 0);
    }

}
