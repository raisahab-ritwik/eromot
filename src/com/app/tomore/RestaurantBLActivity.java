package com.app.tomore;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.app.tomore.GeneralBLActivity.ViewHolder;
import com.app.tomore.beans.BLRestaurantModel;
import com.app.tomore.net.YellowPageParse;
import com.app.tomore.net.YellowPageRequest;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class RestaurantBLActivity  extends Activity{
	private DialogActivity dialog;
	private ArrayList<BLRestaurantModel> restlist;
	private DisplayImageOptions otp;
	BLRestaurantModel Restaurant= new BLRestaurantModel();
	ListView listveiew;
	private Activity mContext;
	RestaurantAdapter newsListAdapter;
	private BLRestaurantModel RestaurantItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bianli_restaurant);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		new GetData(RestaurantBLActivity.this,1).execute("");
		mContext = this;
	

	}
	private void BindDataToListView() {
	
		ListView listView = (ListView) findViewById(R.id.bianlirestaurant_listview);
		newsListAdapter = new RestaurantAdapter();
		listView.setAdapter(newsListAdapter);
	}
	private class GetData extends AsyncTask<String, String, String> {
		// private Context mContext;
		private int mType;

		private GetData(Context context, int type) {
			// this.mContext = context;
			this.mType = type;
			dialog = new DialogActivity(context, type);
		}
		@Override
		protected void onPreExecute() {
			if (mType == 1) {
				if (null != dialog && !dialog.isShowing()) {
					dialog.show();
				}
			}
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = null;
			YellowPageRequest request = new YellowPageRequest(RestaurantBLActivity.this);
			try {
				String page ="1";
				String limit="1000";
				String region="-1";
				Log.d("doInBackground", "start request");
				result = request.getRestaurantId(page,limit,region);
				Log.d("doInBackground", "returned");
			}catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			if (null != dialog) {
				dialog.dismiss();
			}
			Log.d("onPostExecute", "postExec state");
			if (result == null || result.equals("")) {
				// show empty alert
			} else {
				restlist = new ArrayList<BLRestaurantModel>();
				HashMap<String, ArrayList<BLRestaurantModel>> RestMap = new HashMap<String, ArrayList<BLRestaurantModel>>();
				RestMap = new YellowPageParse().parseRestaurantResponse(result);
				restlist = RestMap.get("scarborough");
				try {
				
					//restlist = new YellowPageParse().parseRestaurantResponse(result);
					BindDataToListView();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				if(restlist !=null){
					Intent intent =new Intent(RestaurantBLActivity.this,MyCameraActivity.class);
					intent.putExtra("menuList",(Serializable) restlist);
					// startActivity(intent);
				}
				else{
					// show empty alert
				}
			
			}
		}
		
	}
	
	class ViewHolder {
		private TextView Title;
	    private ImageView Image;
	}
	
	public class RestaurantAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return restlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return restlist.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 RestaurantItem = (BLRestaurantModel) getItem(position);
			ViewHolder viewHolder = new ViewHolder();
			final String hotlevel= RestaurantItem.getHotLevel();
				if(hotlevel.equals("9")){
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.hotlv9_restaurant_listview, null);
							//viewHolder.Title = (TextView) convertView.findViewById(R.id.RestText);
							//viewHolder.Image = (ImageView) convertView.findViewById(R.id.RestImage);
				}
				else{
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.blrestaurantlist, null);
							//viewHolder.Title = (TextView) convertView.findViewById(R.id.RestText);
							//viewHolder.Image = (ImageView) convertView.findViewById(R.id.RestImage);
				}
				viewHolder.Image= (ImageView) convertView.findViewById(R.id.RestImage);
				ImageLoader.getInstance().displayImage(RestaurantItem.getImage(),
						viewHolder.Image,otp);
				viewHolder.Title = (TextView) convertView.findViewById(R.id.RestText);
				viewHolder.Title.setText(RestaurantItem.getTitle());

			
			
			return convertView;
		}

	}
}

		
		


	
	