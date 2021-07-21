package com.example.chores;

import android.content.Context;

import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthBaseClient;
import com.example.chores.R;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.api.BaseApi;

// Using Codepath's RestClientTemplate: https://github.com/codepath/android-rest-client-template/
public class GoogleCalendarClient extends OAuthBaseClient {
    public static final BaseApi REST_API_INSTANCE = GoogleApi20.instance();
    public static final String REST_URL = "https://www.googleapis.com/calendar/v3";
    public static final String REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY;
    public static final String REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET;

    // Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
    public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

    // See https://developer.chrome.com/multidevice/android/intents
    public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

    public GoogleCalendarClient(Context context) {
        super(context, REST_API_INSTANCE,
                REST_URL,
                REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET,
                "https://www.googleapis.com/auth/calendar",
                String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
                        context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
    }

    public void getHomeTimeline(JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        client.get(apiUrl, params, handler);
    }
}
