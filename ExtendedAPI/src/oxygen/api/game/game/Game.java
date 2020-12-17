package oxygen.api.game.game;

import oxygen.api.Client;
import oxygen.api.Timing;
import oxygen.api.accessors.Interfaces;
import oxygen.api.wrappers.InterfaceComponent;

public class Game {

    public static boolean isLoggedIn() {
        return Client.getGameState() >= 30;
    }

    public static boolean logout() {
        if (isLoggedIn()) {
            InterfaceComponent logout = Interfaces.getComponent(182, 8);
            if (logout != null) {
                Timing.sleep(650, 1250);
                if (logout.interact("Logout")) {
                    Timing.sleepUntil(() -> Client.getGameState() != 30, 7500);
                    return true;
                }
            }
        }
        return false;
    }
}
