import java.sql.SQLException;
import java.util.Scanner;


public class Controller {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/viktorgs_dbProsjekt";
        String name = "viktorgs_dbUser";
        String pass = "12345";
        Database db = new Database(url, name, pass);
        Scanner reader = new Scanner(System.in);

        int a = -1;
        System.out.println("Skriv inn et tall for å gjøre et valg: ");

        while (a < 1 || a > 5) {
            System.out.println("[1]: Registrere et apparat, en øvelse eller en treningsøkt med tilhørende data.");
            System.out.println("[2]: Få opp informasjon om et antall n sist gjennomførte treningsøkter med notater.");
            System.out.println("[3]: Se en resultatlogg for en øvelse i et gitt tidsintervall.");
            System.out.println("[4]: Lage en øvelsegruppe eller finne øvelser som er i samme gruppe");
            System.out.println("[5]: Se hvilke øvelser man kan utføre på et gitt apparat.");

            a = reader.nextInt();

            if ( a < 1 || a > 5) {
                System.out.println("Ugyldig nummer, gjør et nytt valg.");
            }
        }

            if (a == 1) {

                int b = -1;
                System.out.println("Hva vil du registrere?");

                while (b < 1 || b > 3) {
                    System.out.println("[1]: Treningsapparat.");
                    System.out.println("[2]: Treningsøvelse.");
                    System.out.println("[3]: Treningsøkt.");
                    b = reader.nextInt();

                    if (b < 1 || b > 3) {
                        System.out.println("Ugyldig nummer, gjør et nytt valg: ");
                    }
                }

                if (b == 1) {
                    System.out.println("Skriv inn navn på apparatet du ønsker å registrere: ");
                    String apparat = reader.next();
                    System.out.println("Gi en kort beskrivelse av apparatet: ");
                    String description = reader.next();
                    db.createApparat(apparat, description);
                }

                if (b == 2) {
                    System.out.println("Skriv inn id på øvelsen du ønsker å registrere: ");
                    int id = reader.nextInt();
                    System.out.println("Skriv inn navn på øvelsen: ");
                    String navn = reader.next();
                    db.createExercise(id, navn);
                }

                if (b == 3) {
                    System.out.println("[1]: Skriv inn id på dagboken du ønsker å registrere treningsøkten i: ");
                    int id = reader.nextInt();
                    System.out.println("[DDMMYY]: Skriv inn dato til treningsøkten: ");
                    String dato = reader.next();
                    System.out.println("[HHMM]: Skriv inn tidspunkt til treningsøkten: ");
                    String time = reader.next();
                    System.out.println("Skriv inn et kort notat til treningsøkten: ");
                    String note = reader.next();
                    System.out.println("Skriv inn et nummer fra 1-10 som beskriver formen din under økten: ");
                    int form = reader.nextInt();
                    System.out.println("Skriv inn et nummer fra 1-10 som beskriver prestasjonen din under økten: ");
                    int prestasjon = reader.nextInt();
                    db.createWorkout(id, dato, time, note, form, prestasjon);
                }
            }

            if (a == 2) {
                System.out.println("...");
            }

            if (a == 3) {
                System.out.println("...");
            }

            if (a == 4) {
                int b = -1;
                System.out.println("Hva vil du gjøre? ");

                while (b < 1 || b > 2) {
                    System.out.println("[1]: Registrere ny øvelsesgruppe. ");
                    System.out.println("[2]: Finne øvelser i samme gruppe. ");
                    b = reader.nextInt();

                    if (b < 1 || b > 2) {
                        System.out.println("Ugyldig nummer, gjør et nytt valg.");
                    }

                    if (b == 1) {
                        System.out.println("Skriv inn navn på øvelsesgruppen: ");
                        String navn = reader.next();
                        db.createExerciseGroup(navn);
                    }

                    if (b == 2) {
                        System.out.println("Skriv inn navn på øvelsesgruppen: ");
                        String navn = reader.next();
                        System.out.println(db.getExercisesByGroup(navn));
                    }
                }
            }
            if (a == 5) {
                System.out.println("Mangler");
            }
        reader.close();
    }
}
