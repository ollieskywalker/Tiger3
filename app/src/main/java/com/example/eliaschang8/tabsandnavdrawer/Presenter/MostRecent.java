package com.example.eliaschang8.tabsandnavdrawer.Presenter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eliaschang8.tabsandnavdrawer.Modler.PostItem;
import com.example.eliaschang8.tabsandnavdrawer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by eliaschang8 on 4/26/17.
 */

public class MostRecent extends Fragment {

    private static final String URL = "http://tigernewspaper.com/wordpress/wp-json/wp/v2/posts";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mostrecent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        JSONParser parser = new JSONParser();

        parser.execute(URL);

    }

    private class JSONParser extends AsyncTask<String, Void, String>{
        private static final String TAG = "TAG";
        String postJSON = "";

        @Override
        protected String doInBackground(String... urls) {
            try {
                java.net.URL url = new java.net.URL(urls[0]);
                URLConnection connection = url.openConnection();

                InputStream inputStream = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while((line = reader.readLine()) != null){
                    postJSON += line;
                    Log.d("onAsyncClass" , "Looping");
                }
                return postJSON;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("onAsyncClass" , "NOT WORKING");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ArrayList<PostItem>posts = new ArrayList<PostItem>();

            if(s != null){
                try {
                    JSONArray postsJSON = new JSONArray(postJSON);

                    for(int i = 0; i < postsJSON.length(); i++){

                        JSONObject objectPost =  postsJSON.optJSONObject(i);

                        JSONObject renderedTitle = objectPost.optJSONObject("title");

                        String title = renderedTitle.optString("rendered");
                        title = android.text.Html.fromHtml(title).toString();

                        Log.d(TAG, "onPostExecute: " + title);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
