package edu.pdx.cs410J.cophares.server;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.cophares.client.Airline;
import edu.pdx.cs410J.cophares.client.Flight;
import edu.pdx.cs410J.cophares.client.AirlineService;
import edu.pdx.cs410J.cophares.client.Parser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * The server-side implementation of the Airline service
 */
public class AirlineServiceImpl extends RemoteServiceServlet implements AirlineService
{
  private Airline airline;
  private Parser parser = new Parser();

  @Override
  public Airline getAirline(String name) {
    if(airline.getName().equalsIgnoreCase(name))
      return airline;
    else
      //change to throw some kind of exception
      return null;
  }

  @Override
  public void addFlight(String name, Flight flight) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    if(airline == null){
      airline = new Airline(name);
    }

    parser.parseFlightNumber(Integer.toString(flight.getNumber()));
    parser.validateAirportCode(flight.getSource());
    parser.parseDate(dateFormat.format(flight.getDeparture()));
    parser.validateAirportCode(flight.getDestination());
    parser.parseDate(dateFormat.format(flight.getArrival()));

    airline.addFlight(flight);
  }

  @Override
  public Airline getAirlineWithAirportCodes(String name, String src, String dest){
    if(!airline.getName().equalsIgnoreCase(name)){

    }

    Airline newAirline = new Airline(name);
    Collection<Flight> flights = airline.getFlights();

    for(Flight f: flights){
      if(f.getSource().equalsIgnoreCase(src) && f.getDestination().equalsIgnoreCase(dest))
        newAirline.addFlight(f);
    }

    return newAirline;
  }

  @Override
  public void throwUndeclaredException() {
    throw new IllegalStateException("Expected undeclared exception");
  }

  @Override
  public void throwDeclaredException() throws IllegalStateException {
    throw new IllegalStateException("Expected declared exception");
  }

  /**
   * Log unhandled exceptions to standard error
   *
   * @param unhandled
   *        The exception that wasn't handled
   */
  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }
}
