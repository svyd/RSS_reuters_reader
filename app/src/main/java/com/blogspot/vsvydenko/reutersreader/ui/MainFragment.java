package com.blogspot.vsvydenko.reutersreader.ui;

import com.blogspot.vsvydenko.reutersreader.R;
import com.blogspot.vsvydenko.reutersreader.utils.PreferenceHelper;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by vsvydenko on 07.10.14.
 */
public class MainFragment extends Fragment {

    private View returnView;
    private Button btnNews;
    private TextView txtDate;
    private TextView txtFeedTitle;

    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        returnView = inflater.inflate(R.layout.fragment_main, container, false);

        return returnView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeContent();
    }

    @Override
    public void onResume(){
        super.onResume();
        setLastVisitedFeedTitle();
        mHandler.post(dateTimeUpdateTask);
    }

    @Override
    public void onPause(){
        mHandler.removeCallbacks(dateTimeUpdateTask);
        super.onPause();
    }

    private void initializeContent() {

        txtDate = (TextView) returnView.findViewById(R.id.txtDate);
        txtFeedTitle = (TextView) returnView.findViewById(R.id.txtFeedTitle);
        btnNews = (Button) returnView.findViewById(R.id.btnNews);

        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RSSActivity.class));
            }
        });

    }

    private void setLastVisitedFeedTitle(){
        String feedTitle = PreferenceHelper.getLastVisitedFeedTitle();
        if (!TextUtils.isEmpty(feedTitle)){
            txtFeedTitle.setText(feedTitle);
            returnView.findViewById(R.id.lastFeedContainer).setVisibility(View.VISIBLE);
        }
    }

    private Runnable dateTimeUpdateTask = new Runnable() {
        @Override
        public void run() {
            txtDate.setText(DateFormat.getDateTimeInstance().format(new Date()));
            mHandler.postDelayed(dateTimeUpdateTask, 1000);
        }
    };

}
