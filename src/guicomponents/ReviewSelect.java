package guicomponents;

import databasemanagement.DatabaseSerialiser;
import databasemanagement.Question;
import databasemanagement.Topic;
import databasemanagement.UserAnswer;
import listeners.AnswerRefreshListener;
import listeners.PanelChangeListener;
import listeners.QuestionPanelListener;
import listeners.UserAnswerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReviewSelect extends SelectPanel{
    private PanelChangeListener panelChangeListener;
    private QuestionPanelListener questionPanelListener;
    private UserAnswerListener userAnswerListener;
    private ArrayList<UserAnswer> userAnswers;
    private ArrayList<Question> questions;
    private JLabel titleLbl;
    private JProgressBar[] topicPrgBar;
    private JLabel[] percentLbls;
    private ArrayList<Topic> topics;
    private AnswerRefreshListener answerRefreshListener;

    public ReviewSelect(ArrayList<Topic> topics, ArrayList<Question> questions, ArrayList<UserAnswer> userAnswers){
        super(questions, topics, userAnswers);

        // declare components
        titleLbl = new JLabel("Review");
        JCheckBox[] topicCbx = new JCheckBox[topics.size()];
        topicPrgBar = new JProgressBar[topics.size()];
        JButton startBtn = new JButton("Start Review");
        JButton answeredBtn = new JButton("Review Answered Questions");
        JButton clearAnsweredBtn = new JButton("Clear All Answered Questions");
        JButton backBtn = new JButton("Back");
        percentLbls = new JLabel[topics.size()];
        JSeparator separator = new JSeparator();
        this.userAnswers = userAnswers;
        this.questions = questions;
        this.topics = topics;

        // titleLbl setup
        titleLbl.setFont(new Font("Helvetica", Font.BOLD, 24));

        // setting up checkboxes and progress bars and percent labels
        for(int i = 0; i < topics.size(); i++){
            // init checkbox
            topicCbx[i] = new JCheckBox(topics.get(i).getName());

            // setup progress bar
            topicPrgBar[i] = new JProgressBar();
            topicPrgBar[i].setIndeterminate(false);

            // find maximum value for progress bar
            int max = 0;
            for(Question q : questions){
                if(q.getTopicId() == topics.get(i).getId())
                    max++;
            }

            // setting maximum and minimum for progress bar
            topicPrgBar[i].setMaximum(max*100); // multiply 100 for resolution
            topicPrgBar[i].setMinimum(0);

            // setting size for progress bar
            topicPrgBar[i].setPreferredSize(new Dimension(350, 20));

            // setting up percent labels
            DecimalFormat df = new DecimalFormat("00.00");
            percentLbls[i] = new JLabel(df.format(topicPrgBar[i].getPercentComplete()*100) + "%");
        }

        /* -------------------------------------------------------------------- Adding Components -------------------------------------------------------------------- */
        // setting up grid bag layout
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        // back button
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridwidth = 2;
        gc.insets = new Insets(10, 10,10,10);

        // action listener
        backBtn.addActionListener((ActionEvent arg0) -> {
            if(panelChangeListener != null)
                panelChangeListener.panelChangePerformed();
        });

        add(backBtn, gc);

        // clear button
        clearAnsweredBtn.addActionListener((arg0) -> {
            int val = new MessageDialog(300,200, "Are you sure?",
                    "This will permanently delete all current records of answered question\n Application will restarts",
                    MessageDialog.DialogType.YES_NO).getValue();

            if(val == 0){
                DatabaseSerialiser<UserAnswer> ua = new DatabaseSerialiser<>("useranswers", new ArrayList<>());
                ua.serialize();

                if(answerRefreshListener != null &&  panelChangeListener != null) {
                    answerRefreshListener.refreshAnswers();
                    panelChangeListener.panelChangePerformed();
                }
            }
        });
        add(clearAnsweredBtn);

        // title
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridy = 1;
        gc.gridwidth = 3;
        gc.insets = new Insets(0,0,30,0);
        add(titleLbl, gc);

        // separator location (index)
        int sepLoc =  6;

        // checkboxes and progress bars and labels
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        for(int  i = 0; i < topics.size(); i++){
            gc.insets = new Insets(3,10,3,10);
            if(i == sepLoc){
                gc.anchor = GridBagConstraints.CENTER;
                gc.insets = new Insets(10,15,10,10);
                gc.gridy++;
                gc.gridwidth = 3;
                gc.gridx = 0;
                gc.fill = GridBagConstraints.HORIZONTAL;
                add(separator, gc);
                gc.fill = GridBagConstraints.NONE;
                gc.gridwidth = 1;
                gc.insets = new Insets(3,10,3,10);
            }
            gc.anchor = GridBagConstraints.LINE_START;
            gc.gridy++;
            gc.gridx = 0;
            add(topicCbx[i], gc);

            gc.gridx = 1;
            add(topicPrgBar[i], gc);

            gc.gridx = 2;
            gc.insets = new Insets(0,0,0,10);
            add(percentLbls[i], gc);
        }

        // submit button
        gc.insets = new Insets(20,10,20,5);
        gc.anchor = GridBagConstraints.LINE_END;
        gc.gridwidth = 1;
        gc.gridy++;
        gc.gridx = 2;
        add(startBtn, gc);

        // action listener
        startBtn.addActionListener((arg0) -> {
            // compile selected questions
            // scan for selected
            ArrayList<Integer> selectedIndexes = new ArrayList<>();
            ArrayList<Question> selectedQuestions = new ArrayList<>();
            for(int i = 0; i < topicCbx.length; i++){
                if(topicCbx[i].isSelected())
                    selectedIndexes.add(topics.get(i).getId());
            }

            // putting questions in final array
            for(Question q : questions){
                for(Integer i : selectedIndexes){
                    if(q.getTopicId() == i)
                        selectedQuestions.add(q);
                }
            }

            // switch panel
            if(questionPanelListener != null)
                questionPanelListener.showQuestionPanel(selectedQuestions);
        });

        // answered button
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_START;
        answeredBtn.addActionListener((arg0) -> {
            if(!(userAnswerListener == null))
                userAnswerListener.showUserAnswers(userAnswers);
        });
        add(answeredBtn, gc);

        //sampleProgressBar(topicPrgBar, percentLbls);

        // setting progress bar values according to questions answered
        for(int i  = 0; i < topics.size(); i++){
            getProgress(topicPrgBar[i], percentLbls[i], topics.get(i).getId());
        }
    }

    public void refresh(){
        super.refresh(topicPrgBar, percentLbls);
    }

    public void setPanelChangeListener(PanelChangeListener panelChangeListener) {
        this.panelChangeListener = panelChangeListener;
    }

    public void setQuestionPanelListener(QuestionPanelListener questionPanelListener){
        this.questionPanelListener = questionPanelListener;
    }

    public void setUserAnswerListener(UserAnswerListener userAnswerListener){
        this.userAnswerListener = userAnswerListener;
    }

    public void setAnswerRefreshListener(AnswerRefreshListener answerRefreshListener){
        this.answerRefreshListener = answerRefreshListener;
    }
}
