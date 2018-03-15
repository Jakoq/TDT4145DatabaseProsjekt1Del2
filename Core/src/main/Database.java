import java.sql.*;

public class Database {

    private String db_URL;
    private String db_user;
    private String db_pw;

    // Sjekker om vi har tilkobling til databasen
    public Database(String address, String username, String password) {
        db_URL = address;
        db_user = username;
        db_pw = password;

        try (Connection connection = getConnection()) {
            System.out.println("Connection established!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Returnerer en tilkobling til databsen
    private Connection getConnection() throws SQLException {
        Connection connection = null;
        try {

            connection = DriverManager.getConnection(db_URL, db_user, db_pw);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    public void createApparat(String name, String description) throws SQLException {

        String updateString = "INSERT INTO Apparat (Navn, Beskrivelse) VALUES(?.?)";


        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(updateString)) {

            statement.setString(1, name);
            statement.setString(2, description);
            statement.executeUpdate();
            System.out.println("Insertion complete.");

        } catch (SQLException e) {
            throw e;
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/viktorgs_dbProsjekt";
        String name = "viktorgs_dbUser";
        String pass = "12345";

        System.out.println("Her");
        Database db = new Database(url, name, pass);
        System.out.println("Hurra");
    }

}
