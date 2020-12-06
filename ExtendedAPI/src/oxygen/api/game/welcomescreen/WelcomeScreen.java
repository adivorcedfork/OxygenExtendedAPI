package oxygen.api.game.welcomescreen;

import oxygen.api.InterfaceAddress;
import oxygen.api.accessors.Interfaces;
import oxygen.api.wrappers.InterfaceComponent;

public class WelcomeScreen {

    private static final int WELCOME_SCREEN_GROUP = 378;

    private static final InterfaceAddress WELCOME_SCREEN = new InterfaceAddress(WELCOME_SCREEN_GROUP, comp ->
            comp != null && comp.isVisible() && comp.getActions() != null && comp.hasAction("Play"));

    public static boolean isOpen() {
        return Interfaces.isOpen(WELCOME_SCREEN_GROUP);
    }

    public static boolean process() {
        if (!WelcomeScreen.isOpen()) {
            return false;
        }
        InterfaceComponent welcomeScreenComp = WELCOME_SCREEN.lookup();
        return welcomeScreenComp != null && welcomeScreenComp.exists() && welcomeScreenComp.isVisible() && welcomeScreenComp.interact("Play");
    }
}
