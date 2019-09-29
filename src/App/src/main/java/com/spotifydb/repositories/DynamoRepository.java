package com.spotifydb.repositories;

import com.spotifydb.repositories.models.DynamoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
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

  public CompletableFuture<PutItemResponse> putDynamoItem(DynamoItem item) {
    logger.info("Inserting an item into DynamoDB");
    PutItemRequest request = PutItemRequest.builder()
      .tableName(tableName)
      .item(item.getItem())
      .build();

    try {
      return client.putItem(request);
    } catch (DynamoDbException e) {
      logger.error("Exception thrown while inserting an item into DynamoDB");
      logger.error(e.getMessage());
      return CompletableFuture.completedFuture(null);
    }
  }

  public CompletableFuture<Map<String, AttributeValue>> getAlbumById(String albumId) {
    logger.info("Retrieving an Album with ID: {}", albumId);

    Map<String, AttributeValue> key = new HashMap<>();

    key.put("PK", AttributeValue.builder()
      .s("/albums/"+albumId).build());
    key.put("SK", AttributeValue.builder()
      .s(albumId).build());

    GetItemRequest request = GetItemRequest.builder()
      .key(key)
      .tableName(tableName)
      .build();

    try {
      CompletableFuture<GetItemResponse> response = client.getItem(request);
      return response.thenApplyAsync(GetItemResponse::item);
    } catch (DynamoDbException e) {
      logger.error("Exception throw while retrieving an Album from DynamoDB");
      logger.error(e.getMessage());
      return CompletableFuture.completedFuture(new HashMap<>());
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
      return CompletableFuture.completedFuture(new HashMap<>());
    }
  }

  public CompletableFuture<List<Map<String, AttributeValue>>> getArtistAlbumsById(String artistId) {
    logger.info("Retrieving Albums for Artist with ID: {}", artistId);

    String partitionAlias = "#p";
    String partitionKey = "PK";
    String partitionKeyValue = "/artists/"+artistId+"/albums";
    String keyConditionExpression = partitionAlias + " = :" + partitionKey;

    Map<String, String> attributeNameAlias = new HashMap<>();
    attributeNameAlias.put(partitionAlias, partitionKey);

    Map<String, AttributeValue> attributeValue = new HashMap<>();
    attributeValue.put(":"+partitionKey, AttributeValue.builder()
      .s(partitionKeyValue).build());

    QueryRequest request = QueryRequest.builder()
      .keyConditionExpression(keyConditionExpression)
      .expressionAttributeNames(attributeNameAlias)
      .expressionAttributeValues(attributeValue)
      .tableName(tableName)
      .build();

    try {
      CompletableFuture<QueryResponse> response = client.query(request);
      return response.thenApplyAsync(QueryResponse::items);
    } catch (DynamoDbException e) {
      logger.error("Exception throw while retrieving an Artist's Albums from DynamoDB");
      logger.error(e.getMessage());
      return CompletableFuture.completedFuture(new ArrayList<>());
    }
  }

  public CompletableFuture<String> getRandomArtistId() {
    logger.info("Retrieving a random Artist ID from DynamoDB");


    String nameAlias = "#n";
    String imageAlias = "#i";
    String filterExpression = "attribute_exists("+nameAlias+") and attribute_exists("+imageAlias+")";

    Map<String, String> attributeNameAlias = new HashMap<>();
    attributeNameAlias.put(nameAlias, "Name");
    attributeNameAlias.put(imageAlias, "Images");

    Map<String, AttributeValue> startKey = new HashMap<>();
    List<Map<String, AttributeValue>> items;
    try {
      do {
        String fakeId = createFakeId();
        startKey.put("PK", AttributeValue.builder()
          .s("/artists/"+fakeId).build());
        startKey.put("SK", AttributeValue.builder()
          .s(fakeId).build());

        ScanRequest request = ScanRequest.builder()
          .tableName(tableName)
          .exclusiveStartKey(startKey)
          .limit(100)
          .expressionAttributeNames(attributeNameAlias)
          .filterExpression(filterExpression)
          .build();

        CompletableFuture<ScanResponse> response = client.scan(request);
        CompletableFuture<List<Map<String,AttributeValue>>> itemsFuture = response.thenApplyAsync(ScanResponse::items);
        items = itemsFuture.join();
      } while (items.isEmpty());
    } catch (DynamoDbException e) {
      logger.error("Exception thrown while retrieving a random Artist ID from DynamoDB");
      return CompletableFuture.completedFuture(null);
    }
    String id = items.get(0).get("Id").s();
    return CompletableFuture.completedFuture(id);
  }

  private String createFakeId() {
    String uuid = UUID.randomUUID().toString();
    String stripped = uuid.replace("-", "");
    return stripped.substring(0, 22);
  }
}
