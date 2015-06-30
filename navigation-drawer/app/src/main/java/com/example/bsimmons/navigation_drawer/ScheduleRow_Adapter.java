package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 09/06/2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleRow_Adapter extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList<Game> games;

    public ScheduleRow_Adapter(Context context, int place, ArrayList<Game> games, String[] values) {
        super(context, R.layout.adapter_squareandtworow, values);
        this.context = context;
        this.games = games;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_squareandtworow, parent, false);
        TextView team1 = (TextView) rowView.findViewById(R.id.name_text);
        ImageView image1 = (ImageView) rowView.findViewById(R.id.team_image);
        TextView team2 = (TextView) rowView.findViewById(R.id.loss_text);
        ImageView image2 = (ImageView) rowView.findViewById(R.id.team2_image);
        TextView time = (TextView) rowView.findViewById(R.id.team_text);
        TextView date = (TextView) rowView.findViewById(R.id.text_date);


        team1.setText(" " + games.get(position).getTeam1());
        team2.setText(games.get(position).getTeam2() + " ");
        image1 = setTeamIcon(games.get(position).getTeam1(), image1);

        image2 = setTeamIcon(games.get(position).getTeam2(),image2);

        /*if(t2.equals("Picaroons")){
            image1.setImageResource(R.drawable.picaroons);
        }*/

        time.setText(" " + games.get(position).getLocation());
        date.setText(games.get(position).getDate() + " ");


        return rowView;
    }

    private ImageView setTeamIcon(String team, ImageView temp) {

        switch (team) {
            case "Fredericton Kia":
                temp.setImageResource(R.drawable.frederictonkia);
                break;
            case "Gunners":
                temp.setImageResource(R.drawable.gunners);
                break;
            case "Sporting":
                temp.setImageResource(R.drawable.sporting);
                break;
            case "Growlers United":
                temp.setImageResource(R.drawable.growlers);
                break;
            case "Picaroons":
                temp.setImageResource(R.drawable.picaroons);
                break;
            case "Rogue Galleons":
                temp.setImageResource(R.drawable.galleons);
                break;
            default:
                temp.setImageResource(R.drawable.soccerball);
                break;
        }
        return temp;
    }
}