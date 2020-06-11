package com.google.sps.data;

/** An comment on a webpage. */
public final class Comment {

  public static final String ENTITY_KIND = "Comment";

  private final long id;
  private final String content;
  private final String author;
  private final long timestamp;
  private final String image;
	      
  public Comment(long id, String content, String author, long timestamp, String image) {
    this.id = id;
    this.content = content;
    this.timestamp = timestamp;
    this.author = author;
    this.image = image;
  }
}
