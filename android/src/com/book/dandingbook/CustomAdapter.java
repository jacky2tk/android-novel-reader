package com.book.dandingbook;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {	
	private static final String TAG = "Danding_CustomAdapter";
	
	//建立項目暫存ArrayList<LinkedHashMap<String,Object>, 方便之後取用
	private ArrayList<LinkedHashMap<String,Object>> TypeList = 
			new ArrayList<LinkedHashMap<String,Object>>();
	
	//建立項目常數, 方便判別
	public static final int SEPARATOR = 0;
	public static final int LIST_ITEM = 1;
	public static final int LAB_ITEM = 2;
	public static final int TXT_ITEM = 3;
	public static final int IMAGE_ITEM = 4;
	public static final int BUTTON_ITEM = 5;
	
	/*Constructor*/
	public CustomAdapter() {
    }
	
	//建立LayoutInflater讓layout xml可以轉成View Objects
	LayoutInflater mInflater;
	public CustomAdapter(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }
	
	/*methods*/
	//加入分隔項方法
	public void addSeparator(String ItemText) {
		View SeparatorView = mInflater.inflate(R.layout.custom_separator, null);
		
		LinkedHashMap<String,Object> ItemList =
				new LinkedHashMap<String,Object>();
		
		int id = TypeList.size();
		ItemList.put("ID", String.valueOf(id));
		ItemList.put("ItemType", SEPARATOR);
		ItemList.put("VIEW", SeparatorView);
		ItemList.put("ItemText", ItemText);
		
		TypeList.add(ItemList);
	}
	
	//加入ListItem方法
	public View addListItem2(String TitleText, String ContentText) {
		View ItemView = mInflater.inflate(android.R.layout.simple_list_item_2, null);
		
		LinkedHashMap<String,Object> ItemList =
				new LinkedHashMap<String,Object>();
		
		int id = TypeList.size();
		ItemList.put("ID", String.valueOf(id));
		ItemList.put("ItemType", LIST_ITEM);
		ItemList.put("VIEW", ItemView);
		ItemList.put("TitleText", TitleText);
		ItemList.put("ContentText", ContentText);
		
		TypeList.add(ItemList);
		return ItemView;
	}
	
	//加入TextView方法
	public View addLabItem(String ItemText) {
		View ItemView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
		
		LinkedHashMap<String,Object> ItemList =
				new LinkedHashMap<String,Object>();
		
		int id = TypeList.size();
		ItemList.put("ID", String.valueOf(id));
		ItemList.put("ItemType", LAB_ITEM);
		ItemList.put("VIEW", ItemView);
		ItemList.put("ItemText", ItemText);
		
		TypeList.add(ItemList);
		return ItemView;
	}
	
	//加入EditView方法
	public View addEditItem(String ItemText, String TxtText) {
		View ItemView = mInflater.inflate(R.layout.custom_edittext_item, null);
		
		LinkedHashMap<String,Object> ItemList =
				new LinkedHashMap<String,Object>();
		
		int id = TypeList.size();
		ItemList.put("ID", String.valueOf(id));
		ItemList.put("ItemType", TXT_ITEM);
		ItemList.put("VIEW", ItemView);
		ItemList.put("ItemText", ItemText);
		ItemList.put("TxtText", TxtText);
		
		TypeList.add(ItemList);
		return ItemView;
	}
	
	//加入ImageView方法
	public void addImage(String ImagePath) {
		Log.d(TAG, "Enter addImage function --> " + ImagePath);
		View ItemView = mInflater.inflate(R.layout.custom_image, null);
		
		LinkedHashMap<String,Object> ItemList =
				new LinkedHashMap<String,Object>();
		
		int id = TypeList.size();
		ItemList.put("ID", String.valueOf(id));
		ItemList.put("ItemType", IMAGE_ITEM);
		ItemList.put("VIEW", ItemView);
		ItemList.put("ImagePath", ImagePath);
		Log.d(TAG, "ImagePath = " + ImagePath);
		
		TypeList.add(ItemList);
	}
	
	//加入Button方法
	public void addButton(String ItemText, OnClickListener ClickLis) {
		Log.d(TAG, "Enter addButton function --> " + ItemText);
		View ItemView = mInflater.inflate(R.layout.custom_button, null);
		
		LinkedHashMap<String,Object> ItemList =
				new LinkedHashMap<String,Object>();
		
		int id = TypeList.size();
		ItemList.put("ID", String.valueOf(id));
		ItemList.put("ItemType", BUTTON_ITEM);
		ItemList.put("VIEW", ItemView);
		ItemList.put("ItemText", ItemText);
		ItemList.put("ClickLis", ClickLis);
		
		TypeList.add(ItemList);
	}

	/*BaseAdapter Implement Methods*/
	public int getCount() {
		return TypeList.size();
	}

	public Object getItem(int position) {
		return TypeList.get(position);
	}

	public long getItemId(int position) {
		return Long.valueOf(String.valueOf(TypeList.get(position).get("ID")));
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "Enter getView");
		//取出儲目前項目
		LinkedHashMap<String,Object> ItemInfo = TypeList.get(position);
		
		//取出目前項目的類型
		int type = (Integer)ItemInfo.get("ItemType");
		convertView = (View)ItemInfo.get("VIEW");

        //ListView listView = (ListView)parent;
        
        //取出目前項目的VIEW
		switch (type) {
			case SEPARATOR:
				TextView Sep = (TextView)convertView.findViewById(R.id.textSeparator);
				Sep.setText((String)ItemInfo.get("ItemText"));
				break;
				
			case LIST_ITEM:
				TextView Lab_Header = (TextView)convertView.findViewById(android.R.id.text1);
				Lab_Header.setText((String)ItemInfo.get("TitleText"));
				TextView Lab_Content = (TextView)convertView.findViewById(android.R.id.text2);
				Lab_Content.setText((String)ItemInfo.get("ContentText"));
				break;
				
			case LAB_ITEM:
				TextView Lab = (TextView)convertView.findViewById(android.R.id.text1);
				Lab.setText((String)ItemInfo.get("ItemText"));
				break;
				
			case TXT_ITEM:
				TextView txtView = (TextView)convertView.findViewById(R.id.Lab_ItemSettings);
				txtView.setText((String)ItemInfo.get("ItemText"));
				EditText edit = (EditText)convertView.findViewById(R.id.Txt_ItemSettings);
				edit.setText((String)ItemInfo.get("TxtText"));				
				break;
				
			case IMAGE_ITEM:
				Log.d(TAG, "4444");
				ImageView imageView = (ImageView)convertView.findViewById(R.id.ItemImage);
				try {
					//img_Show.setImageResource(R.drawable.shin);
					Bitmap bm = BitmapFactory.decodeFile(Network.getSDPath("").concat((String)ItemInfo.get("ImagePath")));
					imageView.setImageBitmap(bm);
				} catch (Exception e) {
					Log.e(TAG, "Image Setting Err: " + e.getMessage());
					System.out.println(e.getMessage());
				}
				break;		
				
			case BUTTON_ITEM:
				Log.d(TAG, "555");
				Button button = (Button)convertView.findViewById(R.id.ItemButton);
				button.setText((String)ItemInfo.get("ItemText"));
				button.setOnClickListener((OnClickListener)ItemInfo.get("ClickLis"));
				break;	
				
		}

        //判斷類型

        //傳回VIEW
		return convertView;
	}
	
	public void clear() {
		TypeList.clear();
	}

}
