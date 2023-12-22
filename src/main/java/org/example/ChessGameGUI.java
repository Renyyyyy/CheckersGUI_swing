package org.example;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ChessGameGUI {
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] squares;
    private Board board;

    private JLabel statusLabel;

    private ImageIcon blackCheckerIcon; // Path to the image for black checkers
    private ImageIcon whiteCheckerIcon; // Path to the image for white checkers

    private ImageIcon blackKingIcon ;
    private ImageIcon whiteKingIcon ;

    private boolean isSelected = false;
    private int selectedRow, selectedCol;
    private boolean isPlayerOneTurn = true; // True if it's player one's turn, false otherwise

    public ChessGameGUI() {
        board = new Board();
        board.initializeBoard(); // Initialize the board with pieces
        initialize();
        populateBoard();
    }

    private void initialize() {
        frame = new JFrame("Checkers Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());

        boardPanel = new JPanel(new GridLayout(8, 8));
        frame.add(boardPanel, BorderLayout.CENTER);
        statusLabel = new JLabel("White's turn");
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);

        squares = new JButton[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JButton();
                squares[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        squareClicked(e);
                    }
                });
                boardPanel.add(squares[row][col]);
            }
        }

        blackCheckerIcon = new ImageIcon("C:\\Users\\Роман\\IdeaProjects\\GUI_MOST_AHUENNO_PROJECT_INTERTEIMENT\\Images\\pngwing.com.png");
        whiteCheckerIcon = new ImageIcon("C:\\Users\\Роман\\IdeaProjects\\GUI_MOST_AHUENNO_PROJECT_INTERTEIMENT\\Images\\pngwing.com (1).png");
        blackKingIcon = new ImageIcon("C:\\Users\\Роман\\IdeaProjects\\GUI_MOST_AHUENNO_PROJECT_INTERTEIMENT\\Images\\K_pngwing.com.png");
        whiteKingIcon = new ImageIcon("C:\\Users\\Роман\\IdeaProjects\\GUI_MOST_AHUENNO_PROJECT_INTERTEIMENT\\Images\\K_black_pngwing.com (1).png");

        frame.setVisible(true);
    }

    private void populateBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = squares[row][col];
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    if (piece.getColor() == Color.BLACK) {
                        square.setIcon(blackCheckerIcon);
                    } else {
                        square.setIcon(whiteCheckerIcon);
                    }
                } else {
                    square.setIcon(null);
                }
            }
        }
    }

    private void squareClicked(ActionEvent e) {
        JButton clickedSquare = (JButton) e.getSource();
        Point clickedPoint = findSquarePosition(clickedSquare);
        int clickedRow = clickedPoint.x;
        int clickedCol = clickedPoint.y;

        if (isSelected) {
            if (validateAndMove(selectedRow, selectedCol, clickedRow, clickedCol)) {
                updateGUI();
                switchTurn();
            }
            isSelected = false;
        } else {
            isSelected = true;
            selectedRow = clickedRow;
            selectedCol = clickedCol;
        }
    }

    private void checkForWin() {
        boolean whiteLeft = false, blackLeft = false;
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    if (piece.getColor() == Color.WHITE) {
                        whiteLeft = true;
                    } else if (piece.getColor() == Color.BLACK) {
                        blackLeft = true;
                    }
                }
            }
        }

        if (!whiteLeft || !blackLeft) {
            gameOver(whiteLeft ? "White wins!" : "Black wins!");
        }
    }

    private void gameOver(String message) {
        JOptionPane.showMessageDialog(frame, message);
        System.exit(0); // or reset the game if you prefer
    }


    private Point findSquarePosition(JButton square) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (squares[row][col] == square) {
                    return new Point(row, col);
                }
            }
        }
        return null; // Square not found
    }

    private boolean validateAndMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getPiece(fromRow, fromCol);
        if (piece != null && piece.getColor() == (isPlayerOneTurn ? Color.WHITE : Color.BLACK) && piece.isValidMove(board, fromRow, fromCol, toRow, toCol)) {
            board.movePiece(fromRow, fromCol, toRow, toCol); // This method should handle the actual movement
            if (toRow == 0 || toRow == 7) { // Check for promotion
                board.placePiece(toRow, toCol, new King(piece.getColor())); // Promote to King
            }
            checkForWin();
            switchTurn();
            return true;
        }
        return false;
    }


    private void updateGUI() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = squares[row][col];
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    ImageIcon icon = piece instanceof King ? (piece.getColor() == Color.BLACK ? blackKingIcon : whiteKingIcon) : (piece.getColor() == Color.BLACK ? blackCheckerIcon : whiteCheckerIcon);
                    square.setIcon(icon);
                } else {
                    square.setIcon(null);
                }
            }
        }
        switchTurn();
        // You can also update turn indicators here if you have them
    }


    private void switchTurn() {
        isPlayerOneTurn = !isPlayerOneTurn;
        statusLabel.setText(isPlayerOneTurn ? "White's turn" : "Black's turn");
        // Update GUI elements to indicate whose turn it is
    }

    // ... Other methods ...
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChessGameGUI(); // Create and show the game GUI
            }
        });
    }
}

