package Unused;

import java.io.IOException;

import project_3.ParserNews;
import project_3.ParserSimple;

/*
 * Contract for visitor subclasses 
 */
public abstract class Visitor {
	//format must be FILE or URL
	enum format {FILE, URL};
	
	//NewsParser constructor visitor, 
	//requires format to be specified, source path (url or filepath) and ParserNews object
	// @return a ParserNews object
	abstract ParserNews visit(ParserNews news, String source, format f);
	
	//Simple Parser constructor visitor, 
	//requires source path (url or filepath) and ParserNews object
	// @return a ParserNews object
	abstract ParserSimple visit(ParserSimple news, String source);
	
}
