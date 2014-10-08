package com.blogspot.vsvydenko.reutersreader.ui;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blogspot.vsvydenko.reutersreader.R;
import com.blogspot.vsvydenko.reutersreader.entity.FeedItem;
import com.blogspot.vsvydenko.reutersreader.utils.PreferenceHelper;
import com.blogspot.vsvydenko.reutersreader.utils.RequestManager;
import com.blogspot.vsvydenko.reutersreader.utils.XMLParser;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vsvydenko on 07.10.14.
 */
public class FeedsListFragment extends Fragment implements ListView.OnItemClickListener {

    public static final String FRAGMENT_ID = "FRAGMENT_ID";

    public static final int BUSINESS_NEWS_FRAGMENT = 1;
    public static final int ENTERTAITMENT_NEWS_FRAGMENT = 2;

    public static final int UPDATE_INTERVAL_MS = 5000; // 5 sec

    public static final String BUSINESS_NEWS_URL = "http://feeds.reuters.com/reuters/businessNews";
    public static final String ENTERTAITMENT_NEWS_URL = "http://feeds.reuters.com/reuters/entertainment";
    public static final String ENVIRONMENT_NEWS_URL = "http://feeds.reuters.com/reuters/environment";

    private int fragmentId;
    private View returnView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

    private XMLParser mXMLParser = new XMLParser();
    private List<FeedItem> mFeedItemList = new ArrayList<FeedItem>();
    private FeedsAdapter mFeedsAdapter;

    private Handler mHandler = new Handler();

    private List<FeedItem> tempNewsList = new LinkedList<FeedItem>();
    private List<FeedItem> entertaitmentNewsList = new LinkedList<FeedItem>();
    private List<FeedItem> environmentNewsList = new LinkedList<FeedItem>();
    private boolean entertaitmentNewsDone, environmentNewsDone;
    private boolean restore;

    public static FeedsListFragment newInstance(int id){
        FeedsListFragment feedsListFragment = new FeedsListFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_ID, id);
        feedsListFragment.setArguments(args);
        return feedsListFragment;
    }

    public FeedsListFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        fragmentId = getArguments().getInt(FRAGMENT_ID, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        returnView = inflater.inflate(R.layout.fragment_feeds_list, container, false);

        return returnView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeContent();
    }

    @Override
    public void onStart(){
        if (restore){
            scheduleUpdate();
        }
        super.onStart();
    }

    @Override
    public void onPause() {
        restore = true;
        super.onPause();
    }

    @Override
    public void onStop() {
        mHandler.removeCallbacks(scheduleTask);
        super.onStop();
    }

    private void initializeContent() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) returnView.findViewById(R.id.swipe_container);
        mListView = (ListView) returnView.findViewById(R.id.list);

        mSwipeRefreshLayout.setEnabled(false);
        //mSwipeRefreshLayout.setOnRefreshListener(swipeListener);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        if (restore && mFeedItemList.size() > 0) {
            updateUI();
            restore = false;
        } else {
            checkFeeds();
        }

    }

    private void checkFeeds() {
        mSwipeRefreshLayout.setRefreshing(true);
        switch (fragmentId){
            case BUSINESS_NEWS_FRAGMENT:
                checkBusinessNews();
                break;
            case ENTERTAITMENT_NEWS_FRAGMENT:
                checkEntertaitmentNews();
                break;
        }

    }

    private void checkBusinessNews(){
        RequestManager.getInstance().doRequest().feed(
                BUSINESS_NEWS_URL, listener, errorListener);
    }

    private void checkEntertaitmentNews(){
        RequestManager.getInstance().doRequest().feed(
                ENTERTAITMENT_NEWS_URL, listener, errorListener);
        RequestManager.getInstance().doRequest().feed(
                ENVIRONMENT_NEWS_URL, listener, errorListener);
    }

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(final String response) {
            mSwipeRefreshLayout.setRefreshing(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    switch (fragmentId){
                        case BUSINESS_NEWS_FRAGMENT:
                            tempNewsList.clear();
                            try {
                                tempNewsList.addAll(mXMLParser.parse(response));
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (!mFeedItemList.equals(tempNewsList)) {
                                updateUI();
                            }
                            scheduleUpdate();
                            break;
                        case ENTERTAITMENT_NEWS_FRAGMENT:
                            if (isEntertaitmentSource(response)){
                                entertaitmentNewsList.clear();
                                try {
                                    entertaitmentNewsList.addAll(mXMLParser.parse(response));
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                entertaitmentNewsDone = true;
                            } else {
                                environmentNewsList.clear();
                                try {
                                    environmentNewsList.addAll(mXMLParser.parse(response));
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                environmentNewsDone = true;
                            }
                            if (entertaitmentNewsDone && environmentNewsDone){
                                tempNewsList.clear();
                                tempNewsList.addAll(entertaitmentNewsList);
                                tempNewsList.addAll(environmentNewsList);
                                if (!mFeedItemList.equals(tempNewsList)) {
                                    updateUI();
                                }
                                entertaitmentNewsDone = false;
                                environmentNewsDone = false;
                                scheduleUpdate();
                            }

                            break;
                    }

                }
            }).start();
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.an_error_occured), Toast.LENGTH_SHORT).show();
            scheduleUpdate();
        }
    };

    private void updateUI(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFeedItemList.clear();
                mFeedItemList.addAll(tempNewsList);
                if (mFeedsAdapter == null) {
                    mFeedsAdapter = new FeedsAdapter(getActivity(), mFeedItemList);
                }
                if (mListView.getAdapter() == null) {
                    mListView.setAdapter(mFeedsAdapter);
                } else {
                    mFeedsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void scheduleUpdate(){
        mHandler.postDelayed(scheduleTask, UPDATE_INTERVAL_MS);
    }

    Runnable scheduleTask = new Runnable() {
        @Override
        public void run() {
            checkFeeds();
        }
    };

    private boolean isEntertaitmentSource(String news){
        String entertaitmentNewsFlag = "<title>Reuters: Entertainment News</title>";
        return news.contains(entertaitmentNewsFlag);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FeedItem feedItem = mFeedItemList.get(position);
        Uri uri = Uri.parse(feedItem.getLink());
        PreferenceHelper.setLastVisitedFeed(feedItem.getTitle());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
