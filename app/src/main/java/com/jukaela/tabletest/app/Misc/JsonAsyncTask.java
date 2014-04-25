package com.jukaela.tabletest.app.Misc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.jukaela.tabletest.app.Activities.MainActivity;
import com.jukaela.tabletest.app.Models.ImageResponseObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonAsyncTask extends AsyncTask<String, String, Void>{

    private ProgressDialog progressDialog;

    InputStream inputStream = null;
    String result = "";
    MainActivity callingActivity;

    public JsonAsyncTask(Activity activity) {
        super();

        progressDialog = new ProgressDialog(activity);

        callingActivity = (MainActivity)activity;
    }

    protected void onPreExecute() {
        progressDialog.setMessage("Downloading your data...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                JsonAsyncTask.this.cancel(true);
            }
        });
    }

    @Override
    protected Void doInBackground(String... params) {
        String url_select = "http://cold-planet-7717.herokuapp.com/microposts/images_from_feed.json";

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url_select);
            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("Accept", "application/json");

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            inputStream = httpEntity.getContent();
        }
        catch (UnsupportedEncodingException e1) {
            Log.e("UnsupportedEncodingException", e1.toString());
            e1.printStackTrace();
        }
        catch (ClientProtocolException e2) {
            Log.e("ClientProtocolException", e2.toString());
            e2.printStackTrace();
        }
        catch (IllegalStateException e3) {
            Log.e("IllegalStateException", e3.toString());
            e3.printStackTrace();
        }
        catch (IOException e4) {
            Log.e("IOException", e4.toString());
            e4.printStackTrace();
        }

        try {
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sBuilder = new StringBuilder();

            String line;

            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line).append("\n");
            }

            inputStream.close();
            result = sBuilder.toString();

        } catch (Exception e) {
            Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
        }

        return null;
    }

    protected void onPostExecute(Void v) {
        try {
            JSONArray jArray = new JSONArray(result);

            ArrayList<ImageResponseObject> tempArray = new ArrayList<ImageResponseObject>();

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);

                ImageResponseObject imageResponseObject = new ImageResponseObject();

                for(Iterator iter = json.keys(); iter.hasNext();) {
                    String key = (String)iter.next();

                    if (key.equals("full_caption")) {
                        try {
                            String value = (String)json.get(key);

                            imageResponseObject.setFullCaption(value);
                        }
                        catch (JSONException e) {
                            Log.d("TableTest", "Unable to read full caption json");
                        }
                    }
                    else if (key.equals("title")) {
                        try {
                            String value = (String)json.get(key);

                            imageResponseObject.setTitle(value);
                        }
                        catch (JSONException e) {
                            Log.d("TableTest", "Unable to read title json");
                        }
                    }
                    else if (key.equals("imageFilename")) {
                        try {
                            String value = (String)json.get(key);

                            imageResponseObject.setImageUrl(value);
                        }
                        catch (JSONException e) {
                            Log.d("TableTest", "Unable to read imageFilename json");
                        }
                        catch (MalformedURLException urlException) {
                            Log.d("TableTest", "The imageFilename URL is malformed.");

                        }
                    }
                }

                tempArray.add(imageResponseObject);
            }

            this.progressDialog.dismiss();

            callingActivity.setupListViewDataSource(tempArray);
        }
        catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }
    }
}
