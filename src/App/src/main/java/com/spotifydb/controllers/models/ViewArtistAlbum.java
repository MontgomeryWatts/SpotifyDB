package com.spotifydb.controllers.models;

import com.wrapper.spotify.model_objects.specification.Image;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

public class ViewArtistAlbum {
  private final String id;
  private final String uri;
  private final String title;
  private final String type;
  private final List<Image> images;

  public ViewArtistAlbum(Map<String, AttributeValue> artistAlbumItem) {
    id = artistAlbumItem.get("Id").s();
    uri = artistAlbumItem.get("Uri").s();
    title = artistAlbumItem.get("Title").s();
    type = artistAlbumItem.get("AlbumType").s();
    images = Utilities.parseImageMapList(artistAlbumItem.get("Images"));
  }
}
