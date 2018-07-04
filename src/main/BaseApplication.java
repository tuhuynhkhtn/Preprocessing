package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseApplication {

    public static List<String> readAttribute(File file) {
        List<String> lines = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader =
                    new BufferedReader(
                            new FileReader(file));

            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.startsWith("@")) {
                    lines.add(line);
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
        return lines;
    }

    public static List<String> readData(File file) {
        List<String> lines = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader =
                    new BufferedReader(
                            new FileReader(file));

            String line = bufferedReader.readLine();
            while (line != null) {
                if (!line.startsWith("%") && !line.startsWith("@")) {
                    lines.add(line);
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
        return lines;
    }

}
