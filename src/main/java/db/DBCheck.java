package db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class DBCheck {
    public static boolean check() {
        // MongoDB connection string
        String connectionString = "mongodb://localhost:27017";
        boolean check = false;

        // Connect to MongoDB server
        try (MongoClient mongoClient = new MongoClient(new MongoClientURI(connectionString))) {
            // Check if the database exists
            boolean exists = mongoClient.getDatabase("wild_life").listCollectionNames().iterator().hasNext();
            if (exists) {
                System.out.println("Database exists!");
                check = false;
            } else {
                System.out.println("Database does not exist!");
                check = true;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return check;
    }
}
