package ibmathprepper;

import databasemanagement.*;
import guicomponents.MessageDialog;
import guicomponents.ProgressDialog;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            // setting up serializers
            DatabaseSerialiser<Question> questionSerializer = new DatabaseSerialiser<>("questions");
            DatabaseSerialiser<Topic> topicSerializer = new DatabaseSerialiser<>("topics");
            DatabaseSerialiser<Exam> examSerializer = new DatabaseSerialiser<>("exams");
            DatabaseSerialiser<UserAnswer> userAnswerSerializer = new DatabaseSerialiser<>("useranswers");

            // retry status
            boolean retry = false;

            // check for files
            boolean q = questionSerializer.exists();
            boolean t = topicSerializer.exists();
            boolean e = examSerializer.exists();
            boolean u = userAnswerSerializer.exists();

            System.out.printf("question: %b, topic: %b, exam: %b, user answers:%b\n", q, t, e, u);

            // if all files exist
            final ProgressDialog prog = new ProgressDialog("Recovering Missing Files", "Connecting");
            if(q && t && e && u){
                prog.setTitleText("Updating");
                SwingWorker<Boolean, String> sw = new SwingWorker<Boolean, String>() {
                    protected Boolean doInBackground() throws Exception {
                        try {
                            // getting connection
                            DatabaseAccessor dbAccess = new DatabaseAccessor("remotemysql.com", "Gg3LUWCPmy", "Gg3LUWCPmy", "5X8Es37piO");
                            dbAccess.connect();
                            prog.setProgressText("Downloading Questions");
                            serializeQuestion(dbAccess);

                            prog.setProgressText("Downloading Topics");
                            serializeTopic(dbAccess);

                            prog.setProgressText("Downloading Exams");
                            serializeExam(dbAccess);

                            // done updating
                            dbAccess.getCon().close();

                            prog.setProgressText("Finished");
                            prog.dispose();

                            // start mainframe
                            ArrayList<Question> questions = questionSerializer.deserialise();
                            ArrayList<Topic> topics = topicSerializer.deserialise();
                            ArrayList<Exam> exams = examSerializer.deserialise();
                            ArrayList<UserAnswer> userAnswers = userAnswerSerializer.deserialise();
                            new MainFrame(questions, topics, exams, userAnswers);
                        }catch(Exception e1){
                            prog.dispose();
                            int val = new MessageDialog(250,150,"Unable to Connect to Database",
                                    "Continue anyway?",
                                    MessageDialog.DialogType.YES_NO).getValue();

                            if(val == 0){
                                // start mainframe
                                ArrayList<Question> questions = questionSerializer.deserialise();
                                ArrayList<Topic> topics = topicSerializer.deserialise();
                                ArrayList<Exam> exams = examSerializer.deserialise();
                                ArrayList<UserAnswer> userAnswers = userAnswerSerializer.deserialise();
                                new MainFrame(questions, topics, exams, userAnswers);
                            }else{
                                System.exit(0);
                            }
                        }
                        return true;
                    }
                };
                sw.execute();
            }
            else {
                // Showing progress dialog
                if (!q && !t && !e && !u) {
                    prog.setTitleText("Performing First Time Setup");
                    // perform first time setup
                    SwingWorker<Boolean, String> sw = new SwingWorker<Boolean, String>() {
                        protected Boolean doInBackground() throws Exception {
                            try {
                                // getting connection
                                DatabaseAccessor dbAccess = new DatabaseAccessor("remotemysql.com", "Gg3LUWCPmy", "Gg3LUWCPmy", "5X8Es37piO");
                                dbAccess.connect();
                                prog.setProgressText("Downloading Questions");
                                serializeQuestion(dbAccess);

                                prog.setProgressText("Downloading Topics");
                                serializeTopic(dbAccess);

                                prog.setProgressText("Downloading Exams");
                                serializeExam(dbAccess);

                                prog.setProgressText("Setup Local Answers");
                                serializeUserAnswer(dbAccess);

                                // done updating
                                dbAccess.getCon().close();

                                prog.setProgressText("Finished");
                                prog.dispose();

                                // start mainframe
                                ArrayList<Question> questions = questionSerializer.deserialise();
                                ArrayList<Topic> topics = topicSerializer.deserialise();
                                ArrayList<Exam> exams = examSerializer.deserialise();
                                ArrayList<UserAnswer> userAnswers = userAnswerSerializer.deserialise();
                                new MainFrame(questions, topics, exams, userAnswers);
                            }catch(Exception e1){
                                new MessageDialog(250,150,"Unable to Connect to Database",
                                        "Required files have not been retrieved",
                                        MessageDialog.DialogType.OK);
                            }
                            return true;
                        }
                    };
                    sw.execute();
                } else {
                    SwingWorker<Boolean, String> sw = new SwingWorker<Boolean, String>() {
                        protected Boolean doInBackground() throws Exception {
                            try {
                                // connecting to database
                                DatabaseAccessor db = new DatabaseAccessor("remotemysql.com", "Gg3LUWCPmy", "Gg3LUWCPmy", "5X8Es37piO");
                                db.connect();
                                if (!q) {
                                    prog.setProgressText("Downloading Questions");
                                    serializeQuestion(db);
                                }
                                if (!t) {
                                    prog.setProgressText("Downloading Topics");
                                    serializeTopic(db);
                                }
                                if (!e) {
                                    prog.setProgressText("Downloading Exams");
                                    serializeExam(db);
                                }
                                if (!u) {
                                    prog.setProgressText("Setup Local Answers");
                                    serializeUserAnswer(db);
                                }
                                db.getCon().close();

                                prog.setProgressText("Finished");
                                prog.dispose();

                                // start mainframe
                                ArrayList<Question> questions = questionSerializer.deserialise();
                                ArrayList<Topic> topics = topicSerializer.deserialise();
                                ArrayList<Exam> exams = examSerializer.deserialise();
                                ArrayList<UserAnswer> userAnswers = userAnswerSerializer.deserialise();
                                new MainFrame(questions, topics, exams, userAnswers);
                            }catch (Exception e1){
                                new MessageDialog(250,150,"Unable to Connect to Database",
                                        "Required files have not been retrieved",
                                        MessageDialog.DialogType.OK);
                            }

                            return true;
                        }
                    };
                    sw.execute();
                }
            }
        });
    }

    public static void serializeQuestion(DatabaseAccessor dba){
        // selecting table
        ArrayList<String[]> qArr = dba.selectTable("questions");

        // prepare for serialization
        ArrayList<Question> questions = DBArrayManager.toQuestionArray(qArr);

        // setup serializer
        DatabaseSerialiser<Question> questionSerializer = new DatabaseSerialiser<>("questions", questions);

        // serialise
        questionSerializer.serialize();
    }

    public static void serializeTopic(DatabaseAccessor dba){
        // selecting table
        ArrayList<String[]> tArr = dba.selectTable("topics");

        // prepare for serialization
        ArrayList<Topic> topics = DBArrayManager.toTopicArray(tArr);

        // setup serializer
        DatabaseSerialiser<Topic> topicSerializer = new DatabaseSerialiser<>("topics", topics);

        // serialise
        topicSerializer.serialize();
    }

    public static void serializeExam(DatabaseAccessor dba){
        // selecting table
        ArrayList<String[]> eArr = dba.selectTable("exams");

        // prepare for serialization
        ArrayList<Exam> exams = DBArrayManager.toExamArray(eArr);

        // setup serializer
        DatabaseSerialiser<Exam> examSerializer = new DatabaseSerialiser<>("exams", exams);

        // serialise
        examSerializer.serialize();
    }

    public static void serializeUserAnswer(DatabaseAccessor dba){
        // creating empty array
        ArrayList<UserAnswer> uArr = new ArrayList<>();

        // setup serializer
        DatabaseSerialiser<UserAnswer> userAnswerSerializer = new DatabaseSerialiser<>("useranswers", uArr);

        // serialise
        userAnswerSerializer.serialize();
    }
}
