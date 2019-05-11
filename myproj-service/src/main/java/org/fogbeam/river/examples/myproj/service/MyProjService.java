package org.fogbeam.river.examples.myproj.service;

import java.io.IOException;
import java.rmi.server.ExportException;

import org.fogbeam.river.examples.myproj.api.MyProjInterface;

import com.sun.jini.config.Config;
import com.sun.jini.start.LifeCycle;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationException;
import net.jini.config.ConfigurationProvider;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceID;
import net.jini.discovery.DiscoveryManagement;
import net.jini.export.Exporter;
import net.jini.lookup.JoinManager;
import net.jini.lookup.ServiceIDListener;
import net.jini.security.ProxyPreparer;

public class MyProjService implements MyProjInterface, ServiceIDListener
{
	private static final String MYPROJ = "org.fogbeam.river.examples.hello.myproj";
	
    Configuration config = null;
    MyProjInterface myProxy = null;
    Exporter exporter = null;
    ProxyPreparer registrarPreparer = null;
    JoinManager joinManager = null;
    DiscoveryManagement discoveryManager = null;
    Entry[] attributes = null;
    ServiceID sid = null;
    
    
    public MyProjService( String[] args, final LifeCycle lc ) throws ConfigurationException, ExportException, IOException
    {
    	
        config = ConfigurationProvider.getInstance(args);

        // Get the exporter and create our proxy.
        exporter = (Exporter) Config.getNonNullEntry(config, MYPROJ, "exporter", Exporter.class);
        myProxy = (MyProjInterface) exporter.export(this);    	
    	
        /* 
        The "discovery manager" looks after finding one or more service 
        registrars for the join manager to use.
        */
       discoveryManager
               = (DiscoveryManagement) config.getEntry(MYPROJ, "discoveryManager",
                       DiscoveryManagement.class);
       attributes = (Entry[]) config.getEntry(MYPROJ, "attributes", Entry[].class);

       /*
        Publish it using the join manager.
        The "join manager" takes care of registering our service with any/all 
        service registrars that appear in the workgroup.
       
        We don't have to do anything with it - just creating it starts the join process.
        */
       joinManager = new JoinManager(myProxy, attributes, this, discoveryManager, null, config);
       System.out.println("MyProj service has been started successfully.");    	
    	
    }
    
    
	public String getName(int objId) throws IOException 
	{
		return "Object with id: " + objId;
	}

    public void serviceIDNotify(ServiceID sid) 
    {
        this.sid = sid;
        System.out.println("MyProj service was assigned service id of " + sid);
    }
	
}
