package api;

import infrastructure.PlatRepositoryMariadb;
import infrastructure.UtilisateurRepositoryMariadb;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import repositories.PlatRepositoryInterface;
import repositories.UtilisateurRepositoryInterface;

import java.io.InputStream;
import java.util.Properties;

/**
 * Classe principale de l'application
 * Gère la connexion à la base de données et l'injection de dépendances via CDI
 */
@ApplicationPath("/api")
@ApplicationScoped
public class PlatsUtilisateursApplication extends Application {

    /**
     * Charge les propriétés depuis config.properties
     * @return objet Properties avec les infos de connexion
     */
    private Properties loadConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("config.properties introuvable !");
                return props;
            }
            props.load(input);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return props;
    }

    /**
     * Méthode CDI produisant la connexion à la BD pour les plats
     */
    @Produces
    private PlatRepositoryInterface openPlatDbConnection() {
        PlatRepositoryMariadb db = null;
        try {
            Properties props = loadConfig();
            db = new PlatRepositoryMariadb(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.pwd")
            );
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return db;
    }

    /**
     * Méthode CDI fermant la connexion à la BD pour les plats
     */
    private void closePlatDbConnection(@Disposes PlatRepositoryInterface platRepo) {
        platRepo.close();
    }

    /**
     * Méthode CDI produisant la connexion à la BD pour les utilisateurs
     */
    @Produces
    private UtilisateurRepositoryInterface openUtilisateurDbConnection() {
        UtilisateurRepositoryMariadb db = null;
        try {
            Properties props = loadConfig();
            db = new UtilisateurRepositoryMariadb(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.pwd")
            );
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return db;
    }

    /**
     * Méthode CDI fermant la connexion à la BD pour les utilisateurs
     */
    private void closeUtilisateurDbConnection(@Disposes UtilisateurRepositoryInterface utilisateurRepo) {
        utilisateurRepo.close();
    }
}
