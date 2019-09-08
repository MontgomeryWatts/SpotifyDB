package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SpotifyManagerTests {
    SpotifyManager CuT;

    private final String fakeId = "abcdef";
    private final String fakeSecret = "ghijkl";

    @Test
    public void nullClientId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
           CuT = new SpotifyManager(null, fakeSecret);
        });
    }

    @Test
    public void emptyClientId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CuT = new SpotifyManager("", fakeSecret);
        });
    }

    @Test
    public void nullClientSecret() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
           CuT = new SpotifyManager(fakeId, null);
        });
    }

    @Test
    public void emptyClientSecret() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CuT = new SpotifyManager(fakeId, "");
        });
    }

    @Test
    public void validCredentials() {
        Assertions.assertDoesNotThrow(() -> {
            CuT = new SpotifyManager(fakeId, fakeSecret);
        });
    }
}
