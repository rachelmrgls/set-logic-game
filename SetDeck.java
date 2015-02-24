/*************************************************************************
 *  Compilation:  javac SetDeck.java
 *  Execution:    java SetDeck
 *
 *  This class provides the base functions of a deck of cards. All functions
 *  other than the constructor are applicable to any type of card deck.
 *
 *  Dependencies: SetCard.java, Math.java
 *
 *  Remarks
 *  -------
 *    -  might add a way to put the cards back into the deck, even after 
 *       being dealt.
 *  
 *  Author : Rachel Margulies, December 2014
 *  
 *************************************************************************/

public class SetDeck {
    private SetCard[] deck;
    private int cardsUsed;  // number of cards dealt

    /*
    *  Constructor. Makes a new deck by constructing the 81 (3^4) possible cards.
    *  Initially the cards are in sorted order, but are shuffled to randomize the order.
    */
    public SetDeck() {
    	deck = new SetCard[81];
        // How many cards have been created so far.
        int cardCt = 0;
        for ( int shape = 0; shape <= 2; shape++ ) {
        	for ( int color = 0; color <= 2; color++ ) {
        		for ( int texture = 0; texture <= 2; texture++ ) {
            		for (int value = 1; value <= 3; value++ ) {
                        // create a card with unique shape, color, texture, and value.
                		deck[cardCt] = new SetCard(shape, color, texture, value);
                		cardCt++;
                	}
                }
            }
        }
        shuffle();
    }

    /*
     * Shuffle the deck. Set cardsUsed to 0.
     */
    public void shuffle() {
        // Iterate backwards through the deck, randomly swapping each card 
        // with one of the cards at an index yet to be visited.
        for (int i = deck.length - 1; i > 0; i-- ) {
            int rand = (int)(Math.random()*(i+1));
            SetCard temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        cardsUsed = 0;
    }
    /*
     * The number of cards remaining in the deck (yet to be dealt).
     */
    public int cardsLeft() {
        return deck.length - cardsUsed;
    }
    /*
     * Remove the next card from the deck and return it. Returns null if 
     * there are no more cards in the deck. 
     */
    public SetCard dealCard() {
        if (cardsUsed == deck.length)
            return null;
        // Cards are not removed; cardsUsed keeps track of the current index.
        return deck[cardsUsed++];
    }
}
