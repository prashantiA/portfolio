package com.example.appengine.users;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
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
    if (userService.isUserLoggedIn()) {
      resp.getWriter()
          .println(
              "<a href=\""+ userService.createLogoutURL("/index.html") + "\">Sign Out</a>");
    } else {
      resp.getWriter()
          .println(
              "<a href=\"" + userService.createLoginURL("/index.html") + "\">Log In</a>");
    }
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    boolean isAdmin = UserServiceFactory.getUserService().isUserLoggedIn() && UserServiceFactory.getUserService().isUserAdmin();
    resp.setContentType("text/plain");
    resp.getWriter().print(Boolean.toString(isAdmin));
  }
}
