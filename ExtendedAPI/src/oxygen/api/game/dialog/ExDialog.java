package oxygen.api.game.dialog;

import oxygen.api.accessors.Interfaces;
import oxygen.api.wrappers.InterfaceComponent;

public class ExDialog {

    private static final int DIALOG_GROUP = 219;

    public static boolean isOpen() {
        return Interfaces.isOpen(DIALOG_GROUP);
    }

    public static boolean process(String... options) {
        for (String option : options) {
            InterfaceComponent optionComp = Interfaces.getComponent(DIALOG_GROUP, comp ->
                    comp.hasText(option));
            if (optionComp != null && optionComp.getActions().length > 0) {
                return optionComp.interact(optionComp.getActions()[0]);
            }
        }
        return false;
    }

    public static boolean process(int index) {
        InterfaceComponent[] optionComps = Interfaces.getComponent(DIALOG_GROUP);
        if (optionComps.length <= index) {
            return false;
        }
        InterfaceComponent targetOptionComp = optionComps[index];
        if (targetOptionComp != null && targetOptionComp.getActions().length > 0) {
            return targetOptionComp.interact(targetOptionComp.getActions()[0]);
        }
        return false;
    }
}
