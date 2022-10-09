import java.io.IOException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

/**
 * The files are generated while this class runs.
 * It will demand from the commandline to input the year, month and the days of the transportation.
 * With these informations the excel file will be generated.
 */
public class Start {
    public static void main(String args[]) throws IOException {
        System.out.println("Fatura hangi yıla ait?");
        String argumentExcel = "";
        Scanner scan = new Scanner(System.in);

        while (true) {
            String yearScanner = scan.nextLine();
            if (Integer.valueOf(yearScanner) >= 2050
                    || Integer.valueOf(yearScanner) <= 2000) {
                System.out.println("Lütfen dört haneli seneyi yazin. Mesela: 2021");
            } else {
                argumentExcel = String.valueOf(yearScanner);
                break;
            }
        }
        System.out.println("Fatura hangi aya ait?");

        while (true) {
            String monthScanner = scan.nextLine();
            if (Integer.valueOf(monthScanner) > 12) {
                System.out.println("Lütfen ay sayısını giriniz. Örneğin mayıs ayı için : 5");
            } else {
                argumentExcel += " " + monthScanner;
                break;
            }
        }
        System.out.println("Faturada ayın hangi günleri listelenmelidir?");
        System.out.println("Lütfen ayın günlerini boşluklarla listeleyin.");
        while (true) {
            String dayScanner = scan.nextLine();
            String dayString[] = dayScanner.split(" ");
            int[] days = new int[dayString.length];
            int errorCounter = 0;
            for (int i = 0; i < dayString.length; i++) {
                days[i] = parseInt(dayString[i]);
                if (days[i] > 31) {
                    errorCounter++;
                }
            }
            String day[] = new String[]{};
            if (days.length >= 31) {
                System.out.println("Lütfen sadece Faturayinin günlerini listeleyin. Mesela: 1 2 3 7 8 9 ...");
            } else {
                argumentExcel += " " + String.valueOf(dayScanner);
                break;
            }
        }
        String argument [] = argumentExcel.split(" ");
        ReadExcel.main(argument);
    }
}
