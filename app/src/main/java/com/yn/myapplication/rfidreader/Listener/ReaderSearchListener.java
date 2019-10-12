package com.yn.myapplication.rfidreader.Listener;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.yn.myapplication.R;
import com.yn.myapplication.rfidreader.ReaderActivity;
import com.yn.myapplication.rfidreader.customViews.DrawableClickListener;
import com.yn.myapplication.util.LoggerUtil;

public class ReaderSearchListener implements DrawableClickListener {

	private static String TAG = "ReaderSearchListener";

	private static JmDNS jmdns;
	private static ReaderActivity mReaderActivity;
	private static WifiManager wifiManager;
	private static InetAddress wifiAddress;
	private static Map<String, String> discoveredReaders = new HashMap<String, String>();
	private static final String WIFI_SERVICE = "wifi";

	
	private static Dialog readersDialog;
	private static LinearLayout readerssSearchView;
	private static ListView readerListView;
	private static LayoutInflater inflater;
	private static EditText ntReaderEditText;
	private static ProgressDialog pDialog;
	private static ProgressBar loadingView;

	@SuppressLint("WrongConstant")
	public ReaderSearchListener(ReaderActivity readerActivity) {
		mReaderActivity = readerActivity;
		wifiManager = (WifiManager) mReaderActivity.getApplicationContext().getSystemService(WIFI_SERVICE);
		readersDialog = new Dialog(mReaderActivity);
		pDialog = new ProgressDialog(mReaderActivity);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		findAllViewsById();
	 
		readersDialog.setContentView(readerssSearchView);
		readersDialog.setCancelable(true);
		readersDialog.setTitle("Discover Readers");
		readerListView.setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
		    	String selectedReader = (String) ((TextView)av.getChildAt(pos)).getText();
		    	if(!selectedReader.equalsIgnoreCase("No readers found")){
		    		ntReaderEditText.setText(selectedReader);
			    	ntReaderEditText.setTag(discoveredReaders.get(selectedReader));	
		    	}		    	
		    	readersDialog.dismiss();
		    }
		});
	}

	private void findAllViewsById() {
		inflater = (LayoutInflater) mReaderActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		readerssSearchView = (LinearLayout) inflater.inflate(
				R.layout.readers_search_diolog, null, true);
		readerListView =(ListView) readerssSearchView.findViewById(R.id.readers_list);
		ntReaderEditText = (EditText) mReaderActivity.findViewById(R.id.search_edit_text);
		 loadingView =(ProgressBar) readerssSearchView.findViewById(R.id.loadingBar);
	}

	@Override
	public void onClick(DrawableClickListener.DrawablePosition target) {
		switch (target) {
        case LEFT:
        	try {
    			int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
    			String ip = intToIp(ipAddress);
    			wifiAddress = InetAddress.getByName(ip);
    			inflater.inflate(R.layout.readers_search_diolog, null, true);
    			ReadersSearchThread readersSearchThread = new ReadersSearchThread();
    			readersSearchThread.execute();
    			
    		} catch (Exception ex) {
    			LoggerUtil.error(TAG, "Exception", ex);
    		}
            break;

        case RIGHT:
        	ntReaderEditText.getText().clear();
            break;
        default:
            break;
		}
		
	}

	public static String intToIp(int i) {
		return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
				+ ((i >> 8) & 0xFF) + "." + (i & 0xFF);
	}

	private static class ReadersSearchThread extends
			AsyncTask<Void, Void, Map<String, String>> {
		@Override
		protected void onPreExecute() {
			loadingView.setVisibility(View.VISIBLE);
			
			readersDialog.show();
		}

		@Override
		protected Map<String, String> doInBackground(Void... arg0) {
			try {
				
				jmdns = JmDNS.create(wifiAddress);
				String[] serviceType = { "_llrp._tcp.local.","_m4api._udp.local." };
				SampleListener listener = new SampleListener();

				for (String service : serviceType) {
					jmdns.addServiceListener(service, listener);
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				readersDialog.dismiss();
			}

			return discoveredReaders;
		}

		@Override
		protected void onPostExecute(Map<String, String> discovedReaders) {
			loadingView.setVisibility(View.GONE);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mReaderActivity,
					android.R.layout.simple_list_item_1);
		   Set<String> readersSet = discovedReaders.keySet();
		   if(readersSet.size()==0){
			   adapter.add("No readers found");
		   }else{
			   for (Iterator reader = readersSet.iterator(); reader.hasNext();)
			   {
					String readerName = (String) reader.next();
					adapter.add(readerName);
			   } 
		   }
		   
			readerListView.setAdapter(adapter);
			

			readersDialog.show();
		}

		static class SampleListener implements ServiceListener {

			@Override
			public void serviceAdded(ServiceEvent event) {
				jmdns.requestServiceInfo(event.getType(), event.getName());
			}

			@Override
			public void serviceRemoved(ServiceEvent event) {
				System.out.println("Service removed : " + event.getName() + "."
						+ event.getType());
			}

			@Override
			public void serviceResolved(ServiceEvent event) {
				System.out.println("serviceResolved : "
						+ event.getInfo().getAddress().getHostAddress() + " - "
						+ event.getType());

				discoveredReaders.put(event.getName(), event.getInfo()
						.getAddress().getHostAddress());
			}
		}
	}

	
}
