package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.sps.data.Comment;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.lang.Thread;
import java.lang.InterruptedException;

/** WebServlet that handles requests to add comments to the page */
@WebServlet("/comment-info")
public class GetCommentInfoServlet extends HttpServlet {

  public final String WRITE_TYPE = "applications/json;";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query(Comment.ENTITY_KIND);
    PreparedQuery results = datastore.prepare(query);
    int size = results.asList(FetchOptions.Builder.withDefaults()).size();

    response.setContentType(WRITE_TYPE);
    response.getWriter().println("[" + Integer.toString(size) + "]");
  }
}
