package guicomponents;

import databasemanagement.Question;
import databasemanagement.UserAnswer;
import latex.LaTeXProcessor;
import latex.LaTeXTextPane;
import listeners.QuestionChangeListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphingQuestionPanel extends JPanel {
    private QuestionChangeListener questionChangeListener;

    public GraphingQuestionPanel(Question q, ArrayList<UserAnswer> userAnswers){
        // layout
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1;
        gc.weighty = 1;

        // latex text pane
        LaTeXTextPane questionPane = new LaTeXTextPane(LaTeXProcessor.toMathComponents(q.getQDesc(), 18), 18);
        questionPane.setPreferredSize(new Dimension(0, 500));

        // adding text pane
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(0, 20, 30, 20);
        add(questionPane, gc);

        // submit button
        JButton submitBtn;
        if(q.isExam())
            submitBtn = new JButton("Next Question");
        else
            submitBtn = new JButton("Check Answer");

        submitBtn.addActionListener((arg0) -> {
            if(questionChangeListener != null)
                questionChangeListener.changeQuestion();
        });

        submitBtn.setPreferredSize(new Dimension(0, 40));

        gc.gridy = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(20,20,20,20);
        gc.anchor = GridBagConstraints.CENTER;

        add(submitBtn, gc);
    }

    public void setQuestionChangeListener(QuestionChangeListener questionChangeListener) {
        this.questionChangeListener = questionChangeListener;
    }
}
