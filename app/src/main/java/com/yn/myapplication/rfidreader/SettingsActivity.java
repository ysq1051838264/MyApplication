package com.yn.myapplication.rfidreader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class SettingsActivity extends Activity {

	private static Button homeButton = null ;

	private ExpandableListAdapter adapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_settings);
//		homeButton = (Button)findViewById(R.id.btn_back_main);
//		homeButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View view) {
//				
//				Intent intent = new Intent(view.getContext(), ReaderActivity.class);
//                startActivity(intent);
//				
//			}
//		});
//		
//		ExpandableListView expandableList = (ExpandableListView) findViewById(R.id.expandableListView1);
//
//		LayoutInflater infalInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View connect = infalInflater.inflate(R.layout.connect, null);
//		View statistics = infalInflater.inflate(R.layout.statistics, null);
//		View firmware = infalInflater.inflate(R.layout.firmware, null);
//		ArrayList<View> ChildViews = new ArrayList<View>();
//		ChildViews.add(connect);
//		ChildViews.add(statistics);
//		ChildViews.add(firmware);
//
//		String[] groups = { "Connect", "Statistics", "Firmware" };
//		adapter = new ExpandableListAdapter(this, groups, ChildViews);
//
//		// Set this blank adapter to the list view
//		expandableList.setAdapter(adapter);
	}
}
