import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BaccaratGame extends Application {
	Font TITLE_FONT = Font.loadFont("file:src/assets/Inter-Black.ttf", 200);
	Font HEADING_FONT = Font.loadFont("file:src/assets/Inter-Medium.ttf", 30);
	Font TEXT_FONT = Font.loadFont("file:src/assets/Inter-SemiBoldItalic.ttf", 15);
	Font RESULT_FONT = Font.loadFont("file:src/assets/Inter-ExtraBold.ttf", 35);
	int CARD_WIDTH = 100;
	int CARD_HEIGHT = 150;
	private ArrayList<Card> playerHand;
	private ArrayList<Card> bankerHand;
	private BaccaratDealer theDealer;
	private BaccaratGameLogic gameLogic;
	private double currentBet;
	private double totalWinnings;
	private ArrayList<Text> gameHistory;
	private TextField playerBet = new TextField("0.00");
	private TextField bankerBet = new TextField("0.00");
	private TextField tieBet = new TextField("0.00");
	private Scene titleScene, placeBetsScene, mainGameScene, historyPageScene;
	private Text roundResults;
	private Text results;
	private Text displayWinningBets;
	private Text playerTotalText, bankerTotalText;

	private String userChoice = "";
	private String outcome = "";
	private int bankerTotal, playerTotal;
	private int drawCardCounter = 0;

	// Determine if user won or lost their bet and return the amount won or lost based on the value in currentBet
	public double evaluateWinnings() {
		// Get the winner of the round
		String currentWinner = gameLogic.whoWon(playerHand, bankerHand);

		double playerBetAmount;
		double bankerBetAmount;
		double tieBetAmount;

		try {
			playerBetAmount = Double.parseDouble(playerBet.getText());
			playerBetAmount = Double.parseDouble(String.format("%.2f", playerBetAmount));
		} catch (NumberFormatException ex) {
			playerBetAmount = 0.0;
		}

		try {
			bankerBetAmount = Double.parseDouble(bankerBet.getText());
			bankerBetAmount = Double.parseDouble(String.format("%.2f", bankerBetAmount));
		} catch (NumberFormatException ex) {
			bankerBetAmount = 0.0;
		}

		try {
			tieBetAmount = Double.parseDouble(tieBet.getText());
			tieBetAmount = Double.parseDouble(String.format("%.2f", tieBetAmount));
		} catch (NumberFormatException ex) {
			tieBetAmount = 0.0;
		}


		double winnings = 0;
		double temp = 0;

		if (currentWinner.equals("Player")) {
			// Player won
			winnings = (playerBetAmount - bankerBetAmount - tieBetAmount);
			temp = winnings;
			if(temp > 0) outcome = " Congrats you Won the bet";
			else outcome = " Sorry you Lost the bet";
			temp = 0;
		} else if (currentWinner.equals("Banker")) {
			// Banker won
			winnings = (bankerBetAmount - tieBetAmount - playerBetAmount);
			temp = winnings;
			if(temp > 0) outcome = " Congrats you Won the bet";
			else outcome = " Sorry you Lost the bet";
			temp = 0;
		} else if (currentWinner.equals("Draw")) {
			// It's a tie
			winnings = (tieBetAmount - bankerBetAmount - playerBetAmount);
			temp = winnings;
			if(temp > 0) outcome = " Congrats you Won the bet";
			else outcome = " Sorry you Lost the bet";
			temp = 0;
		}
		return winnings;
	}

	public static void main(String[] args) {
		System.out.println("Running...");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Baccarat");

		// TITLE PAGE ---------------------------------------
		BorderPane titlePageRoot = new BorderPane();

		// Game title
		Text gameTitle = new Text("BACCARAT");
		gameTitle.setTextAlignment(TextAlignment.CENTER);
		Font gameTitleFont = TITLE_FONT;
		gameTitle.setFont(gameTitleFont);
		
		// Create a linear gradient for the text fill (casino greens)
		gameTitle.setStyle("-fx-fill: linear-gradient(to bottom, #00796B, #032f1d)");

		// Play Button
		Button playButton = getPlayButton();

		// Create a FadeTransition with a duration of 1 second.
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), titlePageRoot);
		fadeOut.setFromValue(1.0); // Starting opacity
		fadeOut.setToValue(0.0);   // Ending opacity
		fadeOut.setOnFinished(e -> {
			primaryStage.setScene(placeBetsScene); // Switch to Scene 2 when the transition is finished
			gameHistory = new ArrayList<>();
			totalWinnings = 0;
		});

		playButton.setOnAction(e -> { fadeOut.play(); });

		// Main Content Container
		VBox titlePageContent = new VBox(50);
		titlePageContent.setAlignment(Pos.CENTER);
		titlePageContent.getChildren().addAll(gameTitle, playButton);
		titlePageRoot.setCenter(titlePageContent);

		// PLACING BETS PAGE ---------------------------------------

		roundResults = new Text("");
		roundResults.setFont(RESULT_FONT);
		roundResults.setFill(Color.WHITE);

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
			try { currentBet = Double.parseDouble(playerBet.getText());
				userChoice = "Player";	}
			catch (NumberFormatException ex){currentBet = 0.0;}
		});
		playerBet.setPrefWidth(200);
		bankerBet.setOnKeyPressed(e -> {
			playerBet.clear();
			tieBet.clear();
			try { currentBet = Double.parseDouble(bankerBet.getText());
				userChoice = "Banker";}
			catch (NumberFormatException ex){ currentBet = 0.0; }
		});
		bankerBet.setPrefWidth(200);
		tieBet.setOnKeyPressed(e -> {
			playerBet.clear();
			bankerBet.clear();
			try { currentBet = Double.parseDouble(tieBet.getText());
				userChoice = "Draw";}
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

		// Move to game after placing bets
		continueButton.setOnAction(e -> {
			primaryStage.setScene(mainGameScene);
			roundResults.setText("");
		});

		BorderPane bettingPageRoot = new BorderPane();
		bettingPageRoot.setCenter(bettingPageContent);
		bettingPageRoot.setBottom(continueButtonContent);

		// MAIN PLAYING FIELD ---------------------------------------

		BorderPane gamePageRoot = new BorderPane();

		displayWinningBets = new Text("Total Winnings: ");
		displayWinningBets.setFont(TEXT_FONT);

		// Storing the cards and hand total respectively
		VBox playersGameContent = new VBox(30);
		VBox bankersGameContent = new VBox(30);

		// Individual Card Containers
		HBox playersCardContainer = new HBox(10);
		HBox bankersCardContainer = new HBox(10);

		// Holds Current Winnings
		HBox currentWinningsContainer = new HBox(5);
		currentWinningsContainer.setAlignment(Pos.CENTER);

		// CurrentWinnings Box
		TextField currentWinningDisplay = new TextField();
		currentWinningDisplay.setText(Double.toString(totalWinnings));
		currentWinningsContainer.getChildren().addAll(displayWinningBets, currentWinningDisplay);

		// Created instances of game logic and dealer
		gameLogic = new BaccaratGameLogic();
		theDealer = new BaccaratDealer();

		// Menu Bar
		MenuBar menuBar = new MenuBar();
		Menu optionsMenu = new Menu("Options");
		MenuItem exitItem = new MenuItem("Exit");
		MenuItem freshStartItem = new MenuItem("Fresh Start");
		optionsMenu.getItems().addAll(exitItem, freshStartItem);
		menuBar.getMenus().add(optionsMenu);

		// Draw Card Button
		Button drawCardButton = getHelperButton("DRAW CARD");
		Font drawCardFont = TEXT_FONT;
		drawCardButton.setFont(drawCardFont);

		// Play Button - resets the cards and the result screen but also stores the result into history
		Button startNewRoundButton = getHelperButton("PLAY AGAIN");

		// Holds the results
		VBox resultsContent = new VBox(50);
		Text resultsLabel = new Text("RESULTS");
		resultsLabel.setFont(HEADING_FONT);

		// Create the Rectangle for result display
		StackPane resultBox = new StackPane();

		// Create Style for resultBox
		resultBox.setPrefSize(300, 200);
		resultBox.setStyle(
				"-fx-background-color: linear-gradient(to bottom, #00796B, #005542), transparent;" +
						"-fx-background-radius: 10;" +
						"-fx-border-color: linear-gradient(to bottom, #004D40, #00231c);" +
						"-fx-border-width: 2;" +
						"-fx-border-radius: 10;" +
						"-fx-padding: 10;"
		);

		resultBox.getChildren().addAll(roundResults);

		// Menu Actions
		exitItem.setOnAction(e -> Platform.exit());
		freshStartItem.setOnAction(e-> {
			totalWinnings = 0.0;
			currentWinningDisplay.setText("");
			roundResults.setText("");
			startNewRoundButton.setDisable(true);
			drawCardButton.setDisable(false);
			gameHistory.clear();

			// remove temporary game elements
			playerHand.clear();
			bankerHand.clear();
			playersGameContent.getChildren().clear();
			bankersGameContent.getChildren().clear();
			playersCardContainer.getChildren().clear();
			bankersCardContainer.getChildren().clear();

			// reset results Text of the round
			roundResults.setText("");

			resultBox.getChildren().clear();
			resultBox.getChildren().addAll(roundResults);

			resultsContent.getChildren().clear();
			resultsContent.getChildren().addAll(resultsLabel, resultBox);

			// Reset the player and banker totals;
			playerTotal = 0;
			bankerTotal = 0;
			playerTotalText.setText("Player Total: " + playerTotal);
			bankerTotalText.setText("Player Total: " + bankerTotal);

			drawCardCounter = 0;
			// return to betting scene to let user place new bets
			primaryStage.setScene(placeBetsScene);
		});

		// Set menu bar at the top
		gamePageRoot.setTop(menuBar);

		// contains the results, cards, and totals
		HBox mainGameContent = new HBox(200);

		resultsContent.getChildren().clear();
		resultsContent.getChildren().addAll(resultsLabel, resultBox);

		startNewRoundButton.setOnAction(e -> {
			// remove temporary game elements
			playerHand.clear();
			bankerHand.clear();
			playersCardContainer.getChildren().clear();
			bankersCardContainer.getChildren().clear();

			// reset results Text of the round
			roundResults.setText("");

			resultBox.getChildren().clear();
			resultBox.getChildren().addAll(roundResults);

			resultsContent.getChildren().clear();
			resultsContent.getChildren().addAll(resultsLabel, resultBox);

			// Reset the player and banker totals;
			playerTotal = 0;
			bankerTotal = 0;
			playerTotalText.setText("Player Total: " + playerTotal);
			bankerTotalText.setText("Player Total: " + bankerTotal);

			drawCardCounter = 0;
			drawCardButton.setDisable(false);
			startNewRoundButton.setDisable(true);
			// return to betting scene to let user place new bets
			primaryStage.setScene(placeBetsScene);
		});

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

				bankersCardContainer.getChildren().addAll(bankerCard1ImageView, bankerCard2ImageView);

				// Calculating the total for banker
				bankerTotal = gameLogic.handTotal(bankerHand);
				bankerTotalText = new Text ("Banker Total: " + bankerTotal);
				bankerTotalText.setFont(HEADING_FONT);

				bankersGameContent.getChildren().clear(); // Clear previous content
				bankersGameContent.getChildren().addAll(bankerTotalText, bankersCardContainer);
				bankersGameContent.setAlignment(Pos.CENTER);

				drawCardCounter += 4;

				// Check for Natural Win
				if (bankerTotal == 8 || bankerTotal == 9 || playerTotal == 8 || playerTotal == 9){
					// Evaluate the winner and display the result
					String winner = gameLogic.whoWon(playerHand, bankerHand);
					roundResults.setText("Winner: " + winner);
					totalWinnings += evaluateWinnings();
					results = new Text();
					results.setText("Player Total: " + playerTotal + " Banker Total: " + bankerTotal + ", " + userChoice + outcome + ", Your total winning are " + totalWinnings);
					results.setFont(TEXT_FONT);
					gameHistory.add(results);

					resultBox.getChildren().clear();
					resultBox.getChildren().addAll(roundResults);

					resultsContent.getChildren().clear();
					resultsContent.getChildren().addAll(resultsLabel, resultBox);

					currentWinningDisplay.setText(Double.toString(totalWinnings));

					// disable button
					drawCardButton.setDisable(true);
					// enable play again button
					startNewRoundButton.setDisable(false);
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
				results = new Text();
				results.setText("Player Total: " + playerTotal + " Banker Total: " + bankerTotal + ", " + userChoice + outcome + ", Your total winning are " + totalWinnings);
				results.setFont(TEXT_FONT);
				gameHistory.add(results);

				// update total winnings
				totalWinnings += evaluateWinnings();

				// reset the labels after game ends
				resultBox.getChildren().clear();
				resultBox.getChildren().addAll(roundResults);

				resultsContent.getChildren().clear();
				resultsContent.getChildren().addAll(resultsLabel, resultBox);

				currentWinningDisplay.setText(Double.toString(totalWinnings));

				// disable button
				drawCardButton.setDisable(true);
				// enable play again button
				startNewRoundButton.setDisable(false);
			}
		});

		// to store history data structures
		VBox historyContent = new VBox(10);

		Button historyButton = getHelperButton("HISTORY");
		historyButton.setOnAction(e->{
			// Creating a ListView to display the game history
			ListView<Text> historyListView = new ListView<>();

			List<Text> gameResults = gameHistory;

			historyListView.getItems().clear();
			historyListView.getItems().addAll(gameResults);
			historyContent.getChildren().clear();
			historyContent.getChildren().add(historyListView);
			primaryStage.setScene(historyPageScene);

		});

		HBox gameBottomMenu = new HBox(150);
		gameBottomMenu.setPadding(new Insets(20, 20, 20, 20));
		gameBottomMenu.getChildren().addAll(historyButton, drawCardButton, startNewRoundButton, currentWinningsContainer);

		resultsContent.setAlignment(Pos.CENTER);
		resultsContent.getChildren().clear();
		resultsContent.getChildren().addAll(resultsLabel, resultBox);

		mainGameContent.getChildren().addAll(playersGameContent, resultsContent, bankersGameContent);
		mainGameContent.setAlignment(Pos.CENTER);

		gamePageRoot.setCenter(mainGameContent);
		gamePageRoot.setBottom(gameBottomMenu);


		// HISTORY PAGE ---------------------------------------
		BorderPane historyPageRoot = new BorderPane();
		VBox historyContainer = new VBox(10);
		historyContainer.setAlignment(Pos.CENTER);
		Text historyLabel = new Text("HISTORY");
		historyLabel.setFont(HEADING_FONT);
		historyPageRoot.setPadding(new Insets (20, 20, 20, 20));

		Button backToMainGameButton = getHelperButton("BACK");
		backToMainGameButton.setOnAction(e -> primaryStage.setScene(mainGameScene));

		historyContainer.getChildren().addAll(historyLabel, historyContent);
		historyPageRoot.setCenter(historyContainer);
		historyPageRoot.setBottom(backToMainGameButton);

		// put root on the scene which goes on to the stage
		titleScene = new Scene(titlePageRoot, 1600, 900);

		placeBetsScene = new Scene(bettingPageRoot, 1600, 900);
		placeBetsScene.getRoot().setStyle("-fx-background-color: #649068;");

		mainGameScene = new Scene(gamePageRoot, 1600, 900);
		mainGameScene.getRoot().setStyle("-fx-background-color: #649068;");

		historyPageScene = new Scene(historyPageRoot, 1600, 900);
		historyPageScene.getRoot().setStyle("-fx-background-color: #649068;");

		primaryStage.setScene(titleScene);
		primaryStage.show();
	}


	// Seprate functions for creating buttons to increase readability
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
		continueButtonTitle.setFont(HEADING_FONT);
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
		buttonTitle.setFont(TEXT_FONT);
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
