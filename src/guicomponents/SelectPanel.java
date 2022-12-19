package guicomponents;

import databasemanagement.Question;
import databasemanagement.Topic;
import databasemanagement.UserAnswer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class SelectPanel extends JPanel {
    private ArrayList<Question> questions;
    private ArrayList<Topic> topics;
    private ArrayList<UserAnswer> userAnswers;
    private Timer animation;

    public SelectPanel(ArrayList<Question> questions, ArrayList<Topic> topics, ArrayList<UserAnswer> userAnswers){
        this.questions = questions;
        this.topics = topics;
        this.userAnswers = userAnswers;
    }

    public void sampleProgressBar(JProgressBar[] progs, JLabel[] lbls){
        for(int i = 0; i < progs.length; i++)
            updateProgress(progs[i], new Random().nextInt(progs[i].getMaximum() + 1), lbls[i]);
    }

    public void getProgress(JProgressBar progressBar, JLabel lbl, int topicId){
        int count = 0;
        int total = 0;

        // counting from userAnswers
        for(UserAnswer ua : userAnswers){
            if(ua.getQuestion().getTopicId() == topicId)
                count++;
        }

        // total from questions
        for(Question q : questions){
            if(q.getTopicId() == topicId)
                total++;
        }

        // setting progress bar values
        if(count >= total)
            updateProgress(progressBar, progressBar.getMaximum(), lbl);
        else
            updateProgress(progressBar, count * 100, lbl);;
    }



    public void updateProgress(JProgressBar p, int val, JLabel lbl){
        // speed of animation
        int speed = 10;

        if(p.getValue() < val) {
            // increasing progress bar
            animation = new Timer(1, (ActionEvent arg0) -> {
                if (p.getValue() + speed >= val && p.getValue() + speed < p.getMaximum())
                    animation.stop();
                else
                    p.setValue(p.getValue() + speed);

                // adjust decimal format
                DecimalFormat df = new DecimalFormat("00.00");
                lbl.setText(df.format(p.getPercentComplete()*100) + "%");
            });
            animation.start();
        }else if(p.getValue() > val){
            // decreasing progress bar
            animation = new Timer(1, (ActionEvent arg0) -> {
                if (p.getValue() - speed <= val && p.getValue() - speed > p.getMinimum())
                    animation.stop();
                else
                    p.setValue(p.getValue() - speed);

                // adjust decimal format
                DecimalFormat df = new DecimalFormat("00.00");
                lbl.setText(df.format(p.getPercentComplete()*100) + "%");
            });
            animation.start();
        }
    }

    public void refresh(JProgressBar[] topicPrgBar, JLabel[] percentLbls){
        for(int i  = 0; i < topics.size(); i++){
            getProgress(topicPrgBar[i], percentLbls[i], topics.get(i).getId());
        }
    }
}
