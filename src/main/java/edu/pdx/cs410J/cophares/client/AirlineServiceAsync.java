package edu.pdx.cs410J.cophares.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.pdx.cs410J.ParserException;

/**
 * The client-side interface to the airline service
 */
public interface AirlineServiceAsync {

  /**
   * Return an airline created on the server
   */
  void getAirline(String name, AsyncCallback<Airline> async);

  /**
   * Returns the flights that match src and dest in new Airline
   * @param name airline name
   * @param src departing airport code
   * @param dest arriving airport code
   */
  void getAirlineWithAirportCodes(String name, String src, String dest, AsyncCallback<Airline> async);

  void addFlight(String name, Flight flight, AsyncCallback<Void> async);

  /**
   * Always throws an exception so that we can see how to handle uncaught
   * exceptions in GWT.
   */
  void throwUndeclaredException(AsyncCallback<Void> async);

  /**
   * Always throws a declared exception so that we can see GWT handles it.
   */
  void throwDeclaredException(AsyncCallback<Void> async);
}
