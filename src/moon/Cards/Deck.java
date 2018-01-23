package moon.Cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Moon on 10/17/2017.
 * Represents a deck of cards.
 * 1-13 Spades
 * 14-26 Hearts
 * 27-39 Clubs
 * 40-52 Diamonds
 */
public class Deck {
    //Holds the list of all the cards
    private ArrayList<Integer> cards = new ArrayList<>();

    //Initialize the deck with all cards in order
    public Deck() {
        //Add all cards to the deck
        Integer[] e = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
                27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
                40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52};
        cards.addAll(Arrays.asList(e));
    }

    //Deal and return a number of cards
    public ArrayList<Card> deal(int number) {
        ArrayList<Card> c = new ArrayList<>();
        //Output cards
        for (int i = 0; i < number; i++) {
            c.add(new Card(cards.get(i)));
        }
        //Remove cards from deck
        for (int i = 0; i < number; i++) {
            cards.remove(0);
        }
        return c;
    }

    //Shuffle the deck
    public void shuffle() {
        int n = cards.size();
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(cards, i, change);
        }
    }

    //Returns how many cards are in the deck
    public int size() {
        return cards.size();
    }

    //----- Array helpers -----//
    private static void swap(ArrayList<Integer> a, int i, int change) {
        int helper = a.get(i);
        a.set(i, a.get(change));
        a.set(change, helper);
    }
}
