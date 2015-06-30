package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 11/06/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

public class Messaging_fragment extends Fragment {

    private ArrayList<MessageInfo> messages;
    private ListView listView ;
    private String team_selected;
    private int selected_Item;
    private boolean already_selected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_messaging,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onStart() {
        super.onStart();


        Button newMessage = (Button) getView().findViewById(R.id.new_message_button);
        //Listening to button event
        newMessage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                try {
                    FragmentManager fm = getActivity().getSupportFragmentManager();

                    fm.beginTransaction()
                            .replace(R.id.container, new New_Message_fragment())
                            .commit();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


            }
        });


        new MessagingAsyncTask().execute("http://bsimms2.byethost5.com/index.php/messages");
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
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    private class MessagingAsyncTask extends AsyncTask<String, Void, String> {
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

                messages = new ArrayList<MessageInfo>();

                //get json array
                JSONArray jsonarray = json.getJSONArray("games");

                for (int i = 0; i < jsonarray.length(); i++) {
                    json = jsonarray.getJSONObject(i);

                    MessageInfo info = new MessageInfo();

                    info.setName(json.optString("name"));
                    info.setSub(json.optString("subject"));
                    info.setTeam(json.optString("team"));
                    info.setContent(json.optString("content"));
                    info.setDate(json.optString("date"));


                    messages.add(info);

                }

                // Get ListView object from xml
                listView = (ListView) getView().findViewById(R.id.MessageList);

                // Defined Array values to show in ListView
                String[] values = new String[messages.size()];

                for(int i=0;i<messages.size()-1;i++){
                    values[i] = messages.get(i).getTeam();
                }


                // Define a new Adapter
                // First parameter - Context
                // Second parameter - Layout for the row
                // Third parameter - ID of the TextView to which the data is written
                // Fourth - the Array of data

                MessagingRow_Adapter adapter = new MessagingRow_Adapter(getActivity(),messages, values);

                // Assign adapter to ListView
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Object o = listView.getItemAtPosition(position);


                        if(selected_Item == position && already_selected == false){
                            listView.getChildAt(position).setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                            already_selected = true;
                        }
                        else {
                            if(selected_Item == position && already_selected == true){
                                listView.getChildAt(position).setBackgroundColor(getResources().getColor(android.R.color.white));
                                already_selected = false;
                            }
                            else {
                                if(already_selected==false)
                                    already_selected = true;
                                listView.getChildAt(selected_Item).setBackgroundColor(getResources().getColor(android.R.color.white));
                                listView.getChildAt(position).setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                            }
                        }
                        selected_Item = position;

                        Toast.makeText(getActivity(),"WORKS:" + selected_Item + already_selected, Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
