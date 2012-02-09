package org.developerworks.android;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.developerworks.android.utils.Item;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class Details extends Activity implements OnClickListener{
	
	private Bundle extras;
	private Dialog alert;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        
        extras = getIntent().getExtras();
        
        TextView title =  (TextView) findViewById(R.id.detailsTitle);
        title.setText(extras.getString("title"));
        TextView date =  (TextView) findViewById(R.id.detailsDate);
        date .setText(extras.getString("date"));
        TextView subtitle =  (TextView) findViewById(R.id.detailsSubTitle);
        subtitle.setText(extras.getString("subtitle"));
		final Button link = (Button) findViewById(R.id.detailsLink);
		link.setOnClickListener(this);
		final ImageView image = (ImageView) findViewById(R.id.detailsImage);
		if (!extras.getString("imgSrc").equals("http://globoesporte.globo.com")){
			image.setImageBitmap(getImageBitmap(extras.getString("imgSrc")));
		}
		
		final Button share = (Button) findViewById(R.id.detailsShare);
		share.setOnClickListener(this);
    }
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.detailsLink:
			Intent viewMessage = new Intent(Intent.ACTION_VIEW, 
					Uri.parse(extras.getString("link")));
			this.startActivity(viewMessage);
			break;
		case R.id.detailsShare:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, extras.getString("link"));
			startActivity(Intent.createChooser(intent, "Compartilhar via:"));
			break;	
		}
	}
	
	public Bitmap getImageBitmap(String url) {
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
}
