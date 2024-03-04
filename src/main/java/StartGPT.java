import java.io.IOException;
import java.util.Scanner;

public class StartGPT {

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);

		String year = getYear(scanner);
		String month = getMonth(scanner);
		String days = getDays(scanner);

		String[] arguments = {year, month, days};
		ReadExcelGPT.processInvoice(arguments);
	}

	private static String getYear(Scanner scanner) {
		System.out.println("Fatura hangi yıla ait?");
		while (true) {
			try {
				int year = Integer.parseInt(scanner.nextLine());
				if (year >= 2000 && year <= 2050) {
					return String.valueOf(year);
				}
				System.out.println("Lütfen dört haneli seneyi yazin. Mesela: 2021");
			} catch (NumberFormatException e) {
				System.out.println("Geçersiz yıl formatı. Lütfen bir sayı girin.");
			}
		}
	}

	private static String getMonth(Scanner scanner) {
		System.out.println("Fatura hangi aya ait?");
		while (true) {
			try {
				int month = Integer.parseInt(scanner.nextLine());
				if (month >= 1 && month <= 12) {
					return String.format("%02d", month); // Leading zero for months 1-9
				}
				System.out.println("Lütfen ay sayısını giriniz. Örneğin mayıs ayı için : 5");
			} catch (NumberFormatException e) {
				System.out.println("Geçersiz ay formatı. Lütfen bir sayı girin.");
			}
		}
	}

	private static String getDays(Scanner scanner) {
		System.out.println("Faturada ayın hangi günleri listelenmelidir?");
		System.out.println("Lütfen ayın günlerini boşluklarla listeleyin.");
		return scanner.nextLine();
	}
}
