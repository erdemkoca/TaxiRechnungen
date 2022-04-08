import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import static java.lang.Integer.valueOf;

public class ReadExcel {
    private static final String NAME = "src/main/resources/2022.01.xlsx";
    private static final String STR1 = "src/main/resources/test4.xlsx";

    //private static final String NAME = "/cvbn/Desktop/2022.01.xlsx";
    public static void main (String []args) throws IOException {
        int [] arg = new int [args.length];
        for (int i = 0; i < args.length; i++) {
            arg [i] = valueOf(args [i]) ;
        }
        try {
            FileInputStream file = new FileInputStream(new File(NAME));
            Workbook workbook = new XSSFWorkbook(file);
            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Sheet> sheets = workbook.sheetIterator();
            while (sheets.hasNext()) {
                Sheet sh = sheets.next();
                System.out.println("Sheet name is" + sh.getSheetName());
                System.out.println("------------------");
                Iterator<Row> iterator = sh.iterator();
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    Iterator <Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        String cellValue = dataFormatter.formatCellValue(cell);
                        System.out.println(cellValue + "\t");
                    }
                    System.out.println();

                }
                boolean line = false;
                int k = 0;
                for (int j = 0; j < arg.length - 2; j++) {
                    int i = j + 2;

                    if (line && i != 0 && arg[i] - arg[i-1] > 1) {
                        k++;
                        line = false;
                    } else {
                        line = true;
                    }
                    if (sh.getRow(7 + i + k).getCell(8) == null)
                    {
                        sh.getRow(7 + i + k).createCell(8);
                    }
                    sh.getRow(7 + i + k).getCell(8).setCellValue(arg[i] + "." + arg[1] + "." + arg[0]);
                    //System.out.println(sh.getRow(7 + i).getCell(8));
                    sh.getRow(7 + i + k).getCell(10).setCellValue("Hinfahrt CHF 31.75 - Rückfahrt CHF 32.70");
                    sh.getRow(7 + i + k).getCell(14).setCellValue(64.45);

                }

                String firstDayDate = "1/" + args[1] + "/" + args[0];

                int lastDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DATE);
                String lastDayDate = Integer.toString(lastDayOfMonth) + "/" + args[1] + "/" + args[0];
                firstDayDate = firstDayDate.replace("/",".");
                lastDayDate = lastDayDate.replace("/",".");
                sh.getRow(41).getCell(14).setCellValue(arg.length * 64.45);
                sh.getRow(21).getCell(3).setCellValue(arg.length * 64.45);
                sh.getRow(15).getCell(4).setCellValue("Abrechnungsperiode " + firstDayDate + " - " + lastDayDate);
                sh.getRow(7).getCell(7).setCellValue(lastDayDate);


            }
            FileOutputStream out = new FileOutputStream(STR1);
            workbook.write(out);
            out.close();
            workbook = new XSSFWorkbook(new FileInputStream(new File(STR1)));
            workbook.close();
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
