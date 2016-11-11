import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Operations {

	private static Connection con;
	private static boolean hasData = false;
	String pattern = "dd/MM/yyyy";
	SimpleDateFormat format = new SimpleDateFormat(pattern);
	private List<String> savedQueries = new ArrayList<>();

	public void addToList(String query){
		if (savedQueries.size() < 1){
			savedQueries.add(query);
		
		}else{
			for (int i = 0;i<savedQueries.size();i++){		
				if (!(savedQueries.get(i).equals(query))){
					savedQueries.add(query);
					break;
				}
			}	
		}
	}

	public List<String> getSavedQueries() {
		return savedQueries;
	}

	public void setSavedQueries(List<String> savedQueries) {
		this.savedQueries = savedQueries;
	}

	public ResultSet displayArticles() throws ClassNotFoundException, SQLException, ParseException {
		if (con == null) {
			getConnection();
		}

		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT author, title, body,datePublish FROM articles2");
		return res;
	}

	private void getConnection() throws SQLException, ClassNotFoundException, ParseException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:SQLiteTest2.db");
		initialise();
	}

	private void initialise() throws SQLException, ParseException {

		if (!hasData) {
			hasData = true;

			Statement state = con.createStatement();
			ResultSet res = state
					.executeQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name = 'articles2'");

			if (!res.next()) {
				System.out.println("Building the articles table with prepolutaed values.");
				Statement state2 = con.createStatement();
				// need to build the table
				state2.execute("CREATE TABLE articles2(" + "id integer," + "author varchar(60)," + "title varchar(60),"
						+ "body varchar(1000)," + "datePublish datetime default current_timestamp,"
						+ "primary key(id));");

			}
		}
	}

	public void addArticles(Article article) throws ClassNotFoundException, SQLException, ParseException {
		if (con == null) {
			getConnection();
		}
		PreparedStatement prep = con.prepareStatement("INSERT INTO articles2 values(?,?,?,?,?);");
		prep.setString(2, article.getAuthor());
		prep.setString(3, article.getTitle());
		prep.setString(4, article.getBody());

		java.sql.Date sqlDateInsert = null;
		sqlDateInsert = new java.sql.Date(article.getDatePublish().getTime());

		prep.setDate(5, sqlDateInsert);

		addToList("INSERT INTO articles2 values(" + article.getAuthor() + "," + article.getTitle() + ","
				+ article.getBody() + ");");

		prep.execute();
	}

	public List<Article> getArticleByAuthor(String query) throws ClassNotFoundException, SQLException, ParseException {
		if (con == null) {
			getConnection();
		}
		if (query == null) {
			query = "";
		} else {
			query = query.trim();
		}

		String queryGetArticle = "select * from articles2 where lower(author) like '%" + query.toLowerCase() + "%'";
		List<Article> result = new LinkedList<>();
		ResultSet rs = con.createStatement().executeQuery(queryGetArticle);

		while (rs.next()) {
			result.add(extractArticle(rs));
		}
		addToList(queryGetArticle);
		return result;
	}

	public List<Article> getArticleByTitle(String query) throws ClassNotFoundException, SQLException, ParseException {
		if (con == null) {
			getConnection();
		}
		if (query == null) {
			query = "";
		} else {
			query = query.trim();
		}
		String queryGetArticle = "select * from articles2 where lower(title) like '%" + query.toLowerCase() + "%'";
		List<Article> result = new LinkedList<>();
		ResultSet rs = con.createStatement().executeQuery(queryGetArticle);

		while (rs.next()) {
			result.add(extractArticle(rs));
		}
		addToList(queryGetArticle);
		return result;
	}

	public List<Article> getArticleWithinDateRange(Date startDate, Date endDate)
			throws ClassNotFoundException, SQLException, ParseException {
		if (con == null) {
			getConnection();
		}

		java.sql.Date sqlStartDate = null;
		sqlStartDate = new java.sql.Date(startDate.getTime());

		java.sql.Date sqlEndDate = null;
		sqlEndDate = new java.sql.Date(endDate.getTime());
		List<Article> result = new LinkedList<>();

		String query = "select * from articles2 where datePublish > " + sqlStartDate + " and  datePublish < " + "'"
				+ sqlEndDate + "'";
		addToList(query);

		ResultSet rs = con.createStatement().executeQuery(query);

		while (rs.next()) {
			result.add(extractArticle(rs));
		}
		return result;
	}

	private Article extractArticle(ResultSet rs) throws SQLException {
		Article article = new Article();

		article.setAuthor(rs.getString("author"));
		article.setTitle(rs.getString("title"));
		article.setDatePublish(rs.getDate("datePublish"));
		article.setBody(rs.getString("body"));

		return article;
	}
}
