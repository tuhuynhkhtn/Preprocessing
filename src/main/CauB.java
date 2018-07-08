package main;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CauB extends BaseApplication {

    public static List<File> getFiles() {
        List<File> files = new ArrayList<>();
        URL fileHeartC = CauB.class.getResource("/resources/heart-c.arff");
        URL fileHeartH = CauB.class.getResource("/resources/heart-h.arff");
        try {
            File heartC = new File(fileHeartC.toURI());
            File heartH = new File(fileHeartH.toURI());
            if (heartC.exists())
                files.add(heartC);
            if(heartH.exists())
                files.add(heartH);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return files;
    }

    public static void combineFiles(File fileSrc, File fileCop) {
        List<String> stringsSrc = readAttribute(fileSrc);
        List<String> stringsCop = readAttribute(fileCop);
        List<String> formattedLines = formatAtrributes(stringsSrc, stringsCop);

        List<String> dataSrc = readData(fileSrc);
        List<String> dataCop = readData(fileCop);
        List<String> formattedData = combineData(dataSrc, dataCop);
        if (formattedLines == null) return;
        List<String> combinedData = new ArrayList<>();
        combinedData.addAll(formattedLines);
        combinedData.addAll(formattedData);
        writeFileOutput("heart-integration-auto.arff", combinedData);
    }

    private static boolean isContainsComponents(String src, String[] components) {
        for (String component : components) {
            if (component.contains("{") || component.contains("}") || component.contains(" ")) {
                component = component.replace("{", "");
                component = component.replace("}", "");
                component = component.replace(" ", "");
            }
            if (!src.contains(component)) return false;
        }
        return true;
    }

    public static List<String> combineData(List<String> dataSrc, List<String> dataCop) {
        List<String> combinedData = new ArrayList<>();
        for (String srcStr : dataSrc) {
            if (srcStr.lastIndexOf(",") == srcStr.length() - 1) {
                srcStr = srcStr.substring(0, srcStr.length() - 1);
            }
            srcStr = srcStr.replace("'", "");
            combinedData.add(srcStr);
        }

        for (String copStr : dataCop) {
            if (copStr.lastIndexOf(",") == copStr.length() - 1) {
                copStr = copStr.substring(0, copStr.length() - 1);
            }
            copStr = copStr.replace("'", "");
            combinedData.add(copStr);
        }
        return combinedData;
    }

    public static List<String> formatAtrributes(List<String> attrSrc, List<String> attrCop) {
        List<String> formatedLines = new ArrayList<>();
        if (attrSrc.size() != attrCop.size()) {
            System.out.println("The two datasets have different headers");
            return null;
        }

        String header = "@relation  intergration-auto-14-heart-disease";
        formatedLines.add(header);

        for (int i = 1; i < attrSrc.size(); i++) {
            String lineSrc = attrSrc.get(i);
            String lineCop = attrCop.get(i);
            if (lineSrc.equals(lineCop) && lineSrc.contains("real")) {
                lineSrc = lineSrc.replace("'", "");
                int index = lineSrc.indexOf("real");
                lineSrc = lineSrc.substring(0, index);
                lineSrc += "numeric";
                formatedLines.add(lineSrc);
            } else {
                Matcher matcherSrc = Pattern.compile("\\{(.*?)\\}").matcher(lineSrc);
                Matcher matcherCop = Pattern.compile("\\{(.*?)\\}").matcher(lineCop);

                if (matcherSrc.find() && matcherCop.find()) {
                    String strWithinCurlyBracketSrc = matcherSrc.group(0);
                    String strWithinCurlyBracketCop = matcherCop.group(0);

                    String removedSpaceStrSrc = strWithinCurlyBracketSrc.replace(" ", "");
                    removedSpaceStrSrc = removedSpaceStrSrc.replace("'", "");

                    String[] components = strWithinCurlyBracketCop.split(",");

                    if (!strWithinCurlyBracketCop.equals(strWithinCurlyBracketSrc)
                            && !isContainsComponents(strWithinCurlyBracketSrc, components)) {
                        System.out.println("The two datasets have different headers: Attributes differ");
                        return null;
                    } else {
                        lineSrc = lineSrc.replace("'", "");
                        strWithinCurlyBracketSrc = strWithinCurlyBracketSrc.replace("'", "");
                        lineSrc = lineSrc.replace(strWithinCurlyBracketSrc, removedSpaceStrSrc);
                        formatedLines.add(lineSrc);
                    }
                }
            }
        }
        String data = "@data";
        formatedLines.add(data);
        return formatedLines;
    }

}
