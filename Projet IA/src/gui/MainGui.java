package gui;

import data.GoodOutputs;
import data.ReadWriteFile;
import gui.components.CustomPanel;
import gui.components.DrawingPanel;
import neural.Train;
import neural.TrainingSet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainGui extends JFrame {

    private final int RESOLUTION = 20;

    private Train networkTrainer;

    private JPanel mainPanel;
    private DrawingPanel drawingPanel;
    private CustomPanel resultPanel;
    private JLabel labelResultat;
    private JButton clearButton;
    private JButton trainButton;
    private JButton transformButton;
    private JButton helpButton;
    private JButton trainNetworkButton;
    private JTextField trainingSetsAmount;
    private JComboBox<String> trainAsCombo;
    private JTextArea outputTextArea;
    private int resultat;
    
    public static void main(String[] args) {
        new MainGui();
    }

    public MainGui() {
    super("IA ");

        networkTrainer = new Train();

        setMainPanel();
        setLeftSide();
        setCenterArea();
        setOutputPanel();
        setRightSide(resultat);
        setOnClicks();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(1260, 500));
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        setContentPane(mainPanel);
    }

    private void setLeftSide() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(410, 440));

        drawingPanel = new DrawingPanel(400, 400, RESOLUTION);

        panel.add(drawingPanel);

        mainPanel.add(panel);
    }

    private void setCenterArea() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setPreferredSize(new Dimension(200, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        trainNetworkButton = new JButton("Train X times:");
        trainingSetsAmount = new JFormattedTextField("5000");
        trainingSetsAmount.setMaximumSize(new Dimension(100, 30));
        trainingSetsAmount.setPreferredSize(new Dimension(100, 30));
        centerPanel.add(trainNetworkButton, gbc);
        centerPanel.add(trainingSetsAmount, gbc);

        centerPanel.add(Box.createVerticalStrut(50));

        helpButton = new JButton("HELP");
        centerPanel.add(helpButton, gbc);

        centerPanel.add(Box.createVerticalStrut(50));

        transformButton = new JButton(">>");
        centerPanel.add(transformButton, gbc);

        centerPanel.add(Box.createVerticalStrut(50));

        clearButton = new JButton("Clear");
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(clearButton, gbc);

        centerPanel.add(Box.createVerticalStrut(50));

        centerPanel.add(new JLabel("Train as:", SwingConstants.CENTER), gbc);

        trainAsCombo = new JComboBox<>(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
        trainAsCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        trainAsCombo.setMaximumSize(new Dimension((int) trainAsCombo.getPreferredSize().getWidth(), 30));
        centerPanel.add(trainAsCombo, gbc);

        trainButton = new JButton("Train");
        centerPanel.add(trainButton, gbc);

        mainPanel.add(centerPanel);
    }

    private void setRightSide(int res) {
        JPanel panel = new JPanel();
        labelResultat = new JLabel();
        labelResultat.setText(String.valueOf(res));
        Font font = new Font("courier", Font.BOLD,160);
        labelResultat.setFont(font);
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 100, 0, 100));
        panel.add(labelResultat);
        mainPanel.add(panel);
    }

    private void setOutputPanel() {
        JPanel outputPanel = new JPanel();
        outputPanel.setPreferredSize(new Dimension(200, 430));

        outputTextArea = new JTextArea();
        outputTextArea.setPreferredSize(new Dimension(200, 430));
        outputPanel.add(outputTextArea);

        mainPanel.add(outputPanel);
    }

    private void setOnClicks() {
        clearButton.addActionListener(e -> drawingPanel.clear());

        trainButton.addActionListener(e -> {
            String letter = (String) trainAsCombo.getSelectedItem();
            networkTrainer.addTrainingSet(new TrainingSet(drawingPanel.getPixels(), GoodOutputs.getInstance().getGoodOutput(letter)));
            ReadWriteFile.saveToFile(drawingPanel.getPixels(), letter);
        });

        transformButton.addActionListener(e -> {
            networkTrainer.setInputs(drawingPanel.getPixels());

            ArrayList<Double> outputs = networkTrainer.getOutputs();
            int index = 0;
            for (int i = 0; i < outputs.size(); i++) {
                if (outputs.get(i) > outputs.get(index))
                    index = i;
            }
            updateTextArea();
            trainAsCombo.setSelectedIndex(index);
        });

        trainNetworkButton.addActionListener(e -> {
            int number = 0;
            try {
                number = Integer.parseInt(trainingSetsAmount.getText());
            } catch (Exception x) {
                JOptionPane.showMessageDialog(this, "Wrong input", "ERROR", JOptionPane.PLAIN_MESSAGE);
            }

            networkTrainer.train(number);
        });

    }

    private void updateTextArea() {
    	HashMap<Integer,Double> hash = new HashMap<Integer,Double>();
        StringBuilder sb = new StringBuilder();
        ArrayList<Double> outputs = networkTrainer.getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            int letterValue = i;
            sb.append(letterValue);
            double value = outputs.get(i);
            if (value < 0.01)
                value = 0;
            if (value > 0.99)
                value = 1;

            value *= 1000;
            int x = (int) (value);
            value = x / 1000.0;
            
            sb.append("\t " + value);
            sb.append("\n");           
            hash.put(i, value);
            
        }
        Map.Entry<Integer,Double> maxEntry = null;
        for (Map.Entry<Integer,Double> entry : hash.entrySet())
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)          
                maxEntry = entry;
        resultat = maxEntry.getKey();
        outputTextArea.setText(sb.toString());
        labelResultat.setText(String.valueOf(resultat));
    }

}
