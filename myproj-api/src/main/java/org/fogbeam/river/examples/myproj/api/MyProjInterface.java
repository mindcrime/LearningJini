package org.fogbeam.river.examples.myproj.api;

import java.io.IOException;
import java.rmi.Remote;

public interface MyProjInterface extends Remote
{
	public String getName( int objId ) throws IOException;
}
