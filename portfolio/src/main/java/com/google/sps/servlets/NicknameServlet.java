package com.google.sps.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.UserLoginInfo;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(
    name = "NicknameServlet",
    urlPatterns = "/nicknames"
)
public class NicknameServlet extends HttpServlet {

  public static final String DEFAULT_NICKNAME = "Anonymous Goose";

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html");
   
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      resp.getWriter().print(DEFAULT_NICKNAME);
      return;
    }

    resp.getWriter().println(getUserNickname(userService.getCurrentUser().getUserId()));
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String nickname = req.getParameter("nickname");
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      System.err.println("No user currently logged in, cannot set nickname");
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    setUserNickname(userService.getCurrentUser().getUserId(), nickname);
  }

  /**
   * Gets the current user info entity under the id
   * If no such entity exists returns null
   */ 
  private Entity getUserNicknameEntity(DatastoreService datastore, String id) {
    Query query = new Query("UserInfo").setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    return results.asSingleEntity();
  }

  /**
   * Returns the nickname of the user with id, or empty String if the user has not set a nickname.
   */
  private String getUserNickname(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity entity = getUserNicknameEntity(datastore, id);
    if (entity == null) {
      return DEFAULT_NICKNAME;
    }
    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }

  /**
   * Sets the user nickname and clears the old nickname if there is one
   */
  private void setUserNickname(String id, String nickname) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity oldRes = getUserNicknameEntity(datastore, id);
    if (oldRes != null) {
      datastore.delete(oldRes.getKey());
    }
    Entity entity = new Entity("UserInfo", id);
    entity.setProperty("id", id);
    entity.setProperty("nickname", nickname);
    datastore.put(entity);
  }
}
