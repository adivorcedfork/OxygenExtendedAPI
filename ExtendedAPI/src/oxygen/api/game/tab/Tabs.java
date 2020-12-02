package oxygen.api.game.tab;

import oxygen.api.game.viewport.Viewport;
import oxygen.api.game.viewport.Viewports;
import oxygen.api.wrappers.InterfaceComponent;

public class Tabs {

    private static final int TAB_OPEN_MATERIAL_ID = 1030;

    public static Tab getOpen() {
        for (Tab tab : Tab.values()) {
            if (isOpen(tab)) {
                return tab;
            }
        }
        return null;
    }

    public static boolean isOpen(Tab tab) {
        InterfaceComponent tabComp = getComponentByViewport(tab);
        return tabComp != null && tabComp.exists() && tabComp.isVisible() && tabComp.getSpriteID2() >= 0;
    }

    public static boolean open(Tab tab) {
        InterfaceComponent tabComp = getComponentByViewport(tab);
        return tabComp != null && tabComp.exists() && tabComp.isVisible() && tabComp.interact(tab.getAction());
    }

    public static boolean exists(Tab tab) {
        InterfaceComponent tabComp = getComponentByViewport(tab);
        return getComponentByViewport(tab) != null && tabComp.exists() && tabComp.isVisible();
    }

    private static InterfaceComponent getComponentByViewport(Tab tab) {
        if (Viewports.isOpen(Viewport.FIXED)) {
            return tab.getFixedAddress().lookup();
        } else {
            return tab.getResizedAddress().lookup();
        }
    }
}
