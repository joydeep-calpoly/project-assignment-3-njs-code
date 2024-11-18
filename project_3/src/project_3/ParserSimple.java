package project_3;

import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import project_3.Visitor.format;

/*
 * Simple Article parser class
 */
public class ParserSimple implements Parser {
	private final String title;
	private final String description;
	private final String url;
	private final String publishedAt;
	@JsonIgnore
	private Logger logger;
	
	//loads JSON into private fields 
	@JsonCreator
	public ParserSimple(
			@JsonProperty("title") String title,
			@JsonProperty("description") String description,
			@JsonProperty("url") String url,
			@JsonProperty("publishedAt") String date) {
		this.title = title;
		this.description = description;
		this.url = url;
		this.publishedAt = date;
	}
	
	/*
	 * A constructor for initializing objects before construction in the visitor 
	 */
	public ParserSimple() {
		this.title = "";
		this.description = "";
		this.url = "";
		this.publishedAt = "";
	}
	
	/*
	 * toString for Simple Article format
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
	
	/** 
	 * @return the name field
	 */ 
	public String getName() {
		return this.title;
	}

	/** 
	 * @return the title field 
	 */
	public String getTitle() {
		return this.title;
	}
	
	/** 
	 * @return the description field
	 */
	public String getDescription() {
		return this.description;
	}

	/** 
	 * @return the url field
	 */
	public String getUrl() {
		return this.url;
	}
	
	/** 
	 * @return the publishedAt field
	 */
	public String getDate() {
		return this.publishedAt;
	}

	@Override
	/*
	 * Filter the article if missing necessary fields 
	 */
	public void filterArticles() 
	{
		if (this.title == null || 
			this.description == null || 
			this.url == null || 
			this.publishedAt == null) 
		{
				StringBuilder warningMsg = new StringBuilder();
				warningMsg.append("The following article has a null field: ");
				warningMsg.append(title);
				logger.warning(warningMsg.toString());
		}
	}
	

	/*
	 * Set logger 
	 */
	@Override
	public void setLogger(Logger Logger) {
			this.logger = logger;
	}
	
	/*
	 * Accept passes this instance into the visitor for construction, 
	 * along with the source file path and format (URL or FILE). 
	 * @return A ParserSimple object
	 */
	@Override
	public Parser accept(Visitor v, String source, format f) {
		return v.visit(this, source);
	}
}
