import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {

	private static BaccaratDealer dealer;
	private static BaccaratGameLogic logic;

	@BeforeEach
	void initialise()	{
		dealer = new BaccaratDealer();
		logic = new BaccaratGameLogic();
	}
	//DealerClass Tests
	@Test
	void testConstructor() {
		assertEquals("BaccaratDealer", dealer.getClass().getName(), "Incorrect Class Name");
	}


	@Test
	void generateDeck1()	{

		assertEquals(52, dealer.deckSize(), "Wrong Deck size");
	}
	@Test
	void generateDeck2()	{
		dealer.generateDeck();
		assertEquals(104, dealer.deckSize(), "Wrong Deck size");

	}
	@Test
	void dealhand1(){
		ArrayList<Card> deal = dealer.dealHand();
		assertEquals(2, deal.size());
	}
	@Test
	void dealhand2(){
		ArrayList<Card> deal = dealer.dealHand();
		assertEquals(50, dealer.deckSize());
	}
	@Test
	void drawOne1(){
		Card card = dealer.drawOne();
		assertNotNull(card, "Card doesn't exist");

	}
	@Test
	void drawOne2(){
		Card card = dealer.drawOne();
		assertEquals(51, dealer.deckSize());
	}
	@Test
	void testShuffleDeckPreservesCardCount() {

		int initialCount = dealer.deckSize();
		dealer.shuffleDeck();
		assertEquals(52, dealer.deckSize(), "Wrong");
	}
	@Test
	void shuffleDeck2() {

		ArrayList<Card> deal = dealer.dealHand();
		dealer.shuffleDeck();

		assertEquals(50, dealer.deckSize(), "Wrong Size");
	}
	@Test
	void deckSize1()	{

		int size = dealer.deckSize();

		assertEquals(52, size, "Wrong Deck size");
	}
	@Test
	void deckSize2()	{

		ArrayList<Card> deal = dealer.dealHand();
		int size = 52 - deal.size();
		assertEquals(50, size, "Wrong Deck size");
	}
	//GameLogic Tests
	@Test
	void whoWon1()	{
		Card card1 = new Card("Hearts", 11);
		Card card2 = new Card("Spades", 2);
		Card card3 = new Card("Clubs", 7);

		ArrayList<Card> hand1 = new ArrayList <>();
		hand1.add(card1);
		hand1.add(card2);
		hand1.add(card3);

		Card card4 = new Card("Diamonds", 5);
		Card card5 = new Card("Hearts", 11);
		Card card6 = new Card("Spades", 13);

		ArrayList<Card> hand2 = new ArrayList <>();
		hand2.add(card4);
		hand2.add(card5);
		hand2.add(card6);

		assertEquals("Player", logic.whoWon(hand1, hand2), "Incorrect Win");
	}
	@Test
	void whoWon2()	{
		Card card1 = new Card("Hearts", 2);
		Card card2 = new Card("Spades", 8);
		Card card3 = new Card("Clubs", 1);

		ArrayList<Card> hand1 = new ArrayList <>();
		hand1.add(card1);
		hand1.add(card2);
		hand1.add(card3);

		Card card4 = new Card("Diamonds", 2);
		Card card5 = new Card("Hearts", 5);
		//Card card6 = new Card("Spades", 4);

		ArrayList<Card> hand2 = new ArrayList <>();
		hand2.add(card4);
		hand2.add(card5);
		//hand2.add(card6);

		assertEquals("Banker", logic.whoWon(hand1, hand2), "Incorrect Win");
	}
	@Test
	void whoWon3()	{
		Card card1 = new Card("Hearts", 11);
		Card card2 = new Card("Spades", 11);
		Card card3 = new Card("Clubs", 9);

		ArrayList<Card> hand1 = new ArrayList <>();
		hand1.add(card1);
		hand1.add(card2);
		hand1.add(card3);

		Card card4 = new Card("Diamonds", 6);
		Card card5 = new Card("Hearts", 9);
		Card card6 = new Card("Spades", 4);

		ArrayList<Card> hand2 = new ArrayList <>();
		hand2.add(card4);
		hand2.add(card5);
		hand2.add(card6);

		assertEquals("Draw", logic.whoWon(hand1, hand2), "Incorrect Win");
	}
	@Test
	void handTotal1(){
		Card card1 = new Card("Hearts", 11);
		Card card2 = new Card("Spades", 13);
		Card card3 = new Card("Clubs", 10);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		hand.add(card2);
		hand.add(card3);

		assertEquals(0, logic.handTotal(hand), "Wrong Value");

	}
	@Test
	void handTotal2(){
		Card card1 = new Card("Hearts", 8);
		Card card2 = new Card("Spades", 9);
		Card card3 = new Card("Clubs", 1);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		hand.add(card2);
		hand.add(card3);

		assertEquals(8, logic.handTotal(hand), "Wrong Value");

	}
	@Test
	void evaluateBankerDraw1(){
		Card card1 = new Card("Hearts", 1);
		Card card2 = new Card("Spades", 2);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		hand.add(card2);

		assertTrue(logic.evaluateBankerDraw(hand, new Card("Hearts", 4)));
	}
	@Test
	void evaluateBankerDraw2(){
		Card card1 = new Card("Hearts", 3);
		Card card2 = new Card("Spades", 2);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		hand.add(card2);

		assertFalse(logic.evaluateBankerDraw(hand, new Card("Hearts", 3)));
	}
	@Test
	void evaluatePlayerDraw1(){
		Card card1 = new Card("Hearts", 3);
		Card card2 = new Card("Spades", 2);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		hand.add(card2);

		assertTrue(logic.evaluatePlayerDraw(hand));

	}
	@Test
	void evaluatePlayerDraw2(){
		Card card1 = new Card("Hearts", 3);
		Card card2 = new Card("Spades", 4);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		hand.add(card2);

		assertFalse(logic.evaluatePlayerDraw(hand));

	}




}
