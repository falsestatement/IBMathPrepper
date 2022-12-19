package latex;

import javax.swing.*;

public class LaTeXButton extends JButton {
    private String name;
    private LaTeXMathComponent mathComponent;

    public LaTeXButton(String name, String latexFormat, int size, int numInputs, JTextField textField){
        // variables
        String[] emptyInputs = new String[numInputs];
        this.name = name;
        for(String s : emptyInputs)
            s = "";

        mathComponent = new LaTeXMathComponent(latexFormat, size, emptyInputs);

        // action listener
        addActionListener((arg0) -> {
            // dialog
            LaTeXDialog dialog = new LaTeXDialog("Insert " + name, mathComponent, textField);
        });

        // setting icon
        String[] vars = new String[numInputs];
        for(int i = 0; i < numInputs; i++)
            vars[i] = Character.toString('a' + i);

        LaTeXMathComponent displayComponent = new LaTeXMathComponent(latexFormat, size, vars);
//        System.out.println(String.format("latex: %s", displayComponent.getString()));
        setIcon(displayComponent.getIcon());

        // tool tip
        setToolTipText(name);
    }
}
