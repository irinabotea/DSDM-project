# FitPulse

FitPulse este o aplicatie Android pentru monitorizarea activitatii fizice. Utilizatorul isi poate
construi propria lista de exercitii, le poate organiza pe zile si poate urmari, prin statistici si
grafice, cum evolueaza in timp. Fiecare cont are propriile date, separate de ale celorlalti utilizatori
de pe acelasi dispozitiv.

## Descrierea aplicatiei

La prima utilizare, utilizatorul isi creeaza un cont prin inregistrare, cu nume de utilizator, email si
parola. Dupa autentificare, sesiunea este retinuta, astfel incat la urmatoarele deschideri se intra
direct in aplicatie. Din ecranul de profil utilizatorul se poate deconecta, iar datele lui raman salvate
pentru data urmatoare.

In sectiunea de exercitii, utilizatorul adauga antrenamente alegand dintr-un set de exercitii comune sau
introducand unul propriu, cu nume, grupa musculara, categorie, numar de seturi si repetari. Categoriile
si grupele musculare sunt aduse dintr-o baza de date online de fitness si sunt afisate in campuri care se
filtreaza pe masura ce utilizatorul scrie. Fiecare exercitiu primeste o data, implicit ziua curenta, iar
in lista exercitiile apar grupate pe zile.

In profil pot fi completate date corporale, precum greutate, inaltime, varsta, gen si obiectiv de
fitness, iar aplicatia calculeaza indicele de masa corporala.

Sectiunea de statistici aduna datele despre exercitii: numarul total de exercitii, seturi si repetari,
grupa musculara cea mai antrenata, precum si grafice. Datele pot fi filtrate pe zi, saptamana, luna sau
total, cu navigare inainte si inapoi intre perioade.

## Tehnologii folosite

Aplicatia este scrisa in Kotlin si foloseste Jetpack Compose pentru interfata, impreuna cu Material 3
pentru aspectul vizual. Ecranele sunt descrise direct in cod, fara fisiere XML de layout.

- Kotlin si Jetpack Compose pentru interfata
- Material 3 pentru tema si componente
- Navigation Compose pentru navigarea intre ecrane
- Room pentru baza de date locala
- Retrofit si Gson pentru cererile HTTP si deserializarea JSON
- SharedPreferences pentru sesiune si datele de profil
- ViewModel, StateFlow si coroutines pentru gestionarea starii si a operatiilor asincrone

## Structura proiectului

```
app/src/main/java/com/fitpulse/app/
├── MainActivity.kt              Punctul de intrare al aplicatiei
├── data/
│   ├── AppDatabase.kt           Configurarea bazei de date Room
│   ├── Exercise.kt              Entitatea pentru exercitii
│   ├── ExerciseDao.kt           Operatiile pe exercitii
│   ├── ExerciseRepository.kt    Accesul la exercitii
│   ├── User.kt                  Entitatea pentru utilizatori
│   ├── UserDao.kt               Operatiile pe utilizatori
│   ├── PredefinedExercises.kt   Lista de exercitii comune
│   ├── SessionManager.kt        Sesiunea si profilul in SharedPreferences
│   └── UserProfile.kt           Datele de profil si calculul IMC
├── navigation/
│   ├── AppNavigation.kt         NavHost, rutele si bara de jos
│   └── Destination.kt           Definirea rutelor
├── network/
│   ├── RetrofitClient.kt        Configurarea Retrofit
│   ├── WgerApiService.kt        Cele doua endpointuri HTTP
│   └── WgerDtos.kt              Modelele pentru raspunsurile JSON
├── ui/
│   ├── components/
│   │   └── SearchableDropdownField.kt   Camp cu lista filtrabila
│   ├── screens/                 Ecranele aplicatiei
│   ├── theme/                   Culori, tipografie si tema
│   └── viewmodel/               AuthViewModel, ExerciseViewModel, MuscleGroupViewModel
└── util/
    ├── PasswordHasher.kt        Generarea hash-ului de parola
    ├── DateUtils.kt             Lucrul cu date si perioade
    └── Validators.kt            Validari pentru email si parola
```

## Navigare si ecrane

Navigarea este gestionata in `AppNavigation.kt`, printr-un `NavHost`. Rutele sunt definite intr-o clasa
`Destination`: login, register, home, exerciseList, addExercise, statistics si profile. Accesul la
sectiunile principale se face dintr-o bara de navigare de jos, care este ascunsa pe ecranele de
autentificare.

Ecranele aplicatiei sunt: Login, Register, Home, lista de exercitii, adaugarea unui exercitiu,
statistici si profil.

## Autentificare

Inregistrarea si autentificarea sunt tratate in `AuthViewModel`, impreuna cu tabelul de utilizatori din
Room.

La inregistrare, aplicatia verifica daca emailul nu este deja folosit, apoi salveaza contul. Parola nu
este pastrata ca text simplu. Se genereaza o valoare aleatorie unica, un salt, care este combinata cu
parola, iar rezultatul este trecut prin algoritmul SHA-256. In baza de date sunt salvate doar hash-ul si
salt-ul, niciodata parola in clar. Aceasta logica se afla in `PasswordHasher.kt`.

La autentificare, parola introdusa este trecuta prin acelasi proces cu salt-ul salvat, iar rezultatul
este comparat cu hash-ul din baza de date. Daca emailul nu exista sau parola nu se potriveste,
utilizatorul primeste un mesaj de eroare. Astfel, un cont trebuie creat inainte de autentificare, iar o
parola gresita este respinsa.

Sesiunea curenta este retinuta in SharedPreferences, prin `SessionManager`, ceea ce permite intrarea
directa in aplicatie la deschiderile urmatoare. La deconectare se sterge doar sesiunea, iar datele
contului raman salvate.

## Stocare locala

Datele sunt salvate local cu Room, in `AppDatabase`, care contine doua entitati: `Exercise` si `User`,
fiecare cu propriul DAO. Accesul la exercitii se face printr-un repository.

Exercitiile sunt legate de utilizatorul care le-a creat, prin campul `userId`, iar interogarile din
`ExerciseDao` filtreaza dupa acest camp. In acest fel, fiecare cont vede doar exercitiile proprii.
Datele de profil sunt pastrate in SharedPreferences, separat pentru fiecare utilizator.

Exercitiile sunt afisate intr-o lista derulabila, realizata cu `LazyColumn`, grupata pe zile.

## Cereri HTTP si deserializare JSON

Pentru datele venite din internet, aplicatia foloseste Retrofit cu un convertor Gson, configurat in
`RetrofitClient.kt`. Interfata `WgerApiService` defineste doua cereri catre API-ul public wger:

- `exercisecategory` pentru categoriile de exercitii
- `muscle` pentru grupele musculare

Raspunsurile in format JSON sunt deserializate in modele Kotlin definite in `WgerDtos.kt` si sunt
incarcate ca optiuni in campurile din ecranul de adaugare a unui exercitiu. Aceste campuri folosesc o
lista derulabila care se filtreaza in functie de textul introdus. Erorile de retea sunt tratate, iar
utilizatorul poate reincerca incarcarea.
