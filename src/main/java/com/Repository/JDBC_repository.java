package com.Repository;

import com.Model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class JDBC_repository<T> implements CrudRepo<T> {

    public JDBC_repository(String url){
        setUrl(url);
    }

    static  String url;
    static final String username = "root";
    static final String password = "amaliablidar";

    public static void main(String[] args) {
        JDBC_repository connection = new JDBC_repository(url) {
            @Override
            public Object findOne(int var1) {
                return null;
            }

            @Override
            public ArrayList findAll() {
                return null;
            }

            @Override
            public Object save(Object var1) {
                return null;
            }

            @Override
            public Object delete(int var1) {
                return null;
            }

            @Override
            public Object update(Object var1) {
                return null;
            }
        };
        connection.createConnection();
    }

    Connection createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(url, username, password);

            return connect;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JDBC_repository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(JDBC_repository.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return null;
    }
    public void setUrl(String url1)
    {
        this.url=url1;
    }

}
