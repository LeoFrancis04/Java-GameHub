package Jason; // This must be the very first line

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class TwoPlayerShooter extends JPanel implements ActionListener, KeyListener {
    int width = 800;
    int height = 500;

    // Player positions
    int p1X = 50, p1Y = 200;
    int p2X = 700, p2Y = 200;
    int playerWidth = 30;
    int playerHeight = 60;
    int speed = 10;

    class Bullet {
        int x, y, dx;
        Bullet(int x, int y, int dx) {
            this.x = x;
            this.y = y;
            this.dx = dx;
        }
    }

    ArrayList<Bullet> bullets = new ArrayList<>();
    int p1Health = 5;
    int p2Health = 5;
    Timer timer;

    public TwoPlayerShooter() {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(20, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.x += b.dx;
            
            // Collision with Player 1
            if (new Rectangle(b.x, b.y, 10, 5)
                .intersects(new Rectangle(p1X, p1Y, playerWidth, playerHeight))
                && b.dx < 0) {
                p1Health--;
                it.remove();
            }
            
            // Collision with Player 2
            else if (new Rectangle(b.x, b.y, 10, 5)
                .intersects(new Rectangle(p2X, p2Y, playerWidth, playerHeight))
                && b.dx > 0) {
                p2Health--;
                it.remove();
            }
            
            else if (b.x < 0 || b.x > width) {
                it.remove();
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Player 1
        g.setColor(Color.red);
        g.fillRect(p1X, p1Y, playerWidth, playerHeight);
        
        // Player 2
        g.setColor(Color.blue);
        g.fillRect(p2X, p2Y, playerWidth, playerHeight);
        
        // Bullets
        g.setColor(Color.white);
        for (Bullet b : bullets) {
            g.fillRect(b.x, b.y, 10, 5);
        }
        
        // Health UI
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("P1 Health: " + p1Health, 20, 30);
        g.drawString("P2 Health: " + p2Health, 620, 30);

        if (p1Health <= 0) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Player 2 Wins!", 250, 250);
            timer.stop();
        }
        if (p2Health <= 0) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Player 1 Wins!", 250, 250);
            timer.stop();
        }
    }

    public void keyPressed(KeyEvent e) {
        // Player 1 movement (W/S)
        if (e.getKeyCode() == KeyEvent.VK_W && p1Y > 0)
            p1Y -= speed;
        if (e.getKeyCode() == KeyEvent.VK_S && p1Y < height - playerHeight)
            p1Y += speed;
            
        // Player 2 movement (Arrows)
        if (e.getKeyCode() == KeyEvent.VK_UP && p2Y > 0)
            p2Y -= speed;
        if (e.getKeyCode() == KeyEvent.VK_DOWN && p2Y < height - playerHeight)
            p2Y += speed;
            
        // Shooting (D for P1, Left Arrow for P2)
        if (e.getKeyCode() == KeyEvent.VK_D) {
            bullets.add(new Bullet(p1X + playerWidth, p1Y + 25, 10));
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            bullets.add(new Bullet(p2X - 10, p2Y + 25, -10));
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("2 Player Shooting Game");
        TwoPlayerShooter game = new TwoPlayerShooter();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}