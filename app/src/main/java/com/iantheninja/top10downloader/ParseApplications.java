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
        Application currentRecord = null;
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
                        if (tagName.equalsIgnoreCase("entry")){
                            inEntry = true; // entry encountered
                            currentRecord = new Application(); //denotes a new application in the list
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xmlPullParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if (tagName.equalsIgnoreCase("entry")) {
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if (tagName.equalsIgnoreCase("name")) {
                                currentRecord.setName(textValue); //sets the name (of app) from xml data
                            } else if (tagName.equalsIgnoreCase("artist")) {
                                currentRecord.setArtist(textValue); //sets the artist/author's from xml data
                            } else if (tagName.equalsIgnoreCase("releasedate")) {
                                currentRecord.setReleaseDate(textValue); //sets the name from xml data
                            }
                        }
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

        for (Application app: applications){
            Log.d("ParseApplications", "************");
            Log.d("ParseApplications", "Name is " + app.getName());
            Log.d("ParseApplications", "Artist is " + app.getArtist());
            Log.d("ParseApplications", "Release Date " + app.getReleaseDate());
        }

        return true;
    }
}
