package Jason;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame implements ActionListener {
    private JButton[] b = new JButton[9];
    private boolean xTurn = true;

    public TicTacToe() {
        setTitle("Tic Tac Toe - Standalone");
        setSize(300, 300);
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        for (int i = 0; i < 9; i++) {
            b[i] = new JButton("");
            b[i].setFont(new Font("Arial", Font.BOLD, 40));
            b[i].addActionListener(this);
            add(b[i]);
        }
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        
        // If the button is already clicked, ignore it
        if (!btn.getText().equals("")) return;

        // Set text based on whose turn it is
        btn.setText(xTurn ? "X" : "O");

        // Check for a winner or a draw
        if (checkWin()) {
            JOptionPane.showMessageDialog(this, (xTurn ? "X" : "O") + " Wins!");
            reset();
        } else if (draw()) {
            JOptionPane.showMessageDialog(this, "It's a Draw!");
            reset();
        } else {
            // Switch turns
            xTurn = !xTurn;
        }
    }

    private boolean checkWin() {
        // Winning combinations: 3 rows, 3 columns, 2 diagonals
        int[][] w = {
            {0,1,2}, {3,4,5}, {6,7,8}, // Rows
            {0,3,6}, {1,4,7}, {2,5,8}, // Columns
            {0,4,8}, {2,4,6}           // Diagonals
        };

        for (int[] p : w) {
            if (!b[p[0]].getText().equals("") &&
                b[p[0]].getText().equals(b[p[1]].getText()) &&
                b[p[1]].getText().equals(b[p[2]].getText())) {
                return true;
            }
        }
        return false;
    }

    private boolean draw() {
        for (JButton x : b) {
            if (x.getText().equals("")) return false;
        }
        return true;
    }

    private void reset() {
        for (JButton x : b) {
            x.setText("");
        }
        xTurn = true;
    }

    public static void main(String[] args) {
        // Ensure GUI runs on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }
}