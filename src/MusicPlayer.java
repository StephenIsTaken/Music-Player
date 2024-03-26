import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class MusicPlayer extends JFrame implements ActionListener, ItemListener {
    JButton playButton, pauseButton, resumeButton, stopButton, addCustomButton;
    JComboBox<String> songList;
    Clip clip;
    File defaultSongsDir = new File("src/songs");
    Map<String, String> songMap = new HashMap<>();

    public MusicPlayer() {
        setTitle("Music Player");
        setSize(400, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        playButton = new JButton("Play");
        playButton.addActionListener(this);


        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);

        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(this);

        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);

        addCustomButton = new JButton("Add Custom");
        addCustomButton.addActionListener(this);

        songList = new JComboBox<>();
        songList.addItemListener(this);
        populateDefaultSongs();

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.setBackground(Color.BLACK);
        playButton.setBackground(new Color(163, 184, 204));
        pauseButton.setBackground(new Color(163, 184, 204));
        resumeButton.setBackground(new Color(163, 184, 204));
        stopButton.setBackground(new Color(163, 184, 204));
        addCustomButton.setBackground(new Color(163, 184, 204));

        panel.add(songList);
        panel.add(playButton);
        panel.add(pauseButton);
        panel.add(resumeButton);
        panel.add(stopButton);
        panel.add(addCustomButton);

        add(panel);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            playMusic();
        } else if (e.getSource() == pauseButton) {
            pauseMusic();
        } else if (e.getSource() == resumeButton) {
            resumeMusic();
        } else if (e.getSource() == stopButton) {
            stopMusic();
        } else if (e.getSource() == addCustomButton) {
            addCustomSong();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            stopMusic();
        }
    }

    public void populateDefaultSongs() {
        File[] files = defaultSongsDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".wav")) {
                    String songName = file.getName();
                    songMap.put(songName, file.getAbsolutePath());
                    songList.addItem(songName);
                }
            }
        }
    }

    public void playMusic() {
        try {
            String selectedSong = (String) songList.getSelectedItem();
            if (selectedSong != null) {
                String filePath = songMap.get(selectedSong);
                if (filePath != null) {
                    File file = new File(filePath);
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                } else {
                    JOptionPane.showMessageDialog(null, "Song path not found.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a song to play.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void resumeMusic() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }

    public void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    public void addCustomSong() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".wav");
            }

            public String getDescription() {
                return "WAV files (*.wav)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null) {
                String fileName = selectedFile.getName();
                String filePath = selectedFile.getAbsolutePath();
                songMap.put(fileName, filePath);
                songList.addItem(fileName);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MusicPlayer().setVisible(true));
    }
}
