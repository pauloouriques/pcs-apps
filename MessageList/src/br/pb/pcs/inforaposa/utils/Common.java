package br.pb.pcs.inforaposa.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Common {
	public static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
	
	public static Bitmap getImageBitmap(String url) {
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
	
	public static String parseDate(String date){
		Long now = Date.parse(FORMATTER.format(new Date()).trim());
		Integer result = (int) ((now - Date.parse(date))/1000);
		if (result < 10){
			return "Neste instante.";
		}else if (result < 60){
			return "Há " + result + " segundos";
		}else if (result/60 < 60){
			return "Há " + (result/60) + " minutos";
		}else if (result/60/60 < 24){
			return "Há " + (result/60/60) + " horas.";
		}else {
			return "Há " + (result/60/60/24) + " dias";
		}		
	}
	
}
