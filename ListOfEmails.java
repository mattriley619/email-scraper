import java.util.Vector;

public class ListOfEmails extends Vector<Email>
{

    ListOfEmails()
    {

    }

    void addEmail(Email e)
    {
        if (this.size() > 0)
        {
            int i = this.size() - 1;
            boolean foundPos = false;

            while(i >= 0 && !foundPos)
            {
                if (e.compareTo(this.elementAt(i)) > 0) // if new email is greater than compared email
                {
                    this.insertElementAt(e, i+1);
                    foundPos = true;
                }
                i--;
            }
        }
        else
            this.addElement(e);

    }

}
