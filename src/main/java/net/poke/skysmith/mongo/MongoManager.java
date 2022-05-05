package net.poke.skysmith.mongo;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import jdk.nashorn.internal.parser.JSONParser;
import net.poke.skysmith.utils.Item;
import net.poke.skysmith.utils.Stat;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MongoManager {
    private static MongoClient client;
    private static MongoDatabase database;
    public static MongoCollection<Document> collection;

    private static boolean connected;

    public void connect() {
        try {
            InputStream is = new FileInputStream("config.json");
            String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(jsonTxt);
            client = MongoClients.create(json.getString("uri"));
            database = client.getDatabase(json.getString("database"));
            collection = database.getCollection(json.getString("collection"));
            System.out.println(collection.countDocuments());
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = true;

        System.out.println("Successfully connected to Server Mongo Database!");
    }

    public boolean isConnected() {
        return connected;
    }

    public void setData(String id, String key, Object value) {
        Document query = new Document("id", id);
        Document found = collection.find(query).first();

        if (found == null) {
            Document update = new Document("id", id);
            update.append(key, value);

            collection.insertOne(update);
            return;
        }

        collection.updateOne(Filters.eq("id", id), Updates.set(key, value));
    }

    public void saveItem(Item item, String userId) {
        Document doc = new Document("_id", item.id);
        doc.append("name", item.name);
        doc.append("description", item.description);
        doc.append("rarity", item.rarity);
        doc.append("numberedLore", item.numberedLore);
        doc.append("pureLore", item.pureLore);
        Document stats = new Document();
        for (Stat stat : item.stats.keySet()) {
            stats.append(stat.name(), item.stats.get(stat));
        }
        doc.append("stats", stats);
        doc.append("owner", userId);
        if (collection.find(new Document("_id", item.id)).first() == null) {
            collection.insertOne(doc);
        } else {
            collection.updateOne(new Document("_id", item.id), doc);
        }
    }

    public Object getData(String id, String key) {
        Document query = new Document("id", id);
        Document found = collection.find(query).first();

        if (found != null)
            return found.get(key);

        return null;
    }

    public String getString(String id, String key) {
        return String.valueOf(getData(id, key));
    }

    public int getInt(String id, String key) {
        return Integer.parseInt(String.valueOf(getData(id, key)));
    }

    public boolean getBoolean(String id, String key) {
        return Boolean.parseBoolean(String.valueOf(getData(id, key)));
    }

    public List<Document> getAllDocuments() {
        MongoCollection<Document> col = collection;
        FindIterable<Document> docs = col.find();
        MongoCursor<Document> cursor = docs.iterator();

        List<Document> found = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                found.add(cursor.next());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return found;
    }

    public Document getDoc(String id) {
        Document query = new Document("_id", id);
        return collection.find(query).first();
    }

    public boolean remove(String id) {
        Document query = new Document("id", id);
        Document found = collection.find(query).first();

        if (found == null) return false;

        collection.deleteOne(found);
        return true;
    }
}
