package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.lang.Thread;
import java.lang.InterruptedException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** WebServlet that handles requests to add comments to the page */
@WebServlet("/add-comment")
public class AddCommentServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity commentEntity = new Entity(Comment.ENTITY_KIND);
    commentEntity.setProperty("content", request.getParameter("comment-text"));
    commentEntity.setProperty("author", request.getParameter("author"));
    commentEntity.setProperty("timestamp", System.currentTimeMillis());
				        
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
  }
}
