package com.google.sps.data;

import com.google.sps.data.Comment;
import java.util.ArrayList;

/** A comment display info package on a webpage. */
public final class DisplayCommentInfo {

  public ArrayList<Comment> comments;
  public String cursor;
	      
  public DisplayCommentInfo(ArrayList<Comment> comments, String cursor) {
    this.comments = comments;
    this.cursor = cursor;
  }
}
