package latex;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.swing.*;

public class LaTeXMathComponent implements MathComponent {
    private String latex;
    private int size;
    public static final String inputIdentifier = "%INPUT%";
    private String latexFormat;
    private String[] rawInputs;

    public LaTeXMathComponent(String latex, int size){
        this.latex = latex;
        this.size = size;
    }

    // @param latexFormat = accepts a correct general latex syntax with inputIdentifier as all inputs
    public LaTeXMathComponent(String latexFormat, int size, String... inputs){
        StringBuilder modStr = new StringBuilder(latexFormat);

        for(String input : inputs){
            int inputPos = modStr.indexOf(inputIdentifier);
            if(inputPos > -1)
                modStr.replace(inputPos, inputPos + inputIdentifier.length(), "{" + input + "}");
            else
                modStr.append("{" + input + "}");
        }

        this.latexFormat = latexFormat;
        latex = modStr.toString();
        this.size = size;
        this.rawInputs = inputs;
    }

    public String getString() {
        return latex;
    }

    public JLabel getLabel(){
        try {
            TeXFormula formula = new TeXFormula(latex);
            TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);

            // setting alignment
            JLabel r = new JLabel(icon);
            r.setAlignmentY(0.6f);

            return r;
        }catch (Exception e){
            e.printStackTrace();
            return new JLabel("Error with LaTeX");
        }
    }

    public TeXIcon getIcon(){
        try {
            TeXFormula formula = new TeXFormula(latex);
            return formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
        }catch(Exception e){
            e.printStackTrace();
            return new TeXFormula("\\LaTeX \\text{ Error}").createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
        }
    }

    public String getLatexFormat() {
        return latexFormat;
    }

    public String[] getRawInputs() {
        return rawInputs;
    }

    public String toString(){
        return String.format("LMC string: \"%s\"", latex);
    }
}
