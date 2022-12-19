package latex;

import javax.swing.*;
import java.awt.*;

public class LaTeXButtonPanel extends JPanel {
    private boolean isExpanded = false;
    private LaTeXButton[] buttons = new LaTeXButton[52];
    private JButton expandBtn = new JButton("Expand");

    public LaTeXButtonPanel(JTextField textField){
        // panel setup
        setLayout(new GridBagLayout());

        // latexFormats
        String[] latexFormats = latexFormats();

        // button parameters
        String[] names = names();
        int[] inputs = numInputs();

        for(int i = 0; i < buttons.length; i++){
//            System.out.println(String.format("name: %s, latex: %s, inputs: %d", names[i], latexFormats[i], inputs[i]));
            buttons[i] = new LaTeXButton(names[i], latexFormats[i], 15, inputs[i], textField);
            buttons[i].setPreferredSize(new Dimension(55,55));
            buttons[i].setMaximumSize(new Dimension(55,55));
            buttons[i].setMinimumSize(new Dimension(55,55));
        }

        // expand button action listener
        expandBtn.addActionListener((arg0) -> {
            if(isExpanded){
                expand();
            } else {
                minimize();
            }
        });

        // constraints
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 0;
        gc.weighty = 0;
        gc.gridx = 0;
        gc.gridy = 0;

        // adding first 16 buttons
        int index = 0;
        for(int i = 0; i < 4; i++){
            gc.gridy++;
            for(int j = 0; j < 4; j++){
                gc.gridx = j;
                add(buttons[index], gc);
                index++;
            }
        }

        // adding expand button
        gc.gridx = 0;
        gc.gridwidth = 4;
        gc.gridy++;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(expandBtn, gc);

        // adding last set of 28 buttons
        gc.gridwidth = 1;
        gc.fill = GridBagConstraints.NONE;
        for(int i = 0; i < 9; i++){
            gc.gridy++;
            for(int j = 0; j < 4; j++) {
                gc.gridx = j;
                add(buttons[index], gc);
                index++;
            }
        }

        // initial minimization
        minimize();
    }

    private String[] latexFormats() {
        String[] r = new String[52];
        String s = LaTeXMathComponent.inputIdentifier;

        r[0] = "\\frac";
        r[1] = String.format("%s^", s);
        r[2] = "\\sqrt";
        r[3] = String.format("\\sqrt[%s]", s);
        r[4] = "\\lim_";
        r[5] = "\\sin";
        r[6] = "\\cos";
        r[7] = "\\tan";
        r[8] = String.format("\\log_%s", s);
        r[9] = "\\ln";
        r[10] = String.format("%s\\tfrac", s);
        r[11] = "\\infty";
        r[12] = "\\int";
        r[13] = String.format("\\int_%s^", s);
        r[14] = String.format("%s\\to", s);
        r[15] = String.format("\\sum_%s^", s);
        r[16] = "\\mathrm{d}";
        r[17] = "\\vec";
        r[18] = String.format("%s_", s);
        r[19] = String.format("{%s_%s}^", s, s);
        r[20] = String.format("_%s^%s\\textrm", s, s);
        r[21] = "\\pi";
        r[22] = "\\csc";
        r[23] = "\\sec";
        r[24] = "\\cot";
        r[25] = "\\pm";
        r[26] = "\\mp";
        r[27] = "\\cap";
        r[28] = "\\cup";
        r[29] = "\\cdot";
        r[30] = "\\leq";
        r[31] = "\\geq";
        r[32] = "\\neq";
        r[33] = "\\approx";
        r[34] = "\\text";
        r[35] = "\\therefore";
        r[36] = "\\because";
        r[37] = "\\mathbb{R}";
        r[38] = "\\mathbb{Z}";
        r[39] = "\\varnothing";
        r[40] = "\\in";
        r[41] = "\\ni";
        r[42] = "\\subset";
        r[43] = "\\supset";
        r[44] = "\\alpha";
        r[45] = "\\beta";
        r[46] = "\\theta";
        r[47] = "\\Delta";
        r[48] = "\\gamma";
        r[49] = "\\mu";
        r[50] = "\\varphi";
        r[51] = "f(x)";

        return r;
    }

    private String[] names(){
        String[] r = new String[52];

        r[0] = "Fraction";
        r[1] = "Exponent";
        r[2] = "Square Root";
        r[3] = "N Root";
        r[4] = "Limit";
        r[5] = "sin";
        r[6] = "cos";
        r[7] = "tan";
        r[8] = "Logarithm";
        r[9] = "Natural Logarithm";
        r[10] = "Mixed Fraction";
        r[11] = "Infinity";
        r[12] = "Integral";
        r[13] = "Integral";
        r[14] = "Approaches to";
        r[15] = "Summation";
        r[16] = "Change in";
        r[17] = "Vector";
        r[18] = "Subscript";
        r[19] = "Subscript Exponent";
        r[20] = "Combination / Permutation";
        r[21] = "Pi";
        r[22] = "csc";
        r[23] = "sec";
        r[24] = "cot";
        r[25] = "Plus Minus";
        r[26] = "Minus Plus";
        r[27] = "And";
        r[28] = "Or";
        r[29] = "Multiply";
        r[30] = "Less than or Equal to";
        r[31] = "Greater than or Equal to";
        r[32] = "Not Equal to";
        r[33] = "Approximate";
        r[34] = "Text";
        r[35] = "Therefore";
        r[36] = "Because";
        r[37] = "Real Number";
        r[38] = "Integer Number";
        r[39] = "Null Set";
        r[40] = "Element of";
        r[41] = "Not Element of";
        r[42] = "Subset";
        r[43] = "Super Set";
        r[44] = "Alpha";
        r[45] = "Beta";
        r[46] = "Theta";
        r[47] = "Delta";
        r[48] = "Gamma";
        r[49] = "Mu";
        r[50] = "Phi";
        r[51] = "Function";

        return r;
    }

    public int[] numInputs(){
        int[] r = new int[52];

        r[0] = 2;
        r[1] = 2;
        r[2] = 1;
        r[3] = 2;
        r[4] = 2;
        r[5] = 1;
        r[6] = 1;
        r[7] = 1;
        r[8] = 2;
        r[9] = 1;
        r[10] = 3;
        r[11] = 0;
        r[12] = 1;
        r[13] = 3;
        r[14] = 2;
        r[15] = 3;
        r[16] = 1;
        r[17] = 1;
        r[18] = 2;
        r[19] = 2;
        r[20] = 3;
        r[21] = 0;
        r[22] = 1;
        r[23] = 1;
        r[24] = 1;
        r[25] = 0;
        r[26] = 0;
        r[27] = 0;
        r[28] = 0;
        r[29] = 0;
        r[30] = 0;
        r[31] = 0;
        r[32] = 0;
        r[33] = 0;
        r[34] = 1;
        r[35] = 0;
        r[36] = 0;
        r[37] = 0;
        r[38] = 0;
        r[39] = 0;
        r[40] = 0;
        r[41] = 0;
        r[42] = 0;
        r[43] = 0;
        r[44] = 0;
        r[45] = 0;
        r[46] = 0;
        r[47] = 0;
        r[48] = 0;
        r[49] = 0;
        r[50] = 0;
        r[51] = 0;

        return r;
    }

    private void changeExpanded(){
        isExpanded = !isExpanded;
    }

    private void expand(){
        int index = 16;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 4; j++){
                buttons[index].setVisible(true);
                buttons[index].revalidate();
                buttons[index].repaint();
                index++;
            }
        }
        changeExpanded();
        repaint();
        revalidate();
        expandBtn.setText("Minimize");
    }

    private void minimize(){
        int index = 16;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 4; j++){
                buttons[index].setVisible(false);
                buttons[index].revalidate();
                buttons[index].repaint();
                index++;
            }
        }
        changeExpanded();
        repaint();
        revalidate();
        expandBtn.setText("Expand");
    }

    public void setEnabled(boolean enabled){
        if(enabled)
            for(LaTeXButton b : buttons)
                b.setEnabled(true);
        else
            for(LaTeXButton b : buttons)
                b.setEnabled(false);
    }
}
