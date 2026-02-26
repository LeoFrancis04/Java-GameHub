package Jason;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.sound.sampled.*;

public class SeaBattlePanel extends JPanel implements ActionListener {
    private JButton[][] grid1 = new JButton[10][10];
    private JButton[][] grid2 = new JButton[10][10];
    private String[][] ships1 = new String[10][10];
    private String[][] ships2 = new String[10][10];
    private int score1 = 0;
    private int score2 = 0;
    private boolean player1Turn = true;
    private JLabel turnLabel;
    private JLabel scoreLabel;
    private String[] shipNames = {"Submarine", "Aircraft", "MotorBoat", "Yacht"};
    private ImageIcon shipIcon;

    public SeaBattlePanel() {
        setLayout(new BorderLayout());

        // Load ship icon from the assets folder
        shipIcon = loadIcon("images/ship.png");

        JPanel center = new JPanel(new GridLayout(1, 2, 30, 0));
        JPanel p1 = new JPanel(new GridLayout(10, 10));
        JPanel p2 = new JPanel(new GridLayout(10, 10));

        p1.setBorder(BorderFactory.createTitledBorder("Player 1 Fleet"));
        p2.setBorder(BorderFactory.createTitledBorder("Player 2 Fleet"));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid1[i][j] = createButton();
                grid2[i][j] = createButton();
                grid1[i][j].addActionListener(this);
                grid2[i][j].addActionListener(this);
                p1.add(grid1[i][j]);
                p2.add(grid2[i][j]);
            }
        }

        center.add(p1);
        center.add(p2);

        turnLabel = new JLabel("Player 1 Turn", JLabel.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 22));
        scoreLabel = new JLabel("Score → P1: 0 | P2: 0", JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));

        add(turnLabel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(scoreLabel, BorderLayout.SOUTH);

        placeShipsRandom(ships1);
        placeShipsRandom(ships2);
    }

    private JButton createButton() {
        JButton b = new JButton();
        b.setBackground(new Color(40, 90, 140)); // ocean color
        return b;
    }

    private void placeShipsRandom(String[][] ships) {
        Random r = new Random();
        for (String s : shipNames) {
            int row = r.nextInt(10);
            int col = r.nextInt(10);
            ships[row][col] = s;
        }
    }

    private void playSound(String fileName) {
        try {
            java.net.URL url = getClass().getResource("images/" + fileName);
            if (url != null) {
                AudioInputStream audio = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
            }
        } catch (Exception e) {
            System.out.println("Audio asset missing: " + fileName);
        }
    }

    private void animateHit(JButton btn) {
        if (shipIcon != null) {
            btn.setIcon(shipIcon);
        } else {
            btn.setBackground(Color.RED);
            btn.setText("💥");
        }
        playSound("hit.wav");
    }

    private void animateMiss(JButton btn) {
        btn.setBackground(new Color(20, 50, 90));
        btn.setText("•");
        playSound("miss.wav");
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (player1Turn && e.getSource() == grid2[i][j]) {
                    if (ships2[i][j] != null) {
                        animateHit(grid2[i][j]);
                        score1++;
                    } else {
                        animateMiss(grid2[i][j]);
                    }
                    grid2[i][j].setEnabled(false);
                    player1Turn = false;
                } else if (!player1Turn && e.getSource() == grid1[i][j]) {
                    if (ships1[i][j] != null) {
                        animateHit(grid1[i][j]);
                        score2++;
                    } else {
                        animateMiss(grid1[i][j]);
                    }
                    grid1[i][j].setEnabled(false);
                    player1Turn = true;
                }
            }
        }

        turnLabel.setText(player1Turn ? "Player 1 Turn" : "Player 2 Turn");
        scoreLabel.setText("Score → P1: " + score1 + " | P2: " + score2);

        if (score1 == 4) {
            JOptionPane.showMessageDialog(this, "Player 1 Wins!");
            resetGame();
        } else if (score2 == 4) {
            JOptionPane.showMessageDialog(this, "Player 2 Wins!");
            resetGame();
        }
    }

    private void resetGame() {
        score1 = 0; score2 = 0;
        player1Turn = true;
        ships1 = new String[10][10];
        ships2 = new String[10][10];
        placeShipsRandom(ships1);
        placeShipsRandom(ships2);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid1[i][j].setText("");
                grid2[i][j].setText("");
                grid1[i][j].setIcon(null);
                grid2[i][j].setIcon(null);
                grid1[i][j].setEnabled(true);
                grid2[i][j].setEnabled(true);
                grid1[i][j].setBackground(new Color(40, 90, 140));
                grid2[i][j].setBackground(new Color(40, 90, 140));
            }
        }
        scoreLabel.setText("Score → P1: 0 | P2: 0");
        turnLabel.setText("Player 1 Turn");
    }

    private ImageIcon loadIcon(String path) {
        java.net.URL location = getClass().getResource(path);
        if (location == null) {
            System.out.println("Image not found: " + path);
            return null;
        }
        ImageIcon icon = new ImageIcon(location);
        Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}