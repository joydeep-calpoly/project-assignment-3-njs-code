package project_3;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

class ArticleTest {
	
	/*
	 * tests that the simple parser is parsed correctly
	 */
	@Test
	void testSimpleVisit() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Path filePath = Paths.get("inputs/simple.txt");
		byte[] jsonData = Files.readAllBytes(filePath);
		ParserSimple article = mapper.readValue(jsonData, ParserSimple.class);
		assertEquals(article.getTitle(), "Assignment #2");
		assertEquals(article.getDescription(),"Extend Assignment #1 to support multiple sources and to introduce source processor.");
		assertEquals(article.getUrl(), "https://canvas.calpoly.edu/courses/55411/assignments/274503");
		assertEquals(article.getDate(), "2021-04-16 09:53:23.709229");
		}
	
	/** verifies that the News API parser preserves and returns all values from a known good article. 
	 */
	@Test
	void testGoodArticle() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Path filePath = Paths.get("tests/goodArticle.json");
		byte[] jsonData = Files.readAllBytes(filePath);
		ParserNews.Article article = mapper.readValue(jsonData, ParserNews.Article.class);
		assertEquals(article.getAuthor(), "By <a href=\"/profiles/julia-hollingsworth\">Julia Hollingsworth</a>, CNN");
		assertEquals(article.getTitle(), "The latest on the coronavirus pandemic and vaccines: Live updates - CNN");
		assertEquals(article.getDescription(), "The coronavirus pandemic has brought countries to a standstill. Meanwhile, vaccinations have already started in some countries as cases continue to rise. Follow here for the latest.");
		assertEquals(article.getUrl(), "https://www.cnn.com/world/live-news/coronavirus-pandemic-vaccine-updates-03-24-21/index.html");
		assertEquals(article.getImageUrl(), "https://cdn.cnn.com/cnnnext/dam/assets/200213175739-03-coronavirus-0213-super-tease.jpg");
		assertEquals(article.getDate(), "2021-03-24T22:32:00Z");
		assertEquals(article.getContent(), "A senior European diplomat is urging caution over the use of proposed new rules that would govern exports of Covid-19 vaccines to outside of the EU. The rules were announced by the European Commissio… [+2476 chars]");
		assertEquals(article.getSource().getName(), "CNN");
		assertEquals(article.getSource().getId(), "cnn");
}
	/** testBadArticles runs a list of only bad articles through the newsParsor. If filtered successfully, it will return an empty list. 
	 */
	@Test
	void testBadArticles() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		byte[] jsonData = Files.readAllBytes(Paths.get("tests/badArticles.json"));
		ParserNews news = mapper.readValue(jsonData, ParserNews.class);
		news.setLogger(makeLogger());
		news.filterArticles();
		List<ParserNews.Article> emptyList = new ArrayList<>();
		assertEquals(news.getArticles(), emptyList);
	}
	
	/** testArticlesFilter verifies the NewsParsor parses the status, articles, and totalResult values without error 
	 */
	@Test
	void testArticlesFilter() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		byte[] jsonData = Files.readAllBytes(Paths.get("tests/example.json"));
		ParserNews news = mapper.readValue(jsonData, ParserNews.class);
		news.setLogger(makeLogger());
		news.filterArticles();
		assertEquals(news.getStatus(), "ok");
		assertEquals(news.getTotalResults(), 38);
	}
	
	/** testMixedArticles verifies the NewsParsor filters out two known bad articles and returns one good article, with all fields
	 */
	@Test
	void testMixedArticles() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		byte[] jsonData = Files.readAllBytes(Paths.get("tests/mixedArticles.json"));
		ParserNews news = mapper.readValue(jsonData, ParserNews.class);
		news.setLogger(makeLogger());
		news.filterArticles();
		assertEquals(news.getStatus(), "ok");
		assertEquals(news.getTotalResults(), 3);
		ParserNews.Article article = news.getArticles().get(0);
		assertEquals(article.getSource().getName(), "Ars Technica");
		assertEquals(article.getSource().getId(), "ars-technica");
		assertEquals(article.getTitle(), "It’s been 20 years since the launch of Mac OS X - Ars Technica");
		assertEquals(article.getAuthor(), "Samuel Axon");
		assertEquals(article.getDescription(), "It's macOS 11 now, but the DNA is the same.");
		assertEquals(article.getUrl(), "https://arstechnica.com/gadgets/2021/03/its-been-20-years-since-the-launch-of-mac-os-x/");
		assertEquals(article.getImageUrl(), "https://cdn.arstechnica.net/wp-content/uploads/2020/06/Screen-Shot-2020-06-22-at-11.27.17-AM-2-1-760x380.png");
		assertEquals(article.getDate(), "2021-03-24T21:44:01Z");
		assertEquals(article.getContent(), "Enlarge/ When presenting a new path forward, Apple CEO Tim Cook put the ARM transition up with the Mac's other big transitions: PowerPC, MacOS X, and Intel.\r\n40 with 35 posters participating\r\nIt was … [+2618 chars]");
	}
	
	/**
	 * makeLogger makes a logger object that will only print newsParsor warnings to the file 'testlog.log." 
	 * @return a custom logger for setting to a newsParsor object. 
	 */
	private static Logger makeLogger() {
		Logger logger = Logger.getLogger(ArticleTest.class.getName());
		LogManager.getLogManager().reset();
		try {
			FileHandler fileHandler = new FileHandler("outputs/testlog.log");
	        logger.addHandler(fileHandler);
			fileHandler.setFormatter(new SimpleFormatter());
		} 
		catch (Exception e) {
			System.out.print(e.getStackTrace());
		} 
		logger.setLevel(java.util.logging.Level.ALL);
        return logger; 
	}

}
