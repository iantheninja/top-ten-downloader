package com.iantheninja.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class ParseApplications {
    private String xmlData;
    private ArrayList<Application> applications;

    public ParseApplications(String xmlData) {
        this.xmlData = xmlData;
        this.applications = new ArrayList<>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public boolean process(){
        boolean status = true;
        Application currentRecord;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();//enables the pulling of large xml data streams
            factory.setNamespaceAware(true);
            XmlPullParser xmlPullParser = factory.newPullParser();//for deriving the little bits of data from from parser factory
            xmlPullParser.setInput(new StringReader(this.xmlData));//gets an input stream (data being recieved)
            int eventType = xmlPullParser.getEventType();// to know where theres an entry in the xml

            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xmlPullParser.getName(); //gets the name of the tag
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        Log.d("ParseApplications", "Starting tag for " + tagName);
                        if (tagName.equalsIgnoreCase("entry")){
                            inEntry = true; // entry encountered
                            currentRecord = new Application(); //denotes a new application in the list
                            break;
                        }
                    case XmlPullParser.END_TAG:
                        Log.d("ParseApplications", "Ending tag for " + tagName);
                        break;
                    default:
                        // functionality to be added later
                }
                eventType = xmlPullParser.next(); // continues to the next section
            }

        }catch (Exception e){
            status = false;
            e.printStackTrace();
        }
        return true;
    }
}
