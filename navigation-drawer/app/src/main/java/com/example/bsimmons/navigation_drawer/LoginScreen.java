package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 08/06/2015.
 */
import android.app.Activity;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginScreen extends Activity {
    // Initializing variables
    EditText inputName;
    EditText inputPass;
    private String playerName;
    private String playerPass;
    private String playerTeam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        inputName = (EditText) findViewById(R.id.editText);
        inputPass= (EditText) findViewById(R.id.editText2);

        Button btnNextScreen = (Button) findViewById(R.id.btnNext);

        //new HttpAsyncTask().execute("http://bsimms2.byethost5.com/index_1?sql=" + str + ".php");

        //new HttpAsyncTask().execute("http://bsimms2.byethost5.com/index_1.php?sql=SELECT%20player_name,password%20FROM%20Player%20WHERE%20player_name%20LIKE%27%bob%%27");



        //Listening to button event
        btnNextScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                try {

                    System.out.println("Inputname:" + inputName.getText().toString());

                    //retrieve username and password from database
                    new HttpAsyncTask().execute("http://bsimms2.byethost5.com/index.php/login/" + inputName.getText().toString());

                    System.out.println("Playername:" + playerName + " playerPass: " + playerPass);


                }catch(Exception e){
                    System.out.println(e.getMessage());
                }



            }
        });
    }

    private void checkLogin(String name, String pass){
        System.out.println("checkname:" + name + " checkPass: " + pass);

        //check username and password against database
        if (name.equalsIgnoreCase(inputName.getText().toString()) && pass.equals(inputPass.getText().toString())) {
            //Starting a new Intent
            Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);

            //Sending data to another Activity
            nextScreen.putExtra("Name", inputName.getText().toString());
            nextScreen.putExtra("Password", inputPass.getText().toString());
            nextScreen.putExtra("Team",playerTeam);

            Log.e("Passing to MainActivity", inputName.getText() + "." + inputPass.getText());

            startActivity(nextScreen);
        } else {
            showError(2);
        }
    }

    private void showError(int err) {
        if(err == 1) {
            inputName.setError("Invalid player name");
        }else if (err==2){
            inputPass.setError("Invalid password");
        }
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getBaseContext(), "Updated!", Toast.LENGTH_LONG).show();
            try {
                JSONObject json = new JSONObject(result);

                //get json array
                JSONArray jsonarray = json.getJSONArray("games");
                json = jsonarray.getJSONObject(0);
                System.out.println(json.toString());
                playerName = json.optString("player_name");
                playerPass = json.optString("password");
                playerTeam = json.optString("team");
                checkLogin(playerName,playerPass);

            }
            catch(Exception e){
                e.getMessage();
                showError(1);
            }
        }
    }


}

