/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mipyykko.muistipeli.ui.javafx;

import com.mipyykko.muistipeli.Main;
import com.mipyykko.muistipeli.malli.*;
import com.mipyykko.muistipeli.malli.impl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import javafx.scene.image.Image;

/**
 * JavaFX-version alkurutiineja, mm. kuvien lataus.
 * 
 * @author pyykkomi
 */
public class JavaFXInit {

    private Map<String, String> kuvalista, taustalista;
    /**
     * Etsii kuvat-hakemiston alta alihakemistoja ja palauttaa niiden nimet.
     * 
     * @return Hakemistojen nimet Set-muodossa.
     * @throws URISyntaxException Väärä URI-synaksi.
     * @throws IOException Virhe lukemisessa.
     */
    public Set<String> haeKuvasetit() throws URISyntaxException, IOException {
        Set<String> kuvasetit = new TreeSet<String>();
        URI uri = getClass().getResource("/kuvat").toURI();
        Path kuvahakemisto;
        FileSystem fileSystem = null;
        if (uri.getScheme().equals("jar")) {
            fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            kuvahakemisto = fileSystem.getPath("/kuvat");
        } else {
            kuvahakemisto = Paths.get(uri);
        }
        Stream<Path> walk = Files.walk(kuvahakemisto, 1);
        for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
            Path p = it.next();
            if (Files.isDirectory(p) && !p.equals(kuvahakemisto)) {
                String[] s = p.toString().split("/"); // TODO mitenhän tää toimii eri koneilla
                kuvasetit.add(s[s.length - 1]);
            }
        }
        
        if (fileSystem != null) {
            fileSystem.close();
        }
        return kuvasetit;
    }
    /**
     * Luo kuvalistan jossa oikeat osoitteet resursseihin.
     *
     * @param tileset Hakemiston nimi. Metodi odottaa hakemistossa olevan
     * tileset.txt-nimisen tiedoston.
     */
    public void lueKuvalista(String tileset) {
        // debug: tää vois ihan hyvin vaan lukea hakemiston kaikki .png-tiedostot
        kuvalista = new HashMap<>();

        InputStream is = Main.class.getClassLoader().getResourceAsStream("kuvat/" + tileset + "/tileset.txt");
        try (Scanner s = new Scanner(is)) {
            while (s.hasNextLine()) {
                String[] t = s.nextLine().split(",");
                if (t.length == 2) {
                    kuvalista.put(t[0].replaceAll("^\"|\"$", ""),
                        "kuvat/" + tileset + "/images/" + t[1].replaceAll("^\"|\"$", ""));
                }
            }
        }
    }

    /**
     * Lukee taustat jostain. Tai nykyään ei tee mitään.
     * 
     * @param bgset Valittu taustasetti.
     */
    public void lueTaustalista(String bgset) {
        taustalista = new HashMap<>();
        // jotain jotain
    }

    /**
     * Luo Image-objektit korttien kuville.
     *
     * @param leveys Pelilaudan leveys.
     * @param korkeus Pelilaudan korkeus
     * @throws Exception Virhe kuvien luonnissa?
     * @return Palauttaa kuvat sisältävän Setin.
     */
    public Set<Kuva> luoKuvat(int leveys, int korkeus) throws Exception {
        Iterator<String> it = kuvalista.keySet().iterator();
        Set<Kuva> kuvat = new HashSet<>();

        try {
            for (int i = 0; i < (leveys * korkeus) / 2; i++) {
                String key = it.next();
                // TODO: ei hyväksy välejä tiedostonimissä!
                String tiedostonimi = URLDecoder.decode(kuvalista.get(key), "UTF-8");
                InputStream kuvaOsoite = Main.class.getClassLoader().getResourceAsStream(tiedostonimi);
                if (kuvaOsoite != null) {
                    Image kuva = new Image(kuvaOsoite); //, 200, 200, false, false);
                    kuvat.add(new JavaFXKuva(key, kuva));
                }
            }
        } catch (Exception e) {
            throw new Exception("Kuvia ei voitu luoda! " + e.getMessage());
        }

        return kuvat;
    }

    /**
     * Lataa taustakuvat, luo Image-oliot ja taustat.
     *
     * @param leveys Pelilaudan leveys.
     * @param korkeus Pelilaudan korkeus.
     * @return Palauttaa taustat sisältävän Setin.
     * @throws Exception Virhe taustoja ladatessa?
     */
    public Set<Tausta> luoTaustat(int leveys, int korkeus) throws Exception {
        // debug: ei taustalistaa
        //Iterator<String> it = taustalista.keySet().iterator();
        Set<Tausta> taustat = new HashSet<>();
        taustalista.put("tausta", "debug");
        
        try {
            for (int i = 0; i < leveys * korkeus; i++) {
                // String key = it.next(); // debug
                String key = "tausta";
                String testiTausta = "taustat/basic/tausta.png"; // "taustat/basic/tausta.png"
                InputStream taustaOsoite = Main.class.getClassLoader().getResourceAsStream(testiTausta);
                //InputStream taustaOsoite = Main.class.getClassLoader().getResourceAsStream(taustalista.get(key));
                Image tausta = new Image(taustaOsoite);
                taustat.add(new JavaFXTausta(taustalista.get(key), tausta));
            }
        } catch (Exception e) {
            throw new Exception("Taustoja ei voitu luoda! " + e.getMessage());
        }
        return taustat;
    }

}
