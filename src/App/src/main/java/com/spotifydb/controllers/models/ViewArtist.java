package com.spotifydb.controllers.models;

import com.wrapper.spotify.model_objects.specification.Image;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewArtist {
  private final String id;
  private final String uri;
  private final String name;
  private final List<String> genres;
  private final List<Image> images;

  public ViewArtist(Map<String, AttributeValue> artistItem) {
    id = artistItem.get("Id").s();
    uri = artistItem.get("Uri").s();
    name = artistItem.get("Name").s();
    genres = artistItem.get("Genres") != null ? artistItem.get("Genres").ss() : new ArrayList<>();
    images = Utilities.parseImageMapList(artistItem.get("Images"));
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

  public List<Image> getImages() {
    return images;
  }
}
