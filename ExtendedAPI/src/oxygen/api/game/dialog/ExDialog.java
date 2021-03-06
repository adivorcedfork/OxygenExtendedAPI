package oxygen.api.game.dialog;

import oxygen.api.Dialog;
import oxygen.api.accessors.Interfaces;
import oxygen.api.action.ActionOpcode;
import oxygen.api.action.MenuAction;
import oxygen.api.events.InterfaceInteractEvent;
import oxygen.api.util.Input;
import oxygen.api.wrappers.InterfaceComponent;

import java.util.Arrays;

public class ExDialog {

    private static final int DIALOG_GROUP = 219;
    private static final int SECONDARY_DIALOG_GROUP = 217;

    private static boolean isPrimaryOpen() {
        return Interfaces.isOpen(DIALOG_GROUP);
    }

    private static boolean isSecondaryOpen() {
        return Interfaces.isOpen(SECONDARY_DIALOG_GROUP);
    }

    public static boolean isOpen() {
        return isPrimaryOpen() || isSecondaryOpen();
    }

    public static boolean isViewingChatOptions() {
        return (isPrimaryOpen() && Interfaces.getComponent(DIALOG_GROUP).length > 0)
                || (isSecondaryOpen() && Interfaces.getComponent(SECONDARY_DIALOG_GROUP).length > 0);
    }

    public static boolean process(String... options) {
        MenuAction dialogAction = (new MenuAction()).setOpcode(ActionOpcode.INTERFACE_DIALOG).setPrimary(0).setAction("Continue");
        InterfaceComponent childOptionsContainer = Interfaces.getComponent(DIALOG_GROUP, 1);

        for (String option : options) {
            InterfaceComponent optionComp = Interfaces.getComponent(DIALOG_GROUP, comp ->
                    comp.hasText(option));

            if (option == null && childOptionsContainer != null) { // See if option is grandchild component
                optionComp = getFirstMatch(childOptionsContainer, options);
            }
            if (optionComp != null) {
                new InterfaceInteractEvent(optionComp, dialogAction).setCompleteCondition(Dialog::isProcessing).execute();
                return true;
            }
        }
        return false;
    }

    public static boolean process(int index) {
        return process(index, false);
    }

    public static boolean process(int index, boolean click) {
        if (!click) {
            Input.typeText(String.valueOf(index + 1), false);
            return true;
        }

        InterfaceComponent[] optionComps = Interfaces.getComponent(DIALOG_GROUP);
        InterfaceComponent childOptionsContainer = Interfaces.getComponent(DIALOG_GROUP, 1);
        InterfaceComponent targetOptionComp = null;

        if (optionComps.length > index) {
            targetOptionComp = optionComps[index];
        }
        if (targetOptionComp == null) {
            targetOptionComp = getFirstMatch(childOptionsContainer, index);
        }
        if (targetOptionComp == null) {
            return false;
        }

        MenuAction dialogAction = (new MenuAction()).setOpcode(ActionOpcode.INTERFACE_DIALOG).setPrimary(0).setAction("Continue");
        new InterfaceInteractEvent(targetOptionComp, dialogAction).setCompleteCondition(Dialog::isProcessing).execute();
        return true;
    }

    // Gets first component that contains any option text
    private static InterfaceComponent getFirstMatch(InterfaceComponent parent, String... options) {
        for (InterfaceComponent child : parent.getChildren()) {
            if (Arrays.stream(options).anyMatch(parent::hasText)) {
                return child;
            }
        }
        return null;
    }

    // Gets first component that contains any option text
    private static InterfaceComponent getFirstMatch(InterfaceComponent parent, int index) {
        InterfaceComponent[] children = parent.getChildren();
        if (children.length > index) {
            return children[index];
        }
        return null;
    }
}
