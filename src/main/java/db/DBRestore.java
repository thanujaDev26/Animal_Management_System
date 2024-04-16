package db;

import java.io.IOException;

public class DBRestore {
    public static void restore() {
        String host = "localhost"; // MongoDB host
        int port = 27017; // MongoDB port
        String databaseName = "wild_life"; // Database name
        String backupPath = "./src/main/resources/db/wild_life"; // Directory containing backup

        try {
            // Execute mongorestore command
            ProcessBuilder pb = new ProcessBuilder("mongorestore", "--host", host, "--port", String.valueOf(port), "--db", databaseName, backupPath);
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Restore completed successfully.");
            } else {
                System.out.println("Restore failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
