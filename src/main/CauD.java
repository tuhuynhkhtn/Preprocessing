package main;

import model.Attribute;
import org.w3c.dom.Attr;
import utils.Utils;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CauD extends BaseApplication {

    public static double calculateMeanFromNumericField(List<String> lines, Attribute attribute) {
        double totalSum = 0;
        int key = attribute.getIndex();
        int size = 0;

        for (String line : lines) {
            String[] values = line.split(",");
            if (line.contains("@relation")) continue;
            if (values.length > key && !values[key].contains("?") && !line.isEmpty()) {
                totalSum += Double.parseDouble(values[key]);
                size++;
            }
        }
        return totalSum / size;
    }

    private static double calculateStdDev(List<String> lines, Attribute attribute) {
        double mean = calculateMeanFromNumericField(lines, attribute);
        int key = attribute.getIndex();
        double sum = 0;
        int size = 0;
        for (String line : lines) {
            String[] values = line.split(",");
            if (values.length > key && !values[key].contains("?") && !line.isEmpty()) {
                double value = Double.parseDouble(values[key]);
                double minusResult = value - mean;
                sum += Math.pow(minusResult, 2);
                size++;
            }
        }
        double stdDevValue = Math.sqrt(sum / (size - 1));
        return stdDevValue;
    }

    private static String combinedStringComponents(String[] componnets) {
        StringBuilder newStrBuilder = new StringBuilder();
        for (String str : componnets) {
            newStrBuilder.append(str).append(",");
        }
        String newStr = newStrBuilder.toString();
        newStr = newStr.substring(0, newStr.length() - 1);
        return newStr;
    }

    private static List<String> standardlizedAttribute(List<String> lines, Attribute attribute) {
        double mean = calculateMeanFromNumericField(lines, attribute);
        double stdDev = calculateStdDev(lines, attribute);
        int key = attribute.getIndex();
        List<String> results = new ArrayList<>();
        String newStr = "";
        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.CEILING);

        for (String line : lines) {
            String[] values = line.split(",");
            if (values.length > key && !values[key].contains("?") && !line.isEmpty()) {
                double value = Double.parseDouble(values[key]);
                double stdlizedValue = (value - mean) / stdDev;
                values[key] = df.format(stdlizedValue);
                newStr  = combinedStringComponents(values);
            }
            results.add(newStr);
        }
        return results;
    }

    private static List<String> standardlizedAllAtrributes(List<String> lines, List<Attribute> attributes) {
        List<String> stardardlizedResult = lines;
        for (Attribute attribute : attributes) {
            if (attribute.getType().equals(Utils.TYPE_NUMERIC)) {
                stardardlizedResult = standardlizedAttribute(stardardlizedResult, attribute);
            }
        }
        return stardardlizedResult;
    }

    public static void standardlized() {
        File file = getFileFromInput();
        if (file == null) return;
        List<Attribute> attributes = CauC.createHashFromListAttributes(file);
        List<String> data = readData(file);
        List<String> headers = readAttribute(file);
        List<String> result = standardlizedAllAtrributes(data, attributes);
        List<String> combinedResult = combinedFile(headers, result);
        writeFileOutput("heart-normal-auto.arff", combinedResult);
    }

    private static List<String> combinedFile(List<String> header, List<String> data) {
        List<String> combined = new ArrayList<>();
        combined.addAll(header);
        combined.addAll(data);
        return combined;
    }

    public static File getFileFromInput() {
        File file = new File("heart-cleaned-auto.arff");
        if (file.exists()) {
            return file;
        }
        return null;
    }

}
