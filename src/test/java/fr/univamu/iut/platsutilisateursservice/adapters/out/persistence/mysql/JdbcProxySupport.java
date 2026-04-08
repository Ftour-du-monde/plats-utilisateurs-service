package fr.univamu.iut.platsutilisateursservice.adapters.out.persistence.mysql;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class JdbcProxySupport {

    private JdbcProxySupport() {
    }

    static DataSource dataSource(Connection connection) {
        return (DataSource) Proxy.newProxyInstance(
                DataSource.class.getClassLoader(),
                new Class[]{DataSource.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if ("getConnection".equals(name)) {
                        return connection;
                    }
                    if ("isWrapperFor".equals(name)) {
                        return false;
                    }
                    if ("unwrap".equals(name)) {
                        return null;
                    }
                    return defaultValue(method.getReturnType());
                }
        );
    }

    static Connection connection(Map<String, PreparedStatement> statementsBySql) {
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if ("prepareStatement".equals(name)) {
                        String sql = (String) args[0];
                        PreparedStatement statement = statementsBySql.get(sql);
                        if (statement == null) {
                            throw new SQLException("No statement stub configured for SQL: " + sql);
                        }
                        return statement;
                    }
                    if ("close".equals(name)) {
                        return null;
                    }
                    if ("isClosed".equals(name)) {
                        return false;
                    }
                    return defaultValue(method.getReturnType());
                }
        );
    }

    static PreparedStatement preparedStatement(StatementScript script) {
        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                new Class[]{PreparedStatement.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    switch (name) {
                        case "setString":
                        case "setLong":
                        case "setBigDecimal":
                            script.parameters.put((Integer) args[0], args[1]);
                            return null;
                        case "executeQuery":
                            if (script.executeQueryException != null) {
                                throw script.executeQueryException;
                            }
                            return script.queryResult == null ? resultSet(List.of()) : script.queryResult;
                        case "executeUpdate":
                            if (script.executeUpdateException != null) {
                                throw script.executeUpdateException;
                            }
                            return script.executeUpdateResult;
                        case "getGeneratedKeys":
                            return script.generatedKeys == null ? resultSet(List.of()) : script.generatedKeys;
                        case "close":
                            return null;
                        default:
                            return defaultValue(method.getReturnType());
                    }
                }
        );
    }

    static ResultSet resultSet(List<Map<Object, Object>> rows) {
        Cursor cursor = new Cursor();
        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class[]{ResultSet.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    switch (name) {
                        case "next":
                            cursor.index++;
                            return cursor.index < rows.size();
                        case "getLong":
                            return value(rows, cursor.index, args[0]) == null
                                    ? 0L
                                    : ((Number) value(rows, cursor.index, args[0])).longValue();
                        case "getString":
                            Object stringValue = value(rows, cursor.index, args[0]);
                            return stringValue == null ? null : String.valueOf(stringValue);
                        case "getBigDecimal":
                            Object decimalValue = value(rows, cursor.index, args[0]);
                            if (decimalValue == null) {
                                return null;
                            }
                            if (decimalValue instanceof BigDecimal) {
                                return decimalValue;
                            }
                            return new BigDecimal(String.valueOf(decimalValue));
                        case "close":
                            return null;
                        default:
                            return defaultValue(method.getReturnType());
                    }
                }
        );
    }

    static Map<Object, Object> row(Object... keyValues) {
        Map<Object, Object> row = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            row.put(keyValues[i], keyValues[i + 1]);
        }
        return row;
    }

    private static Object value(List<Map<Object, Object>> rows, int rowIndex, Object key) {
        if (rowIndex < 0 || rowIndex >= rows.size()) {
            return null;
        }
        return rows.get(rowIndex).get(key);
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (boolean.class.equals(returnType)) {
            return false;
        }
        if (byte.class.equals(returnType)) {
            return (byte) 0;
        }
        if (short.class.equals(returnType)) {
            return (short) 0;
        }
        if (int.class.equals(returnType)) {
            return 0;
        }
        if (long.class.equals(returnType)) {
            return 0L;
        }
        if (float.class.equals(returnType)) {
            return 0f;
        }
        if (double.class.equals(returnType)) {
            return 0d;
        }
        if (char.class.equals(returnType)) {
            return '\0';
        }
        return null;
    }

    static final class StatementScript {
        final Map<Integer, Object> parameters = new HashMap<>();
        SQLException executeQueryException;
        SQLException executeUpdateException;
        int executeUpdateResult = 1;
        ResultSet queryResult;
        ResultSet generatedKeys;
    }

    private static final class Cursor {
        private int index = -1;
    }
}
