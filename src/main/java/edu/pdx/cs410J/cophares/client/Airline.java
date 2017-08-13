package edu.pdx.cs410J.cophares.client;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

public class Airline extends AbstractAirline<Flight>
{
  private String airlineName;
  private Collection<Flight> flights;

  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public Airline() {

  }

  /**
   * Constructor with argument
   * @param name name of the airline
   */
  public Airline(String name){
    airlineName = name;
    flights = new TreeSet();
  }

  /**
   * This method returns the name of the airline
   * @return airlineName
   */
  @Override
  public String getName() {
    return airlineName;
  }

  /**
   * This function adds a flight to the airlines collection
   * @param flight flight to be added
   */
  @Override
  public void addFlight(Flight flight) {
    this.flights.add(flight);
  }

  /**
   * This function returns the collection of flights for the airline
   * @return flights
   */
  @Override
  public Collection<Flight> getFlights() {
    return this.flights;
  }
}
