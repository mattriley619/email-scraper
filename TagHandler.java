import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import java.util.Vector;

public class TagHandler extends HTMLEditorKit.ParserCallback
{
    Vector<String> listOfLinks = new Vector<>();


    TagHandler()
    {

    }


    public void handleText(char[] data, int pos)
    {

    }
    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos)
    {

    }
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos)
    {
        if (t == HTML.Tag.A)
        {
            if (a.getAttribute(HTML.Attribute.HREF) != null)
            {
                if (a.getAttribute(HTML.Attribute.HREF).toString().toUpperCase().startsWith("MAILTO:"))
                {
                    System.out.println("EMAIL FROM HREF: " + a.getAttribute(HTML.Attribute.HREF));

                }
                else
                {
                  boolean isDupe = false;
                  int i = 0;

                  while(i < listOfLinks.size() && !isDupe && System.currentTimeMillis() - EmailHarvester.START_TIME < EmailHarvester.MAX_EXPANSION_TIME )
                  {
                      if (a.getAttribute(HTML.Attribute.HREF).equals(listOfLinks.elementAt(i)))
                      {
                          isDupe = true;
                      }
                      i++;
                  }
                  if (!isDupe)
                      listOfLinks.addElement(a.getAttribute(HTML.Attribute.HREF).toString());
                }

            }





        }


    }
    public void handleEndTag(HTML.Tag t, int pos)
    {

    }

    Vector<String> getListOfLinks()
    {
        return listOfLinks;
    }

}
