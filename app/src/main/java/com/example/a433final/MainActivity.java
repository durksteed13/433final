package com.example.a433final;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class MainActivity extends AppCompatActivity {

    String currencyName;

    ArrayList<Info> infos;






    private static final String USGS_REQUEST_URL =
            "http://data.fixer.io/api/latest?access_key=4c4a98631c11dcc16fe6f242aff5e153";

    // use DAY_OF_MONTH to get data



    //ArrayList<Info> pairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_list);




        infos = new ArrayList<Info>();

        // call task to get data

        mainAsyncTask task = new mainAsyncTask();
        task.execute();

        //addPairs();




    }







    private void updateUi(Event price) {

        infos.add(new Info("EUR/AUD",Double.toString(price.AUD)));

        infos.add(new Info("EUR/BTC",Double.toString(price.BTC)));

        infos.add(new Info("EUR/CAD",Double.toString(price.CAD)));

        infos.add(new Info("EUR/CNY",Double.toString(price.CNY)));

        infos.add(new Info("EUR/GBP",Double.toString(price.GBP)));

        infos.add(new Info("EUR/NZD",Double.toString(price.NZD)));

        infos.add(new Info("EUR/RUB",Double.toString(price.RUB)));

        infos.add(new Info("EUR/USD",Double.toString(price.USD)));

        infos.add(new Info("EUR/BRL",Double.toString(price.BRL)));

        infos.add(new Info("EUR/INR",Double.toString(price.INR)));

        infos.add(new Info("EUR/MXN",Double.toString(price.MXN)));

        infos.add(new Info("EUR/JPY",Double.toString(price.JPY)));

        InfoAdapter infoAdapter=new InfoAdapter(this, infos,R.color.category_main);

        final ListView listView=(ListView)findViewById(R.id.list);

        listView.setAdapter(infoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < 12){

                    String s = infos.get(position).pName;

                    String[] partS = s.split("/");

                    currencyName = partS[1];



                    Intent myintent=new Intent(MainActivity.this,GraphActivity.class);
                    myintent.putExtra("price", infos.get(position).price);
                    myintent.putExtra("name", currencyName);




                    startActivity(myintent);

                }

            }
        });




    }


    private class mainAsyncTask extends AsyncTask<URL, Void, Event> {

        @Override
        protected Event doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(USGS_REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }



            // Extract relevant fields from the JSON response and create an {@link Event} object
            Event price = extractFeatureFromJson(jsonResponse);


            return price;
        }

        /**
         * Update the screen with the given date (which was the result of the
         * {@link mainAsyncTask}).
         */
        @Override
        protected void onPostExecute(Event price) {
            if (price == null) {
                return;
            }

            updateUi(price);
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
                urlConnection.setReadTimeout(100000 /* milliseconds */);
                urlConnection.setConnectTimeout(150000 /* milliseconds */);
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

        private Event extractFeatureFromJson(String priceJSON) {

            if(TextUtils.isEmpty(priceJSON)){
                return null;
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(priceJSON);
                JSONObject rate = baseJsonResponse.getJSONObject("rates");


                double AUD= rate.getDouble("AUD");

                double BTC = rate.getDouble("BTC");

                double CAD = rate.getDouble("CAD");

                double CNY = rate.getDouble("CNY");

                double GBP = rate.getDouble("GBP");

                double NZD = rate.getDouble("NZD");

                double RUB = rate.getDouble("RUB");

                double USD = rate.getDouble("USD");

                double BRL = rate.getDouble("ALL");

                double INR = rate.getDouble("INR");

                double MXN = rate.getDouble("MXN");

                double JPY = rate.getDouble("JPY");




                Log.v("Tag2", "get title");


                // Create a new {@link Event} object
                return new Event(AUD,BTC,CAD,CNY,GBP,NZD,RUB,USD,BRL,INR,MXN,JPY );

            } catch (JSONException e) {

            }
            return null;
        }
    }








}
