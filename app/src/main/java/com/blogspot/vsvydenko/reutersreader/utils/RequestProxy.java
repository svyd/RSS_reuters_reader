package com.blogspot.vsvydenko.reutersreader.utils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.Context;

/**
 * Created by vsvydenko on 29.06.14.
 */
public class RequestProxy {

    private RequestQueue mRequestQueue;

    // package access constructor
    RequestProxy(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public void feed(String url,
            Response.Listener listener, Response.ErrorListener errorListener) {
        // feed request
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                listener,
                errorListener
        );
        stringRequest.setRetryPolicy(setTimeoutPolicy(15000));
        mRequestQueue.add(stringRequest);
    }

    private RetryPolicy setTimeoutPolicy(int timeInMilisec) {

        return new DefaultRetryPolicy(timeInMilisec,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

}
