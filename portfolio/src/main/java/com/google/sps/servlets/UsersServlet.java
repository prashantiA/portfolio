package com.example.appengine.users;

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
    name = "UserAPI",
    description = "UserAPI: Login / Logout with UserService",
    urlPatterns = "/userapi"
)
public class UsersServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    resp.setContentType("text/html");

    String userUrl = "<a href=\"" + userService.createLoginURL("/index.html") + "\">Log In</a>";
    if (userService.isUserLoggedIn()) {
      userUrl = "<a href=\""+ userService.createLogoutURL("/index.html") + "\">Sign Out</a>";
    }

    resp.getWriter().println(userUrl);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    boolean loggedIn = userService.isUserLoggedIn();
    boolean isAdmin = loggedIn && userService.isUserAdmin();
    resp.setContentType("applications/json");
    UserLoginInfo info = new UserLoginInfo(isAdmin, loggedIn);
    Gson gson = new Gson();
    resp.getWriter().print(gson.toJson(info));
  }
}
