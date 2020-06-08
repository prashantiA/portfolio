// Copyright 2019 Google LLC
// gle.appengine.api.datastore.FetchOptions
//
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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions; 
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.sps.data.Comment;
import com.google.sps.data.DisplayCommentInfo;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

/** Servlet that returns comments to be displayed */
@WebServlet("/display-comments")
public class DisplayCommentsServlet extends HttpServlet {

  public final String WRITE_TYPE = "applications/json;";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(WRITE_TYPE);
    response.setCharacterEncoding("UTF-8");
    Gson gson = new Gson();

    int numToDisplay;
    try {
      numToDisplay = Integer.parseInt(request.getParameter("num"));
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + request.getParameter("num"));
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    Query query = new Query(Comment.ENTITY_KIND).addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(numToDisplay);
    String encodedCursor = request.getParameter("page");
    if (encodedCursor != null) {
      fetchOptions.startCursor(Cursor.fromWebSafeString(encodedCursor));
    }

    QueryResultList<Entity> results = datastore.prepare(query).asQueryResultList(fetchOptions);


    ArrayList<Comment> commentContent = new ArrayList<Comment>();
    int commentCount = 0;
    for (Entity entity : results) {
      long id = entity.getKey().getId();
      String commentText = (String) entity.getProperty("content");
      long timestamp = (long) entity.getProperty("timestamp");
      Comment comment = new Comment (id, commentText, timestamp);
      commentContent.add(comment);
    }
   
    DisplayCommentInfo info = new DisplayCommentInfo(commentContent, results.getCursor().toWebSafeString());

    String output = gson.toJson(info);
    response.getWriter().println(output);
  }
}
