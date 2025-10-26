# E-Mobility sistem
E-mobility je simulacioni informacioni sistem za evidenciju i praćenje iznajmljivanja električnih automobila, bicikala i trotineta. 
Program omogućava evidentiranje iznajmljivanja, računanje troškova iznajmljivanja, generisanje računa, praćenje kvarova 
i novoa baterije prevoznih sredstava, kao i izradu finansijskih izvještaja na osnovu simulacije poslovanja.

## Funkcionalnosti
* Evidencija i upravljanje različitim vrstama električnih prevoznih sredstava (automobili, bicikli i trotineti)
* Simulacija kretanja prevoznih sredstava po mapi dimenzija 20×20
* Grafički interfejs (Swing) sa više ekrana:
  * mapa - pregled užeg i šireg dijela grada, te praćenje kretanja vozila od iznajmljivanja do razduživanja
  * tabela vozila - prikaz svih registrovanih automobila, bicikala i trotineta
  * spisak kvarova - evidencija vremena kvara, vozila na kom je kvar nastao, te opisa kvara
  * rezultati poslovanja - sumarni i dnevni finansijski izvještaji, te evidencija vozila (za svaku od vrstu) koja su pričinila najveću štetu u poslovanju.
* Automatsko generisanje računa za svako iznajmljivanje u _.txt_ formatu
* Izračunavanje popusta, promocija, troškova na popravak kvarova i troškova održavanja
* Serijalizacija i deserijalizacija podataka o prevoznim sredstvima koja su napravila najveći gubitak.

## Izvještaji
Sumarni izvještaj uključuje:
1. ukupan prihod
2. ukupan popust
3. ukupnan iznos promocija
4. ukupan iznos vožnji u užem i širem dijelu grada
5. troškove održavanja
6. troškove kvarova
7. ukupne troškove kompanije
8. ukupan porez.

Dnevni izvještaj prikazuje stavke 1-6 grupisane po datumu.

## Tehnologije i alati
Java - poslovna logika
Swing - grafički korisnički interfejs
Properties fajlovi - konfiguracija i parametri sistema
Javadoc - generisanje dokumentacije
Eclispe IDE - razvojno okruženje

# Autorska prava
© 2025 Aleksandra Vučićević
