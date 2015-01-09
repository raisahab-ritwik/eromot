package com.app.tomore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.app.tomore.adapters.FansAdapter;
import com.app.tomore.beans.FansModel;
import com.app.tomore.net.UserCenterParse;
import com.app.tomore.net.UserCenterRequest;
import com.app.tomore.utils.PullToRefreshListView;
import com.app.tomore.utils.ToastUtils;
import com.google.gson.JsonSyntaxException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MainFansActivity extends Activity {

	private DialogActivity dialog;
	private Activity mContext;
	private ArrayList<FansModel> fansList;
	FansAdapter fansListAdapter;
	private PullToRefreshListView mListView;
	private boolean onRefresh = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_fans);
		mContext = this;
		new MyFans(MainFansActivity.this, 1).execute("");
	}
	
	private class MyFans extends AsyncTask<String, String, String> {
		private int mType;

		private MyFans(Context context, int type) {
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
			String result = null;
			UserCenterRequest request = new UserCenterRequest(MainFansActivity.this);
			//memberID, viewerID, limit, page
			try {
				result = request.getFansRequest("25", "34", "10", "1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;			
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (null != dialog) {
				dialog.dismiss();
			}
//			mListView.onRefreshComplete();
			Log.d("onPostExecute", "postExec state");
			if (result == null || result.equals("")) {
				ToastUtils.showToast(mContext, "�б�Ϊ��");
			} else {
				if(fansList!=null && fansList.size()>0)
				{
					fansList.clear();
				}
				else
				{
					fansList = new ArrayList<FansModel>();
				}
				try {
					fansList = new UserCenterParse().parseFansResponse(result);
					BindDataToListView();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	private void BindDataToListView(){
		if (onRefresh) {
			onRefresh = false;
		}
		if (fansListAdapter == null) {
			fansListAdapter = new FansAdapter();
			mListView.setAdapter(fansListAdapter);
		} else {
			fansListAdapter.notifyDataSetChanged();
		}
		if (fansList != null && fansList.size() > 0) {
	//		showDataUi();
		} else {
	//		showNoDataUi();
		}
	}
}
