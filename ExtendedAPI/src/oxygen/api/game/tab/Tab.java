package oxygen.api.game.tab;

import oxygen.api.InterfaceAddress;

import static oxygen.api.game.Constant.FIXED_VIEWPORT_GROUP;
import static oxygen.api.game.Constant.RESIZED_VIEWPORT_GROUP;

public enum Tab {
    COMBAT("Combat Options"),
    SKILLS("Skills"),
    QUEST_LIST("Quest List"),
    INVENTORY("Inventory"),
    EQUIPMENT("Worn Equipment"),
    PRAYER("Prayer"),
    MAGIC("Magic"),
    CLAN_CHAT("Clan Chat"),
    FRIENDS_LIST("Friends List"),
    ACCOUNT_MANAGEMENT("Account Management"),
    LOGOUT("Logout"),
    OPTIONS("Options"),
    EMOTES("Emotes"),
    MUSIC("Music Player");

    private final String action;
    private final InterfaceAddress fixedAddress;
    private final InterfaceAddress resizedAddress;

    Tab(String action) {
        this.action = action;
        this.fixedAddress = new InterfaceAddress(FIXED_VIEWPORT_GROUP, comp -> comp.hasAction(action));
        this.resizedAddress = new InterfaceAddress(RESIZED_VIEWPORT_GROUP, comp -> comp.hasAction(action));
    }

    public String getAction() {
        return action;
    }

    public InterfaceAddress getFixedAddress() {
        return fixedAddress;
    }

    public InterfaceAddress getResizedAddress() {
        return resizedAddress;
    }
}
