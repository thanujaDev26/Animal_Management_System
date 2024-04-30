package db;

import java.io.IOException;

public class DBBackup {

    public static void backup() {
        String host = "localhost"; // MongoDB host
        int port = 27017; // MongoDB port
        String databaseName = "wild_life"; // Database name
        String backupPath = "./src/main/resources/db"; // Directory to store backup

        try {
            // Execute mongodump command
            ProcessBuilder pb = new ProcessBuilder("mongodump", "--host", host, "--port", String.valueOf(port), "--db", databaseName, "--out", backupPath);
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Backup completed successfully.");
            } else {
                System.out.println("Backup failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
