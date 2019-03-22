package com.sse.attemper.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class DataSourceProperties {

	private HikariDataSource first;
	private HikariDataSource second;

	public HikariDataSource getFirst() {
		return first;
	}

    public DataSource getSecond(){
        return second;
    }

    public void setFirst(HikariDataSource first) {
		this.first = first;
	}
    
    public void setSecond(HikariDataSource second) {
		this.second = second;
	}
}
