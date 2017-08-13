package edu.pdx.cs410J.cophares.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.i18n.shared.DateTimeFormat;
import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Cole on 7/25/2017.
 */
public class PrettyPrinter implements AirlineDumper {
    private String filename;
    private Flight flight;
    private long flightDuration;
    //private DateTimeFormat dateTimeFormat;

    @VisibleForTesting
    static final String DATE_FORMAT_PATTERN = "MM/dd/yyyy hh:mm a";
    private final DateFormatter dateFormatter;
    private StringBuilder prettyText = new StringBuilder();

    PrettyPrinter(){
        this(new DateFormatter(){
            @Override
            public String format(Date date){
                DateTimeFormat format = DateFormatter.getFormat(DATE_FORMAT_PATTERN);
                return format.format(date);
            }
        });
    }

    /*
    PrettyPrinter(String filename){
        this.filename = filename;
        dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
    }
    */

    @VisibleForTesting
    PrettyPrinter(DateFormatter dateFormatter){
        this.dateFormatter = dateFormatter;
    }

    /**
     * This method prints out the airline information in a "pretty" style
     * @param abstractAirline airline to be printed out
     * @throws IOException throws if file filename is not valid
     */
    @Override
    public void dump(AbstractAirline abstractAirline) {
        //gets the flights from abstractAirline
        Collection<Flight> flights = abstractAirline.getFlights();

        //makes sure flights exist before writing to file
        if (flights.size() == 0) {
            System.out.println(abstractAirline.getName() + " has no flights!");
            return;
        }

        //write the airline name to file
        prettyText.append("Airline Name: " + abstractAirline.getName() + "\n\n");
        //
        for (int i = 0; i < flights.size(); ++i) {
            flight = (Flight) flights.toArray()[i];

            prettyFlight(flight);
        }
        //flush the stream to file and close the writer
        //bw.flush();
        //bw.close();
    }

    private void prettyFlight(Flight flight){
        //get departure and arrival dates to find the time of the flight
        Date departTime = flight.getDeparture();
        Date arriveTime = flight.getArrival();
        flightDuration = arriveTime.getTime() - departTime.getTime();

        //append flight info to StringBuilder
        prettyText.append("\tFlight Number:  \t" + flight.getNumber() + "\n");
        prettyText.append("\tDeparting From: \t" + AirportNames.getName(flight.getSource().toUpperCase()));
        //dateTimeFormat.format(flight.getDeparture()) + "\n" +
        prettyText.append("\n\tDeparting Time: \t" + prettyDate(departTime) + "\n");
        prettyText.append("\tArriving At: \t\t" + AirportNames.getName(flight.getDestination().toUpperCase()));
        //dateTimeFormat.format(flight.getArrival()) + "\n" +
        prettyText.append("\n\tArriving Time:  \t" + prettyDate(arriveTime) + "\n");
        prettyText.append("\tFlight Duration: \t" + getTimeLengthInMinutes(flight.getDeparture(), flight.getArrival()) + " mins.\n\n");
        //TimeUnit.MILLISECONDS.toMinutes(flightDuration) + " mins.\n\n");
    }

    private String prettyDate(Date date){
        return this.dateFormatter.format(date);
    }

    public String getPrettyText(){
        return prettyText.toString();
    }

    interface DateFormatter {
        String format(Date date);
    }

    private String getTimeLengthInMinutes(Date departTime, Date arriveTime){
        return String.valueOf((arriveTime.getTime() - departTime.getTime()) / 60000);
    }
}
