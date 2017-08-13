package edu.pdx.cs410J.cophares.client;

import com.google.gwt.i18n.shared.DateTimeFormat;
import edu.pdx.cs410J.AbstractFlight;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Flight extends AbstractFlight
{

  private int flightNumber;
  private String source;
  private Date departTime;
  private String destination;
  private Date arriveTime;

  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public Flight() {

  }

  /**
   * Constructor with all arguments required for private data
   * @param number flight number
   * @param src airport code of departing city
   * @param departTime time flight departs src
   * @param dest airport code of arriving city
   * @param arriveTime time flight arrives at dest
   */
  public Flight(int number, String src, Date departTime, String dest, Date arriveTime){
    flightNumber = number;
    source = src;
    this.departTime = departTime;
    destination = dest;
    this.arriveTime = arriveTime;
  }

  /**
   * This method returns the flight number
   * @return flightNumber
   */
  @Override
  public int getNumber() {
    return flightNumber;
  }

  /**
   * This method returns the departing airport code
   * @return source
   */
  @Override
  public String getSource() {
    return source;
  }

  /**
   * This method returns the departure time as a Date object
   * @return departTime
   */
  @Override
  public Date getDeparture() {
    return departTime;
  }

  /**
   * This method returns the departure time of the flight as a String
   * @return message and departTime
   */
  public String getDepartureString() {
    return formatDate(departTime);
  }

  /**
   * This method returns the arriving airport code
   * @return destination
   */
  public String getDestination() {
    return destination;
  }

  /**
   * This method returns the arrival time as a Date object
   * @return arriveTime
   */
  public Date getArrival() {
    return arriveTime;
  }

  /**
   * This method returns the arrival time of the flight as a String
   * @return message and arriveTime
   */
  public String getArrivalString() {
    return formatDate(arriveTime);
  }

  /**
   * Formats a date object into a string
   * @param date either departTime or arriveTime
   * @return formatted string of date object
   */
  public String formatDate(Date date){
    DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
    return dateTimeFormat.format(date);
  }
}
