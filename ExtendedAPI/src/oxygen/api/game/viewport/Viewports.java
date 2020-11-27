package oxygen.api.game.viewport;

import oxygen.api.wrappers.InterfaceComponent;

public class Viewports {

    public static boolean isOpen(Viewport viewport) {
        InterfaceComponent viewportComp = viewport.getAddress().lookup();
        return viewportComp != null && viewportComp.exists() && viewportComp.isVisible();
    }
}
