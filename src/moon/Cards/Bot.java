package moon.Cards;

/*
 * Created by Moon on 1/11/2018
 */

import java.util.ArrayList;

public interface Bot {
    //Return your bot's name
    String getName();

    //Hand variables, so the game can check to see if you've won
    Hand getHand();
    Hand getFaceups();

    //This adds a card to your hand.
    //You'll wanna deal with this inside
    //your own bot so you know what cards
    //you have
    void addCard(Card[] c);
    void addCard(ArrayList<Card> c);

    //This method is called when it's time
    //to swap out cards from your hand and
    //your face up cards. Return when you're done.
    void swapCards();

    //Using the current cards, judges the cards they want
    //to play from their hand and returns them
    Card[] getPlay();

    //Sets the last cards played, for the bot
    //to use to judge their best plays
    //NOTE: This is called whether it's the
    //bot's turn or not, which leaves the bot creator
    //the option of keeping track of *all* plays for
    //vindictive purposes
    void setLastPlay(Card[] play);

    //Sets the face up cards
    //NOTE: Face downs are handed by the
    //BotBattle class. They're random anyway, soooo
    //I'll just do them randomly for you.
    void setFaceups(ArrayList<Card> faceups);

    //Reset your hands for the next game
    void resetHands();

    void printHands();
}
