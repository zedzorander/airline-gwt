package edu.pdx.cs410J.cophares.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.i18n.shared.DateTimeFormat;
import edu.pdx.cs410J.AirportNames;

import java.util.Date;

/**
 * Created by Cole on 7/24/2017.
 */
public class Parser {
    private DateTimeFormat dateTimeFormat;

    public Parser(){
        String pattern = "MM/dd/yyyy hh:mm a";
        dateTimeFormat = DateTimeFormat.getFormat(pattern);
    }

    /**
     * checks the length of src and dest
     * also checks that src and dest are a letter A-Z or a-z
     * @param code: either the src argument of the dest arg
     * @return code if it is a valid code
     */
    public String validateAirportCode(String code) {
        if(code.length() == 3){
            for(int i = 0; i < code.length(); ++i){
                if(!Character.isLetter(code.charAt(i))){
                    //throw new ParserException("Invalid Airport Code: " + code);
                    return null;
                }
            }
        }else if(AirportNames.getName(code) == null) {
            return null;
            //throw new ParserException("Airport Code Is Not A Recognized Airport: " + code);
        }
        return code;
    }

    /**
     * turns flight number string command line argument into int
     * @param flightNumber flights identifying number
     * @return true if parameter can be converted to int, otherwise exits with error 1
     */
    public int parseFlightNumber(String flightNumber) throws NumberFormatException {
        try {
            return Integer.parseInt(flightNumber);
        }catch(NumberFormatException ex){
            throw ex;
        }
    }

    /**
     * checks for valid date form and returns date as a Date object
     * @param date the date of the flight (either departTime or arriveTime)
     * @return Date object (either departTime or arriveTime)
     */
    public Date parseDate(String date) throws IllegalArgumentException{
        return dateTimeFormat.parseStrict(date);
    }


    /*
     * Validate date format with regular expression. Following borrowed taken from http://www.mkyong.com/regular-expressions/how-to-validate-date-with-regular-expression/
     * @param flightTime date address for validation
     * @return true valid date format, false invalid date format
     *
    private boolean dateValidation(String flightTime){
        String datePattern = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((20)\\d\\d) ([01]?[0-9]|2[0-3]):[0-5][0-9] ([aApP][mM])";
        Pattern pattern = Pattern.compile(datePattern);
        Matcher matcher;

        matcher = pattern.matcher(flightTime);

        if(matcher.matches()){

            matcher.reset();

            if(matcher.find()) {

                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") && (month.equals("4") || month.equals("6") || month.equals("9") ||
                        month.equals("11") || month.equals("04") || month.equals("06") || month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if (year % 4 == 0) {
                        if (day.equals("30") || day.equals("31")) {
                            System.err.println("Invalid Date: " + flightTime);
                            return false;
                        } else
                            return true;
                    } else
                        return !(day.equals("29") || day.equals("30") || day.equals("31"));
                }else
                    return true;
            }else
                return false;
        }else
            return false;
    }
    */
}
