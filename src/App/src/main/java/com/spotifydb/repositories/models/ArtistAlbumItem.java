package com.spotifydb.repositories.models;

import com.wrapper.spotify.model_objects.specification.Album;
import com.wrapper.spotify.model_objects.specification.Artist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;

public class ArtistAlbumItem extends DynamoItem {
  private static Logger logger = LoggerFactory.getLogger(ArtistAlbumItem.class);

  public ArtistAlbumItem(Artist artist, Album album) {
    if (artist == null) {
      logger.error("ArtistAlbumItem constructor was passed a null Artist");
      throw new IllegalArgumentException();
    }

    if (album == null) {
      logger.error("ArtistAlbumItem constructor was passed a null Album");
      throw new IllegalArgumentException();
    }

    logger.info("Creating an ArtistAlbumItem for Album with ID: {}, and Artist with ID: {}", album.getId(), artist.getId());

    item.put("PK", AttributeValue.builder()
      .s("/artists/"+artist.getId()+"/albums")
      .build());
    item.put("SK", AttributeValue.builder()
      .s(album.getAlbumType().getType()+":"+album.getId())
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
    item.put("AlbumType", AttributeValue.builder()
      .s(album.getAlbumType().getType())
      .build());

    List<AttributeValue> images = Utilities.createImageMapList(album.getImages());
    if (images.size() > 0) {
      item.put("Images", AttributeValue.builder()
        .l(images)
        .build());
    }
  }
}
