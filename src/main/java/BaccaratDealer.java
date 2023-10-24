import java.util.ArrayList;

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
        ArrayList<Card> deal = new ArrayList<>(2);
        deal.set(0, deck.remove(0));
        deal.set(1, deck.remove(0));

        return deal;
    }

    public Card drawOne(){
        return deck.remove(0);
    }

    public void shuffleDeck(){
        for (int i = 0; i < deck.size(); i++){
            int index = (int) (Math.random() * deck.size());

            Card temp = deck.get(i);
            deck.set(i, deck.get(index));
            deck.set(index, temp);
        }
    }

    public int deckSize(){
        return deck.size();
    }
}
