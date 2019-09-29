package com.spotifydb.controllers.models;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public class ViewAlbumArtist {
  private final String name;
  private final String id;

  public ViewAlbumArtist(Map<String, AttributeValue> item) {
    name = item.get("Name").s();
    id = item.get("Id").s();
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }
}
