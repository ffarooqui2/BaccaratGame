import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.lang.Double;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class BaccaratGame extends Application {

	Font TITLE_FONT = Font.loadFont("file:src/fonts/Inter-Black.ttf", 200);
	Font HEADING_FONT = Font.loadFont("file:src/fonts/Inter-Medium.ttf", 30);

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
	private double playerTotal, bankerTotal;
	private Text playerTotalText, bankerTotalText;

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
			System.out.println("Current Bet: " + currentBet);
		});

		BorderPane bettingPageRoot = new BorderPane();
		bettingPageRoot.setCenter(bettingPageContent);
		bettingPageRoot.setBottom(continueButtonContent);

		// MAIN PLAYING FIELD ---------------------------------------

		BorderPane gamePageRoot = new BorderPane();

		// Menu Bar
		MenuBar menuBar = new MenuBar();
		Menu optionsMenu = new Menu("Options");
		MenuItem exitItem = new MenuItem("Exit");
		MenuItem freshStartItem = new MenuItem("Fresh Start");
		optionsMenu.getItems().addAll(exitItem, freshStartItem);
		menuBar.getMenus().add(optionsMenu);

		// handle menu
		exitItem.setOnAction(e -> Platform.exit());
		freshStartItem.setOnAction(e-> {
			totalWinnings = 0.0;
			roundResults.setText("");
			primaryStage.setScene(placeBetsScene);
		});
		// Set menu bar at the top
		gamePageRoot.setTop(menuBar);


		// Individual Card Containers
		VBox playersGameContent = new VBox(30);
		VBox bankersGameContent = new VBox(30);
		HBox playersCardContainer = new HBox(10);
		HBox bankersCardContainer = new HBox(10);

		roundResults = new Text("");
		roundResults.setFont(HEADING_FONT);

		// Created instances of game logic and dealer
		gameLogic = new BaccaratGameLogic();
		theDealer = new BaccaratDealer();

		// Deal two cards for the player and banker
		playerHand = theDealer.dealHand();
		bankerHand = theDealer.dealHand();

		Image playerCard1 = new Image(playerHand.get(0).getValue()+  "_of_" + playerHand.get(0).getSuite().toLowerCase() + ".png");
		Image playerCard2 = new Image(playerHand.get(1).getValue()+  "_of_" + playerHand.get(1).getSuite().toLowerCase() + ".png");
		Image bankerCard1 = new Image(bankerHand.get(0).getValue()+  "_of_" + bankerHand.get(0).getSuite().toLowerCase() + ".png");
		Image bankerCard2 = new Image(bankerHand.get(1).getValue()+  "_of_" + bankerHand.get(1).getSuite().toLowerCase() + ".png");

		// Create ImageView instances for each card and set their dimensions
		ImageView playerCard1ImageView = new ImageView(playerCard1);
		playerCard1ImageView.setFitWidth(100); // Set the desired width
		playerCard1ImageView.setFitHeight(150); // Set the desired height

		ImageView playerCard2ImageView = new ImageView(playerCard2);
		playerCard2ImageView.setFitWidth(100); // Set the desired width
		playerCard2ImageView.setFitHeight(150); // Set the desired height

		ImageView bankerCard1ImageView = new ImageView(bankerCard1);
		bankerCard1ImageView.setFitWidth(100); // Set the desired width
		bankerCard1ImageView.setFitHeight(150); // Set the desired height

		ImageView bankerCard2ImageView = new ImageView(bankerCard2);
		bankerCard2ImageView.setFitWidth(100); // Set the desired width
		bankerCard2ImageView.setFitHeight(150); // Set the desired height

		// Display player's cards
		playersCardContainer.getChildren().addAll(playerCard1ImageView, playerCard2ImageView);
		playersCardContainer.setAlignment(Pos.CENTER);

		// Display banker's cards
		bankersCardContainer.getChildren().addAll(bankerCard1ImageView, bankerCard2ImageView);
		bankersCardContainer.setAlignment(Pos.CENTER);

		// Assuming you have containers for displaying hand totals: playersGameContent and bankersGameContent

		// Calculate and display the player's hand total
		playerTotal = gameLogic.handTotal(playerHand);
		playerTotalText = new Text("Player Total: " + playerTotal);
		playerTotalText.setFont(HEADING_FONT);
		playersGameContent.getChildren().addAll(playerTotalText, playersCardContainer);
		playersGameContent.setAlignment(Pos.CENTER);

		// Calculate and display the banker's hand total
		bankerTotal = gameLogic.handTotal(bankerHand);
		bankerTotalText = new Text("Banker Total: " + bankerTotal);
		bankerTotalText.setFont(HEADING_FONT);
		bankersGameContent.getChildren().addAll(bankerTotalText, bankersCardContainer);
		bankersGameContent.setAlignment(Pos.CENTER);

		String winner;
		if (bankerTotal == 8 || bankerTotal == 9 || playerTotal == 8 || playerTotal == 9){
			// Natural Win
			winner = gameLogic.whoWon(playerHand, bankerHand);
			// Stop game
		}


		// DRAW CARD BUTTON
		Button drawCardButton = new Button("DRAW");
		HBox gameBottomMenu = new HBox(150);
		gameBottomMenu.setPadding(new Insets(20, 20, 20, 20));
		gameBottomMenu.getChildren().add(drawCardButton);


		// DRAW CARD ACTION
		drawCardButton.setOnAction(e -> {
			if (gameLogic.evaluatePlayerDraw(playerHand)) {

					Card drawnCard = theDealer.drawOne();
					playerHand.add(drawnCard);

					String cardImagePath = drawnCard.getValue() + "_of_" + drawnCard.getSuite().toLowerCase() + ".png";
					Image playerCardImage = new Image(cardImagePath);
					ImageView playerCardImageView = new ImageView(playerCardImage);
					playerCardImageView.setFitWidth(100);
					playerCardImageView.setFitHeight(150);

					playersCardContainer.getChildren().add(playerCardImageView);

					int playerTotal = gameLogic.handTotal(playerHand);
					playerTotalText.setText("Player Total: " + playerTotal);

					playersGameContent.getChildren().clear(); // Clear previous content
					playersGameContent.getChildren().addAll(playerTotalText, playersCardContainer);
					playersGameContent.setAlignment(Pos.CENTER);

					System.out.println("Winner: " + gameLogic.whoWon(playerHand, bankerHand));

					//drawCardButton.setDisable(true); // Disable the button after drawing one card
			}

			if (gameLogic.evaluateBankerDraw( bankerHand, playerHand.get(playerHand.size() - 1))) {

					Card drawnCard = theDealer.drawOne();
					bankerHand.add(drawnCard);

					String cardImagePath = drawnCard.getValue() + "_of_" + drawnCard.getSuite().toLowerCase() + ".png";
					Image bankerCardImage = new Image(cardImagePath);
					ImageView bankerCardImageView = new ImageView(bankerCardImage);
					bankerCardImageView.setFitWidth(100);
					bankerCardImageView.setFitHeight(150);

					bankersCardContainer.getChildren().add(bankerCardImageView);

					int bankerTotal = gameLogic.handTotal(bankerHand);
					bankerTotalText.setText("Banker Total: " + bankerTotal);

					bankersGameContent.getChildren().clear(); // Clear previous content
					bankersGameContent.getChildren().addAll(bankerTotalText, bankersCardContainer);
					bankersGameContent.setAlignment(Pos.CENTER);

					System.out.println("Winner: " + gameLogic.whoWon(playerHand, bankerHand));

					drawCardButton.setDisable(true); // Disable the button after drawing one card
			}
		});

		HBox mainGameContent = new HBox(500);
		mainGameContent.getChildren().addAll(playersGameContent, bankersGameContent);
		mainGameContent.setAlignment(Pos.CENTER);

		VBox resultsContainer = new VBox(10);
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


}
