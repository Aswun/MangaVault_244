# MangaVault: Digital Comic Collection Tracker 📚

**MangaVault** adalah aplikasi Android native yang dirancang untuk membantu penggemar manga mengelola koleksi bacaan mereka. Aplikasi ini menerapkan pendekatan **Offline-First**, memungkinkan pengguna menyimpan dan mengatur koleksi manga secara lokal, sambil tetap menyediakan fitur pencarian online yang terintegrasi dengan **Jikan API (MyAnimeList)**.

Aplikasi ini dibangun menggunakan **Kotlin** dan **Jetpack Compose** dengan arsitektur **MVVM (Model-View-ViewModel)**.

---

## ✨ Fitur Utama

### 🔐 1. Local Authentication (Multi-User)
- Sistem login lokal yang aman (password di-hash).
- Mendukung banyak pengguna dalam satu perangkat.
- Data koleksi manga terpisah untuk setiap pengguna (Private Collection).
- **Catatan:** Tidak memerlukan koneksi internet untuk login.

### 📚 2. Library Management (Offline)
- Menampilkan daftar koleksi manga pribadi.
- CRUD Lengkap:
  - **Edit:** Mengubah status baca, jumlah volume yang dimiliki, dan rating pribadi.
  - **Delete:** Menghapus manga dari koleksi.
  - **Sort:** Mengurutkan berdasarkan Judul, Status, atau Rating.

### 🔍 3. Manga Search (Online)
- Pencarian manga real-time menggunakan **Jikan REST API v4**.
- Menampilkan detail lengkap (Sinopsis, Score, Status Publikasi).
- Langsung menambahkan manga hasil pencarian ke Library lokal.

### ⚙️ 4. Settings
- Dukungan **Dark Mode** dan **Light Mode**.
- Manajemen sesi (Logout).

---

## 🛠️ Tech Stack & Libraries

Aplikasi ini dibangun menggunakan teknologi modern Android Development:

| Kategori | Teknologi / Library |
| :--- | :--- |
| **Bahasa** | Kotlin |
| **UI Framework** | Jetpack Compose (Material Design 3) |
| **Architecture** | MVVM + Repository Pattern |
| **Database** | Room Database (SQLite) |
| **Networking** | Retrofit + OkHttp |
| **Async Process** | Kotlin Coroutines & Flow |
| **Dependency Injection** | Manual DI (AppContainer) |
| **Image Loading** | Coil |
| **Navigation** | Jetpack Navigation Compose |
| **Data Storage** | DataStore Preferences (Session Management) |
| **API** | [Jikan API v4](https://jikan.moe/) (Unofficial MyAnimeList API) |

---

## 📱 Screenshots

| Login Screen | Library (Home) | Search Manga |
|:---:|:---:|:---:|
| <img width="320" height="693" alt="image" src="https://github.com/user-attachments/assets/a8b93e9e-99cc-457b-88e1-610e7b073ce5" /> | <img width="320" height="693" alt="image" src="https://github.com/user-attachments/assets/e4f71a3b-f7ab-4818-8691-c5e5ce728d2d" /> | <img width="320" height="693" alt="image" src="https://github.com/user-attachments/assets/24d7e859-fa3e-4597-85fa-50c98bc2fd66" /> |

| Detail Manga | Settings (Dark Mode) |
|:---:|:---:|
| <img width="320" height="693" alt="image" src="https://github.com/user-attachments/assets/c908cd79-9d0c-4522-9a8c-04bff5c1fc39" /> | <img width="320" height="693" alt="image" src="https://github.com/user-attachments/assets/5e00f5bd-4b8a-4b5e-abc3-d110fb07fe6c" /> |

---

## 🚀 Cara Instalasi & Menjalankan Aplikasi

1.  **Clone Repository**
    ```bash
    git clone [https://github.com/username-anda/MangaVault.git](https://github.com/aswun/MangaVault_244.git)
    ```
2.  **Buka di Android Studio**
    * Buka Android Studio -> File -> Open -> Pilih folder `MangaVault`.
3.  **Sync Gradle**
    * Tunggu hingga proses sinkronisasi Gradle selesai.
4.  **Run Application**
    * Jalankan pada Emulator atau Perangkat Fisik (Minimal Android 8.0 / Oreo).

---

## 🔑 Akun Demo (Default Credentials)

Saat pertama kali dijalankan, database akan otomatis terisi dengan akun berikut untuk pengujian:

| Username | Password | Keterangan |
| :--- | :--- | :--- |
| **aswin** | `aswin123` | Akun Utama |
| **ardhi** | `ardhi123` | Akun Kedua |

> **Catatan:** Password disimpan dalam bentuk Hash di database demi keamanan.

---

## 📂 Struktur Database (ERD)

Aplikasi menggunakan **Room Database** dengan dua entitas utama:

1.  **UserEntity**: Menyimpan data login (`userId`, `username`, `passwordHash`).
2.  **MangaEntity**: Menyimpan data koleksi (`mangaId`, `userId`, `title`, `imageUrl`, `status`, `volumeOwned`, `rating`).

Relasi: **One-to-Many** (Satu User memiliki banyak Manga). Tabel Manga menggunakan *Composite Primary Key* (`mangaId` + `userId`) untuk memastikan satu manga bisa dimiliki oleh user yang berbeda.

---

## 🤝 Kontribusi

Proyek ini dikembangkan sebagai bagian dari tugas Pengembangan Aplikasi Mobile. Jika Anda ingin berkontribusi:

1.  Fork repository ini.
2.  Buat branch fitur baru (`git checkout -b fitur-baru`).
3.  Commit perubahan Anda (`git commit -m 'Menambahkan fitur baru'`).
4.  Push ke branch (`git push origin fitur-baru`).
5.  Buat Pull Request.

---

## 📄 Lisensi

Copyright © 2026 **Aswin Lutfian Prasetyo**.
All Rights Reserved.
