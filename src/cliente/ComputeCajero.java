package cliente;


import com.bancormi.ComputeBanco;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Juan on 04/04/2014.
 */
public class ComputeCajero {
    public static void main(String[] args) {
        try {
            String name = "ComputeBanco";
            //Registry registry = LocateRegistry.getRegistry(args[0]);
            Registry registry = LocateRegistry.getRegistry(1099);
            ComputeBanco comp = (ComputeBanco) registry.lookup(name);
            String datosCliente = comp.getDatosCliente(1);
            System.out.println(datosCliente);
            int montoCuenta = comp.getMontoCuenta(1);
            System.out.println(montoCuenta + "");
        } catch (Exception e) {
            System.err.println("ComputeBanco exception:");
            e.printStackTrace();
        }

    }
}
