package com.example.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map;

/**
 * Service for retrieving task statistics using JDBC.
 */
@Service
public class TaskStatisticsJdbcService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TaskStatisticsJdbcService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Gets the count of tasks grouped by priority.
     * @return a list of maps containing priority and count
     */
    public List<Map<String, Object>> getTasksCountByPriority() {
        String sql = "SELECT priority, COUNT(*) as count FROM tasks GROUP BY priority ORDER BY priority";

        return jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> result = new HashMap<>();
                result.put("priority", rs.getString("priority"));
                result.put("count", rs.getLong("count"));
                return result;
            }
        });
    }
}