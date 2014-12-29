package com.app.tomore;

import com.app.tomore.beans.GeneralBLModel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GeneralBLDetailActivity extends Activity {

	private DialogActivity dialog;
	private GeneralBLModel BLModel;
	private GoogleMap map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.general_bl_detail_layout);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		getWindow().setSoftInputMode(  WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Intent intent = getIntent();
		BLModel = (GeneralBLModel) intent.getSerializableExtra("BLdata");
		BindData();
	}
	
	
	private void BindData()
	{
		TextView languageView = (TextView) getWindow().getDecorView()
				.findViewById(R.id.serviceLanguage);
		TextView contact = (TextView) getWindow().getDecorView()
				.findViewById(R.id.contact);
		TextView infotext = (TextView) getWindow().getDecorView()
				.findViewById(R.id.infotext);
		TextView detailinfotext = (TextView) getWindow().getDecorView()
				.findViewById(R.id.detailinfotext);
		TextView addressinfotext = (TextView) getWindow().getDecorView()
				.findViewById(R.id.addressinfotext);
		TextView phoneinfo = (TextView) getWindow().getDecorView()
				.findViewById(R.id.phoneinfo);
		TextView phoneinfo2 = (TextView) getWindow().getDecorView()
				.findViewById(R.id.phoneinfo2);
		EditText messagetosent = (EditText) getWindow().getDecorView()
				.findViewById(R.id.messagetosent);
		TextView sendlabel = (TextView) getWindow().getDecorView()
				.findViewById(R.id.sendlabel);
		
		String servicelanuage = getString (R.string.servicelanuage);
		String contacttext = getString (R.string.contact);
		String detailinfo_Chinese = getString (R.string.detailinfo);
		String addressinfo_Chinese = getString (R.string.addressinfo);
		String phone = getString (R.string.phone);
		String sent = getString (R.string.sent);
		
		String language = "";
        if(BLModel.getLanguage_C().equals("1"))
        {
        	language = getString(R.string.cantonese) ;
        }
        
        if(BLModel.getLanguage_E().equals("1")) 
        {
        	if(BLModel.getLanguage_C().equals("1")){
        			language = language + "/" + getString(R.string.english);;
        	}
        	else{
        		language = getString(R.string.english);;
        	}
        }
       
        if(BLModel.getLanguage_M().equals("1")) 
        {
        	if(BLModel.getLanguage_C().equals("1") || BLModel.getLanguage_E().equals("1"))
        	{
        		language = language + "/" + getString(R.string.mandarin);
        	}
        	else{
        		language = getString(R.string.mandarin);
        	}
        }
        
        languageView.setText(servicelanuage +":" + language);
        contact.setText(contacttext + ":   " +BLModel.getContact());
        infotext.setText(detailinfo_Chinese);
        detailinfotext.setText(BLModel.getContent());
        addressinfotext.setText(addressinfo_Chinese);
        phoneinfo.setText(phone + ": " + BLModel.getPhone1());
        if(BLModel.getPhone2().equals("")){
        	phoneinfo2.setVisibility(View.INVISIBLE);
        }
        else{
        	phoneinfo2.setText(phone + ": " + BLModel.getPhone2());
        }
        sendlabel.setText(sent);
        
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        
    /*(    if (BLModel.getLatitude() != null && BLModel.getLongtitude() != null)
        {
	        final LatLng location = new LatLng(Double.parseDouble(BLModel.getLatitude()), Double.parseDouble(BLModel.getLongtitude()));
	        
	        Marker go_to_location = map.addMarker(new MarkerOptions().position(location)
	                .title("current_location"));
	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
	
	        // Zoom in, animating the camera.
	        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null); 
        }*/
        try {
	        final LatLng location = new LatLng(Double.parseDouble(BLModel.getLatitude()), Double.parseDouble(BLModel.getLongitude()));
	        
	        Marker go_to_location = map.addMarker(new MarkerOptions().position(location)
	                .title(BLModel.getTitle()));
	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
	
	        // Zoom in, animating the camera.
	        map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null); 
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }
}

