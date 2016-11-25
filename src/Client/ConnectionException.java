package Client;

public class ConnectionException extends Exception {
	
	/**
	 * Constructor.
	 * Good to have.
	 */
	public ConnectionException() {
		super();
	}
	
	/**
	 * Used to close the client if shit hits the fan.
	 * @return the string that is the key word for shutting down the client.
	 */
	public String specialDelivery(){
		return "serverShuttingDown";
	}
	
	/**
	 * Simply write out my custom exception message :)
	 * @return a witty string.   
	 */
	@Override
	public String getMessage(){	
		return "\n\n*** Wops... "
				+ "\n*** you have lost the connection to the server. \n";
	}
}