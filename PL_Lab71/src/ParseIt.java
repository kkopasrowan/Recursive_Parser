
/**
 * Write a description of class ParseIt here.
 * 
 *  Implements (drives) a demo recursive descent parser for 
 *  an Assignment Statement
 *  
 *  based on Pratt Zelkowitz example in C
 * 
 * @author (adapted by Prof. Weissman) 
 * @version (August 2023)
 */
public class ParseIt
{
    // instance variables 
    private String initString;  //what we want to parse
    private String curString;  //what's left to parse
    private String nextTokenStr;  //convention: always maintain as next token to process

    /** 
     * description of highest sentential form the parser can evaluate 
     */
    private final String impLevel = "Variable";  //what the code can evaluate

    /**
     * Constructor for objects of class ParseIt
     */
    public ParseIt(String initString)
    {
        this.initString = new String(initString);  
        this.curString = new String(initString); 
        this.nextTokenStr = "";
        nextToken();  
    }

    /**
     *   parse() is the main driver
     *   
     *   added exception handling throughout 
     *    
     *      -- in the java version, Error() call is replaced by simply throwing a ParseException
     */
    public void parse()
    {
        //shd probably test for null curString...

        try {
            variable() ;   //invoke higest sentential method
            System.out.println( impLevel + " Result: OK");
        }
        catch (ParseException e)
        {
            System.out.println( impLevel + " Result: ERROR  "+ e.getMessage());
        }
    }

    public void expression() throws ParseException{
        //Objective #2: Add code to parse expressions here        
        variable(); // Parsing a variable is a valid expression
        
        // Check for a +
        if (nextTokenStr.equals("+")) {
            nextToken();
            // Check if the next token is a number
            try {
                Integer.parseInt(nextTokenStr);
                nextToken(); 
            } catch (NumberFormatException e) {
                throw new ParseException("Need something after the +");
            }
        }
        
        // You can add more complex parsing logic for expressions here
    }


    public void variable() throws ParseException
    {
        int id_pos = identifier();  
        if( id_pos > 0 ) //not a simple identifier
        {
            //following could be modified to LOOP 
            // for multiple subscripts!
            if( nextTokenStr.charAt(id_pos) == '[' ) //first char 
            {
                nextTokenStr = nextTokenStr.substring(id_pos);   //token.Delete(0,id_pos-1);
                //	 Sublist();
                if( nextTokenStr.charAt(nextTokenStr.length()-1) == ']'  )
                    nextTokenStr = " ";  // done--OK!
                else 
                    throw new ParseException("Missing right bracket");   //Error("Missing right bracket");
            }
            else // not a variable name at all 
               throw new ParseException("Invalid Character in Identifier");  // Error("Invalid Character in Identifier");

        } //if (!Id..)
        else if (id_pos == -1)
            throw new ParseException("Expected a Variable");  //Error("Expected a Variable");
        //case of a simple variable falls thru: OK!

        nextToken();   

    }

    public void assignmentStatement() throws ParseException
    {
        //Objective #3: Add code to parse assignment statements            
        variable(); // Parse variable
        
        if (nextTokenStr.equals("=")) {
            nextToken();
            expression();//Parse expression
        } else {
            throw new ParseException("Expected = in assignment statement");
        }
    }

    int identifier()
    //non-destructive test   of  nextTokenStr
    // 0==simple variable
    // -1 == failure  e.g. 4v%arname   -+44
    // pos == failure offset in token  e.g. num1[22]

    {  
        if (Character.isLetter(nextTokenStr.charAt(0)))   //first char a letter
        { //so far so good
            int pos=0; //position in nextTokenStr to test
            while ( (pos < nextTokenStr.length()) &&
            (
                Character.isLetter(nextTokenStr.charAt(pos)) 
                || Character.isDigit(nextTokenStr.charAt(pos))
                || nextTokenStr.charAt(pos) == '_' 
            ) ) 
                pos++;

            if (pos < nextTokenStr.length()) /*|| (token[pos] != ' ')*/ 
                return pos; //invalid character or a subscript...
        }
        else
            return -1;  //failed -- e.g., a number by itself!

        return 0;  // case of space-delimited variable identifier

    }

    /**
     * nextToken() extracts [and removes] a space-delimited String from curString
     *   and stores in nextTokenStr   
     *   
     *   sets nextTokenStr to " " [one space] when no further text remain to be processed
     *   
     *   conventition: this routine is invoked so as to always populate nextTokenStr 
     */
    void nextToken()
    {
        //adapted from a c implementation

        String ltoken=""; //to build local token
        //this HORRIBLY inefficient java routine kills a ton of immutable Strings during processing :=(

        // remove leading blanks
        while (!curString.isEmpty() && curString.charAt(0) == ' ')
            curString = curString.substring(1);  // remove leading blanks   -- could do java trim()

        //extract next token ... to blank or eof()
        while (  !curString.isEmpty() 
        &&  curString.charAt(0) != ' '
        && curString.charAt(0) != '\n'
        )
        {
            ltoken = ltoken + curString.charAt(0);  //should bypass initial space or trim token at end
            curString = curString.substring(1);   // remove current char
        }

        //OLD comment:   i ASSUMED initial length of CString is zero -- check!
        /*  if ((in_char == EOF) || (in_char = '\n'))
        exit(0);
         */
        /*		if (parse_phrase.IsEmpty())   //problematic -- misses some errors  
        exit(0);   
         */

        if (ltoken.isEmpty()) 
            ltoken = " ";  //fix for trailing +, - etc. 
        //  avoids assert() errors for empty return value
        this.nextTokenStr = ltoken;

    }

}
