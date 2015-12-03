package com.hkust.android.event.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hkust.android.event.R;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.Note;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

	private Note[] notes;
	private ClickListener clickListener;

	public NotesAdapter(Context context, String TagName, int numNotes) {
		if (TagName.equalsIgnoreCase(Constants.EXPLORE_FRAGMENT)) {
			notes = getExploreEvents(context, numNotes);
		} else if (TagName.equalsIgnoreCase(Constants.MYEVENT_FRAGMENT)) {
			notes = getMyEventEvents(context, numNotes);
		} else if (TagName.equalsIgnoreCase(Constants.PENDING_FRAGMENT)) {
			notes = getPendingEvents(context, numNotes);
		}
	}

	@Override
	public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent,
				false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Note noteModel = notes[position];
		String title = noteModel.getTitle();
		String note = noteModel.getNote();
		String info_date = noteModel.getInfo_date();
		int info_date_Image = noteModel.getInfo_date_Image();
		String info_location = noteModel.getInfo_location();
		int info_location_Image = noteModel.getInfo_location_Image();
		int color = noteModel.getColor();

		// Set text
		holder.titleTextView.setText(title);
		holder.noteTextView.setText(note);
		holder.infoDateTextView.setText(info_date);
		holder.infoLocationTextView.setText(info_location);


		// Set image
		holder.infoDateImageView.setImageResource(info_date_Image);
		holder.infoLocationImageView.setImageResource(info_location_Image);

		int paddingTop = (holder.titleTextView.getVisibility() != View.VISIBLE) ? 0
				: holder.itemView.getContext().getResources()
				.getDimensionPixelSize(R.dimen.note_content_spacing);
		holder.noteTextView.setPadding(holder.noteTextView.getPaddingLeft(), paddingTop,
				holder.noteTextView.getPaddingRight(), holder.noteTextView.getPaddingBottom());

		// Set background color
		((CardView) holder.itemView).setCardBackgroundColor(color);
	}

	@Override
	public int getItemCount() {
		return notes.length;
	}

	private Note[] generateNotes(Context context, int numNotes) {
		Note[] notes = new Note[numNotes];
		for (int i = 0; i < notes.length; i++) {
			notes[i] = Note.randomNote(context);
		}
		return notes;
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private TextView titleTextView;
		private TextView noteTextView;
		private LinearLayout infoDateLayout;
		private TextView infoDateTextView;
		private ImageView infoDateImageView;
		private LinearLayout infoLocationLayout;
		private TextView infoLocationTextView;
		private ImageView infoLocationImageView;

		public ViewHolder(View itemView) {
			super(itemView);
			titleTextView = (TextView) itemView.findViewById(R.id.note_title);
			noteTextView = (TextView) itemView.findViewById(R.id.note_text);
			infoDateLayout = (LinearLayout) itemView.findViewById(R.id.note_info_date_layout);
			infoDateTextView = (TextView) itemView.findViewById(R.id.note_info_date);
			infoDateImageView = (ImageView) itemView.findViewById(R.id.note_info_date_image);
			infoLocationLayout = (LinearLayout) itemView.findViewById(R.id.note_info_location_layout);
			infoLocationTextView = (TextView) itemView.findViewById(R.id.note_info_location);
			infoLocationImageView = (ImageView) itemView.findViewById(R.id.note_info_location_image);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			// context.startActivity(new Intent(context, PendingEventDetailActivity.class));
			if (clickListener != null) {
				clickListener.itemClicked(v, getAdapterPosition());
			}
		}
	}

	public interface ClickListener {
		public void itemClicked(View view, int position);

	}

	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}

	private Note[] getExploreEvents(Context context, int numNotes) {

		// send request to server
		AsyncHttpClient client = new AsyncHttpClient();

		//1、获取Preferences
		SharedPreferences settings = context.getSharedPreferences("setting", 0);
		//2、取出数据
		String token = settings.getString("token","default");
		Log.i("PPPP", token);

		client.get(Constants.SERVER_URL + Constants.GET_ALL_EVENT+"?token="+token, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String message = new String(responseBody);
				Log.i("PPPPPP", message);
				if (statusCode == 200) {

				} else {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				Log.i("PPPPPP", statusCode+"");
				Log.i("PPPPPP", "ON FAILURE");
			}
		});

		Note[] notes = new Note[numNotes];
		for (int i = 0; i < notes.length; i++) {
			notes[i] = Note.randomNote(context);
		}
		return notes;
	}

	private Note[] getPendingEvents(Context context, int numNotes) {
		Note[] notes = new Note[numNotes];
		for (int i = 0; i < notes.length; i++) {
			notes[i] = Note.randomOwnNote(context);
		}
		return notes;
	}

	private Note[] getMyEventEvents(Context context, int numNotes) {
		Note[] notes = new Note[numNotes];
		for (int i = 0; i < notes.length; i++) {
			notes[i] = Note.randomOwnNote(context);
		}
		return notes;
	}
}
