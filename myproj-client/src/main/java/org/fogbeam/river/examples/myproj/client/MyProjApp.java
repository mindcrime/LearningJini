package org.fogbeam.river.examples.myproj.client;

import java.util.Scanner;

import org.fogbeam.river.examples.myproj.api.MyProjInterface;

import com.sun.jini.start.LifeCycle;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationProvider;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.security.ProxyPreparer;

public class MyProjApp 
{
    private static final String MODULE=MyProjApp.class.getPackage().getName();
    
    public MyProjApp( final String[] args, LifeCycle lc ) 
    {
        main( args );
    }
    
    public static synchronized void main(String[] args) 
    {
        try 
        {
            // Get the config
            Configuration config = ConfigurationProvider.getInstance(args);
            // From the config, get the ServiceDiscoveryManager
            ServiceDiscoveryManager sdm=
                    (ServiceDiscoveryManager) 
                    config.getEntry(MODULE, "sdm", ServiceDiscoveryManager.class);

            // We'll also need a proxy preparer.
            ProxyPreparer preparer=(ProxyPreparer) config.getEntry(MODULE, 
                    "greeterPreparer", ProxyPreparer.class);

            // While the sdm is finding registrars, let's ask the user for their
            // name.
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter requested objId:");
            int objId = Integer.parseInt( in.nextLine() );
            
            // Query the sdm for Greeter services.
            ServiceTemplate template=new ServiceTemplate(
                    null,
                    new Class[] { MyProjInterface.class },
                    new Entry[0]
            );
            
            ServiceItem[] serviceItems=sdm.lookup(template, 5, null);
            
            if (serviceItems.length==0) 
            {
                System.out.println("We didn't find any MyProj services.");
                System.exit(0);
            }
            
            // Pick a service item
            ServiceItem chosen=serviceItems[0];
            
            // Prepare the proxy.
            MyProjInterface myProj = (MyProjInterface) preparer.prepareProxy(chosen.service);
            
            // Make the call
            String message = myProj.getName( objId );
            
            // Print the result
            System.out.println("Located object [" + message + "]");
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        } 
        finally 
        {
            System.exit(0);
        }
    }    
}
