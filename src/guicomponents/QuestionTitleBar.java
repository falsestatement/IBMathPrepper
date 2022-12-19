package guicomponents;

import databasemanagement.Question;
import listeners.PanelChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

public class QuestionTitleBar extends JPanel {
    private Timer countdown;
    private PanelChangeListener panelChangeListener;
    private long currentTime;
    private boolean timerRunning;
    private long startTime;
    private long duration;
    private JLabel timeLbl;
    float maxHue, minHue, maxTime, minTime, curHue;
    float totalTime;

    public QuestionTitleBar(Question q){
        // declare components / variables
        JLabel title = new JLabel(q.getName());
        JButton backBtn = new JButton("Back");
        totalTime = q.getPointVal() * 60000;
        currentTime = (long)totalTime;
        timeLbl = new JLabel(new SimpleDateFormat("mm:ss:SSS").format(currentTime));
        boolean isExam = q.isExam();
        timerRunning = false;
        startTime = System.currentTimeMillis();
        duration = currentTime;
        curHue = 1f/3f;

        // colour
        minHue = 0;
        maxHue = 1f/3f;
        minTime = 0;
        maxTime = q.getPointVal() * 60000;

        // time label setup
        timeLbl.setFont(new Font("Helvetica", Font.PLAIN, 24));
        timeLbl.setForeground(Color.getHSBColor(curHue, 1, 0.75f));
        timeLbl.setOpaque(true);
        timeLbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // back button setup
        backBtn.addActionListener((arg0) -> {
            if(panelChangeListener != null)
                panelChangeListener.panelChangePerformed();
        });

        // title setup
        title.setFont(new Font("Helvetica", Font.BOLD, 32));

        // panel setup
        setLayout(new GridBagLayout());

        // adding components
        GridBagConstraints gc = new GridBagConstraints();

        // backBtn
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(20,20,5,20);
        add(backBtn, gc);

        // title
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0, 20, 5, 20);
        gc.gridy = 1;
        gc.gridwidth = 2;
        add(title, gc);

        // timer
        if(isExam){
            gc.gridy = 0;
            gc.gridwidth = 1;
            gc.anchor = GridBagConstraints.LINE_END;
            gc.insets = new Insets(20,20,5,20);

            timeLbl.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);

                    if(timerRunning){
                        countdown.stop();
                        countdown = null;
                        setTimerRunning(!timerRunning);
                    }
                    else if(currentTime > 0){
                        startTime = System.currentTimeMillis();
                        duration = currentTime;
                        countdownStart();
                        setTimerRunning(!timerRunning);
                    }
                }

                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    timeLbl.setForeground(Color.getHSBColor(curHue, 1, 0.5f));
                }

                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    timeLbl.setForeground(Color.getHSBColor(curHue, 0.9f, 0.9f));
                }

                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    timeLbl.setForeground(Color.getHSBColor(curHue, 0.9f, 0.9f));
                }

                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    timeLbl.setForeground((Color.getHSBColor(curHue, 1, 0.75f)));
                }
            });
            timeLbl.setVisible(true);
            add(timeLbl, gc);
        }else {
            gc.gridy = 0;
            gc.gridwidth = 1;
            gc.anchor = GridBagConstraints.LINE_END;
            gc.insets = new Insets(20,20,5,20);
            timeLbl.setText(" ");
            add(timeLbl, gc);
        }
    }

    public void setPanelChangeListener(PanelChangeListener panelChangeListener){
        this.panelChangeListener = panelChangeListener;
    }

    public void setCurrentTime(long time){
        currentTime = time;
    }

    public void setTimerRunning(boolean b){
        timerRunning = b;
        System.out.println(timerRunning);
    }

    public void countdownStart(){
        countdown = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // calculated time lapsed
                long curTime = System.currentTimeMillis();
                long timeLapsed = curTime - startTime;

                setCurrentTime(duration - timeLapsed);

                // label setup
                SimpleDateFormat df = new SimpleDateFormat("mm:ss:SSS");
                timeLbl.setText(df.format(currentTime));

                // label colours
                curHue = ((float)currentTime - minTime) / (maxTime - minTime) * (maxHue - minHue) + minHue;

                timeLbl.setForeground(Color.getHSBColor(curHue, 1, 0.75f));

                // once time finished
                if(timeLapsed >= duration){
                    countdown.stop();
                    currentTime = 0;
                    timeLbl.setText(new SimpleDateFormat("mm:ss:SSS").format(currentTime));
                }
            }
        });

        countdown.start();
    }
}
