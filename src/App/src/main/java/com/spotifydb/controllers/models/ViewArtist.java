package com.spotifydb.controllers.models;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

public class ViewArtist {
  private final String id;
  private final String uri;
  private final String name;
  private final List<String> genres;
  private final List<String> imageUrls;

  public ViewArtist(Map<String, AttributeValue> artistItem) {
    id = artistItem.get("Id").s();
    uri = artistItem.get("Uri").s();
    name = artistItem.get("Name").s();
    genres = artistItem.get("Genres").ss();
    imageUrls = artistItem.get("Images").ss();
  }

  public String getId() {
    return id;
  }

  public String getUri() {
    return uri;
  }

  public String getName() {
    return name;
  }

  public List<String> getGenres() {
    return genres;
  }

  public List<String> getImageUrls() {
    return imageUrls;
  }
}
