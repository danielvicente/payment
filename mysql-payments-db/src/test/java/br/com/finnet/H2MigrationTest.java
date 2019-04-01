package br.com.finnet;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class H2MigrationTest {

    @Test
    public void runMigrationsOnH2() {
        JdbcDataSource datasource = new JdbcDataSource();
        datasource.setURL("jdbc:h2:mem:stage_domain_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false;MODE=MYSQL");
        datasource.setUser("sa");
        datasource.setPassword("");

        Flyway flyway = new Flyway();
        flyway.setDataSource(datasource);
        flyway.setLocations("classpath:sql");

        Map<String, String> placeholders = new HashMap<>();

        placeholders.put("onlyLocalStart", "/*");
        placeholders.put("onlyLocalEnd", "*/");
        placeholders.put("onlyMySQLStart", "/*");
        placeholders.put("onlyMySQLEnd", "*/");
        placeholders.put("onlyH2Start", "");
        placeholders.put("onlyH2End", "");
        placeholders.put("json.type", "text");

        flyway.setPlaceholders(placeholders);

        flyway.migrate();
    }
}