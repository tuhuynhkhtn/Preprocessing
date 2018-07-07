package main;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CauA {

    @SuppressWarnings("resource")
	public static void timKiemChiMuc() {
        System.out.println("1. heart-c.arff \t 2. heart-h.arff");
        Scanner scanner = new Scanner(System.in);
        int value = scanner.nextInt();
        URL fileName = null;
        switch (value) {
            case 1:
                fileName = CauA.class.getResource("/resources/heart-c.arff");
                break;
            case 2:
                fileName = CauA.class.getResource("/resources/heart-h.arff");
                break;
            default:
                break;
        }

        if (fileName != null) {
            try {
                File file = new File(fileName.toURI());
                List<String> lines = readFile(file);
                System.out.println("Chỉ mục tìm kiếm trong file: ");
                int index = scanner.nextInt();
                if (index >= 0 && index < lines.size()) {
                    System.out.println(lines.get(index));
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> readFile(File file) {
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
