package com.spotifydb.controllers.models;

import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewAlbum {
  private final String id;
  private final String uri;
  private final String title;
  private final List<Image> images;
  private final List<ArtistSimplified> artists;
  private final List<TrackSimplified> tracks;

  public ViewAlbum(Map<String, AttributeValue> albumItem) {
    id = albumItem.get("Id").s();
    uri = albumItem.get("Uri").s();
    title = albumItem.get("Title").s();
    artists = extractArtists(albumItem.get("Artists").l());
    tracks  = extractTracks(albumItem.get("Tracks").l());
    images = Utilities.parseImageMapList(albumItem.get("Images"));
  }

  private List<ArtistSimplified> extractArtists(List<AttributeValue> artistList) {
    List<ArtistSimplified> extractedArtists = new ArrayList<>();
    for (AttributeValue artist : artistList) {
      Map<String, AttributeValue> item = artist.m();
      extractedArtists.add(new ArtistSimplified.Builder()
      .setName(item.get("Name").s())
      .setId(item.get("Id").s())
      .build());
    }
    return extractedArtists;
  }

  private List<TrackSimplified> extractTracks(List<AttributeValue> trackList) {
    List<TrackSimplified> extractedTracks = new ArrayList<>();
    for (AttributeValue track : trackList) {
      Map<String, AttributeValue> item = track.m();
      extractedTracks.add(new TrackSimplified.Builder()
        .setName(item.get("Title").s())
        .setUri(item.get("Uri").s())
        .setExplicit(item.get("IsExplicit").bool())
        .setDurationMs(Integer.parseInt(item.get("Duration").n()))
        .setArtists(extractArtists(item.get("Artists").l()).toArray(new ArtistSimplified[0]))
        .build());
    }
    return extractedTracks;
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

  public List<ArtistSimplified> getArtists() {
    return artists;
  }

  public List<TrackSimplified> getTracks() {
    return tracks;
  }
}
