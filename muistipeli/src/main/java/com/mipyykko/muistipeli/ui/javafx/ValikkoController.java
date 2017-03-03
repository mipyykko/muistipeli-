/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.Kuva;
import com.mipyykko.muistipeli.malli.Tausta;
import com.mipyykko.muistipeli.malli.enums.JavaFXIkkuna;
import com.mipyykko.muistipeli.malli.enums.Korttityyppi;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author pyykkomi
 */
public class ValikkoController implements Initializable, ControlledRuutu {

    class Pelimoodi {
        int leveys;
        int korkeus;
        
        public Pelimoodi(int leveys, int korkeus) {
            this.leveys = leveys;
            this.korkeus = korkeus;
        }
        
        @Override
        public String toString() {
            return leveys + "x" + korkeus;
        }
    }
    
    private IkkunaController ikkunaController;

    @FXML
    private Text otsikko;
    @FXML
    private ComboBox kokovalikko;
    @FXML
    private ComboBox kuvavalikko;
    @FXML
    private Button aloitusnappi;
    @FXML
    private GridPane valikkoIkkuna;
    @FXML
    private Button lopetusnappi;
    @FXML
    private Button optionsnappi;
    @FXML
    private GridPane optionsPane;
    @FXML
    private GridPane valikko;
    @FXML
    private Text virheText;

    private JavaFXInit jfi;

    private boolean optionsEsilla;
    private boolean sliderAlustusKaynnissa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alustaKuvavalikko();
        lueKuvalista();
        alustaKokovalikko();
        aloitusnappi.setOnAction(e -> aloitusNappiKlikattu());
        lopetusnappi.setOnAction(e -> ikkunaController.getUI().sulje());
        optionsEsilla = false;
        optionsnappi.setOnAction(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), optionsPane);
            if (optionsEsilla) {
                optionsnappi.setText("Näytä asetukset");
                tt.setFromY(valikko.getHeight());
                tt.setToY(0);
            } else {
                optionsnappi.setText("Piilota asetukset");
                tt.setFromY(0);
                tt.setToY(valikko.getHeight());
            }
            tt.setRate(1);
            optionsEsilla = !optionsEsilla;
            tt.play();
            kuvavalikko.setLayoutX(0);
        });
    }

    @Override
    public void asetaParent(IkkunaController parent) {
        ikkunaController = parent;
    }

    private void alustaKuvavalikko() {
        ObservableList<String> kuvaOptions = FXCollections.observableArrayList();
        try {
            for (String s : new JavaFXInit().haeKuvasetit()) {
                String[] hakemisto = s.split("/"); //TODO: ei toimi joka vehkeellä
                kuvaOptions.add(hakemisto[hakemisto.length - 1]);
            }
        } catch (Exception e) {
            try {
                throw new Exception("Ei kuvasettejä odotetussa hakemistossa!");
            } catch (Exception ex) {
                Logger.getLogger(ValikkoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        kuvavalikko.setItems(kuvaOptions);
        kuvavalikko.getSelectionModel().selectFirst();        
    }
    
    private void alustaKokovalikko() {
        lueKuvalista();
        int settikoko = jfi.getKuvaLista().size() * 2;
        ObservableList<Pelimoodi> kokoOptions = FXCollections.observableArrayList();
        int x = 3;
        int y = 3;
        while (x * y <= settikoko) {
            if ((x * y) % 2 == 0) {
                kokoOptions.add(new Pelimoodi(x, y));
            }
            y++;
            if (x * y > settikoko) {
                y = 3;
                x++;
            }
        } 
        kokovalikko.setItems(kokoOptions);
        kokovalikko.getSelectionModel().select(kokoOptions.size() / 2);
        kokovalikko.setTranslateX(0.0);
        kokovalikko.setLayoutX(0);
    }
    
    private void lueKuvalista() {
        jfi = new JavaFXInit();
        String setti = (String) kuvavalikko.getValue();
        if (setti == null || setti.isEmpty()) {
            return;
        }
        jfi.lueKuvalista(setti);
        jfi.lueTaustalista("debug");

    }

    private void aloitusNappiKlikattu()/* throws Exception*/ {
        int leveys = ((Pelimoodi) kokovalikko.getValue()).leveys;
        int korkeus = ((Pelimoodi) kokovalikko.getValue()).korkeus;
        Set<Kuva> kuvat = null;
        try {
            kuvat = jfi.luoKuvat(leveys, korkeus);
        } catch (Exception ex) {
            System.err.println("Kuvia ei voitu luoda");
        }
        Set<Tausta> taustat = null;
        try {
            taustat = jfi.luoTaustat(leveys, korkeus);
        } catch (Exception ex) {
            System.err.println("Taustoja ei voitu luoda");
        }
        Peli peli = new Peli(Korttityyppi.JAVAFX);
        try {
            peli.uusiPeli(leveys, korkeus, kuvat, taustat);
            ikkunaController.setPeli(peli);
            ((PeliController) ikkunaController.getController(JavaFXIkkuna.PELI)).alustaRuudukko();
            ikkunaController.asetaIkkuna(JavaFXIkkuna.PELI);
        } catch (Exception e) {
            virheText.opacityProperty().setValue(1);
            virheText.setText(e.getMessage());
            Animation a = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(virheText.opacityProperty(), 1d)),
                    new KeyFrame(Duration.seconds(2), new KeyValue(virheText.opacityProperty(), 0d)));
            a.play();
        }
    }

}
