/*************************************************************************
 *  Compilation:  javac SetBoard.java
 *  Execution:    java SetBoard 
 *
 *  This class provides the base functions that can be done on a Set game 
 *  board. A game board consists of 3 rows and 4 columns of Set cards. This
 *  board allows for the one-by-one selection of cards to form a set, and 
 *  when 3 cards have been selected, it checks if a valid set has been formed.
 *  The user's score is also tracked. The SetBoard also enables removal of a
 *  column, in the case where a set may not be present. 
 *
 *  Dependencies: SetCard.java, SetDeck.java, Math.java
 *
 *  Remarks
 *  -------
 *    -  might add a way to put the cards removed in a call to redeal() at 
 *       at the bottom of the deck...
 *  
 *  Author : Rachel Margulies, December 2014
 *  
 *************************************************************************/

public class SetBoard {
	private SetCard[][] board;
	private SetDeck deck;
	private int score;  		// user's current score

	// Location of the first selected card.
	private int row1; 
	private int col1;
	// Location of the second selected card.
	private int row2;
	private int col2;
	// Location of the third selected card.
	private int row3;
	private int col3;

	// Used if gameplay is controlled by arrow keys. Location of the hovering selector.
	public int hovRow;
	public int hovCol;

	// boolean for tracking if the displayed gameboard needs to be reloaded.
	public boolean reload;
	public boolean falseSet;

	/*
	*  Constructor. Makes a new board with new deck. Deals the original gameboard.
	*  Ensures that no cards are considered to be selected. Resets the user's score.
	*/
	public SetBoard() {
		board = new SetCard[3][4];
		deck = new SetDeck();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = deck.dealCard();
			}
		}
		row1 = -1;
		row2 = -1;
		row3 = -1;
		col1 = -1;
		col2 = -1;
		col3 = -1;
		
		hovRow = 0;
		hovCol = 0;

		score = 0;

		falseSet = false;
	}
	/*
	*  Returns the SetCard at the given row and column in the board.
	*/
	public SetCard getCard(int row, int col) {
		return board[row][col];
	}
	/*
	*  Returns the user's current score.
	*/
	public int getScore() {
		return score;
	}
	/*
	*  Returns the number of cards left to be dealt.
	*/
	public int cardsLeft() {
		return deck.cardsLeft();
	}
	/*
	*  Checks if the card at the given row and column has been selected or not.
	*/
	public boolean isCardSelected(int row, int col) {
		if (row1 == row && col1 == col) {
			return true;
		}
		else if (row2 == row && col2 == col) {
			return true;
		}
		else if (row3 == row && col3 == col) {
			return true;
		}
		return false;

	}
	/*
	*  Either selects the card at this position, or unselects it if it was already selected.
	*  If 3 cards have been selected, this functions checks whether the cards form a valid set.
	*/
	public void cardSelect (int row, int col) {
		// if this card was already selected, (and is the first card) unselect it
		if (row1 == row && col1 == col) {
			row1 = -1;
			col1 = -1;
			return;
		}
		// if this card was already selected, (and is the second card) unselect it
		if (row2 == row && col2 == col) {
			row2 = -1;
			col2 = -1;
			return;
		}
		// if the first card is not set, set it
		if (row1 == -1) {
			row1 = row;
			col1 = col;
		}
		// if the second card is not set, set it
		else if (row2 == -1) {
			row2 = row;
			col2 = col;
		}
		// check for complete set
		else {
			// set the third card.
			row3 = row;
			col3 = col;
			// check if the set is valid (add points if so, and redeal).
			if (checkSet()) {
				redeal();
			}
			// if the set is not valid, unselect all cards that were being considered.
			else {
				falseSet = true;
			}
			reload = true;
		}

	}
	/*
	*  Checks if the selected cards form a valid set. If not, return false. If it is a valid set, 
	*  tally the score (1 point for each uniqueness among the cards --> 1-4 possible points for a
	*  given set).
	*/
	private boolean checkSet() {
		// get the SetCards corresponding to the selected cards on the board.
		SetCard c1 = board[row1][col1];
		SetCard c2 = board[row2][col2];
		SetCard c3 = board[row3][col3];

		/* calculate if the characteristics of the three cards allow it to be a valid set. 
		*  Since shape, color, and texture each are each assigned 0, 1, 2, I use the following fact
		*  for each of the characteristics (shape as example) the sum can take values between 0-6:
		*  - if the sum is 0, they must be 3 0s. (VALID)
		*  - if the sum is 1, there must be 2 0s and 1 1s. (NOT VALID)
		*  - if the sum is 2, they are either 2 1s and 1 0s, or 1 2s and 2 0s. (NOT VALID)
		*  - if the sum is 3, there must be 3 1s, or 1 0s 1 1s and 1 2s. (BOTH VALID)
		*  - if the sum is 4, they are either 2 2s and 1 0s, or 1 2s and 2 1s (NOT VALID)
		*  - if the sum is 5, there must be 2 2s, and 1 1s. (NOT VALID)
		*  - if the sum is 6, they must be 3 2s. (VALID)
		*  Thus, the only sums which produce valid combinations, following the rules of Set, 
		*  are 0, 3, and 6. 
		*/ 

		// check shape
		switch (c1.getShape() + c2.getShape() + c3.getShape()) {
           // all SQUIGGLE
           case 0:	break;
           // all OVAL or all UNIQUE
           case 3:	break;
           // all DIAMOND;
           case 6:	break;
           // not a valid Set
           default:	return false;
        }
        switch (c1.getColor() + c2.getColor() + c3.getColor()) {
           // all PURPLE
           case 0:	break;
           // all GREEN or all UNIQUE
           case 3:	break;
           // all RED;
           case 6:	break;
           // not a valid Set
           default:	return false;
        }
        switch (c1.getTexture() + c2.getTexture() + c3.getTexture()) {
           // all SOLID
           case 0:	break;
           // all EMPTY or all UNIQUE
           case 3:	break;
           // all SHADED;
           case 6:	break;
           // not a valid Set
           default:	return false;
        }
        switch (c1.getValue() + c2.getValue() + c3.getValue() - 3) {
           // all 1
           case 0:	break;
           // all 2 or all UNIQUE
           case 3:	break;
           // all 3;
           case 6:	break;
           // not a valid Set
           default:	return false;
        }

        // a point for each type of difference between the 3 cards (point range, 1-4)
        if (c1.getShape() != c2.getShape()) score++;
        if (c1.getColor() != c2.getColor()) score++;
        if (c1.getTexture() != c2.getTexture()) score++;
        if (c1.getValue() != c2.getValue()) score++;

        return true;
	}
	public void clearSet() {
		row1 = -1;
		row2 = -1;
		row3 = -1;
		col1 = -1;
		col2 = -1;
		col3 = -1;
		falseSet = false;
	}
	/*
	*  Removes and redeals a column chosen at random from the game board. Used when the user cannot
	*  locate a set.
	*/
	public void removeCol() {
		// select a random column
		int col = (int) (Math.random()* board[0].length);
		// set the selected cards to this column
		col1 = col;
		col2 = col;
		col3 = col;
		row1 = 0;
		row2 = 1;
		row3 = 2;

		SetCard[] toRemove = {board[0][col],board[1][col], board[2][col]};
		deck.shiftDeck(toRemove);
		redeal();
	}
	/*
	*  Replace the cards at the selected positions with new cards from the deck, and 
	*  set all cards to unselected. 
	*/
	public void redeal() {
		board[row1][col1] = deck.dealCard();
		board[row2][col2] = deck.dealCard();
		board[row3][col3] = deck.dealCard();
		row1 = -1;
		row2 = -1;
		row3 = -1;
		col1 = -1;
		col2 = -1;
		col3 = -1;
	}
}