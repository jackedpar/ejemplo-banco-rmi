package com.bancormi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Juan on 04/04/2014.
 */
public class Conexion {
    final String HOST="localhost";//final crea una variable constante
    final String USER="root";
    final String PASSW="root123";
    final String DB="banco";
    private static Conexion ourInstance = new Conexion();
    private Connection conex;

    public static Conexion getInstance() {
        return ourInstance;
    }

    private Conexion() {
        try {

            Class.forName("com.mysql.jdbc.Driver");//carga el driver en tramp real
            conex = DriverManager.getConnection("jdbc:mysql://" + HOST + "/" + DB, USER, PASSW);
        }
        catch(Exception e)
        {
            System.out.println("El error es: "+e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection open()
    {
        return conex;
    }

    public void closeConex() {
        // TODO Auto-generated method stub
        try {
            conex.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
