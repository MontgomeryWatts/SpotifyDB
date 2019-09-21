package com.spotifydb.controllers.models;

import com.wrapper.spotify.model_objects.specification.Image;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utilities {
  public static List<Image> parseImageMapList(AttributeValue avList) {
    if (avList == null) return new ArrayList<>();
    List<Image> images = new ArrayList<>();
    for (AttributeValue image: avList.l()) {
      Map<String, AttributeValue> item = image.m();
      images.add(new Image.Builder()
        .setHeight(Integer.parseInt(item.get("Height").n()))
        .setWidth(Integer.parseInt(item.get("Width").n()))
        .setUrl(item.get("ImageUrl").s())
        .build());
    }
    return images;
  }
}
