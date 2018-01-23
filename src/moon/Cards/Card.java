package moon.Cards;

/**
 * Created by Moon on 10/17/2017.
 * Represents a card.
 * 1-13 Spades
 * 14-26 Hearts
 * 27-39 Clubs
 * 40-52 Diamonds
 */
public class Card {
    //ID of card in the overall deck
    private int cardID;

    //Point value of the card. Aces are low.
    private int value;

    public enum Suit {
        SPADES(0),
        HEARTS(1),
        CLUBS(2),
        DIAMONDS(3);

        private int value = -1;
        Suit(int value) { this.value = value; }
    }

    //Suit value. 0 = Spades, 1 = Hearts, 2 = Clubs, 3 = Diamonds
    private Suit suit;

    public Card(int id) {
        this.cardID = id;

        //Assign suit value
        int suitNum = ((cardID - 1) / 13);
        if (suitNum == 0) suit = Suit.SPADES;
        else if (suitNum == 1) suit = Suit.HEARTS;
        else if (suitNum == 2) suit = Suit.CLUBS;
        else if (suitNum == 3) suit = Suit.DIAMONDS;

        //Assign card value, aces are low
        value = (cardID % 13) == 0 ? 13 : (cardID % 13);
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        //This card's cardID in its suit (ie: 9, 4, 6)
        String suitValueString = Integer.toString(value);
        if (value == 1) suitValueString = "Ace";
        else if (value == 11) suitValueString = "Jack";
        else if (value == 12) suitValueString = "Queen";
        else if (value == 13) suitValueString = "King";

        return suitValueString + " of " +
                (((suit == Suit.SPADES)) ? "Spades" : "") + (((suit == Suit.HEARTS)) ? "Hearts" : "") + (((suit == Suit.CLUBS)) ? "Clubs" : "") + (((suit == Suit.DIAMONDS)) ? "Diamonds" : "");
    }
}
