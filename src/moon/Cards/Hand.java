package moon.Cards;

import java.util.ArrayList;

/**
 * Created by Moon on 10/17/2017.
 */
public class Hand {
    public ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCards(ArrayList<Card> c) {
        for (Card i: c) {
            addCards(i);
        }
    }

    public void addCards(Card[] c) {
        for (int i = 0; i < c.length; i++) {
            addCards(c[i]);
        }
    }

    public void addCards(Card c) {
        cards.add(c);
    }

    public Card[] removeCard(Card[] c) {
        ArrayList<Card> ret = new ArrayList<>();
        for (Card card : c) {
            ret.add(removeCard(card));
        }
        Card[] reta = new Card[ret.size()];
        ret.toArray(reta);
        return reta;
    }

    public Card removeCard(Card c) {
        return cards.remove(cards.indexOf(c));
    }

    public Card[] getCards() {
        return cards.toArray(new Card[cards.size()]);
    }

    public void clear() {
        cards.clear();
    }

    public String printCards() {
        String ret = cards.get(0).getName();
        for (int i = 1; i < cards.size(); i++) {
            ret += "\n" + cards.get(i).getName();
        }
        return ret;
    }
}
