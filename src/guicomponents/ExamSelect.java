package guicomponents;

import databasemanagement.Exam;
import databasemanagement.Question;
import databasemanagement.Topic;
import databasemanagement.UserAnswer;
import listeners.PanelChangeListener;
import listeners.QuestionPanelListener;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ExamSelect extends SelectPanel {
    private PanelChangeListener panelChangeListener;
    private ArrayList<QuestionPanelListener> questionPanelListeners;
    private JProgressBar[] topicPrgBar;
    private JLabel[] percentLbls;

    public ExamSelect(ArrayList<Exam> exams, ArrayList<Topic> topics, ArrayList<Question> questions, ArrayList<UserAnswer> userAnswers){
        super(questions, topics, userAnswers);

        // declare components
        JComboBox<String> yearComBox;
        JComboBox<String> monthComBox;
        JComboBox<String> paperComBox;
        JLabel yearLbl = new JLabel("Year:");
        JLabel monthLbl = new JLabel("Month:");
        JLabel paperLbl = new JLabel("Paper:");
        JButton startBtn = new JButton("Start Exam");
        JButton backBtn = new JButton("Back");
        JLabel titleLbl = new JLabel("Exam");
        JLabel byTopicLbl = new JLabel("By Topic");
        JLabel fullExamLbl = new JLabel("Full Exam");
        JSeparator horizontalSptr = new JSeparator();
        JSeparator verticalSptr = new JSeparator();
        percentLbls = new JLabel[topics.size()];
        JCheckBox[] topicCbx = new JCheckBox[topics.size()];
        topicPrgBar = new JProgressBar[topics.size()];
        JPanel topicChoicePanel = new JPanel();
        JPanel examChoicePanel = new JPanel();
        JButton byTopicBtn = new JButton("Start Review");
        questionPanelListeners = new ArrayList<>();

        // finding year, month and paper choices
        ArrayList<String> years = new ArrayList<>();
        ArrayList<String> months = new ArrayList<>();
        ArrayList<String> papers = new ArrayList<>();

        // adding "any" option
        years.add("Any");
        months.add("Any");
        papers.add("Any");

        // choices from database
        for(Exam e : exams){
            if(!years.contains(Integer.toString(e.getYear())))
                years.add(Integer.toString(e.getYear()));
            if(!months.contains(e.getMonth()))
                months.add(e.getMonth());
            if(!papers.contains(Integer.toString(e.getPaper())))
                papers.add(Integer.toString(e.getPaper()));
        }

        // converting to basic array
        yearComBox = new JComboBox<>(years.toArray(new String[years.size()]));
        monthComBox = new JComboBox<>(months.toArray(new String[months.size()]));
        paperComBox = new JComboBox<>(papers.toArray(new String[papers.size()]));

        // title setup
        titleLbl.setFont(new Font("Helvetica", Font.BOLD, 24));
        byTopicLbl.setFont(new Font("Helvetica", Font.BOLD, 18));
        fullExamLbl.setFont(new Font("Helvetica", Font.BOLD, 18));

        // setting up combo boxes
        yearComBox.setPreferredSize(new Dimension(200, 20));
        monthComBox.setPreferredSize(new Dimension(200, 20));
        paperComBox.setPreferredSize(new Dimension(200, 20));

        // setting up separators
        horizontalSptr.setOrientation(JSeparator.HORIZONTAL);
        verticalSptr.setOrientation(JSeparator.VERTICAL);

        // setting up panel
        setLayout(new GridBagLayout());
        topicChoicePanel.setLayout(new GridBagLayout());
        examChoicePanel.setLayout(new GridBagLayout());

        // Setting up constraints
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(10,10,10,10);

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
            topicPrgBar[i].setPreferredSize(new Dimension(200, 20));

            // setting up percent labels
            DecimalFormat df = new DecimalFormat("00.00");
            percentLbls[i] = new JLabel(df.format(topicPrgBar[i].getPercentComplete()*100) + "%");
        }

        // adding components
        // by topic panel
        // horizontalSptr location
        int sepLoc = 6;

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
                topicChoicePanel.add(horizontalSptr, gc);
                gc.fill = GridBagConstraints.NONE;
                gc.gridwidth = 1;
                gc.insets = new Insets(3,10,3,10);
            }
            gc.anchor = GridBagConstraints.LINE_START;
            gc.gridy++;
            gc.gridx = 0;
            topicChoicePanel.add(topicCbx[i], gc);
            gc.gridx = 1;
            topicChoicePanel.add(topicPrgBar[i], gc);
            gc.gridx = 2;
            gc.anchor = GridBagConstraints.LINE_END;
            gc.insets = new Insets(0,0,0,10);
            topicChoicePanel.add(percentLbls[i], gc);
        }

        // exam choice panel
        // year
        gc.insets = new Insets(5,10,5,10);
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridy = 0;
        gc.gridx = 0;
        examChoicePanel.add(yearLbl, gc);
        gc.gridx = 1;
        examChoicePanel.add(yearComBox, gc);

        // month
        gc.gridy++;
        gc.gridx = 0;
        examChoicePanel.add(monthLbl, gc);
        gc.gridx = 1;
        examChoicePanel.add(monthComBox, gc);

        // paper
        gc.gridy++;
        gc.gridx = 0;
        examChoicePanel.add(paperLbl, gc);
        gc.gridx = 1;
        examChoicePanel.add(paperComBox, gc);

        // back button
        // settings
        backBtn.addActionListener((arg0) -> {
            if(panelChangeListener != null)
                panelChangeListener.panelChangePerformed();
        });

        gc.insets = new Insets( 10,10,10,10);
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 0;
        gc.gridy = 0;
        add(backBtn, gc);

        // title
        gc.gridx = 0;
        gc.gridy++;
        gc.gridwidth = 3;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets( 0,0,20,0);
        add(titleLbl, gc);

        // subtitles
        gc.insets = new Insets( 20,20,20,20);
        gc.gridx = 0;
        gc.gridy++;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.CENTER;
        add(byTopicLbl, gc);

        gc.gridx = 2;
        gc.anchor = GridBagConstraints.CENTER;
        add(fullExamLbl, gc);


        // topic choice panel
        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridwidth = 1;
        add(topicChoicePanel, gc);

        // vertical separator
        gc.gridx = 1;
        gc.fill = GridBagConstraints.VERTICAL;
        add(verticalSptr, gc);

        // adding exam choice panel
        gc.gridx = 2;
        gc.fill = GridBagConstraints.NONE;
        add(examChoicePanel, gc);

        // start buttons
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridy++;

        byTopicBtn.addActionListener((arg0) -> {
            // fetching questions
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

            if(questionPanelListeners.size() > 0)
                questionPanelListeners.get(0).showQuestionPanel(selectedQuestions);
        });

        add(byTopicBtn, gc);

        gc.gridx = 2;

        startBtn.addActionListener((arg0) -> {
            // getting info from combo boxes
            String year = (String) yearComBox.getSelectedItem();
            String month = (String) monthComBox.getSelectedItem();
            String paper = (String) paperComBox.getSelectedItem();

            // search terms
            int y = 0, p = 0;

            // setting terms
            if(!year.equals("Any"))
                y = Integer.parseInt(year);
            if(!paper.equals("Any"))
                p = Integer.parseInt(paper);

            // searching for exam
            ArrayList<Exam> e = new ArrayList<>();

            // by year:
            if(y != 0) {
                for (Exam exam : exams)
                    if (exam.getYear() == y)
                        e.add(exam);
            }else {
                for (Exam exam : exams)
                    e.add(exam);
            }

            // by month
            if(!month.equals("Any")){
                for(int i = 0; i < e.size(); i++)
                    if(!e.get(i).getMonth().equals(month)) {
                        e.remove(i);
                        i--;
                    }
            }

            // by paper
            if(p != 0){
                for(int i = 0; i < e.size(); i++)
                    if(e.get(i).getPaper() != p){
                        e.remove(i);
                        i--;
                    }
            }

            // find questions
            ArrayList<Question> q = new ArrayList<>();

            for(Question question : questions) {
                for (Exam exam : e)
                    if (question.getExamId() == exam.getId())
                        q.add(question);
            }

            if(questionPanelListeners.size() > 1)
                questionPanelListeners.get(1).showQuestionPanel(q);
        });

        add(startBtn, gc);

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

    public void addQuestionPanelListener(QuestionPanelListener listener){
        questionPanelListeners.add(listener);
    }
}
