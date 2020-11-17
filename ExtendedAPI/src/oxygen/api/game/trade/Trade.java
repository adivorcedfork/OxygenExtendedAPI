package oxygen.api.game.trade;

import oxygen.api.InterfaceAddress;
import oxygen.api.accessors.Interfaces;
import oxygen.api.commons.ArrayCommons;
import oxygen.api.commons.NumberCommons;
import oxygen.api.commons.StringCommons;
import oxygen.api.containers.Inventory;
import oxygen.api.wrappers.InterfaceComponent;
import oxygen.api.wrappers.Item;

import java.util.Arrays;
import java.util.Objects;
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


    private static final String WE_ACCEPTED_MESSAGE = "Waiting for other player...";
    private static final String THEY_ACCEPTED_MESSAGE = "Other player has accepted.";

    private static final InterfaceAddress FIRST_SCREEN_WE_ACCEPTED = new InterfaceAddress(FIRST_TRADE_WINDOW_GROUP, comp ->
            comp.getText().equals(WE_ACCEPTED_MESSAGE));

    private static final InterfaceAddress FIRST_SCREEN_THEY_ACCEPTED = new InterfaceAddress(FIRST_TRADE_WINDOW_GROUP, comp ->
            comp.getText().equals(THEY_ACCEPTED_MESSAGE));

    private static final InterfaceAddress SECOND_SCREEN_WE_ACCEPTED = new InterfaceAddress(SECOND_TRADE_WINDOW_GROUP, comp ->
            comp.getText().equals(WE_ACCEPTED_MESSAGE));

    private static final InterfaceAddress SECOND_SCREEN_THEY_ACCEPTED = new InterfaceAddress(SECOND_TRADE_WINDOW_GROUP, comp ->
            comp.getText().equals(THEY_ACCEPTED_MESSAGE));

    public static boolean hasAccepted(Party party) {
        String acceptedText = party == Party.ME ? WE_ACCEPTED_MESSAGE : THEY_ACCEPTED_MESSAGE;
        InterfaceComponent acceptedComp = null;
        if (Trade.isOpen(Screen.FIRST)) {
            acceptedComp = party == Party.ME ? FIRST_SCREEN_WE_ACCEPTED.lookup() : FIRST_SCREEN_THEY_ACCEPTED.lookup();
        }
        else if (Trade.isOpen(Screen.SECOND)) {
            acceptedComp = party == Party.ME ? SECOND_SCREEN_WE_ACCEPTED.lookup() : SECOND_SCREEN_THEY_ACCEPTED.lookup();
        }
        return acceptedComp != null && acceptedComp.exists() && acceptedComp.isVisible() && acceptedComp.getText().equals(acceptedText);
    }

    private static final String INVENTORY_ITEM_ACTION_TAG = "<col=ff9040>"; // TODO: Do this better

    private static boolean offer(Item item, OfferAction action) {
        if (item == null || action == null) {
            return false;
        }
        InterfaceComponent itemComp = TradeInventory.getAt(item.getIndex());
        if (itemComp == null) {
            return false;
        }
        return itemComp.interact(action.getAction() + INVENTORY_ITEM_ACTION_TAG);
    }

    public static boolean offer(String name, OfferAction action) {
        return offer(Inventory.getFirst(name), action);
    }

    public static boolean offer(int id, OfferAction action) {
        return offer(Inventory.getFirst(id), action);
    }

    public static boolean offer(Predicate<Item> predicate, OfferAction action) {
        return offer(Inventory.getFirst(predicate), action);
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


    private static final int OUR_OFFER_ITEMS_CHILD_INDEX = 25;
    private static final int THEIR_OFFER_ITEMS_CHILD_INDEX = 28;

    private static final InterfaceAddress OUR_OFFER_ITEMS = new InterfaceAddress(FIRST_TRADE_WINDOW_GROUP, OUR_OFFER_ITEMS_CHILD_INDEX);
    private static final InterfaceAddress THEIR_OFFER_ITEMS = new InterfaceAddress(FIRST_TRADE_WINDOW_GROUP, THEIR_OFFER_ITEMS_CHILD_INDEX);

    private static InterfaceComponent[] getItemComponents(Party party) {
        InterfaceComponent parent = party == Party.ME ? OUR_OFFER_ITEMS.lookup() : THEIR_OFFER_ITEMS.lookup();
        if (parent != null) {
            return Arrays.stream(parent.getChildren())
                    .filter(comp -> comp.getItemID() != -1 && comp.isVisible() && comp.exists())
                    .toArray(InterfaceComponent[]::new);
        }
        return new InterfaceComponent[0];
    }

    private static InterfaceComponent[] getItemComponents() {
        return ArrayCommons.concatenate(getItemComponents(Party.ME), getItemComponents(Party.THEM));
    }

    private static InterfaceComponent getItemComponent(String name, Party party) {
        for (InterfaceComponent itemComp : getItemComponents(party)) {
            if (StringCommons.replaceColorTag(itemComp.getName()).equals(name)) {
                return itemComp;
            }
        }
        return null;
    }

    private static InterfaceComponent getItemComponent(int id, Party party) {
        for (InterfaceComponent itemComp : getItemComponents(party)) {
            if (itemComp.getItemID() == id) {
                return itemComp;
            }
        }
        return null;
    }

    private static InterfaceComponent[] getItemComponents(String name) {
        InterfaceComponent[] itemComps = new InterfaceComponent[] { getItemComponent(name, Party.ME), getItemComponent(name, Party.THEM) };
        return Arrays.stream(itemComps)
                .filter(Objects::nonNull)
                .toArray(InterfaceComponent[]::new);
    }

    private static InterfaceComponent[] getItemComponents(int id) {
        InterfaceComponent[] itemComps = new InterfaceComponent[] { getItemComponent(id, Party.ME), getItemComponent(id, Party.THEM) };
        return Arrays.stream(itemComps)
                .filter(Objects::nonNull)
                .toArray(InterfaceComponent[]::new);
    }

    private static int getCount(InterfaceComponent[] itemComps) {
        int count = 0;
        for (InterfaceComponent itemComp : itemComps) {
            count += Math.max(0, itemComp.getItemCount());
        }
        return count;
    }

    private static int getCount(InterfaceComponent itemComp) {
        if (itemComp == null) {
            return 0;
        }
        return Math.max(0, itemComp.getItemCount());
    }

    public static int getCount(String name) {
        return getCount(getItemComponents(name));
    }

    public static int getCount(int id) {
        return getCount(getItemComponents(id));
    }

    public static int getCount(String name, Party party) {
        return getCount(getItemComponent(name, party));
    }

    public static int getCount(int id, Party party) {
        return getCount(getItemComponent(id, party));
    }

    public static boolean contains(String name) {
        return getItemComponent(name, Party.ME) != null || getItemComponent(name, Party.THEM) != null;
    }

    public static boolean contains(int id) {
        return getItemComponent(id, Party.ME) != null || getItemComponent(id, Party.THEM) != null;
    }

    public static boolean contains(String name, Party party) {
        return getItemComponent(name, party) != null;
    }

    public static boolean contains(int id, Party party) {
        return getItemComponent(id, party) != null;
    }

    public static boolean containsAnyExcept(String name) {
        return Arrays.stream(getItemComponents()).anyMatch(comp -> !StringCommons.replaceColorTag(comp.getName()).equals(name));
    }

    public static boolean containsAnyExcept(int id) {
        return Arrays.stream(getItemComponents()).anyMatch(comp -> comp.getItemID() != id);
    }

    public static boolean containsAnyExcept(String name, Party party) {
        return Arrays.stream(getItemComponents(party)).anyMatch(comp -> !StringCommons.replaceColorTag(comp.getName()).equals(name));
    }

    public static boolean containsAnyExcept(int id, Party party) {
        return Arrays.stream(getItemComponents(party)).anyMatch(comp -> comp.getItemID() != id);
    }


    private static final String FIRST_SCREEN_TRADER_NAME_PREFIX = "Trading With: ";
    private static final InterfaceAddress FIRST_SCREEN_TRADER_NAME = new InterfaceAddress(FIRST_TRADE_WINDOW_GROUP, comp ->
            comp.getText().startsWith(FIRST_SCREEN_TRADER_NAME_PREFIX));

    private static final String SECOND_SCREEN_TRADER_NAME_PREFIX = "Trading with:";
    private static final InterfaceAddress SECOND_SCREEN_TRADER_NAME = new InterfaceAddress(SECOND_TRADE_WINDOW_GROUP, comp ->
            comp.getText().startsWith(SECOND_SCREEN_TRADER_NAME_PREFIX));

    public static String getTheirName() {
        InterfaceComponent traderNameComp = Trade.isOpen(Screen.FIRST) ? FIRST_SCREEN_TRADER_NAME.lookup() : SECOND_SCREEN_TRADER_NAME.lookup();
        String prefix = Trade.isOpen(Screen.FIRST) ? FIRST_SCREEN_TRADER_NAME_PREFIX : SECOND_SCREEN_TRADER_NAME_PREFIX;
        if (traderNameComp != null && traderNameComp.isVisible() && traderNameComp.exists()) {
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
}
