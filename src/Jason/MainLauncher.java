package Jason;
import javax.swing.*;
import java.awt.*;

public class MainLauncher extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JButton lastClicked = null;

    public MainLauncher() {
        setTitle("Game Hub");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== LEFT SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(30, 30, 40));
        sidebar.setPreferredSize(new Dimension(160, 700));

        JLabel title = new JLabel("GAMES", JLabel.CENTER);
        title.setForeground(new Color(100, 200, 255));
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(title);

        // ===== CARD PANEL (Game Area) =====
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.BLACK);

        // Welcome screen
        JPanel welcome = new JPanel(new BorderLayout());
        welcome.setBackground(new Color(20, 20, 30));
        JLabel welcomeLabel = new JLabel("Select a Game →", JLabel.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcome.add(welcomeLabel);

        cardPanel.add(welcome,              "Welcome");
        cardPanel.add(new MathsQuizPanel(), "Maths");
        cardPanel.add(new SeaBattlePanel(), "SeaBattle");
        cardPanel.add(new TicTacToePanel(), "TicTacToe");
        cardPanel.add(new TwoPlayerShooterPanel(), "Shooter");
        cardPanel.add(new FlappyBird(),     "Flappy");

        // Add nav buttons
        addNavButton(sidebar, "🧮 Maths Quiz",  "Maths");
        addNavButton(sidebar, "⚓ Sea Battle",  "SeaBattle");
        addNavButton(sidebar, "⭕ Tic Tac Toe", "TicTacToe");
        addNavButton(sidebar, "🔫 Shooter",     "Shooter");
        addNavButton(sidebar, "🐦 Flappy Bird", "Flappy");

        add(sidebar,    BorderLayout.WEST);
        add(cardPanel,  BorderLayout.CENTER);
        setVisible(true);
    }

    private void addNavButton(JPanel sidebar, String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(160, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(new Color(50, 50, 65));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            // Highlight active button
            if (lastClicked != null)
                lastClicked.setBackground(new Color(50, 50, 65));
            btn.setBackground(new Color(0, 120, 200));
            lastClicked = btn;

            // Switch game panel
            cardLayout.show(cardPanel, cardName);

            // Fix keyboard focus for Shooter and Flappy
            SwingUtilities.invokeLater(() -> {
                for (Component c : cardPanel.getComponents())
                    if (c.isVisible()) c.requestFocusInWindow();
            });
        });

        sidebar.add(btn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainLauncher());
    }
}