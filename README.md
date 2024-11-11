### Talevici Adela ###

# Tema POO  - GwentStone #

## Organizare, design ##

- toata ierarhia de minioni/eroi pleaca de la clasa `Card`, o clasa abstracta care contine numai proprietatile
comune eroilor si minionilor.
- clasa Card este mostenita direct de 2 clase: `Minion` si `Hero`. Si clasele acestea sunt abstracte, fiind
folosite doar pentru polimorfism.
- am facut mostenirea asa pentru ca a fost mai usor sa implementez mai departe clasele concrete de minioni si eroi: fiecare
clasa poate da override la metodele care trebuie schimbate din clasa parinte, spre exemplu abilitatile speciale.
- am avut o problema cu instantierea claselor, pe care am rezolvat-o cu clasele de tip Factory. De exemplu, folosesc o lista
minioni pentru a tine deck-ul unui player, dar minionii trebuie sa fie un tip concret, in functie de nume(Berserker, Ripper etc.). Prin
utilizarea Factory, am separat logica de instantiere de logica pricipala a programului, si am obtinut un cod mai clar si mai usor de extins.
- Pachetul `game` contine clasele care controleaza jocul:
  - `GameManager` trece prin toata lista de jocuri, pregateste jucatorii si porneste un joc, pentru care trimite toate comenzile pe rand
  - `Game` reprezinta un meci intreg. Executa comenzi primite de la GameManager.
  - `GameCommands` contine comenzile mai lungi, de atac, abilitati speciale, si plasarea cartilor.
  - `GameTable` este masa de joc si contine cateva comenzi mai scurte, care tin strict de asezarea cartilor pe masa.
- `Player`: contine deck-urile, cartile din mana, pachetul curent, eroul. Pentru pachetul curent am folosit o stiva, functioneaza exact ca un pachet de carti.

## Detalii de implementare, flow ##

- In Main, transform clasele din `input` in clasele facute de mine si initializez jucatorii. Executia se muta in GameManager.
- In GameManager, pentru fiecare joc, se pregatesc jucatorii: se alege pachetul de carti, se amesteca, etc. Important aici este faptul ca se face
un **deep copy** la fiecare minion, si se adauga intr-o lista noua, nu se amesteca in direct in deck. Daca s-ar fi facut direct in deck,
nu mai putea reveni la starea initiala, minionii fiind modificati in urma jocurilor.
- Din GameManager se trimit miscarile catre `Game`. Game se ocupa cu toate informatiile necesare pentru un meci intreg: mana pe runda, jucatorul curent,
start si end pentru runde. Se parseaza comenzile si se apeleaza metoda care le rezolva. La sfarsit, in GameManager se trece prin tot output-ul
unui meci si se adauga la output-ul final.
- `GameCommands`: comenzile de aici seamana destul de mult: pentru atac/abilitati speciale, se verifica daca se poate face miscarea respectiva,
si daca nu se poate se trimite un mesaj de eroare, altfel un node gol. Important aici este ca o coordonata din joc (1, 1) va fi in realitate (2, 1) 
deci va trebui sa modificam x. Se face mereu 3 - x ca sa obtinem coordonata corecta.

