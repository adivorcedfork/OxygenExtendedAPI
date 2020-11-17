package oxygen.api.game.enterinput;

import oxygen.api.InterfaceAddress;
import oxygen.api.util.Input;
import oxygen.api.wrappers.InterfaceComponent;

public class EnterInput {

    private static final int ENTER_INPUT_GROUP = 162; // Child component = 44

    private static final InterfaceAddress ENTER_INPUT_TEXT = new InterfaceAddress(ENTER_INPUT_GROUP, comp ->
            comp.getText().equals("Enter amount:"));

    public static boolean isOpen() {
        InterfaceComponent enterInputComp = ENTER_INPUT_TEXT.lookup();
        return enterInputComp != null && enterInputComp.exists() && enterInputComp.isVisible();
    }

    public static boolean initiate(String text) {
        if (!EnterInput.isOpen()) {
            return false;
        }
        Input.typeText(text, true);
        return true;
    }

    public static boolean initiate(int number) {
        return initiate(String.valueOf(number));
    }
}
