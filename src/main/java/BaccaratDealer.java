import java.util.ArrayList;
import java.util.Collections;

public class BaccaratDealer {


    private ArrayList<Card> deck;

    //Constructor
    public BaccaratDealer(){
        deck = new ArrayList<Card>();
        generateDeck();
        shuffleDeck();
    }

    //Generates a deck of cards, 52 unique cards
    public void generateDeck(){
        for (int i = 0; i < 4; i++){
            String suite = "";
            switch(i){
                case 0:
                    suite = "Diamonds";
                case 1:
                    suite = "Clubs";
                case 2:
                    suite = "Hearts";
                case 3:
                    suite = "Spades";
            }
            for (int j = 1; j <= 13; j++){
                deck.add(new Card(suite, j));
            }
        }
    }

    //Deals two cards from the deck
    public ArrayList<Card> dealHand(){
        ArrayList<Card> deal = new ArrayList<>(3);
        deal.add(0, deck.remove(0));
        deal.add(1, deck.remove(0));

        return deal;
    }

    //Draws one card out of the deck
    public Card drawOne(){
        return deck.remove(0);
    }

    //Shuffles the deck to randomize the order
    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    //returns the deck size
    public int deckSize(){
        return deck.size();
    }
}