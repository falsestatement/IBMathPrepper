package latex;

import java.util.ArrayList;

public class PlainMathComponent implements MathComponent{
    private String text;

    public PlainMathComponent (String text){
        this.text = text;
    }

    public String getString() {
        return text;
    }

    public String toString(){
        return String.format("PMC string: \"%s\"", text);
    }
}
