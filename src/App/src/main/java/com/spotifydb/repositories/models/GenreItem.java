package com.spotifydb.repositories.models;

import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;

public class GenreItem extends DynamoItem{
  private static Logger logger = LoggerFactory.getLogger(GenreItem.class);

  public GenreItem(String genre, Artist artist) {
    if (genre == null || genre.isEmpty()) {
      logger.error("GenreItem constructor was passed a null genre");
      throw new IllegalArgumentException();
    }
    if (artist == null) {
      logger.error("GenreItem constructor was passed a null Artist");
      throw new IllegalArgumentException();
    }

    logger.info("Creating a GenreItem for genre: {} and Artist with ID: {}", genre, artist.getId());

    item.put("PK", AttributeValue.builder()
      .s("/genres/"+genre)
      .build());
    item.put("SK", AttributeValue.builder()
      .s("/artists/"+artist.getId())
      .build());
    item.put("Name", AttributeValue.builder()
      .s(artist.getName())
      .build());
    item.put("Uri", AttributeValue.builder()
      .s(artist.getUri())
      .build());
    item.put("Id", AttributeValue.builder()
      .s(artist.getId())
      .build());

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
