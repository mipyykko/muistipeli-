/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.JavaFXIkkuna;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author pyykkomi
 */
public class TulosController implements Initializable, ControlledRuutu {

    private IkkunaController ikkunaController;
    @FXML private GridPane valikko;
    @FXML private HBox valikkoWrapper;
    @FXML private Text otsikko;
    @FXML private Text tulosteksti;
    @FXML private Button valikkoButton;
    @FXML private Button uusipeliButton;
    private Peli peli;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        otsikko.setFont(Font.loadFont(getClass().getResourceAsStream("/fontit/GoodDog.otf"), 48));
        valikkoWrapper.setStyle("-fx-background-color: #FFFF00; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 50, 0, 0, 0);");
        if (peli != null) {
            tulosteksti.setText("Käytit " + peli.getSiirrotLkm() + " siirtoa.");
        }
        valikkoButton.setOnAction(ae -> ikkunaController.asetaIkkuna(JavaFXIkkuna.VALIKKO));
        uusipeliButton.setOnAction(ae -> {
            try {
                peli.uusiPeli();
                ikkunaController.setPeli(peli);
                ((PeliController) ikkunaController.getController(JavaFXIkkuna.PELI)).alustaRuudukko();
                ikkunaController.asetaIkkuna(JavaFXIkkuna.PELI);
            } catch (Exception e) {
                System.out.println("Pelin luominen epäonnistui! " + e.getMessage()); // debug
            }            
        });
    }    

    public void asetaPeli(Peli peli) {
        this.peli = peli;
    }
    
    @Override
    public void asetaParent(IkkunaController ikkuna) {
        ikkunaController = ikkuna;
    }
    
}
