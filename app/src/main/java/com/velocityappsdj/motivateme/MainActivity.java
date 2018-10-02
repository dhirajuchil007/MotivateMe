package com.velocityappsdj.motivateme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    static TextView author;
    static TextView quote;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
   // static boolean valid;
    static int y=0;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref=this.getSharedPreferences("Lastten", Context.MODE_PRIVATE);
      MobileAds.initialize(this, "ca-app-pub-9540086841520699~5000751331");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("4E0FA1397589AB27")
                .addTestDevice("BF576BF490C34E2A")
                .build();
        mAdView.loadAd(adRequest);
        quote =(TextView)findViewById(R.id.quote_view);
        author=(TextView)findViewById(R.id.author_view);
        ImageView closeButton=(ImageView)findViewById(R.id.close_image);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView aboutImage =(ImageView) findViewById(R.id.app_icon);
        aboutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,About.class);
                startActivity(intent);
            }
        });
        int x=1;

        String nUrl="https://motivate-me-3f4d6.firebaseio.com/Quotes/A0.json";

              new GetNumData().execute(nUrl);




}

class GetUrlContentTask extends AsyncTask<String, Integer, String> {
    String content="";
    String result ="";

    protected String doInBackground(String... urls) {
        try {
            URL serachUrl=new URL(urls[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget= new HttpGet(serachUrl.toURI());

            HttpResponse response = httpclient.execute(httpget);

            if(response.getStatusLine().getStatusCode()==200){

                String server_response = EntityUtils.toString(response.getEntity());
                result =server_response;
                Log.d("Server response", server_response );
            } else {
                Log.i("Server response", "Failed to get server response" );
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        return result;

    }

    protected void onProgressUpdate(Integer... progress) {


    }

    protected void onPostExecute(String result) {
        // this is executed on the main thread after the process is over
        // update your UI here
        String qut=null;
        String aut=null;

        if(result.equals(null)||result=="")
        {MainActivity.quote.setText("Check Internet Connection");}
        else
        {
            try {
                JSONObject obj=new JSONObject(result);
                 qut=obj.getString("text");
                 aut=obj.getString("author");

            } catch (JSONException e) {
                e.printStackTrace();
            }

                MainActivity.quote.setText(qut);
                MainActivity.author.setText("- " + aut);
              //  MainActivity.valid=true;

        }


        Log.d("lol",qut);
    }
}
class GetNumData extends AsyncTask<String, Integer, String> {
    String content="";
    String result ="";

    protected String doInBackground(String... urls) {
        try {
            URL serachUrl=new URL(urls[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget= new HttpGet(serachUrl.toURI());

            HttpResponse response = httpclient.execute(httpget);

            if(response.getStatusLine().getStatusCode()==200){

                String server_response = EntityUtils.toString(response.getEntity());
                result =server_response;
                Log.d("Server response", server_response );
            } else {
                Log.i("Server response", "Failed to get server response" );
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        return result;

    }

    protected void onProgressUpdate(Integer... progress) {


    }

    protected void onPostExecute(String result) {
        // this is executed on the main thread after the process is over
        // update your UI here
        String qut=null;
        String aut=null;
        int y=0;
        if(result.equals(null)||result=="")
        {MainActivity.quote.setText("Check Internet connection");}
        else
        {
            try {
                JSONObject obj=new JSONObject(result);
                qut=obj.getString("text");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            y=Integer.parseInt(qut);
            //  MainActivity.valid=true;
            int x=1;
            String deleted="6,3,32,46,58.65,66,68,70,71,78,79,120,121,123,125,126,148,150,153,175,179,192,193,196,202.205,213,215,220,221,226,228,232,238,242,246,247,248,251,276,";
            boolean flag=false;
            do {
                Random r = new Random();
                do {
                    x = r.nextInt(y)+1;
                    Log.d("NUmbergen", ""+x);
                }
                while (deleted.contains(","+Integer.toString(x)+","));

                String last10=sharedPref.getString("Lastten","10,20,30,40,50,60,70,80,90,100");
                if(last10.contains(","+Integer.toString(x)+",")){
                    continue;
                }
                else{
                    last10=last10.substring(last10.indexOf(",")+1);
                    last10+=","+Integer.toString(x);
                    editor=sharedPref.edit();
                    editor.putString("Lastten",last10);
                    editor.commit();
                    flag=true;
                    Log.d("last",last10);


                }
            }
            while(flag==false   );
            String index="A"+Integer.toString(x);
            String sUrl="https://motivate-me-3f4d6.firebaseio.com/Quotes/"+index+".json";
            boolean valid=false;

            new GetUrlContentTask().execute(sUrl);

        }


        Log.d("Y",Integer.toString(MainActivity.y));
    }
}}