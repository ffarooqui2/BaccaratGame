import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.sql.SQLClientInfoException;
import java.util.ArrayList;


public class BaccaratGame extends Application {

	private ArrayList<Card> playerHand;
	private ArrayList<Card> bankerHand;
	private BaccaratDealer theDealer;
	private BaccaratGameLogic gameLogic;
	private double currentBet;
	private double totalWinnings;

	Scene scene1, scene2;

	public double evaluateWinnings(){
		return 0;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World");

		BaccaratDealer dealer = new BaccaratDealer();
		System.out.println(dealer.deckSize());
		launch(args);
	}


	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Baccarat something something...");

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


		// BET
		BorderPane root2 = new BorderPane();
		TextField playerBet = new TextField("$0.00");
		playerBet.setPrefWidth(200);
		TextField bankerBet = new TextField("$0.00");
		bankerBet.setPrefWidth(200);

		HBox bettingPageContent = new HBox();
		bettingPageContent.setSpacing(200);
		bettingPageContent.getChildren().addAll(playerBet, bankerBet);
		bettingPageContent.setAlignment(Pos.CENTER);
		root2.setCenter(bettingPageContent);


		// put root on the scene which goes on to the stage
		scene1 = new Scene(root1, 1920,1080);
		scene2 = new Scene(root2, 1920, 1080);
		primaryStage.setScene(scene1);
		primaryStage.show();
	}
}
