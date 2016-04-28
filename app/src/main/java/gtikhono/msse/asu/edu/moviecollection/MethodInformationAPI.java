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
* Purpose: Using AsyncTask to update UI components and modify the server
*
* Orginal Code made by Copyright ©  2016 Tim Lindquist, and modified by
* Gabriela Tikhonova to use on Assignemnt 9
*
* Ser423 Mobile Applications
* @author   Gabriela Tikhonova  mailto:gtikhono@asu.edu.
* @version March, 4, 2016
*/

import android.support.v7.app.AppCompatActivity;

public class MethodInformationAPI {
    public String method;
    public AppCompatActivity parent;
    public String urlString;
    public String resultAsJson;

    MethodInformationAPI(AppCompatActivity parent, String urlString, String method, String[] params){
        this.method = method;
        this.parent = parent;
        this.urlString = urlString;
        this.resultAsJson = "{}";
    }


}
