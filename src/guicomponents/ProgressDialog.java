package guicomponents;

import javax.swing.*;
import java.awt.*;

public class ProgressDialog extends JFrame {

    private JLabel progressLbl;
    private JProgressBar progressBar;
    private JLabel titleLbl;

    public ProgressDialog(String title, String progressLblText){
        super(title);

        // Setting up JFrame
        setUndecorated(true);
        setResizable(false);
        setSize(new Dimension(400,70));
        setVisible(true);
        setLocationRelativeTo(null);

        // Setting up components
        JPanel panel = new JPanel();
        progressBar = new JProgressBar();
        progressLbl = new JLabel(progressLblText);
        titleLbl = new JLabel(title);

        // setup panel
        panel.setLayout(new GridBagLayout());

        // setup progress bar
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(150, 20));
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);

        // adding components
        // setting up constraints
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(5, 10, 5, 10);

        gc.gridwidth = 2;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.PAGE_END;
        panel.add(titleLbl, gc);

        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        panel.add(progressLbl, gc);

        gc.gridx = 1;
        panel.add(progressBar, gc);

        add(panel);


        // movable window
        ComponentMover cm = new ComponentMover(this, panel);
    }

    public void setProgressText(String text){
        progressLbl.setText(text);
    }

    public void setProgress(int progress){
        progressBar.setValue(progress);
    }

    public int getProgress(){
        return progressBar.getValue();
    }

    public void setTitleText(String title){
        titleLbl.setText(title);
    }
}
