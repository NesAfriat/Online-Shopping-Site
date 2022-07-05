package Domain.Market;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static ConcurrentLinkedQueue<String> events;
    private static ConcurrentLinkedQueue<String> errors;


    private static class LoggerHolder {
        private static final Logger instance = new Logger();
    }

    public static Logger getInstance(){
        return Logger.LoggerHolder.instance;
    }


    private Logger(){
        events = new ConcurrentLinkedQueue<>();
        errors = new ConcurrentLinkedQueue<>();
    }

    /**
     * log event
     * @param message - message to the admin
     */
    public void logEvent(String message){
        events.add(message);
        try {
            FileWriter myWriter = new FileWriter("EventLogger.txt", true);
            myWriter.write(message);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    /**
     * log error
     * @param message - error message that has occurred
     */
    public void logError(String message){
        errors.add(message);
        try {
            FileWriter myWriter = new FileWriter("ErrorLogger.txt", true);
            myWriter.write(message);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * return all events to the admin
     * @return string array
     */
    public String[] getAllEvents(){
        return events.toArray(new String[0]);
    }
    /**
     * return all errors to the admin
     * @return string array
     */
    public String[] getAllErrors(){
        return errors.toArray(new String[0]);
    }

    /**
     * clear all logged events
     */
    public void clearEventLogs(){
        events.clear();
    }

    /**
     * clear all logged errors
     */
    public void clearErrorLogs(){
        errors.clear();
    }

    public void writeEvents(){
        try {
            FileWriter myWriter = new FileWriter("EventLogger.txt", true);
            for (String event: events) {
                myWriter.write(event);
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
