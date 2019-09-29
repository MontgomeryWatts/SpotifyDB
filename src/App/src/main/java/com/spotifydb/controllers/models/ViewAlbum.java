package com.spotifydb.controllers.models;

import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewAlbum {
  private final String id;
  private final String uri;
  private final String title;
  private final List<Image> images;
  private final List<ViewAlbumArtist> artists;
  private final List<ViewAlbumTrack> tracks;

  public ViewAlbum(Map<String, AttributeValue> albumItem) {
    id = albumItem.get("Id").s();
    uri = albumItem.get("Uri").s();
    title = albumItem.get("Title").s();
    artists = extractArtists(albumItem.get("Artists").l());
    tracks  = extractTracks(albumItem.get("Tracks").l());
    images = Utilities.parseImageMapList(albumItem.get("Images"));
  }

  private List<ViewAlbumArtist> extractArtists(List<AttributeValue> artistList) {
    return artistList.stream().map(AttributeValue::m).map(ViewAlbumArtist::new).collect(Collectors.toList());
  }

  private List<ViewAlbumTrack> extractTracks(List<AttributeValue> trackList) {
    return trackList.stream().map(AttributeValue::m).map(ViewAlbumTrack::new).collect(Collectors.toList());
  }

  public String getId() {
    return id;
  }

  public String getUri() {
    return uri;
  }

  public String getTitle() {
    return title;
  }

  public List<Image> getImages() {
    return images;
  }

  public List<ViewAlbumArtist> getArtists() {
    return artists;
  }

  public List<ViewAlbumTrack> getTracks() {
    return tracks;
  }
}
