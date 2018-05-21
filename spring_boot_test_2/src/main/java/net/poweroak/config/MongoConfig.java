package net.poweroak.config;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

import net.poweroak.dao.impl.BaseDaoImpl;

@Configuration
@ComponentScan({"net.poweroak.config"})
@EnableMongoRepositories(repositoryBaseClass=BaseDaoImpl.class,basePackages="net.poweroak.dao")
public class MongoConfig {
	@Resource
	private MongoProperties mongoProperties;
	@Bean
	public MongoDbFactory mongoDbFactory() {
		return new SimpleMongoDbFactory(new MongoClient(mongoProperties.getHost(),mongoProperties.getPort()),mongoProperties.getDatabase());
	}
}
