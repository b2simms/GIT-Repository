package com.example.bsimmons.navigation_drawer;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ScheduleOLD_fragment extends Fragment {

    ArrayList<Game> games;
    ListView listView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_schedule,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();


        new SpinnerAsyncTask().execute("http://bsimms2.byethost5.com/index_3.php");

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }



    private class SpinnerAsyncTask extends AsyncTask<String, Void, String> {

        ///SPINNER START
        ArrayList<String> teamlist = new ArrayList<String>();

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getActivity().getBaseContext(), "Updated!", Toast.LENGTH_LONG).show();
            try {
                JSONObject json = new JSONObject(result);



                //get json array
                JSONArray jsonarray = json.getJSONArray("games");

                for (int i = 0; i < jsonarray.length(); i++) {
                    json = jsonarray.getJSONObject(i);

                    teamlist.add(json.optString("team"));
                }

                // Locate the spinner in activity_main.xml
                Spinner mySpinner = (Spinner)  getView().findViewById(R.id.my_spinner);

                // Spinner adapter
                mySpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, teamlist));

                // Spinner on item click listener
                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

                        String teamSelected = teamlist.get(position);

                        new HttpAsyncTask().execute("http://bsimms2.byethost5.com/index_2.php?team=" + teamSelected);

                    }

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // ListView Clicked item index
                        int itemPosition     = position;

                        // ListView Clicked item value
                        String  itemValue    = (String) listView.getItemAtPosition(position);

                        // Show Alert
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                                .show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

                ///SPINNER END

            } catch(Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

        }
    }




    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getActivity().getBaseContext(), "Updated!", Toast.LENGTH_LONG).show();
            try {
                JSONObject json = new JSONObject(result);

                ///SPINNER START

                games = new ArrayList<Game>();

                //get json array
                JSONArray jsonarray = json.getJSONArray("games");

                for (int i = 0; i < jsonarray.length(); i++) {
                    json = jsonarray.getJSONObject(i);

                    Game game = new Game();

                    game.setGame_id(json.optString("game_id"));
                    game.setTeam1(json.optString("team1"));
                    game.setTeam2(json.optString("team2"));
                    game.setDay(json.optString("day"));
                    game.setTime(json.optString("time"));

                    games.add(game);

                }

                Spinner mySpinner = (Spinner)  getView().findViewById(R.id.my_spinner);
                int pos = mySpinner.getSelectedItemPosition();


                // Get ListView object from xml
                listView = (ListView)  getView().findViewById(R.id.listView);

                // Defined Array values to show in ListView
                String[] values = new String[games.size()];

                for(int i=0;i<games.size()-1;i++){
                    values[i] = games.get(i).getGame_id();
                }


                // Define a new Adapter
                // First parameter - Context
                // Second parameter - Layout for the row
                // Third parameter - ID of the TextView to which the data is written
                // Fourth - the Array of data

                ScheduleRow_Adapter adapter = new ScheduleRow_Adapter(getActivity(),pos,games,values);

                // Assign adapter to ListView
                listView.setAdapter(adapter);



                ///SPINNER END

            } catch(Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

        }
    }

}