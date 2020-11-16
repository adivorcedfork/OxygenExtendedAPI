package oxygen.api.game.trade;

import oxygen.api.InterfaceAddress;
import oxygen.api.accessors.Interfaces;
import oxygen.api.commons.NumberCommons;
import oxygen.api.commons.StringCommons;
import oxygen.api.containers.Inventory;
import oxygen.api.wrappers.InterfaceComponent;
import oxygen.api.wrappers.Item;

import java.util.Arrays;
import java.util.function.Predicate;

public class Trade {

    public enum Party {
        ME,
        THEM
    }

    public enum Screen {
        FIRST,
        SECOND
    }

    public enum OfferAction {
        ONE("Offer"),
        FIVE("Offer-5"),
        TEN("Offer-10"),
        ALL("Offer-All"),
        X("Offer-X");

        private final String action;

        OfferAction(String action) {
            this.action = action;
        }

        public String getAction() {
            return action;
        }
    }

    private static final int FIRST_TRADE_WINDOW_GROUP = 335;
    private static final int SECOND_TRADE_WINDOW_GROUP = 334;

    public static boolean isOpen() {
        return Trade.isOpen(Screen.FIRST) || Trade.isOpen(Screen.SECOND);
    }

    public static boolean isOpen(Screen screen) {
        if (screen == Screen.FIRST) {
            return Interfaces.isOpen(FIRST_TRADE_WINDOW_GROUP);
        }
        else if (screen == Screen.SECOND) {
            return Interfaces.isOpen(SECOND_TRADE_WINDOW_GROUP);
        }
        return false;
    }

    private static final InterfaceAddress FIRST_SCREEN_ACCEPT_BUTTON = new InterfaceAddress(FIRST_TRADE_WINDOW_GROUP, comp ->
            comp.hasAction("Accept"));

    private static final InterfaceAddress SECOND_SCREEN_ACCEPT_BUTTON = new InterfaceAddress(SECOND_TRADE_WINDOW_GROUP, comp ->
            comp.hasAction("Accept"));

    public static boolean accept() {
        InterfaceComponent acceptButton = Trade.isOpen(Screen.FIRST) ? FIRST_SCREEN_ACCEPT_BUTTON.lookup() : SECOND_SCREEN_ACCEPT_BUTTON.lookup();
        return acceptButton != null && acceptButton.interact("Accept");
    }

    private static final InterfaceAddress FIRST_SCREEN_DECLINE_BUTTON = new InterfaceAddress(FIRST_TRADE_WINDOW_GROUP, comp ->
            comp.hasAction("Decline"));

    private static final InterfaceAddress SECOND_SCREEN_DECLINE_BUTTON = new InterfaceAddress(SECOND_TRADE_WINDOW_GROUP, comp ->
            comp.hasAction("Decline"));

    public static boolean decline() {
        InterfaceComponent declineButton = Trade.isOpen(Screen.FIRST) ? FIRST_SCREEN_DECLINE_BUTTON.lookup() : SECOND_SCREEN_DECLINE_BUTTON.lookup();
        return declineButton != null && declineButton.interact("Decline");
    }

    // TODO: Do by InterfaceAddress instead of predicate? InterfaceAddress didn't update before. Do by checking comp.getText(message)?
    private static final Predicate<InterfaceComponent> FIRST_SCREEN_WE_ACCEPTED_PRED = comp ->
            comp.getText().equals("Waiting for other player...");

    private static final Predicate<InterfaceComponent> FIRST_SCREEN_THEY_ACCEPTED_PRED = comp ->
            comp.getText().equals("Other player has accepted.");

    private static final Predicate<InterfaceComponent> SECOND_SCREEN_WE_ACCEPTED_PRED = comp ->
            comp.getText().equals("Waiting for other player...");

    private static final Predicate<InterfaceComponent> SECOND_SCREEN_THEY_ACCEPTED_PRED = comp ->
            comp.getText().equals("Other player has accepted.");

    public static boolean hasAccepted(Party party) {
        if (Trade.isOpen(Screen.FIRST)) {
            return Interfaces.getComponent(FIRST_TRADE_WINDOW_GROUP, party == Party.ME ? FIRST_SCREEN_WE_ACCEPTED_PRED : FIRST_SCREEN_THEY_ACCEPTED_PRED) != null;
        }
        else if (Trade.isOpen(Screen.SECOND)) {
            return Interfaces.getComponent(SECOND_TRADE_WINDOW_GROUP, party == Party.ME ? SECOND_SCREEN_WE_ACCEPTED_PRED : SECOND_SCREEN_THEY_ACCEPTED_PRED) != null;
        }
        return false;
    }

    private static final String INVENTORY_ITEM_ACTION_TAG = "<col=ff9040>"; // TODO: Do this better

    private static boolean offer(Item item, OfferAction amount) {
        if (item == null || amount == null) {
            return false;
        }
        InterfaceComponent itemComp = TradeInventory.getAt(item.getIndex());
        if (itemComp == null) {
            return false;
        }
        return itemComp.interact(amount.getAction() + INVENTORY_ITEM_ACTION_TAG);
    }

    public static boolean offer(String name, OfferAction amount) {
        return offer(Inventory.getFirst(name), amount);
    }

    public static boolean offer(int id, OfferAction amount) {
        return offer(Inventory.getFirst(id), amount);
    }

    public static boolean offer(Predicate<Item> predicate, OfferAction amount) {
        return offer(Inventory.getFirst(predicate), amount);
    }

    public static boolean offerAll(String name) {
        return offer(name, OfferAction.ALL);
    }

    public static boolean offerAll(int id) {
        return offer(id, OfferAction.ALL);
    }

    public static boolean offerAll(Predicate<Item> predicate) {
        return offer(predicate, OfferAction.ALL);
    }

    public static boolean offerX(String name) {
        return offer(name, OfferAction.X);
    }

    public static boolean offerX(int id) {
        return offer(id, OfferAction.X);
    }

    public static boolean offerX(Predicate<Item> predicate) {
        return offer(predicate, OfferAction.X);
    }



    //335, 28, <child> is their items
    //335, 25 <child> your items


    public static boolean getCount(String item) {
        return false; // TODO: Implement
    }
//
//    public static boolean contains(String item) {
//        return false; // TODO: Implement
//    }
//
//    public static boolean containsAnyExcept(String item) {
//        return false; // TODO: Implement
//    }



    private static final String FIRST_SCREEN_TRADER_NAME_PREFIX = "Trading With: ";
    private static final InterfaceAddress FIRST_SCREEN_TRADER_NAME = new InterfaceAddress(FIRST_TRADE_WINDOW_GROUP, comp ->
            comp.getText().startsWith(FIRST_SCREEN_TRADER_NAME_PREFIX));

    private static final String SECOND_SCREEN_TRADER_NAME_PREFIX = "Trading with:";
    private static final InterfaceAddress SECOND_SCREEN_TRADER_NAME = new InterfaceAddress(SECOND_TRADE_WINDOW_GROUP, comp ->
            comp.getText().startsWith(SECOND_SCREEN_TRADER_NAME_PREFIX));

    public static String getTheirName() {
        InterfaceComponent traderNameComp = Trade.isOpen(Screen.FIRST) ? FIRST_SCREEN_TRADER_NAME.lookup() : SECOND_SCREEN_TRADER_NAME.lookup();
        String prefix = Trade.isOpen(Screen.FIRST) ? FIRST_SCREEN_TRADER_NAME_PREFIX : SECOND_SCREEN_TRADER_NAME_PREFIX;
        if (traderNameComp != null && traderNameComp.isVisible()) {
            return StringCommons.replaceColorTag(traderNameComp.getText()).replaceAll(prefix, "").trim();
        }
        return null;
    }

    private static final String[] FIRST_SCREEN_MY_OFFER_PRICE_PREFIXES = new String[] {"Your offer:", "You offer:"};
    private static final InterfaceAddress FIRST_SCREEN_MY_OFFER_PRICE = new InterfaceAddress(FIRST_TRADE_WINDOW_GROUP, comp ->
            Arrays.stream(FIRST_SCREEN_MY_OFFER_PRICE_PREFIXES).anyMatch(comp.getText()::startsWith));

    private static final InterfaceAddress FIRST_SCREEN_THEIR_OFFER_PRICE = new InterfaceAddress(FIRST_TRADE_WINDOW_GROUP, comp ->
            comp.getText().startsWith(getTheirName() + " offers:"));

    private static final String SECOND_SCREEN_MY_OFFER_PRICE_PREFIX = "You are about to give:";
    private static final InterfaceAddress SECOND_SCREEN_MY_OFFER_PRICE = new InterfaceAddress(SECOND_TRADE_WINDOW_GROUP, comp ->
            comp.getText().startsWith(SECOND_SCREEN_MY_OFFER_PRICE_PREFIX));

    private static final String SECOND_SCREEN_THEIR_OFFER_PRICE_PREFIX = "In return you will receive:";
    private static final InterfaceAddress SECOND_SCREEN_THEIR_OFFER_PRICE = new InterfaceAddress(SECOND_TRADE_WINDOW_GROUP, comp ->
            comp.getText().startsWith(SECOND_SCREEN_THEIR_OFFER_PRICE_PREFIX));

    public static int getOfferPrice(Party party) {
        InterfaceComponent priceComp = null;
        if (Trade.isOpen(Screen.FIRST)) {
            priceComp = party == Party.ME ? FIRST_SCREEN_MY_OFFER_PRICE.lookup() : FIRST_SCREEN_THEIR_OFFER_PRICE.lookup();
        }
        if (Trade.isOpen(Screen.SECOND)) {
            priceComp = party == Party.ME ? SECOND_SCREEN_MY_OFFER_PRICE.lookup() : SECOND_SCREEN_THEIR_OFFER_PRICE.lookup();
        }
        if (priceComp != null && priceComp.isVisible()) {
            String priceText = StringCommons.replaceColorTag(priceComp.getText());
            if (priceText.contains("One")) {
                return 1;
            } else {
                return NumberCommons.numbersFromString(priceText.substring(priceText.indexOf(':')));
            }
        }
        return -1;
    }



//    public static List<RSItemDefinition> getAllItemDefinitions() {
//        List<RSItemDefinition> itemDefinitions = new ArrayList<>();
//        itemDefinitions.addAll(getItemDefinitions(false));
//        itemDefinitions.addAll(getItemDefinitions(true));
//        return itemDefinitions;
//    }


//    public static List<RSItemDefinition> getItemDefinitions(boolean theirItems) {
//        if (Trade.isOpen(Screen.FIRST)) {
//            return getFirstTradeScreenDefinitions(theirItems);
//        }
//        if (Trade.isOpen(Screen.SECOND)) {
//            return getSecondTradeScreenDefinitions(theirItems);
//        }
//        return new ArrayList<>();
//    }


//    private static List<RSItemDefinition> getFirstTradeScreenDefinitions(boolean theirItems) {
//        List<RSItemDefinition> itemDefinitions = new ArrayList<>();
//        Item[] items = theirItems ? Trade.getTheirItems() : Trade.getMyItems(); // TODO: Fix
//        for (Item item : items) {
//            RSItemDefinition itemDefinition = Definitions.getItem(item.getID());
//            if (itemDefinition != null) {
//                itemDefinitions.add(itemDefinition);
//            }
//        }
//        return itemDefinitions;
//    }
//
//    private static final int SECOND_SCREEN_MY_ITEMS_INDEX = 28;
//    private static final int SECOND_SCREEN_THEIR_ITEMS_INDEX = 29;
//    private static final InterfaceAddress SECOND_SCREEN_MY_ITEMS = new InterfaceAddress(SECOND_TRADE_WINDOW_PARENT_INDEX, comp ->
//            comp.getChildIndex() == SECOND_SCREEN_MY_ITEMS_INDEX); // comp != null &&
//    private static final InterfaceAddress SECOND_SCREEN_THEIR_ITEMS = new InterfaceAddress(SECOND_TRADE_WINDOW_PARENT_INDEX, comp ->
//            comp.getChildIndex() == SECOND_SCREEN_THEIR_ITEMS_INDEX); // comp != null &&

//    private static List<RSItemDefinition> getSecondTradeScreenDefinitions(boolean theirItems) {
//        List<RSItemDefinition> itemDefinitions = new ArrayList<>();
//        InterfaceComponent itemContainerComp = theirItems ? SECOND_SCREEN_MY_ITEMS.lookup() : SECOND_SCREEN_THEIR_ITEMS.lookup();
//        if (itemContainerComp == null) {
//            return itemDefinitions;
//        }
//        InterfaceComponent[] itemComps = itemContainerComp.getChildren();
//        if (itemComps == null) {
//            return itemDefinitions;
//        }
//        for (InterfaceComponent itemComp : itemComps) {
//            String itemText = itemComp.getText();
//            if (itemText == null) {
//                continue;
//            }
//            String itemName = (itemText.contains("<")) ? itemText.substring(0, itemText.indexOf('<')) : itemText;
//            RSItemDefinition itemDefinition = null; //Definitions.getItem(itemName, a->true); // TODO: Fix
//            if (itemDefinition != null) {
//                itemDefinitions.add(itemDefinition);
//            }
//        }
//        return itemDefinitions;
//    }
}
