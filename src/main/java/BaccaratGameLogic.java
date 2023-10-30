import java.util.ArrayList;
public class BaccaratGameLogic {

    //Returns the total value of the cards
    public int handTotal(ArrayList<Card> hand){
        int total = 0;
        int val = 0;

        for (Card card : hand) {
            val = card.getValue();
            if (val< 10) total += val;
        }

        return total % 10;
    }

    // Determines who won the game based on the handTotal
    public String whoWon(ArrayList<Card> hand1, ArrayList<Card> hand2){

        int playerHand = handTotal(hand1);
        int bankerHand = handTotal(hand2);

        if(playerHand == bankerHand){
            return "Draw";
        }
        else if(playerHand > bankerHand){
            return "Player";
        }
        else{
            return "Banker";
        }
    }

    // Determines when the banker can draw a card which is dependent on the player
    public boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard){
        int playerTotal = playerCard.getValue();
        int bankerTotal = handTotal(hand);

        if(hand.size() == 3) return false;

        if (bankerTotal < 3) return true;

        else if(bankerTotal == 3 && playerTotal != 8) return true;

        else if(bankerTotal == 4 && (playerTotal >= 2 && playerTotal <= 7)) return true;

        else if(bankerTotal == 5 && (playerTotal >= 4 && playerTotal <= 7)) return true;

        else return bankerTotal == 6 && (playerTotal == 6 || playerTotal == 7);
    }

    //Determines whether the player can draw an extra card
    public boolean evaluatePlayerDraw(ArrayList<Card> hand){
        int playerTotal = handTotal(hand);
        return playerTotal < 6 && hand.size() != 3;
    }
}
