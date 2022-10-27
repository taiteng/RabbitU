package com.example.rabbitu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardAdapter extends BaseAdapter {

    Leaderboard leaderboard;
    ArrayList<String> nameList;
    ArrayList<String> minList;
    boolean first = true;
    int rank = 0;

    public LeaderboardAdapter(Leaderboard leaderboard, ArrayList<String> nameList , ArrayList<String> minList) {
        this.leaderboard = leaderboard;
        this.nameList = nameList;
        this.minList = minList;
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(leaderboard).inflate(R.layout.leaderboard_item, parent, false);


        TextView itemName, itemMin, itemRank;

        //List view item initialization
        itemName = convertView.findViewById(R.id.leaderboard_item_name);
        itemMin = convertView.findViewById(R.id.leaderboard_item_min);
        itemRank = convertView.findViewById(R.id.leaderboard_item_rank);

        itemName.setText(nameList.get(position)); //get Name
        itemMin.setText(minList.get(position)); //get Minutes
        itemRank.setText(String.valueOf(minList.indexOf(minList.get(position))+4)); //get Position number


        return convertView;
    }
}
