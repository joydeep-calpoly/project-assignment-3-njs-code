package project_3;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import project_3.Visitor.format;

class Driver {
	
	/* Main is responsible for:
	 * 		Constructing the concrete visitor object
	 * 		Initializing/constructing Parser objects with the visitor, 
	 * 		Setting the logger and filtering articles (Note: this could have been moved to visitor, 
	 * 			but would require the visitor to create the logger/log file, which seemed too tightly coupled/not closed to modification)
	 * 		Printing output
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		//JSON inputs
		String simpleFile = "inputs/simple.txt";
		String newsFile = "inputs/newsapi.txt";
		String newsApiURL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=3a1ad67f3a504678800bbdfc70b37696";
		
		//initialize logger and parser constructor visitor
		Logger logger = makeLogger("outputs/mylog.log");
		Visitor visitor = new VisitorConstructor();
		
		//initialize a simple object to access non-static methods
		Parser simple = new ParserSimple();
		//accept visitor to construct simple parser, pass file
		simple = simple.accept(visitor, simpleFile, format.FILE);
		//set logger and filter for null values
		simple.setLogger(logger);
		simple.filterArticles();
		//print to console
		System.out.print(simple);
		
		//initialize ParserNews object
		Parser apiURL = new ParserNews();
		//accept visitor to construct news API parser from a URL source
		apiURL = apiURL.accept(visitor, newsApiURL, format.URL);
		apiURL.setLogger(logger);
		apiURL.filterArticles();
		System.out.print(apiURL);
		
		//initialize ParserNews object
		Parser APIfile = new ParserNews();
		//accept visitor to construct news API parser from a File source
		APIfile = APIfile.accept(visitor, newsFile, format.FILE);
		APIfile.setLogger(logger);
		APIfile.filterArticles();
		System.out.print(APIfile);
	}

	/* makeLogger
	 * Creates and returns a custom logger to print warnings to the log file provided as an argument
	 */
 	private static Logger makeLogger(String fileName) {
		// create logger 
		Logger logger = Logger.getLogger(Driver.class.getName());
		//disable all default handlers
		LogManager.getLogManager().reset(); 
		//Add a file handler which will print to myLog.log in the inputs folder
		try {
			FileHandler fileHandler = new FileHandler(fileName);
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