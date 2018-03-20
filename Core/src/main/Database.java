

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Database {

    private String db_URL;
    private String db_user;
    private String db_pw;
    private Connection connection;

    // Sjekker om vi har tilkobling til databasen
    public Database(String address, String username, String password) {
        db_URL = address;
        db_user = username;
        db_pw = password;

        try {
            Connection connection = getConnection();
            this.connection = connection;
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

    // Legger inn et apparat i databasen. Skriv inn navn (hovednøkkel) og beskrivelse.
    public void createApparat(String name, String description) throws SQLException {

        String updateString = "INSERT INTO Apparat (Navn, Beskrivelse) VALUES(?,?)";


        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(updateString)) {

            statement.setString(1, name);
            statement.setString(2, description);
            statement.executeUpdate();
            System.out.println("Apparatet ble registrert.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Legger inn en øvelse i databasen. Skriv inn id (hovednøkkel) og navn.
    public void createExercise(int id, String name) throws SQLException {

        String updateString = "INSERT INTO Øvelse (ID, Navn) VALUES(?,?)";

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(updateString)) {

            statement.setInt(1, id);
            statement.setString(2, name);
            statement.executeUpdate();
            System.out.println("Øvelsen ble registrert.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Legger inn en treningsøkt  databasen.
    // Skriv inn id (hovednøkkel) dato, tid, beskrivelse, form og prestasjon.
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
            e.printStackTrace();
        }
    }

    // Legger inn en treningsgruppe i databasen. Skriv inn øvelsestype (hovednøkkel)
    public void createExerciseGroup(String exerciseType) {

        String updateString = "INSERT INTO Øvelsesgruppe (Øvelsestype) VALUES (?)";

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(updateString)) {

            statement.setString(1, exerciseType);
            statement.executeUpdate();
            System.out.println("Øvelsesgruppe registrert.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Henter ut øvelser basert på øvelsestype.
    public ArrayList<String> getExercisesByGroup(String exerciseGroup) throws SQLException {
        String query = "SELECT Øvelse.Navn FROM " +
                "(Øvelse JOIN ØvelseIGruppe ON Øvelse.ID = ØvelseIGruppe.ØvelseID) " +
                "JOIN Øvelsesgruppe ON ØvelseIGruppe.Øvelsestype = Øvelsesgruppe.Øvelsestype " +
                "WHERE Øvelsesgruppe.Øvelsestype = ?";
        ArrayList<String> exercises = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, exerciseGroup);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                exercises.add(rs.getString("Navn"));
            }
        } catch (SQLException e) {
            throw e;
        }
        return exercises;
    }


    // Henter ut antall øvelser per apparat.
    public HashMap<String, Integer> excercisesPerEquipment() throws SQLException {
        String apparatList = "SELECT Navn FROM Apparat";
        HashMap<String, Integer> epe = new HashMap<>(); //epe = excercise per equipment
        PreparedStatement statement1 = this.connection.prepareStatement(apparatList);
        ResultSet rs1 = statement1.executeQuery();
        while(rs1.next())
            epe.put(rs1.getString("Navn"), 0);

        for (String apparat:epe.keySet()) {
            String query = "SELECT count(*)\n" +
                    "FROM (Apparat as A) JOIN (ApparatØvelse as AØ) on A.Navn=AØ.Apparat_navn where A.Navn = ?";
            PreparedStatement s2 = this.connection.prepareStatement(query);
            s2.setString(1,apparat);
            ResultSet rs2 = s2.executeQuery();
            while (rs2.next()) {
                epe.put(apparat, rs2.getInt(1));
            }
        }
        return epe;
    }

    public ArrayList<String> getNLastWorkouts(int n){
        ArrayList<String> workouts = new ArrayList<String>();
        String query = "SELECT Notat, Dato " +
                "FROM Treningsøkt AS T" +
                " ORDER BY Dato DESC limit ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,n);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                String dato = rs.getString("Dato");
                String notat = rs.getString("Notat");
                workouts.add(dato + ": " + notat);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return workouts;
    }

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/viktorgs_dbProsjekt";
        String name = "viktorgs_dbUser";
        String pass = "12345";

        System.out.println("Her");
        Database db = new Database(url, name, pass);
        System.out.println("Hurra");
        System.out.println(db.excercisesPerEquipment());


        // db.createApparat("Nedtrekk", "Triceps"); Funket
        // db.createExercise(1, "Kneløft"); Funket
        // db.createWorkout(1, "150318", "1445", "Fantastisk", 10, 10); Funket
        // db.createExerciseGroup("Bevegelse"); Funket
        // ArrayList<String> list = db.getExercisesByGroup("styrke"); Funket
        // ArrayList<String> list = db.getExercisesByGroup("bevegelse"); Funket
    }
}


