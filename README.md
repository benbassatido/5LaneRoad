# 🚗💥 HW2 – Car Crash Game (Android)

## 📌 General Description
An Android game where the player controls a car moving between lanes and tries to avoid falling spikes 💣.  
During the game, the player can collect coins 🪙, increase the traveled distance 📏, and save high scores that also include geographic location 📍.

This project was developed as part of an Android development course assignment 🎓.

---

## 🎮 Game Rules
- 🚘 The car moves left and right between **5 lanes**
- ⬇️ Spikes fall from the top of the screen
- 💥 Hitting a spike reduces one life
- ❤️ The player starts with **3 lives**
- 🪙 Collecting a coin adds **+10 meters** to the distance
- ☠️ When all lives are lost → **Game Over**

---

## 🕹️ Control Modes
- 🎛️ **Buttons Mode** – Left / Right buttons
- 📱 **Sensor Mode** – Control using device tilt
- ⚡ **Fast Mode** – Increased game speed

---

## 🔊 Sound
- 💥 A crash sound (`boom.mp3`) is played when the car hits a spike
- 🎧 Sound playback is handled by `SingleSoundPlayer` to avoid performance issues and sound overlap

---

## 🗺️ Scores & Map
- 🏁 At game over, the player can save their name and score
- 📍 Scores are saved together with the **current geographic location**
- 🗺️ A map view is implemented using **Google Maps**
- 📌 Selecting a score focuses the map on the saved location using a marker

---

## 🧱 Architecture
- 🖥️ **MainActivity** – UI, controls, and sound handling
- 🧠 **GameManager** – Game logic only
- 💾 **ScoreManager** – High score management and location handling
- 🗺️ **MapFragment** – Google Maps display
- 🧩 Clear separation between game logic and UI components

---

## 🛠️ Technologies & Libraries
- 🧑‍💻 Kotlin
- 🤖 Android SDK
- 🗺️ Google Maps SDK
- 📡 Fused Location Provider
- 🧬 Gson
- 💾 SharedPreferences
- 🔊 MediaPlayer

