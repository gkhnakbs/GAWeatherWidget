<div align="center">

  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_cloudy.webp" alt="Weather App Logo" width="120" />

  <h1>Weather App with Widget</h1>

  <p>Gerçek zamanlı hava durumu takibi + Ana ekran widget'ı</p>

  ![Android](https://img.shields.io/badge/Android-API%2026+-3DDC84?logo=android&logoColor=white)
  ![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white)
  ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?logo=jetpackcompose&logoColor=white)
  ![Jetpack Glance](https://img.shields.io/badge/Jetpack%20Glance-Widget-FF6F00?logo=android&logoColor=white)
  ![License](https://img.shields.io/badge/License-MIT-yellow)

</div>

---

## 📱 Uygulama

Güncel hava durumu bilgilerini sunan modern bir Android uygulaması. Uygulamayı açmana gerek kalmadan bilgilere ulaşmak için **Jetpack Glance** ile geliştirilmiş bir ana ekran widget'ı da içeriyor.

## ✨ Özellikler

| Özellik | Detay |
|---|---|
| 🌡️ Canlı Hava Durumu | Retrofit ile anlık API verisi |
| 🟧 Ana Ekran Widget'ı | **Jetpack Glance** ile Compose tabanlı widget |
| 🔄 Otomatik Güncelleme | WorkManager ile arka plan senkronizasyonu |
| 💾 Yerel Depolama | DataStore ile veri kalıcılığı |
| 🎨 Modern UI | Jetpack Compose + Material 3 |

## 🛠️ Teknoloji Yığını

```
UI          → Jetpack Compose + Material 3
Widget      → Jetpack Glance         ← ✨
DI          → Dagger Hilt
Network     → Retrofit + Gson
Storage     → DataStore
Background  → WorkManager + Hilt Worker
```

## 🏗️ Mimari

```
app/
├── data/
│   ├── api/          # Retrofit servisleri
│   └── di/           # Hilt modülleri
├── domain/           # İş mantığı
└── presentation/
    ├── ui/
    │   ├── mainScreen/   # Ana ekran
    │   └── widget/       # Jetpack Glance widget ✨
    └── theme/
```

## 🚀 Kurulum

```bash
git clone <repo-url>
cd WeatherAppWithWidget
./gradlew installDebug
```

## 📄 Lisans

Bu proje [MIT Lisansı](LICENSE) ile lisanslanmıştır.

---

<div align="center">
  <sub>Widget, <strong>Jetpack Glance</strong> kullanılarak Compose söz dizimiyle yazıldı.</sub>
</div>
