package application;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.concurrent.TimeUnit;


public class BlackJack {
    //Global variables to be accessed in main class
    static int balance = 0;
    static int bet = 0;
    static ArrayList<Card> playerHand = new ArrayList<>();
    static ArrayList<Card> dealerHand = new ArrayList<>();
    static Deck deck = shuffle(setDeck());
    static boolean firstRound = true;
    static boolean playerTurn = true;
    public static void reset() {
        //balance = 0;
        bet = 0;
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        deck = shuffle(setDeck());
        firstRound = true;
        playerTurn = true;
    }
    
    
    //Adds cards to the deck
    public static Deck setDeck(){
        Deck cards = new Deck();
        cards.cards = new ArrayList<>();
        for(int i = 2; i < 11; i++) {
            for(int j =0; j < 4; j++) {
                Card piece = new Card();
                piece.val = i;
                piece.name = Integer.toString(i);
                cards.cards.add(piece);
            }           
        }
        for(int j =0; j < 4; j++) {
            Card piece = new Card();
            piece.val = 10;
            piece.name = "J";
            cards.cards.add(piece);
        }
        for(int j =0; j < 4; j++) {
            Card piece = new Card();
            piece.val = 10;
            piece.name = "Q";
            cards.cards.add(piece);
        }
        for(int j =0; j < 4; j++) {
            Card piece = new Card();
            piece.val = 10;
            piece.name = "K";
            cards.cards.add(piece);
        }
        for(int j =0; j < 4; j++) {
            Card piece = new Card();
            piece.val = 1;
            piece.name = "A";
            cards.cards.add(piece);
        }
        return cards;
    }
    
    //Shuffles the deck
    public static Deck shuffle(Deck cards){
        Collections.shuffle(cards.cards);
        return cards;
    }
    
    //sets up beginning of the round
    public static void roundStart() {
        playerHand.add(deck.cards.get(0));
        deck.cards.remove(0);
        playerHand.add(deck.cards.get(0));
        deck.cards.remove(0);
        //Deal dealers cards
        dealerHand.add(deck.cards.get(0));
        deck.cards.remove(0);
        dealerHand.add(deck.cards.get(0));
        deck.cards.remove(0);
        playerHand = getCardTotalAce(playerHand);
        dealerHand = getCardTotalAce(dealerHand);
        
    }
    //Hit activity
    public static void hit() {
        playerHand.add(deck.cards.get(0));
        deck.cards.remove(0);
        playerHand = getCardTotalAce(playerHand);
        displayPlayerHand(playerHand);
        displayDealerHand(dealerHand,playerTurn);
    }
    //Stand activity
    public static void stand() {
        playerTurn = false;
    }
    //Double activity
    public static void dub() {
        playerHand.add(deck.cards.get(0));
        deck.cards.remove(0);
        playerHand = getCardTotalAce(playerHand);
        displayPlayerHand(playerHand);
        displayDealerHand(dealerHand,playerTurn);
    }
    //Runs the dealers turn
    public static void dealerTurn() {
      
        dealerHand = getCardTotalAce(dealerHand);
        while(getCardTotal(dealerHand) < 17) {
            dealerHand.add(deck.cards.get(0));
            deck.cards.remove(0);
        }
    }
    
    //Generates result of the game
    public static String result() {
        if(getCardTotal(dealerHand) > 21 || getCardTotal(dealerHand) < getCardTotal(playerHand)) {
            
            balance+=2*bet;
            return "Player win!";
        }else if(getCardTotal(dealerHand) == getCardTotal(playerHand)) {
            balance+=bet;
            return "Push!";
        }else {
            return "Dealer win!";

        }
    }
    /* To be implemented
    public static String playerSplitOption(BufferedReader reader) {
        System.out.println("Enter 'H' to hit 'S' to stand 'D' to double '2' to Split");
        String answer = "";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }*/
    //Returns players card value
    public static int getCardTotal(ArrayList<Card> hand) {
        int total = 0;
        for(int i = 0; i < hand.size(); i++) {
            total+=hand.get(i).val;
        }
        return total;
    }
    //returns the card total if has an Ace
    public static ArrayList<Card> getCardTotalAce(ArrayList<Card> hand) {
        int total = getCardTotal(hand);
        Card ace = new Card();
        if(containsAce(hand) !=-1) {
            if(total <= 11 ) {
                ace.val=11;
                ace.name = "A";
                hand.set(containsAce(hand), ace);
            }else if(total >21 && hand.get(containsAce(hand)).val == 11){
                ace.val=1;
                ace.name = "A";
                hand.set(containsAce(hand), ace);
            }
        }
        return hand;
    }
    
    //Displays the players cards and values
    public static String playerHand(ArrayList<Card> hand) {
        String cards = "";
        int value = getCardTotal(hand);
        hand = getCardTotalAce(hand);
        value = getCardTotal(hand);
        for(int i = 0; i < hand.size(); i++) {
            cards = cards + hand.get(i).name + " ";
        }
        cards = cards + " Value: " + value;
        return cards;
    }
  //Displays the players cards and values
    public static String displayPlayerHand(ArrayList<Card> hand) {
        return "Player cards: " + playerHand(hand);
    }
    public static String dealerHand(ArrayList<Card> hand, boolean playerTurn) {
        //String cards = "";
        if(!playerTurn) {
            return(hand.get(0).name + " Value: "+ hand.get(0).val);
        }else {
            return(playerHand(hand));
        }
    }
    //Displays the dealers cards and values
    public static String displayDealerHand(ArrayList<Card> hand, boolean playerTurn) {
        return "Dealer cards: " + dealerHand(hand, playerTurn);
    }
    //Checks if there is a BJ
    public static boolean checkBJ(ArrayList<Card> hand) {
        if(hand.get(0).name=="A"&&hand.get(1).val==10) {
            return true;
        }else if(hand.get(1).name=="A"&&hand.get(0).val==10) {
            return true;
        }else {
            return false;
        }
    }
    //Checks if the hand contains and ace and returns position of ace
    public static int containsAce(ArrayList<Card> hand) {
        for(int i =0; i < hand.size();i++) {
            if(hand.get(i).name == "A") {
                return i;
            }
        }
        return -1;
    }

}



