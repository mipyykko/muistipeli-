###
**Aihe:** Muistipeli

Toteutetaan yksinkertainen muistipeli, jossa pelaajan tehtävänä on löytää kuvapuoli alaspäin käännetyistä korteista parit. Pelissä on erilaisia vaikeustasoja, ja parhaista pistemääristä (aika, siirrot) pidetään yllä taulukkoa. Tarkoituksena on myös toteuttaa jonkintasoista konfiguroitavuutta, esim. eri korttisarjojen muodossa.

**Käyttäjät:** Pelaaja

**Pelaajan toiminnot:** 

* vaikeustason valitseminen
* pelin käynnistäminen
* pelin pelaaminen
  * kortin klikkaaminen
    * ei onnistu, jos kortti on käännetty tai odotetaan animaation loppua
    * kun kaksi korttia on käännetty, tarkistetaan onko pari
    * jos ei ole pari, odotetaan sekunti ja käännetään pari takaisin
    * kun kaikki parit on löydetty, peli loppuu

**Luokkakaavio:**

![Luokkakaavio](luokkakaavio.png)

**Sekvenssikaaviot:**

Ensimmäisen kuvan tilanteessa on kutsuttu peli-luokan uusiPeli-metodia oikeilla parametreilla:

![Sekvenssikaavio 1](sekvenssi1.png)

Seuraavan kuvan tilanteessa on jo yksi kortti käännetty ja klikataan korttia, joka osoittautuu ensimmäisen pariksi:

![Sekvenssikaavio 2](sekvenssi2.png)
