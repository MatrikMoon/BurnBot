
/*
 * Created by Moon on 1/11/2018 when I should be working on MPC
 * This is a poorly made implementation of burn. Do forgive the overuse of hashmaps and
 * loops, but I'm in a rush to move on to other projects.
 */

import moon.Cards.*;

import java.util.*;

public class BotBattle {
    //private list of players, shuffeld for random order
    private static ArrayList<String> botNames = new ArrayList<>();

    //Win counts
    private static HashMap<String, Integer> winCount = new HashMap<>();
    
    //List of bot players
    private static HashMap<String, Bot> botMap = new HashMap<>();

    //Stores each bot's face down cards
    private static HashMap<String, Hand> faceDowns = new HashMap<>();

    //Deck for this game
    private static Deck d = new Deck();

    //Deck representing discard pile
    private static Hand discard = new Hand();

    public static void main(String[] args) {
        //Begin by adding all the bots
        //NOTE: For the example, I'll be adding two of my own bot.
        //NOTE 2: When it comes time to battle, I'll be taking only
        //your bot classes which extend the Bot interface

        MoonExample a = new MoonExample();
        MoonExample b = new MoonExample();
        botMap.put(a.getName(), a);
        botMap.put(b.getName(), b);

        for (Map.Entry<String, Bot> e : botMap.entrySet()) {
            //Add their name to the name list
            botNames.add(e.getKey());
            winCount.put(e.getKey(), 0);
        }

        int totalGames = 1;
        int lastUpdate = 0;

        //Print the above bar for reference
        System.out.print("[");
        for (int i = 0; i < 100; i++) {
            System.out.print(" ");
        }
        System.out.println("]");
        System.out.print(" ");

        for (int iter = 0; iter < totalGames; iter++) {
            //Begin game by shuffling a deck and dealing all the necessary cards
            d.shuffle();

            System.out.println("Shuffling...");

            for (Map.Entry<String, Bot> e : botMap.entrySet()) {
                //Deal hand and face-ups
                e.getValue().addCard(d.deal(3));
                e.getValue().setFaceups(d.deal(3));

                //We'll control the face down cards here so
                //there's no peeking
                Hand fd = new Hand();
                fd.addCards(d.deal(3));
                faceDowns.put(e.getKey(), fd);

                //Alert the bot they can swap out cards now
                e.getValue().swapCards();

                //e.getValue().printHands();
            }

            //System.out.println("BEGIN PLAY!");

            //It is now time to begin play.
            //We'll pick a random starter
            Collections.shuffle(botNames);
            int playerIndex = 0;

            boolean finished = false;
            while (!finished) {
                //If it's time to loop around to the first player, reset the player index
                if (playerIndex >= botMap.size()) playerIndex = 0;

                String playerName = botNames.get(playerIndex);
                Bot player = botMap.get(playerName);

                Card[] play;
                //If the player has cards in their hand or faceups, it's their
                //responsibility to play the best card
                if (player.getFaceups().getCards().length > 0 ||
                        player.getHand().getCards().length > 0) {
                    play = player.getPlay();
                }
                //Otherwise, we'll play from their face downs
                else {
                    Hand fd = faceDowns.get(playerName);
                    play = new Card[]{fd.removeCard(fd.getCards()[0])};
                }

                if (player.getHand().getCards().length > 0) {
                    System.out.println(player.getName() + " PLAYED " + play[0].getName() + " FROM THEIR HAND");
                } else if (player.getFaceups().getCards().length > 0) {
                    System.out.println(player.getName() + " PLAYED " + play[0].getName() + " FROM THEIR FACE UP CARDS");
                } else {
                    System.out.println(player.getName() + " PLAYED " + play[0].getName() + " FROM THEIR FACE DOWN CARDS");
                }

                //Verify the play. If it's invalid, give the player all the cards
                if (discard.getCards().length > 0 && !verifyPlay(play)) {
                    player.addCard(play);
                    player.addCard(discard.getCards());
                    discard.clear();

                    System.out.println(player.getName() + " PICKS UP THE DISCARDS");
                } else {
                    discard.addCards(play);
                }

                //If the play was a 10, we need to clear the discards and let the player play again
                if (play[0].getValue() == 10) {
                    discard.clear();
                    playerIndex--; //We need to play again, soooo
                }

                //Check for winner
                if (player.getFaceups().getCards().length <= 0 &&
                        player.getHand().getCards().length <= 0 &&
                        faceDowns.get(playerName).getCards().length <= 0) {
                    finished = true;
                    System.out.println(player.getName() + " WINS!");
                    winCount.put(playerName, winCount.get(playerName) + 1);
                    break;
                }

                //If there's cards left in the deck, players draw up to 3
                if (d.size() > 0) {
                    for (int e = 0; e < play.length; e++) {
                        player.addCard(d.deal(1));
                    }
                }
                updateLastPlayed(discard.getCards());

                playerIndex++;
            }

            //Post-game: reset hands and decks
            d = new Deck();
            for (Map.Entry<String, Bot> e : botMap.entrySet()) {
                e.getValue().resetHands();
            }

            if (lastUpdate < ((100.0 / totalGames) * iter)) {
                System.out.print("=");
                lastUpdate++;
            }
        }

        //Print the winner list
        System.out.println("\nTotals:");
        for (Map.Entry<String, Integer> e : winCount.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }

    private static void updateLastPlayed(Card[] c) {
        for (Map.Entry<String, Bot> e : botMap.entrySet()) {
            e.getValue().setLastPlay(c);
        }
    }

    //Return true if the play is valid
    private static boolean verifyPlay(Card[] play) {
        //If the last played card isn't magic, just return the normal play >= discard
        if (!isMagic(discard.getCards()[discard.getCards().length - 1])) {
            return getValue(play[0]) >= getValue(discard.getCards()[discard.getCards().length - 1]);
        }

        //If the last played card was a 2, any card played on top of it is valid
        //NOTE: Technically, there doesn't have to be a special case for this, because 2
        //is the lowest possible play anyway... But meh.
        else if (discard.getCards()[discard.getCards().length - 1].getValue() == 2) return true;

        //If a 7 was played, return true if the value of the new played card is 7 or lower, OR MAGIC
        else if (discard.getCards()[discard.getCards().length - 1].getValue() == 7) return getValue(play[0]) <= 7 || isMagic(play[0]);

        return false;
    }

    /* ----- CARD HELPER METHODS ----- */

    private static boolean isMagic(Card c) {
        return (c.getValue() == 2 || c.getValue() == 7 || c.getValue() == 10);
    }

    //This is a bare minimal setup, which suits the needs of making sure plays are valid
    private static int getValue(Card c) {
        if (!isMagic(c) && !(c.getValue() == 1)) return c.getValue();
        else if (c.getValue() == 1) return 13;
        else if (isMagic(c)) return 14;
        return -1;
    }
}
