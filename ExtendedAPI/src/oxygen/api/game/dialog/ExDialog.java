package oxygen.api.game.dialog;

import oxygen.api.accessors.Interfaces;

public class ExDialog {

    private static final int DIALOG_GROUP = 219;

    public static boolean isOpen() {
        return Interfaces.isOpen(DIALOG_GROUP);
    }
}
