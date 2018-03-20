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

    // Legger inn et apparat i databasen.
    public void createApparat(String name, String description) throws SQLException {

        String updateString = "INSERT INTO Apparat (Navn, Beskrivelse) VALUES(?,?)";


        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(updateString)) {

            statement.setString(1, name);
            statement.setString(2, description);
            statement.executeUpdate();
            System.out.println("Insertion complete.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Legger inn en øvelse i databasen.
    public void createExercise(int id, String name) throws SQLException {

        String updateString = "INSERT INTO Øvelse (ID, Navn) VALUES(?,?)";

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(updateString)) {

            statement.setInt(1, id);
            statement.setString(2, name);
            statement.executeUpdate();
            System.out.println("Insertion complete.");

        } catch (SQLException e) {
            throw e;
        }
    }

    public void createWorkout(int dagbokID,
                              String date,
                              String time,
                              String note,
                              int form,
                              int prestasjon)
            throws SQLException {

        String updateString = "INSERT INTO Treningsøkt (DagbokID, Dato, Tidspunkt, Notat, Form, Prestasjon) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(updateString)) {

            statement.setInt(1, dagbokID);
            statement.setString(2, date);
            statement.setString(3, time);
            statement.setString(4, note);
            statement.setInt(5, form);
            statement.setInt(6, prestasjon);
            statement.executeUpdate();
            System.out.println("Insertion complete.");

        } catch (SQLException e) {
            throw e;
        }
    }

    public int equipment_in_use() throws SQLException {
        String updateString = "SELECT count(*)\n" +
                "FROM (Apparat as A) JOIN (ApparatØvelse as AØ) on A.Navn=AØ.Apparat_navn";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateString)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }

        }
        throw new SQLException();
    }





    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/viktorgs_dbProsjekt";
        String name = "viktorgs_dbUser";
        String pass = "12345";


        System.out.println("Her");
        Database db = new Database(url, name, pass);
        System.out.println("Hurra");
        System.out.println(db.equipment_in_use());

        // db.createApparat("Benk", "Massive gains"); Funket
        // db.createExercise(1, "Kneløft"); Funket
        // db.createWorkout(1, "150318", "1445", "Fantastisk", 10, 10); Funket
    }
}

