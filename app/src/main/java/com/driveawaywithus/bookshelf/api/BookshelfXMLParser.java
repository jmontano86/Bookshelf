package com.driveawaywithus.bookshelf.api;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jeremiah on 8/2/2016.
 */
public class BookshelfXMLParser {

    private static final String ns = null;
    private static final String TAG = "BOOKSHELF_XML_PARSE";

    public String[] parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private String[] readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        String[] entries = new String[20];
        int event = parser.getEventType();

        String title = "";
        String StartTag = "";
        String rating = "";
        int x = 0;
        int pubYear = 0;
        int pubDay = 0;
        int pubMonth = 0;
        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {
                case XmlPullParser.START_TAG:
                    StartTag = parser.getName();

                    switch (StartTag) {
                        case "title":
                            title = parser.nextText();
                            break;

                    }
                    break;
                case XmlPullParser.TEXT:
                    switch (StartTag) {
                        case "title":
                            if (parser.getText() != null) {
                                //entries.add(parser.getText());
                                Log.d(TAG, "Title: " + parser.getText());
                            }
                            StartTag = "";
                            break;
                        case "name":
                            if (parser.getText() != null) {
                                entries[x] = (title + "-" + parser.getText() + "-" +
                                        getPublicationDate(pubYear, pubMonth, pubDay) +
                                        "-" + rating);
                                x = x + 1;
                                pubYear = 0;
                                pubDay = 0;
                                pubMonth = 0;
                                Log.d(TAG, "Author: " + parser.getText());
                            }
                            StartTag = "";
                            break;
                        case "original_publication_month":
                            if (parser.getText() != null && !parser.getText().contains("\n")) {
                                pubMonth = Integer.parseInt(parser.getText());
                            }
                            StartTag = "";
                            break;
                        case "original_publication_day":
                            if (parser.getText() != null && !parser.getText().contains("\n")) {
                                pubDay = Integer.parseInt(parser.getText());
                            }
                            StartTag = "";
                            break;
                        case "original_publication_year":
                            if (parser.getText() != null && !parser.getText().contains("\n")) {
                                pubYear = Integer.parseInt(parser.getText());
                            }
                            StartTag = "";
                            break;
                        case "average_rating":
                            rating = parser.getText();
                            StartTag = "";
                            break;
                    }
            }
            event = parser.next();
        }
        return entries;
    }

    private boolean isNotError(XmlPullParser parser) {
        try {
            String test = parser.nextText();
            return true;
        } catch (XmlPullParserException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private String getPublicationDate(int pubYear, int pubMonth, int pubDay) {
        String strDate = "";

        if (pubDay == 0) {
            if (pubMonth == 0) {
                strDate = pubYear + "";
            } else {
                if (getPubMonth(pubMonth) != null) {
                    strDate = getPubMonth(pubMonth) + " " + pubYear;
                }
            }
        } else {
            if (getPubMonth(pubMonth) != null) {
                strDate = getPubMonth(pubMonth) + " " + pubDay + ", " + pubYear;
            }
        }

        return strDate;
    }

    private String getPubMonth(int pubMonth) {
        switch (pubMonth) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return null;
        }
    }
}