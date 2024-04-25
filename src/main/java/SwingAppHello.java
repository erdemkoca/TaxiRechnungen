import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class SwingAppHello
{

	private static void createAndShowGUI() {
		// Create the window
		JFrame frame = new JFrame("Swing Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add a label with text
		JLabel label = new JLabel("Hello, Swing!");
		frame.getContentPane().add(label);

		// Set the window size and make it visible
		frame.setSize(300, 100);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Ensure the GUI creation is done in the Event Dispatch Thread
		SwingUtilities.invokeLater(SwingAppHello::createAndShowGUI);
	}
}
