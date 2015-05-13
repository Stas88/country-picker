package com.phone.st.countrieslistexample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phone.st.countrieslistexampl.R;
import com.phone.st.countrieslistexample.dialog.CountryPickerDialog;
import com.phone.st.countrieslistexample.xmlparser.model.Country;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private RelativeLayout countryContainer;
    private Button codeButton;
    private EditText phoneEdit;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String TAG = "Countries";
    public static final String COUNTRY  = "country";
    public static final String NAME  = "name";
    public static final String PHONECODE  = "phoneCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryContainer = (RelativeLayout)findViewById(R.id.your_phone_country_container_rl);
        codeButton = (Button)findViewById(R.id.your_phone_country_code_btn);
        phoneEdit = (EditText) findViewById(R.id.your_phone_phone_number_et);
        List<Country> listOfCountries = new ArrayList<Country>();
        try {
            listOfCountries = parseXML();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Map<String,String> countriesMap = new HashMap<String,String>();
        Log.d(TAG, "parsed " + listOfCountries.size() + " countries");
        for(Country country : listOfCountries) {
            Log.d(TAG, "Country: " + country.getName() + ", phone code: " + country.getPhoneCode());
            countriesMap.put(country.getName(), country.getPhoneCode());
        }
        countryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "countries container click");
                final CountryPickerDialog dialog = CountryPickerDialog.newInstance(getString(R.string.title), countriesMap);
                dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        dialog.dismiss();
                        String country = ((TextView)view).getText().toString();
                        String code = countriesMap.get(country);
                        ((TextView)countryContainer.findViewById(R.id.your_phone_country_name_tv)).setText(country);
                        codeButton.setText(code);
                    }
                });
                dialog.show(getFragmentManager(), "DIALOG");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private List<Country> parseXML() throws XmlPullParserException, IOException {
        List<Country> listOfCountries = new ArrayList<Country>(205);
        String tmp = "";
        try {
            XmlPullParser parser = getResources().getXml(R.xml.countries);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (parser.getEventType()) {
                    // начало документа
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(LOG_TAG, "START_DOCUMENT");
                        break;
                    // начало тэга
                    case XmlPullParser.START_TAG:
                        Log.d(LOG_TAG, "START_TAG: name = " + parser.getName()
                                + ", depth = " + parser.getDepth() + ", attrCount = "
                                + parser.getAttributeCount());
                        if(parser.getName().equals(COUNTRY)) {
                            Country country = new Country();
                            for (int i = 0; i < parser.getAttributeCount(); i++) {
                                String attribute = parser.getAttributeName(i);
                                if(attribute.equals(NAME)) {
                                    country.setName(parser.getAttributeValue(i));
                                } else if(attribute.equals(PHONECODE)) {
                                    country.setPhoneCode(parser.getAttributeValue(i));
                                }
                            }
                            listOfCountries.add(country);
                        }
                        break;
                    // конец тэга
                    case XmlPullParser.END_TAG:
                        break;
                    // содержимое тэга
                    case XmlPullParser.TEXT:
                        break;
                    default:
                        break;
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listOfCountries;
    }
}
