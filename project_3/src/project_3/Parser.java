package project_3;

import java.util.logging.Logger;

import project_3.Visitor.format;

/* Parser interface
 * defines the basic behaviors a parser should have:
 */
public interface Parser {	
	//method to filter the articles based on necessary fields
	public void filterArticles();
	//method to set a Logger object 
	public void setLogger(Logger logger);
	//toString method
	public String toString();
	//visitor accept method
	public Parser accept(Visitor v, String source, format f);
}

