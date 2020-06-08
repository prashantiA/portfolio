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
    name = "UserInfo",
    urlPatterns = "/user-info"
)
public class UserInfoServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    boolean loggedIn = userService.isUserLoggedIn();
    boolean isAdmin = loggedIn && userService.isUserAdmin();
    resp.setContentType("applications/json");
    UserLoginInfo info = new UserLoginInfo(isAdmin, loggedIn);
    Gson gson = new Gson();
    resp.getWriter().print(gson.toJson(info));
  }
}
