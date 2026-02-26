package Jason;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MathsQuiz extends JFrame {
    private JLabel questionLabel, timerLabel, roundLabel;
    private JLabel p1ScoreLabel, p2ScoreLabel;
    private JButton option1, option2, option3;
    private int correctAnswer;
    private int player1Score = 0;
    private int player2Score = 0;
    private int round = 1;
    private final int MAX_ROUNDS = 10;
    private int timeLeft = 15;
    private Timer timer;
    private boolean p1Answered = false;
    private boolean p2Answered = false;
    private Random random = new Random();

    public MathsQuiz() {
        setTitle("Dual Player Math Quiz - Standalone");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TOP PANEL (Stats & Question) =====
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        roundLabel = new JLabel("Round: 1 / 10", JLabel.CENTER);
        roundLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        questionLabel = new JLabel("", JLabel.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 32));
        
        timerLabel = new JLabel("Time Left: 15", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.RED);
        
        topPanel.add(roundLabel);
        topPanel.add(questionLabel);
        topPanel.add(timerLabel);
        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL (Option Buttons) =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        option1 = createOptionButton();
        option2 = createOptionButton();
        option3 = createOptionButton();
        centerPanel.add(option1);
        centerPanel.add(option2);
        centerPanel.add(option3);
        add(centerPanel, BorderLayout.CENTER);

        // ===== BOTTOM PANEL (Scores) =====
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        p1ScoreLabel = new JLabel("Player 1 Score: 0", JLabel.CENTER);
        p1ScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        p2ScoreLabel = new JLabel("Player 2 Score: 0", JLabel.CENTER);
        p2ScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bottomPanel.add(p1ScoreLabel);
        bottomPanel.add(p2ScoreLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        startNewRound();
        setVisible(true);
    }

    private JButton createOptionButton() {
        JButton btn = new JButton();
        btn.setFont(new Font("Arial", Font.BOLD, 24));
        btn.addActionListener(e -> checkAnswer(btn));
        return btn;
    }

    private void generateQuestion() {
        int num1 = random.nextInt(20) + 1;
        int num2 = random.nextInt(10) + 1;
        int operator = random.nextInt(4);
        
        switch (operator) {
            case 0:
                correctAnswer = num1 + num2;
                questionLabel.setText(num1 + " + " + num2 + " = ?");
                break;
            case 1:
                correctAnswer = num1 - num2;
                questionLabel.setText(num1 + " - " + num2 + " = ?");
                break;
            case 2:
                correctAnswer = num1 * num2;
                questionLabel.setText(num1 + " × " + num2 + " = ?");
                break;
            case 3:
                correctAnswer = num1;
                num1 = num1 * num2;
                questionLabel.setText(num1 + " ÷ " + num2 + " = ?");
                break;
        }

        ArrayList<Integer> options = new ArrayList<>();
        options.add(correctAnswer);
        while (options.size() < 3) {
            int wrong = correctAnswer + random.nextInt(10) - 5;
            if (!options.contains(wrong)) {
                options.add(wrong);
            }
        }
        Collections.shuffle(options);
        option1.setText(String.valueOf(options.get(0)));
        option2.setText(String.valueOf(options.get(1)));
        option3.setText(String.valueOf(options.get(2)));
    }

    private void checkAnswer(JButton clickedButton) {
        int selected = Integer.parseInt(clickedButton.getText());
        if (!p1Answered) {
            if (selected == correctAnswer) {
                player1Score++;
                p1ScoreLabel.setText("Player 1 Score: " + player1Score);
            }
            p1Answered = true;
            return;
        }
        if (!p2Answered) {
            if (selected == correctAnswer) {
                player2Score++;
                p2ScoreLabel.setText("Player 2 Score: " + player2Score);
            }
            p2Answered = true;
        }
        if (p1Answered && p2Answered) {
            timer.stop();
            nextRound();
        }
    }

    private void startTimer() {
        timeLeft = 15;
        timerLabel.setText("Time Left: 15");
        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft);
            if (timeLeft <= 0) {
                timer.stop();
                nextRound();
            }
        });
        timer.start();
    }

    private void startNewRound() {
        p1Answered = false;
        p2Answered = false;
        generateQuestion();
        startTimer();
    }

    private void nextRound() {
        round++;
        if (round > MAX_ROUNDS) {
            timer.stop();
            String message;
            if (player1Score > player2Score) message = "🏆 Player 1 Wins!";
            else if (player2Score > player1Score) message = "🏆 Player 2 Wins!";
            else message = "🤝 It's a Draw!";
            
            JOptionPane.showMessageDialog(this, message);
            
            // Reset Game
            player1Score = 0;
            player2Score = 0;
            round = 1;
            p1ScoreLabel.setText("Player 1 Score: 0");
            p2ScoreLabel.setText("Player 2 Score: 0");
            roundLabel.setText("Round: 1 / 10");
            startNewRound();
        } else {
            roundLabel.setText("Round: " + round + " / 10");
            startNewRound();
        }
    }

    public static void main(String[] args) {
        // Ensure GUI runs on the correct thread
        SwingUtilities.invokeLater(() -> new MathsQuiz());
    }
}