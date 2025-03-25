import java.lang.*;


public class Email implements Comparable<Email>
{
    String email;

    Email(String email)
    {
        this.email = email;
    }






    public int compareTo(Email e)
    {
        return email.compareTo(e.email);
    }

    @Override
    public String toString()
    {
        return email;
    }
}
