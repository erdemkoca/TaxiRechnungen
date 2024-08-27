import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SwingApp {
	private static final JFrame frame = new JFrame("Invoice Generator");
	private static final List<JToggleButton> dayButtons = new ArrayList<>();
	private static final JComboBox<Integer> yearComboBox = new JComboBox<>();
	private static final JComboBox<String> monthComboBox = new JComboBox<>();
	private static final JPanel daysPanel = new JPanel(new GridLayout(5, 7, 5, 5));
	private static final JLabel totalSelectedDaysLabel = new JLabel("Toplam seçili gün sayısı: 0");
	// Verlagern Sie die Definition von turkishMonths, um sie als Klassenvariable zu machen
	private static final String[] turkishMonths = {"Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"};




	public static void createAndShowGUI() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 400); // Adjusted for space for day buttons
		JPanel panel = new JPanel();
		panel.setLayout(null); // Using null layout for simplicity, consider using a LayoutManager
		frame.add(panel);
		placeComponents(panel);
		frame.setVisible(true);
	}

	private static void placeComponents(JPanel panel) {
		JLabel yearLabel = new JLabel("Sene:");
		yearLabel.setBounds(10, 20, 80, 25);
		panel.add(yearLabel);

		// Populate the yearComboBox with years from 1990 to current year + 5 (for future selection)
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int futureYear = currentYear + 5; // Allow selection up to 5 years into the future
		for (int year = 1990; year <= futureYear; year++) {
			yearComboBox.addItem(year);
		}
		yearComboBox.setBounds(100, 20, 165, 25);
		panel.add(yearComboBox);

		// Set the current year as the default selected item
		yearComboBox.setSelectedItem(currentYear);

		// Month label
		JLabel monthLabel = new JLabel("Ay:");
		monthLabel.setBounds(10, 55, 80, 25); // Adjusted positions
		panel.add(monthLabel);

		// Populate the monthComboBox with Turkish month names
		String[] turkishMonths = {"Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran",
				"Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"};

		// Populate the monthComboBox with months
		for (String month : turkishMonths) {
			monthComboBox.addItem(month);
		}
		monthComboBox.setBounds(100, 55, 165, 25);
		panel.add(monthComboBox);

		// Set the default selected month to the month before the current month
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH); // Note: January = 0 in Calendar
		// int defaultMonth = currentMonth == 0 ? 12 : currentMonth; // Adjust for January
		// monthComboBox.setSelectedItem(defaultMonth);
		if (currentMonth == 0) {
			yearComboBox.setSelectedItem(currentYear - 1); // Set year to last year if January
			monthComboBox.setSelectedItem(turkishMonths[11]); // Set month to December
		} else {
			monthComboBox.setSelectedItem(turkishMonths[currentMonth - 1]);
		}

		// Day selection panel
		JLabel dayLabel = new JLabel("Günler:");
		dayLabel.setBounds(10, 85, 80, 25);
		panel.add(dayLabel);

		daysPanel.setBounds(10, 110, 760, 100);
		panel.add(daysPanel);

		for (int day = 1; day <= 31; day++) {
			JToggleButton dayButton = new JToggleButton(String.valueOf(day));
			dayButtons.add(dayButton);
			daysPanel.add(dayButton);
		}

		// Generate Button and its ActionListener
		JButton generateButton = new JButton("Fatura oluştur");
		generateButton.setBounds(10, 220, 120, 25); // Adjust position as needed
		panel.add(generateButton);

		totalSelectedDaysLabel.setBounds(10, 320, 300, 25); // Position und Größe anpassen
		panel.add(totalSelectedDaysLabel);

//		generateButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				String selectedYear = yearComboBox.getSelectedItem().toString();
//				String selectedMonthName = monthComboBox.getSelectedItem().toString();
//
//				// Konvertieren des Monatsnamens in eine Zahl
//				int selectedMonth = Arrays.asList(turkishMonths).indexOf(selectedMonthName) + 1;
//				String formattedMonth = selectedMonth < 10 ? "0" + selectedMonth : String.valueOf(selectedMonth);
//
//				List<String> selectedDays = new ArrayList<>();
//				for (JToggleButton button : dayButtons) {
//					if (button.isSelected()) {
//						selectedDays.add(button.getText());
//					}
//				}
//
//				// Bereite die Argumente vor, einschließlich der konvertierten Monatszahl
//				List<String> arguments = new ArrayList<>();
//				arguments.add(selectedYear);
//				arguments.add(formattedMonth);
//				arguments.addAll(selectedDays);
//
//				try {
//					ReadExcel.main(arguments.toArray(new String[0])); // Stelle sicher, dass ReadExcel.main diese Argumente verarbeiten kann
//					frame.dispose(); // Schließe das GUI
//				} catch (Exception ex) {
//					ex.printStackTrace();
//					JOptionPane.showMessageDialog(frame, "Error generating the Excel file.", "Error", JOptionPane.ERROR_MESSAGE);
//				}
//			}
//		});

		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedYear = yearComboBox.getSelectedItem().toString();
				String selectedMonthName = monthComboBox.getSelectedItem().toString();
				int selectedMonth = Arrays.asList(turkishMonths).indexOf(selectedMonthName) + 1;
				String formattedMonth = selectedMonth < 10 ? "0" + selectedMonth : String.valueOf(selectedMonth);

				List<String> selectedDays = new ArrayList<>();
				for (JToggleButton button : dayButtons) {
					if (button.isSelected()) {
						selectedDays.add(button.getText());
					}
				}

				try {
					// Get the directory where the JAR file is located
					String jarPath = new File(SwingApp.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
					String dirPath = new File(jarPath).getParent();

					// Create a new directory named 'Taxirechnungen' if it doesn't exist
					File taxirechnungenDir = new File(dirPath, "Taxirechnungen");
					if (!taxirechnungenDir.exists()) {
						taxirechnungenDir.mkdir();
					}

					// Generate the full path to the PDF file
					String fileName = selectedYear + "." + formattedMonth + ".pdf";
					String pdfPath = new File(taxirechnungenDir, fileName).getPath();

					// Create the PDF
					TextToPdf.createPdfFromGuiData(pdfPath, Integer.parseInt(selectedYear), formattedMonth, selectedDays);

					JOptionPane.showMessageDialog(frame, "PDF erfolgreich generiert.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Fehler beim Generieren der PDF-Datei.", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		});



		// Listener für Monats- und Jahres-JComboBox hinzufügen
		ActionListener updateDaysActionListener = e -> {
			updateDayButtons(); // Aktualisiert die Tage basierend auf dem ausgewählten Monat
			updateTotalSelectedDays(); // Aktualisiert die Gesamtzahl der ausgewählten Tage
		};
		yearComboBox.addActionListener(updateDaysActionListener);
		monthComboBox.addActionListener(updateDaysActionListener);

		// Initialisiere die Tage basierend auf dem aktuellen Monat und Jahr
		updateDayButtons();


	}

	private static void updateDayButtons() {
		daysPanel.removeAll();
		dayButtons.clear();

		Integer year = (Integer) yearComboBox.getSelectedItem();
		String selectedMonthName = (String) monthComboBox.getSelectedItem();
		// Verwenden Sie turkishMonths, um den Monat in eine Zahl umzuwandeln
		int month = Arrays.asList(turkishMonths).indexOf(selectedMonthName) + 1;

		if (year == null || selectedMonthName == null) return;

		int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
		for (int day = 1; day <= daysInMonth; day++) {
			JToggleButton dayButton = new JToggleButton(String.valueOf(day));
			dayButton.addActionListener(e -> updateTotalSelectedDays());
			dayButtons.add(dayButton);
			daysPanel.add(dayButton);
		}

		daysPanel.revalidate();
		daysPanel.repaint();
	}


	// Methode zum Aktualisieren der Anzeige der Gesamtzahl der ausgewählten Tage
	private static void updateTotalSelectedDays() {
		long count = dayButtons.stream().filter(AbstractButton::isSelected).count(); // Zählt die ausgewählten Tage
		totalSelectedDaysLabel.setText("Toplam seçili gün sayısı: " + count); // Aktualisiert das Label
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(SwingApp::createAndShowGUI);
	}
}
