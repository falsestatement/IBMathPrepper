package guicomponents;

import databasemanagement.Question;
import databasemanagement.UserAnswer;
import listeners.PanelChangeListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QuestionMain extends JSplitPane {
    private PanelChangeListener panelChangeListener;
    private int curQuestion;
    private JPanel[] questionDisplays;

    public QuestionMain(ArrayList<Question> questions, ArrayList<UserAnswer> userAnswers, boolean userAnswerViewingMode){
        super(JSplitPane.HORIZONTAL_SPLIT);

        // declare components
        JPanel btnPanel = new JPanel();
        JPanel questionPanel = new JPanel();
        JScrollPane btnScrollPane = new JScrollPane(btnPanel);
        JScrollPane questionScrollPane = new JScrollPane(questionPanel);
        questionDisplays = new JPanel[questions.size()];

        JButton[] qBtns = new JButton[questions.size()];

        // question buttons setup
        for(int i = 0; i < qBtns.length; i++)
            qBtns[i] = new JButton(questions.get(i).getName());

        // question display setup
        for(int i = 0; i < questionDisplays.length; i++) {
            switch (questions.get(i).getType()) {
                case 0:
                    MultiPartQuestionPanel multiPartQuestionPanel = new MultiPartQuestionPanel(questions.get(i), userAnswers);
                    multiPartQuestionPanel.setQuestionChangeListener(() -> {
                        if(curQuestion < qBtns.length - 1)
                            qBtns[curQuestion + 1].doClick();
                        else if(panelChangeListener != null)
                            panelChangeListener.panelChangePerformed();
                    });

                    int tmp = i;
                    multiPartQuestionPanel.setAnswerRequestListener((answerPanel) -> {
                        questionDisplays[tmp].add(answerPanel);
                        questionDisplays[tmp].repaint();
                        questionDisplays[tmp].revalidate();

                    });

                    questionDisplays[i] = new JPanel();
                    questionDisplays[i].setLayout(new BoxLayout(questionDisplays[i], BoxLayout.Y_AXIS));
                    questionDisplays[i].add(multiPartQuestionPanel);

                    // answer viewing mode
                    if(userAnswerViewingMode == true) {
                        multiPartQuestionPanel.disableInputs();
                        multiPartQuestionPanel.setAnswerField(userAnswers.get(i).getAnswer());
                        questionDisplays[i].add(new AnswerPanel(questions.get(i), () -> {
                            if(curQuestion < qBtns.length - 1)
                                qBtns[curQuestion + 1].doClick();
                            else if(panelChangeListener != null)
                                panelChangeListener.panelChangePerformed();
                        }));
                    }
                    break;

                case 1:
                    GraphingQuestionPanel graphingQuestionPanel = new GraphingQuestionPanel(questions.get(i), userAnswers);
                    graphingQuestionPanel.setQuestionChangeListener(() -> {
                        if(curQuestion < qBtns.length - 1)
                            qBtns[curQuestion + 1].doClick();
                        else if(panelChangeListener != null)
                            panelChangeListener.panelChangePerformed();
                    });

                    questionDisplays[i] = graphingQuestionPanel;
                    break;
            }
        }

        // split pane setup
        setDividerLocation(300);
        setResizeWeight(0);
        setLeftComponent(btnScrollPane);
        setRightComponent(questionScrollPane);
        setEnabled(false);

        // btn scroll setup
        btnScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        btnScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        btnScrollPane.getVerticalScrollBar().setUnitIncrement(5);
        btnScrollPane.setMinimumSize(new Dimension(300, 50));
        btnScrollPane.setPreferredSize(new Dimension(300, 50));
        btnScrollPane.setMaximumSize(new Dimension(300, 50));

        // question scroll panel

        questionScrollPane.getVerticalScrollBar().setUnitIncrement(5);
        questionScrollPane.getHorizontalScrollBar().setUnitIncrement(5);

        // panels
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));

        questionPanel.setLayout(new BorderLayout(0, 20));

        // adding buttons
        for(int i = 0; i < qBtns.length; i++) {
            // setting size
            qBtns[i].setMinimumSize(new Dimension(280, 70));
            qBtns[i].setPreferredSize(new Dimension(280, 70));
            qBtns[i].setMaximumSize(new Dimension(280, 70));

            // action listener
            final int tmp = i;
            qBtns[i].addActionListener((arg0) -> {
                for(JButton btns : qBtns)
                    btns.setEnabled(true);
                qBtns[tmp].setEnabled(false);
                questionPanel.removeAll();

                // adding display parts
                QuestionTitleBar qtb = new QuestionTitleBar(questions.get(tmp));
                qtb.setPanelChangeListener(() -> {
                    if(panelChangeListener != null)
                        panelChangeListener.panelChangePerformed();
                });
                questionPanel.add(qtb, BorderLayout.NORTH);
                questionPanel.add(questionDisplays[tmp], BorderLayout.CENTER);

                questionPanel.revalidate();
                questionPanel.repaint();
                for(JButton btns : qBtns)
                    btns.setFocusPainted(false);
                curQuestion = tmp;
            });
            btnPanel.add(qBtns[i]);
        }

        GridBagConstraints gc = new GridBagConstraints();
        gc.weighty = 1;
        gc.weightx = 2;

        qBtns[0].doClick();
    }

    public void setPanelChangeListener(PanelChangeListener panelChangeListener) {
        this.panelChangeListener = panelChangeListener;
    }
}
