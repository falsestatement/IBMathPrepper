package latex;

import guicomponents.PDControlScrollPane;
import guicomponents.ScrollablePanel;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

public class LaTeXTextPane extends PDControlScrollPane {
    private JTextPane tp;

    public LaTeXTextPane(ArrayList<MathComponent> components, int fontSize){
        // setting up styles
        StyleContext context = new StyleContext();
        StyledDocument document = new DefaultStyledDocument(context);
        Style labelStyle = context.getStyle(StyleContext.DEFAULT_STYLE);

        // adding components into text pane
        for(MathComponent mc : components){
            if(mc instanceof PlainMathComponent)
                try {
                    document.insertString(document.getLength(), mc.getString().replace("%n", "\n"), null);
                }catch (Exception e){
                    e.printStackTrace();
                }
            else if(mc instanceof  LaTeXMathComponent) {
                StyleConstants.setComponent(labelStyle, ((LaTeXMathComponent) mc).getLabel());

                // inserting label
                try {
                    document.insertString(document.getLength(), " ", labelStyle);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        // setting up text pane
        tp = new JTextPane(document);
        tp.setEditable(false);
        tp.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));

        // Scrollable panel
        ScrollablePanel sp = new ScrollablePanel(new BorderLayout());
        sp.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);
        sp.setScrollableHeight(ScrollablePanel.ScrollableSizeHint.STRETCH);
        sp.add(tp);

        getViewport().add(sp);
    }

    public void setText(String text){
        tp.setText(text);
    }
}
