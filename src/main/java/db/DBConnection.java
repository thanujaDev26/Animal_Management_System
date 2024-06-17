package db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class DBConnection {

    private static final String DATABASE_NAME = "wild_life";
    private static MongoClient mongoClient;
    private static Datastore datastore;
    private static DBConnection dbconnection;

    private DBConnection(){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        datastore = Morphia.createDatastore(mongoClient, DATABASE_NAME);

        // Register shutdown hook to close MongoClient when the application exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (mongoClient != null) mongoClient.close();
        }));
    }

    public static DBConnection getInstance() {
        return (dbconnection == null) ? dbconnection = new DBConnection() : dbconnection;
    }

    public Datastore getDatastore() {
        return datastore;
    }
}
