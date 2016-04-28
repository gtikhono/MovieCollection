package gtikhono.msse.asu.edu.moviecollection;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
* Purpose: Display a Movie Collection from a jsonRPC server as a list with an adapter
* main Activity of the application
*
* Ser423 Mobile Applications
* @author   Gabriela Tikhonova  mailto:gtikhono@asu.edu.
* @version April 28, 2016
*/


public class MainActivity extends AppCompatActivity {

    public String[] names;
    ArrayAdapter<String> adapter;
    ListView listView ;
    Intent viewMovieDescriptionIntent;
    Intent addAMovieIntent;
    TextView urlET;
    Button addButton;
    Button searchButton;
    String searchName;
    EditText searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity mainActivitythis = this;

        urlET = (TextView) findViewById(R.id.urlET);
        addButton = (Button) findViewById(R.id.addMovieButton);
        listView = (ListView) findViewById(R.id.list);

        addAMovieIntent = new Intent(this.getApplicationContext(), newMovieActivity.class);
        viewMovieDescriptionIntent = new Intent(this.getApplicationContext(), Descriptions.class);

        searchQuery = (EditText) findViewById(R.id.searchTextField);
        searchButton = (Button) findViewById(R.id.searchButton);

        names = new String[]{""};
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<>(Arrays.asList(names)));
        listView.setAdapter(adapter);

        this.populateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);
                viewMovieDescriptionIntent.putExtra("titleToView", item);
                startActivity(viewMovieDescriptionIntent);

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(addAMovieIntent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchName = searchQuery.getText().toString();
                System.out.println("searchName: " + searchName);
                try {
                    MethodInformationAPI mi = new MethodInformationAPI(mainActivitythis, "http://www.omdbapi.com/?t=" + setStringQuery(searchName) + "&y=&plot=short&r=json", "checkMovie",
                            new String[]{});
                    AsyncCollectionConnectAPI ac = (AsyncCollectionConnectAPI) new AsyncCollectionConnectAPI().execute(mi);
                } catch (Exception ex) {
                    android.util.Log.w(this.getClass().getSimpleName(), "Exception searching for title: " +
                            ex.getMessage());
                }
            }
        });

    }

    public String compareandAddtoDB(String[] namesFromServer) {
        String ret = "";
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<String> listOfTitles = new ArrayList<String>();


        try {
            MovieDB db = new MovieDB((Context) this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select Title from movie;", new String[]{});
            while (cur.moveToNext()) {
                try {
                    al.add(cur.getString(0));
                    listOfTitles.add(cur.getString(0));
                } catch (Exception ex) {
                    android.util.Log.w(this.getClass().getSimpleName(), "exception stepping thru cursor" + ex.getMessage());
                }
            }
            String[] namesFromDB = (String[]) al.toArray(new String[al.size()]);

            Arrays.sort(namesFromServer);
            Arrays.sort(namesFromDB);

            System.out.println("serverNames " + Arrays.toString(namesFromServer));
            System.out.println("dbNames " + Arrays.toString(namesFromDB));


            for (String sName : namesFromServer) {
                if (!contains(sName,namesFromDB)) {
                    System.out.println("New Movie to add! "+sName);
                    listOfTitles.add(sName);
                    try {
                        MethodInformation mi = new MethodInformation(this, urlET.getText().toString(), "get",
                                new String[]{sName});
                        AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(), "Exception processing selection: " +
                                ex.getMessage());
                    }
                }
            }

        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "unable to setup movie listview");
        }
        return ret;
    }



    public void startNewMovieActivity(String searchname){
        Intent intent = new Intent(this.getApplicationContext(), newMovieActivity.class);
        intent.putExtra("searchName", searchname);
        startActivity(intent);
    }

    public String setStringQuery(String input){
        input = input.replace(' ', '+');
        return input;
    }

    public void startAlert(){
        Intent intent = new Intent(this.getApplicationContext(), AlertActivity.class);
        startActivity(intent);
    }

    public String populateList() {
        String ret = "";
        try {
            MovieDB db = new MovieDB((Context) this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select Title from movie;", new String[]{});
            ArrayList<String> al = new ArrayList<String>();
            while (cur.moveToNext()) {
                try {
                    al.add(cur.getString(0));
                } catch (Exception ex) {
                    android.util.Log.w(this.getClass().getSimpleName(), "exception stepping thru cursor" + ex.getMessage());
                }
            }

            if (al.isEmpty()){
                try{
                    MethodInformation mi = new MethodInformation(this, urlET.getText().toString(),"getTitles",
                            new String[]{});
                    AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
                } catch (Exception ex) {
                    android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                            ex.getMessage());
                }
            }
            else {
                String[] namesForAdapter = (String[]) al.toArray(new String[al.size()]);
                System.out.println("Adapter Array: " + Arrays.toString(namesForAdapter));
                ret = (namesForAdapter.length > 0 ? namesForAdapter[0] : "");
                adapter.clear();
                adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<>(Arrays.asList(namesForAdapter)));
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "unable to setup movie listview");
        }
        return ret;
    }

    public void addMovietoDB(MovieDescription movietoAdd){
        try{
            MovieDB db = new MovieDB(this);
            SQLiteDatabase crsDB = db.openDB();
            String insert = "insert into movie values('"+movietoAdd.title+"','"+
                    movietoAdd.year+"','"+movietoAdd.rated+"','"+
                    movietoAdd.released+"','"+
                    movietoAdd.runtime+"','"+
                    movietoAdd.genre+"','"+
                    movietoAdd.actors+"','"+
                    movietoAdd.plot+"','"+
                    movietoAdd.filename+"');";

            System.out.println(insert);
            crsDB.execSQL(insert);
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception adding movie information: "+
                    ex.getMessage());
        }
    }

    public boolean contains(String item, String[] array){
        for (String a:array){
            if (item.equals(a)){
                return true;
            }
        }
        return false;
    }



}
