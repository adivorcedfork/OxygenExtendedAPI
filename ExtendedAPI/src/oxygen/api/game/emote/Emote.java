package oxygen.api.game.emote;

import oxygen.api.InterfaceAddress;
import oxygen.api.wrappers.InterfaceComponent;

import static oxygen.api.game.Constant.EMOTES_GROUP;
import static oxygen.api.game.Constant.EMOTES_SUBGROUP;

public enum Emote {
    BOW("Bow", new InterfaceAddress(EMOTES_GROUP, EMOTES_SUBGROUP, 2)),
    CRY("Cry", new InterfaceAddress(EMOTES_GROUP, EMOTES_SUBGROUP, 16)),
    CHEER("Cheer", new InterfaceAddress(EMOTES_GROUP, EMOTES_SUBGROUP, 7)),
    SHRUG("Shrug", new InterfaceAddress(EMOTES_GROUP, EMOTES_SUBGROUP, 6)),
    DANCE("Dance", new InterfaceAddress(EMOTES_GROUP, EMOTES_SUBGROUP, 12));

    private final String action;
    private final InterfaceAddress address;

    Emote(String action, InterfaceAddress address) {
        this.action = action;
        this.address = address;
    }

    public String getAction() {
        return action;
    }

    public InterfaceComponent getComponent() {
        return address.lookup();
    }
}
