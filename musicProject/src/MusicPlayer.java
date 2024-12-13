import processing.core.PApplet;
import processing.core.PImage;
import ddf.minim.*;

import java.io.File;

// Define the Song class to store song information
class Song {
    String name;       // Song name
    AudioPlayer player;  // Audio player

    // Constructor to initialize song name and audio player
    Song(String name, AudioPlayer player) {
        this.name = name;
        this.player = player;
    }
}

public class MusicPlayer extends PApplet {
    PImage backgroundImage1; // Background image 1
    PImage backgroundImage2; // Background image 2
    PImage addSongBackground; // Background image for adding songs
    Minim minim;             // Minim library instance for handling audio
    Song[] songs = new Song[10]; // Array to store up to 10 songs

    int songCount = 0; // Current number of songs

    int currentPage = 0;     // Index of the current displayed page
    int currentMusicIndex = 0; // Index of the currently playing music
    int barWidth = 600;      // Width of the progress bar

    boolean isAddingSong = false; // Whether adding a song
    String statusMessage = "";   // Status message

    // Main method to start the PApplet application
    public static void main(String[] args) {
        PApplet.main("MusicPlayer");
    }

    // Set window size
    public void settings() {
        size(1500, 1250);
    }

    // Setup method called once when the program starts
    public void setup() {
        backgroundImage1 = loadImage("background1.png"); // Load background image 1
        if (backgroundImage1 == null) {
            println("Error loading background1.png");
        }
        backgroundImage2 = loadImage("background2.png"); // Load background image 2
        if (backgroundImage2 == null) {
            println("Error loading background2.png");
        }
        addSongBackground = loadImage("background2.png"); // Load add song background image
        if (addSongBackground == null) {
            println("Error loading addSongBackground.png");
        }
        minim = new Minim(this); // Initialize Minim library

        // Initialize song information
        try {
            songs[0] = new Song("Song 1", minim.loadFile("data/music1.mp3")); // Load first song
            if (songs[0].player == null) {
                println("Error loading music1.mp3");
            }
            songs[1] = new Song("Song 2", minim.loadFile("data/music2.mp3")); // Load second song
            if (songs[1].player == null) {
                println("Error loading music2.mp3");
            }
            songs[2] = new Song("Song 3", minim.loadFile("data/music3.mp3")); // Load third song
            if (songs[2].player == null) {
                println("Error loading music3.mp3");
            }
            songs[3] = new Song("Song 4", minim.loadFile("data/music4.mp3")); // Load fourth song
            if (songs[3].player == null) {
                println("Error loading music4.mp3");
            }
            songCount = 4; // Update current number of songs
        } catch (Exception e) {
            println("Error loading audio files: " + e.getMessage()); // Capture and print error loading audio files
        }
    }

    // Draw method called repeatedly to update screen content
    public void draw() {
        background(50); // Set background color

        if (isAddingSong) {
            drawAddSongPage(); // Draw add song page
        } else if (currentPage == 0) {
            drawPage1(); // Draw page 1
        } else if (currentPage == 1) {
            drawPage2(); // Draw page 2
        }
    }

    // Draw the content of page 1
    void drawPage1() {
        if (backgroundImage1 != null) {
            image(backgroundImage1, 0, 0); // Draw background image 1
        } else {
            fill(255, 0, 0);
            textSize(32);
            textAlign(CENTER, CENTER);
            text("Error: background1.png not loaded", width / 2, height / 2);
        }
        fill(0);
        textSize(48);
        textAlign(CENTER, CENTER);
        drawButton("START", width / 2, height / 2 + 100); // Draw button "START"
        drawButton("ADD SONG", width / 2, height / 2 + 200); // Draw button "ADD SONG"

        // Display status message
        fill(255, 0, 0);
        textSize(24);
        textAlign(CENTER, TOP);
        text(statusMessage, width / 2, height / 2 + 300);
    }

    // Draw the content of page 2
    void drawPage2() {
        if (backgroundImage2 != null) {
            image(backgroundImage2, 0, 0); // Draw background image 2
        } else {
            fill(255, 0, 0);
            textSize(32);
            textAlign(CENTER, CENTER);
            text("Error: background2.png not loaded", width / 2, height / 2);
        }
        fill(0);
        textSize(48);
        textAlign(CENTER, CENTER);

        // Display the name of the currently playing song
        textSize(36);
        if (currentMusicIndex >= 0 && currentMusicIndex < songCount && songs[currentMusicIndex] != null) {
            text(songs[currentMusicIndex].name, width / 2, height / 2 - 200);
        } else {
            text("No valid song selected", width / 2, height / 2 - 200);
        }

        textSize(36);
        text("press space to start/pause", width / 2, height / 2 - 100); // Prompt information
        drawButton("BACK", width / 2, height / 2 + 250); // Draw button "BACK"
        drawButton("switch up", width / 2, height / 2 + 150); // Draw button "switch up"
        drawButton("switch down", width / 2, height / 2 + 200); // Draw button "switch down"

        // Display status message
        fill(255, 0, 0);
        textSize(24);
        textAlign(CENTER, TOP);
        text(statusMessage, width / 2, height / 2 + 350);

        // Check if there is a valid song and it's playing
        if (currentMusicIndex >= 0 && currentMusicIndex < songCount && songs[currentMusicIndex] != null && songs[currentMusicIndex].player != null && songs[currentMusicIndex].player.isPlaying()) {
            // Draw waveform
            for (int i = 0; i < songs[currentMusicIndex].player.bufferSize() - 1; i++) {
                float x1 = map(i, 0, songs[currentMusicIndex].player.bufferSize(), 0, width); // Map x coordinate
                float x2 = map(i + 1, 0, songs[currentMusicIndex].player.bufferSize(), 0, width); // Map next x coordinate

                // Draw left channel waveform
                stroke(255, 0, 0);
                line(x1, 50 + songs[currentMusicIndex].player.left.get(i) * 50, x2, 50 + songs[currentMusicIndex].player.left.get(i + 1) * 50);

                // Draw right channel waveform
                line(x1, 150 + songs[currentMusicIndex].player.right.get(i) * 50, x2, 150 + songs[currentMusicIndex].player.right.get(i + 1) * 50);
            }

            // Draw progress bar
            float progress = (float) songs[currentMusicIndex].player.position() / songs[currentMusicIndex].player.length();
            float mappedProgress = map(progress, 0, 1, 0, barWidth);
            fill(255, 255, 0);
            noStroke();
            rect(width / 2 - barWidth / 2, height - 100, mappedProgress, 20); // Draw progress bar

            // Display remaining time
            fill(0);
            textSize(16);
            text("Remaining: " + formatTime(songs[currentMusicIndex].player.length() - songs[currentMusicIndex].player.position()), width / 2 - barWidth / 2, height - 130);
        } else {
            // Debugging information
            fill(255, 0, 0);
            textSize(16);
            text("No song playing or invalid index", width / 2, height - 130);
        }
    }

    // Draw the content of the add song page
    void drawAddSongPage() {
        if (addSongBackground != null) {
            image(addSongBackground, 0, 0); // Draw add song background image
        } else {
            fill(255, 0, 0);
            textSize(32);
            textAlign(CENTER, CENTER);
            text("Error: addSongBackground.png not loaded", width / 2, height / 2);
        }
        fill(0); // Set text color to black
        textSize(32); // Set text size
        textAlign(CENTER, CENTER); // Set text alignment to center
        text("Adding a song...", width / 2, height / 2 - 100); // Prompt user that a song is being added
        text("Please select an audio file", width / 2, height / 2); // Prompt user to select an audio file
    }

    // Method to draw buttons
    void drawButton(String label, float x, float y) {
        fill(192, 192, 192); // Set button fill color
        stroke(128, 128, 128); // Set button border color
        strokeWeight(2); // Set border weight
        rectMode(CENTER);
        rect(x, y, 200, 50, 10); // Draw rounded rectangle button

        fill(0);
        textSize(24);
        textAlign(CENTER, CENTER);
        text(label, x, y); // Display label text on button
    }

    // Handle mouse pressed events
    public void mousePressed() {
        if (currentPage == 0) { // If on home page
            if (mouseY > height / 2 + 75 && mouseY < height / 2 + 125) { // Clicked "START" button
                currentPage = 1; // Switch to music player page
            } else if (mouseY > height / 2 + 175 && mouseY < height / 2 + 225) { // Clicked "ADD SONG" button
                isAddingSong = true; // Mark as adding a song
                selectInput("Select an audio file", "fileSelected"); // Open file selection dialog
            }
        } else if (currentPage == 1) { // If on music player page
            if (mouseY > height / 2 + 225 && mouseY < height / 2 + 275) { // Clicked "BACK" button
                stopCurrentMusic(); // Stop currently playing music
                currentPage = 0; // Return to home page
            } else if (mouseY > height / 2 + 125 && mouseY < height / 2 + 175) { // Clicked "SWITCH UP" button
                switchUp(); // Switch to previous song
            } else if (mouseY > height / 2 + 175 && mouseY < height / 2 + 225) { // Clicked "SWITCH DOWN" button
                switchDown(); // Switch to next song
            }
        }
    }

    // File selection callback function
    public void fileSelected(File selection) {
        if (selection == null) {
            statusMessage = "The user did not select a file"; // User canceled file selection
        } else if (songCount < songs.length) { // If song list is not full
            String filePath = selection.getAbsolutePath(); // Get file path
            String songName = selection.getName(); // Get file name
            AudioPlayer player = minim.loadFile(filePath);
            if (player == null) {
                statusMessage = "Failed to load the selected file";
            } else {
                songs[songCount++] = new Song(songName, player); // Add new song to list
                statusMessage = "Successfully added a song: " + songName; // Print success message
            }
        } else {
            statusMessage = "Song list is full, can't add more songs!"; // Print error message
        }
        isAddingSong = false; // Cancel adding song state
    }

    // Switch to the previous song
    void switchUp() {
        stopCurrentMusic(); // Stop currently playing music
        currentMusicIndex--;
        if (currentMusicIndex < 0 || songs[currentMusicIndex] == null) {
            currentMusicIndex = songCount - 1; // Loop back to last song
        }
        playCurrentMusic(); // Start playing new music
    }

    // Switch to the next song
    void switchDown() {
        stopCurrentMusic(); // Stop currently playing music
        currentMusicIndex++;
        if (currentMusicIndex >= songCount || songs[currentMusicIndex] == null) {
            currentMusicIndex = 0; // Loop back to first song
        }
        playCurrentMusic(); // Start playing new music
    }

    // Start playing the current music
    void playCurrentMusic() {
        if (currentMusicIndex >= 0 && currentMusicIndex < songCount && songs[currentMusicIndex] != null && songs[currentMusicIndex].player != null) {
            songs[currentMusicIndex].player.rewind(); // Reset music playback position
            songs[currentMusicIndex].player.play(); // Start playing music
        }
    }

    // Stop playing the current music
    void stopCurrentMusic() {
        if (currentMusicIndex >= 0 && currentMusicIndex < songCount && songs[currentMusicIndex] != null && songs[currentMusicIndex].player != null) {
            songs[currentMusicIndex].player.pause(); // Pause playing music
        }
    }

    // Format time in MM:SS format
    String formatTime(int millis) {
        int seconds = millis / 1000; // Convert milliseconds to seconds
        int minutes = seconds / 60; // Convert seconds to minutes
        seconds %= 60; // Get remaining seconds
        return nf(minutes, 2) + ":" + nf(seconds, 2); // Format time as MM:SS
    }

    // Handle keyboard input events
    public void keyPressed() {
        if (keyCode == ' ') {
            if (currentMusicIndex >= 0 && currentMusicIndex < songCount && songs[currentMusicIndex] != null && songs[currentMusicIndex].player != null) {
                if (songs[currentMusicIndex].player.isPlaying()) {
                    songs[currentMusicIndex].player.pause(); // Pause playing music
                } else {
                    songs[currentMusicIndex].player.play(); // Start playing music
                }
            }
        }
    }
}


