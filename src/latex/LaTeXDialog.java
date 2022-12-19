package latex;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class LaTeXDialog extends JDialog {
    private JLabel previewIcon;
    private LaTeXMathComponent latexComponent, curComponent;
    private int inputAmount;
    private JTextField[] inputFields;
    private String[] iLabels;

    public LaTeXDialog(String title, LaTeXMathComponent latexComponent, JTextField textField){
        // dialog setup
        super(null, title, ModalityType.APPLICATION_MODAL);
        setSize(new Dimension(500,250));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // variables
        String[] inputs = latexComponent.getRawInputs();
        this.latexComponent = latexComponent;
        inputAmount = inputs.length;

        // setting up input label
        iLabels = new String[inputs.length];
        for (int i = 0; i < iLabels.length; i++)
            iLabels[i] = Character.toString((char)('a' + i));

        // components
        JPanel panel = new JPanel();
        curComponent = new LaTeXMathComponent(latexComponent.getLatexFormat(), 36, iLabels);
        previewIcon = curComponent.getLabel();
        inputFields = new JTextField[inputs.length];
        JLabel[] fieldLbls = new JLabel[inputs.length];
        JButton submitBtn = new JButton("Insert");
        JPanel inputPanel = new JPanel();

        // component setup
        for(int i = 0; i < inputs.length; i++){
            // field labels
            fieldLbls[i] = new LaTeXMathComponent(iLabels[i] + ":", 18).getLabel();

            // input fields
            inputFields[i] = new JTextField("");
            inputFields[i].setPreferredSize(new Dimension(200, 20));
            inputFields[i].setText(inputs[i]);

            // input field document listener
            inputFields[i].getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    updatePreview();
                }

                public void removeUpdate(DocumentEvent e) {
                    updatePreview();
                }

                public void changedUpdate(DocumentEvent e) {
                    updatePreview();
                }
            });
        }

        // preview icon scroll
        JScrollPane iconScroll = new JScrollPane(previewIcon);
        iconScroll.setPreferredSize(new Dimension(200,150));

        // initial update
        updatePreview();

        // submit button action listener
        submitBtn.addActionListener((arg0) -> {
            textField.setText(textField.getText() + curComponent.getString());
            dispose();
        });

        // layout
        panel.setLayout(new GridBagLayout());
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1;
        gc.weighty = 1;

        // inputPanel
        gc.insets = new Insets(2,2,2,2);

        // input panel elements
        for(int i = 0; i < inputs.length; i++){
            gc.gridy++;
            // field labels
            gc.gridx = 0;
            gc.anchor = GridBagConstraints.LINE_END;
            inputPanel.add(fieldLbls[i], gc);

            // fields
            gc.gridx = 1;
            gc.anchor = GridBagConstraints.LINE_START;
            inputPanel.add(inputFields[i], gc);

        }
        // adding previewIcon
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(iconScroll, gc);

        // adding input panel
        gc.gridx = 1;
        panel.add(inputPanel, gc);

        // adding submit button
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.gridy = 1;
        panel.add(submitBtn, gc);

        // adding components
        add(panel);

        // making it visible
        setVisible(true);
    }

    private void updatePreview(){
        String[] fieldInputs = new String[inputAmount];
        for(int i = 0; i < inputAmount; i++){
            if(inputFields[i].getText().isEmpty())
                fieldInputs[i] = iLabels[i];
            else
                fieldInputs[i] = inputFields[i].getText();
        }


        curComponent = new LaTeXMathComponent(latexComponent.getLatexFormat(), 36, fieldInputs);
        previewIcon.setIcon(curComponent.getIcon());
    }
}
