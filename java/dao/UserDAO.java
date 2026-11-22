
package dao;

import db.DBConnection;
import model.User;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UserDAO {

    private static final String HASH_PREFIX = "PBKDF2$";
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final int ITERATIONS = 210000;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;

    private static final SecureRandom RANDOM = new SecureRandom();

    // Map database record to User object
    private User mapUser(ResultSet rs) throws SQLException {

        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));

        return user;
    }

    // Generate a secure password hash
    private String hashPassword(String password)
            throws GeneralSecurityException {

        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);

        byte[] hash = generateHash(password, salt, ITERATIONS);

        return HASH_PREFIX
                + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    // Generate PBKDF2 hash
    private byte[] generateHash(String password, byte[] salt,
                                int iterations)
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

    // Verify password against stored hash or legacy password
    private boolean verifyPassword(String password, String storedPassword) {

        if (password == null || storedPassword == null) {
            return false;
        }

        if (!storedPassword.startsWith(HASH_PREFIX)) {

            return MessageDigest.isEqual(
                    password.getBytes(StandardCharsets.UTF_8),
                    storedPassword.getBytes(StandardCharsets.UTF_8)
            );
        }

        try {
            String[] parts = storedPassword.split("\\$");

            if (parts.length != 4 || !parts[0].equals("PBKDF2")) {
                return false;
            }

            int iterations = Integer.parseInt(parts[1]);

            if (iterations < 10000 || iterations > 1000000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] originalHash = Base64.getDecoder().decode(parts[3]);

            if (salt.length != SALT_LENGTH
                    || originalHash.length != KEY_LENGTH / 8) {
                return false;
            }

            byte[] newHash = generateHash(password, salt, iterations);

            return MessageDigest.isEqual(originalHash, newHash);

        } catch (GeneralSecurityException | IllegalArgumentException e) {
            System.err.println("Password verification failed.");
            return false;
        }
    }

    // Check whether a password contains a valid PBKDF2 hash
    private boolean isHashedPassword(String password) {

        if (password == null || !password.startsWith(HASH_PREFIX)) {
            return false;
        }

        try {
            String[] parts = password.split("\\$");

            if (parts.length != 4 || !parts[0].equals("PBKDF2")) {
                return false;
            }

            int iterations = Integer.parseInt(parts[1]);

            if (iterations < 10000 || iterations > 1000000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] hash = Base64.getDecoder().decode(parts[3]);

            return salt.length == SALT_LENGTH
                    && hash.length == KEY_LENGTH / 8;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Validate customer details
    private boolean isValidUserDetails(String name, String email) {

        if (name == null || email == null) {
            return false;
        }

        String cleanName = name.trim();
        String cleanEmail = email.trim();

        return !cleanName.isEmpty()
                && cleanName.length() <= 100
                && !cleanEmail.isEmpty()
                && cleanEmail.length() <= 100
                && cleanEmail.matches(
                        "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@"
                        + "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?"
                        + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$"
                );
    }

    // CREATE - Add User
    public boolean addUser(User user) {

        if (user == null
                || !isValidUserDetails(user.getName(), user.getEmail())
                || user.getPassword() == null
                || user.getPassword().length() < 8
                || user.getPassword().length() > 64) {
            return false;
        }

        String sql = "INSERT INTO users (name, email, password) "
                + "VALUES (?, ?, ?)";

        try {
            String storedPassword = hashPassword(user.getPassword());

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, user.getName().trim());
                ps.setString(2,
                        user.getEmail().trim().toLowerCase(Locale.ROOT));
                ps.setString(3, storedPassword);

                return ps.executeUpdate() > 0;
            }

        } catch (GeneralSecurityException | SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    // READ - Get All Users
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users ORDER BY user_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapUser(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }

        return users;
    }

    // READ - Get User By ID
    public User getUserById(int userId) {

        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapUser(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
        }

        return null;
    }

    // UPDATE - Update User
    public boolean updateUser(User user) {

        if (user == null
                || user.getUserId() <= 0
                || !isValidUserDetails(user.getName(), user.getEmail())
                || user.getPassword() == null
                || user.getPassword().isEmpty()) {
            return false;
        }

        String sql = "UPDATE users SET name = ?, email = ?, password = ? "
                + "WHERE user_id = ?";

        try {
            String storedPassword = user.getPassword();

            if (!isHashedPassword(storedPassword)) {

                if (storedPassword.length() < 8
                        || storedPassword.length() > 64) {
                    return false;
                }

                storedPassword = hashPassword(storedPassword);
            }

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, user.getName().trim());
                ps.setString(2,
                        user.getEmail().trim().toLowerCase(Locale.ROOT));
                ps.setString(3, storedPassword);
                ps.setInt(4, user.getUserId());

                return ps.executeUpdate() > 0;
            }

        } catch (GeneralSecurityException | SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    // DELETE - Delete User
    public boolean deleteUser(int userId) {

        if (userId <= 0) {
            return false;
        }

        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error deleting user. The user may have existing bookings: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // Upgrade legacy password after successful login
    private void upgradePassword(int userId, String oldPassword,
                                 String newPassword) {

        String sql = "UPDATE users SET password = ? "
                + "WHERE user_id = ? AND password = ?";

        try {
            String hashedPassword = hashPassword(newPassword);

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, hashedPassword);
                ps.setInt(2, userId);
                ps.setString(3, oldPassword);

                ps.executeUpdate();
            }

        } catch (GeneralSecurityException | SQLException e) {
            System.err.println(
                    "Could not upgrade existing password: " + e.getMessage()
            );
        }
    }

    // LOGIN - Find customer by email and verify password
    public User login(String email, String password) {

        if (email == null || password == null
                || email.trim().isEmpty() || password.isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase(Locale.ROOT));

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User user = mapUser(rs);
                    String storedPassword = user.getPassword();

                    if (verifyPassword(password, storedPassword)) {

                        if (!isHashedPassword(storedPassword)) {
                            upgradePassword(
                                    user.getUserId(),
                                    storedPassword,
                                    password
                            );
                        }

                        return user;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error during customer login: " + e.getMessage()
            );
        }

        return null;
    }

    // CHECK - Check whether email already exists
    public boolean emailExists(String email) {

        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase(Locale.ROOT));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error checking email: " + e.getMessage()
            );
            return false;
        }
    }
}
