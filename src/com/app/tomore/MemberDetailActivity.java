package com.app.tomore;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.app.tomore.beans.CardModel;
import com.app.tomore.beans.CommonModel;
import com.app.tomore.net.CardsRequest;
import com.app.tomore.net.ToMoreParse;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MemberDetailActivity extends Activity {

	private DialogActivity dialog;
	private CardModel cardItem;
	private ImageView frontImageView;
	private ImageView backImageView;
	private ImageView barcodeImageView;
	private Button btnFrontEdit;
	private Button btnBackEdit;
	private Button btnGenerateBarcode;
	private Button btnSubmit;
	private Button btnEdit;
	private EditText editTitle;
	private EditText editDes;
	private EditText editBarcode;
	private TextView barcodeValueLable;
	private TextView barcodeLable;
	private TextView titleLable;
	private TextView desLable;
	private Bitmap bitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		cardItem = (CardModel) intent.getSerializableExtra("cardList");
		setContentView(R.layout.member_detail);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);

		frontImageView = (ImageView) getWindow().getDecorView().findViewById(
				R.id.frontViewImg);
		backImageView = (ImageView) getWindow().getDecorView().findViewById(
				R.id.backViewImg);
		barcodeImageView = (ImageView) getWindow().getDecorView().findViewById(
				R.id.barcodeImg);

		btnFrontEdit = (Button) getWindow().getDecorView().findViewById(
				R.id.btnFrontEdit);
		btnBackEdit = (Button) getWindow().getDecorView().findViewById(
				R.id.btnBackEdit);
		btnGenerateBarcode = (Button) getWindow().getDecorView().findViewById(
				R.id.btnGenerateBarcode);
		btnSubmit = (Button) getWindow().getDecorView().findViewById(
				R.id.btnSubmit);
		editTitle = (EditText) getWindow().getDecorView().findViewById(
				R.id.editTitle);
		editDes = (EditText) getWindow().getDecorView().findViewById(
				R.id.editDes);
		editBarcode = (EditText) getWindow().getDecorView().findViewById(
				R.id.editBarcode);
		barcodeValueLable = (TextView) getWindow().getDecorView().findViewById(
				R.id.barcodeValueLable);
		barcodeLable = (TextView) getWindow().getDecorView().findViewById(
				R.id.barcodeLable);
		titleLable = (TextView) getWindow().getDecorView().findViewById(
				R.id.titleLable);
		desLable = (TextView) getWindow().getDecorView().findViewById(
				R.id.desLable);
		
		RelativeLayout rl = (RelativeLayout) getWindow().getDecorView()
				.findViewById(R.id.bar_title_member_add);
		
		btnEdit = (Button) rl.findViewById(R.id.bar_title_bt_edit);
		
		try {
			bitmap = encodeAsBitmap(cardItem.getCardBarcode(),
					BarcodeFormat.CODE_128, 600, 250);
			barcodeImageView.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		BindData();
		new GetData(MemberDetailActivity.this, 1).execute("");		

		btnEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (cardItem == null) {
					return;
				}

				if (btnEdit.getText().equals("取消")) {

					btnFrontEdit.setVisibility(View.INVISIBLE);
					btnBackEdit.setVisibility(View.INVISIBLE);
					btnGenerateBarcode.setVisibility(View.INVISIBLE);
					btnSubmit.setVisibility(View.INVISIBLE);

					if (cardItem.getCardType().equals("1")) {
						editTitle.setVisibility(View.INVISIBLE);
						editDes.setVisibility(View.INVISIBLE);
						editBarcode.setVisibility(View.INVISIBLE);
						titleLable.setVisibility(View.INVISIBLE);
						desLable.setVisibility(View.INVISIBLE);
						barcodeLable.setVisibility(View.INVISIBLE);
					}

					editTitle.setEnabled(false);
					editDes.setEnabled(false);
					editBarcode.setEnabled(false);

					btnEdit.setText("编辑");
					return;
				} else {

					btnFrontEdit.setVisibility(View.VISIBLE);
					btnBackEdit.setVisibility(View.VISIBLE);
					btnGenerateBarcode.setVisibility(View.VISIBLE);
					btnSubmit.setVisibility(View.VISIBLE);

					
					editTitle.setVisibility(View.VISIBLE);
					editDes.setVisibility(View.VISIBLE);
					editBarcode.setVisibility(View.VISIBLE);
					titleLable.setVisibility(View.VISIBLE);
					desLable.setVisibility(View.VISIBLE);
					barcodeLable.setVisibility(View.VISIBLE);

					editTitle.setEnabled(true);
					editDes.setEnabled(true);
					editBarcode.setEnabled(true);

					btnEdit.setText("取消");
				}

			}
		});

		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (cardItem == null) {
					return;
				}

				String cardID, cardTitle, cardBarcode, cardDes;

				cardID = cardItem.getCardID();
				cardTitle = editTitle.getText().toString();
				cardDes = editDes.getText().toString();
				cardBarcode = editBarcode.getText().toString();

				// need image uploader

				String result = null;
				CardsRequest request = new CardsRequest(
						MemberDetailActivity.this);

				try {
					Log.d("doInBackground", "start request");
					result = request.updateCardInfo(cardID, cardTitle,
							cardBarcode, cardDes);
					Log.d("doInBackground", "returned");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				CommonModel returnResult = new ToMoreParse()
						.CommonPares(result);
				// result alert

			}
		});

		final Button btnBack = (Button) rl
				.findViewById(R.id.bar_title_bt_member);

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		frontImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// loadPhoto(frontImageView,200,200);
				Intent intent = new Intent(MemberDetailActivity.this,
						SpaceImageDetailActivity.class);
				// intent.putExtra("images", (ArrayList<String>) datas);
				// intent.putExtra("position", position);
				int[] location = new int[2];
				frontImageView.getLocationOnScreen(location);
				intent.putExtra("locationX", location[0]);
				intent.putExtra("locationY", location[1]);

				intent.putExtra("Url", cardItem.getFrontViewImage());

				intent.putExtra("width", frontImageView.getWidth());
				intent.putExtra("height", frontImageView.getHeight());
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});

		backImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// loadPhoto(frontImageView,200,200);
				Intent intent = new Intent(MemberDetailActivity.this,
						SpaceImageDetailActivity.class);
				// intent.putExtra("images", (ArrayList<String>) datas);
				// intent.putExtra("position", position);
				int[] location = new int[2];
				frontImageView.getLocationOnScreen(location);
				intent.putExtra("locationX", location[0]);
				intent.putExtra("locationY", location[1]);

				intent.putExtra("Url", cardItem.getBackViewImage());

				intent.putExtra("width", backImageView.getWidth());
				intent.putExtra("height", backImageView.getHeight());
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});

		btnGenerateBarcode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String barcode_data = editBarcode.getText().toString();
				try {
					bitmap = encodeAsBitmap(barcode_data,
							BarcodeFormat.CODE_128, 600, 250);
					barcodeImageView.setImageBitmap(bitmap);
					barcodeValueLable.setText(barcode_data);
				} catch (WriterException e) {
					e.printStackTrace();
				}
			}
		});

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
			String result = null;
			// try {
			Log.d("doInBackground", "start request");

			Log.d("doInBackground", "returned");
			// }
			// catch (IOException e) {
			// e.printStackTrace();
			// } catch (TimeoutException e) {
			// e.printStackTrace();
			// }

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
				// cardList = new ArrayList<CardModel>();
				try {
					// cardList = new CardsParse().parseCardResponse(result);

				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				if (cardItem != null) {
					// Intent intent = new Intent(MemberDetailActivity.this,
					// MyCameraActivity.class); // fake redirect..
					// intent.putExtra("cardList", (Serializable) cardList);
					// startActivity(intent);
				} else {
					// show empty alert
				}
			}
		}
	}

	private void BindData() {

		// Picasso.with(MemberDetailActivity.this)
		// .load(cardItem.getFrontViewImage()).resize(550, 280)
		// .into(frontImageView);
		// Picasso.with(MemberDetailActivity.this)
		// .load(cardItem.getBackViewImage()).resize(550, 280)
		// .into(backImageView);

		final String frontImageUrl = cardItem.getFrontViewImage();
		frontImageView.setTag(frontImageUrl);
		ImageLoader.getInstance().loadImage(frontImageUrl,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						ImageView imageViewByTag = (ImageView) getWindow()
								.getDecorView().findViewWithTag(imageUri);
						if (imageViewByTag != null) {
							// imageViewByTag.setImageBitmap(loadedImage);

							int bwidth = loadedImage.getWidth();
							int bheight = loadedImage.getHeight();
							int swidth = imageViewByTag.getWidth();
							int sheight = imageViewByTag.getHeight();
							int new_width = swidth;
							int new_height = (int) Math.floor((double) bheight
									* ((double) new_width / (double) bwidth));
							Bitmap newbitMap = Bitmap.createScaledBitmap(
									loadedImage, new_width, new_height, true);
							imageViewByTag.setImageBitmap(newbitMap);
						}
					}
				});

		final String backImageUrl = cardItem.getBackViewImage();
		backImageView.setTag(backImageUrl);
		ImageLoader.getInstance().loadImage(backImageUrl,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						ImageView imageViewByTag = (ImageView) getWindow()
								.getDecorView().findViewWithTag(imageUri);
						if (imageViewByTag != null) {
							// imageViewByTag.setImageBitmap(loadedImage);

							int bwidth = loadedImage.getWidth();
							int bheight = loadedImage.getHeight();
							int swidth = imageViewByTag.getWidth();
							int sheight = imageViewByTag.getHeight();
							int new_width = swidth;
							int new_height = (int) Math.floor((double) bheight
									* ((double) new_width / (double) bwidth));
							Bitmap newbitMap = Bitmap.createScaledBitmap(
									loadedImage, new_width, new_height, true);
							imageViewByTag.setImageBitmap(newbitMap);
						}
					}
				});

		btnFrontEdit.setVisibility(View.INVISIBLE);
		btnBackEdit.setVisibility(View.INVISIBLE);
		btnGenerateBarcode.setVisibility(View.INVISIBLE);
		btnSubmit.setVisibility(View.INVISIBLE);

		editTitle.setText(cardItem.getCardTitle());
		editTitle.setEnabled(false);
		editDes.setText(cardItem.getCardDes());
		editDes.setEnabled(false);
		editBarcode.setText(cardItem.getCardBarcode());
		editBarcode.setEnabled(false);
		barcodeValueLable.setText(cardItem.getCardBarcode());

		if (cardItem.getCardType().equals("1")) {
			editTitle.setVisibility(View.INVISIBLE);
			editDes.setVisibility(View.INVISIBLE);
			editBarcode.setVisibility(View.INVISIBLE);
			titleLable.setVisibility(View.INVISIBLE);
			desLable.setVisibility(View.INVISIBLE);
			barcodeLable.setVisibility(View.INVISIBLE);
		}
		else if(cardItem.getCardType().equals("0"))
		{
			btnEdit.setVisibility(View.INVISIBLE);
		}

	}

	/**************************************************************
	 * getting from com.google.zxing.client.android.encode.QRCodeEncoder
	 * 
	 * See the sites below http://code.google.com/p/zxing/
	 * http://code.google.com
	 * /p/zxing/source/browse/trunk/android/src/com/google/
	 * zxing/client/android/encode/EncodeActivity.java
	 * http://code.google.com/p/zxing
	 * /source/browse/trunk/android/src/com/google/
	 * zxing/client/android/encode/QRCodeEncoder.java
	 */

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width,
			int img_height) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result;
		try {
			result = writer.encode(contentsToEncode, format, img_width,
					img_height, hints);
		} catch (IllegalArgumentException iae) {
			// Unsupported format
			return null;
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}

}
