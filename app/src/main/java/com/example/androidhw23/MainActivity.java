package com.example.androidhw23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView=(TextView)findViewById(R.id.textView);
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Create background thread to connect and get data
            new TranTask().execute("https://atm201605.appspot.com/h");
        } else {
            textView.setText("No network connection available.");
        }
       /* OkHttpClient client=new OkHttpClient();

        Request request=new Request.Builder()
                            .url("http://atm201605.appspot.com/h")
                            .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                TextView textView=(TextView)findViewById(R.id.textView);
                textView.setText("連線失敗!");
                //告知使用者連線失敗
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json=response.body().string();
                        Log.d("OKHTTP",json);
                //解析JSON
                parseJSON(json);
            }
        });

        */

       // new TranTask().execute("http://atm201605.appspot.com/h");

    }

   class TranTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder sb = new StringBuilder();
            try {
                return downloadUrl(urls[0]);

                }

            catch (IOException e){
                return "Unable to retrieve web page. URL may be invalid.";
            }

        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            Log.d("JSON",s);
            parseJSON(s);
        }
    }
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);    //read timeout，是传递数据的超时时间。
            conn.setConnectTimeout(15000 /* milliseconds */); //connect timeout 是建立连接的超时时间；
            conn.setRequestMethod("GET");
            conn.setDoInput(true);//httpUrlConnection.setDoInput(true);以后就可以使用conn.getInputStream().read();
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();//A status code of 200 indicates success.
            Log.d("Debug", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
    private void parseJSON(String s){

        TextView textView=(TextView)findViewById(R.id.textView);

textView.setText(s);

    }




}
