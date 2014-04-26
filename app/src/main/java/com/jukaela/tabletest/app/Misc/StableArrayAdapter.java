package com.jukaela.tabletest.app.Misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jukaela.tabletest.app.AsyncTasks.ImageLoaderTask;
import com.jukaela.tabletest.app.Models.ImageResponseObject;
import com.jukaela.tabletest.app.R;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class StableArrayAdapter extends ArrayAdapter<ImageResponseObject> {
    ArrayList<ImageResponseObject> objects;
    Context context;
    LayoutInflater layoutInflater;

    public StableArrayAdapter(Context context, int textViewResourceId, ArrayList<ImageResponseObject> objects) {
        super(context, textViewResourceId, objects);

        this.context = context;
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_item, parent, false);

            assert convertView != null;

            holder = new ViewHolder();

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            TextView textView = (TextView) convertView.findViewById(R.id.title);

            holder.imageView = imageView;
            holder.textView = textView;
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageResponseObject object = objects.get(position);

        if (object != null) {
            holder.textView.setText(object.getFullCaption());

            if (holder.imageView != null) {
                try {
                    new ImageLoaderTask(holder.imageView).execute(object.getThumbnailUrl());
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}