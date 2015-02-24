/*************************************************************************
 *  Compilation:  javac SetCard.java
 *  Execution:    java SetCard
 *
 *  This class provides the framework and characteristics of a card in Set.
 *
 *  Dependencies: None.
 *
 *  Author : Rachel Margulies, December 2014
 *  
 *************************************************************************/

public class SetCard {
	public final static int SQUIGGLE = 0, OVAL = 1, DIAMOND = 2;    // possible shapes
                        
	public final static int PURPLE = 0, GREEN = 1, RED = 2;         // possible colors

    public final static int SOLID = 0, EMPTY = 1, SHADED = 2;       // possible textures
    
    private final int shape;        // SQUIGGLE, OVAL, DIAMOND
    private final int color;        // PURPLE, GREEN, RED
    private final int texture;      // SOLID, EMPTY, SHADED
    private final int value;        // 1, 2, 3
    
    /*
    *  Constructor. Makes a new card.
    */
    public SetCard(int theShape, int theColor, int theTexture, int theValue) {
    	shape = theShape;
    	color = theColor;
    	texture = theTexture;
    	value = theValue;
    }
    /*
    *  Returns the card's shape.
    */
    public int getShape() {
    	return shape;
    }
    /*
    *  Returns the card's color.
    */
    public int getColor() {
    	return color;
    }
    /*
    *  Returns the card's texture.
    */
    public int getTexture() {
    	return texture;
    }
    /*
    *  Returns the card's value.
    */
    public int getValue() {
    	return value;
    }
    /*
    *  Returns a string representation of the card's characteristics: "value color shape texture".
    */
    public String toString() {
        String s = "" + value;
        switch (color) {
           // purple
            case 0:  s = s + " prp ";
                    break;
           // green
            case 1:  s = s + " grn ";
                    break;
            // red
            case 2:  s = s + " red ";
                    break;
            default: s = s + " ??? ";
                    break;
        }
        switch (shape) {
           // squiggle
            case 0:  s = s + " sqig ";
                    break;
           // oval
            case 1:  s = s + " oval ";
                    break;
            // diamond
            case 2:  s = s + " diam ";
                    break;
            default: s = s + " ???? ";
                    break;
        }
        switch (texture) {
           // solid
            case 0:  s = s + " sol ";
                    break;
           // empty
            case 1:  s = s + " emp ";
                    break;
            // shaded
            case 2:  s = s + " shd ";
                    break;
            default: s = s + " ??? ";
                    break;
        }
        return s;
    }
}