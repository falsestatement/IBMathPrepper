package latex;

import java.util.ArrayList;

public abstract class LaTeXProcessor {
    public static final String LaTeX_LOCATOR = "\\ltx";

    public static ArrayList<MathComponent> toMathComponents(String str, int fontSize) {
        // declare variables
        String modStr = str;
        int latexIndex = -1;
        ArrayList<MathComponent> r = new ArrayList<>();

        while(findLaTeX(modStr) != null){
            String curLaTeX = findLaTeX(modStr);
            if(curLaTeX != null){
                // setup variables
                latexIndex = modStr.indexOf(LaTeX_LOCATOR);

                //  declare the current working string, composed of one plain text and one latex component.
                String workStr = modStr.substring(0, latexIndex + LaTeX_LOCATOR.length() +
                        findLaTeX(modStr).length() + 2);

                // add the plain text component
                // check if empty
                if(!workStr.substring(0, latexIndex).isEmpty())
                    r.add(new PlainMathComponent(workStr.substring(0, latexIndex)));

                // add the LaTeX component
                r.add(new LaTeXMathComponent(findLaTeX(workStr), fontSize));

                // clean modStr
                modStr = modStr.substring(latexIndex + LaTeX_LOCATOR.length() + findLaTeX(modStr).length() + 2);
            }
        }

        // put in remaining plain text
        // check if is empty
        if(!modStr.isEmpty())
            r.add(new PlainMathComponent(modStr));

        return r;
    }

    public static String findLaTeX(String str){
        if(str.contains(LaTeX_LOCATOR)){
            // set modString to start at {
            String modStr = str.substring(str.indexOf(LaTeX_LOCATOR) + LaTeX_LOCATOR.length());

            // count open and close braces
            int openBrace = 0;
            int closeBrace = 0;

            for(int i = 0; i < modStr.length(); i++){
                if(modStr.charAt(i) == '{')
                    openBrace++;
                if(modStr.charAt(i) == '}')
                    closeBrace++;

                // check if its non-zero equal
                if(openBrace > 0 && openBrace == closeBrace){
                    modStr = modStr.substring(1, i);
                    break;
                }
            }

            // return null if incorrect amount of braces
            if(openBrace != closeBrace)
                return null;

            return modStr;
        }else
            return null;
    }
}
