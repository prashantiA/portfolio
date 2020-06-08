package com.google.sps.data;

/* Holds information about the login and admin status of a user */
public final class UserLoginInfo {
  
  public boolean isAdmin;
  public boolean isLoggedIn;

  public UserLoginInfo(boolean isAdmin, boolean isLoggedIn) {
    this.isAdmin = isAdmin;
    this.isLoggedIn = isLoggedIn;
  }
}
