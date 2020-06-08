package com.google.sps.data;

public final class UserLoginInfo {
  
  public boolean isAdmin;
  public boolean isLoggedIn;

  public UserLoginInfo(boolean isAdmin, boolean isLoggedIn) {
    this.isAdmin = isAdmin;
    this.isLoggedIn = isLoggedIn;
  }
}
