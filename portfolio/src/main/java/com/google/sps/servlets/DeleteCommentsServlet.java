package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.Comment;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/** Servlet that handles requests to delete comments */
@WebServlet("/delete-comments")
public class DeleteCommentsServlet extends HttpServlet {

  private static final String DELETE_ALL = "all";
  private static final String BY_ID = "byId";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String type = request.getParameter("type");
    if (type == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      System.err.println("No parameter for type");
    }
    else if (type.equals(DELETE_ALL)) deleteAll();
    else if (type.equals(BY_ID)) {
      try {
        long id = Long.parseLong(request.getParameter("id"));
	deleteById(id);
      } catch (NumberFormatException e) {
        System.err.println("Failed to parse to long: " + request.getParameter("id"));
	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      }
    }
    else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      System.err.println("Value " + type + " for parameter type is invalid");
    }
  }

   /** Deleted all comments from datastore */
  private void deleteAll() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query(Comment.ENTITY_KIND);
    PreparedQuery results = datastore.prepare(query);
    
    ArrayList<Key> toDelete = new ArrayList<Key>();
    for (Entity entity : results.asIterable()) {
      toDelete.add(entity.getKey());
    }

    datastore.delete(toDelete);
  }

  /** Delete by ID
   * @param id the id of the comment to be deleted
   */
  private void deleteById(long id) {
    Key key = KeyFactory.createKey("Comment", id);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.delete(key);
  }
}
