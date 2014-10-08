package com.blogspot.vsvydenko.reutersreader.ui;

import com.blogspot.vsvydenko.reutersreader.R;
import com.blogspot.vsvydenko.reutersreader.entity.FeedItem;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vsvydenko on 30.06.14.
 */
public class FeedsAdapter extends BaseAdapter {

    private Context mContext;
    private List<FeedItem> mItems;

    public FeedsAdapter(Context mContext, List<FeedItem> items) {
        this.mContext = mContext;
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_feed, parent, false);
        }

        TextView titleTextView = ViewHolder.get(convertView, R.id.txtTitle);
        TextView pubDateTextView = ViewHolder.get(convertView, R.id.txtPubDate);
        TextView descriptionTextView = ViewHolder.get(convertView, R.id.txtDescription);

        FeedItem feedItem = (FeedItem) getItem(position);

        titleTextView.setText(feedItem.getTitle());
        pubDateTextView.setText(feedItem.getPubDate());
        descriptionTextView.setText(feedItem.getDescription());

        return convertView;
    }

    public static class ViewHolder {

        // I added a generic return type to reduce the casting noise in client code
        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }
    }
}
