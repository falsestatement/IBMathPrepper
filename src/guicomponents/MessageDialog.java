package guicomponents;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;

public class MessageDialog extends JDialog {
    private JButton yesBtn, noBtn;
    private int value;
    public MessageDialog(int width, int height, String title, String message, DialogType type){
        // basic setup
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setTitle(title);
        setSize(new Dimension(width, height));
        setResizable(false);
        setLocationRelativeTo(null);

        // components
        JTextPane msgPane = new JTextPane();
        msgPane.setEditable(false);
        msgPane.setOpaque(false);
        msgPane.setText(message);

        StyledDocument doc = msgPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // layout
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 2;
        gc.insets = new Insets(10,20,20,20);

        add(msgPane, gc);

        switch(type){
            case YES_NO:
                // adding yes / no button
                yesBtn = new JButton("Yes");
                noBtn = new JButton("No");

                yesBtn.addActionListener((arg0) -> {value = 0; dispose();});
                noBtn.addActionListener((arg0) -> {value = 1; dispose();});

                gc.fill = GridBagConstraints.NONE;
                gc.gridwidth = 1;
                gc.gridy = 1;
                gc.anchor = GridBagConstraints.PAGE_START;
                add(yesBtn, gc);

                gc.gridx = 1;
                add(noBtn, gc);
                break;

            case OK_CANCEL:
                // adding yes / no button
                yesBtn = new JButton("Ok");
                noBtn = new JButton("Cancel");

                yesBtn.addActionListener((arg0) -> {value = 0; dispose();});
                noBtn.addActionListener((arg0) -> {value = 1; dispose();});

                gc.fill = GridBagConstraints.NONE;
                gc.gridwidth = 1;
                gc.gridy = 1;
                gc.anchor = GridBagConstraints.PAGE_START;
                add(yesBtn, gc);

                gc.gridx = 1;
                add(noBtn, gc);
                break;

            case OK:
                yesBtn = new JButton("Ok");
                yesBtn.addActionListener((arg0) -> {value = 0; dispose();});

                gc.fill = GridBagConstraints.NONE;
                gc.gridwidth = 1;
                gc.gridy = 1;
                gc.anchor = GridBagConstraints.PAGE_START;
                add(yesBtn, gc);
        }


        setVisible(true);
    }

    public enum DialogType{
        YES_NO,
        OK_CANCEL,
        OK
    }

    public int getValue(){
        return value;
    }
}
