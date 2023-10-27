import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class Card {
    private String suite;
    private int value;
    public Card (String suite, int value){
        this.suite = suite;
        this.value = value;
    }

    public String getSuite (){
        return this.suite;
    }

    public int getValue (){
        return this.value;
    }

}
