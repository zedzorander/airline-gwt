package edu.pdx.cs410J.cophares.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import edu.pdx.cs410J.ParserException;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic GWT class that makes sure that we can send an airline back from the server
 */
public class AirlineGwt implements EntryPoint {

  private final Alerter alerter;
  private final AirlineServiceAsync airlineService;
  private final Logger logger;

  @VisibleForTesting
  Label programName;

  @VisibleForTesting
  Label searchByAirline;

  @VisibleForTesting
  TextBox airlineNameText;

  @VisibleForTesting
  Button showAirlineButton;

  @VisibleForTesting
  Label searchFlightsByAirports;

  @VisibleForTesting
  TextBox airlineAirportText;

  @VisibleForTesting
  Button showFlightsByAirportButton;

  @VisibleForTesting
  Label addFlight;

  @VisibleForTesting
  TextBox flightText;

  @VisibleForTesting
  Button addFlightButton;

  @VisibleForTesting
  TextArea flightInfoText;

  @VisibleForTesting
  //PrettyPrinter

  /*
  @VisibleForTesting
  Button showUndeclaredExceptionButton;

  @VisibleForTesting
  Button showDeclaredExceptionButton;

  @VisibleForTesting
  Button showClientSideExceptionButton;
  */

  public AirlineGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  @VisibleForTesting
  AirlineGwt(Alerter alerter) {
    this.alerter = alerter;
    this.airlineService = GWT.create(AirlineService.class);
    this.logger = Logger.getLogger("airline");
    Logger.getLogger("").setLevel(Level.INFO);  // Quiet down the default logging
  }

  private void alertOnException(Throwable throwable) {
    Throwable unwrapped = unwrapUmbrellaException(throwable);
    StringBuilder sb = new StringBuilder();
    sb.append(unwrapped.toString());
    sb.append('\n');

    for (StackTraceElement element : unwrapped.getStackTrace()) {
      sb.append("  at ");
      sb.append(element.toString());
      sb.append('\n');
    }

    this.alerter.alert(sb.toString());
  }

  private Throwable unwrapUmbrellaException(Throwable throwable) {
    if (throwable instanceof UmbrellaException) {
      UmbrellaException umbrella = (UmbrellaException) throwable;
      if (umbrella.getCauses().size() == 1) {
        return unwrapUmbrellaException(umbrella.getCauses().iterator().next());
      }

    }

    return throwable;
  }

  private void addWidgets(VerticalPanel panel) {
    final Parser parser = new Parser();
    final StringBuilder sb = new StringBuilder();

    programName = new Label("Welcome to Cole's Air Phares");

    //Search by airline widgets
    searchByAirline = new Label("Search by Airline (e.g. Delta)");
    airlineNameText = new TextBox();
    showAirlineButton = new Button("Search");

    //gets airline and pretty prints
    showAirlineButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        logger.info("Calling getAirline");
        final String name = airlineNameText.getText();

        if(name != null){
          airlineService.getAirline(name, new AsyncCallback<Airline>() {
            @Override
            public void onFailure(Throwable ex) {
              alertOnException(ex);
            }

            @Override
            public void onSuccess(Airline airline) {
              flightInfoText.setText("Flights for " + name + " airline:\n");
              showAirline(airline, flightInfoText);
              //showAirline("Flights for " + name + " airline:\n", sb, airline);
            }
          });
        }
      }
    });

    //search by airline and airports widgets
    searchFlightsByAirports = new Label("Show Airline Flights by Airport (e.g. Delta PDX SFO");
    airlineAirportText = new TextBox();
    showFlightsByAirportButton = new Button("Search");

    //searches airlines by source and destination and pretty prints
    showFlightsByAirportButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        logger.info("Calling getAirlineWithAirportCodes");
        final String name, src, dest;

        //get variables from text box for String variables
        String input = airlineAirportText.getText();
        String[] parts = input.split(" ");
        int index = 0;

        //initialize and parse String variables for parsing
        name = parts[index++];
        src = parser.validateAirportCode(parts[index++]);
        dest = parser.validateAirportCode(parts[index++]);

        //search for flights by airport codes
        airlineService.getAirlineWithAirportCodes(name, src, dest, new AsyncCallback<Airline>() {
          @Override
          public void onFailure(Throwable ex) {
            alertOnException(ex);
          }

          @Override
          public void onSuccess(Airline airline) {
            flightInfoText.setText("Flights between " + src + " and " + dest + ":\n");
            showAirline(airline, flightInfoText);
            //showAirline("Flights between " + src + " and " + dest + ":\n", sb, airline);
          }
        });
      }
    });

    //add flight widgets
    addFlight = new Label("Add Flight (e.g. Delta 42 PDX 08/21/2017 07:34 am SFO 02/10/2019 11:58 pm)");
    flightText = new TextBox();
    flightText.setVisibleLength(70);
    addFlightButton = new Button("Add");

    //parses data and adds flight to airline
    addFlightButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        logger.info("Calling addFlight");
        final String name, src, dest;
        final int flightNumber;
        final Date departDate, arriveDate;
        int index = 0;

        String input = flightText.getText();
        String[] args = input.split(" ");

        name = args[index++];
        flightNumber = parser.parseFlightNumber(args[index++]);
        src = parser.validateAirportCode(args[index++]);
        departDate = parser.parseDate(args[index++] +  " " + args[index++] + " " + args[index++]);
        dest = parser.validateAirportCode(args[index++]);
        arriveDate = parser.parseDate(args[index++] + " " + args[index++] + " " + args[index++]);

        if(arriveDate.compareTo(departDate) < 0)
          alertOnException(new ParserException("Arrival Date Is Before Departure Date!"));

        //add flight to airline
        Flight flight = new Flight(flightNumber, src, departDate, dest, arriveDate);
        airlineService.addFlight(name, flight, new AsyncCallback<Void>() {
          @Override
          public void onFailure(Throwable ex) {
            alertOnException(ex);
          }

          @Override
          public void onSuccess(Void aVoid) {
            alerter.alert("Flight " + flightNumber + " from " + src + " to " + dest + " has been added!");
          }
        });
      }
    });

    //text area widget
    flightInfoText = new TextArea();
    flightInfoText.setCharacterWidth(80);
    flightInfoText.setVisibleLines(35);

    //add widgets to panel
    panel.add(programName);
    panel.add(searchByAirline);
    panel.add(airlineNameText);
    panel.add(showAirlineButton);
    panel.add(searchFlightsByAirports);
    panel.add(airlineAirportText);
    panel.add(showFlightsByAirportButton);
    panel.add(addFlight);
    panel.add(flightText);
    panel.add(addFlightButton);
    panel.add(flightInfoText);

  }


  private void showAirline(Airline airline, TextArea flightInfoText){
    PrettyPrinter prettyPrinter = new PrettyPrinter();

    prettyPrinter.dump(airline);

    flightInfoText.setText(prettyPrinter.getPrettyText());
  }



  @Override
  public void onModuleLoad() {
    setUpUncaughtExceptionHandler();

    // The UncaughtExceptionHandler won't catch exceptions during module load
    // So, you have to set up the UI after module load...
    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        setupUI();
      }
    });

  }

  private void setupUI() {
    RootPanel rootPanel = RootPanel.get();
    VerticalPanel panel = new VerticalPanel();
    rootPanel.add(panel);

    addWidgets(panel);
  }

  private void setUpUncaughtExceptionHandler() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      @Override
      public void onUncaughtException(Throwable throwable) {
        alertOnException(throwable);
      }
    });
  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

  //----------------------------------IGNORE ANYTHING BELOW HERE------------------------------------------------------

  /*
  private void showAirline(String message, StringBuilder sb, Airline airline) {

    sb.append(message);
    prettyPrintAirline(sb, airline, flightInfoText);
    sb.setLength(0);


    airlineService.getAirline(name, new AsyncCallback<Airline>() {

      @Override
      public void onFailure(Throwable ex) {
        alertOnException(ex);
      }

      @Override
      public void onSuccess(Airline airline) {
        StringBuilder sb = new StringBuilder(airline.toString());
        Collection<Flight> flights = airline.getFlights();
        for (Flight flight : flights) {
          sb.append(flight);
          sb.append("\n");
        }
        alerter.alert(sb.toString());
      }
    });

  }


  private void prettyPrintAirline(StringBuilder sb, Airline airline, TextArea flightInfoText){
    //gets the flights from abstractAirline
    Collection<Flight> flights = airline.getFlights();

    //write the airline name to file and set the date format
    Flight flight;
    DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");

    //write all the flight information to file
    for(int i = 0; i < flights.size(); ++i) {
      flight = (Flight) flights.toArray()[i];

      sb.append("\tFlight Number:  \t" + flight.getNumber() + "\n" + "\tDeparting From: \t" +
              AirportNames.getName(flight.getSource().toUpperCase()) + "\n\tDeparting Time: \t" +
              dateTimeFormat.format(flight.getDeparture()) + "\n" + "\tArriving At: \t\t" +
              AirportNames.getName(flight.getDestination().toUpperCase()) + "\n\tArriving Time:  \t" +
              dateTimeFormat.format(flight.getArrival()) + "\n" + "\tFlight Duration: \t" +
              getTimeLengthInMinutes(flight.getDeparture(), flight.getArrival()) + " mins.\n\n");
              //TimeUnit.MILLISECONDS.toMinutes(flightDuration) + " mins.\n\n");
    }
    flightInfoText.setText(sb.toString());
  }

  interface DateFormatter{
    public String format(Date date);
  }

  private String getTimeLengthInMinutes(Date departTime, Date arriveTime){
    return String.valueOf((arriveTime.getTime() - departTime.getTime()) / 60000);
  }
  */
}
