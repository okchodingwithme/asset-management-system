package kh.gov.treasury.config;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import kh.gov.treasury.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
public class MySQLDataSource {

	@Value("${key.config.algorithm}")
    private String algorithm;	
	@Value("${key.config.keyPadding}")
	private String keyPadding;	
	@Value("${key.config.keyPass}")
	private String keyPass;	
	@Value("${key.config.keyIV}")
	private String keyIV;
	
	@Value("${mysql_db.config.url}")
	private String url;
	@Value("${mysql_db.config.username}")
	private String username;
	@Value("${mysql_db.config.pwd}")
	private String pwd;
	@Value("${mysql_db.config.driver}")
	private String driver;
	
	@Value("${mysql_db.config.minimumIdle}")
	private String minimumIdle;
	@Value("${mysql_db.config.maximumPoolSize}")
	private String maximumPoolSize;
	@Value("${mysql_db.config.connectionTimeout}")
	private String connectionTimeout;
	@Value("${mysql_db.config.idleTimeout}")
	private String idleTimeout;
	@Value("${mysql_db.config.maxLifetime}")
	private String maxLifetime;
	
	@Bean
	DataSource dataSource() {	
		HikariDataSource hikariDataSource= new HikariDataSource();
		try {
			HikariConfig hikariConfig = new HikariConfig();
		    hikariConfig.setJdbcUrl(url);
		    hikariConfig.setUsername(CryptoUtils.decrypt(username, keyPass, keyIV, algorithm, keyPadding));
		    hikariConfig.setPassword(CryptoUtils.decrypt(pwd, keyPass, keyIV, algorithm, keyPadding));
		    hikariConfig.setDriverClassName(driver);		    
		    hikariConfig.setMinimumIdle(Integer.valueOf(minimumIdle));
		    hikariConfig.setMaximumPoolSize(Integer.valueOf(maximumPoolSize));
		    hikariConfig.setConnectionTimeout(Integer.valueOf(connectionTimeout));
		    hikariConfig.setIdleTimeout(Integer.valueOf(idleTimeout));
		    hikariConfig.setMaxLifetime(Integer.valueOf(maxLifetime));		    
		    hikariDataSource = new HikariDataSource(hikariConfig);	    			
		} catch (Exception e) {
			log.error("Inside dataSource - Error Message={}", e.getMessage());
		}
		return hikariDataSource;
	}
}