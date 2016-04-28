package gtikhono.msse.asu.edu.moviecollection;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import gtikhono.msse.asu.edu.moviecollection.MovieDB;

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
* Purpose: Take user input of a Movie Description and saves
* that to the DB
*
*
* Ser423 Mobile Applications
* @author   Gabriela Tikhonova  mailto:gtikhono@asu.edu.
* @version April 28, 2016
*/

public class newMovieActivity extends AppCompatActivity {

    EditText title;
    EditText year;
    EditText rated;
    EditText released;
    EditText runtime;
    Spinner genre;
    EditText actors;
    EditText plot;
    Bundle movieBundle;


    MovieDescription newMovie = new MovieDescription();
    TextView urlET;
    Intent mainActivityIntent;
    Button addMovieButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movie);

        addMovieButton = (Button) findViewById(R.id.saveButton);
        mainActivityIntent = new Intent(this.getApplicationContext(), MainActivity.class);

        movieBundle = getIntent().getExtras();

        title = (EditText) findViewById(R.id.newTitle);
        year = (EditText) findViewById(R.id.newYear);
        rated = (EditText) findViewById(R.id.newRated);
        released = (EditText) findViewById(R.id.newReleased);
        runtime = (EditText) findViewById(R.id.newRuntime);
        plot = (EditText) findViewById(R.id.newPlot);
        genre = (Spinner) findViewById(R.id.spinner);
        actors = (EditText) findViewById(R.id.newActors);


        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableSubmitIfReady();
            }
        });

        try {
            String searchForTitle = movieBundle.getString("searchName");
            System.out.println("searchForTitle: " + searchForTitle);


            if (!searchForTitle.equals("")) {
                try {
                    MethodInformationAPI mi = new MethodInformationAPI(this, "http://www.omdbapi.com/?t=" + setStringQuery(searchForTitle) + "&y=&plot=short&r=json", "getMovie",
                            new String[]{});
                    AsyncCollectionConnectAPI ac = (AsyncCollectionConnectAPI) new AsyncCollectionConnectAPI().execute(mi);
                } catch (Exception ex) {
                    android.util.Log.w(this.getClass().getSimpleName(), "Exception searching for title: " +
                            ex.getMessage());
                }
            }
        }catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "movieBundle.getString(\"searchName\"); is null: " +
                    ex.getMessage());
        }


        addMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovie();
                startActivity(mainActivityIntent);
            }
        });

    }

    public void enableSubmitIfReady() {
        boolean isReady =title.getText().toString().length()>0;
        addMovieButton.setEnabled(isReady);
    }


    public void addMovie(){
        android.util.Log.d(this.getClass().getSimpleName(), "add Clicked. Adding: " + this.title.getText().toString());
        try{
            MovieDB db = new MovieDB(newMovieActivity.this);
            SQLiteDatabase crsDB = db.openDB();
            String insert = "insert into movie values('"+title.getText().toString()+"','"+
                    year.getText().toString()+"','"+rated.getText().toString()+"','"+
                    released.getText().toString()+"','"+
                    runtime.getText().toString()+"','"+
                    genre.getSelectedItem().toString()+"','"+
                    actors.getText().toString()+"','"+
                    plot.getText().toString()+"','"+
                    "noFile"+"');";
            crsDB.execSQL(insert);
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception adding movie information: "+
                    ex.getMessage());
        }
    }
    public String setStringQuery(String input){
        input = input.replace(' ', '+');
        return input;

    }

    public void startAlert(){
        Intent intent = new Intent(this.getApplicationContext(), AlertActivity.class);
        startActivity(intent);
    }

    public void setSpinner(String[] genres){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genres); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genre.setAdapter(spinnerArrayAdapter);
    }


}
