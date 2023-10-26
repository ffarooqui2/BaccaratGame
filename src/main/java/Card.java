import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class Card {
    private String suite;
    private int value;
    private ImageView imageView;
    public Card (String suite, int value){
        this.suite = suite;
        this.value = value;
        Image image = new Image("src/images/" + value + "_of_" + suite.toLowerCase() + ".png");
        this.imageView = new ImageView(image);
    }

    public String getSuite (){
        return this.suite;
    }

    public int getValue (){
        return this.value;
    }

    public ImageView getImageView() { return imageView; }
}
