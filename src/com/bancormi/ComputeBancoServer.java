package com.bancormi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComputeBancoServer implements ComputeBanco{
    public ComputeBancoServer() {
        super();
    }

    public static void main(String[] args) {
        try {
            String name = "ComputeBanco";
            ComputeBanco bancoServer = new ComputeBancoServer();
            ComputeBanco stub =
                    (ComputeBanco) UnicastRemoteObject.exportObject(bancoServer, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ComputeBancoServer bound");
        } catch (Exception e) {
            System.err.println("ComputeBancoServer exception:");
            e.printStackTrace();
        }
    }

    @Override
    public String getDatosCliente(int idCliente) throws RemoteException, SQLException {
        String datosCliente =  "Datos del cliente: ";
        String sql= "SELECT * FROM cliente WHERE id = " + idCliente;
        ResultSet resultSet = Conexion.getInstance().open().createStatement().executeQuery(sql);
        if(resultSet.next()){
            return datosCliente + resultSet.getString("nombre")
                    + resultSet.getString("rfc")
                    + resultSet.getString("direccion")
                    + resultSet.getInt("telefono");
        }
        return "No encontrado";
    }

    @Override
    public String getCuentasCliente(int idCliente) throws RemoteException, SQLException {
        String datosCliente =  "Cuentas del cliente: ";
        String sql= "SELECT * FROM cuenta WHERE idCliente =" + idCliente;
        ResultSet resultSet = Conexion.getInstance().open().createStatement().executeQuery(sql);
        while(resultSet.next()){
            return datosCliente + resultSet.getString("nombre")
                    + resultSet.getString("rfc")
                    + resultSet.getString("direccion")
                    + resultSet.getInt("telefono");
        }
        return "No encontrado";
    }

    @Override
    public boolean doTransaccion(int idCuentaOrigen, int idCuentaDestino, int cantidad) throws RemoteException, SQLException {
        String errorMsg = "";
        if(!existeValor("cuenta", "id", idCuentaOrigen)){ errorMsg += "idCuentaOrigen no existe \n"; }
        else if(!existeValor("cuenta", "id", idCuentaDestino)){ errorMsg += "idCuentaDestino no existe \n"; }
        else {
            int restaMonto = getMontoCuenta(idCuentaOrigen) - cantidad;
            int sumaMonto = getMontoCuenta(idCuentaDestino) + cantidad;
            String sql = "UPDATE cuenta SET monto = " + restaMonto + " WHERE numCuenta = " + idCuentaOrigen;
            Conexion.getInstance().open().createStatement().executeUpdate(sql);

            sql = "UPDATE cuenta SET monto = " + sumaMonto + " WHERE numCuenta = " + idCuentaDestino;
            Conexion.getInstance().open().createStatement().executeUpdate(sql);

            sql = "UPDATE cuenta SET monto = " + restaMonto + " WHERE numCuenta = " + idCuentaOrigen;
            Conexion.getInstance().open().createStatement().executeUpdate(sql);

            // INSERT INTO cuenta ( numCuenta , monto, idCliente ) VALUES(1876543332, 2000100, 1);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            int numTransaccion = idCuentaOrigen + idCuentaDestino + (int)Math.random()*1000;
            sql = "INSERT INTO transaccion (numTransaccion, idCuentaOrigen, idCuentaDestino, monto, fecha) " +
                    " VALUES("+ numTransaccion + ", " + idCuentaOrigen + ", " + idCuentaDestino + ", " + cantidad + ", " +
                    dateFormat.format(date) + ")";
            Conexion.getInstance().open().createStatement().executeUpdate(sql);

            return true;
        }
        System.out.println(errorMsg);
        return false;
    }

    private boolean existeValor(String tabla, String columna, int valor) throws SQLException {
        String sql= "SELECT * FROM "+ tabla +" WHERE " + columna + " = " + valor;
        ResultSet resultSet = Conexion.getInstance().open().createStatement().executeQuery(sql);
        if(resultSet.next()){ return true; }
        return false;
    }

    @Override
    public List<String> getHistorialTransacciones() throws RemoteException, SQLException {
        List<String> historial = new ArrayList<String>();

        String sql= "SELECT * FROM transaccion";
        ResultSet resultSet = Conexion.getInstance().open().createStatement().executeQuery(sql);
        while(resultSet.next()){
            historial.add(resultSet.getInt("numTransaccion") + resultSet.getInt("idCuentaOrigen") + resultSet.getInt("idCuentaDestino") +
                    resultSet.getInt("monto") + resultSet.getString("fecha") + "");
        }
        return historial;
    }

    @Override
    public int getMontoCuenta(int idCuenta) throws RemoteException, SQLException {
        String sql= "SELECT monto FROM cuenta WHERE id = " + idCuenta;
        ResultSet resultSet = Conexion.getInstance().open().createStatement().executeQuery(sql);
        if(resultSet.next()){ return resultSet.getInt("monto"); }
        System.out.println("La cuenta con el id " + idCuenta + " no existe");
        return 0;
    }
}
