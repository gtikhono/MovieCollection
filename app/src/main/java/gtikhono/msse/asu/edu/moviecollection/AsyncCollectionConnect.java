package gtikhono.msse.asu.edu.moviecollection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/*
* Copyright © 2016 Gabriela Tikhonova
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* Purpose: Using AsyncTask to update UI components and modify the server
*
* Orginal Code made by Copyright ©  2016 Tim Lindquist, and modified by
* Gabriela Tikhonova to use on Assignemnt 5
*
* Ser423 Mobile Applications
* @author   Gabriela Tikhonova  mailto:gtikhono@asu.edu.
* @version March, 4, 2016
*/

public class AsyncCollectionConnect extends AsyncTask<MethodInformation, Integer, MethodInformation> {

    @Override
    protected void onPreExecute(){
        android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
    }


    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest){
        // key is the waypoint category, value is an array of waypoint names in that category
        MovieDescription aStud = new MovieDescription();
        android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        try {
            JSONArray ja = new JSONArray(aRequest[0].params);
            android.util.Log.d(this.getClass().getSimpleName(),"params: "+ja.toString());
            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+aRequest[0].method+"\", \"params\":"+ja.toString()+
                    ",\"id\":3}";
            android.util.Log.d(this.getClass().getSimpleName(),"requestData: "+requestData+" url: "+aRequest[0].urlString);
            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(aRequest[0].urlString)),  aRequest[0].parent);
            String resultStr = conn.call(requestData);
            aRequest[0].resultAsJson = resultStr;
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformation res){
        Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        Log.d(this.getClass().getSimpleName(), " resulting is: " + res.resultAsJson);

        MainActivity main = (MainActivity) res.parent;
        try {
            if (res.method.equals("getTitles")) {
                JSONObject jo = new JSONObject(res.resultAsJson);
                JSONArray ja = jo.getJSONArray("result");
                ArrayList<String> al = new ArrayList<String>();
                for (int i = 0; i < ja.length(); i++) {
                    al.add(ja.getString(i));
                }
                String[] names = al.toArray(new String[0]);
                main.adapter.clear();
                for (int i = 0; i < names.length; i++) {
                    main.adapter.add(names[i]);
                }
                main.adapter.notifyDataSetChanged();

                main.compareandAddtoDB(names);


            }
            if (res.method.equals("remove")) {
                JSONObject jo = new JSONObject(res.resultAsJson);

            }
            if (res.method.equals("add")) {
                JSONObject jo = new JSONObject(res.resultAsJson);

            }
            else if (res.method.equals("get")) {
                JSONObject jo = new JSONObject(res.resultAsJson);

                MovieDescription aMovie = new MovieDescription(jo.getJSONObject("result"));
                System.out.println("Title in ASYNC: " + aMovie.title);

                main.addMovietoDB(aMovie);

            }
        }catch (Exception ex){
            Log.d(this.getClass().getSimpleName(),"Exception: "+ex.getMessage());
        }
    }





}
