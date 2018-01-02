package org.apps4av.hubblewallpaper;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

/**
 * Created by zkhan on 1/1/18.
 */

public class StreamParser {

    public static LinkedList<Item> parse(Context context) {

        LinkedList<Item> items = new LinkedList<Item>();

        try {

            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();

            URL u = new URL("http://spacetelescopelive.org/feed/");
            InputStream is = u.openStream();
            parser.setInput(is, null);

            int event = parser.getEventType();
            String text = null;
            Item item = null;
            while (event != XmlPullParser.END_DOCUMENT)  {
                String name = parser.getName();
                switch (event) {

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.START_TAG:
                        if(name.equals("item")) {
                            item = new Item();
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(null == item) {
                            break;
                        }
                        if(name.equals("item")) {
                            items.add(item);
                            item = null;
                        }
                        if(name.equals("title")) {
                            item.setTitle(text);
                        }
                        if(name.equals("pubDate")) {
                            item.setDate(text);
                        }
                        if(name.equals("guid")) {
                            item.setLink(text);
                        }
                        if(name.equals("description")) {
                            item.setDescription(text);
                        }
                        if(name.equals("enclosure")) {
                            item.setUrl(parser.getAttributeValue(null, "url"));
                        }

                        break;
                }
                event = parser.next();
            }
            is.close();
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SecurityException se) {
            se.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return items;
    }

}
