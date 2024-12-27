# KazeMelody

**KazeMelody** is a Java Swing-based GUI application with MySQL CRUD functionality designed for managing and playing anime opening songs.

## Features

### Song Data CRUD:
- Add, view, update, and delete song information such as:
    - Song Name
    - Artist Name
    - Album
    - Song File Path

### Music Player:
- Supports `.mp3` format with playback controls (Play, Pause).
- Autoplay feature with random song selection.

### Intuitive Interface:
- Simple and user-friendly design.

### Stable and Lightweight:
- Optimized for fast performance.

## How to Use

### MySQL Database Setup
1. Start the database server using software like Laragon or XAMPP.
2. Create a database named **`musiclibrary`**.
3. Create a table named **`music`** with the following columns:
    - `songName` (varchar)
    - `artistName` (varchar)
    - `album` (varchar)
    - `pathSong` (varchar)

### Clone Repository
```bash
git clone https://github.com/MRamadhan000/MusicManagementGUI.git
```

### Running the Application
1. Ensure the database server is running.
2. Open the project in an IDE such as IntelliJ IDEA or Eclipse.
3. Run the main program file (`MainApp.java`).

### Adding Song Data
1. Fill in the song information in the input form within the application.
2. Click the **Save** button to add the data to the database.

### Playing Music
1. Select a song from the available list.
2. Click the **Play** button to play the song.
3. Use the **Pause** button to pause playback.
4. If the song is finished it will start playing the songs randomly.

## Technologies Used
- **Java Swing**: For the user interface.
- **MySQL**: For song data storage.
- **JlPlayer**: For playing `.mp3` files.

## License
This project is licensed under the [MIT License](LICENSE).

---

**KazeMelody**: Bring the spirit of anime to your music! 🎶✨
