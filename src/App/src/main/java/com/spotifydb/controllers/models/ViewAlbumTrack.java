package com.spotifydb.controllers.models;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewAlbumTrack {
  private final String title;
  private final String uri;
  private final boolean isExplicit;
  private final int duration;
  private final List<ViewAlbumArtist> artists;

  public ViewAlbumTrack(Map<String, AttributeValue> item) {
    title = item.get("Title").s();
    uri = item.get("Uri").s();
    isExplicit = item.get("IsExplicit").bool();
    duration = Integer.parseInt(item.get("Duration").n());
    artists = item.get("Artists").l().stream().map(AttributeValue::m).map(ViewAlbumArtist::new).collect(Collectors.toList());
  }

  public String getTitle() {
    return title;
  }

  public String getUri() {
    return uri;
  }

  public boolean isExplicit() {
    return isExplicit;
  }

  public int getDuration() {
    return duration;
  }

  public List<ViewAlbumArtist> getArtists() {
    return artists;
  }
}
