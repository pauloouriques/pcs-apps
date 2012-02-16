package br.pb.pcs.inforaposa;

import br.pb.pcs.inforaposa.utils.Common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Details extends Activity implements OnClickListener{
	
	private Bundle extras;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        
        extras = getIntent().getExtras();
        
        TextView title =  (TextView) findViewById(R.id.detailsTitle);
        title.setText(extras.getString("title"));
        TextView date =  (TextView) findViewById(R.id.detailsDate);
        date .setText(Common.parseDate(extras.getString("date")));
        TextView subtitle =  (TextView) findViewById(R.id.detailsSubTitle);
        subtitle.setText(extras.getString("subtitle"));
		final Button link = (Button) findViewById(R.id.detailsLink);
		link.setOnClickListener(this);
		final ImageView image = (ImageView) findViewById(R.id.detailsImage);
		if (!extras.getString("imgSrc").equals("http://globoesporte.globo.com")){
			image.setImageBitmap(Common.getImageBitmap(extras.getString("imgSrc")));
			System.out.println(image.getWidth()+ " "+ image.getHeight());
		}else{
			image.setImageResource(R.drawable.no_image);
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
	

}
