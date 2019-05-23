package com.github.attemper.config.base.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class DataSourceProperties {

	private DataSource first;
	private DataSource second;

	public DataSource getFirst() {
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
