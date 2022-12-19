package ibmathprepper;

import databasemanagement.*;
import guicomponents.ExamSelect;
import guicomponents.MessageDialog;
import guicomponents.QuestionMain;
import guicomponents.ReviewSelect;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private int size;
    private JPanel mainPanel;
    private ArrayList<Question> questions;
    private ArrayList<Topic> topics;
    private ArrayList<Exam> exams;
    private ArrayList<UserAnswer> userAnswers;

    public MainFrame(ArrayList<Question> questions, ArrayList<Topic> topics, ArrayList<Exam> exams, ArrayList<UserAnswer> userAnswers){
        super("IB Math Prep");

        // init variables
        size = 200;
        mainPanel = new JPanel();
        this.questions = questions;
        this.topics = topics;
        this.exams  = exams;
        this.userAnswers = userAnswers;

        // Making the frame
        int sizeMultiplier = 75;
        setSize(16 * sizeMultiplier, 9 * sizeMultiplier);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(650, 325));;
        mainPanel.setLayout(new GridBagLayout());

        // setup main menu
        mainMenu();

        // main panel
        add(mainPanel);
        setVisible(true);
        /* ------------------------------------------------------------------------ TESTING ------------------------------------------------------------------------ */

//        new MessageDialog("test", "This message is to be displayed", MessageDialog.DialogType.OK_CANCEL);

//        new LaTeXDialog("Insert Fraction", new LaTeXMathComponent(String.format("\\frac%s%s", LaTeXMathComponent.inputIdentifier, LaTeXMathComponent.inputIdentifier), 18, "1123", "2213"));

//        DBArrayManager.printArray(LaTeXProcessor.toMathComponents(questions.get(0).getADesc(), 20));

//        System.out.println(LaTeXProcessor.findLaTeX("Some random text then BOOM equation: \\ltx{some equation here yeet{{2}}} some more random text"));

//        new ProgressDialog("test", "Processing");

//        DatabaseAccessor test = new DatabaseAccessor("remotemysql.com","ySxYvH4mLl", "ySxYvH4mLl", "NQ5otqUIVy");
//        test.connect();
//        DatabaseSerialiser<Exam> testSerial = new DatabaseSerialiser<>("exams", DBArrayManager.toExamArray(test.selectTable("exams")));
//        testSerial.serialize();
//        DBArrayManager.printArray(testSerial.deserialise());
//        DBArrayManager.printArray(DBArrayManager.toQuestionArray(test.selectTable("questions")));

//        System.out.println(new Question(1, 1, "Some Question", "What is life", "An annoyance", 5, 1, false, 3, 2));

//        System.out.println(test.numCols("questions"));
//        System.out.println(test.numRows("questions"));
//
//        for(String s : test.getRecord("questions", 1))
//            System.out.print(s + " | ");
//        System.out.println();
//        for(String[] sArr : test.selectTable("questions")){
//            for(String s : sArr)
//                System.out.print(s + " | ");
//            System.out.println();
//        }
//        for(String s : test.searchRecord("questions", 1))
//            System.out.println(s);
//        try{
//            test.getCon().close();
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
    }

    public void mainMenu(){
        // set layout
        mainPanel.setLayout(new GridLayout());

        // Setting up the two buttons
        JButton revBtn = new JButton("Review");
        JButton examBtn = new JButton("Mock Exam");

        // Button settings
        // RevBtn settings
        revBtn.setPreferredSize(new Dimension(size, size));

        // action listener
        revBtn.addActionListener((arg0) -> {
            ReviewSelect reviewSelect = new ReviewSelect(topics, questions, userAnswers);

            // review select listeners

            // back button
            reviewSelect.setPanelChangeListener(() -> {
                mainPanel.removeAll();
                mainMenu();
                mainPanel.repaint();
                mainPanel.revalidate();
            });

            // show user answers
            reviewSelect.setUserAnswerListener((userAnswers) -> {
                // extract questions from userAnswers
                if(!userAnswers.isEmpty()) {
                    mainPanel.removeAll();
                    mainPanel.setLayout(new BorderLayout());

                    ArrayList<Question> userQuestions = new ArrayList<>();
                    for (UserAnswer a : userAnswers) {
                        userQuestions.add(a.getQuestion());
                    }

                    // setup User Answers interface
                    QuestionMain userMain = new QuestionMain(userQuestions, userAnswers, true);
                    userMain.setPanelChangeListener(() -> {
                        mainPanel.setLayout(new GridBagLayout());
                        mainPanel.removeAll();
                        mainPanel.add(reviewSelect);
                        mainPanel.repaint();
                        mainPanel.revalidate();
                        reviewSelect.refresh();
                    });

                    // adding userMain
                    mainPanel.add(userMain);
                    mainPanel.repaint();
                    mainPanel.revalidate();
                }else{
                    // warning dialog
                    new MessageDialog(400,150, "No Answered Questions Found",
                            "Please answer at least 1 question to be able to view them",
                            MessageDialog.DialogType.OK);
                }
            });

            // start review button
            reviewSelect.setAnswerRefreshListener(() -> {
                userAnswers = new DatabaseSerialiser<UserAnswer>("useranswers").deserialise();
            });

            reviewSelect.setQuestionPanelListener((questions) -> {
                // question main
                if(questions.size() > 0){
                    mainPanel.removeAll();
                    mainPanel.setLayout(new BorderLayout());

                    // setting up main question panel
                    QuestionMain qm = new QuestionMain(questions, userAnswers, false);
                    qm.setPanelChangeListener(() -> {
                        mainPanel.setLayout(new GridBagLayout());
                        mainPanel.removeAll();
                        mainPanel.add(reviewSelect);
                        mainPanel.repaint();
                        mainPanel.revalidate();
                        reviewSelect.refresh();
                    });

                    mainPanel.add(qm, BorderLayout.CENTER);
                    mainPanel.repaint();
                    mainPanel.revalidate();
                }else{
                    // warning dialog
                    new MessageDialog(400,150, "Topic Selection",
                            "Please select at least 1 topic to start review",
                            MessageDialog.DialogType.OK);
                }

            });

            mainPanel.setLayout(new GridBagLayout());
            mainPanel.removeAll();
            mainPanel.add(reviewSelect);
            mainPanel.repaint();
            mainPanel.revalidate();
            reviewSelect.refresh();
        });

        // ExamBtn settings
        examBtn.setPreferredSize(new Dimension(size, size));

        // action listener
        examBtn.addActionListener((arg0) -> {
            ExamSelect examSelect = new ExamSelect(exams, topics, questions, userAnswers);
            examSelect.setPanelChangeListener(() -> {
                mainPanel.removeAll();
                mainMenu();
                mainPanel.repaint();
                mainPanel.revalidate();
                examSelect.refresh();
            });

            // start review button
            examSelect.addQuestionPanelListener((questions) -> {
                // question main
                if(questions.size() > 0){
                    mainPanel.removeAll();
                    mainPanel.setLayout(new BorderLayout());

                    // setting up main question panel
                    QuestionMain qm = new QuestionMain(questions, userAnswers, false);
                    qm.setPanelChangeListener(() -> {
                        mainPanel.setLayout(new GridBagLayout());
                        mainPanel.removeAll();
                        mainPanel.add(examSelect);
                        mainPanel.repaint();
                        mainPanel.revalidate();
                        examSelect.refresh();
                    });

                    mainPanel.add(qm, BorderLayout.CENTER);
                    mainPanel.repaint();
                    mainPanel.revalidate();
                }else{
                    // warning dialog
                    new MessageDialog(400,150, "Topic Selection",
                            "Please select at least 1 topic to start review",
                            MessageDialog.DialogType.OK);
                }
            });

            // review by exam button
            examSelect.addQuestionPanelListener((questions) -> {
                if(questions.size() > 0){
                    mainPanel.removeAll();
                    mainPanel.setLayout(new BorderLayout());

                    // setting up main question panel
                    QuestionMain qm = new QuestionMain(questions, userAnswers, false);
                    qm.setPanelChangeListener(() -> {
                        mainPanel.setLayout(new GridBagLayout());
                        mainPanel.removeAll();
                        mainPanel.add(examSelect);
                        mainPanel.repaint();
                        mainPanel.revalidate();
                        examSelect.refresh();
                    });

                    mainPanel.add(qm, BorderLayout.CENTER);
                    mainPanel.repaint();
                    mainPanel.revalidate();
                }
            });

            mainPanel.removeAll();
            mainPanel.add(examSelect);
            mainPanel.repaint();
            mainPanel.revalidate();
        });

        // Adding components
        // revBtn
        mainPanel.add(revBtn);

        // examBtn
        mainPanel.add(examBtn);

    }
}
