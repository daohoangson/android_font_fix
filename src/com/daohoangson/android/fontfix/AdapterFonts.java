package com.daohoangson.android.fontfix;

import com.w8iig.trafficfines.R;
import com.w8iig.trafficfines.AdapterData.ViewHolder;
import com.w8iig.trafficfines.data.DataAbstract.FineValues;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class AdapterFonts extends ArrayAdapter {
	
	public AdapterFonts(Context context) {
		super(context, 0);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = null;
		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_fine, parent, false);
			holder = new ViewHolder();
			holder.cbMark = (CheckBox) row.findViewById(R.id.cb_fine_mark);
			holder.txtName = (TextView) row.findViewById(R.id.txt_fine_name);
			holder.txtDescription = (TextView) row
					.findViewById(R.id.txt_fine_description);
			holder.txtValueHigh = (TextView) row
					.findViewById(R.id.txt_fine_value_high);
			holder.txtValueLow = (TextView) row
					.findViewById(R.id.txt_fine_value_low);
			row.setTag(holder);

			holder.cbMark.setOnCheckedChangeListener(mMarklistener);
		} else {
			row = convertView;
			Object tag = row.getTag();
			if (tag instanceof ViewHolder) {
				holder = (ViewHolder) tag;
			}
		}

		if (row == null || holder == null) {
			Log.e(TAG, String.format("getView: row=%s, holder=%s", row, holder));
			return null;
		}

		Integer fineId = Integer.valueOf((int) getItemId(position));
		if (mData != null) {
			int nameResId = mData.getFineNameResId(fineId);
			int descResId = mData.getFineDescriptionResId(fineId);
			FineValues value = mData.getFineValue(fineId);

			if (nameResId == 0 || descResId == 0 || value == null) {
				Log.e(TAG, String.format("getView: nameResId=%s,"
						+ "descResId=%s,value=%s", nameResId, descResId, value));
				return null;
			}

			holder.fineId = fineId;
			holder.cbMark.setChecked(mMarked.contains(Integer.valueOf(fineId)));
			holder.txtName.setText(nameResId);
			holder.txtDescription.setText(descResId);
			if (value.isRange()) {
				holder.txtValueHigh.setText(formatValue(value.getHigh()));
				holder.txtValueLow.setText(formatValue(value.getLow()));
			} else {
				holder.txtValueHigh.setText(formatValue(value.getHigh()));
				holder.txtValueLow.setText("");
			}
			// expand the hit target of the mark checkbox
			// it's the little thing...
			expandHitTarget(row, holder.cbMark);
		} else {
			Log.e(TAG, "getView: mData=null");
		}

		return row;
	}

	static private class Font {
		
	}
	
	static private class ViewHolder {
		
	}
}
