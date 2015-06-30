package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 11/06/2015.
 */

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class Schedule_fragment extends Fragment {

    private ArrayList<Game> games;
    private ListView listView;
    private String team_selected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


        Intent i = getActivity().getIntent();
        team_selected = i.getStringExtra("Team");

        new SpinnerAsyncTask().execute("http://bsimms2.byethost5.com/index.php/team");
    }


    public static String GET(String url) {
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
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected() {
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


            try {
                Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_LONG).show();

                JSONObject json = new JSONObject(result);


                //get json array
                JSONArray jsonarray = json.getJSONArray("games");

                for (int i = 0; i < jsonarray.length(); i++) {
                    json = jsonarray.getJSONObject(i);

                    teamlist.add(json.optString("team1"));
                }

                // Locate the spinner in activity_main.xml
                Spinner mySpinner = (Spinner) getView().findViewById(R.id.my_spinner);

                // Spinner adapter
                mySpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, teamlist));

                for (int i = 0; i < teamlist.size(); i++) {
                    if (team_selected.equals(teamlist.get(i))) {
                        String[] team_arr = team_selected.split("\\s+");
                        if (team_arr.length > 1) {
                            team_selected = team_arr[0] + "%20" + team_arr[1];

                        }
                        mySpinner.setSelection(i);

                    }
                }


                // Spinner on item click listener
                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

                        String teamSelected = teamlist.get(position);

                        String[] splitStr = teamSelected.split("\\s+");
                        if (splitStr.length > 1) {
                            teamSelected = splitStr[0] + "%20" + splitStr[1];

                        }

                        new HttpAsyncTask().execute("http://bsimms2.byethost5.com/index.php/schedule/" + teamSelected);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

                ///SPINNER END

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private int setMonthInt(String month) {
        switch (month) {
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;
            case "Dec":
                return 12;
            default:
                return -1;
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
                    game.setDate(json.optString("date"));
                    game.setLocation(json.optString("location"));

                    games.add(game);

                }

                Spinner mySpinner = (Spinner) getView().findViewById(R.id.my_spinner);
                int pos = mySpinner.getSelectedItemPosition();


                // Get ListView object from xml
                listView = (ListView) getView().findViewById(R.id.listView);

                // Defined Array values to show in ListView
                String[] values = new String[games.size()];

                for (int i = 0; i < games.size() - 1; i++) {
                    values[i] = games.get(i).getGame_id();
                }


                // Define a new Adapter
                // First parameter - Context
                // Second parameter - Layout for the row
                // Third parameter - ID of the TextView to which the data is written
                // Fourth - the Array of data

                ScheduleRow_Adapter adapter = new ScheduleRow_Adapter(getActivity(), pos, games, values);

                // Assign adapter to ListView
                listView.setAdapter(adapter);


                GregorianCalendar gCalend = new GregorianCalendar();

                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                String[] splitTime = currentDateTimeString.split("\\s+");
                String[] splitDate = splitTime[1].split(",");

                int listViewSelection = 0;

                for (int i = 0; i < games.size(); i++) {

                    String[] splitStr = games.get(i).getDate().split("-");

                    if (setMonthInt(splitStr[0]) >= setMonthInt(splitTime[0])) {
                        if ((Integer) Integer.parseInt(splitStr[1]) >= (Integer) Integer.parseInt(splitDate[0])) {
                            listViewSelection = i;
                            break;
                        }
                    }

                }

                listView.setSelection(listViewSelection);

                // Spinner on item click listener
                listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        System.out.println("onItemSelected Listview ");

                    }

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // ListView Clicked item index
                        int itemPosition = position;

                        // ListView Clicked item value
                        String itemValue = (String) listView.getItemAtPosition(position);

                        // Show Location and Day
                        Toast.makeText(getActivity(),
                                "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

                ///SPINNER END

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
