package oxygen.api.game.dialog;

import oxygen.api.Dialog;
import oxygen.api.accessors.Interfaces;
import oxygen.api.action.ActionOpcode;
import oxygen.api.action.MenuAction;
import oxygen.api.events.InterfaceInteractEvent;
import oxygen.api.wrappers.InterfaceComponent;

public class ExDialog {

    private static final int DIALOG_GROUP = 219;

    public static boolean isOpen() {
        return Interfaces.isOpen(DIALOG_GROUP);
    }

    public static boolean process(String... options) {
        MenuAction dialogAction = (new MenuAction()).setOpcode(ActionOpcode.INTERFACE_DIALOG).setPrimary(0).setAction("Continue");
        for (String option : options) {
            InterfaceComponent optionComp = Interfaces.getComponent(DIALOG_GROUP, comp ->
                    comp.hasText(option));
            if (optionComp != null) {
                new InterfaceInteractEvent(optionComp, dialogAction).setCompleteCondition(Dialog::isProcessing).execute();
                return true;
            }
        }
        return false;
    }

    public static boolean process(int index) {
        InterfaceComponent[] optionComps = Interfaces.getComponent(DIALOG_GROUP);
        if (optionComps.length <= index) {
            return false;
        }
        MenuAction dialogAction = (new MenuAction()).setOpcode(ActionOpcode.INTERFACE_DIALOG).setPrimary(0).setAction("Continue");
        InterfaceComponent targetOptionComp = optionComps[index];
        if (targetOptionComp != null) {
            new InterfaceInteractEvent(targetOptionComp, dialogAction).setCompleteCondition(Dialog::isProcessing).execute();
            return true;
        }
        return false;
    }
}
