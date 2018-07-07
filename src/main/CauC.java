package main;

import model.Attribute;
import utils.Utils;

import java.io.*;
import java.util.*;

public class CauC extends BaseApplication {

    public static File getFileFromInput() {
        File file = new File("heart-integration-auto.arff");
        if (file.exists()) {
            return file;
        }
        return null;
    }

    public static List<String> getAllData(File file) {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith("@") && !line.startsWith("%")) {
                    lines.add(line);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static List<Attribute> createHashFromListAttributes(File file) {
        List<Attribute> map = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader =
                    new BufferedReader(
                            new FileReader(file));

            String line = bufferedReader.readLine();
            int key = 0;
            while (line != null) {
                if (line.startsWith("@")) {
                    String[] components = line.split(" ");

                    if (components[0].contains("@attribute")) {
                        Attribute attr = new Attribute();
                        attr.setIndex(key);
                        attr.setName(components[1]);
                        List<String> values = new ArrayList<>();
                        if (components[2].equals("numeric")) {
                            attr.setType(Utils.TYPE_NUMERIC);
                            values.add(components[2]);
                            attr.setValues(values);
                        } else {
                            attr.setType(Utils.TYPE_NOMIMAL);
                            components[2] = components[2].replace("{", "");
                            components[2] = components[2].replace("}", "");
                            String[] componentValues = components[2].split(",");
                            values.addAll(Arrays.asList(componentValues));
                            attr.setValues(values);
                        }
                        map.add(attr);
                        key++;
                    }
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert bufferedReader != null;
                bufferedReader.close();
            }  catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static double calculateMeanFromNumericField(List<String> lines, Attribute attribute) {
        double totalSum = 0;
        int key = attribute.getIndex();
        int size = 0;

        for (String line : lines) {
            String[] values = line.split(",");
            if (values.length > key && !values[key].contains("?") && !line.isEmpty()) {
                totalSum += Double.parseDouble(values[key]);
                size++;
            }
        }
        return totalSum / size;
    }

    public static String getHighestValueFromNomimalField(
            String valueStr, List<String> lines, List<Attribute> map) {
        String higestValue = "";
        int max = 0;
        for (Attribute attribute : map) {
            if (attribute.getName().equals(valueStr)) {
                for (String attr : attribute.getValues()) {
                    int quantity = countNumberOfValue(attr, lines);
                    if (max == 0 || quantity > max) {
                        max = quantity;
                        higestValue = attr;
                    }
                }
            }
        }
        return higestValue;
    }

    private static int countNumberOfValue(String valueKey, List<String> lines) {
        int count = 0;
        for (String line : lines) {
            if (line.contains(valueKey)) {
                count++;
            }
        }
        return count;
    }

    public static void cleanupFiles() {
        File file = getFileFromInput();
        List<Attribute> attributes = createHashFromListAttributes(file);
        List<String> data = getAllData(file);
        List<String> header = extractHeader(file);

        data = replaceMissingValues(data, attributes);
        List<String> combined = combinedFile(header, data);
        writeFileOutput("heart-cleaned-auto.arff", combined);
    }

    private static List<String> extractHeader(File file) {
        return readAttribute(file);
    }

    private static List<String> combinedFile(List<String> header, List<String> data) {
        List<String> combined = new ArrayList<>();
        combined.addAll(header);
        combined.addAll(data);
        return combined;
    }

    public static List<String> replaceMissingValues(List<String> lines, List<Attribute> map) {
        List<String> cleanupLines = lines;
        for (Attribute attribute : map) {
            cleanupLines = replaceMissingValuesWithAttribute(cleanupLines, attribute, map);

        }
        return cleanupLines;
    }

    public static List<String> replaceMissingValuesWithAttribute(List<String> lines, Attribute attribute, List<Attribute> map) {

        List<String> cleanupLines = lines;
        Object object;
        if (attribute.getType().equals(Utils.TYPE_NUMERIC)) {
            object = calculateMeanFromNumericField(lines, attribute);
        } else {
            object = getHighestValueFromNomimalField(attribute.getName(), lines, map);
        }

        for (String line : lines) {
            int indexLine = lines.indexOf(line);
            String[] components = line.split(",");
            String cleanup = "";
            int key = attribute.getIndex();
            if (components.length <= key || !components[key].equals("?")) continue;
            if (object != null) {
                components[key] = String.valueOf(object);
            }
            for (String component : components) {
                cleanup += component + ",";
            }

            cleanup = cleanup.substring(0, cleanup.length() - 1);
            cleanupLines.set(indexLine, cleanup);
        }
        return cleanupLines;
    }


}
