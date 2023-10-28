import java.util.ArrayList;
import java.util.Collections;

public class BaccaratDealer {


    private ArrayList<Card> deck;

    public BaccaratDealer(){
        deck = new ArrayList<Card>();
        generateDeck();
        shuffleDeck();
    }

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

    public ArrayList<Card> dealHand(){
        ArrayList<Card> deal = new ArrayList<>(3);
        deal.add(0, deck.remove(0));
        deal.add(1, deck.remove(0));

        return deal;
    }

    public Card drawOne(){
        return deck.remove(0);
    }

    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    public int deckSize(){
        return deck.size();
    }
}
