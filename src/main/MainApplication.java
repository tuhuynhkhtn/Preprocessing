package main;

import java.io.File;
import java.util.List;

public class MainApplication {

    public static void main(String[] args) {
        //CauA.timKiemChiMuc();
        List<File> files = CauB.getFiles();
        CauB.combineFiles(files.get(0), files.get(1));
    }

}
