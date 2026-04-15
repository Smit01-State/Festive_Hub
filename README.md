# Festive Hub 🎊

Festive Hub is an Android application designed to help users discover and manage festive events. Whether you are looking for local celebrations or managing your own events, Festive Hub provides a comprehensive platform for all your festive needs.

## 🚀 Features

- **User Authentication**: Secure Login and Registration system.
- **Event Discovery**: Browse through various event categories.
- **Event Management**: Admin/Manager panel to add, edit, and delete events.
- **Wishlist & Cart**: Save your favorite events for later or manage your bookings.
- **Profile Management**: View and update user profile details.
- **Responsive UI**: Built with Material Design and Scalable DP (SDP) for compatibility across various screen sizes.

## 🛠️ Tech Stack

- **Language**: Java
- **Networking**: [Volley](https://github.com/google/volley) for API requests.
- **Image Loading**: [Glide](https://github.com/bumptech/glide) for efficient image loading and caching.
- **UI Components**: Material Components for Android.
- **Data Persistence**: SharedPreferences for session management and SQLite for local data.
- **Backend**: PHP with MySQL (Expected at `http://10.0.2.2/FinalGpgApp/`).

## 📁 Project Structure

- `com.Festive_Hub.android`: Main application package containing Activities and Adapters.
- `com.Festive_Hub.android.network`: API clients and networking logic.
- `com.Festive_Hub.android.Hash`: Security utilities (MD5 Hashing).

## ⚙️ Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/Festive_Hub.git
   ```
2. **Open in Android Studio**:
   - File > Open > Select the `Festive_Hub` folder.
3. **Configure Backend**:
   - Ensure your local server (XAMPP/WAMP) is running.
   - Host the `FinalGpgApp` PHP scripts.
   - Update `BASE_URL` in `ApiClient.java` if your server IP is different.
4. **Build and Run**:
   - Connect an Android device or use an emulator.
   - Click the 'Run' button in Android Studio.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
