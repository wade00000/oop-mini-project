// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    public static String hashPassword(String password) {
        // Define a cost factor (work factor). Default is 10.
        int logRounds = 12;  // Increasing this value makes it more secure, but slower

        // Generate the salt
        String salt = BCrypt.gensalt(logRounds);

        // Hash the password with the salt
        return BCrypt.hashpw(password, salt);
    }

    public static boolean verifyPassword(String typed,String hashedPassword){
        return BCrypt.checkpw(typed,hashedPassword) ;
    }
}