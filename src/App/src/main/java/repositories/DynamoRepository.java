package repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.models.DynamoItem;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

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
}
