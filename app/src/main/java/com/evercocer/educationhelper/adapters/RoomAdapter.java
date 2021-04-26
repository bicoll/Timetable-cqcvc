package com.evercocer.educationhelper.adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evercocer.educationhelper.R;
import com.evercocer.educationhelper.model.Room;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RoomAdapter extends BaseAdapter {
    private ArrayList<Room> rooms;
    private Context context;

    public RoomAdapter(ArrayList<Room> rooms, Context context) {
        this.rooms = rooms;
        this.context = context;
    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    @Override
    public Object getItem(int position) {
        return rooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_roomlist, parent, false);
            holder = new ViewHolder();
            holder.classRoom = convertView.findViewById(R.id.textView);
            holder.number = convertView.findViewById(R.id.textView2);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        Room room = rooms.get(position);
        String s = room.getClassRoom() + "--" + room.getCampus() + "--" + room.getVolume();
        Log.d(TAG, s);
        holder.classRoom.setText(s);
        holder.number.setText(room.getNumber());
        return convertView;
    }

    public static class ViewHolder {
        TextView classRoom;
        TextView number;
    }

}


