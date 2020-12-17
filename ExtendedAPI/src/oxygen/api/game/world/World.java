package oxygen.api.game.world;

import oxygen.api.Client;
import oxygen.api.accessors.Interfaces;
import oxygen.api.commons.Random;
import oxygen.api.game.game.Game;
import oxygen.api.wrappers.InterfaceComponent;

public class World {

    public static int[] P2P_WORLDS = {302, 303, 304, 305, 306, 307, 309, 310, 311, 312, 313, 314, 315, 317, 318, 319, 320, 321, 322, 323, 325, 327, 328, 329, 330, 331, 332, 333, 334, 336, 337, 338, 339, 340, 3141, 342, 344, 346, 347, 348, 350, 351, 352, 354, 355, 356, 357, 358, 359, 360, 362, 365, 367, 368, 369, 370, 374, 375, 376, 377, 378, 386, 387, 388, 389, 390, 395, 421, 422, 424, 443, 444, 445, 446, 463, 464, 465, 466, 477, 478, 479, 480, 481, 482, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 505, 506, 507, 508, 509, 510, 511, 512, 513, 514, 515, 516, 517, 518, 519, 520, 521, 522, 523, 524, 525, 531, 532, 533, 534, 535};
    public static int[] F2P_WORLDS = {301, 308, 316, 326, 335, 371, 379, 380, 382, 383, 384, 393, 394, 397, 398, 399, 418, 425, 426, 430, 431, 433, 434, 435, 436, 437, 451, 452, 453, 454, 455, 456, 469, 470, 471, 472, 473, 483, 497, 498, 499, 500, 501};

    public static String username;
    public static String password;

    public static int getRandomF2PWorld() {
        return Random.nextInt(F2P_WORLDS.length);
    }

    public static int getRandomP2PWorld() {
        int randomWorld = Random.nextInt(P2P_WORLDS.length);
        System.out.println("New World to hop to is: " + P2P_WORLDS[randomWorld]);
        return P2P_WORLDS[randomWorld];
    }

    public static int getCurrentWorld() {

        int currentWorld = 0;
        if (Client.getGameState() >= 30) {

            InterfaceComponent friendsListCurrentWorldInterface = Interfaces.getComponent(429, 3);
            InterfaceComponent ignoreListCurrentWorldInterface = Interfaces.getComponent(432, 3);

            if (friendsListCurrentWorldInterface != null && friendsListCurrentWorldInterface.exists()) {
                String worldAsString = friendsListCurrentWorldInterface.getText();
                String[] worldAsStringSplit = splitStringEvery(worldAsString, 21);
                currentWorld = Integer.parseInt(worldAsStringSplit[1]);
                System.out.println("Current world is: " + currentWorld);

            } else if (ignoreListCurrentWorldInterface != null && ignoreListCurrentWorldInterface.exists()) {
                String worldAsString = ignoreListCurrentWorldInterface.getText();
                String[] worldAsStringSplit = splitStringEvery(worldAsString, 20);
                currentWorld = Integer.parseInt(worldAsStringSplit[1]);
                System.out.println("Current world is: " + currentWorld);
            }
            return currentWorld;
        }
        return -1;
    }

    public static String[] splitStringEvery(String s, int interval) {
        int arrayLength = (int) Math.ceil(((s.length() / (double) interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + interval);
            j += interval;
        } //Add the last bit
        result[lastIndex] = s.substring(j);

        return result;
    }

    public static void hopTo(int world) {

        if (isLoggedIn()) {
            Game.logout();
        }
        if (!isLoggedIn()) {
            username = Client.getLoginUsername();
            password = Client.getLoginPassword();
            Client.setWorld(world);
        }
        Client.login(username, password);
    }
    public static boolean isLoggedIn() {
        return Client.getGameState() >= 30;
    }
}
