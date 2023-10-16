
/**
 * Write a description of class ParseException here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ParseException extends Exception
{
    
    public ParseException()
    {
         super("Parse Exception");
    }
    
    public ParseException(String msg)
    {
         super("Parse Exception: " + msg);
    }

   
}
