/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Alkuruudun valikko.
 * 
 * @author pyykkomi
 */
public class Valikko extends GridPane {

    private GridPane sisalto;
    private Button aloitusnappi;
    private ComboBox kuvavalikko;

    /**
     * Luo valikon osat ja sijoittaa ne. Hakee myös valikkoon kuvasettien nimet.
     * 
     * @throws Exception Jotain tapahtui, luultavasti kuvasettejä ei löydy.
     */
    public Valikko() throws Exception {
        sisalto = new GridPane();
        sisalto.setBackground(null);
        Text otsikko = new Text("Muistipeliö");
        otsikko.setFont(Font.loadFont(getClass().getClassLoader().getResourceAsStream("fontit/GoodDog.otf"), 48));
        otsikko.setTextAlignment(TextAlignment.CENTER);

        aloitusnappi = new Button("Aloita peli");
        aloitusnappi.setAlignment(Pos.CENTER);

        kuvavalikko = new ComboBox();
        //debug:
        ObservableList<String> kuvaOptions = FXCollections.observableArrayList();
        try {
            for (String s : new JavaFXInit().haeKuvasetit()) {
                String[] hakemisto = s.split("/");
                kuvaOptions.add(hakemisto[hakemisto.length - 1]);
            }
        } catch (Exception e) {
            throw new Exception("Ei kuvasettejä odotetussa hakemistossa!");
        }
        kuvavalikko.setItems(kuvaOptions);
        kuvavalikko.getSelectionModel().selectFirst();

        sisalto.add(otsikko, 1, 0);
        sisalto.add(kuvavalikko, 1, 1);
        sisalto.add(aloitusnappi, 1, 2);

        GridPane.setHalignment(otsikko, HPos.CENTER);
        GridPane.setHalignment(aloitusnappi, HPos.CENTER);
        GridPane.setHalignment(kuvavalikko, HPos.CENTER);
    }

    public GridPane getSisalto() {
        return sisalto;
    }

    public Button getAloitusnappi() {
        return aloitusnappi;
    }

    public String getKuvavalikkoValittu() {
        return (String) kuvavalikko.getValue();
    }
}
