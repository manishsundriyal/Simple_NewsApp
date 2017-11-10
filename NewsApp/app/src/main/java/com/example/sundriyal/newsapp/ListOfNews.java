package com.example.sundriyal.newsapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sundriyal on 11/9/2017.
 */

public class ListOfNews extends AppCompatActivity {





    private String MY_API_KEY = "b94a2b1c-524c-48ff-8be0-0061a02852eb";
    private String QUERY = "bitcoin";
    public static String NUMBER_OF_PAGES = "30";
    private String URL = "https://content.guardianapis.com/search?q=" + QUERY + "&page-size=" + NUMBER_OF_PAGES + "&api-key=" + MY_API_KEY;


    ArrayList<String> headingsOfNews;
    ArrayList<String> webUrls;
    Context context;
    private static final String TAG="Myactivity";
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Hello234");
        setContentView(R.layout.list_of_news);
        Log.d(TAG,"Hello");
        context=this;
        headingsOfNews = new ArrayList<String>();
        webUrls = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.listView);
        GetData getData = new GetData();
        getData.execute(URL);
    }


    public class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                String result = "";
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data;
                data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {

                JSONObject MainObject = new JSONObject(result);

                String response = MainObject.getString("response");
                JSONObject responseJSON = new JSONObject(response);

                Log.d("XML",responseJSON.toString());


                String results = responseJSON.getString("results");
                JSONArray jsonArray = new JSONArray(results);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newsObject = jsonArray.getJSONObject(i);
                    String heading = newsObject.getString("webTitle");
                    String webUrl = newsObject.getString("webUrl");
                    headingsOfNews.add(heading);
                    webUrls.add(webUrl);
                }

                updateListView();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void updateListView() {

        CustomAdapter customAdapter=new CustomAdapter();
        listView.setAdapter(customAdapter);

//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,headingsOfNews);
//        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,DisplayNewsActivity.class);
                intent.putExtra("URL",webUrls.get(position));
                startActivity(intent);
            }
        });

    }

    public class CustomAdapter extends BaseAdapter {
        TextView heading;
        @Override
        public int getCount() {
            return Integer.parseInt(NUMBER_OF_PAGES);
        }

        @Override
        public Object getItem(int position) {
            return headingsOfNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.custom_list_row, parent, false);
            }

            heading = (TextView) row.findViewById(R.id.textViewForNews);
            heading.setText(headingsOfNews.get(position));
            return row;
        }
    }
}
