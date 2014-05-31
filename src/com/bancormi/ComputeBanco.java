package com.bancormi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Juan on 04/04/2014.
 * Microsoft Windows:
 *
 * cd C:\Users\Juan\IdeaProjects\Banco RMI\src
 * javac com\bancormi\ComputeBanco.java
 * jar cvf compute.jar com\bancormi\compute\*.class
 * cd C:\Users\Juan\IdeaProjects\Banco RMI\out\production\Banco RMI
 * rmiregistry
 */
public interface ComputeBanco extends Remote {
    String getDatosCliente(int idCliente) throws RemoteException, SQLException;

    String getCuentasCliente(int idCliente) throws RemoteException, SQLException;

    boolean doTransaccion(int idCuentaOrigen, int idCuentaDestino, int cantidad) throws RemoteException, SQLException;

    List<String> getHistorialTransacciones() throws RemoteException, SQLException;

    int getMontoCuenta(int idCuenta) throws RemoteException, SQLException;
}
