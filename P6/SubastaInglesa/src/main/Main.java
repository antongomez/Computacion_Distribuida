package main;

import jade.core.ProfileImpl;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

/**
 *
 * @author Anton Gomez
 */
public class Main {

    public static void main(String[] args) throws StaleProxyException, ControllerException {
        // Lanzamos o contenedor principal
        AgentContainer mc = lanzarContenedorPrincipal();

        // Lanzamos os axentes
        AgentController axenteVendedor = mc.createNewAgent("Vendedor", "vendedor.Vendedor", new Object[]{});
        axenteVendedor.start();
        
        AgentController axenteComprador1 = mc.createNewAgent("Comprador1", "comprador.Comprador", new Object[]{});
        axenteComprador1.start();
        
        AgentController axenteComprador2 = mc.createNewAgent("Comprador2", "comprador.Comprador", new Object[]{});
        axenteComprador2.start();
    }

    private static AgentContainer lanzarContenedorPrincipal() throws ControllerException {
        AgentContainer mc;

        Properties p = new ExtendedProperties();
        p.setProperty("gui", "true");
        ProfileImpl pc = new ProfileImpl(p);
        mc = jade.core.Runtime.instance().createMainContainer(pc);
        mc.start();

        return mc;
    }

}
