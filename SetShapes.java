/*************************************************************************
 *  Compilation:  javac SetShapes.java
 *  Execution:    java SetShapes
 *
 *  This class draws the content of a SetCard. It takes into account
 *  shape, color, value, and texture.
 *
 *  Dependencies: SetCard.java, StdDraw.java, Color.java
 *
 *  Remarks
 *  -------
 *    -  might clean up the appearance of the cards.
 *    -  might change how a SHADED card looks.
 *  
 *  Author : Rachel Margulies, December 2014
 *  
 *************************************************************************/

import java.awt.Color;


public class SetShapes {

    // draw a card of squiggle type.
	public static void squiggle(SetCard card, double x, double y) {
        penColor(card);

        // draw the correct number of shapes, at appropriate offsets
        switch (card.getValue()) {
            case 1:     squiggle(card.getTexture(), x, y);
                        break;
            case 2:     squiggle(card.getTexture(), x - 25, y);
                        squiggle(card.getTexture(), x + 25, y);
                        break;
            case 3:     squiggle(card.getTexture(), x - 45, y);
                        squiggle(card.getTexture(), x, y);
                        squiggle(card.getTexture(), x + 45, y);
                        break;
            default:    break;
        }
        // draw a darker outline if the card is shaded texture.
        if (card.getTexture() == SetCard.SHADED) {
            SetCard c = new SetCard(card.getShape(), card.getColor(), SetCard.EMPTY, card.getValue());
            squiggle(c, x, y);
        }
    }

    // draw a squiggle
    private static void squiggle(int texture, double x, double y) {
        double[] xArr = {x, x + 20 , x + 5, x + 20, x, x - 20, x - 5, x - 20};
        double[] yArr = {y + 40, y + 25 , y - 10, y - 25, y - 40, y - 25,  y + 10, y + 25};
        switch (texture) {
           // empty texture, so draw an empty polygon
            case SetCard.EMPTY:  StdDraw.polygon(xArr, yArr);
                                break;

            // other texture, draw a filled polygon
           default:             StdDraw.filledPolygon(xArr, yArr);
                                break;
        }
    }

    // draw a card of diamond type.
    public static void diamond(SetCard card, double x, double y) {
        penColor(card);

        // draw the correct number of shapes, at appropriate offsets
        switch (card.getValue()) {
            case 1:     diamond(card.getTexture(), x, y);
                        break;
            case 2:     diamond(card.getTexture(), x - 25, y);
                        diamond(card.getTexture(), x + 25, y);
                        break;
            case 3:     diamond(card.getTexture(), x - 45, y);
                        diamond(card.getTexture(), x, y);
                        diamond(card.getTexture(), x + 45, y);
                        break;
            default:    break;
        }
        // draw a darker outline if the card is shaded texture.
        if (card.getTexture() == SetCard.SHADED) {
            SetCard c = new SetCard(card.getShape(), card.getColor(), SetCard.EMPTY, card.getValue());
            diamond(c, x, y);
        }
    }

    // draw a diamond
    private static void diamond(int texture, double x, double y) {
        double[] xArr = {x, x + 20 , x,  x - 20};
        double[] yArr = {y + 40, y, y - 40, y};
        switch (texture) {
           // empty texture, so draw an empty polygon
           case SetCard.EMPTY:  StdDraw.polygon(xArr, yArr);
                                break;
            // other texture, draw a filled polygon
           default:             StdDraw.filledPolygon(xArr, yArr);
                                break;
        }
    }

    // draw a card of oval type.
    public static void oval(SetCard card, double x, double y) {
        penColor(card);

        // draw the correct number of shapes, at appropriate offsets
        switch (card.getValue()) {
            case 1:     oval(card.getTexture(), x, y);
                        break;
            case 2:     oval(card.getTexture(), x - 25, y);
                        oval(card.getTexture(), x + 25, y);
                        break;
            case 3:     oval(card.getTexture(), x - 45, y);
                        oval(card.getTexture(), x, y);
                        oval(card.getTexture(), x + 45, y);
                        break;
            default:    break;
        }
        // draw a darker outline if the card is shaded texture.
        if (card.getTexture() == SetCard.SHADED) {
            SetCard c = new SetCard(card.getShape(), card.getColor(), SetCard.EMPTY, card.getValue());
            oval(c, x, y);
        }
    }

    // draw an oval
    private static void oval(int texture, double x, double y) {
        switch (texture) {
           // empty texture, so draw an empty polygon
           case SetCard.EMPTY:  StdDraw.ellipse(x, y, 17, 40);
                                break;
            // other texture, draw a filled polygon
           default:             StdDraw.filledEllipse(x, y, 17, 40);
                                break;
        }
    }

    // set the pen color to draw the shape
    private static void penColor(SetCard card) {
        Color color;
        switch (card.getColor()) {
           // PURPLE
           case SetCard.PURPLE: color = SetGameplay.PURPLE; 
                                break;
           // GREEN 
           case SetCard.GREEN:  color = SetGameplay.GREEN; 
                                break;
            // RED
           case SetCard.RED:    color = SetGameplay.RED; 
                                break;
           default: return;
        }
        switch (card.getTexture()) {
           // SHADED
           case SetCard.SHADED: StdDraw.setPenColor(new Color(color.getRed(), 
                                                              color.getGreen(), 
                                                              color.getBlue(), 
                                                              90));
                                break;
            // other
           default:             StdDraw.setPenColor(color);
        }
    }
}
