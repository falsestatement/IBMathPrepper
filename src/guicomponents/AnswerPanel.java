package guicomponents;

import databasemanagement.Question;
import latex.LaTeXProcessor;
import latex.LaTeXTextPane;
import listeners.QuestionChangeListener;

import javax.swing.*;
import java.awt.*;

public class AnswerPanel extends JPanel {
    public AnswerPanel(Question q, QuestionChangeListener questionChangeListener){


        // Answer text pane and button
        LaTeXTextPane answerPane = new LaTeXTextPane(LaTeXProcessor.toMathComponents(q.getADesc(), 18), 18);
        JButton nxtBtn = new JButton("Next Question");

        nxtBtn.setPreferredSize(new Dimension(0,50));
        answerPane.setPreferredSize(new Dimension(0, 500));

        nxtBtn.addActionListener((arg0) -> questionChangeListener.changeQuestion());

        // layout
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(20,20,20,20);

        add(answerPane, gc);

        gc.gridy = 1;
        gc.insets = new Insets(0,20,20,20);

        add(nxtBtn, gc);
    }
}
