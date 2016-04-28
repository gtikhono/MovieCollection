package gtikhono.msse.asu.edu.moviecollection;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
* Purpose: Display a MovieDescription of the movie chosen in MainActivity
* from a jsonRPC server, and Remove the movie chosen from the server
*
* JSON string source: http://www.omdbapi.com/
*
* Ser423 Mobile Applications
* @author   Gabriela Tikhonova  mailto:gtikhono@asu.edu.
* @version version April 28, 2016
*/

public class Descriptions extends AppCompatActivity {

    public TextView title;
    TextView year;
    TextView rated;
    TextView released;
    TextView runtime;
    TextView genre;
    TextView actors;
    TextView plot;
    Bundle movieBundle;
    String newTitle = "";
    TextView urlET;
    Button removeMovieButton;
    Button viewMovieButton;
    Intent intentRemove;
    Intent intentView;
    String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descriptions);

        movieBundle = getIntent().getExtras();

        try {
            newTitle = movieBundle.getString("titleToView");
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception processing newTitle: " +
                    ex.getMessage());
        }

        removeMovieButton = (Button) findViewById(R.id.removeMovie);
        viewMovieButton = (Button) findViewById(R.id.viewVideo);
        title = (TextView) findViewById(R.id.title);
        year = (TextView) findViewById(R.id.year);
        rated = (TextView) findViewById(R.id.rated);
        released = (TextView) findViewById(R.id.released);
        runtime = (TextView) findViewById(R.id.runtime);
        plot = (TextView) findViewById(R.id.plot);
        genre = (TextView) findViewById(R.id.genre);
        actors = (TextView) findViewById(R.id.actors);
        loadFields(newTitle);

        intentRemove = new Intent(this.getApplicationContext(), MainActivity.class);
        intentView = new Intent(this.getApplicationContext(), VideoActivity.class);
        removeMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMovie(newTitle);
                startActivity(intentRemove);
            }
        });

        viewMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentView.putExtra("file", fileName);
                startActivity(intentView);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Implement onOptionsItemSelected(MenuItem item){} to handle clicks of buttons that are
     * in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        Intent i = new Intent(this, MainActivity.class);
        switch (item.getItemId()) {
            case R.id.backarrow:
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadFields(String titlePassedIn){
        try{
            MovieDB db = new MovieDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select Year,Rated,Released,Runtime,Genre,Actors,Plot,Filename from movie where Title=? ;",
                    new String[]{titlePassedIn});
            String yearS = "";
            String ratedS = "";
            String releasedS = "";
            String runtimeS = "";
            String genreS = "";
            String actorsS = "";
            String plotS = "";
            while (cur.moveToNext()){
                try {
                    yearS = cur.getString(0);
                    ratedS = cur.getString(1);
                    releasedS = cur.getString(2);
                    runtimeS = cur.getString(3);
                    genreS = cur.getString(4);
                    actorsS = cur.getString(5);
                    plotS = cur.getString(6);
                    fileName = cur.getString(7);
                    System.out.println("fileName from DB"+ fileName);
                    if (fileName.equals("noFile")){
                        viewMovieButton.setEnabled(false);
                    }
                }catch(Exception ex){

                }
            }

            title.setText(titlePassedIn);
            year.setText(yearS);
            rated.setText(ratedS);
            released.setText(releasedS);
            runtime.setText(runtimeS);
            genre.setText(genreS);
            actors.setText(actorsS);
            plot.setText(plotS);
            cur.close();
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception getting movie info: "+
                    ex.getMessage());
        }
    }

    private void removeMovie(String titlePassedIn){
        android.util.Log.d(this.getClass().getSimpleName(), "remove Clicked");
        System.out.println("TitlePassedIn "+titlePassedIn);
        try{
            MovieDB db = new MovieDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            crsDB.delete("movie","Title="+"'"+titlePassedIn+"'",null);
            crsDB.close();
            db.close();
        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(), "Exception deleting movie: " +
                    ex.getMessage());
        }

    }
}
