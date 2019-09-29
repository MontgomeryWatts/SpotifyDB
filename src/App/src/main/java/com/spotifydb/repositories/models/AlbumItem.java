package com.spotifydb.repositories.models;

import com.wrapper.spotify.model_objects.specification.Album;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumItem extends DynamoItem {
  private static Logger logger = LoggerFactory.getLogger(AlbumItem.class);

  public AlbumItem(Album album) {
    if (album == null) {
      logger.error("AlbumItem constructor was passed a null Album");
      throw new IllegalArgumentException();
    }

    logger.info("Creating an AlbumItem for Album with ID: {}", album.getId());

    item.put("PK", AttributeValue.builder()
      .s("/albums/"+album.getId())
      .build());
    item.put("SK", AttributeValue.builder()
      .s(album.getId())
      .build());
    item.put("Id", AttributeValue.builder()
      .s(album.getId())
      .build());
    item.put("Uri", AttributeValue.builder()
      .s(album.getUri())
      .build());
    item.put("Title", AttributeValue.builder()
      .s(album.getName())
      .build());

    List<AttributeValue> tracks = new ArrayList<>();
    for (TrackSimplified track: album.getTracks().getItems()) {
      Map<String, AttributeValue> trackItem = new HashMap<>();
      trackItem.put("Title", AttributeValue.builder().s(track.getName()).build());
      trackItem.put("IsExplicit", AttributeValue.builder().bool(track.getIsExplicit()).build());
      trackItem.put("Duration", AttributeValue.builder().n(Integer.toString(track.getDurationMs() / 1000)).build());
      trackItem.put("Uri", AttributeValue.builder().s(track.getUri()).build());

      List<AttributeValue> artists = new ArrayList<>();
      for (ArtistSimplified artist: track.getArtists()) {
        Map<String, AttributeValue> artistItem = new HashMap<>();
        artistItem.put("Name", AttributeValue.builder()
          .s(artist.getName()).build());
        artistItem.put("Id", AttributeValue.builder()
          .s(artist.getId()).build());
        artists.add(AttributeValue.builder().m(artistItem).build());
      }
      trackItem.put("Artists", AttributeValue.builder().l(artists).build());

      tracks.add(AttributeValue.builder().m(trackItem).build());
    }

    item.put("Tracks", AttributeValue.builder().l(tracks).build());

    List<AttributeValue> artists = new ArrayList<>();
    for (ArtistSimplified artist: album.getArtists()) {
      Map<String, AttributeValue> artistItem = new HashMap<>();
      artistItem.put("Name", AttributeValue.builder()
        .s(artist.getName()).build());
      artistItem.put("Id", AttributeValue.builder()
        .s(artist.getId()).build());
      artists.add(AttributeValue.builder().m(artistItem).build());
    }
    if (artists.size() > 0) {
      item.put("Artists", AttributeValue.builder()
        .l(artists)
        .build());
    }

    List<AttributeValue> images = Utilities.createImageMapList(album.getImages());
    if (images.size() > 0) {
      item.put("Images", AttributeValue.builder()
        .l(images)
        .build());
    }
  }
}
