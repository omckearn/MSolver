import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI used to represent a minesweeper game
 * used for debugging MSolver
 */
public class MGUI {
    private final JFrame frame;
    private final JButton[][] buttons;
    private final int rows;
    private final int columns;


    /**
     * @param rows # rows
     * @param columns # cols
     */
    public MGUI(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.buttons = new JButton[rows][columns];
        this.frame = new JFrame("Minesweeper Game");

        initialize();
    }


    /**
     * initialize the board
     */
    private void initialize() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JButton button = new JButton();
                button.addActionListener(new ButtonClickListener(i, j));
                buttons[i][j] = button;
                frame.add(button);
            }
        }

        frame.pack();
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        private final int row;
        private final int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // add game logic here
            System.out.println("Button clicked at: [" + row + ", " + col + "]");
        }
    }

    public static void main(String[] args) {
        // Example usage - create a 10x10 Minesweeper game GUI
        SwingUtilities.invokeLater(() -> new MGUI(10, 10));
    }
}
