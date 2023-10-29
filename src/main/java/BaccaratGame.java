import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class BaccaratGame extends Application {

	Font TITLE_FONT = Font.loadFont("file:src/assets/Inter-Black.ttf", 200);
	Font HEADING_FONT = Font.loadFont("file:src/assets/Inter-Medium.ttf", 30);
	Font TEXT_FONT = Font.loadFont("file:src/assets/Inter-SemiBoldItalic.ttf", 10);

	int CARD_WIDTH = 100;
	int CARD_HEIGHT = 150;

	private ArrayList<Card> playerHand;
	private ArrayList<Card> bankerHand;
	private BaccaratDealer theDealer;
	private BaccaratGameLogic gameLogic;
	private double currentBet;
	private double totalWinnings;

	// Input Boxes for Player or Banker
	private TextField playerBet = new TextField("0.00");
	private TextField bankerBet = new TextField("0.00");
	private TextField tieBet = new TextField("0.00");
	private Scene titleScene, placeBetsScene, mainGameScene;
	private Text roundResults;
	private Text playerTotalText, bankerTotalText;
	private int bankerTotal, playerTotal;
	private int drawCardCounter = 0;

	// Determine if user won or lost their bet and return the amount won or lost based on the value in currentBet
	public double evaluateWinnings(){

		String currentWinner = gameLogic.whoWon(playerHand, bankerHand);

		double playerBetAmount = Double.parseDouble(playerBet.getText());
		double bankerBetAmount = Double.parseDouble(bankerBet.getText());
		double tieBetAmount = Double.parseDouble(tieBet.getText());

        switch (currentWinner) {
            case "Player":
                // Player won
                return playerBetAmount;
            case "Banker":
                // Banker won
				return bankerBetAmount;
            case "Draw":
                // It's a tie
				return tieBetAmount;
        }
		return 0;
	}

	// totalWinning += evaluateWinnings()

	public static void main(String[] args) {
		System.out.println("Running...");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Baccarat");

		// TITLE PAGE ---------------------------------------
		BorderPane titlePageRoot = new BorderPane();

		// game title
		Text gameTitle = new Text("BACCARAT");
		gameTitle.setTextAlignment(TextAlignment.CENTER);
		Font gameTitleFont = TITLE_FONT;
		gameTitle.setFont(gameTitleFont);

		// play button
		Button playButton = getPlayButton();

		// Create a FadeTransition with a duration of 1 second.
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), titlePageRoot);
		fadeOut.setFromValue(1.0); // Starting opacity
		fadeOut.setToValue(0.0);   // Ending opacity
		fadeOut.setOnFinished(e -> primaryStage.setScene(placeBetsScene)); // Switch to Scene 2 when the transition is finished

		playButton.setOnAction(e -> { fadeOut.play(); });

		// Main Content Container
		VBox titlePageContent = new VBox(50);
		titlePageContent.setAlignment(Pos.CENTER);
		titlePageContent.getChildren().addAll(gameTitle, playButton);
		titlePageRoot.setCenter(titlePageContent);

		// PLACING BETS PAGE ---------------------------------------

		// Labels for betting boxes
		Text playerTitle = new Text("PLAYER");
		playerTitle.setFont(HEADING_FONT);
		Text bankerTitle = new Text("BANKER");
		bankerTitle.setFont(HEADING_FONT);
		Text tieTitle = new Text("TIE");
		tieTitle.setFont(HEADING_FONT);

		// The following code makes it so that the user can only bet on 1 of 3 outcomes
		playerBet.setOnKeyPressed(e -> {
			bankerBet.clear();
			tieBet.clear();
			try { currentBet = Double.parseDouble(playerBet.getText());}
			catch (NumberFormatException ex){currentBet = 0.0;}
		});
		playerBet.setPrefWidth(200);
		bankerBet.setOnKeyPressed(e -> {
			playerBet.clear();
			tieBet.clear();
			try { currentBet = Double.parseDouble(bankerBet.getText());}
			catch (NumberFormatException ex){ currentBet = 0.0; }
		});
		bankerBet.setPrefWidth(200);
		tieBet.setOnKeyPressed(e -> {
			playerBet.clear();
			bankerBet.clear();
			try { currentBet = Double.parseDouble(tieBet.getText());}
			catch (NumberFormatException ex){currentBet = 0.0;}
		});
		tieBet.setPrefWidth(200);

		// Individual Sections for respective titles and betting inputs
		VBox playerInputContent = new VBox(20);
		VBox tieInputContent = new VBox(20);
		VBox bankerInputContent = new VBox(20);

		playerInputContent.getChildren().addAll(playerTitle, playerBet);
		playerInputContent.setAlignment(Pos.CENTER);
		bankerInputContent.getChildren().addAll(bankerTitle, bankerBet);
		bankerInputContent.setAlignment(Pos.CENTER);
		tieInputContent.getChildren().addAll(tieTitle, tieBet);
		tieInputContent.setAlignment(Pos.CENTER);

		// Put Input Boxes in Container
		HBox bettingPageContent = new HBox(200);
		bettingPageContent.getChildren().addAll(playerInputContent, tieInputContent, bankerInputContent);
		bettingPageContent.setAlignment(Pos.CENTER);

		// Button to continue to card table
		Button continueButton = getContinueButton();

		// Container for Button
		HBox continueButtonContent = new HBox(20);
		continueButtonContent.setPadding(new Insets(20, 20, 20, 20));
		continueButtonContent.getChildren().add(continueButton);
		continueButtonContent.setAlignment(Pos.CENTER);

		continueButton.setOnAction(e -> {
			primaryStage.setScene(mainGameScene);
			System.out.println("Current Bet: " + currentBet); // comment out later
		});

		BorderPane bettingPageRoot = new BorderPane();
		bettingPageRoot.setCenter(bettingPageContent);
		bettingPageRoot.setBottom(continueButtonContent);

		// MAIN PLAYING FIELD ---------------------------------------

		BorderPane gamePageRoot = new BorderPane();

		// Created instances of game logic and dealer
		gameLogic = new BaccaratGameLogic();
		theDealer = new BaccaratDealer();

		// Create results Text of the round
		roundResults = new Text("");
		roundResults.setFont(HEADING_FONT);

		// Menu Bar
		MenuBar menuBar = new MenuBar();
		Menu optionsMenu = new Menu("Options");
		MenuItem exitItem = new MenuItem("Exit");
		MenuItem freshStartItem = new MenuItem("Fresh Start");
		optionsMenu.getItems().addAll(exitItem, freshStartItem);
		menuBar.getMenus().add(optionsMenu);

		// Menu Actions
		exitItem.setOnAction(e -> Platform.exit());
		freshStartItem.setOnAction(e-> {
			totalWinnings = 0.0;
			roundResults.setText("");
			primaryStage.setScene(placeBetsScene);
		});
		// Set menu bar at the top
		gamePageRoot.setTop(menuBar);

		// contains the results, cards, and totals
		HBox mainGameContent = new HBox(200);
		// holds the results
		VBox resultsContainer = new VBox(50);
		// Storing the cards and hand total respectively
		VBox playersGameContent = new VBox(30);
		VBox bankersGameContent = new VBox(30);
		// Individual Card Containers
		HBox playersCardContainer = new HBox(10);
		HBox bankersCardContainer = new HBox(10);
		// CurrentWinnings Box
		TextField currentWinningDisplay = new TextField(Double.toString(totalWinnings));

		// Play Button - resets the cards and the result screen but also stores the result into history
		Button startNewRound = getHelperButton("DEAL");
		startNewRound.setOnAction(e -> {
			// remove temporary game elements
			playerHand.clear();
			bankerHand.clear();
			playersCardContainer.getChildren().clear();
			bankersCardContainer.getChildren().clear();

			// return to betting scene to let user place new bets
			primaryStage.setScene(placeBetsScene);
		});

		// Draw Card Button
		Button drawCardButton = getHelperButton("DRAW");
		Font drawCardFont = TEXT_FONT;
		drawCardButton.setFont(drawCardFont);


		drawCardButton.setOnAction(e -> {

			if (drawCardCounter < 4) {
				// Draw two for player
				playerHand = theDealer.dealHand();
				Image playerCard1 = new Image(playerHand.get(0).getValue()+  "_of_" + playerHand.get(0).getSuite().toLowerCase() + ".png");
				Image playerCard2 = new Image(playerHand.get(1).getValue()+  "_of_" + playerHand.get(1).getSuite().toLowerCase() + ".png");

				// Create ImageView instances for each card and set their dimensions
				ImageView playerCard1ImageView = new ImageView(playerCard1);
				playerCard1ImageView.setFitWidth(CARD_WIDTH); // Set the desired width
				playerCard1ImageView.setFitHeight(CARD_HEIGHT); // Set the desired height

				ImageView playerCard2ImageView = new ImageView(playerCard2);
				playerCard2ImageView.setFitWidth(CARD_WIDTH); // Set the desired width
				playerCard2ImageView.setFitHeight(CARD_HEIGHT); // Set the desired height

				playersCardContainer.getChildren().addAll(playerCard1ImageView, playerCard2ImageView);

				playerTotal = gameLogic.handTotal(playerHand);
				playerTotalText = new Text("Player Total: " + playerTotal);
				playerTotalText.setFont(HEADING_FONT);

				playersGameContent.getChildren().clear();
				playersGameContent.getChildren().addAll(playerTotalText, playersCardContainer);
				playersGameContent.setAlignment(Pos.CENTER);

				// Draw two for banker
				bankerHand = theDealer.dealHand();

				Image bankerCard1 = new Image(bankerHand.get(0).getValue()+  "_of_" + bankerHand.get(0).getSuite().toLowerCase() + ".png");
				Image bankerCard2 = new Image(bankerHand.get(1).getValue()+  "_of_" + bankerHand.get(1).getSuite().toLowerCase() + ".png");

				ImageView bankerCard1ImageView = new ImageView(bankerCard1);
				bankerCard1ImageView.setFitWidth(CARD_WIDTH); // Set the desired width
				bankerCard1ImageView.setFitHeight(CARD_HEIGHT); // Set the desired height

				ImageView bankerCard2ImageView = new ImageView(bankerCard2);
				bankerCard2ImageView.setFitWidth(CARD_WIDTH); // Set the desired width
				bankerCard2ImageView.setFitHeight(CARD_HEIGHT); // Set the desired height

				// Calculating the total for banker
				bankerTotal = gameLogic.handTotal(playerHand);
				bankerTotalText.setText("Player Total: " + bankerTotal);
				bankerTotalText.setFont(HEADING_FONT);

				bankersGameContent.getChildren().clear(); // Clear previous content
				bankersGameContent.getChildren().addAll(playerTotalText, playersCardContainer);
				bankersGameContent.setAlignment(Pos.CENTER);

				drawCardCounter += 4;



				// Natural Win
				if (bankerTotal == 8 || bankerTotal == 9 || playerTotal == 8 || playerTotal == 9){
					// disable button
					drawCardButton.setDisable((true));

					// Evaluate the winner and display the result
					String winner = gameLogic.whoWon(playerHand, bankerHand);
					roundResults.setText("Winner: " + winner);
					totalWinnings += evaluateWinnings();
					// end game
					// enable play again button
					startNewRound.setDisable(false);

				}
			}

			else {
				// Check who can draw next, player goes first
				if (gameLogic.evaluatePlayerDraw(playerHand)){

					Card cardDrawn = theDealer.drawOne();
					playerHand.add(cardDrawn);

					String cardImagePath = cardDrawn.getValue() + "_of_" + cardDrawn.getSuite().toLowerCase() + ".png";
					Image playerCardImage = new Image(cardImagePath);
					ImageView playerCardImageView = new ImageView(playerCardImage);
					playerCardImageView.setFitHeight(CARD_HEIGHT);
					playerCardImageView.setFitWidth(CARD_WIDTH);

					playersCardContainer.getChildren().add(playerCardImageView);

					int playerTotal = gameLogic.handTotal(playerHand);
					playerTotalText.setText("Player Total: " + playerTotal);

					playersGameContent.getChildren().clear();
					playersGameContent.getChildren().addAll(playerTotalText, playersCardContainer);
					playersGameContent.setAlignment(Pos.CENTER);


				}
				if (gameLogic.evaluateBankerDraw(bankerHand, playerHand.get(playerHand.size() - 1))){
					Card drawnCard = theDealer.drawOne();
					bankerHand.add(drawnCard);

					String cardImagePath = drawnCard.getValue() + "_of_" + drawnCard.getSuite().toLowerCase() + ".png";
					Image bankerCardImage = new Image(cardImagePath);
					ImageView bankerCardImageView = new ImageView(bankerCardImage);
					bankerCardImageView.setFitWidth(CARD_WIDTH);
					bankerCardImageView.setFitHeight(CARD_HEIGHT);

					bankersCardContainer.getChildren().add(bankerCardImageView);

					int bankerTotal = gameLogic.handTotal(bankerHand);
					bankerTotalText.setText("Banker Total: " + bankerTotal);

					bankersGameContent.getChildren().clear(); // Clear previous content
					bankersGameContent.getChildren().addAll(bankerTotalText, bankersCardContainer);
					bankersGameContent.setAlignment(Pos.CENTER);

				}


				// Game over should now be over since all draws are exercised, display final result
				String winner = gameLogic.whoWon(playerHand, bankerHand);
				roundResults.setText("Winner: " + winner);
				totalWinnings += evaluateWinnings();

				// disable button
				drawCardButton.setDisable((true));
				startNewRound.setDisable(false);

			}
		});

		HBox gameBottomMenu = new HBox(150);
		gameBottomMenu.setPadding(new Insets(20, 20, 20, 20));
		gameBottomMenu.getChildren().addAll(drawCardButton, startNewRound, currentWinningDisplay);

		mainGameContent.getChildren().addAll(playersGameContent, resultsContainer, bankersGameContent);
		mainGameContent.setAlignment(Pos.CENTER);

		resultsContainer.setAlignment(Pos.CENTER);
		resultsContainer.getChildren().addAll(roundResults);

		gamePageRoot.setCenter(mainGameContent);
		gamePageRoot.setBottom(gameBottomMenu);
		gamePageRoot.setLeft(resultsContainer);

		// put root on the scene which goes on to the stage
		titleScene = new Scene(titlePageRoot, 1280,720);
		placeBetsScene = new Scene(bettingPageRoot, 1280, 720);
		mainGameScene = new Scene(gamePageRoot, 1280, 720);

		primaryStage.setScene(titleScene);
		primaryStage.show();
	}

	private Button getPlayButton(){
		Button playButton = new Button();
		Text playButtonTitle = new Text("PLAY");
		playButtonTitle.setFont(HEADING_FONT);
		playButton.setGraphic(playButtonTitle);
		playButton.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
				"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
		playButton.setPadding(new Insets(10, 20, 10, 20));
		playButton.setPrefSize(200, 50);

		// Button Hovering Animation
		playButton.setOnMouseEntered(e -> {
			playButton.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
					"-fx-border-radius: 20px; " +
					"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0); " +
					"-fx-border-color: #f83838; -fx-border-width: 3px;");
		});
		playButton.setOnMouseExited(e -> {
			playButton.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
					"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
		});

		return playButton;
	}
	private Button getContinueButton() {
		Button continueButton = new Button();
		Text continueButtonTitle = new Text("Continue");
		continueButton.setFont(HEADING_FONT);
		continueButton.setGraphic(continueButtonTitle);
		continueButton.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
				"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
		continueButton.setPadding(new Insets(10, 20, 10, 20));
		continueButton.setPrefSize(200, 50);

		// Hovering over continue button
		continueButton.setOnMouseEntered(e -> {
			continueButton.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
					"-fx-border-radius: 20px; " +
					"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0); " +
					"-fx-border-color: #497dfc; -fx-border-width: 3px;");
		});

		continueButton.setOnMouseExited(e -> {
			continueButton.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
					"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
		});
		return continueButton;
	}
	private Button getHelperButton(String label){
		Button button = new Button();
		Text buttonTitle = new Text(label);
		button.setFont(TEXT_FONT);
		button.setGraphic(buttonTitle);
		button.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
				"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
		button.setPadding(new Insets(10, 20, 10, 20));
		button.setPrefSize(100, 25);

		// Hovering over continue button
		button.setOnMouseEntered(e -> {
			button.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
					"-fx-border-radius: 20px; " +
					"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0); " +
					"-fx-border-color: #005e07; -fx-border-width: 3px;");
		});

		button.setOnMouseExited(e -> {
			button.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
					"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
		});

		return button;
	}


}
