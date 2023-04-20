package io;

import generator.Formula.Formula;

import java.io.*;
import java.util.ArrayList;
import java.io.FileWriter;

import com.opencsv.CSVWriter;

/**
 * A class that represents a serializer that handles the storing and loading of formulas to files.
 */
public class Serializer {
    
    private static CSVWriter writer;

    /**
     * A method that stores a single formula to a file.
     * @param formula The formula that should be stored.
     * @param fileName The name of the file that the formula should be stored in.
     */
    public static void saveFormulasSingle(Formula formula, String fileName) {
        File saveDirectory = new File("formulas");
        saveDirectory.mkdir();
        try (FileOutputStream fileOutputStream = new FileOutputStream(saveDirectory + File.separator + fileName, true);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(formula);
        } catch (Exception e) {
            System.out.println("File could not be found");
        }
    }

    /**
     * A method that stores several formulas to a file.
     * @param formula A list of formulas that should be stored.
     * @param fileName The name of the file that the formulas should be stored in.
     */
    public static void saveFormulas(ArrayList<Formula> formula, String fileName) {
        File saveDirectory = new File("formulas");
        saveDirectory.mkdir();
        try (FileOutputStream fileOutputStream = new FileOutputStream(saveDirectory + File.separator + fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(formula);
        } catch (Exception e) {
            System.out.println("File could not be found");
        }
    }

    /**
     * A method that loads formulas from a specific file.
     * @param fileName The name of the file that the formulas should be loaded from.
     * @return A list of the formulas that were loaded.
     */
    public static ArrayList<Formula> loadFormulas(String fileName) {
        File saveDirectory = new File("formulas");
        try (FileInputStream fileInputStream = new FileInputStream(saveDirectory + File.separator + fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (ArrayList<Formula>) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println("Loading did not work: " + fileName);
            return null;
        }
    }

    /**
     * A method that generates a csv file to store the run time and memory usage of formulas.
     * @param fileName The name of the file that will be generated.
     */
    public static void makeFile(String fileName) {
        File file = new File("data/" + fileName);
        try {
            FileWriter outputfile = new FileWriter(file);
            writer = new CSVWriter(outputfile);
            String[] header = { "Formula", "Time (ns)", "RAM usage (bytes)", "Tautology"};
            writer.writeNext(header);
        }
        catch (IOException e) {
            System.out.println("File-writing did not work: " + fileName);
        }
    }

    /**
     * A method that stores the run time, RAM usage, and whether it is a tautology of a formula to the csv file.
     * @param formulaString The string that represents the formula.
     * @param execTime The run time of the formula.
     * @param memory The RAM usage of the formula.
     * @param tautology A boolean variable indicating whether the formula is a tautology.
     */
    public static void saveFormulaData(String formulaString, long execTime, long memory, boolean tautology){
        String[] data1 = {formulaString, String.valueOf(execTime), String.valueOf(memory), String.valueOf(tautology)};
        writer.writeNext(data1);
    }

    /**
     * This method closes the csv-file after all the formula data was stored.
     */
    public static void closeFile() {
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("The CSV-file could not be closed.");
        }
    }
}
