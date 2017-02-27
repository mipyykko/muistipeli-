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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author pyykkomi
 */
public class ValikkoController implements Initializable, ControlledRuutu {

    private IkkunaController ikkunaController;
    
    @FXML private Text otsikko;
    @FXML private ComboBox kuvavalikko;
    @FXML private Button aloitusnappi;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        otsikko.setFont(Font.loadFont(getClass().getResourceAsStream("/fontit/GoodDog.otf"), 48));
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
        aloitusnappi.setOnAction((ActionEvent ae) -> aloitusNappiKlikattu(ae));
    }

    @Override
    public void asetaParent(IkkunaController parent) {
        ikkunaController = parent;
    }
    
    private void aloitusNappiKlikattu(ActionEvent event)/* throws Exception*/ {
        //if (peli == null || peli.getTila() == Pelitila.VALIKKO) {
        int leveys = 4; // DEBUG, TODO jne. jne.
        int korkeus = 5;
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
            ikkunaController.setPeli(peli);
            ikkunaController.asetaIkkuna(JavaFXIkkuna.PELI);
        } catch (Exception e) {
            System.out.println("Pelin luominen epäonnistui! " + e.getMessage()); // debug
        }
    }

}
