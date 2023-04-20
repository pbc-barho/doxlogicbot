package io;

import generator.formula.Formula;

import java.io.*;
import java.util.ArrayList;

/**
 * A class that represents a serializer that handles the storing and loading of formulas to files.
 */
public class Serializer {

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
        } catch (Exception ignored) {}
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
        } catch (Exception ignored) {}
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
            return null;
        }
    }
}
