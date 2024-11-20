package project_3;
import project_3.FormatAcceptor.source;
import project_3.FormatAcceptor.format;

public class Client {

	public static void main(String[] args) {
		//file paths
		String simpleFile = "inputs/simple.txt";
		String newsFile = "inputs/newsapi.txt";
		String newsApiURL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=3a1ad67f3a504678800bbdfc70b37696";
		
		//the format acceptor correctly matches the format of the filepath or url
		FormatAcceptor simple = new FormatAcceptor(format.file, source.simple, simpleFile);
		simple.run();
		FormatAcceptor news = new FormatAcceptor(format.file, source.newsapi, newsFile);
		news.run();
		FormatAcceptor url = new FormatAcceptor(format.url, source.newsapi, newsApiURL);
		url.run();
	}

}
