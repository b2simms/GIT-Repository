package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 12/06/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bsimmons on 09/06/2015.
 */

public class MessagingRow_Adapter extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList<MessageInfo> info;

    public MessagingRow_Adapter(Context context, ArrayList<MessageInfo> info, String[] values) {
        super(context, R.layout.adapter_messagingrow, values);
        this.context = context;
        this.info = info;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_messagingrow, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.From);
        ImageView team_image = (ImageView) rowView.findViewById(R.id.icon);
        TextView sub = (TextView) rowView.findViewById(R.id.subject);
        TextView content = (TextView) rowView.findViewById(R.id.content);
        TextView date = (TextView) rowView.findViewById(R.id.date);


        name.setText(" " + info.get(position).getName());
        team_image = setTeamIcon(info.get(position).getTeam(), team_image);
        sub.setText(" " + info.get(position).getSub());
        content.setText(" " + info.get(position).getContent());
        date.setText(" " + info.get(position).getDate());

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