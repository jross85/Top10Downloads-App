package com.justinross.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by ross on 10/5/15.
 */
public class ParseApplications {
    private String xmlData;
    private ArrayList<Application> applications; //all entries stored in this array

    public ParseApplications(String xmlData) {
        this.xmlData = xmlData;
        applications = new ArrayList<>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public boolean process() {
        boolean status = true;
        Application currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        //Log.d("ParseApplications", "Starting tag for " + tagName);
                        if(tagName.equalsIgnoreCase("entry")) {
                            inEntry = true;
                            currentRecord = new Application(); //knows when to start a new entry
                        }

                    case XmlPullParser.TEXT:
                            textValue = xpp.getText();

                        break;
                    case XmlPullParser.END_TAG:
                        //Log.d("ParseApplications", "Ending tag for " + tagName);
                        if(inEntry) {
                            if(tagName.equalsIgnoreCase("entry")) {
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if(tagName.equalsIgnoreCase("name")) {
                                currentRecord.setName(textValue);
                            } else if(tagName.equalsIgnoreCase("artist")) {
                                currentRecord.setArtist(textValue);
                            } else if(tagName.equalsIgnoreCase("releaseDate")) {
                                currentRecord.setReleaseDate(textValue);
                            }
                        }
                        break;
                    default:
                        //Nothing else to do

                }
                eventType = xpp.next();
            }

            }
        catch(Exception e) {
            status = false;
            e.printStackTrace();
        }

        for(Application app: applications) {
            Log.d("ParseApplications", "******************");
            Log.d("ParseApplications", "Artist: " + app.getArtist());
            Log.d("ParseApplications", "Name: " + app.getName());
            Log.d("ParseApplications", "ReleaseDate: " + app.getReleaseDate());

        }
        return true;
    }
}
