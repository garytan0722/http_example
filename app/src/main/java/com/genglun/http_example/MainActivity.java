package com.genglun.http_example;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("NewTag", "Eeee1");
        new HttpGetRequestAsyncTask().execute();
        Log.d("NewTag","Eeee2");
    }
    private class HttpGetRequestAsyncTask extends AsyncTask<Void ,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection urlConnection=null;
            try{
                URL url=new URL("http://140.124.181.35:3000/events/14.json");
                urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setUseCaches(false);
                //定義表頭
                urlConnection.setRequestProperty("Accept", "application/json");//說明接受json
                urlConnection.setRequestProperty("Content-Type", "application/json");//內容的類型為json
                urlConnection.setRequestProperty("User-Agent", System.getProperty("http.agent", "Android " + Build.VERSION.RELEASE));//說明透過哪個東西連上去（例如瀏覽器）
                //Status code
                int nStatusCode =urlConnection.getResponseCode();
                Log.d("NewTag","status code:"+nStatusCode);

                //header
                Map<String,List<String>> headerFields=urlConnection.getHeaderFields();
                for(String key:headerFields.keySet()){
                    List<String> values=headerFields.get(key);
                    for(String value:values){
                        Log.d("NewTag","Header Key:"+key+"value"+value);
                    }
                }

                //Parse response body
                BufferedReader br=new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
                StringBuilder sb =new StringBuilder();
                String str="";
                while((str=br.readLine())!=null){
                    sb.append(str);
                }
                Log.d("NewTag", "body" + sb.toString());
                EventItem eventItem=parseEventItemResponse(sb.toString());
                urlConnection.disconnect();
            }catch(Exception e){
                Log.d("NewTag","e:"+e);
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public static EventItem parseEventItemResponse(String eventItemResponse){
        EventItem enentItem =new Gson().fromJson(eventItemResponse,EventItem.class);
        return enentItem;
    }
    private void printeventItem(EventItem eventItem){
        Log.d("EventItemTag","id:"+eventItem.getId());
        Log.d("EventItemTag","name"+eventItem.getName());
        Log.d("EventItemTag","description"+eventItem.getDescription());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
