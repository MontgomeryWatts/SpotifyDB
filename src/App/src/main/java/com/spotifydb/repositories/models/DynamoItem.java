package com.spotifydb.repositories.models;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

public abstract class DynamoItem {
  Map<String, AttributeValue> item = new HashMap<>();

  public Map<String, AttributeValue> getItem() {
    return item;
  }
}
