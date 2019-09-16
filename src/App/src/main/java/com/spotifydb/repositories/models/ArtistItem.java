package com.spotifydb.repositories.models;

import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;

public class ArtistItem extends DynamoItem {
  private static Logger logger = LoggerFactory.getLogger(ArtistItem.class);

  public ArtistItem(Artist artist) {
    if (artist == null) {
      logger.error("ArtistItem constructor was passed null");
      throw new IllegalArgumentException();
    }

    logger.info("Creating an ArtistItem for Artist with ID : {}", artist.getId());

    item.put("PK", AttributeValue.builder()
      .s("/artists/"+artist.getId())
      .build());
    item.put("SK", AttributeValue.builder()
      .s(artist.getId())
      .build());
    item.put("Uri", AttributeValue.builder()
      .s(artist.getUri())
      .build());
    item.put("Name", AttributeValue.builder()
      .s(artist.getName())
      .build());
    item.put("Id", AttributeValue.builder()
      .s(artist.getId())
      .build());

    String[] genres = artist.getGenres();

    if (genres.length > 0) {
      item.put("Genres", AttributeValue.builder()
        .ss(genres)
        .build());
    }

    List<String> imageUrls = new ArrayList<>();
    for(Image image: artist.getImages())
      imageUrls.add(image.getUrl());

    if (imageUrls.size() > 0) {
      item.put("Images", AttributeValue.builder()
        .ss(imageUrls)
        .build());
    }
  }
}
