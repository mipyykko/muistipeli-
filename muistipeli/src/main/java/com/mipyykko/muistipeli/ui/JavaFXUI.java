/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui;

import com.mipyykko.muistipeli.logiikka.Peli;
import com.mipyykko.muistipeli.malli.enums.Pelitila;
import com.mipyykko.muistipeli.malli.impl.JavaFXKortti;
import java.awt.Point;
import javafx.animation.PauseTransition;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    private GridPane ruudukko;
    private BorderPane ikkuna;
    private Text score;
    private HBox status;
    private Scene scene;
    private boolean firstrun;
    private Point[] siirto;
    private Node[] siirtoNodet;
    private ImageView[][] ivRuudukko;

    public JavaFXUI(Peli peli) {
        this.peli = peli;
        this.ikkunaleveys = 560; // magic numbers!
        this.ikkunakorkeus = 620;
        this.firstrun = true;
        this.siirto = new Point[2];
        this.siirtoNodet = new Node[2];
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
        ikkuna = new BorderPane();
        status = new HBox();
        status.setPadding(new Insets(15, 15, 15, 15));
        status.setSpacing(10);
        status.setStyle("-fx-background-color: #800000");
        score = new Text("Siirrot: 0 Parit: 0");
        score.setFont(Font.loadFont(getClass().getClassLoader().getResource("fontit/GoodDog.otf").toExternalForm(), 24));
        score.setFill(Color.YELLOW);
        status.getChildren().add(score);
        ikkuna.setTop(status);
        
        ruudukko = new GridPane();
        //ruudukko.prefHeightProperty().bind(ikkuna.heightProperty().subtract(status.heightProperty()));
        //ruudukko.prefWidthProperty().bind(ikkuna.widthProperty().subtract(status.widthProperty()));
        ruudukko.setPadding(new Insets(5, 0, 5, 0));
        ruudukko.setVgap(4);
        ruudukko.setHgap(4);

        ikkuna.setCenter(ruudukko);
        
        kortit = new Group();
        kortit.getChildren().clear();
        ivRuudukko = new ImageView[peli.getPelilauta().getLeveys()][peli.getPelilauta().getKorkeus()];
        // NEXT: imageviewit pysyy samoina, imaget vaihtuu
        for (int y = 0; y < peli.getPelilauta().getKorkeus(); y++) {
            for (int x = 0; x < peli.getPelilauta().getLeveys(); x++) {
                JavaFXKortti k = (JavaFXKortti) peli.getPelilauta().getKortti(new Point(x, y));
                //kortit.getChildren().remove(k);
                ivRuudukko[x][y] = new ImageView((Image) k.getSisalto());
                sijoitaJaSkaalaaIV(ivRuudukko[x][y], x, y);
                ruudukko.getChildren().add(ivRuudukko[x][y]);
            }
        }
        ruudukko.getChildren().add(kortit);
        ruudukko.setOnMouseClicked((MouseEvent event) -> klikattuRuutua(event));
        scene = new Scene(ikkuna, ikkunaleveys, ikkunakorkeus);
        ruudukko.setBackground(null);
        scene.setFill(Color.YELLOW);

        primaryStage.setTitle("Muistipeliö");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
        
        vilautaKortteja(1);
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

    private void sulje() {
        primaryStage.close();
    }

    private void sijoitaJaSkaalaaIV(ImageView iv, int x, int y) {
        /* note to self:
            for file in *.png; do convert -resize 256x256 $file -background none -gravity center -extent 256x256 $file; done
        */
        // TODO: magic numbers!
        iv.setPreserveRatio(true);
        iv.fitWidthProperty().bind(ikkuna.widthProperty().divide(peli.getPelilauta().getLeveys()));
        iv.fitHeightProperty().bind(ikkuna.heightProperty().subtract(70).divide(peli.getPelilauta().getKorkeus()));
        GridPane.setColumnIndex(iv, x);
        GridPane.setRowIndex(iv, y);
    }

    private void kaannaKortti(Node n, Point p) {
        JavaFXKortti j = (JavaFXKortti) peli.getPelilauta().getKortti(p);
        ImageView ivAlku = ivRuudukko[p.x][p.y];
        ScaleTransition stPiilota = new ScaleTransition(Duration.millis(150), ivAlku);
        stPiilota.setFromX(1);
        stPiilota.setToX(0);

        j.kaanna();

        ImageView ivLoppu = new ImageView((Image) j.getSisalto());
        sijoitaJaSkaalaaIV(ivLoppu, p.x, p.y);
        ivLoppu.setScaleX(0);
        ScaleTransition stNayta = new ScaleTransition(Duration.millis(150), ivLoppu);
        stNayta.setFromX(0);
        stNayta.setToX(1);

        stPiilota.setOnFinished((ActionEvent t) -> {
            ruudukko.getChildren().remove(ivAlku);
            ruudukko.getChildren().add(ivLoppu);
            ivRuudukko[p.x][p.y] = ivLoppu;
            stNayta.play();
        });
        //peli.setTila(Pelitila.ANIM_ALKU);
        stPiilota.play();
        stNayta.setOnFinished((ActionEvent t) -> {
            //j.oikeaKuva();
        });
    }
    
    private PauseTransition odotaKortinKaantoa(Point p, int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished((ActionEvent t) -> {
            System.out.println("hello polly"); // debug
            kaannaKortti(ivRuudukko[p.x][p.y], p);
        });
        return pause;
    }
    
    private void vilautaKortteja(int seconds) {
        // yhyy
    }
    
    private void odotaEnnenParinKaantoa(int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(new EventHandler<ActionEvent>() {
            Node[] nodes = siirtoNodet;
            
            @Override
            public void handle(ActionEvent t) {
                for (Node n : nodes) {
                    kaannaKortti(n, new Point(GridPane.getColumnIndex(n), GridPane.getRowIndex(n)));
                }
                peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
            }
        });
        pause.play();
    }

    private void paivitaScore() {
        score.setText("Siirrot: " + peli.getSiirrotLkm() + " Parit: " + peli.getParitLkm());
    }
    
    private void hoidaSiirto(Node n, Point p) {
        kaannaKortti(n, p);
        if (siirto[0] != null) {
            siirto[1] = p;
            siirtoNodet[1] = n;
            if (!peli.tarkistaPari(siirto)) {
                // sekunnin tauko korttien kääntämisen jälkeen
                odotaEnnenParinKaantoa(1);
            } else {
                // pari
                peli.lisaaPari();
                if (peli.peliLoppu()) {
                    peli.setTila(Pelitila.PELI_LOPPU);
                } else {
                    peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
                }
            }
            peli.lisaaSiirto();
            paivitaScore();
            siirto = new Point[2];
            siirtoNodet = new Node[2];
        } else {
            siirto[0] = p;
            siirtoNodet[0] = n;
            peli.setTila(Pelitila.ODOTTAA_SIIRTOA);
        }
    }
    
    private void klikattuRuutua(MouseEvent event) {

        Node n = (Node) event.getTarget();
        if (n == null || !(n instanceof GridPane) && !(n instanceof ImageView)) {
            return;
        }
        Integer x = ruudukko.getColumnIndex(n), y = ruudukko.getRowIndex(n);
        if (x == null || y == null) {
            return;
        }
        Point p = new Point(x, y);
        
        if (peli.getTila() != Pelitila.ODOTTAA_SIIRTOA || 
            peli.getPelilauta().getKortti(p).kaannetty()) {
            return;
        }

        peli.setTila(Pelitila.ANIM_KAYNNISSA);
        hoidaSiirto(n, p);
        if (peli.getTila() == Pelitila.PELI_LOPPU) {
            System.out.println("lölz"); //TODO
        }
    }
}
