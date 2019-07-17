package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import data.GoodOutputs;
import data.ReadWriteFile;
import neural.Train;
import neural.TrainingSet;
import ui.components.DrawingPanel;

public class Main extends JFrame {


    private Train networkTrainer;

    private JPanel mainPanel;
    private DrawingPanel drawingPanel;
    private JLabel labelResultat;
    private JButton clearButton;
    private JButton trainButton;
    private JButton transformButton;
    private JButton trainNetworkButton;
    private JTextField trainingSetsAmount;
    private JComboBox<String> trainAsCombo;
    private int resultat;
    
    public static void main(String[] args) {
        new Main();
    }

    public Main() {

        networkTrainer = new Train();
        setMainPanel();
        setLeft();
        setRightSide(resultat);
        setOnClicks();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(1000, 500));
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        setContentPane(mainPanel);
    }

    private void setLeft() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(550, 440));
        drawingPanel = new DrawingPanel(400, 400, 20);
        panel.add(drawingPanel);
        trainNetworkButton = new JButton("Nb phases de training:");
        trainingSetsAmount = new JFormattedTextField("5000");
        trainingSetsAmount.setMaximumSize(new Dimension(100, 30));
        trainingSetsAmount.setPreferredSize(new Dimension(100, 30));
        panel.add(trainNetworkButton);
        panel.add(trainingSetsAmount);
        mainPanel.add(panel);
        trainAsCombo = new JComboBox<>(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
        trainAsCombo.setMaximumSize(new Dimension((int) trainAsCombo.getPreferredSize().getWidth(), 30));
        panel.add(trainAsCombo);
        trainButton = new JButton("Save");
        panel.add(trainButton);
        clearButton = new JButton("Reset");
        panel.add(clearButton);
        transformButton = new JButton(">>>>");
        panel.add(transformButton);
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

    private void setOnClicks() {
        clearButton.addActionListener(e -> drawingPanel.clear());

        trainButton.addActionListener(e -> {
        	System.out.println("save");
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
            value *= 1000;
            int x = (int) (value);
            value = x / 1000.0;
            
            sb.append(" ---- " + value);
                  
            hash.put(i, value);        
        }
        System.out.println("Numéro   : / ---- 0   ----   1   ----   2   ----   3   ----   4   ----   5   ----   6   ----   7   ----   8   ----   9" );
        System.out.println("Résultat : " + sb);
        Map.Entry<Integer,Double> maxEntry = null;
        for (Map.Entry<Integer,Double> entry : hash.entrySet())
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)          
                maxEntry = entry;
        // Resultat
        resultat = maxEntry.getKey();
        labelResultat.setText(String.valueOf(resultat));

    }

}
