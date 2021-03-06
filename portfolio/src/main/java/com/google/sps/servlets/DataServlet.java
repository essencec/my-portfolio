// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {   
        String numOfCommentsString = request.getParameter("numOfComments");
        int numOfComments;
        try {
            numOfComments  = Integer.parseInt(numOfCommentsString);
        } catch(NumberFormatException e){  
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/html;");
        response.getWriter().println("Error: `numOfComments` is not a string");
        return;
        }
     
        Query query = new Query("Comment");
        PreparedQuery results = datastore.prepare(query);
        List<Entity> entity = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(numOfComments));
        ArrayList<String> jsonData = new ArrayList<String>();
        for(int i = 0; i < entity.size(); i++) {
            String temp = (String) entity.get(i).getProperty("text");
            jsonData.add(temp);
        }
        
        response.setContentType("application/json;");
        String json = convertToJson(jsonData);
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String text = getParameter(request, "text-input", "");

        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("text", text);

        datastore.put(commentEntity);

        response.setContentType("text/html;");
        response.getWriter().println(text);

        response.sendRedirect("/index.html");
    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    private String convertToJson(ArrayList<String> jsonData) {
      Gson gson = new Gson();
      String json = gson.toJson(jsonData);
      return json;
    } 
}
