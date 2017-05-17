package com.study.xml.xmlpasrse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Button mBtn;
    String xml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xml =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<list>" +
                        "<person>" +
                        "<name>고남길</name>" +
                        "<phone>01072553466</phone>" +
                        "<addr>서울 송파구</addr>" +
                        "</person>" +

                        "<person>" +
                        "<name>고은별</name>" +
                        "<phone>01096263466</phone>" +
                        "<addr>서울 광진구</addr>" +
                        "</person>" +

                        "<person>" +
                        "<name>최가영</name>" +
                        "<phone>01033798546</phone>" +
                        "<addr>경기도 의정부</addr>" +
                        "</person>" +
                        "</list>";
        ;

        listView = (ListView)findViewById(R.id.listView);




        mBtn = (Button)findViewById(R.id.mbtnParse);
        mBtn.setOnClickListener(mOnClickListener);

    }

    Button.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<Person> list = xmlParser();
            String[] data = new String[list.size()];
            for(int i=0;i<list.size();i++) {
                data[i] = list.get(i).getName()+" "+
                        list.get(i).getPhone()+" "+list.get(i).getAddr();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, data);
            listView.setAdapter(adapter);
        }
    };

    private ArrayList<Person> xmlParser() {
        ArrayList<Person> arrayList = new ArrayList<Person>();
        InputStream is = getResources().openRawResource(R.raw.person);


        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is,"UTF-8");
//            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();
            Person person = null;

            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("person")) {
                            person = new Person();
                        }
                        if(startTag.equals("name")) {
                            person.setName(parser.nextText());
                        }
                        if(startTag.equals("phone")) {
                            person.setPhone(parser.nextText());
                        }
                        if(startTag.equals("addr")) {
                            person.setAddr(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("person")) {
                            arrayList.add(person);
                        }
                        break;
                }
                eventType = parser.next();
            }


        }catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
