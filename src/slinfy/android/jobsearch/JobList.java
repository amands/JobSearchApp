package slinfy.android.jobsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class JobList extends ListActivity {

	// XML node keys
	static final String KEY_ITEM = "result"; // parent node
	static final String KEY_jobTitle = "jobtitle";
	static final String KEY_company = "company";
	static final String KEY_city = "city";
	static final String KEY_state = "state";
	static final String KEY_country = "country";
	static final String KEY_forLoc = "formattedLocation";
	static final String KEY_source = "source";
	static final String KEY_date = "date";
	static final String KEY_snippet = "snippet";
	static final String KEY_url = "url";
	static final String KEY_onMouseDown = "onmousedown";
	static final String KEY_jobKey = "jobkey";

	private String query, loc;

	ProgressDialog dialog;

	private ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
	ListAdapter adapter;

	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dialog = ProgressDialog.show(this, "Loading Data....", "Please wait",
				true, false);

		query = getIntent().getStringExtra("q");
		loc = getIntent().getStringExtra("l");

		getListView().setBackgroundResource(R.drawable.bg);
		getListView().setCacheColorHint(Color.TRANSPARENT);

		new GetData().execute("");

		// Adding menuItems to ListView
		adapter = new SimpleAdapter(this, menuItems, R.layout.job_list,
				new String[] { KEY_jobTitle, KEY_company, KEY_city, KEY_state,
						KEY_country }, new int[] { R.id.tv1, R.id.tv2,
						R.id.tv3, R.id.tv4, R.id.tv5 });

	}

	public class GetData extends AsyncTask<String, Integer, String> {

		String result = "fail";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			System.out.println("RecipeDetail.GetData.doInBackground()");

			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(
						"http://api.indeed.com/ads/apisearch?publisher=4290558872696563&q="
								+ query + "&l=" + loc);

				try {
					// Execute HTTP Request
					HttpResponse response = httpclient.execute(httpget);

					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "iso-8859-1"), 8);
					StringBuilder sb = new StringBuilder();
					result = "";
					while ((result = reader.readLine()) != null) {
						sb.append(result);
					}
					is.close();
					result = sb.toString();

					System.out.println("Result::" + result);

				} catch (ClientProtocolException e) {
				} catch (IOException e) {
				}
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			System.out
					.println("Registeration Screen.GetData.onProgressUpdate()");

			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result1) {

			super.onPostExecute(result1);

			System.out.println("JobList.GetData.onPostExecute()");

			Document doc = getDomElement(result); // getting DOM element
			NodeList nl = doc.getElementsByTagName(KEY_ITEM);

			// looping through all item nodes <item>
			for (int i = 0; i < nl.getLength(); i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				Element e = (Element) nl.item(i);
				// adding each child node to HashMap key => value
				map.put(KEY_ITEM, getValue(e, KEY_ITEM));
				map.put(KEY_jobTitle, getValue(e, KEY_jobTitle));
				map.put(KEY_company, getValue(e, KEY_company));
				map.put(KEY_city, getValue(e, KEY_city));
				map.put(KEY_state, getValue(e, KEY_state));
				map.put(KEY_country, getValue(e, KEY_country));
				map.put(KEY_forLoc, getValue(e, KEY_forLoc));
				map.put(KEY_source, getValue(e, KEY_source));
				map.put(KEY_date, getValue(e, KEY_date));
				map.put(KEY_snippet, getValue(e, KEY_snippet));
				map.put(KEY_url, getValue(e, KEY_url));
				map.put(KEY_onMouseDown, getValue(e, KEY_onMouseDown));
				map.put(KEY_jobKey, getValue(e, KEY_jobKey));

				// adding HashList to ArrayList
				menuItems.add(map);
			}

			setListAdapter(adapter);

			dialog.dismiss();

		}

		protected Document getDomElement(String xml) {
			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {

				DocumentBuilder db = dbf.newDocumentBuilder();

				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = db.parse(is);

			} catch (ParserConfigurationException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			} catch (SAXException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			} catch (IOException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			}
			// return DOM
			return doc;
		}

		protected String getValue(Element item, String str) {
			NodeList n = item.getElementsByTagName(str);
			return this.getElementValue(n.item(0));
		}

		protected final String getElementValue(Node elem) {
			Node child;
			if (elem != null) {
				if (elem.hasChildNodes()) {
					for (child = elem.getFirstChild(); child != null; child = child
							.getNextSibling()) {
						if (child.getNodeType() == Node.TEXT_NODE) {
							return child.getNodeValue();
						}
					}
				}
			}
			return "";
		}

	}
}
