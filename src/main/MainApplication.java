package main;

import java.io.File;
import java.util.List;

import model.Attribute;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class MainApplication {

    public static void main(String[] args) {
        //CauA.timKiemChiMuc();
        List<File> files = CauB.getFiles();
        CauB.combineFiles(files.get(0), files.get(1));
        //CauA.timKiemChiMuc();
        File file = CauC.getFileFromInput();
        List<Attribute> attributes = CauC.createHashFromListAttributes(file);
        List<String> data = CauC.getAllData(file);
        data = CauC.replaceMissingValues(data, attributes);
        for (String line : data) {
            System.out.println(line);
        }
    }

}
