package com.spotifydb.repositories;

import com.spotifydb.repositories.models.DynamoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;


public class DynamoRepository {
  private DynamoDbClient client;
  private String tableName;
  private static Logger logger = LoggerFactory.getLogger(DynamoRepository.class);

  public DynamoRepository(String profileName, String tableName){
    logger.info("Initializing DynamoRepository");
    this.tableName = tableName;
    client = DynamoDbClient.builder()
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

  public GetItemResponse getArtistById(String artistId) {
    logger.info("Retrieving an Artist with ID: {}", artistId);

    Map<String, AttributeValue> key = new HashMap<>();
    key.put("PK", AttributeValue.builder()
      .s("/artists/"+artistId).build());
    key.put("SK", AttributeValue.builder().s(artistId).build());

    GetItemRequest request = GetItemRequest.builder()
      .key(key)
      .tableName(tableName)
      .build();

    try {
      return client.getItem(request);
    } catch (DynamoDbException e) {
      logger.error("Exception throw while retrieving an Artist from DynamoDB");
      logger.error(e.getMessage());
      return null;
    }
  }

  public QueryResponse getArtistItem(String artistId) {
    logger.info("Retrieving an Artist with ID: {}", artistId);

    Condition partitionCondition = Condition.builder()
      .comparisonOperator(ComparisonOperator.EQ)
      .attributeValueList(AttributeValue.builder().s("/artists/"+artistId).build())
      .build();

    Map<String, Condition> conditions = new HashMap<>();
    conditions.put("PK", partitionCondition);

    QueryRequest request = QueryRequest.builder()
      .keyConditions(conditions)
      .tableName(tableName)
      .build();

    try {
      return client.query(request);
    } catch (DynamoDbException e) {
      logger.error("Exception throw while retrieving an Artist from DynamoDB");
      logger.error(e.getMessage());
      return null;
    }
  }
}
