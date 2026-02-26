package Jason;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int width = 400, height = 600;
    int birdX = 100, birdY = 300, birdSize = 30;
    int velocity = 0, gravity = 1;
    private Image birdImage;

    class Pipe {
        int x, y, w, h;
        boolean scored = false;
        Pipe(int x, int y, int w, int h) { this.x = x; this.y = y; this.w = w; this.h = h; }
    }

    ArrayList<Pipe> pipes = new ArrayList<>();
    Timer timer, pipeTimer;
    boolean gameOver = false;
    int score = 0;
    Random random = new Random();

    public FlappyBird() {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);
        setFocusable(true);
        addKeyListener(this);

        // Load Asset
        java.net.URL imgUrl = getClass().getResource("images/Flappy.jpeg");
        if (imgUrl != null) {
            birdImage = new ImageIcon(imgUrl).getImage().getScaledInstance(birdSize, birdSize, Image.SCALE_SMOOTH);
        }

        timer = new Timer(20, this);
        timer.start();
        pipeTimer = new Timer(1500, e -> addPipes());
        pipeTimer.start();
    }

    public void addPipes() {
        int topHeight = random.nextInt(300) + 50;
        pipes.add(new Pipe(width, 0, 60, topHeight));
        pipes.add(new Pipe(width, topHeight + 150, 60, height - topHeight - 150));
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            velocity += gravity;
            birdY += velocity;
            Iterator<Pipe> it = pipes.iterator();
            while (it.hasNext()) {
                Pipe p = it.next();
                p.x -= 4;
                if (!p.scored && p.x + p.w < birdX) { p.scored = true; score++; }
                if (p.x + p.w < 0) it.remove();
                if (new Rectangle(birdX, birdY, birdSize, birdSize).intersects(new Rectangle(p.x, p.y, p.w, p.h))) gameOver = true;
            }
            if (birdY > height || birdY < 0) gameOver = true;
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (birdImage != null) g.drawImage(birdImage, birdX, birdY, this);
        else { g.setColor(Color.yellow); g.fillOval(birdX, birdY, birdSize, birdSize); }

        g.setColor(Color.green);
        for (Pipe p : pipes) g.fillRect(p.x, p.y, p.w, p.h);

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 40);
        if (gameOver) g.drawString("Game Over! Press R", 100, 300);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) velocity = -10;
        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) restartGame();
    }

    public void restartGame() { birdY = 300; velocity = 0; pipes.clear(); score = 0; gameOver = false; }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}