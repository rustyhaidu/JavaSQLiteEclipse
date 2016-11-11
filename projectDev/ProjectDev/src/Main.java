import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static boolean returnToMainMenu = true;
	public static Scanner sc = new Scanner(System.in);
	public static int in;
	public static Operations test = new Operations();	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		while (returnToMainMenu) {

			try {					

				String pattern = "dd/MM/yyyy";
				SimpleDateFormat format = new SimpleDateFormat(pattern);

				System.out.println("1. Find Article by Author");
				System.out.println("2. Find Article by Title");
				System.out.println("3. Find Article inside time interval");
				System.out.println("4. Load Queries");
				System.out.println("5. Exit");
				
				in = sc.nextInt();

				switch (in) {
				case 1:
					System.out.println(" Enter Author by which you want to search!");

					Scanner scanWord = new Scanner(System.in);
					String searchWord = scanWord.nextLine();

					List<Article> listArticlesByAuthor = new LinkedList<Article>();
					listArticlesByAuthor = test.getArticleByAuthor(searchWord);

					for (int i = 0; i < listArticlesByAuthor.size(); i++) {
						System.out.println("Author: " + listArticlesByAuthor.get(i).getAuthor());
						System.out.println("Title: " + listArticlesByAuthor.get(i).getTitle());
						System.out.println("Body: " + listArticlesByAuthor.get(i).getBody());
						System.out.println("Date Published: " + listArticlesByAuthor.get(i).getDatePublish());
					}					
					returnToMainMenu();
					break;

				case 2:
					System.out.println(" Enter Title(or Part) by which you want to search!");

					Scanner scanTitle = new Scanner(System.in);
					String searchTitle = scanTitle.nextLine();

					List<Article> listArticles = new LinkedList<Article>();
					listArticles = test.getArticleByTitle(searchTitle);

					for (int i = 0; i < listArticles.size(); i++) {
						System.out.println("Author: " + listArticles.get(i).getAuthor());
						System.out.println("Title: " + listArticles.get(i).getTitle());
						System.out.println("Body: " + listArticles.get(i).getBody());
						System.out.println("Date Published: " + listArticles.get(i).getDatePublish());
					}
					returnToMainMenu();
					break;
				case 3:
					System.out.println(" Enter Start Date Range!");

					Scanner scanDate = new Scanner(System.in);
					String searchStartDate = scanDate.nextLine();

					System.out.println(" Enter End Date Range!");
					scanDate = new Scanner(System.in);
					String searchEndDate = scanDate.nextLine();

					List<Article> listArticlesByWithinRange = new LinkedList<Article>();
					Date startDate = format.parse(searchStartDate);
					Date endDate = format.parse(searchEndDate);
					listArticlesByWithinRange = test.getArticleWithinDateRange(startDate, endDate);

					for (int i = 0; i < listArticlesByWithinRange.size(); i++) {
						System.out.println("Author: " + listArticlesByWithinRange.get(i).getAuthor());
						System.out.println("Title: " + listArticlesByWithinRange.get(i).getTitle());
						System.out.println("Body: " + listArticlesByWithinRange.get(i).getBody());
						System.out.println("Date Published: " + listArticlesByWithinRange.get(i).getDatePublish());
						System.out.println();
					}
					returnToMainMenu();
					break;
				case 4:					
					for (int i = 0; i < test.getSavedQueries().size(); i++) {
						System.out.println("Query: " + test.getSavedQueries().get(i));
					}
					System.out.println();
					returnToMainMenu();
					break;
				case 0:
					return;
				}

			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public static void returnToMainMenu() {
		System.out.println("1. Return to Main Menu");
		System.out.println("0. Exit");
		in = sc.nextInt();
		switch (in) {
		case 1:
			returnToMainMenu = true;
			break;
		case 0:
			returnToMainMenu = false;
			break;
		default:
			returnToMainMenu = false;
		}
	}
}
