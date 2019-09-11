package managers;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.TooManyRequestsException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Album;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.albums.GetSeveralAlbumsRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistsAlbumsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpotifyManager {
    private SpotifyApi spotifyApi;
    private static Logger logger = LoggerFactory.getLogger(SpotifyManager.class);

    public SpotifyManager(String clientId, String clientSecret){
        logger.info("Initializing SpotifyManager");
        if (clientId == null || clientId.isEmpty()) {
          logger.error("SpotifyManager passed invalid client ID");
          throw new IllegalArgumentException("Invalid client ID");
        }
        if (clientSecret == null || clientSecret.isEmpty()) {
          logger.error("SpotifyManager passed invalid client secret");
          throw new IllegalArgumentException("Invalid client secret");
        }

        spotifyApi = SpotifyApi.builder()
          .setClientId(clientId)
          .setClientSecret(clientSecret)
          .build();

        getClientCredentialsSync();
    }

    private void getClientCredentialsSync(){
        logger.info("Retrieving ClientCredentials synchronously");
        ClientCredentialsRequest request = spotifyApi.clientCredentials().build();
        try {
          ClientCredentials credentials = request.execute();
          spotifyApi.setAccessToken(credentials.getAccessToken());
        } catch (IOException | SpotifyWebApiException e) {
          logger.error("Exception thrown while retrieving credentials in SpotifyManager initialization");
          logger.error(e.getMessage());
        }
    }

    private void handleRateLimit(int seconds) {
        int milliseconds = seconds * 1000;
        logger.info("Rate limit exceeded, attempting to wait for {} milliseconds", milliseconds);
        try {
          Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
          logger.error("Exception thrown while waiting");
          logger.error(e.getMessage());
        }
    }

    public Artist getArtistById(String artistId) {
        logger.info("Retrieving Artist by ID: {}", artistId);
        GetArtistRequest request = spotifyApi.getArtist(artistId).build();
        try {
          return request.execute();
        } catch (IOException | SpotifyWebApiException e) {
          logger.error("Exception thrown while retrieving Artist info for ID: {}", artistId);
          logger.error(e.getMessage());
          return null;
        }
    }

    public Artist[] getArtistsByName(String name) {
        logger.info("Searching for Artists that match name: {}", name);
        List<Artist> artists = new ArrayList<>();
        int offset = 0;
        int itemsTotal = 0;
        do {
          try {
            SearchArtistsRequest request = spotifyApi.searchArtists(name)
              .limit(50) // Max number of results allowed for this operation
              .offset(offset)
              .build();
            Paging<Artist> artistPaging = request.execute();
            itemsTotal = artistPaging.getTotal();
            Artist[] artistArray = artistPaging.getItems();
            Collections.addAll(artists, artistArray);
            offset += artistArray.length;
          } catch (TooManyRequestsException e) {
            handleRateLimit(e.getRetryAfter());
          } catch (IOException | SpotifyWebApiException e) {
            logger.error("Exception thrown while retrieving Artists that match name: {}", name);
            logger.error(e.getMessage());
          }
        } while (artists.size() != itemsTotal);
        return artists.toArray(new Artist[0]);
    }

    public String[] getAllArtistAlbumIds(String artistId) {
        logger.info("Retrieving album IDs for Artist with ID: {}", artistId);
        List<String> albumIds = new ArrayList<>();
        int offset = 0;
        int itemsTotal = 0;
        do {
          try {
            GetArtistsAlbumsRequest request = spotifyApi.getArtistsAlbums(artistId)
              .limit(50) // Max number of results allowed for this operation
              .offset(offset)
              .build();
            Paging<AlbumSimplified> albumsPaging = request.execute();
            itemsTotal = albumsPaging.getTotal();
            AlbumSimplified[] albums = albumsPaging.getItems();
            for (AlbumSimplified album: albums) {
              albumIds.add(album.getId());
            }
            offset += albums.length;
          } catch (TooManyRequestsException e) {
            handleRateLimit(e.getRetryAfter());
          } catch (IOException | SpotifyWebApiException e) {
            logger.error("Exception thrown while retrieving Album IDs for Artist with ID: {}", artistId);
            logger.error(e.getMessage());
          }
        } while (albumIds.size() != itemsTotal);
        return albumIds.toArray(new String[0]);
    }

    public Album[] getAlbumsByIds(String[] albumIds) {
        logger.info("Retrieving Albums by IDs");
        List<Album> albums = new ArrayList<>();
        int startIndex = 0;
        int endIndex = albumIds.length < 20 ? albumIds.length : 20;
        if (albumIds.length > 0) {
          do {
            try {
              String[] slice = Arrays.copyOfRange(albumIds, startIndex, endIndex);
              GetSeveralAlbumsRequest request = spotifyApi.getSeveralAlbums(slice).build();
              Album[] albumsArray = request.execute();
              Collections.addAll(albums, albumsArray);
              startIndex = endIndex;
              endIndex = albumIds.length - startIndex < 20 ? startIndex + (albumIds.length - startIndex) : startIndex + 20;
            } catch (TooManyRequestsException e) {
              handleRateLimit(e.getRetryAfter());
            } catch (IOException | SpotifyWebApiException e) {
              logger.error("Exception thrown while retrieving Albums by ID");
              logger.error(e.getMessage());
            }
          } while (albums.size() != albumIds.length);
        }
        return albums.toArray(new Album[0]);
    }
}
