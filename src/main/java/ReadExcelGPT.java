import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadExcelGPT {

	private static final String TEMPLATE_PATH = "src/main/resources/template.xlsx";

	public static void processInvoice(String[] args) throws IOException {
		try (FileInputStream file = new FileInputStream(new File(TEMPLATE_PATH));
				Workbook workbook = new XSSFWorkbook(file)) {

			updateWorkbook(workbook, args);

			String outputPath = String.format("src/main/resources/%s.%s.xlsx", args[0], args[1]);
			try (FileOutputStream out = new FileOutputStream(outputPath)) {
				workbook.write(out);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void updateWorkbook(Workbook workbook, String[] args) {
		Sheet sheet = workbook.getSheetAt(0); // Annahme, dass die Änderungen im ersten Blatt gemacht werden
		int year = Integer.parseInt(args[0]);
		int month = Integer.parseInt(args[1]);
		String[] days = args[2].split(" "); // Teilt die Tage in ein Array

		// Hier implementieren Sie die spezifische Logik zur Aktualisierung der Tabelle.
		// Zum Beispiel könnten Sie für jeden Tag im `days` Array eine bestimmte Zelle aktualisieren.
		for (String day : days) {
			int dayInt = Integer.parseInt(day);
			// Hier kommt die Logik, um die spezifischen Zellen zu finden und zu aktualisieren.
			// Zum Beispiel:
			// Row row = sheet.getRow(rowIndex);
			// Cell cell = row.getCell(cellIndex);
			// cell.setCellValue(...); // Setzt den neuen Wert
		}

		// Weitere Anpassungen können hier hinzugefügt werden, z.B. das Setzen des Gesamtbetrags,
		// das Aktualisieren des Datumsbereichs etc.
	}
}
