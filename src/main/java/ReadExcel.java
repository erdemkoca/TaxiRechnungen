import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.YearMonth;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static java.lang.Integer.valueOf;

public class ReadExcel
{
    // private static final String NAME = "src/main/resources/2022.01.xlsx";
    private static final String STR1 = "src/main/resources/";
    // private static final String STR1 = System.getProperty("user.home") + "/Desktop";

    public static void main(String[] args) {
        try {
            InputStream file = ReadExcel.class.getResourceAsStream("/2022.01.xlsx");
            if (file == null) {
                throw new FileNotFoundException("Die Datei 2022.01.xlsx wurde nicht gefunden");
            }
            Workbook workbook = new XSSFWorkbook(file);
            DataFormatter dataFormatter = new DataFormatter();

            int[] arg = new int[args.length];
            for (int i = 0; i < args.length; i++) {
                arg[i] = Integer.valueOf(args[i]);
            }

            Iterator<Sheet> sheets = workbook.sheetIterator();
            while (sheets.hasNext())
            {
                Sheet sh = sheets.next();
                Iterator<Row> iterator = sh.iterator();
                while (iterator.hasNext())
                {
                    Row row = iterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext())
                    {
                        Cell cell = cellIterator.next();
                        String cellValue = dataFormatter.formatCellValue(cell);
                    }
                }

                boolean line = false;
                int k = 0;
                for (int j = 0; j < arg.length - 2; j++)
                {
                    int i = j + 2;

                    if (line && i != 0 && arg[i] - arg[i - 1] > 1)
                    {
                        k++;
                        line = false;
                    }
                    else
                    {
                        line = true;
                    }
                    if (sh.getRow(7 + i + k).getCell(8) == null)
                    {
                        sh.getRow(7 + i + k).createCell(8);
                    }
                    sh.getRow(7 + i + k).getCell(8).setCellValue(arg[i] + "." + arg[1] + "." + arg[0]);
                    sh.getRow(7 + i + k).getCell(10).setCellValue("Hinfahrt CHF 48.20 - Rückfahrt CHF 48.20");
                    sh.getRow(7 + i + k).getCell(14).setCellValue(96.40);
                }

                YearMonth yearMonthObject = YearMonth.of(arg[0], arg[1]);
                int lastDayOfMonth = yearMonthObject.lengthOfMonth();
                String firstDayDate = "1." + arg[1] + "." + arg[0];
                String lastDayDate = lastDayOfMonth + "." + arg[1] + "." + arg[0];

                sh.getRow(41).getCell(14).setCellValue(roundDownToFiveRappen((arg.length - 2) * 96.40 * 1.081));
                sh.getRow(21).getCell(3).setCellValue(roundDownToFiveRappen((arg.length - 2) * 96.40 * 1.081));
                sh.getRow(15).getCell(4).setCellValue("Abrechnungsperiode " + firstDayDate + " - " + lastDayDate);
                sh.getRow(7).getCell(7).setCellValue(lastDayDate);
                String lastTwoOfCharYear = "" + arg[0] % 100; // Simplified to get last two digits
                String invoiceNumber = "RECHNUNGSNR.: " + lastTwoOfCharYear + "." + arg[1];
                sh.getRow(6).getCell(7).setCellValue(invoiceNumber);
            }
            // -8 +3

            String specificSTR = STR1 + arg[0] + "." + (arg[1] < 10 ? "0" : "") + arg[1] + ".xlsx";
            FileOutputStream out = new FileOutputStream(specificSTR);
            //TODO: where does it save the file if we run it with JAR that has no resource folder? how is this handled? Maybe the problem that Jar is not running
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            System.out.println("Datei wurde nicht gefunden: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ein IO-Fehler ist aufgetreten: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // public static void main(String[] args) throws FileNotFoundException
    // {
    //     try
    //     {
    //         // Code, der eine Exception auslösen könnte
    //         FileInputStream file = new FileInputStream(new File("src/main/resources/2022.01.xlsx"));
    //         // Weiterer Code zur Verarbeitung der Datei
    //
    //         int[] arg = new int[args.length];
    //         for (int i = 0; i < args.length; i++)
    //         {
    //             arg[i] = valueOf(args[i]);
    //         }
    //         try
    //         {
    //             FileInputStream file = new FileInputStream(new File(NAME));
    //             Workbook workbook = new XSSFWorkbook(file);
    //             DataFormatter dataFormatter = new DataFormatter();
    //             Iterator<Sheet> sheets = workbook.sheetIterator();
    //             while (sheets.hasNext())
    //             {
    //                 Sheet sh = sheets.next();
    //                 Iterator<Row> iterator = sh.iterator();
    //                 while (iterator.hasNext())
    //                 {
    //                     Row row = iterator.next();
    //                     Iterator<Cell> cellIterator = row.cellIterator();
    //                     while (cellIterator.hasNext())
    //                     {
    //                         Cell cell = cellIterator.next();
    //                         String cellValue = dataFormatter.formatCellValue(cell);
    //                     }
    //                 }
    //
    //                 boolean line = false;
    //                 int k = 0;
    //                 for (int j = 0; j < arg.length - 2; j++)
    //                 {
    //                     int i = j + 2;
    //
    //                     if (line && i != 0 && arg[i] - arg[i - 1] > 1)
    //                     {
    //                         k++;
    //                         line = false;
    //                     }
    //                     else
    //                     {
    //                         line = true;
    //                     }
    //                     if (sh.getRow(7 + i + k).getCell(8) == null)
    //                     {
    //                         sh.getRow(7 + i + k).createCell(8);
    //                     }
    //                     sh.getRow(7 + i + k).getCell(8).setCellValue(arg[i] + "." + arg[1] + "." + arg[0]);
    //                     sh.getRow(7 + i + k).getCell(10).setCellValue("Hinfahrt CHF 48.20 - Rückfahrt CHF 48.20");
    //                     sh.getRow(7 + i + k).getCell(14).setCellValue(96.40);
    //                 }
    //
    //                 YearMonth yearMonthObject = YearMonth.of(arg[0], arg[1]);
    //                 int lastDayOfMonth = yearMonthObject.lengthOfMonth();
    //                 String firstDayDate = "1." + arg[1] + "." + arg[0];
    //                 String lastDayDate = lastDayOfMonth + "." + arg[1] + "." + arg[0];
    //
    //                 sh.getRow(41).getCell(14).setCellValue(roundDownToFiveRappen((arg.length - 2) * 96.40 * 1.081));
    //                 sh.getRow(21).getCell(3).setCellValue(roundDownToFiveRappen((arg.length - 2) * 96.40 * 1.081));
    //                 sh.getRow(15).getCell(4).setCellValue("Abrechnungsperiode " + firstDayDate + " - " + lastDayDate);
    //                 sh.getRow(7).getCell(7).setCellValue(lastDayDate);
    //                 String lastTwoOfCharYear = "" + arg[0] % 100; // Simplified to get last two digits
    //                 String invoiceNumber = "RECHNUNGSNR.: " + lastTwoOfCharYear + "." + arg[1];
    //                 sh.getRow(6).getCell(7).setCellValue(invoiceNumber);
    //             }
    //             // -8 +3
    //
    //             String specificSTR = STR1 + arg[0] + "." + (arg[1] < 10 ? "0" : "") + arg[1] + ".xlsx";
    //             FileOutputStream out = new FileOutputStream(specificSTR);
    //             workbook.write(out);
    //             out.close();
    //             workbook = new XSSFWorkbook(new FileInputStream(new File(specificSTR)));
    //             workbook.close();
    //         }
    //         catch (FileNotFoundException e)
    //         {
    //             System.out.println("Datei wurde nicht gefunden: " + e.getMessage());
    //         }
    //         catch (IOException e)
    //         {
    //             System.out.println("Ein IO-Fehler ist aufgetreten: " + e.getMessage());
    //         }
    //         catch (Exception e)
    //         {
    //             // Fange andere Exceptions ab
    //             e.printStackTrace();
    //         }
    //     }
    // }

    public static double roundDownToFiveRappen(double value) {
        // Multiply by 100 to work with the value in Rappen accurately
        double rappen = value * 100;
        // Find the remainder when dividing by 10 (to identify the last digit)
        double remainder = rappen % 10;
        // Subtract the remainder if it's less than 5, otherwise subtract the remainder and add 5 to round down to the nearest 5
        if (remainder < 5) {
            rappen -= remainder;
        } else {
            rappen = rappen - remainder + 5;
        }
        // Convert back to CHF and round to 2 decimal places if necessary
        return Math.floor(rappen) / 100;
    }


}
