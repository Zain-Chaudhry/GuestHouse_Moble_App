# Diamond Guest House

Android app for managing **guest house operations**: bookings, guest records, check-ins/check-outs, and simple reporting. Built with **Jetpack Compose**, **Material 3**, and a **local Room** database, with **Firebase Authentication** (including Google Sign-In) for accounts.

---

## Features

| Area | What you can do |
|------|-----------------|
| **Auth** | Sign up, log in, forgot password, Google Sign-In |
| **Dashboard** | Home with revenue snapshot, today’s check-ins / check-outs, shortcuts |
| **Rooms & guests** | Add guests (local & foreigner flows), room assignment, dates, amounts |
| **Search & booking** | Find guests by CNIC or passport; select customers and confirm booking |
| **Operations** | View today’s bookings and check-outs |
| **Reports** | Daily / weekly / monthly reports with check-in counts and income |
| **Settings** | Sign out and account-related actions |

---

## Tech stack

| Layer | Choice |
|--------|--------|
| **UI** | Jetpack Compose, Material 3, Compose ConstraintLayout |
| **Navigation** | Navigation 3 (`NavDisplay`, `NavKey`, Kotlin Serialization) |
| **DI** | [Koin](https://insert-koin.io/) (Android, Compose, ViewModels) |
| **Local data** | Room |
| **Auth** | Firebase Auth, Google Credential Manager / Credentials API |
| **Async** | Kotlin coroutines, Flow |
| **Language** | Kotlin |

---

## Architecture

This project uses a **mix of MVVM and MVI**:

- **MVVM:** Compose screens, **ViewModels**, **StateFlow** / **Flow** for state, repositories, Room, and **Koin** for DI.
- **MVI:** User actions go through **`submitUserEvent` → `onUserEvent`** with sealed **`UserEvent`** types; **`UiState`** is updated in one direction; one-off UI effects (navigation, toasts) use sealed **`UiEvent`** and **`SharedFlow`**.

Together, that gives MVVM structure with MVI-style events and unidirectional state updates.

---

## Requirements

- **Android Studio** (recent stable; project uses AGP **8.13.x**)
- **JDK** 17+ (recommended for current Android Gradle Plugin)
- **Android SDK** — `compileSdk` / `targetSdk` **36**, **minSdk** **27**
- Emulator or device with a Google account if you test Google Sign-In

---

## Getting started

1. **Clone the repository**

   ```bash
   git clone https://github.com/zain-chaudhry/GuestHouse_Moble_App.git
   cd GuestHouse_Moble_App
   ```

2. **Open** the project in Android Studio (open the root folder that contains `settings.gradle.kts`).

3. **Sync** Gradle and wait for dependencies to resolve.

4. **Run**

   - Select a device or emulator → **Run** the `app` configuration.

### Build from the command line

```bash
# Windows
gradlew.bat assembleDebug

# macOS / Linux
./gradlew assembleDebug
```

---

## Project layout (high level)

```
app/src/main/java/com/example/diamondguesthouse/
├── appNavigation/          # Nav keys, NavDisplay, navigation commands
├── core/                     # Shared UI, utils, services
├── data/                     # Room, persistence, mappers
├── domain/                   # models
├── di/                       # Koin modules
├── presentation/             # Feature screens & ViewModels
└── theme/                    # Theme, colors, typography
```

---

## Versioning

- **versionName:** `1.0` (see `app/build.gradle.kts` → `defaultConfig`)

---

## License

This project is provided as-is for the Diamond Guest House app. Add a `LICENSE` file in the repository if you want a standard open-source license.

---

## Contributing

Issues and pull requests are welcome. Please keep changes focused, match existing Kotlin/Compose style, and run a **Debug** build before submitting.
