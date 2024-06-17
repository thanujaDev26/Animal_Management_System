import org.mindrot.jbcrypt.BCrypt;

public class Generator {
    public static void main(String[] args) {
        String password = "Thanuja20@";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        System.out.println(hashedPassword);
    }
}
