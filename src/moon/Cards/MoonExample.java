package moon.Cards;

import java.util.ArrayList;
import java.util.Random;

/*
 * Created by Moon on 1/11/2018
 * This is an example of how to make a Burn Bot
 * using my helper classes and interface
 *
 * Note that the specific helper classes we'll
 * need are Card (represents a card) and Hand
 * (represents a group of cards)
 */
public class MoonExample implements Bot{
    private Hand hand = new Hand();
    private Hand faceups = new Hand();
    private Hand plays = new Hand();

    //Bot name, partially random
    private String name = "MoonBot" + Integer.toString(new Random().nextInt(500));

    //My bot's name
    public String getName() {
        return name;
    }

    //Return our hand
    public Hand getHand() {
        return hand;
    }

    //Return face up cards
    public Hand getFaceups() {
        return faceups;
    }

    //These add a card/cards to your hand.
    public void addCard(Card[] c) {
        hand.addCards(c);
    }

    public void addCard(ArrayList<Card> c) {
        hand.addCards(c);
    }

    //This method is called when it's time
    //to swap out cards from your hand and
    //your face up cards.
    public void swapCards() {
        //This gets a little hairy. Sorry.
        boolean done = false;
        while (!done) {
            done = true;
            Card[] fh = faceups.getCards();

            for (int i = 0; i < 3; i++) {
                for (int e = 0; e < 3; e++) {
                    if (getValue(fh[i]) < getValue(hand.getCards()[i])) {
                        faceups.addCards(hand.removeCard(hand.getCards()[i]));
                        hand.addCards(faceups.removeCard(fh[i]));

                        done = false; //The loop needs to keep going until it makes one pass without replacing a card
                        break;
                    }
                }
            }
        }
    }

    //Using the current cards, judges the cards they want
    //to play from their hand and returns them
    //AND REMOVES THEM FROM YOUR HAND
    public Card[] getPlay() {
        //If we still have cards in our hand
        if (hand.getCards().length > 0) {
            if (plays.getCards().length > 0) {
                //Remove the card/s from our hand
                //NOTE: Yes, yes you do do this on your own
                Card[] bplay = getBestPlay(plays.getCards(), hand.getCards());
                hand.removeCard(bplay);
                return bplay;
            }
            else {
                Card[] bplay = getBestPlay(new Card[]{new Card(2)}, hand.getCards());
                hand.removeCard(bplay);
                return bplay;
            }
        }
        //If we have to play from faceups
        else if (faceups.getCards().length > 0) {
            if (plays.getCards().length > 0) {
                //Remove the card/s from our faceups
                //NOTE: Yes, yes you do do this on your own
                Card[] bplay = getBestPlay(plays.getCards(), faceups.getCards());
                faceups.removeCard(bplay);
                return bplay;
            }
            else {
                Card[] bplay = getBestPlay(new Card[]{new Card(2)}, faceups.getCards());
                faceups.removeCard(bplay);
                return bplay;
            }
        }

        return null;
    }

    //Returns the best available cards to play
    private Card[] getBestPlay(Card[] plays, Card[] options) {
        Card topCard = plays[plays.length - 1];

        Card minCard = options[0]; //Set to a random value that we have in our hand
        for (Card c : options) {
            if (getValue(c) < getValue(minCard) && getValue(topCard) <= getValue(c)) {
                minCard = c;
            }
        }

        //Game will add to our hand if we fail to play a card lower than the one on top of the discards
        return new Card[] {minCard};
    }

    //Sets the last cards played, for the bot
    //to use to judge their best plays
    //NOTE: This is called whether it's the
    //bot's turn or not, which leaves the bot creator
    //the option of keeping track of *all* plays for
    //vindictive purposes
    public void setLastPlay(Card[] play) {
        if (play == null || play.length <= 0) {
            plays.clear();
        }
        else plays.addCards(play);
    }

    //Sets the face up cards
    //NOTE: Face downs are handed by the
    //BotBattle class. They're random anyway, soooo
    //I'll just do them randomly for you.
    public void setFaceups(ArrayList<Card> faceups) {
        this.faceups.addCards(faceups);
    }

    //Reset for next game
    public void resetHands() {
        hand = new Hand();
        faceups = new Hand();
        plays = new Hand();
    }

    /* ----- CARD HELPER METHODS ----- */

    private boolean isMagic(Card c) {
        return (c.getValue() == 2 || c.getValue() == 7 || c.getValue() == 10);
    }

    //TODO: Change this to suit your desires
    //In-house card values, SUBJECT TO CHANGE
    //This is where some of your bot's strategies are formed
    private int getValue(Card c) {
        if (!isMagic(c) && !(c.getValue() == 1)) return c.getValue();
        else if (c.getValue() == 1) return 14;
        else if (isMagic(c)) return 15;
        return -1;
    }

    public void printHands() {
        System.out.println(getName() + " : Hand:");
        System.out.println(hand.printCards());
        System.out.println();

        System.out.println(getName() + " : Faceups:");
        System.out.println(faceups.printCards());
        System.out.println();
        System.out.println();
    }
}
