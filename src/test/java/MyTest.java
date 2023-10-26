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
	@Test
	void testConstructor() {
		assertEquals("BaccaratDealer", dealer.getClass().getName(), "Incorrect Class Name");
	}


	@Test
	void DealerGenerateDeck1()	{

		assertEquals(52, dealer.deckSize(), "Wrong Deck size");
	}
	@Test
	void DealerGenerateDeck2()	{
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
	void shuffleDeck1() {

		dealer.shuffleDeck();

		assertNotEquals(52, dealer.deckSize(), "The deck should be shuffled.");
	}
	@Test
	void shuffleDeck2() {
		// Get the initial count of cards in the deck
		int initialCount = dealer.deckSize();

		// Shuffle the deck
		dealer.shuffleDeck();

		// Verify that the count of cards in the shuffled deck remains the same
		assertEquals(initialCount, dealer.deckSize(), "Shuffling should preserve the card count.");
	}



}
