# 🎵 Music Player App

A simple and efficient **Music Player App** for Android that allows users to play, pause, seek, and navigate through audio files stored on their device.

## 📌 Features

- 🎶 **Play, Pause, Next, Previous Controls**
- ⏩ **Fast Forward & Rewind** (10 seconds)
- 📂 **Auto-fetch Audio Files from Device Storage**
- 📊 **Seek Bar & Time Progress Display**
- 🎛️ **Custom UI with Animated Album Art**
- 🔄 **Auto-play Next Song on Completion**

## 📱 Screenshots
![Home Screen](Screenshots/home_screen.png)
![Now Playing](Screenshots/play_screen.png)
>

## ⚙️ Tech Stack

- **Java** - Main programming language
- **Android SDK** - Development framework
- **MediaPlayer API** - Audio playback
- **Dexter Library** - Runtime permission handling
- **SeekBar & Handlers** - UI updates & progress tracking
- **XML & ConstraintLayout** - UI design

## 🚀 How to Run the App

1. Clone the repository:
   ```sh
   git clone https://github.com/YourUsername/MusicPlayerApp.git
   ```
2. Open the project in **Android Studio**.
3. Connect a physical device or start an emulator.
4. Click **Run ▶️** to install and launch the app.

## 🔧 Permissions Used

- `READ_EXTERNAL_STORAGE` (Android 12 & below) - To fetch music files
- `READ_MEDIA_AUDIO` (Android 13+) - Required for accessing audio files

## 🛠️ How It Works

1. **Fetching Songs:** The app uses `MediaStore.Audio.Media` to retrieve all audio files from storage.
2. **Playback Controls:** `MediaPlayer` API manages play, pause, seek, and next/previous navigation.
3. **SeekBar & UI Updates:** A `Handler` updates the SeekBar and track progress dynamically.
4. **Permissions Handling:** The **Dexter library** ensures proper permissions are granted before accessing audio files.

## 🐛 Known Issues & Fixes

- **SeekBar not updating properly?** ✅ Fixed by adding a `Handler` to update `txtsstart` every 500ms.
- **App crashes on song click?** ✅ Fixed by properly passing `String` file paths instead of `File` objects.
- **Songs not loading?** ✅ Ensure the app has proper storage/audio access permissions.

## 💡 Future Enhancements

- 🌐 **Online Music Streaming**
- 🎨 **Dark Mode UI**
- 🔊 **Equalizer Integration**
- 📜 **Lyrics Display**

## 📜 License

This project is **open-source** under the [MIT License](LICENSE).

## 👨‍💻 Author

**[Harsh](https://github.com/HarshSindhi1109)** - Passionate Java Full Stack Developer 🚀

I hope you find this repo helpful.
