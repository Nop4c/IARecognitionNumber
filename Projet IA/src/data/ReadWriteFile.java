package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import neural.TrainingSet;

public class ReadWriteFile {

    public static ArrayList<TrainingSet> readTrainingSets() {
        ArrayList<TrainingSet> trainingSets = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            for (ArrayList<Integer> list : readFromFile("/" + i + ".txt")) {
                trainingSets.add(new TrainingSet(list, GoodOutputs.getInstance().getGoodOutput(String.valueOf(i))));
            }
        }
        return trainingSets;
    }

    private static ArrayList<ArrayList<Integer>> readFromFile(String filename) {
        ArrayList<ArrayList<Integer>> inputs = new ArrayList<>();

        try {
            System.out.println(filename);
            InputStream in = ReadWriteFile.class.getClass().getResourceAsStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = reader.readLine()) != null) {
                ArrayList<Integer> input = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    int value = 0;
                    try {
                        value = Integer.parseInt(String.valueOf(line.charAt(i)));
                    } catch (Exception e) {
                    }
                    input.add(value);
                    
                }
                inputs.add(input);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputs;
    }

    public static void saveToFile(ArrayList<Integer> input, String filename) {
        try {
            File file = new File("resources/" + filename + ".txt");
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            for (Integer i : input) {
                pw.write(Integer.toString(i));
            }
            pw.write("\n");
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
