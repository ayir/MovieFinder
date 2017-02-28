package com.example.riyasharma.moviefinder;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button search;
    EditText movieText;
    ProgressBar progressBar;
    TextView titleView;
    TextView genreView;
    TextView plotView;
    TextView releasedView;
    TextView ratingView;
    String movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = (Button) findViewById(R.id.queryButton);
        movieText = (EditText) findViewById(R.id.emailText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        titleView = (TextView) findViewById(R.id.titleView);
        genreView=(TextView)findViewById(R.id.genreView);
        plotView=(TextView)findViewById(R.id.plotView);
        releasedView=(TextView)findViewById(R.id.releasedView);
        ratingView=(TextView)findViewById(R.id.ratingView);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                movie = movieText.getText().toString().trim().replaceAll(" ", "+");
                RetrieveData rd = new RetrieveData();
                rd.execute();
            }
        });
    }

    private class RetrieveData extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            titleView.setText("");
            plotView.setText("");
            releasedView.setText("");
            ratingView.setText("");
            genreView.setText("");
        }

        protected String doInBackground(Void... urls) {

            // Do some validation here

            try {
                URL url = new URL("http://www.omdbapi.com/?t="+movie+"&y=&plot=short&r=json");//http://img.omdbapi.com/?i=tt2294629&apikey=a12c8f58
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            progressBar.setVisibility(View.GONE);
            if (response == null) {
                response = "THERE WAS AN ERROR";
                titleView.setText(response);
            } else {

                String title = "";
                String Genre = "";
                String date = "";
                String plot = "";
                String rating = "";
                Log.i("INFO", response);
                try {
                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                    title = object.getString("Title");
                    Genre = object.getString("Genre");
                    date = object.getString("Released");
                    plot = object.getString("Plot");
                    rating = object.getString("Rated");

                } catch (JSONException e) {
                    // Appropriate error handling code
                }
                titleView.setText("TITLE:" + title);
                genreView.setText("GENRE:" + Genre);
                releasedView.setText("RELEASED:" + date);
                plotView.setText("PLOT:" + plot);
                ratingView.setText("RATING:" + rating);
            }
        }
    }
}