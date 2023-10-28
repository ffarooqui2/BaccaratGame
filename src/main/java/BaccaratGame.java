import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

	Scene scene1, scene2, scene3;

	private double playerTotal, bankerTotal;

	private Text playerTotalText, bankerTotalText;

	public double evaluateWinnings(){
		return 0;
	}


	public static void main(String[] args) {
		System.out.println("Hello World");
		BaccaratDealer dealer = new BaccaratDealer();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Baccarat");


		// The scene root where content will be played out on
		BorderPane root1 = new BorderPane();

		// Title for opening page
		Text title = new Text("BACCARAT");
		title.setTextAlignment(TextAlignment.CENTER);
		Font titleFont = Font.loadFont("file:src/fonts/Inter-Black.ttf", 200);
		title.setFont(titleFont);

		// Creating button for play
		Button playButton = new Button();
		Text playButtonTitle = new Text("PLAY");
		playButtonTitle.setFont(Font.loadFont("file:src/fonts/Inter-Medium.ttf", 20));
		playButton.setGraphic(playButtonTitle);
		playButton.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
							"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
		playButton.setPadding(new Insets(10, 20, 10, 20));
		playButton.setPrefSize(200, 50);

		// Create a FadeTransition with a duration of 1 second (adjust the duration as needed)
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), root1);
		fadeOut.setFromValue(1.0); // Starting opacity
		fadeOut.setToValue(0.0);   // Ending opacity
		fadeOut.setOnFinished(e -> primaryStage.setScene(scene2)); // Switch to Scene 2 when the transition is finished

		playButton.setOnAction(e -> {
			fadeOut.play();
		});

		// Hovering over play button
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


		// Store the button and title in a vertical box (to be stacked on)
		VBox homePageContent = new VBox(50);
		homePageContent.setAlignment(Pos.CENTER);
		homePageContent.getChildren().addAll(title, playButton);

		root1.setCenter(homePageContent);

		// Placing Bets Page ------------
		Text playerTitle = new Text("PLAYER");
		playerTitle.setFont(Font.loadFont("file:src/fonts/Inter-Medium.ttf", 20));
		Text bankerTitle = new Text("BANKER");
		bankerTitle.setFont(Font.loadFont("file:src/fonts/Inter-Medium.ttf", 20));
		Text tieTitle = new Text("TIE");
		tieTitle.setFont(Font.loadFont("file:src/fonts/Inter-Medium.ttf", 20));

		playerBet.setOnKeyPressed(e -> {
			bankerBet.clear();
			tieBet.clear();
			try {
				currentBet = Double.parseDouble(playerBet.getText());
			} catch (NumberFormatException ex){
				currentBet = 0.0;
			}
		});
		playerBet.setPrefWidth(200);

		bankerBet.setOnKeyPressed(e -> {
			playerBet.clear();
			tieBet.clear();
			try {
				currentBet = Double.parseDouble(bankerBet.getText());
			} catch (NumberFormatException ex){
				currentBet = 0.0;
			}

		});
		bankerBet.setPrefWidth(200);

		tieBet.setOnKeyPressed(e -> {
			playerBet.clear();
			bankerBet.clear();
			try {
				currentBet = Double.parseDouble(tieBet.getText());
			} catch (NumberFormatException ex){
				currentBet = 0.0;
			}

		});
		tieBet.setPrefWidth(200);

		// Put Input Boxes in Container
		HBox bettingPageContent = new HBox();
		bettingPageContent.setSpacing(200);

		VBox playerInputContent = new VBox();
		playerInputContent.setSpacing(20);
		VBox tieInputContent = new VBox();
		tieInputContent.setSpacing(20);
		VBox bankerInputContent = new VBox();
		bankerInputContent.setSpacing(20);

		playerInputContent.getChildren().addAll(playerTitle, playerBet);
		playerInputContent.setAlignment(Pos.CENTER);

		bankerInputContent.getChildren().addAll(bankerTitle, bankerBet);
		bankerInputContent.setAlignment(Pos.CENTER);

		tieInputContent.getChildren().addAll(tieTitle, tieBet);
		tieInputContent.setAlignment(Pos.CENTER);

		bettingPageContent.getChildren().addAll(playerInputContent, tieInputContent, bankerInputContent);
		bettingPageContent.setAlignment(Pos.CENTER);


		// Continue to the actual game
		Button continueButton = new Button();
		Text continueButtonTitle = new Text("Continue");
		continueButton.setFont(Font.loadFont("file:src/fonts/Inter-Medium.ttf", 30));
		continueButton.setGraphic(continueButtonTitle);
		continueButton.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
				"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
		continueButton.setPadding(new Insets(10, 20, 10, 20));
		continueButton.setPrefSize(200, 50);

		continueButton.setOnAction(e -> {
			primaryStage.setScene(scene3);
			System.out.println("Current Bet: " + currentBet);
		});

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

		HBox continueButtonContent = new HBox(20);
		continueButtonContent.setPadding(new Insets(20, 20, 20, 20));
		continueButtonContent.getChildren().add(continueButton);
		continueButtonContent.setAlignment(Pos.CENTER);

		BorderPane root2 = new BorderPane();
		root2.setCenter(bettingPageContent);
		root2.setBottom(continueButtonContent);

		// Main Game ----------
		VBox playersGameContent = new VBox(30);
		VBox bankersGameContent = new VBox(30);

		HBox playersCardContainer = new HBox(10);
		HBox bankersCardContainer = new HBox(10);

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
		playerTotalText.setFont(Font.loadFont("file:src/fonts/Inter-Medium.ttf", 20));
		playersGameContent.getChildren().addAll(playerTotalText, playersCardContainer);
		playersGameContent.setAlignment(Pos.CENTER);

		// Calculate and display the banker's hand total
		bankerTotal = gameLogic.handTotal(bankerHand);
		bankerTotalText = new Text("Banker Total: " + bankerTotal);
		bankerTotalText.setFont(Font.loadFont("file:src/fonts/Inter-Medium.ttf", 20));
		bankersGameContent.getChildren().addAll(bankerTotalText, bankersCardContainer);
		bankersGameContent.setAlignment(Pos.CENTER);

		String winner;
		if (bankerTotal == 8 || bankerTotal == 9 || playerTotal == 8 || playerTotal == 9){
			// Natural Win
			winner = gameLogic.whoWon(playerHand, bankerHand);
			// Stop game
		}

		Button drawCardButton = new Button("DRAW");
		HBox gameBottomMenu = new HBox(150);
		gameBottomMenu.setPadding(new Insets(20, 20, 20, 20));
		gameBottomMenu.getChildren().add(drawCardButton);

		drawCardButton.setOnAction(e -> {
			playerHand.add(theDealer.drawOne());
			Image playerCard3 = new Image(playerHand.get(2).getValue()+  "_of_" + playerHand.get(2).getSuite().toLowerCase() + ".png");
			ImageView playerCard3ImageView = new ImageView(playerCard1);
			playerCard3ImageView.setFitWidth(100); // Set the desired width
			playerCard3ImageView.setFitHeight(150); // Set the desired height

			playersCardContainer.getChildren().add(playerCard3ImageView);
			playerTotal = gameLogic.handTotal(playerHand);
			playerTotalText = new Text("Player Total: " + playerTotal);
			playerTotalText.setFont(Font.loadFont("file:src/fonts/Inter-Medium.ttf", 20));
			playersGameContent.getChildren().addAll(playerTotalText, playersCardContainer);
			playersGameContent.setAlignment(Pos.CENTER);

			System.out.println("Winner" + gameLogic.whoWon(playerHand, bankerHand));
		});

		BorderPane root3 = new BorderPane();

		HBox mainGameContent = new HBox(500);
		mainGameContent.getChildren().addAll(playersGameContent, bankersGameContent);
		mainGameContent.setAlignment(Pos.CENTER);

		root3.setCenter(mainGameContent);
		root3.setBottom(gameBottomMenu);

		// put root on the scene which goes on to the stage
		scene1 = new Scene(root1, 1280,720);
		scene2 = new Scene(root2, 1280, 720);
		scene3 = new Scene(root3, 1280, 720);
		primaryStage.setScene(scene1);
		primaryStage.show();

	}
}
