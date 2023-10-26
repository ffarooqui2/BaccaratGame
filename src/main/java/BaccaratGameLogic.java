
import java.util.ArrayList;
public class BaccaratGameLogic {
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
    public int handTotal(ArrayList<Card> hand){
        int total = 0;
        int val = 0;

        for (Card card : hand) {
            val = card.getValue();
            if (val< 10)
                total += val;
        }

        return total % 10;
    }
    public boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard){
        int playerTotal = playerCard.getValue();
        int bankerTotal = handTotal(hand);

        if (bankerTotal < 3){
            return true;
        }
        else if(bankerTotal == 3 && playerTotal != 8){
            return true;
        }
        else if(bankerTotal == 4 && (playerTotal >= 2 && playerTotal <= 7)){
            return true;
        }
        else if(bankerTotal == 5 && (playerTotal >= 4 && playerTotal <= 7)){
            return true;
        }
        else return bankerTotal == 6 && (playerTotal == 6 || playerTotal == 7);

    }
    public boolean evaluatePlayerDraw(ArrayList<Card> hand){
        int playerTotal = handTotal(hand);
        return playerTotal < 6;
    }


}
