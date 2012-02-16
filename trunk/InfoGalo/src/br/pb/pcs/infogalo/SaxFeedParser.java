package br.pb.pcs.infogalo;

import java.net.UnknownHostException;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxFeedParser extends BaseFeedParser {

	protected SaxFeedParser(String feedUrl){
		super(feedUrl);
	}
	
	public List<Message> parse() throws UnknownHostException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			RssHandler handler = new RssHandler();
			parser.parse(this.getInputStream(), handler);
			return handler.getMessages();
		} catch (UnknownHostException e) {
			throw new UnknownHostException();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
}