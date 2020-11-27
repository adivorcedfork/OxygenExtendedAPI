package oxygen.api.game.tab;

import oxygen.api.game.viewport.Viewport;
import oxygen.api.game.viewport.Viewports;
import oxygen.api.wrappers.InterfaceComponent;

public class Tabs {

    // private static final int TAB_OPEN_MATERIAL_ID = 1030;

    public static boolean isOpen(Tab tab) {
        InterfaceComponent tabComp = getComponentByViewport(tab);
        return tabComp != null && tabComp.exists() && tabComp.getSpriteID2() >= 0;
    }

    public static boolean open(Tab tab) {
        InterfaceComponent tabComp = getComponentByViewport(tab);
        if (tabComp == null) {
            System.out.println("!!!! CAN'T FIND TAB COMP FOR " + tab.name()); // TODO: Remove
        }
        if (tabComp != null && !tabComp.exists()) {
            System.out.println("!!!! TAB COMP DOES NOT EXIST " + tab.name()); // TODO: Remove
        }
        return tabComp != null && tabComp.exists() && tabComp.interact(tab.getAction());
    }

    private static InterfaceComponent getComponentByViewport(Tab tab) {
        if (Viewports.isOpen(Viewport.FIXED)) {
            return tab.getFixedAddress().lookup();
        } else {
            return tab.getResizedAddress().lookup();
        }
    }
}
