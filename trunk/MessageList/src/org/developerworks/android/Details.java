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
			createDialog();
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
	
	private void createDialog(){
		final Item[] items = {
			    new Item("Facebook", R.drawable.facebook),
			    new Item("Twitter", R.drawable.twitter),
			    new Item("Email", R.drawable.email),
			};
		
		ListAdapter adapter = new ArrayAdapter<Item>(
			this, android.R.layout.select_dialog_item,
		    android.R.id.text1, items){
	        public View getView(int position, View convertView, ViewGroup parent) {
	            View v = super.getView(position, convertView, parent);
	            TextView tv = (TextView)v.findViewById(android.R.id.text1);
	            tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);
	            int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
	            tv.setCompoundDrawablePadding(dp5);
	            return v;
	        }
		};
			
    	AlertDialog.Builder builder = new AlertDialog.Builder(Details.this);
    	builder.setTitle("Compartilhar via:");
    	builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            

			public void onClick(DialogInterface dialog, int item) {
            	
            	if (items[item].toString().equals("Visite a p�gina do im�vel")){
            		String url = "http://www.tecnisa.com.br/";
            		Intent intent = new Intent(Intent.ACTION_VIEW);  
            		intent.setData(Uri.parse(url));  
            		startActivity(intent);
            	}
            	else if (items[item].toString().equals("Twitter")){
            		alert.dismiss();
            	}
            	else if (items[item].toString().equals("Facebook")){
            		alert.dismiss();
            	}
            }
    	});
    	builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {}
        });
    	
    	alert = builder.create();
    	alert.show();
	}
	
	
}
