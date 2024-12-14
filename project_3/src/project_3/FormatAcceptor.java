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

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * FormatAcceptor is a class for simplifying Parser construction and displaying for a client
 */
public class FormatAcceptor {
	enum format {file, url};
	enum source {simple, newsapi};
	private format format;
	private source source;
	private String sourcePath;
	
	/*
	 * The constructor must take a format, source, and sourcePath. 
	 * These will determine the parser that is created in the run() method
	 * The sourcePath must correctly match the format and source specifications 
	 * An exception is thrown if url and simple are combined, as this is an illegal argument 
	 */
	public FormatAcceptor(format f, source s, String sourcePath) {
		if (f.equals(format.url) && s.equals(source.simple)) {
			throw new IllegalArgumentException("Source cannot be URL when format is simple");
		}
		this.format = f;
		this.source = s;
		this.sourcePath = sourcePath;
	}
	
	/*
	 * Create parser object and run accept to visit
	 */
	void run() {
		ObjectMapper mapper = new ObjectMapper();
		Parser obj = null;
		
		//create Parser object depending on constructor parameters 
		try {
			//Simple File format
			if (this.format.equals(format.file) && this.source.equals(source.simple)) {
				byte[] jsonData = Files.readAllBytes(Paths.get(sourcePath));
				obj = mapper.readValue(jsonData, ParserSimple.class);
			}
			
			//News API File format
			else if (this.format.equals(format.file) &&  this.source.equals(source.newsapi)) {
				byte[] jsonData = Files.readAllBytes(Paths.get(sourcePath));
				obj = mapper.readValue(jsonData, ParserNews.class);
			}
			
			//News API URL format
			else if (this.format.equals(format.url) && this.source.equals(source.newsapi)) {
				URL apiURL = new URI(sourcePath).toURL(); 
				obj = mapper.readValue(apiURL, ParserNews.class);
			}
			
			//Defensive Programming 
			else {
				throw new IllegalStateException("Format and source not instantiated correctly.");
			}
		}
		catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		} 
		obj.setLogger(makeLogger("outputs/mylog.log"));
		accept(obj);
		}
		
	/* 
	 * Visit parser to filter articles and display
	 */
	private void accept(Parser p) {
		p.visit();
	}
	
	/*
	 * method for creating and configuring the logger 
	 */
	private static Logger makeLogger(String fileName) {
		// create logger 
		Logger logger = Logger.getLogger(fileName);
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
