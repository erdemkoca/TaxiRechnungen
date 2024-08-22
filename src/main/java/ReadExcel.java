import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.YearMonth;
import java.util.Iterator;

public class ReadExcel {
    private static final String STR1 = "src/main/resources/";

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

            processWorkbook(workbook, dataFormatter, arg);

            String specificSTR = STR1 + arg[0] + "." + (arg[1] < 10 ? "0" : "") + arg[1] + ".xlsx";
            try (FileOutputStream out = new FileOutputStream(specificSTR)) {
                workbook.write(out);
            }
            workbook.close();

            // Convert Excel to PDF
            convertExcelToPdf(specificSTR, specificSTR.replace(".xlsx", ".pdf"));
        } catch (FileNotFoundException e) {
            System.out.println("Datei wurde nicht gefunden: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ein IO-Fehler ist aufgetreten: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processWorkbook(Workbook workbook, DataFormatter dataFormatter, int[] arg) throws IOException {
        Iterator<Sheet> sheets = workbook.sheetIterator();
        while (sheets.hasNext()) {
            Sheet sh = sheets.next();
            Iterator<Row> iterator = sh.iterator();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellValue = dataFormatter.formatCellValue(cell);
                }
            }

            boolean line = false;
            int k = 0;
            for (int j = 0; j < arg.length - 2; j++) {
                int i = j + 2;

                if (line && i != 0 && arg[i] - arg[i - 1] > 1) {
                    k++;
                    line = false;
                } else {
                    line = true;
                }
                if (sh.getRow(7 + i + k).getCell(8) == null) {
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
    }

    private static void convertExcelToPdf(String excelFilePath, String pdfFilePath) throws IOException, InvalidFormatException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        Workbook workbook = new XSSFWorkbook(new File(excelFilePath));
        PdfWriter writer = new PdfWriter(pdfFilePath);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        DataFormatter formatter = new DataFormatter();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            document.add(new Paragraph("Sheet: " + sheet.getSheetName()));

            for (Row row : sheet) {
                StringBuilder rowContent = new StringBuilder();
                for (Cell cell : row) {
                    String cellValue = formatter.formatCellValue(cell);
                    rowContent.append(cellValue).append("\t");
                }
                document.add(new Paragraph(rowContent.toString()));
            }
            document.add(new Paragraph("\n"));
        }

        document.close();
        workbook.close();
    }



    public static double roundDownToFiveRappen(double value) {
        double rappen = value * 100;
        double remainder = rappen % 10;
        if (remainder < 5) {
            rappen -= remainder;
        } else {
            rappen = rappen - remainder + 5;
        }
        return Math.floor(rappen) / 100;
    }
}
