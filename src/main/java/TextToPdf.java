import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TextToPdf {

    public static void createPdfFromGuiData(String dest, int year, String month, java.util.List<String> selectedDays) {
        try {
            PdfFont fontBold = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
            PdfFont fontRegular = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Header und Unternehmensinformationen
            document.add(new Paragraph("RECHNUNG")
                    .setFont(fontBold)
                    .setFontSize(18)
                    .setFontColor(DeviceGray.GRAY)
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("Taxi & Transportkurier Ahmet Koca")
                    .setFont(fontBold)
                    .setFontSize(12));

            document.add(new Paragraph("Im Westfeld 17\nTelefon: 079 644 08 40")
                    .setFont(fontRegular)
                    .setFontSize(10));

            document.add(new Paragraph(String.format("Rechnungsnummer.: 24.5\nDatum: 31.%s.%d", month, year))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFont(fontRegular)
                    .setFontSize(10));

            // Kundeninformationen
//            document.add(new Paragraph("An:\nMuhammed Ikbal Koca\nIm Westfeld 17\n4055 Basel")
//                    .setFont(fontRegular)
//                    .setFontSize(10));

            // Text-Objekte erstellen
            Text labelText = new Text("An:").setFont(fontBold).setFontSize(10);
            Text nameText = new Text("\nMuhammed Ikbal Koca\nIm Westfeld 17\n4055 Basel").setFont(fontRegular).setFontSize(10);

            // Paragraph mit gemischten Schriftarten
            Paragraph customerInfo = new Paragraph()
                    .add(labelText)
                    .add(nameText);
            document.add(customerInfo);

            // Text-Objekte erstellen
            Text labelText2 = new Text("Für:").setFont(fontBold).setFontSize(10).setTextAlignment(TextAlignment.RIGHT);
            Text nameText2 = new Text(String.format("\nRechnungsfahrt Dychrain\nAbrechnungsperiode 1.%s.%d - 31.%s.%d", month, year, month, year)).setFont(fontRegular).setFontSize(10);

            // Paragraph mit gemischten Schriftarten
            Paragraph customerInfo2 = new Paragraph()
                    .add(labelText2)
                    .add(nameText2);
            document.add(customerInfo2);

//            // Dienstleistungsinformationen
//            document.add(new Paragraph(String.format("Für:\nRechnungsfahrt Dychrain\nAbrechnungsperiode 1.%s.%d - 31.%s.%d", month, year, month, year))
//                    .setTextAlignment(TextAlignment.RIGHT)
//                    .setFont(fontRegular)
//                    .setFontSize(10));

            //Paragraph
            Paragraph ppp = new Paragraph("Dies ist ein Absatz mit Abständen.")
                    .setMarginTop(12)   // Abstand oben
                    .setMarginBottom(12); // Abstand unten

            // Berechnung der Gesamtsumme
            double totalSum = 96.40 * selectedDays.size();
            Paragraph totalSumParagraph = new Paragraph(String.format("Summe Total: CHF %.2f", totalSum))
                    .setFont(fontBold)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setUnderline()
                    .setMarginTop(52);
            document.add(totalSumParagraph);

            // Zahlungsinformationen
            document.add(new Paragraph("Bitte überweisen Sie den Gesamtbetrag auf das folgende Konto:")
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setMarginTop(72));

            //Paragraph
            Paragraph p = new Paragraph("Dies ist ein Absatz mit Abständen.")
                    .setMarginTop(32)   // Abstand oben
                    .setMarginBottom(32); // Abstand unten

            // Zahlungsinformationen
            document.add(new Paragraph("Taxi & Transportkurier Ahmet Koca\nIBAN CH83 0900 0000 4073 0339 8\nKontonummer 40-730339-8\nBIC POFICHBEXXX\nMwSt Nr: CHE-108.242.406")
                    .setFont(fontRegular)
                    .setFontSize(10));

            //Paragraph
            Paragraph pp = new Paragraph("Dies ist ein Absatz mit Abständen.")
                    .setMarginTop(52)   // Abstand oben
                    .setMarginBottom(52); // Abstand unten

            // Zahlungsinformationen
            document.add(new Paragraph("Bei Fragen zur Rechnung stehe ich Ihnen gerne telefonisch zur Verfügung.")
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setMarginTop(100));

            // Vielen Dank
            document.add(new Paragraph("Vielen Dank für Ihre Bestellung!")
                    .setFont(fontBold)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12)
                    .setMarginTop(50));

            // Seitenumbruch vor der Auflistung der Tage
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            // Hin- Rückfahrt Information
            document.add(new Paragraph("Hinfahrt: Von Zuhause nach Dychrain, 8:30 Uhr\nRückfahrt: Von Dychrain nach Hause, 16:30 Uhr")
                    .setFont(fontRegular)
                    .setItalic()
                    .setMarginBottom(50)
                    .setFontSize(10));

            // Details zu den Fahrten
//            List list = new List()
//                    .setSymbolIndent(12)
//                    .setListSymbol("\u2022")
//                    .setFont(fontRegular)
//                    .setFontSize(10);
//            for (String day : selectedDays) {
//                list.add(new ListItem(String.format("%s.%s.%d        Hinfahrt CHF 48.20 - Rückfahrt CHF 48.20                                 CHF 96.40", day, month, year)));
//            }
//            document.add(list);

            // Details zu den Fahrten als Tabelle
            float[] columnWidths = {1, 5, 2}; // Relative Breite der Spalten
            Table table = new Table(UnitValue.createPercentArray(columnWidths))
                    .useAllAvailableWidth()
                    .setFontSize(10)
                    .setFont(fontRegular);

            int previousDay = 0;

            for (String dayStr : selectedDays) {
                int day = Integer.parseInt(dayStr);

                // Füge eine leere Zeile ein, wenn die Tage nicht aufeinander folgen
                if (previousDay != 0 && day != previousDay + 1) {
                    table.addCell(new Cell(1, 3).add(new Paragraph(" ")).setBorder(new SolidBorder(DeviceGray.WHITE, 2.5f)));
                }

                Cell dateCell = new Cell().add(new Paragraph(String.format("%d.%s.%d", day, month, year)));
                Cell descriptionCell = new Cell().add(new Paragraph("Hinfahrt CHF 48.20 - Rückfahrt CHF 48.20"));
                Cell amountCell = new Cell().add(new Paragraph("CHF 96.40"));

                // Setze sichtbare Ränder für die Zellen
                dateCell.setBorder(new SolidBorder(DeviceGray.WHITE, 0.5f));
                descriptionCell.setBorder(new SolidBorder(DeviceGray.WHITE, 0.5f));
                amountCell.setBorder(new SolidBorder(DeviceGray.WHITE, 0.5f));

                table.addCell(dateCell);
                table.addCell(descriptionCell);
                table.addCell(amountCell);

                previousDay = day;
            }

            document.add(table);

            document.add(new Paragraph(String.format("Summe Total inkl. 8.1%% MwSt:                                                             CHF %.2f", totalSum))
                    .setFont(fontBold)
                    .setFontSize(10)
                    .setMarginTop(50)
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();
        } catch (FileNotFoundException e) {
            System.out.println("Datei wurde nicht gefunden: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
