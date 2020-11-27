package oxygen.api.game.emote;

import oxygen.api.InterfaceAddress;
import oxygen.api.wrappers.InterfaceComponent;

public enum Emote {

    BOW("Bow", new InterfaceAddress(216, 1, 2)),
    CRY("Cry", new InterfaceAddress(216, 1, 16)),
    CHEER("Cheer", new InterfaceAddress(216, 1, 7)),
    SHRUG("Shrug", new InterfaceAddress(216, 1, 6)),
    DANCE("Dance", new InterfaceAddress(216, 1, 12));

    private final String name;
    private final InterfaceAddress address;

    Emote(String name, InterfaceAddress address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public InterfaceComponent getComponent() {
        return address.lookup();
    }
}
