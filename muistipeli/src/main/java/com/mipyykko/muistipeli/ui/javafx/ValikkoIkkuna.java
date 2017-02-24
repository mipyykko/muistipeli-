/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.JavaFXMain;
import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Valikko-ikkuna.
 *
 * @author pyykkomi
 */
public class ValikkoIkkuna extends GridPane {

    private Button aloitusnappi;
    private ComboBox kuvavalikko;
    private GridPane sisalto;
    private Stage primaryStage;

    /**
     * Luo valikkoikkunan ja valikon sisältöineen.
     *
     * @param primaryStage Stage johon luodaan peliä käynnistettäessä uusi
     * scene.
     */
    public ValikkoIkkuna()/* throws Exception*/ {
        super();
        this.primaryStage = JavaFXMain.getStage();
        sisalto = new GridPane();
        setAlignment(Pos.CENTER);
        setPadding(new Insets(15));
        setHgap(16);
        setVgap(8);
        setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));

        sisalto.setBackground(null);
        Text otsikko = new Text("Muistipeliö");
        otsikko.setFont(Font.loadFont(getClass().getClassLoader().getResourceAsStream("fontit/GoodDog.otf"), 48));
        otsikko.setTextAlignment(TextAlignment.CENTER);

        aloitusnappi = new Button("Aloita peli");
        aloitusnappi.setAlignment(Pos.CENTER);
        aloitusnappi.setOnAction((ActionEvent event) -> {
            //try {
            aloitusNappiKlikattu(event);
            //} catch (Exception ex) {
            //    System.err.println("Jotain tapahtui aloitusnappia painettassa :x " + ex.getMessage()); // DEBUG
            //}
        });

        kuvavalikko = new ComboBox();
        //debug:
        ObservableList<String> kuvaOptions = FXCollections.observableArrayList();
        try {
            for (String s : new JavaFXInit().haeKuvasetit()) {
                String[] hakemisto = s.split("/");
                kuvaOptions.add(hakemisto[hakemisto.length - 1]);
            }
        } catch (Exception e) {
            //throw new Exception("Ei kuvasettejä odotetussa hakemistossa!");
        }
        kuvavalikko.setItems(kuvaOptions);
        kuvavalikko.getSelectionModel().selectFirst();

        sisalto.add(otsikko, 1, 0);
        sisalto.add(kuvavalikko, 1, 1);
        sisalto.add(aloitusnappi, 1, 2);

        GridPane.setHalignment(otsikko, HPos.CENTER);
        GridPane.setHalignment(aloitusnappi, HPos.CENTER);
        GridPane.setHalignment(kuvavalikko, HPos.CENTER);

        add(sisalto, 1, 1);
    }

    public Button getAloitusnappi() {
        return aloitusnappi;
    }

    /**
     * Palauttaa kuvavalikko-comboboxin valitun arvon.
     *
     * @return kuvasetin nimi String-muodossa.
     */
    public String getKuvavalikkoValittu() {
        return (String) kuvavalikko.getValue();
    }

    private void aloitusNappiKlikattu(ActionEvent event)/* throws Exception*/ {
        //if (peli == null || peli.getTila() == Pelitila.VALIKKO) {
        int leveys = 2;
        int korkeus = 2;
        JavaFXInit jfi = new JavaFXInit();
        String setti = (String) kuvavalikko.getValue();
        if (setti == null || setti.isEmpty()) {
            return;
        }
        jfi.lueKuvalista(setti);
        jfi.lueTaustalista("debug");
        Set<Kuva> kuvat = null;
        try {
            kuvat = jfi.luoKuvat(leveys, korkeus);
        } catch (Exception ex) { // debug
            Logger.getLogger(ValikkoIkkuna.class.getName()).log(Level.SEVERE, null, ex);
        }
        Set<Tausta> taustat = null;
        try {
            taustat = jfi.luoTaustat(leveys, korkeus);
        } catch (Exception ex) { // debug
            Logger.getLogger(ValikkoIkkuna.class.getName()).log(Level.SEVERE, null, ex);
        }
        // debug pelin käynnistys?
        Peli peli = new Peli(Korttityyppi.JAVAFX);
        try {
            peli.uusiPeli(leveys, korkeus, kuvat, taustat);
            PeliIkkuna peliIkkuna = new PeliIkkuna(peli);
            //peliIkkuna.getRuudukko().alustaRuudukko();
            primaryStage.setScene(new Scene(peliIkkuna, this.getScene().getWidth()/*ikkunaleveys*/,
                    this.getScene().getHeight()/*ikkunakorkeus*/));
        } catch (Exception e) {
            System.out.println("Pelin luominen epäonnistui! " + e.getMessage()); // debug
        }
    }
}
