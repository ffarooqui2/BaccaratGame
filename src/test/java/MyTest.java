import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
	void DealerGenerateDeck1()	{
		//dealer.generateDeck();
		assertEquals(52, dealer.deckSize(), "Wrong Deck size");
	}

}
