package storage.postgresql;

import java.sql.*;

public class Connector {
    private String url;
    private String user;
    private String password;
    private Connection connection;

    public Connector(Builder builder) {
        this.url = builder.getUrl();
        this.user = builder.getUser();
        this.password = builder.getPassword();
    }

    public void getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Connection OK");
            } else {
                System.out.println("Connection Failed");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet executeStatement(String command) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);
            statement.close();
            return resultSet;
        } catch (SQLException ex) {
            return null;
        }
    }

    public static class Builder {
        private String url;
        private String user;
        private String password;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setUser(String user) {
            this.user = user;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getUrl() {
            return url;
        }


        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }
    }
}
