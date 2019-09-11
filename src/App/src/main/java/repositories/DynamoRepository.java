package repositories;

import com.wrapper.spotify.model_objects.specification.Album;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

  public void putGenreItem(String genre, Artist artist) {
    logger.info("Inserting a Genre record for artist with ID: {} in genre: {}", artist.getId(), genre);
    HashMap<String, AttributeValue> genreItem = new HashMap<>();

    genreItem.put("PK", AttributeValue.builder()
      .s("/genres/"+genre)
      .build());
    genreItem.put("SK", AttributeValue.builder()
      .s("/artists/"+artist.getId())
      .build());
    genreItem.put("Name", AttributeValue.builder()
      .s(artist.getName())
      .build());
    genreItem.put("Uri", AttributeValue.builder()
      .s(artist.getUri())
      .build());

    List<String> imageUrls = new ArrayList<>();
    for(Image image: artist.getImages())
      imageUrls.add(image.getUrl());

    genreItem.put("Images", AttributeValue.builder()
      .ss(imageUrls)
      .build());

    PutItemRequest request = PutItemRequest.builder()
      .tableName(tableName)
      .item(genreItem)
      .build();
    try {
      client.putItem(request);
    } catch (Exception e) {
      logger.error("Exception thrown while inserting a Genre item into DynamoDB");
      logger.error(e.getMessage());
    }
  }

  public void putArtistAlbumItem(Artist artist, Album album) {
    logger.info("Inserting an Album record for artist with ID: {} and album with ID: {}", artist.getId(), album.getId());
    HashMap<String, AttributeValue> artistAlbumItem = new HashMap<>();

    artistAlbumItem.put("PK", AttributeValue.builder()
      .s("/artists/"+artist.getId()+"/albums")
      .build());
    artistAlbumItem.put("SK", AttributeValue.builder()
      .s(album.getId())
      .build());
    artistAlbumItem.put("Uri", AttributeValue.builder()
      .s(album.getUri())
      .build());
    artistAlbumItem.put("Title", AttributeValue.builder()
      .s(album.getName())
      .build());
    artistAlbumItem.put("AlbumType", AttributeValue.builder()
      .s(album.getAlbumType().getType())
      .build());

    List<String> imageUrls = new ArrayList<>();
    for(Image image: album.getImages())
      imageUrls.add(image.getUrl());

    artistAlbumItem.put("Images", AttributeValue.builder()
      .ss(imageUrls)
      .build());

    PutItemRequest request = PutItemRequest.builder()
      .tableName(tableName)
      .item(artistAlbumItem)
      .build();
    try {
      client.putItem(request);
    } catch (Exception e) {
      logger.error("Exception thrown while inserting a Artist Album item into DynamoDB");
      logger.error(e.getMessage());
    }
  }

  public void putArtistItem(Artist artist) {
    logger.info("Inserting an Artist record for artist with ID: {}", artist.getId());
    HashMap<String, AttributeValue> artistItem = new HashMap<>();

    artistItem.put("PK", AttributeValue.builder()
      .s("/artists/"+artist.getId())
      .build());
    artistItem.put("SK", AttributeValue.builder()
      .s(artist.getName())
      .build());
    artistItem.put("Uri", AttributeValue.builder()
      .s(artist.getUri())
      .build());
    artistItem.put("Genres", AttributeValue.builder()
      .ss(artist.getGenres())
      .build());

    List<String> imageUrls = new ArrayList<>();
    for(Image image: artist.getImages())
      imageUrls.add(image.getUrl());

    artistItem.put("Images", AttributeValue.builder()
      .ss(imageUrls)
      .build());
    PutItemRequest request = PutItemRequest.builder()
      .tableName(tableName)
      .item(artistItem)
      .build();
    try {
      client.putItem(request);
    } catch (Exception e) {
      logger.error("Exception thrown while inserting an Artist item into DynamoDB");
      logger.error(e.getMessage());
    }
  }
}
