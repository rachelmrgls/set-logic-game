/*************************************************************************
 *  Compilation:  javac SetGameplay.java
 *  Execution:    java SetGameplay
 *
 *  This is the executable file that controls the gameplay of Set. It is
 *  playable using mouse interactions and (by uncommenting) keyboard 
 *  interactions. It displays a board of 3 rows and 4 columns, with Set 
 *  cards in each position. The cards remaining, and the user's score are
 *  also displayed. 
 *
 *  Dependencies: SetCard.java, SetDeck.java, SetBoard.java, SetShapes.java,
 *                StdDraw.java, Color.java
 *  
 *  Author : Rachel Margulies, December 2014
 *  
 *************************************************************************/

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.*;

public class SetGameplay {
	private static SetBoard board;

    private static final double CARD_WIDTH = 160;
    private static final double CARD_HEIGHT = 110;
    private static final double CARD_SPACING = 20;

    public static final Color PURPLE = new Color(100, 0, 127);
    public static final Color GREEN = new Color(35, 168, 61);
    public static final Color RED = Color.RED;
    public static final Color BACK_PURPLE = new Color(198, 167, 214);

    /* 
    *  Main function: runs the game. 
    */
    public static void main(String[] args) {
        // set up the canvas
        StdDraw.setCanvasSize(800, 600);
        StdDraw.setYscale(0, 500);
        StdDraw.setXscale(0, 800);

        StdDraw.setPenRadius(.004);

        // set up the SetBoard data structure.
        board = new SetBoard();
        board.hovRow = 0;
        board.hovCol = 0;
        
        // show the original board
        refreshBoard();
        
        boolean mouseClicked = false;

        // loop forever (aka, until the game is actively quit)
        while (true) {
            // check to see if the user has done a full mouse click.
            if (StdDraw.mousePressed()) { mouseClicked = true; }
            if (!StdDraw.mousePressed() && mouseClicked) {
                mouseClicked = false;
                // get mouse coordinates
                int x = (int) StdDraw.mouseX();
                int y = (int) StdDraw.mouseY();
                // calculate which card in what row and column were clicked
                int col = (int) ((x - 25)  / (CARD_WIDTH + CARD_SPACING));
                int row = (int) ((y - 70)  / (CARD_HEIGHT + CARD_SPACING));
                // if the click was at a valid coordinate for a playing card...select it.
                if (x > 25 && y > 70 && row < 3 && col < 4)  {
                    board.hovRow = row;
                    board.hovCol = col;
                    selectCard();
                }
            }
            // check if the user has typed a key; if so, process it  
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                // remove a random column
                if (key == 'r') {
                    board.removeCol();
                    refreshBoard();
                }
                // quit game (starts a new game)
                if (key == 'q') {
                    endGame();
                    board = new SetBoard();
                    refreshBoard();
                }
                /* UNCOMMENT TO ENABLE KEYBOARD INTERACTIONS (for selecting Sets)
                // select card
                if (key == ' ') {
                    selectCard();
                }
                // up
                if (key == 'i') {
                    if (board.hovRow != 2) {
                        switchCard(board.hovRow + 1, board.hovCol);
                    }
                }
                // down
                if (key == 'k') {
                    if (board.hovRow != 0) {
                        switchCard(board.hovRow - 1, board.hovCol);
                    }
                }
                // right
                if (key == 'l') {
                    if (board.hovCol != 3) {
                        switchCard(board.hovRow, board.hovCol + 1);
                    }
                }
                //left
                if (key == 'j') {
                    if (board.hovCol != 0) {
                        switchCard(board.hovRow, board.hovCol - 1);
                    }
                }
                */
                
            }   
        }

    }

    /* 
    *  Runs when 'q' is pressed. Shows a notice with the user's score, saves it in high scores,
    *  and then starts a new game.
    */
    public static void endGame() {
        // write to file
        StringBuilder name = new StringBuilder();
        int dashOn = 0;
        boolean loop = true;
        while (loop) {
        // scanner: enter your name (6 letter max, letters and numbers only) to save your high score. 
            dashOn = (dashOn+1) % 4;
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == KeyEvent.VK_ENTER) { // enter 
                    loop = false;
                }
                else if (key == KeyEvent.VK_DELETE) { // delete 
                    name.deleteCharAt(name.length()-1);
                }
                else if (Character.isLetterOrDigit(key) && name.length() < 6) {
                    name.append(key+"");
                }
            }
            StdDraw.setPenColor(new Color(50, 50, 50, 255));
            StdDraw.filledRectangle(400, 250, 250, 150);
            StdDraw.setPenColor(StdDraw.WHITE);
            String nameS = (dashOn < 2) ? name.toString() + "_" : name.toString() + " ";
            StdDraw.text(400, 300, "Good game! Your score was " + board.getScore() + ".");
            StdDraw.text(400, 250, "Type your name (6 char max) and hit enter to save.");
            StdDraw.textLeft(350, 200, nameS);
            StdDraw.show(100);
        }
        File file = null;
        try {
            file = new File("highscores.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader("highscores.txt"));
            String line;
            int count = 0;
            boolean wrote = false;
            StringBuilder newScores = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (count == 10) { break;}
                String[] score = line.split("\t");
                int val = Integer.parseInt(score[1]);
                if (board.getScore() > val) {
                    newScores.append(name.toString() + "\t" + board.getScore() + "\n");
                    count++;
                    wrote = true;
                    if (count == 10) { break;}
                }
                newScores.append(line + "\n");
                count++;
            }
            if (count < 10 && !wrote) {
                newScores.append(name.toString() + "\t" + board.getScore() + "\n");
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            bw.write(newScores.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StdDraw.setPenColor(new Color(50, 50, 50, 255));
        StdDraw.filledRectangle(400, 250, 250, 150);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(400, 250, "SAVED! Starting new game....");
        StdDraw.show(750);
    }
    /* 
    *  CURRENTLY DISABLED. When keyboard interactions are enabled, this method
    *  is used to indicated which card the user is hovering over (visual guidance).
    */
    public static void switchCard(int newRow, int newCol) {
        /*
        // if the card previously hovered over is selected, it needs to be colored blue. 
        if (board.isCardSelected(board.hovRow, board.hovCol)) {
            StdDraw.setPenColor(StdDraw.BLUE);
        }
        // else the card previously hovered over is unselected, so it needs to be colored black.
        else {
            StdDraw.setPenColor(StdDraw.BLACK);
        }
        // draw the border of the previously selected card (in the appropriate color)
        StdDraw.rectangle(25.0 + (CARD_SPACING + CARD_WIDTH) * board.hovCol + CARD_WIDTH/2, 
                          70.0 + (CARD_SPACING + CARD_HEIGHT) * board.hovRow + CARD_HEIGHT/2,
                          CARD_WIDTH/2, CARD_HEIGHT/2);
        // adjust which card is being hovered over.
        board.hovRow = newRow;
        board.hovCol = newCol;

        // color the border of the card currently being hovered over light blue.
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.rectangle(25.0 + (CARD_SPACING + CARD_WIDTH) * board.hovCol + CARD_WIDTH/2, 
                          70.0 + (CARD_SPACING + CARD_HEIGHT) * board.hovRow + CARD_HEIGHT/2,
                          CARD_WIDTH/2, CARD_HEIGHT/2);

        StdDraw.show();
        */
    }
    /* 
    *  Select the card currently hovered over (keyboard interaction) or just clicked 
    *  (mouse interaction). Color its border blue. Check if a set is formed. If so,
    *  reload the display. 
    */

    public static void selectCard() {
        // select the corresponding card on the SetBoard.
        board.cardSelect(board.hovRow, board.hovCol);

        //switchCard(board.hovRow, board.hovCol);

        // if a set was made, refresh the display.
        if (board.reload) {
            StdDraw.clear();
            board.reload = false;
        }
        refreshBoard();
    }
    /* 
    *  Wipes the current display, and reloads all content. 
    */
    public static void refreshBoard() {
        //StdDraw.clear();
        // draw the background
        StdDraw.setPenColor(BACK_PURPLE);
        StdDraw.filledRectangle(10, 0, 850, 600);

        // draw the board, with all of the cards.
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                // find the location of the card to be drawn.
                double x = 25.0 + (CARD_SPACING + CARD_WIDTH) * i + CARD_WIDTH/2;
                double y = 70.0 + (CARD_SPACING + CARD_HEIGHT) * j + CARD_HEIGHT/2;
                
                // draw the card
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.filledRectangle(x, y, CARD_WIDTH/2, CARD_HEIGHT/2);
                
                if (board.isCardSelected(j, i)) {
                    StdDraw.setPenRadius(.009);
                    if (board.falseSet) {
                        StdDraw.setPenColor(new Color(213, 0, 0));
                    }
                    else {
                        StdDraw.setPenColor(new Color(24, 127, 235));
                    }
                }
                else {
                    StdDraw.setPenColor(StdDraw.BLACK);
                }
                StdDraw.rectangle(x, y, CARD_WIDTH/2, CARD_HEIGHT/2);

                StdDraw.setPenRadius(.004);
                // draw the sumbols on the card (according to the characteristics of the SetCard)
                SetCard card = board.getCard(j, i);
                // if there is no card at that position, move on to the next card to be drawn.
                if (card == null) {
                    continue;
                }
                // call the SetShapes.java class to draw the card content
                switch (card.getShape()) {
                    case SetCard.SQUIGGLE:  SetShapes.squiggle(card, x, y);
                                            break;
                    case SetCard.DIAMOND:   SetShapes.diamond(card, x, y);
                                            break;
                    case SetCard.OVAL:      SetShapes.oval(card, x, y);
                                            break;
                    default:                break;
                }
                //StdDraw.setPenColor(StdDraw.BLACK);
                //StdDraw.text(x, y, card.toString());
            }
        }
        // if keyboard interactions are enabled, this recolors the card currently being hovered on.
        //switchCard(board.hovRow, board.hovCol);

        // write all of the text information
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.textRight(760.0, 30.0, "Press 'q' to quit");
        StdDraw.textLeft(20.0, 30.0, "Press 'r' to remove a column");
        StdDraw.textRight(760.0, 480.0, "Score: " + board.getScore());
        StdDraw.textLeft(20.0, 480.0, "Cards remaining: " + board.cardsLeft());
        StdDraw.setFont(new Font("Times", Font.PLAIN, 50));
        StdDraw.text(380.0, 480.0, "SET");
        StdDraw.setFont();
        StdDraw.setFont(new Font(StdDraw.getFont().getName(), Font.PLAIN, 18));
        StdDraw.text(380.0, 15.0, "by Rachel Margulies");
        StdDraw.setFont();
        StdDraw.show(200);
        if (board.falseSet) {
            board.clearSet();
            refreshBoard();
        }
    }
}
