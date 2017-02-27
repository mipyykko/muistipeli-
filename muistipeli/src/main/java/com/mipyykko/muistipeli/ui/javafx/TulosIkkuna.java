/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.JavaFXMain;
import com.mipyykko.muistipeli.logiikka.Peli;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Pelin tuloksen kertova ikkuna josta mahdollista myös käynnistää uusi peli.
 *
 * @author pyykkomi
 */
public class TulosIkkuna extends GridPane {

    private Stage primaryStage;
    private GridPane valikko;
    private Peli peli;

    /**
     * Tulosikkunan konstruktori.
     *
     * @param peli Peli-objekti josta tulos haetaan.
     */
    public TulosIkkuna(Peli peli) {
        super();
        this.primaryStage = JavaFXMain.getStage();
        this.peli = peli;

        setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        valikko = new GridPane();
        valikko.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
        setAlignment(Pos.CENTER);
        valikko.setPadding(new Insets(15));
        valikko.setHgap(16);
        valikko.setVgap(8);

        Text onnitteluText = new Text("Jee! Voitit!");
        onnitteluText.setFont(Font.loadFont(getClass().getClassLoader().getResourceAsStream("fontit/GoodDog.otf"), 64));
        onnitteluText.setTextAlignment(TextAlignment.CENTER);

        Text tulosText = new Text("Käytit " + peli.getSiirrotLkm() + " siirtoa.");
        tulosText.setTextAlignment(TextAlignment.CENTER);

        Button valikkoButton = new Button("Alkuvalikkoon");
        valikkoButton.setOnAction((ActionEvent ae) -> {
            ValikkoIkkuna valikkoIkkuna = new ValikkoIkkuna();
            primaryStage.setScene(new Scene(valikkoIkkuna, this.getScene().getWidth(),
                    this.getScene().getHeight()));
        });
        Button uusipeliButton = new Button("Uusi peli");
        uusipeliButton.setOnAction((ActionEvent ae) -> {
            try {
                // TODO: tähän se liukuma toiseen suuntaan kunhan saadaan koko roska stackpaneen
                peli.uusiPeli();
                PeliIkkuna peliIkkuna = new PeliIkkuna(peli);
                primaryStage.setScene(new Scene(peliIkkuna, this.getScene().getWidth(),
                        this.getScene().getHeight()));
            } catch (Exception e) {
                System.out.println("Pelin luominen epäonnistui! " + e.getMessage()); // debug
            }
        });

        valikko.addColumn(1, onnitteluText, new HBox(), tulosText, valikkoButton, uusipeliButton);

        setHalignment(valikko, HPos.CENTER);
        setHalignment(onnitteluText, HPos.CENTER);
        setHalignment(tulosText, HPos.CENTER);
        setHalignment(valikkoButton, HPos.CENTER);
        setHalignment(uusipeliButton, HPos.CENTER);

        add(valikko, 1, 1);
    }
}
