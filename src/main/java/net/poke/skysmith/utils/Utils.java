package net.poke.skysmith.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static Boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void setLoreLine(int lineNumber, Item item, String builder) {
        String loreToBeSet = builder.trim();
        ArrayList<String> newLore = new ArrayList<String>();

        ArrayList<String> im = item.description;
        if (!im.isEmpty()) {
            ArrayList<String> oldLore = im;
            try {
                oldLore.set(lineNumber, loreToBeSet); // ERROR WILL BE CAUSED IF BIGGER
                newLore = oldLore;
            } catch (IndexOutOfBoundsException e) {
                // Fill new lore with old stuff
                newLore.addAll(oldLore);
                for (int i = oldLore.size() - 1; i < lineNumber; i++) { // Expand new lore to proper size
                    newLore.add("");
                }
                newLore.set(lineNumber, loreToBeSet);
            }
        } else { // Item has no lore
            for (int i = 0; i <= lineNumber; i++) {
                newLore.add("");
            }
            newLore.set(lineNumber, loreToBeSet);
        }
        item.description = newLore;
    }
}
