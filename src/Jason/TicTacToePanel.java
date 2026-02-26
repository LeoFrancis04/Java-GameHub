package Jason;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToePanel extends JPanel implements ActionListener {
    private JButton[] b = new JButton[9];
    private boolean xTurn = true;

    public TicTacToePanel() {
        setLayout(new GridLayout(3, 3, 5, 5));
        setBackground(new Color(40, 40, 50));

        for (int i = 0; i < 9; i++) {
            b[i] = new JButton("");
            b[i].setFont(new Font("Arial", Font.BOLD, 50));
            b[i].setFocusPainted(false);
            b[i].setBackground(new Color(60, 60, 75));
            b[i].setForeground(Color.WHITE);
            b[i].addActionListener(this);
            add(b[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        if (!btn.getText().equals("")) return;

        btn.setText(xTurn ? "X" : "O");
        btn.setForeground(xTurn ? new Color(255, 100, 100) : new Color(100, 200, 255));

        if (checkWin()) {
            JOptionPane.showMessageDialog(this, (xTurn ? "X" : "O") + " Wins!");
            reset();
        } else if (draw()) {
            JOptionPane.showMessageDialog(this, "It's a Draw!");
            reset();
        } else {
            xTurn = !xTurn;
        }
    }

    private boolean checkWin() {
        int[][] w = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}
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
        for (JButton x : b) if (x.getText().equals("")) return false;
        return true;
    }

    public void reset() {
        for (JButton x : b) {
            x.setText("");
            x.setBackground(new Color(60, 60, 75));
        }
        xTurn = true;
    }
}
