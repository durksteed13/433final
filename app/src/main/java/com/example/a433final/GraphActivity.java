package com.example.a433final;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;


import com.example.a433final.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

public class GraphActivity extends AppCompatActivity {

    PlotView pv;

    ArrayList<Day> days =new ArrayList<Day>(7);

    String pair;

    // get DAY_OF_MONTH

    int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    String currentDayString = String.valueOf(currentDay);

    TextView databox;

    // To Do
    // make sure date work
    String URL_For_Each_Day =
            "http://data.fixer.io/api/2019-04-" + currentDayString + "?access_key=4c4a98631c11dcc16fe6f242aff5e153&base=EUR&symbols=AUD,BTC,CAD,CNY,GBP,NZD,RUB,USD,BRL,INR,MXN,JPY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_graph);

        databox = findViewById(R.id.databox);

        pv = (PlotView) findViewById(R.id.plotview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            pair = extras.getString("name");
            TextView t =(TextView) findViewById(R.id.pair);
            t.setText(pair);
            String price = extras.getString("price");
            TextView u = (TextView) findViewById(R.id.price);
            u.setText(price);


            for (int i = 0;i < 6;i ++){




                secondAsyncTask task = new secondAsyncTask();
                task.execute();




                Log.v("cd", currentDayString);







            }

            int s2 = days.size();
            String str2 = Integer.toString(s2);

            Log.v("sm", str2);

            //Log.v("d", days.get(0).date+ " "+ days.get(1).date + " "+ days.get(2).date);




            //The key argument here must match that used in the other activity
        }

        /*
        for(int i = 0; i < 7; i++) {
            double random = Math.random() * 49 + 1;
            pv.addPoint((float)random);
        }
        */




    }

    public void updateHistory(Day date){

        days.add(date);

        int s = days.size();
        String str1 = Integer.toString(s);

        Log.v("size", str1);

        if(s == 6){
            Log.v("dq", days.get(0).date+ " "+ days.get(1).date + " "+ days.get(2).date+" "+days.get(3).date+ " "+ days.get(4).date + " "+ days.get(5).date);
            Log.v("dp", days.get(0).pPrice+ " "+ days.get(1).pPrice + " "+ days.get(2).pPrice);


            for(int i =0;i<6;i++){
                pv.addPoint((float)(double)days.get(i).pPrice);
                pv.invalidate();

//                if(i != 0) {
//                    databox.setText(databox.getText() + "\n" + "Date: " + days.get(i).date + "\t" + days.get(i).pPrice);
//                }
//                databox.setText(databox.getText() + "Date: " + days.get(i).date + "\t" + days.get(i).pPrice);



            }



        }


        currentDay = currentDay - 1;
        currentDayString = String.valueOf(currentDay);
        URL_For_Each_Day =
                "http://data.fixer.io/api/2019-04-" + currentDayString + "?access_key=4c4a98631c11dcc16fe6f242aff5e153&base=EUR&symbols=AUD,BTC,CAD,CNY,GBP,NZD,RUB,USD,BRL,INR,MXN,JPY";



        return;

    }





    private class secondAsyncTask extends AsyncTask<URL, Void, Day> {

        @Override
        protected Day doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(URL_For_Each_Day);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }



            // Extract relevant fields from the JSON response and create an {@link Event} object
            Day date = extractFeatureFromJson(jsonResponse);







            return date;
        }


        @Override
        protected void onPostExecute(Day date) {
            if (date == null) {
                return;
            }

            Log.v("testD", "onPostExecute : "+date.date);


            updateHistory(date);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {

                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";


            if (url == null){
                return jsonResponse;
            }
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }


        private Day extractFeatureFromJson(String dateJSON) {

            if(TextUtils.isEmpty(dateJSON)){
                return null;
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(dateJSON);

                String date = baseJsonResponse.getString("date");

                JSONObject rate = baseJsonResponse.getJSONObject("rates");


                double value= rate.getDouble(pair);


                // Create a new {@link Event} object
                return new Day(date,value );

            } catch (JSONException e) {

            }
            return null;
        }
    }

}
