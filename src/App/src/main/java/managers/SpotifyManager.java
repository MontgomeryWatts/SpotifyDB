package managers;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Album;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.albums.GetSeveralAlbumsRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistsAlbumsRequest;
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
            logger.error("Exception thrown retrieving credentials");
        }
    }

    public Artist getArtistById(String artistId) {
        logger.info("Retrieving Artist by ID: {}", artistId);
        GetArtistRequest request = spotifyApi.getArtist(artistId).build();
        try {
            return request.execute();
        } catch (IOException | SpotifyWebApiException e) {
            logger.error("Exception thrown retrieving Artist info for ID: {}", artistId);
            return null;
        }
    }

    public String[] getAllArtistAlbumIds(String artistId) {
        logger.info("Retrieving album IDs for Artist with ID: {}", artistId);
        List<String> albumIds = new ArrayList<>();
        try {
            int offset = 0;
            int itemsAdded = 0;
            int itemsTotal;
            do {
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
                itemsAdded += albums.length;
                offset += albums.length;
            } while (itemsAdded != itemsTotal);
        } catch (IOException | SpotifyWebApiException e) {
            logger.error("Exception thrown while retrieving album IDs for Artist with ID: {}", artistId);
        }
        return albumIds.toArray(new String[0]);
    }

    public Album[] getAlbumsByIds(String[] albumIds) {
        logger.info("Retrieving Albums by IDs");
        List<Album> albums = new ArrayList<>();
        try {
            int startIndex = 0;
            int endIndex = albumIds.length < 20 ? albumIds.length : 20;
            do {
                String[] slice = Arrays.copyOfRange(albumIds, startIndex, endIndex);
                GetSeveralAlbumsRequest request = spotifyApi.getSeveralAlbums(slice).build();
                Album[] albumsArray = request.execute();
                Collections.addAll(albums, albumsArray);
                startIndex = endIndex;
                endIndex = albumIds.length - startIndex < 20 ? startIndex + (albumIds.length - startIndex) : startIndex + 20;
            } while (albums.size() != albumIds.length);
        } catch (IOException | SpotifyWebApiException e) {
            logger.error("Exception thrown while retrieving Albums by ID");
        }
        return albums.toArray(new Album[0]);
    }
}
