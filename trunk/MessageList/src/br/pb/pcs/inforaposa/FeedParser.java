package br.pb.pcs.inforaposa;
import java.net.UnknownHostException;
import java.util.List;

public interface FeedParser {
	List<Message> parse() throws UnknownHostException;
}
