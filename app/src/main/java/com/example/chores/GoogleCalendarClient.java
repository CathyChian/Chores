package com.example.chores;

import android.content.Context;

import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.api.BaseApi;
import com.google.api.services.calendar.CalendarScopes;

// Using CodePath's RestClientTemplate: https://github.com/codepath/android-rest-client-template/
    // Uses CodePath's OAuth Handler: https://github.com/codepath/android-oauth-handler
public class GoogleCalendarClient extends OAuthBaseClient {
    public static final BaseApi REST_API_INSTANCE = GoogleApi20.instance();
    public static final String REST_URL = "https://www.googleapis.com/calendar/v3";
    public static final String REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY;
    public static final String REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET;
    public static final String OAUTH2_SCOPE = CalendarScopes.CALENDAR;

    public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

    public static final String REST_CALLBACK_URL_TEMPLATE = "https://fakeredirectlink.com";

    public GoogleCalendarClient(Context context) {
        super(context, REST_API_INSTANCE,
                REST_URL,
                REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET,
                OAUTH2_SCOPE,
                String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
                        context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
    }

    public void getHomeTimeline(JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        client.get(apiUrl, params, handler);
    }
}
