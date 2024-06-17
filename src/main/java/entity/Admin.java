package entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data

@Entity("admin")
public class Admin {
    @Id
    private String uname;
    private String password;
    private String email;

    public Admin(String uname, String password, String email) {
        this.uname = uname;
        this.password = password;
        this.email = email;
    }
}
