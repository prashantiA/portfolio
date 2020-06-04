package com.google.sps.data;

/** An comment on a webpage. */
public final class Comment {

  public static final String ENTITY_KIND = "Comment";

  private final long id;
  private final String content;
  private final long timestamp;
	      
  public Comment(long id, String content, long timestamp) {
    this.id = id;
    this.content = content;
    this.timestamp = timestamp;
  }
}
