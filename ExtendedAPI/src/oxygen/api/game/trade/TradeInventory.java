package oxygen.api.game.trade;

import oxygen.api.InterfaceAddress;
import oxygen.api.accessors.Interfaces;
import oxygen.api.commons.StringCommons;
import oxygen.api.wrappers.InterfaceComponent;

public class TradeInventory {

    private static final int TRADE_INVENTORY_GROUP = 336;
    private static final InterfaceAddress TRADE_INVENTORY_ADDRESS = new InterfaceAddress(TRADE_INVENTORY_GROUP, 0);

    private static InterfaceComponent[] getTradeInventory() {
        InterfaceComponent parent = TRADE_INVENTORY_ADDRESS.lookup();
        if (parent != null) {
            return parent.getChildren();
        }
        return new InterfaceComponent[0];
    }

    public static InterfaceComponent getFirst(String name) {
        for (InterfaceComponent itemComp : getTradeInventory()) {
            if (StringCommons.replaceColorTag(itemComp.getName()).equals(name)) {
                return itemComp;
            }
        }
        return null;
    }

    public static InterfaceComponent getFirst(int id) {
        for (InterfaceComponent itemComp : getTradeInventory()) {
            if (itemComp.getItemID() == id) {
                return itemComp;
            }
        }
        return null;
    }

    public static InterfaceComponent getAt(int index) {
        return Interfaces.getComponent(TRADE_INVENTORY_GROUP, 0, index);
    }
}
