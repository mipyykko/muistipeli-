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

    private IkkunaController ikkunaController;

    @FXML
    private Text otsikko;
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
    private Slider leveysSlider;
    @FXML
    private Slider korkeusSlider;
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
        lueKuvalista();
        alustaSliderit();
        kuvavalikko.setOnAction(e -> {
            alustaSliderit();
        });
        aloitusnappi.setOnAction(e -> aloitusNappiKlikattu());
        lopetusnappi.setOnAction(e -> ikkunaController.getUI().sulje());
        optionsEsilla = false;
        optionsnappi.setOnAction(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), optionsPane);
            if (optionsEsilla) {
                tt.setFromY(valikko.getHeight());
                tt.setToY(0);
            } else {
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

    private void alustaSliderit() {
        lueKuvalista();
        sliderAlustusKaynnissa = true;
        int settikoko = jfi.getKuvaLista().size() * 2;
        for (Slider s : new Slider[]{korkeusSlider, leveysSlider}) {
            s.setMin(3);
            s.setSnapToTicks(true);
            s.setMax(Math.floor(settikoko / 3));
            s.setShowTickLabels(true);
            s.setShowTickMarks(true);
            s.setMajorTickUnit(1);
            s.setMinorTickCount(0);
            s.setBlockIncrement(1);
            s.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (!sliderAlustusKaynnissa) {
                    if (s.getId().equals("leveysSlider")) {
                        korkeusSlider.setMax(Math.floor(settikoko / leveysSlider.getValue()));
                    } else {
                        leveysSlider.setMax(Math.floor(settikoko / korkeusSlider.getValue()));
                    }
                    if ((korkeusSlider.getValue() * leveysSlider.getValue()) % 2 != 0) {
                        if (korkeusSlider.getValue() > korkeusSlider.getMin()) {
                            korkeusSlider.decrement();
                        } else {
                            leveysSlider.decrement();
                        }
                    }
                    }
                }
            });
            s.setValue(Math.floor(settikoko / 4));
        }
        sliderAlustusKaynnissa = false;
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
        int leveys = (int) leveysSlider.getValue();
        int korkeus = (int) korkeusSlider.getValue();
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
