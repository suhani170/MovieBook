
package dao;

import db.DBConnection;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class AdminDAO {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 210000;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;

    private static final SecureRandom RANDOM = new SecureRandom();

    public boolean login(String username, String password) {

        if (username == null || password == null
                || username.trim().isEmpty() || password.isEmpty()) {
            return false;
        }

        String sql = "SELECT admin_id, password FROM admins "
                + "WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return false;
                }

                int adminId = rs.getInt("admin_id");
                String storedPassword = rs.getString("password");

                if (storedPassword == null) {
                    return false;
                }

                if (isHashedPassword(storedPassword)) {
                    return verifyPassword(password, storedPassword);
                }

                // Verify an old plain-text password
                if (!MessageDigest.isEqual(
                        password.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                        storedPassword.getBytes(java.nio.charset.StandardCharsets.UTF_8))) {
                    return false;
                }

                // Upgrade the old password to PBKDF2
                String newHash = hashPassword(password);

                String updateSQL = "UPDATE admins SET password = ? "
                        + "WHERE admin_id = ? AND password = ?";

                try (PreparedStatement updatePS =
                        con.prepareStatement(updateSQL)) {

                    updatePS.setString(1, newHash);
                    updatePS.setInt(2, adminId);
                    updatePS.setString(3, storedPassword);

                    updatePS.executeUpdate();
                }

                return true;
            }

        } catch (SQLException | GeneralSecurityException e) {
            System.err.println("Admin login failed: " + e.getMessage());
            return false;
        }
    }

    private String hashPassword(String password)
            throws GeneralSecurityException {

        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);

        byte[] hash = generateHash(password, salt, ITERATIONS);

        return "PBKDF2$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    private boolean verifyPassword(String password, String storedHash)
            throws GeneralSecurityException {

        try {
            String[] parts = storedHash.split("\\$");

            if (parts.length != 4 || !parts[0].equals("PBKDF2")) {
                return false;
            }

            int iterations = Integer.parseInt(parts[1]);

            if (iterations < 10000 || iterations > 1000000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);

            if (salt.length != SALT_LENGTH
                    || expectedHash.length != KEY_LENGTH / 8) {
                return false;
            }

            byte[] actualHash = generateHash(password, salt, iterations);

            return MessageDigest.isEqual(expectedHash, actualHash);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private byte[] generateHash(String password, byte[] salt, int iterations)
            throws GeneralSecurityException {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                KEY_LENGTH
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(ALGORITHM);

            return factory.generateSecret(spec).getEncoded();

        } finally {
            spec.clearPassword();
        }
    }

    private boolean isHashedPassword(String password) {
        return password.startsWith("PBKDF2$");
    }
}
