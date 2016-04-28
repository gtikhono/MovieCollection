package gtikhono.msse.asu.edu.moviecollection;

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
* Purpose: Using AsyncTask to update UI components with an API call
*
* Orginal Code made by Copyright ©  2016 Tim Lindquist, and modified by
* Gabriela Tikhonova to use on Assignemnt 7
*
* Ser423 Mobile Applications
* @author   Gabriela Tikhonova  mailto:gtikhono@asu.edu.
* @version March 31, 2016
*/

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.URL;

public class AsyncCollectionConnectAPI extends AsyncTask<MethodInformationAPI, Integer, MethodInformationAPI> {

    @Override
    protected void onPreExecute(){
        Log.d(this.getClass().getSimpleName(), "in onPreExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
    }


    @Override
    protected MethodInformationAPI doInBackground(MethodInformationAPI... aRequest){
        Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        try {
            String requestData = aRequest[0].urlString;
            Log.d(this.getClass().getSimpleName(),"requestData: "+requestData+" url: "+aRequest[0].urlString);
            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(aRequest[0].urlString)),  aRequest[0].parent);
            String resultStr = conn.call(requestData);
            aRequest[0].resultAsJson = resultStr;
        }catch (Exception ex){
            Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformationAPI res){
        Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        Log.d(this.getClass().getSimpleName(), " resulting is: " + res.resultAsJson);



        try {
            if (res.method.equals("getMovie")) {
                JSONObject jo = new JSONObject(res.resultAsJson);
                newMovieActivity addNewActivity = (newMovieActivity) res.parent;
                MovieDescription aMovie = new MovieDescription(jo);
                System.out.println("Title in ASYNC: " + aMovie.title);
                addNewActivity.title.setText(aMovie.title);
                addNewActivity.year.setText(aMovie.year);
                addNewActivity.rated.setText(aMovie.rated);
                addNewActivity.released.setText(aMovie.released);
                addNewActivity.runtime.setText(aMovie.runtime);
                addNewActivity.plot.setText(aMovie.plot);
                addNewActivity.actors.setText(aMovie.actors);
                String[] genres = aMovie.genre.split(",");
                addNewActivity.setSpinner(genres);

            }
            if (res.method.equals("checkMovie")){
                MainActivity mainAct = (MainActivity) res.parent;
                JSONObject jo = new JSONObject(res.resultAsJson);
                MovieDescription aMovie = new MovieDescription(jo);
                if (aMovie.title != null) {
                    String searchName = mainAct.searchQuery.getText().toString();
                    System.out.println("searchName: " + searchName);
                    mainAct.startNewMovieActivity(searchName);
                }
                else{
                    mainAct.startAlert();
                }
            }

        }catch (Exception ex){
            Log.d(this.getClass().getSimpleName(), "Exception: " + ex.getMessage());
        }
    }


}
