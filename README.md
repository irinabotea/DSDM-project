# FitPulse

**FitPulse** este o aplicație Android destinată utilizatorilor care doresc să își monitorizeze
activitatea fizică și progresul în timp. Aplicația permite crearea și gestionarea unor liste de
exerciții personalizate, planificarea antrenamentelor și înregistrarea rezultatelor. Pe baza datelor
colectate, aplicația generează statistici relevante (număr de antrenamente, evoluția performanței,
frecvența exercițiilor).

Scopul principal al aplicației este de a încuraja un stil de viață activ și de a oferi utilizatorilor
o imagine clară asupra progresului lor fizic.

---

## Cuprins
- [Funcționalități](#-funcționalități)
- [Tehnologii folosite](#-tehnologii-folosite)
- [Arhitectura proiectului](#-arhitectura-proiectului)
- [Structura codului](#-structura-codului)
- [Cerințe de mediu](#-cerințe-de-mediu)
- [Instalare și rulare](#-instalare-și-rulare)
- [Ecranele aplicației](#-ecranele-aplicației)
- [Baza de date](#-baza-de-date)
- [Comunicarea cu serverul (API)](#-comunicarea-cu-serverul-api)
- [Acoperirea cerințelor proiectului](#-acoperirea-cerințelor-proiectului)

---

## Funcționalități

- **Autentificare** – ecrane de **Login** și **Register** cu validarea câmpurilor.
- **Sesiune persistentă** – utilizatorul rămâne logat între deschideri (SharedPreferences); opțiune
  de **Logout**.
- **Profil utilizator** – salvarea datelor corporale (greutate, înălțime, vârstă, gen, obiectiv de
  fitness) și calcularea automată a **IMC (BMI)**.
- **Listă de exerciții** – adăugarea, vizualizarea și stocarea exercițiilor într-o bază de date locală,
  afișate într-o listă derulabilă (scrollabilă), **grupate pe zile**.
- **Adăugare exerciții** – din catalogul de exerciții comune (selectabile rapid) sau personalizate;
  câmpuri de **categorie** și **grupă musculară** sub formă de liste derulabile care se filtrează pe
  măsură ce scrii.
- **Selector de dată** – fiecare exercițiu primește o dată (implicit ziua curentă), printr-un
  DatePicker.
- **Statistici și grafice** – număr total de exerciții, seturi, repetări, grupă musculară cea mai
  antrenată, medii, plus **grafic cu bare** și **grafic circular (donut)**. Datele pot fi filtrate pe
  **Zi / Săptămână / Lună / Total**, cu navigare înainte/înapoi între perioade.
- **Date din internet** – categoriile și grupele musculare sunt încărcate dintr-un **API public**
  (wger) prin cereri HTTP, deserializate din JSON.

---

## Tehnologii folosite

| Tehnologie | Rol |
|------------|-----|
| **Kotlin** | Limbajul de programare |
| **Jetpack Compose** | UI declarativ (fără XML pentru layout) |
| **Material 3** | Componente și temă vizuală |
| **Navigation Compose** | Navigarea între ecrane + bară de jos |
| **Room** | Baza de date locală (peste SQLite) |
| **Retrofit + Gson** | Cereri HTTP și deserializare JSON |
| **ViewModel + StateFlow** | Gestionarea stării și separarea logicii de UI |
| **SharedPreferences** | Stocarea sesiunii și a datelor de profil |
| **Coroutines** | Operații asincrone (bază de date, rețea) |

---

## Arhitectura proiectului

Aplicația respectă o separare clară pe straturi:

```
UI (Compose Screens)  ──►  ViewModel (stare)  ──►  Repository / SharedPreferences / API
                                                         │
                                                         ▼
                                                Room (SQLite)  /  wger API
```

- **UI (ecrane Compose)** – afișează starea și trimite evenimente către ViewModel.
- **ViewModel** – deține starea (`StateFlow` / `mutableStateOf`) și apelează sursele de date.
- **Repository / DAO** – accesul la baza de date Room.
- **Network** – accesul la API-ul extern prin Retrofit.

---

## Structura codului

```
app/src/main/java/com/fitpulse/app/
├── MainActivity.kt              # Punctul de intrare (setContent)
├── data/                        # Stratul de date
│   ├── AppDatabase.kt           # Baza de date Room (singleton)
│   ├── Exercise.kt              # Entitate (tabel exerciții)
│   ├── ExerciseDao.kt           # Operații pe baza de date (DAO)
│   ├── ExerciseRepository.kt    # Acces async la DAO
│   ├── PredefinedExercises.kt   # Catalog local de exerciții comune
│   ├── SessionManager.kt        # Sesiune + profil (SharedPreferences)
│   └── UserProfile.kt           # Model profil + calcul IMC
├── navigation/
│   ├── AppNavigation.kt         # NavHost, rutele și bara de jos
│   └── Destination.kt           # Definirea rutelor
├── network/                     # Stratul de rețea
│   ├── RetrofitClient.kt        # Configurarea Retrofit
│   ├── WgerApiService.kt        # Cele 2 endpoint-uri HTTP
│   └── WgerDtos.kt              # Modele pentru JSON
├── ui/
│   ├── components/
│   │   └── SearchableDropdownField.kt  # Câmp cu listă derulabilă + filtrare
│   ├── screens/                 # Ecranele aplicației
│   ├── theme/                   # Culori, tipografie, temă
│   └── viewmodel/               # ExerciseViewModel, MuscleGroupViewModel
└── util/
    ├── DateUtils.kt             # Lucrul cu date/perioade
    └── Validators.kt            # Validări (email, parolă etc.)
```

---

## Cerințe de mediu

- **Android Studio** (versiune recentă)
- **JDK 11**
- **Android SDK** – `minSdk = 24`, `targetSdk = 36`
- Un **emulator** sau un **dispozitiv fizic** Android (cu conexiune la internet pentru funcția de
  încărcare a categoriilor/grupelor musculare)

Versiuni cheie: Kotlin `2.4.0`, AGP `9.2.1`, Compose BOM `2026.05.01`, Room `2.8.4`, Retrofit `2.11.0`.

---

## ▶ Instalare și rulare

### Varianta 1 — Android Studio (recomandat)
1. Clonează depozitul:
   ```bash
   git clone https://github.com/irinabotea/DSDM-project.git
   ```
2. Deschide proiectul în **Android Studio** și așteaptă sincronizarea Gradle.
3. Alege un emulator sau conectează un telefon (cu *USB debugging* activat).
4. Apasă butonul **Run ▶**.

### Varianta 2 — Linia de comandă
Din directorul rădăcină al proiectului:
```bash
# Construiește aplicația (APK debug)
./gradlew assembleDebug

# Instalează pe un emulator/dispozitiv pornit
./gradlew installDebug
```
APK-ul rezultat se găsește în `app/build/outputs/apk/debug/app-debug.apk`.

---

## Ecranele aplicației

| Ecran | Descriere |
|-------|-----------|
| **Login** | Autentificare cu email și parolă (cu validare). |
| **Register** | Crearea unui cont nou (nume, email, parolă, confirmare). |
| **Home** | Ecran principal cu acces rapid la exerciții și statistici. |
| **My Exercises** | Lista exercițiilor salvate, grupate pe zile; buton **+** pentru adăugare. |
| **Add Exercise** | Formular de adăugare cu catalog, categorii/grupe musculare din API și selector de dată. |
| **Statistics** | Statistici și grafice, cu filtrare pe Zi/Săptămână/Lună/Total. |
| **Profile** | Date corporale, IMC și buton de Logout. |

Navigarea principală se face prin **bara de jos** (Home, Exercises, Statistics, Profile).

---

## Baza de date

Aplicația folosește **Room** (un strat peste SQLite) pentru stocarea locală:

- **Entitate `Exercise`** – câmpuri: `id`, `name`, `muscleGroup`, `category`, `sets`, `reps`, `date`.
- **DAO (`ExerciseDao`)** – operații: citire (`Flow`), inserare, ștergere, numărare.
- **`AppDatabase`** – singleton cu acces unic la baza de date.
- **`ExerciseRepository`** – intermediază accesul asincron la DAO.

Datele rămân salvate între deschiderile aplicației.

> Notă: în modul **offline**, exercițiile salvate local rămân disponibile; doar sugestiile de
> categorii/grupe musculare din API necesită internet.

---

## Comunicarea cu serverul (API)

Aplicația face cereri HTTP către API-ul public **[wger](https://wger.de/en/software/api)**:

1. `GET /api/v2/exercisecategory/` – categoriile de exerciții.
2. `GET /api/v2/muscle/` – grupele musculare.

Rezultatele JSON sunt **deserializate** (Gson) și afișate ca opțiuni selectabile în ecranul de
**Add Exercise**. Erorile de rețea sunt tratate, iar utilizatorul poate reîncerca (**Retry**).

---