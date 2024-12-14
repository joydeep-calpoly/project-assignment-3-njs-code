package project_3;

import java.util.List;
import java.util.Iterator;
import com.fasterxml.jackson.annotation.*;
import java.util.logging.Logger;

/*
 * Parser class for NewsAPI format
 */
class ParserNews implements Parser {
	private String status = "";
	private int totalResults = 0;
	private List<Article> articles;
	@JsonIgnore
	private Logger logger;
	
	/*
	 * load JSON values into fields
	 */
	@JsonCreator
	protected ParserNews(
			@JsonProperty("status") String status, 
			@JsonProperty("totalResults") int totalResults, 
			@JsonProperty("articles") List<Article> articles) {
		this.status = status;
		this.totalResults = totalResults;
		this.articles = articles;
	}
	
	/*
	 * A constructor for initializing objects before construction in the visitor 
	 */
	public ParserNews() {
	}
	
	/**
	 * @param logger: setter method for taking a Logger object from the driver
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * filterArticles removes any articles that have null values, and logs a warning message to mylog.log
	 */
	public void filterArticles() {
		Iterator<Article> iterator = this.articles.iterator();
		while (iterator.hasNext()) {
			Article article = iterator.next();
			if (article.title == null || 
				article.description == null || 
				article.url == null || 
				article.publishedAt == null) {
				iterator.remove();
				StringBuilder warningMsg = new StringBuilder();
				warningMsg.append("The following article has a null field: ");
				warningMsg.append(article.getName());
				logger.warning(warningMsg.toString());
			}
		}
	}
	
	/** 
	 * @return a list of Articles
	 */
	public List<Article> getArticles(){
		return this.articles;
	}
	
	/** 
	 * @return a string representing the JSON's status value
	 */
	public String getStatus() {
		return this.status;
	}
	
	/** 
	 * @return an integer, the number of results in the JSON call
	 */
	public int getTotalResults() {
		return this.totalResults;
	}

	/**
	 * @return a string containing the status, results, and articles
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Status: \n\t");
		str.append(this.status);
		str.append("\nTotal Results: \n\t");
		str.append(this.totalResults);
		str.append("\nArticles: \n");
		for (Article article : this.articles) {
			str.append(article);
		}
		return str.toString();
	} 

	/*
	 * Object for de-serializing newsAPI articles.  
	 */
	static class Article {
		private final Source source; 
		private final String author;
		private final String title;
		private final String description;
		private final String url;
		private final String urlToImage;
		private final String publishedAt;
		private final String content;
		
		@JsonCreator
		private Article(@JsonProperty("source") Source source,
				@JsonProperty("author") String author,
				@JsonProperty("title") String title,
				@JsonProperty("description") String description,
				@JsonProperty("url") String url,
				@JsonProperty("urlToImage") String urlToImage,
				@JsonProperty("publishedAt") String date,
				@JsonProperty("content") String content) 
		{
			this.source = source;
			this.author = author;
			this.title = title;
			this.description = description; 
			this.url = url;
			this.urlToImage = urlToImage;
			this.publishedAt = date;
			this.content = content;
			}
		
		static class Source {
			@JsonProperty("name")
			private String name;
			@JsonProperty("id")
			private String id;
			
			public String getName() {
				return this.name;
			}
			public String getId() {
				return this.id;
			}
		}
		
		/**
		 * Formats article to print title, description, date, and URL with clean formating 
		 */ 
		@Override 
		public String toString() {
			StringBuilder str = new StringBuilder(); 
			str.append("Title: ");
			str.append(this.title);
			str.append("\n");
			str.append("Description: ");
			str.append(this.description);
			str.append("\n");
			str.append("Publish Date: ");
			str.append(this.publishedAt);
			str.append("\n");
			str.append("Url: ");
			str.append(this.url);
			str.append("\n\n");
			return str.toString();
		}
		
		/** getName is a getter for the article's name
		 * @return the name field
		 */ 
		public String getName() {
			return this.title;
		}
		
		/** getSource is a getter for the article's source object
		 * @return the source field, containing the name and id fields
		 */ 
		public Source getSource() {
			return this.source;
		}

		/** getAuthor is a getter for the article's author
		 * @return the author field
		 */
		public String getAuthor() {
			return this.author;
		}

		/** getTitle is a getter for the article's title
		 * @return the name field 
		 */
		public String getTitle() {
			return this.title;
		}
		
		/** getDescription is a getter for the article's description
		 * @return the description field
		 */
		public String getDescription() {
			return this.description;
		}

		/** getUrl is a getter for the article's URL
		 * @return the url field
		 */
		public String getUrl() {
			return this.url;
		}

		/** getImageUrl is a getter for the article's Image URL
		 * @return the urlToImage field
		 */
		public String getImageUrl() {
			return this.urlToImage;
		}
		
		/** getDate is a getter for the article's publish date
		 * @return the publishedAt field
		 */
		public String getDate() {
			return this.publishedAt;
		}

		/** getContent is a getter for the article's content
		 * @return the content field
		 */
		public String getContent() {
			return this.content;
		}
	}

	@Override
	/*
	 * filter and output articles
	 */
	public void visit() {
		filterArticles();
		System.out.println(this);
	}
	
	
	
	
}