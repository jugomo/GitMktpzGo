package com.site11.jugomo.gitmktpzgo.Util;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class RequestHandler extends AsyncTask<String, Integer, String> {

    public enum REQ_TYPE {
        GET,
        POST
    }

    private String url;
    private REQ_TYPE type;
    private OnResultReceived listener;


    public RequestHandler(String url, REQ_TYPE type, OnResultReceived listener) {
        this.url = url;
        this.type = type;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {

        String result = null;

        try {
            HttpClient client = new DefaultHttpClient();

            HttpUriRequest command = null;

            if (type == REQ_TYPE.GET) {
                HttpGet get = new HttpGet();
                get.setURI(new URI(url));
                command = get;
            } else {
                HttpPost post = new HttpPost();
                post.setURI(new URI(url));
                command = post;
            }

            HttpResponse response = client.execute(command);

            BufferedReader buffer = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            result = buffer.readLine();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (listener != null) {
            listener.onResultReceived(s);
        }
    }
}
