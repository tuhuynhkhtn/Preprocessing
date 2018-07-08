package main;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class MainApplication {

    public static void main(String[] args) {
        //CauA.timKiemChiMuc();
        /*List<File> files = CauB.getFiles();
        CauB.combineFiles(files.get(1), files.get(0));*/
        //CauC.cleanupFiles();
        //CauD.standardlized();

        int choice = 0;
        while (choice == 0) {
            System.out.println("1. Tìm kiếm chỉ mục \t 2. Tự động");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    CauA.timKiemChiMuc();
                    break;
                case 2:
                    List<File> files = CauB.getFiles();
                    CauB.combineFiles(files.get(1), files.get(0));
                    CauC.cleanupFiles();
                    CauD.standardlized();
                    CauE.SimpleRandSampleWithoutReplacement(0.5F);
                    CauE.SimpleRandSampleWithReplacement(0.5F);
                    System.out.println("Hoàn tất!");
                    break;
                default:
                    break;
            }
            choice = continueInput(scanner, choice);
        }
    }

    private static int continueInput(Scanner scanner, int choice) {
        System.out.println("0. Tiếp tục? \t 1. Thoát");
        return scanner.nextInt();
    }

}
