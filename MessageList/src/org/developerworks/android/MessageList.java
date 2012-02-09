package org.developerworks.android;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MessageList extends ListActivity {
	
	private List<Message> messages;
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        loadFeed();
    }
    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		ArrayAdapter<Message> adapter = new MessageItemAdapter(getApplicationContext(), R.layout.listitem, this.messages);
		if (adapter.getCount() > 0){
			adapter.clear();
		}
		this.loadFeed();
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, Details.class);
		intent.putExtra("link", messages.get(position).getLink().toExternalForm());
		intent.putExtra("title", messages.get(position).getTitle());
		intent.putExtra("date", messages.get(position).getDate().toString());
		intent.putExtra("imgSrc", parseImg(messages.get(position).getDescription()));
		intent.putExtra("subtitle", parseSubtitle(messages.get(position).getDescription()));
    	startActivity(intent);
	}

	private void loadFeed(){
    	try{
    		Log.i("AndroidNews", "ParserType: SAX");
	    	FeedParser parser = FeedParserFactory.getParser();
	    	long start = System.currentTimeMillis();
	    	messages = parser.parse();
	    	long duration = System.currentTimeMillis() - start;
	    	Log.i("AndroidNews", "Parser duration=" + duration);
	    	String xml = writeXml();
	    	Log.i("AndroidNews", xml);
	    	List<String> titles = new ArrayList<String>(messages.size());
	    	for (Message msg : messages){
	    		if (msg.getTitle().length() < 200)
	    			titles.add(msg.getTitle());
	    	}
	    	this.messages.remove(0);
	    	MessageItemAdapter adapter = new MessageItemAdapter(getApplicationContext(), R.layout.listitem, this.messages);
	    	this.setListAdapter(adapter);
    	} catch (Throwable t){
    		Log.e("AndroidNews",t.getMessage(),t);
    	}
    }
    
	private String writeXml(){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "messages");
			serializer.attribute("", "number", String.valueOf(messages.size()));
			for (Message msg: messages){
				serializer.startTag("", "message");
				serializer.attribute("", "date", msg.getDate());
				serializer.startTag("", "title");
				serializer.text(msg.getTitle());
				serializer.endTag("", "title");
				serializer.startTag("", "url");
				serializer.text(msg.getLink().toExternalForm());
				serializer.endTag("", "url");
				serializer.startTag("", "body");
				serializer.text(msg.getDescription());
				serializer.endTag("", "body");
				serializer.endTag("", "message");
			}
			serializer.endTag("", "messages");
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public class MessageItemAdapter extends ArrayAdapter<Message> {
		private List<Message> messages;

		public MessageItemAdapter(Context context, int textViewResourceId, List<Message> messages) {
			super(context, textViewResourceId, messages);
			this.messages = messages;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.listitem, null);
			}

			Message message = messages.get(position);
			if (message != null) {
				TextView title = (TextView) v.findViewById(R.id.title);
				TextView date = (TextView) v.findViewById(R.id.date);
				ImageView image = (ImageView) v.findViewById(R.id.image);

				if (title != null) {
					title.setText(message.getTitle());
				}

				if(date != null) {
					date.setText(message.getDate());
				}
				
				if(image != null) {
					if (!parseImg(message.getDescription()).equals("http://globoesporte.globo.com")){
						image.setImageBitmap(getImageBitmap(parseImg(message.getDescription())));
					}
				}
			}
			return v;
		}
	}
	
	Bitmap getImageBitmap(String url) {
        Bitmap bm = null; 
        try { 
            URL aURL = new URL(url); 
            URLConnection conn = aURL.openConnection(); 
            conn.connect(); 
            InputStream is = conn.getInputStream(); 
            BufferedInputStream bis = new BufferedInputStream(is); 
            bm = BitmapFactory.decodeStream(bis); 
            bis.close(); 
            is.close(); 
       } catch (IOException e) { 
           Log.e("ERRO AQUI", "Error getting bitmap", e); 
       } 
       return bm; 
    } 
	
	public String parseImg(String description){
		String aux = description.substring(description.indexOf("src='") + 5);
		String imgPath = aux.substring(0, aux.indexOf("'"));
		return imgPath;
	}
	
	public String parseSubtitle(String description){
		String imgPath = description.substring(description.indexOf("</a><br />") + 10);
		return imgPath;
	}
	
}