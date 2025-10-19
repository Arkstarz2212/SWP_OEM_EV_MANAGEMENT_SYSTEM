package G1swp.proj.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConnectionConfig implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionConfig.class);

    private final DataSource dataSource;

    public DatabaseConnectionConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            logger.info("✓ Successfully connected to the database!");
            logger.info("Database URL: {}", dataSource.getConnection().getMetaData().getURL());
        } catch (Exception e) {
            logger.error("✗ Failed to connect to the database!", e);
            logger.error("Error message: {}", e.getMessage());
        }
    }
}

