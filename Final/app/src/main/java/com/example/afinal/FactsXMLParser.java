package com.example.afinal;


import android.content.Context;
import android.content.res.AssetManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FactsXMLParser {

	final static String KEY_FACT="fact";
	final static String KEY_TYPE="type";
	final static String KEY_ID="id";
	final static String KEY_TEXT="text";

	public static final String FACTS_FILE_CAT = "facts_cat";
	public static final String FACTS_FILE_DOG = "facts_dog";

	public static ArrayList<Fact> parseFacts(Context context, String factsFileType){
		ArrayList<Fact> data = null;
		InputStream in = openCountriesFile(context, factsFileType);
		XmlPullParserFactory xmlFactoryObject;
		try {
				xmlFactoryObject = XmlPullParserFactory.newInstance();
				XmlPullParser parser = xmlFactoryObject.newPullParser();
		
				parser.setInput(in, null);
		        int eventType = parser.getEventType();
		        Fact currentFact = null;
		        String inTag = "";
		        String strTagText = null;
		
		        while (eventType != XmlPullParser.END_DOCUMENT){
		        	inTag = parser.getName();
		            switch (eventType){
		                case XmlPullParser.START_DOCUMENT:
		                	data = new ArrayList<Fact>();
		                    break;
		                case XmlPullParser.START_TAG:
		                	if (inTag.equalsIgnoreCase(KEY_FACT))
		                    	currentFact = new Fact();
		                    break;
		                case XmlPullParser.TEXT:
		                	strTagText = parser.getText();
		                	break;
		                case XmlPullParser.END_TAG:
		                	if (inTag.equalsIgnoreCase(KEY_FACT))
		                    	data.add(currentFact);
		                	else if (inTag.equalsIgnoreCase(KEY_TYPE)){
								if (strTagText.equals("cat"))
									currentFact.setFactType(Fact.FACT_TYPE_CAT);
								else
									currentFact.setFactType(Fact.FACT_TYPE_DOG);
							}
		                	else if (inTag.equalsIgnoreCase(KEY_ID))
								currentFact.setFactID(Integer.valueOf(strTagText));
		                	else if (inTag.equalsIgnoreCase(KEY_TEXT))
								currentFact.setFactText(strTagText);
		            		inTag ="";
		                	break;	                    
		            }//switch
		            eventType = parser.next();
		        }//while
			} catch (Exception e) {e.printStackTrace();}
		return data;
	}

	private static InputStream openCountriesFile(Context context, String fileName){
		AssetManager assetManager = context.getAssets();
		InputStream in =null;
		try {
			in = assetManager.open(fileName);
		} catch (IOException e) {e.printStackTrace();}
		return in;
	}
}
