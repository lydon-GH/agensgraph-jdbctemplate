//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.agensgraph.config;

import com.zaxxer.hikari.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.*;
import org.springframework.jdbc.core.*;

import javax.sql.*;

@Configuration
public class HikariDataSourceConfig {
    @Autowired
    private Environment env;

    public HikariDataSourceConfig() {
    }

    /**
     * @return
     */
    @Bean(
            name = {"outerDataSource"}
    )
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(this.env.getProperty("agens.outer.datasource.driverClassName"));
        dataSource.setJdbcUrl(this.env.getProperty("agens.outer.datasource.url"));
        dataSource.setUsername(this.env.getProperty("agens.outer.datasource.username"));
        dataSource.setPassword(this.env.getProperty("agens.outer.datasource.password"));
        dataSource.setConnectionTestQuery("SELECT 1 as test");
        String graphPath = this.env.getProperty("agens.outer.datasource.graph_path");
        dataSource.setSchema(graphPath);
        dataSource.setConnectionInitSql("set client_encoding='utf8'; SET GRAPH_PATH=" + graphPath + "; set statement_timeout to " + this.env.getProperty("agens.config.query_timeout") + ";");
        dataSource.setMinimumIdle(5);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("agensHikariCP");
        dataSource.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        dataSource.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
        dataSource.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        dataSource.addDataSourceProperty("dataSource.useServerPrepStmts", "true");
        System.out.println("<config> agens.datasource.url = " + this.env.getProperty("agens.outer.datasource.url"));
        System.out.println("<config> agens.datasource.schema = " + graphPath);
        return dataSource;
    }

    @Bean(
            name = {"outerJdbcTemplate"}
    )
    public JdbcTemplate outerJdbcTemplate(@Qualifier("outerDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
