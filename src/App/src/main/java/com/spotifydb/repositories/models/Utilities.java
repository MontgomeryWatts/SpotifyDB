package com.spotifydb.repositories.models;

import com.wrapper.spotify.model_objects.specification.Image;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {
  public static List<AttributeValue> createImageMapList(Image... images) {
    List<AttributeValue> imageList = new ArrayList<>();
    for(Image image: images){
      Map<String, AttributeValue> imageItem = new HashMap<>();

      imageItem.put("Height", AttributeValue.builder()
        .n(image.getHeight().toString())
        .build());
      imageItem.put("Width", AttributeValue.builder()
        .n(image.getWidth().toString())
        .build());
      imageItem.put("ImageUrl", AttributeValue.builder()
        .s(image.getUrl())
        .build());

      imageList.add(AttributeValue.builder().m(imageItem).build());
    }
    return imageList;
  }
}
