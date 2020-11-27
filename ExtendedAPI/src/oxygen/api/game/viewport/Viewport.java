package oxygen.api.game.viewport;

import oxygen.api.InterfaceAddress;

import static oxygen.api.game.Constant.FIXED_VIEWPORT_GROUP;
import static oxygen.api.game.Constant.RESIZED_VIEWPORT_GROUP;

public enum Viewport {
    FIXED(new InterfaceAddress(FIXED_VIEWPORT_GROUP, 0)),
    RESIZED(new InterfaceAddress(RESIZED_VIEWPORT_GROUP, 0));

    private final InterfaceAddress address;

    Viewport(InterfaceAddress address) {
        this.address = address;
    }

    public InterfaceAddress getAddress() {
        return address;
    }
}
