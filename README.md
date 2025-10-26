# E-Mobility sistem
E-mobility je simulacioni informacioni sistem za evidenciju i praćenje iznajmljivanja električnih automobila, bicikala i trotineta. 
Program omogućava evidentiranje iznajmljivanja, računanje troškova iznajmljivanja, generisanje računa, praćenje kvarova 
i novoa baterije prevoznih sredstava, kao i izradu finansijskih izvještaja na osnovu simulacije poslovanja.

## Funkcionalnosti
* Evidencija i upravljanje različitim vrstama električnih prevoznih sredstava (automobili, bicikli i trotineti)
* Simulacija kretanja prevoznih sredstava po mapi dimenzija 20×20, pri čemu se prevozna sredstva kreću pravolinijski - prvo horizontalno, a zatim vertikalno
* Grafički interfejs (implementiran u Swing-u) sa više ekrana:
  * početni ekran - služi za navigaciju kroz aplikaciju
  * mapa - pregled užeg i šireg dijela grada, te praćenje kretanja vozila od iznajmljivanja do razduživanja
  * tabela vozila - prikaz svih registrovanih automobila, bicikala i trotineta
  * spisak kvarova - evidencija vremena kvara, vozila na kom je kvar nastao, te opisa kvara
  * rezultati poslovanja - sumarni i dnevni finansijski izvještaji, te evidencija vozila (za svaku od vrstu) koja su pričinila najveću štetu u poslovanju.
* Automatsko generisanje računa za svako iznajmljivanje u _.txt_ formatu
* Izračunavanje popusta, promocija, troškova na popravak kvarova i troškova održavanja
* Serijalizacija i deserijalizacija podataka o prevoznim sredstvima koja su napravila najveći gubitak.

## Izvještaji
Sumarni izvještaj uključuje:
1. ukupan prihod [suma svih iznosa za plaćanje na svim računima]
2. ukupan popust [suma svih iznosa popusta sa svih računa]
3. ukupnan iznos promocija [suma vrijednosti svih promocija sa svih računa]
4. ukupan iznos vožnji u užem i širem dijelu grada
5. troškove održavanja [ukupan prihod * 0,2]
6. troškove kvarova [suma svih pojedinačnih troškova nastalih zbog kvarova, koji se računaju kao koeficijent (automobili = 0.07, bicikli = 0.04, trotineti = 0.02) * cijena nabavke pokvarenog prevoznog sredstva]
7. ukupne troškove kompanije [20% ukupnog prihoda]
8. ukupan porez [(ukupan prihod - ukupan iznos za održavanje - ukupan iznos za popravke kvarova - troškovi kompanije) * 10% ]

Dnevni izvještaj prikazuje stavke 1-6 grupisane po datumu.

## Izgled aplikacije
<p align="center">
<img width="1919" height="1019" alt="emobility1" src="https://github.com/user-attachments/assets/3c9462e7-5590-47e7-8b68-b734f007a519">
<img width="1919" height="1012" alt="emobility1 1" src="https://github.com/user-attachments/assets/fc345401-7545-457a-bb70-6311df8f8ae2">
<img width="1919" height="1012" alt="emobility1 2" src="https://github.com/user-attachments/assets/bb4e8c2a-fde6-43de-a9b2-5fcfd06ad6e6">
<br>
<em>Početni ekran (dugmad za prikaz kvarova i izvještaja su onemogućena prije izvršenja simulacije)</em>
</p>

<p align="center">
<img width="1919" height="1018" alt="emobility2" src="https://github.com/user-attachments/assets/79d162bc-5b72-4ce2-a492-6d55caf8d847">
<br>
<em>Registrovana vozila</em>
</p>

<p align="center">
<img width="1918" height="1019" alt="emobility3" src="https://github.com/user-attachments/assets/49e715b3-4530-44ce-9a96-a3bfca0233ae">
<br>
<em>Prikaz mape sa užim i širim dijelom grada</em>
</p>

<p align="center">
<img width="1919" height="1020" alt="emobility4" src="https://github.com/user-attachments/assets/a84e235c-1520-4e59-b7a6-2cd6bed67b83">
 <img width="1919" height="1019" alt="emobility4 1" src="https://github.com/user-attachments/assets/5197f10d-78b0-421b-8ea1-c2ed4a892006">
<br>
<em>Praćenje kretanja vozila po mapi</em>
</p>

<p align="center">
<img width="1919" height="1018" alt="emobility5" src="https://github.com/user-attachments/assets/60c1cdce-2e8c-4afe-aef4-b677a5a92765">
<br>
<em>Obavještenje o završetku simulacije</em>
</p>

<p align="center">
<img width="1919" height="1017" alt="emobility6" src="https://github.com/user-attachments/assets/a72180e6-b673-4db9-bb16-6c66835ad1bf">
<br>
<em>Kvarovi nastali na vozilima (tokom simulacije)</em>
</p>

<p align="center">
<img width="1919" height="1017" alt="emobility7" src="https://github.com/user-attachments/assets/c03d46f3-c9e8-4889-b880-897a72fe3ef8">
<img width="1919" height="1018" alt="emobility7 1" src="https://github.com/user-attachments/assets/406a0dda-1dd5-4fd0-950d-ea102fecc2ac">
<img width="1919" height="1019" alt="emobility7 2" src="https://github.com/user-attachments/assets/723f54c5-9c15-4c12-941b-d2324e314980">
<br>
<em>Ekran sa izvještajima</em>
</p>

## Napomena
Ovaj projektni zadatak podrazumijeva:
* rad sa nitima
* rad sa properties fajlovima
* izradu grafičkog korisničkog interfejsa
* Java serijalizaciju i deserijalizaciju
* binarnu serijalizaciju i deserijalizaciju
* rad sa Javadoc alatom 

## Tehnologije i alati
Java - poslovna logika  
Swing - grafički korisnički interfejs  
Properties fajlovi - konfiguracija i parametri sistema  
Javadoc - generisanje dokumentacije  
Eclispe IDE - razvojno okruženje

# Autorska prava
© 2025 Aleksandra Vučićević
