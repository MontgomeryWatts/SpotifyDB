package com.spotifydb.repositories;

import com.spotifydb.repositories.models.DynamoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class DynamoRepository {
  private DynamoDbAsyncClient client;
  private String tableName;
  private static Logger logger = LoggerFactory.getLogger(DynamoRepository.class);

  public DynamoRepository(String profileName, String tableName){
    logger.info("Initializing DynamoRepository");
    this.tableName = tableName;
    client = DynamoDbAsyncClient.builder()
      .region(Region.US_EAST_1)
      .credentialsProvider(ProfileCredentialsProvider.builder()
        .profileName(profileName)
        .build())
      .build();
    logger.info("Successfully created DynamoRepository");
  }

  public void putDynamoItem(DynamoItem item) {
    logger.info("Inserting an item into DynamoDB");
    PutItemRequest request = PutItemRequest.builder()
      .tableName(tableName)
      .item(item.getItem())
      .build();

    try {
      client.putItem(request);
    } catch (DynamoDbException e) {
      logger.error("Exception thrown while inserting an item into DynamoDB");
      logger.error(e.getMessage());
    }
  }

  public CompletableFuture<Map<String, AttributeValue>> getArtistById(String artistId) {
    logger.info("Retrieving an Artist with ID: {}", artistId);

    Map<String, AttributeValue> key = new HashMap<>();

    key.put("PK", AttributeValue.builder()
      .s("/artists/"+artistId).build());
    key.put("SK", AttributeValue.builder()
      .s(artistId).build());

    GetItemRequest request = GetItemRequest.builder()
      .key(key)
      .tableName(tableName)
      .build();

    try {
      CompletableFuture<GetItemResponse> response = client.getItem(request);
      return response.thenApplyAsync(GetItemResponse::item);
    } catch (DynamoDbException e) {
      logger.error("Exception throw while retrieving an Artist from DynamoDB");
      logger.error(e.getMessage());
      return CompletableFuture.completedFuture(null);
    }
  }
}
