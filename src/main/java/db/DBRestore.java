package db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DBRestore {
    public static void restore() {
        String host = "localhost"; // MongoDB host
        int port = 27017; // MongoDB port
        String databaseName = "wild_life"; // Database name
        String backupPath = "./src/main/resources/db/wild_life"; // Directory containing backup

        if(!DBCheck.check()){
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
        }else{
            try {
                String command = String.format("mongorestore --db %s %s/%s", databaseName, backupPath, databaseName);
                Process process = Runtime.getRuntime().exec(command);

                // Read the output from the command
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                process.waitFor();
                System.out.println("Restore completed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
