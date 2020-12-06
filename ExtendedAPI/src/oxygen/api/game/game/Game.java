package oxygen.api.game.game;

import oxygen.api.Client;

public class Game {

    public static boolean isLoggedIn() {
        return Client.getGameState() >= 30;
    }

    public static boolean logout() {
        // TODO: Do it
        return false;
    }
}
