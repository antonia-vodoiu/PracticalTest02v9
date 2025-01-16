package ro.pub.cs.systems.eim.practicaltest02v9;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PracticalTest02MainActivityv9 extends AppCompatActivity {

    private EditText wordInput;
    private EditText minLettersInput;
    private TextView resultsTextView;

    private BroadcastReceiver parsedWordsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String parsedWords = intent.getStringExtra("parsedWords");
            if (parsedWords != null) {
                resultsTextView.setText(parsedWords);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v9_main);

        wordInput = findViewById(R.id.wordInput);
        minLettersInput = findViewById(R.id.minLettersInput);
        resultsTextView = findViewById(R.id.resultsTextView);
        Button searchButton = findViewById(R.id.searchButton);
        Button openMapButton = findViewById(R.id.openMapButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = wordInput.getText().toString();
                int minLetters = Integer.parseInt(minLettersInput.getText().toString());
                fetchAnagrams(word, minLetters);
            }
        });
        openMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(PracticalTest02MainActivityv9.this, MapsActivity.class);
                startActivity(mapIntent);
            }
        });

        // Register the local broadcast receiver
        IntentFilter filter = new IntentFilter("ro.pub.cs.systems.eim.practicaltest02v9.PARSED_WORDS");
        LocalBroadcastManager.getInstance(this).registerReceiver(parsedWordsReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the local broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(parsedWordsReceiver);
    }

    private void fetchAnagrams(String word, int minLetters) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.anagramica.com/all/:" + word);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    String jsonResponseString = response.toString();

                    // Log the complete JSON response
                    Log.d("JSON Response", jsonResponseString);

                    JSONObject jsonResponse = new JSONObject(jsonResponseString);
                    JSONArray allAnagrams = jsonResponse.getJSONArray("all");

                    StringBuilder filteredAnagrams = new StringBuilder();

                    for (int i = 0; i < allAnagrams.length(); i++) {
                        String anagram = allAnagrams.getString(i);
                        if (anagram.length() >= minLetters) {
                            filteredAnagrams.append(anagram).append("\n");
                        }
                    }

                    String filteredAnagramsString = filteredAnagrams.toString();

                    // Log results
                    Log.d("Anagrams", filteredAnagramsString);

                    // Send local broadcast with parsed words
                    sendLocalBroadcastWithParsedWords(filteredAnagramsString);

                } catch (Exception e) {
                    Log.e("Error", e.getMessage(), e);
                }
            }
        }).start();
    }

    private void sendLocalBroadcastWithParsedWords(String parsedWords) {
        Intent intent = new Intent("ro.pub.cs.systems.eim.practicaltest02v9.PARSED_WORDS");
        intent.putExtra("parsedWords", parsedWords);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}