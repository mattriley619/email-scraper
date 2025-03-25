import javax.swing.*;
import javax.swing.text.html.parser.ParserDelegator;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.Vector;


class EmailHarvester
{
    public static final int MAX_DISTANCE = 3;
    public static final long MAX_RUNTIME = 10000;
    public static final long MAX_EXPANSION_TIME = 10000;
    public static final long START_TIME = System.currentTimeMillis();


    public static void main(String[] args)
    {

        new FrameClass();


    }
}
class FrameClass extends JFrame
        implements ActionListener
{
    JList<String> list;
    DefaultListModel<String> dlm;
    ExtractedData extractedData;
    Vector<ExtractedData> dataList;

    TagHandler tagHandler;

    JScrollPane scrollPane;
    JButton goButton;
    JTextField urlField;
    URL url;
    BufferedReader pageReader;
    String str;
    String bigStr;
    EmailFinder emailFinder;
    int pos;
    String tempBaseDomain;
    String[] domainParts;


    FrameClass()
    {
        dlm = new DefaultListModel<String>();
        list = new JList<String>(dlm);
        list.setBackground(Color.LIGHT_GRAY);

        scrollPane = new JScrollPane(list);


        goButton = new JButton("Go");
        goButton.addActionListener(this);
        urlField = new JTextField("Enter URL here");

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(goButton, BorderLayout.SOUTH);
        this.add(urlField, BorderLayout.NORTH);


        setupMainFrame();
    }

    void setupMainFrame()
    {
        Toolkit tk;
        Dimension d;

        tk = Toolkit.getDefaultToolkit();
        d = tk.getScreenSize();
        setSize(d.width / 2, d.height / 2);
        setLocation(d.width / 4, d.height / 4);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Email Harvester");
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == goButton)
        {
           crawl();

        }
    }

    void crawl()
    {
        initialize();

        tagHandler.listOfLinks = tagHandler.getListOfLinks();
        for(int i = 0; i < tagHandler.listOfLinks.size(); i++)
        {
            System.out.println(tagHandler.listOfLinks.elementAt(i));
        }
        while(pos < tagHandler.listOfLinks.size() && System.currentTimeMillis() - EmailHarvester.START_TIME < EmailHarvester.MAX_RUNTIME)
        {
            if (tryToParse(tagHandler.listOfLinks.elementAt(pos), tempBaseDomain))
            {
                try
                {
                    str = pageReader.readLine();
                    bigStr = str;

                    while (str != null)
                    {
                        str = pageReader.readLine();
                        if (str != null)
                            bigStr = bigStr.concat(str);
                    }
                    emailFinder = new EmailFinder(bigStr);
                    emailFinder.findEmails();

                    for(int i = 0; i < emailFinder.emailList.size(); i++)
                        dlm.addElement(emailFinder.emailList.elementAt(i).toString());

                    extractedData = new ExtractedData();
                    extractedData.link = tagHandler.listOfLinks.elementAt(pos);
                    extractedData.baseDomain = dataList.elementAt(dataList.size() - 2).baseDomain;
                    tempBaseDomain = extractedData.baseDomain;

                    dataList.addElement(extractedData);

                }
                catch(Exception e)
                {
                    System.out.println("Exception in crawl()");
                }




            }




            pos++;
        }


    }

    void initialize()
    {
        try
        {
            dlm.clear();
            url = new URL(urlField.getText());
            pageReader = new BufferedReader(new InputStreamReader(url.openStream()));
            str = pageReader.readLine();
            bigStr = str;

            while (str != null)
            {
                str = pageReader.readLine();
                if (str != null)
                    bigStr = bigStr.concat(str);
            }
            emailFinder = new EmailFinder(bigStr);
            emailFinder.findEmails();

            for(int i = 0; i < emailFinder.emailList.size(); i++)
                dlm.addElement(emailFinder.emailList.elementAt(i).toString());

            extractedData = new ExtractedData();
            extractedData.link = urlField.getText();
            extractedData.distance = 0;
            domainParts = urlField.getText().split("\\.");
            if(domainParts.length > 2)
                extractedData.baseDomain = domainParts[0] + "." + domainParts[1] + "." + domainParts[2];
            else if(domainParts.length == 2)
                extractedData.baseDomain = domainParts[0] + "." + domainParts[1];
            tempBaseDomain = extractedData.baseDomain;

            dataList = new Vector<ExtractedData>();
            dataList.addElement(extractedData);


            pos = 1;

            tagHandler = new TagHandler();

            new ParserDelegator().parse(new InputStreamReader(url.openStream()), tagHandler, true);

        }
        catch (MalformedURLException mfe)
        {
            JOptionPane.showMessageDialog(null, "Error - Malformed URL");
        }
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(null, "Error - IO Exception");
        }

    }

    boolean tryToParse(String link, String baseDomain)
    {

        try
        {

            url = new URL(link);
            System.out.println(url.toString());
            pageReader = new BufferedReader(new InputStreamReader(url.openStream()));
            new ParserDelegator().parse(new InputStreamReader(url.openStream()), tagHandler, true);

            return true;
        }
        catch(Exception e)
        {
            System.out.println("Exception in tryToParse() - Level 1");

            try
            {
                url = new URL("http://" + link);
                System.out.println(url.toString());
                pageReader = new BufferedReader(new InputStreamReader(url.openStream()));
                new ParserDelegator().parse(new InputStreamReader(url.openStream()), tagHandler, true);

                return true;
            }
            catch(Exception r)
            {
                System.out.println("Exception in tryToParse() - Level 2");

                try
                {
                    url = new URL("https://" + link);
                    System.out.println(url.toString());
                    pageReader = new BufferedReader(new InputStreamReader(url.openStream()));
                    new ParserDelegator().parse(new InputStreamReader(url.openStream()), tagHandler, true);

                    return true;
                }
                catch(Exception t)
                {
                    System.out.println("Exception in tryToParse() - Level 3");

                    try
                    {
                        url = new URL(baseDomain + link);
                        System.out.println(url.toString());
                        pageReader = new BufferedReader(new InputStreamReader(url.openStream()));
                        new ParserDelegator().parse(new InputStreamReader(url.openStream()), tagHandler, true);

                        return true;
                    }
                    catch(Exception y)
                    {
                        System.out.println("Exception in tryToParse() - Level 4");

                        try
                        {
                            url = new URL(baseDomain + "/" + link);
                            System.out.println(url.toString());
                            pageReader = new BufferedReader(new InputStreamReader(url.openStream()));
                            new ParserDelegator().parse(new InputStreamReader(url.openStream()), tagHandler, true);

                            return true;
                        }
                        catch(Exception u)
                        {
                            System.out.println("Exception in tryToParse() - Level 5");


                        }
                    }
                }

            }

        }

        return false;
    }

}