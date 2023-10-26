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


public class BaccaratGame extends Application {

	private ArrayList<Card> playerHand;
	private ArrayList<Card> bankerHand;
	private BaccaratDealer theDealer;
	private BaccaratGameLogic gameLogic;
	private double currentBet;
	private double totalWinnings;

	Scene scene1, scene2, scene3;

	public double evaluateWinnings(){
		return 0;
	}


	public static void main(String[] args) {
		System.out.println("Hello World");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Baccarat");

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

		playButton.setOnAction(e -> primaryStage.setScene(scene2));

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

		// The scene root where content will be played out on
		BorderPane root1 = new BorderPane();
		root1.setCenter(homePageContent);

		Text playerTitle = new Text("PLAYER");
		playerTitle.setFont(Font.loadFont("file:src/fonts/Inter-Medium.ttf", 20));
		Text bankerTitle = new Text("BANKER");
		bankerTitle.setFont(Font.loadFont("file:src/fonts/Inter-Medium.ttf", 20));

		// Input Boxes for Player or Banker
		TextField playerBet = new TextField("0.00");
		TextField bankerBet = new TextField("0.00");

		playerBet.setOnKeyPressed(e -> {
			bankerBet.clear();
			System.out.println("Player's Bet: " + playerBet.getText());
		});
		playerBet.setPrefWidth(200);

		bankerBet.setOnKeyPressed(e -> {
			playerBet.clear();
			System.out.println("Banker's Bet: " + bankerBet.getText());
		});
		bankerBet.setPrefWidth(200);


		// Put Input Boxes in Container
		HBox bettingPageContent = new HBox();
		bettingPageContent.setSpacing(200);

		VBox playerInputContent = new VBox();
		playerInputContent.setSpacing(20);

		VBox bankerInputContent = new VBox();
		bankerInputContent.setSpacing(20);

		playerInputContent.getChildren().addAll(playerTitle, playerBet);
		playerInputContent.setAlignment(Pos.CENTER);

		bankerInputContent.getChildren().addAll(bankerTitle, bankerBet);
		bankerInputContent.setAlignment(Pos.CENTER);

		bettingPageContent.getChildren().addAll(playerInputContent, bankerInputContent);
		bettingPageContent.setAlignment(Pos.CENTER);


		// Continue to the actual game
		Button continueButton = new Button();
		Text continueButtonTitle = new Text("Continue");
		continueButton.setFont(Font.loadFont("file:src/fonts/Inter-Medium.ttf", 20));
		continueButton.setGraphic(continueButtonTitle);
		continueButton.setStyle("-fx-background-color: white; -fx-background-radius: 20px; " +
				"-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
		continueButton.setPadding(new Insets(10, 20, 10, 20));
		continueButton.setPrefSize(200, 50);

		continueButton.setOnAction(e -> primaryStage.setScene(scene3));

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
		continueButtonContent.getChildren().add(continueButton);
		continueButtonContent.setAlignment(Pos.CENTER);

		BorderPane root2 = new BorderPane();
		root2.setCenter(bettingPageContent);
		root2.setBottom(continueButtonContent);



		BorderPane root3 = new BorderPane();

		// put root on the scene which goes on to the stage
		scene1 = new Scene(root1, 1920,1080);
		scene2 = new Scene(root2, 1920, 1080);
		scene3 = new Scene(root3, 1920, 1080);
		primaryStage.setScene(scene1);
		primaryStage.show();

	}

}
