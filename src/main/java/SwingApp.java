import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SwingApp {
	private static final JFrame frame = new JFrame("Invoice Generator");
	private static final List<JToggleButton> dayButtons = new ArrayList<>();
	// These are already class level, ensuring they are accessible throughout the class
	private static final JTextField yearText = new JTextField(20);
	private static final JTextField monthText = new JTextField(20);

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
		JLabel yearLabel = new JLabel("Year:");
		yearLabel.setBounds(10, 20, 80, 25);
		panel.add(yearLabel);

		yearText.setBounds(100, 20, 165, 25);
		panel.add(yearText);

		JLabel monthLabel = new JLabel("Month:");
		monthLabel.setBounds(10, 50, 80, 25);
		panel.add(monthLabel);

		monthText.setBounds(100, 50, 165, 25);
		panel.add(monthText);

		// Day selection panel
		JPanel daysPanel = new JPanel(new GridLayout(5, 7, 5, 5)); // Grid of 5 rows and 7 columns with padding
		daysPanel.setBounds(10, 110, 760, 100); // You may need to adjust the size and position
		panel.add(daysPanel);

		for (int day = 1; day <= 31; day++) {
			JToggleButton dayButton = new JToggleButton(String.valueOf(day));
			dayButtons.add(dayButton);
			daysPanel.add(dayButton);
		}

		// Generate Button and its ActionListener
		JButton generateButton = new JButton("Generate");
		generateButton.setBounds(10, 220, 120, 25); // Adjust position as needed
		panel.add(generateButton);

		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String year = yearText.getText();
				String month = monthText.getText();
				List<String> selectedDays = new ArrayList<>();
				for (JToggleButton button : dayButtons) {
					if (button.isSelected()) {
						selectedDays.add(button.getText());
					}
				}

				// Prepare the arguments array with year, month, and selected days
				String[] arguments = new String[2 + selectedDays.size()];
				arguments[0] = year;
				arguments[1] = month;
				System.arraycopy(selectedDays.toArray(new String[0]), 0, arguments, 2, selectedDays.size());

				try {
					ReadExcel.main(arguments);
					// System.out.println("Selected Dates: " + String.join(", ", arguments));
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Error generating the Excel file.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(SwingApp::createAndShowGUI);
	}
}
