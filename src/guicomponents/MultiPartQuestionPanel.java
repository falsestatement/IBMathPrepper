package guicomponents;

import databasemanagement.Question;
import databasemanagement.UserAnswer;
import latex.LaTeXButtonPanel;
import latex.LaTeXMathComponent;
import latex.LaTeXProcessor;
import latex.LaTeXTextPane;
import listeners.AnswerRequestListener;
import listeners.QuestionChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class MultiPartQuestionPanel extends JPanel {
    private JLabel previewLbl = new JLabel();
    private JTextField answerField = new JTextField();
    private PDControlScrollPane previewScroll = new PDControlScrollPane();
    private QuestionChangeListener questionChangeListener;
    private AnswerRequestListener answerRequestListener;
    private JButton submitBtn;
    private LaTeXButtonPanel buttonPanel;
    private Question q;

    public MultiPartQuestionPanel(Question q, ArrayList<UserAnswer> userAnswers) {
        this.q = q;

        // layout
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1;
        gc.weighty = 1;

        // latex text pane
        LaTeXTextPane questionPane = new LaTeXTextPane(LaTeXProcessor.toMathComponents(q.getQDesc(), 18), 18);
        questionPane.setPreferredSize(new Dimension(0, 300));

        // adding text pane
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(0, 20, 30, 20);
        add(questionPane, gc);

        // answer field
        answerField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateLabel();
            }

            public void removeUpdate(DocumentEvent e) {
                updateLabel();
            }

            public void changedUpdate(DocumentEvent e) {
                updateLabel();
            }
        });

        // adding answer field
        gc.weightx = 0;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(20,260,20,20);
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 0;
        add(answerField, gc);

        // latex button panel
        buttonPanel = new LaTeXButtonPanel(answerField);

        // adding latex button panel
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(20,20,20,20);
        gc.gridy = 1;
        add(buttonPanel, gc);

        // adding live preview
        previewScroll.getViewport().add(previewLbl);
        previewScroll.setPreferredSize(new Dimension(0, 150));
        previewScroll.getHorizontalScrollBar().setUnitIncrement(5);

        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(60,263,20,23);
        add(previewScroll, gc);

        // submit button
        if(q.isExam()){
            submitBtn = new JButton("Next Question");

            // action listener
            submitBtn.addActionListener((arg0) -> {
                int val;
                if(questionChangeListener != null && !answerField.getText().isEmpty()) {
                    questionChangeListener.changeQuestion();
                    userAnswers.add(new UserAnswer(q, answerField.getText()));
                    UserAnswer.updateLocal(userAnswers);
                }
                else {
                    val = new MessageDialog(400,200,"Empty Answer",
                            "The answer field is empty, do you still want to submit?",
                            MessageDialog.DialogType.YES_NO).getValue();
                    if(val == 0 && questionChangeListener != null) {
                        questionChangeListener.changeQuestion();
                        userAnswers.add(new UserAnswer(q, answerField.getText()));
                        UserAnswer.updateLocal(userAnswers);
                    }
                }
            });
        }
        else {
            submitBtn = new JButton("Check Answer");

            // action listener
            submitBtn.addActionListener((arg0) -> {
                int val;
                if(answerRequestListener != null && !answerField.getText().isEmpty()){
                    answerRequestListener.showAnswer(new AnswerPanel(q, questionChangeListener));
                    userAnswers.add(new UserAnswer(q, answerField.getText()));
                    UserAnswer.updateLocal(userAnswers);
                    submitBtn.setEnabled(false);
                    answerField.setEnabled(false);
                    buttonPanel.setEnabled(false);
                } else {
                    val = new MessageDialog(400,200,"Empty Answer",
                            "The answer field is empty, do you still want to submit?",
                            MessageDialog.DialogType.YES_NO).getValue();
                    if(val == 0 && questionChangeListener != null){
                        answerRequestListener.showAnswer(new AnswerPanel(q, questionChangeListener));
                        userAnswers.add(new UserAnswer(q, answerField.getText()));
                        UserAnswer.updateLocal(userAnswers);
                        submitBtn.setEnabled(false);
                        answerField.setEnabled(false);
                        buttonPanel.setEnabled(false);
                    }
                }
            });
        }

        submitBtn.setPreferredSize(new Dimension(0, 37));

        gc.insets = new Insets(230, 260,0,20);
        add(submitBtn, gc);

    }

    private void updateLabel() {
        previewLbl.setIcon(new LaTeXMathComponent(answerField.getText(), 32).getIcon());
        previewScroll.getHorizontalScrollBar().setValue(previewScroll.getHorizontalScrollBar().getMaximum());
    }

    public void setQuestionChangeListener(QuestionChangeListener questionChangeListener) {
        this.questionChangeListener = questionChangeListener;
    }

    public void setAnswerRequestListener(AnswerRequestListener answerRequestListener){
        this.answerRequestListener = answerRequestListener;
    }

    public void disableInputs(){
        submitBtn.setEnabled(false);
        answerField.setEnabled(false);
        buttonPanel.setEnabled(false);
    }

    public void setAnswerField(String answer){
        answerField.setText(answer);
    }
}
