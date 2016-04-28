package gtikhono.msse.asu.edu.moviecollection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Vector;

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
* Purpose: Movie Description class is to create a
* Movie Description with a JSON String
*
* Ser423 Mobile Applications
* @author   Gabriela Tikhonova  mailto:gtikhono@asu.edu.
* @version April 28, 2016
*/

public class MovieDescription {

    public String title;
    public String year;
    public String rated;
    public String released;
    public String runtime;
    public String genre;
    public String actors;
    public String plot;

    public String filename="noFile";

    //Movie Description Class takes in a JSON String as the constructor
    public MovieDescription(String jsonString){
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            title = jsonObject.getString("Title");
            year = jsonObject.getString("Year");
            rated = jsonObject.getString("Rated");
            released = jsonObject.getString("Released");
            runtime = jsonObject.getString("Runtime");
            genre = jsonObject.getString("Genre");
            actors = jsonObject.getString("Actors");
            plot = jsonObject.getString("Plot");

            try{
                filename = jsonObject.getString("Filename");
            }catch(Exception ex){
            }




        }catch (Exception ex){
            //android.util.Log.w(this.getClass().getSimpleName(),"Problem with converting from JSON string: " + jsonString);
        }
    }

    public MovieDescription(){

    }

    public MovieDescription(String title,String year,String rated,String released,String runtime,String genre,String actors,String plot){
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.actors = actors;
        this.plot = plot;
    }

    public MovieDescription(JSONObject jsonObj){
        try{
            System.out.println("constructor from json received: " +
                    jsonObj.toString());

            title = jsonObj.getString("Title");
            year = jsonObj.getString("Year");
            rated = jsonObj.getString("Rated");
            released = jsonObj.getString("Released");
            runtime = jsonObj.getString("Runtime");
            genre = jsonObj.getString("Genre");
            actors = jsonObj.getString("Actors");
            plot = jsonObj.getString("Plot");

            try{
                filename = jsonObj.getString("Filename");
            }catch(Exception ex){
            }
        }catch(Exception ex){
            System.out.println(this.getClass().getSimpleName()+
                    ": error converting from json string");
        }
    }

    /**
     @returns a JSON String with the class member variables intitiilized with the Movie Description class constructor
     */
    public String toJsonString(){
        String jsonString = "";
        try{
            jsonString = this.toJson().toString();
        }catch (Exception ex){
            System.out.println(this.getClass().getSimpleName()+
                    ": error converting to json string");
        }
        return jsonString;
    }



    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("Title",title);
            jsonObject.put("Year", year);
            jsonObject.put("Rated", rated);
            jsonObject.put("Released", released);
            jsonObject.put("Runtime", runtime);
            jsonObject.put("Plot", plot);
            jsonObject.put("Genre",genre);
            jsonObject.put("Actors",actors);
            try{
                jsonObject.put("Filename",filename);
            }catch(Exception ex){
            }
        }catch (Exception ex){
            System.out.println(this.getClass().getSimpleName()+
                    ": error converting to json");
        }
        return jsonObject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }



}