package com.evercocer.educationhelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evercocer.educationhelper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChapterListAdapter extends BaseAdapter {
    private Context mContext;

    private String[] chapterInfo;

    public ChapterListAdapter(Context mContext, String[] chapterInfo) {
        this.mContext = mContext;
        this.chapterInfo = chapterInfo;
    }

    public ChapterListAdapter() {
    }

    @Override
    public int getCount() {
        return chapterInfo.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder ;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_lv_chapter, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mLinearLayout = convertView.findViewById(R.id.ll_main);
            mViewHolder.tv_chapter = convertView.findViewById(R.id.tv_chapter);
            mViewHolder.tv_date = convertView.findViewById(R.id.tv_date);

            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();

        String startDate = chapterInfo[position];
        mViewHolder.tv_date.setText(startDate);
        mViewHolder.tv_chapter.setText(String.valueOf(position + 1));
        return convertView;
    }

    public class ViewHolder {
        private LinearLayout mLinearLayout;
        private TextView tv_chapter;
        private TextView tv_date;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
