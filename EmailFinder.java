import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailFinder
{
    String bigStr;
    ListOfEmails emailList;
    Pattern pattern;
    Matcher matcher;
    boolean done;

    EmailFinder(String str)
    {
        bigStr = str;
        emailList = new ListOfEmails();
    }



    void findEmails()
    {
        pattern = Pattern.compile("[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");
        matcher = pattern.matcher(bigStr);
        done = false;

        while(!done)
        {
            if (matcher.find())
            {
                //System.out.println(bigStr.substring(matcher.start(), matcher.end()));
                emailList.addEmail(new Email(bigStr.substring(matcher.start(), matcher.end())));
                matcher.region(matcher.end(), bigStr.length());

            }
            else
            {
                done = true;
                System.out.println("No more matches.");
            }
        }


    }









}
