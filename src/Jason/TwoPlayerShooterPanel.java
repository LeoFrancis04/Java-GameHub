package Jason;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class TwoPlayerShooterPanel extends JPanel implements ActionListener, KeyListener {

    // ===== GAME DIMENSIONS =====
    int width = 800, height = 500;

    // ===== PLAYER POSITIONS =====
    int p1X = 50,  p1Y = 200;
    int p2X = 680, p2Y = 200;
    int playerWidth = 60, playerHeight = 80;
    int speed = 10;

    // ===== IMAGES =====
    private Image p1Image;    // Gun1.jpeg
    private Image p2Image;    // Gun2.jpeg
    private Image bgImage;    // background (optional)

    // ===== BULLETS =====
    class Bullet {
        int x, y, dx;
        Bullet(int x, int y, int dx) { this.x=x; this.y=y; this.dx=dx; }
    }
    ArrayList<Bullet> bullets = new ArrayList<>();

    // ===== HEALTH =====
    int p1Health = 5, p2Health = 5;
    boolean gameEnded = false;
    Timer timer;

    public TwoPlayerShooterPanel() {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Load images (falls back to colored rectangles if missing)
        p1Image = loadImage("images/Gun1.jpeg", playerWidth, playerHeight);
        p2Image = loadImage("images/Gun2.jpeg",  playerWidth, playerHeight);

        timer = new Timer(20, this);
        timer.start();
    }

    // ===== IMAGE LOADER =====
    private Image loadImage(String path, int w, int h) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) {
            System.out.println("Image not found: " + path + " (using fallback color)");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
    }

    // ===== GAME LOOP =====
    public void actionPerformed(ActionEvent e) {
        if (gameEnded) return;

        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.x += b.dx;

            // Hit Player 1 (bullet going left)
            if (b.dx < 0 && new Rectangle(b.x, b.y, 10, 5)
                    .intersects(new Rectangle(p1X, p1Y, playerWidth, playerHeight))) {
                p1Health--;
                it.remove();
                continue;
            }
            // Hit Player 2 (bullet going right)
            if (b.dx > 0 && new Rectangle(b.x, b.y, 10, 5)
                    .intersects(new Rectangle(p2X, p2Y, playerWidth, playerHeight))) {
                p2Health--;
                it.remove();
                continue;
            }
            // Off screen
            if (b.x < 0 || b.x > width) it.remove();
        }

        if (p1Health <= 0 || p2Health <= 0) {
            gameEnded = true;
            timer.stop();
        }
        repaint();
    }

    // ===== DRAW =====
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.setColor(new Color(15, 15, 25));
        g.fillRect(0, 0, width, height);

        // Ground line
        g.setColor(new Color(60, 60, 80));
        g.fillRect(0, height - 30, width, 30);

        // Draw Player 1
        if (p1Image != null) {
            g.drawImage(p1Image, p1X, p1Y, this);
        } else {
            g.setColor(Color.RED);
            g.fillRect(p1X, p1Y, playerWidth, playerHeight);
        }

        // Draw Player 2 (flip horizontally by drawing mirrored)
        if (p2Image != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.translate(p2X + playerWidth, p2Y);
            g2d.scale(-1, 1); // mirror horizontally
            g2d.drawImage(p2Image, 0, 0, this);
            g2d.dispose();
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(p2X, p2Y, playerWidth, playerHeight);
        }

        // Bullets
        g.setColor(Color.YELLOW);
        for (Bullet b : bullets) g.fillRect(b.x, b.y, 10, 5);

        // Health bars
        drawHealthBar(g, 20, 20, p1Health, Color.RED, "P1");
        drawHealthBar(g, width - 120, 20, p2Health, Color.BLUE, "P2");

        // Controls hint
        g.setColor(new Color(100, 100, 120));
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("P1: W/S move | D shoot", 20, height - 8);
        g.drawString("P2: ↑/↓ move | ← shoot", width - 160, height - 8);

        // Win screen
        if (gameEnded) {
            g.setColor(new Color(0, 0, 0, 160));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            String winner = p1Health <= 0 ? "🏆 Player 2 Wins!" : "🏆 Player 1 Wins!";
            g.setColor(Color.YELLOW);
            g.drawString(winner, 180, 250);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Press R to play again", 270, 300);
        }
    }

    private void drawHealthBar(Graphics g, int x, int y, int health, Color color, String label) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, 100, 15);
        g.setColor(color);
        g.fillRect(x, y, health * 20, 15);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(label + " ❤ " + health, x, y + 30);
    }

    // ===== KEYBOARD =====
    public void keyPressed(KeyEvent e) {
        if (gameEnded) {
            if (e.getKeyCode() == KeyEvent.VK_R) resetGame();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_W && p1Y > 0)
            p1Y -= speed;
        if (e.getKeyCode() == KeyEvent.VK_S && p1Y < height - playerHeight - 30)
            p1Y += speed;
        if (e.getKeyCode() == KeyEvent.VK_UP && p2Y > 0)
            p2Y -= speed;
        if (e.getKeyCode() == KeyEvent.VK_DOWN && p2Y < height - playerHeight - 30)
            p2Y += speed;
        if (e.getKeyCode() == KeyEvent.VK_D)
            bullets.add(new Bullet(p1X + playerWidth, p1Y + 30, 10));
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            bullets.add(new Bullet(p2X - 10, p2Y + 30, -10));
    }

    private void resetGame() {
        p1Health = 5; p2Health = 5;
        p1X = 50;    p2X = 680;
        p1Y = 200;   p2Y = 200;
        bullets.clear();
        gameEnded = false;
        timer.restart();
        repaint();
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}