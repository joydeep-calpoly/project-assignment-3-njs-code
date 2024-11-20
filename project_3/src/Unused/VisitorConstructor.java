package Unused;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.ObjectMapper;

import Unused.Visitor;

/*
 * Concrete visitor class for constructing NewsAPI and Simple Parser objects
 */
public class VisitorConstructor extends Visitor {
	ObjectMapper mapper;

	/*
	 * Simple constructor to instantiate an ObjectMapper
	 */
	protected VisitorConstructor() {
		this.mapper = new ObjectMapper();
	}

	/*
	 * Visitor for NewsAPI format
	 * Format specifies if source is URL or a local filepath
	 * Catches IOException 
	 * @return a ParserNewsObject
	 */
	@Override
	public ParserNews visit(ParserNews news, String source, format f) 
	{
		try {
			//if FILE format
			if (f.equals(format.FILE)) {
				//find file and map to ParserNews object
				byte[] jsonData = Files.readAllBytes(Paths.get(source));
				news = mapper.readValue(jsonData, ParserNews.class);
			}	
			//if URL format
			else if (f.equals(format.URL)) {
				//create URL object and pass to readValue(), map to ParserNews Object
				URL apiURL = new URI(source).toURL();
				news = mapper.readValue(apiURL, ParserNews.class);
				return news;
			}
			else {
				throw new IOException();
			}
		}
		catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
		return news; 
	}

	/*
	 * Visitor for Simple Article format
	 * Source must be File for simple articles. An IOException will occur otherwise. 
	 * Catches IOException 
	 * @return a ParserSimple Object
	 */
	@Override
	public ParserSimple visit(ParserSimple news, String source) {
		try {
			byte[] jsonData = Files.readAllBytes(Paths.get(source));
			return mapper.readValue(jsonData, ParserSimple.class);
		} 
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
