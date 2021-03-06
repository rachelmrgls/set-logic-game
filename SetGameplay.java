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
    StdDraw.setXscale(0, 750);

    StdDraw.setPenRadius(.004);

    // set up the SetBoard data structure.
    board = new SetBoard();
    board.hovRow = 0;
    board.hovCol = 0;
    
    // show the original board
    refreshBoard();
    
    boolean mouseClicked = false;
    boolean isPaused = false;
    boolean tutorialMode = false;

    // loop forever (aka, until the game is actively quit)
    while (true) {
      // check to see if the user has done a full mouse click.
      if (isPaused) {
        if (StdDraw.hasNextKeyTyped()) {
          char key = StdDraw.nextKeyTyped();
          if (key == 'h') {
            isPaused = false;
            refreshBoard();
          }
        }
        else { continue; }
      }
      if (tutorialMode) {
        if (StdDraw.hasNextKeyTyped()) {
          char key = StdDraw.nextKeyTyped();
          if (key == 't') {
            tutorialMode = false;
            board.isTutorial = false;
            refreshBoard();
          }
        }
        else if (StdDraw.mousePressed()) { mouseClicked = true; }
        else if (!StdDraw.mousePressed() && mouseClicked) {
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
            tutorialSelectCard();
          }
        }
        else { continue; }
      }
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
          StdDraw.setPenColor(new Color(50, 50, 50, 255));
          StdDraw.filledRectangle(375, 250, 170, 75);
          StdDraw.setPenColor(StdDraw.WHITE);
          StdDraw.text(375, 275, "Removing a row...");
          StdDraw.text(375, 225, "Minus " + board.possibleSets + " points for possible sets");
          StdDraw.show(500);
          board.removeCol();
          refreshBoard();
        }
        // quit game (starts a new game)
        if (key == 'q') {
          endGame();
          board = new SetBoard();
          refreshBoard();
        }
        // show possible sets value
        if (key == 'p') {
          StdDraw.setPenColor(new Color(50, 50, 50, 255));
          StdDraw.filledRectangle(375, 250, 150, 75);
          StdDraw.setPenColor(StdDraw.WHITE);
          StdDraw.text(375, 250, "Possible Sets: " + board.possibleSets);
          StdDraw.show(500);
          refreshBoard();
        }
        if (key == 'h') {
          StdDraw.setPenColor(new Color(50, 50, 50, 255));
          StdDraw.filledRectangle(375, 250, 250, 150);
          StdDraw.setPenColor(StdDraw.WHITE);
          StdDraw.text(375, 350, "HELP");
          StdDraw.text(375, 300, "Press 'p' to see how many sets are possible");
          StdDraw.text(375, 275, "Press 'r' to remove a row if you're stuck");
          StdDraw.text(375, 250, "Press 't' to for a tutorial");
          StdDraw.text(375, 225, "Press 'q' to quit and end the game");
          StdDraw.text(375, 175, "Press 'h' to resume the game");
          StdDraw.show(200);
          isPaused = true;
        }
        if (key == 't') {
          StdDraw.setPenColor(new Color(50, 50, 50, 255));
          StdDraw.filledRectangle(375, 250, 200, 100);
          StdDraw.setPenColor(StdDraw.WHITE);
          StdDraw.text(375.0, 300, "Welcome to Tutorial Mode");
          StdDraw.text(375.0, 250, "Press 't' to exit tutorial mode");
          StdDraw.text(375.0, 225, "Select any two cards to see the card that will");
          StdDraw.text(375.0, 200, "\"Complete the Set\"");
          StdDraw.show(5000);
          tutorialMode = true;
          board.isTutorial = true;
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
      StdDraw.filledRectangle(375, 250, 250, 150);
      StdDraw.setPenColor(StdDraw.WHITE);
      String nameS = (dashOn < 2) ? name.toString() + "_" : name.toString() + " ";
      StdDraw.text(375, 300, "Good game! Your score was " + board.getScore() + ".");
      StdDraw.text(375, 250, "Type your name (6 char max) and hit enter to save.");
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
    StdDraw.filledRectangle(375, 250, 250, 150);
    StdDraw.setPenColor(StdDraw.WHITE);
    StdDraw.text(375, 250, "SAVED! Starting new game....");
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
    int oldScore = board.getScore();
    board.cardSelect(board.hovRow, board.hovCol);

    //switchCard(board.hovRow, board.hovCol);

    // if a set was made, refresh the display.
    if (board.reload) {
      if (!board.falseSet) {
        StdDraw.setPenColor(new Color(50, 50, 50, 255));
        StdDraw.filledRectangle(375, 250, 150, 75);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(375, 250, "Nice one! " + (board.getScore() - oldScore) + "  points!");
        StdDraw.show(500);
      }
      StdDraw.clear();
      board.reload = false;
    }
    refreshBoard();
  }
  
  /* 
  *  Select the card just clicked (mouse interaction). Color its border blue. If two
  *  cards have been selected, display the missing card for 1.5 seconds. Then clear 
  *  selected cards.
  */
  public static void tutorialSelectCard() {
    // select the corresponding card on the SetBoard.
    int oldScore = board.getScore();
    SetCard[] set = board.tutorialCardSelect(board.hovRow, board.hovCol);

    if (set == null) { 
      refreshBoard(); 
      return;
    }

    // if a set was made, refresh the display.
    if (board.reload) {
      StdDraw.setPenColor(new Color(50, 50, 50, 255));
      StdDraw.filledRectangle(375, 250, 300, 150);
      StdDraw.setPenColor(StdDraw.WHITE);
      // find the location of the card to be drawn.
      for (int i = 0; i < 3; i++) {
        double x = 375;
        if (i == 0) {
          x = x - CARD_SPACING - CARD_WIDTH;
        }
        if (i == 2) {
          x = x + CARD_SPACING + CARD_WIDTH;
        }
        double y = 275;
          
        // draw the card
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(x, y, CARD_WIDTH/2, CARD_HEIGHT/2);
        
        StdDraw.setPenRadius(.009);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.rectangle(x, y, CARD_WIDTH/2, CARD_HEIGHT/2);

        StdDraw.setPenRadius(.004);
        // draw the symbols on the card (according to the characteristics of the SetCard)
        SetCard card = set[i];
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
      }
      StdDraw.setPenColor(StdDraw.WHITE);
      StdDraw.text(375, 175, "In the game, you select three cards that form a set");
      StdDraw.text(375, 150, "and they would be cleared from the board");
      StdDraw.show(5000);
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
    StdDraw.filledRectangle(10, 0, 800, 600);

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
        // draw the symbols on the card (according to the characteristics of the SetCard)
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
    if (board.isTutorial) {
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setFont(new Font("Times", Font.PLAIN, 50));
      StdDraw.text(375, 490.0, "SET: TUTORIAL MODE");
      StdDraw.setFont();
      StdDraw.setFont(new Font(StdDraw.getFont().getName(), Font.PLAIN, 18));
      StdDraw.text(375.0, 458.0, "Press 't' to exit tutorial mode");
      StdDraw.text(375.0, 40.0, "Select any two cards to see the card that will \"Complete the Set\"");
      //StdDraw.text(380.0, 5.0, "Rachel Margulies");
      StdDraw.setFont();

    }
    else {
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.textRight(735.0, 470.0, "'h' for help");
      //StdDraw.textLeft(20.0, 30.0, "Press 'r' to remove a column");
      StdDraw.textLeft(20.0, 458.0, "Score: " + board.getScore());
      StdDraw.textLeft(20.0, 480.0, "Cards left: " + board.cardsLeft());
      StdDraw.setFont(new Font("Times", Font.PLAIN, 50));
      StdDraw.text(375.0, 480.0, "SET");
      StdDraw.setFont();
      StdDraw.setFont(new Font(StdDraw.getFont().getName(), Font.PLAIN, 18));
      StdDraw.text(375.0, 15.0, "Rachel Margulies");
      StdDraw.setFont();
    }
    StdDraw.show(400);
    if (board.falseSet) {
      board.clearSet();
      refreshBoard();
    }
    if (board.endGame) {
      endGame();
      board = new SetBoard();
      refreshBoard();
    }
    if (board.possibleSets == 0) {
      StdDraw.setPenColor(new Color(50, 50, 50, 255));
      StdDraw.filledRectangle(375, 250, 175, 100);
      StdDraw.setPenColor(StdDraw.WHITE);
      StdDraw.text(375, 275, "No possible sets!");
      StdDraw.text(375, 225, "Removing a column...");
      StdDraw.show(750);
      board.removeCol();
      refreshBoard();
    }
  }
}
