package managers;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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

    public void getClientCredentialsSync(){
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
}
