package edu.pdx.cs410J.cophares.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.pdx.cs410J.ParserException;

/**
 * A GWT remote service that returns a dummy airline
 */
@RemoteServiceRelativePath("airline")
public interface AirlineService extends RemoteService {

  /**
   * Returns the current date and time on the server
   */
  Airline getAirline(String name);

  /**
   * Returns the flights that match src and dest in new Airline
   * @param name airline name
   * @param src departing airport code
   * @param dest arriving airport code
   */
  Airline getAirlineWithAirportCodes(String name, String src, String dest);

  void addFlight(String name, Flight flight);

  /**
   * Always throws an undeclared exception so that we can see GWT handles it.
   */
  void throwUndeclaredException();

  /**
   * Always throws a declared exception so that we can see GWT handles it.
   */
  void throwDeclaredException() throws IllegalStateException;

}
