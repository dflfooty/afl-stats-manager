package net.afl.aflstats.db;

import org.flywaydb.core.Flyway;

public class Migrations {
    public static void main(String[] args) throws Exception {
        Flyway flyway = Flyway.configure().dataSource(System.getenv("JDBC_DATABASE_URL"),
                System.getenv("JDBC_DATABASE_USERNAME"), System.getenv("JDBC_DATABASE_PASSWORD")).load();
        flyway.migrate();
    }
}
