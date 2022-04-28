package net.poke.skysmith.utils;

import com.uploadcare.upload.FileUploader;
import com.uploadcare.upload.UploadFailureException;
import com.uploadcare.upload.Uploader;
import net.poke.skysmith.Main;

import java.io.File;
import java.net.MalformedURLException;
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
        List<String> newLore = new ArrayList<String>();

        List<String> im = item.description;
        if (!im.isEmpty()) {
            List<String> oldLore = im;
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

    public static String uploadfileToUploadCare(File file) {
        String url = null;
        try {
            Uploader uploader = new FileUploader(Main.client, file);
            com.uploadcare.api.File uploadedFile = uploader.upload();
            String uploadedFileId = uploadedFile.getFileId();

            com.uploadcare.api.File requestedByIdFile = Main.client.getFile(uploadedFileId);
            url = requestedByIdFile.getUrl().toString();
        } catch (UploadFailureException e) {
            System.out.println("Upload failed: " + e.getMessage());
        }
        return url;
    }
}
