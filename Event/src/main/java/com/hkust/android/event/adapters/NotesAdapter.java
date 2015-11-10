package com.hkust.android.event.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hkust.android.event.EventDetailActivity;
import com.hkust.android.event.R;
import com.hkust.android.event.models.Note;

/**
 * Created by Gordon Wong on 7/18/2015.
 *
 * Adapter for the all items screen.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

	private Note[] notes;
    private Context context;
    private ClickListener clickListener;
	public NotesAdapter(Context context, int numNotes) {
		notes = generateNotes(context, numNotes);
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

//		// Set visibilities
//		holder.titleTextView.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
//		holder.noteTextView.setVisibility(TextUtils.isEmpty(note) ? View.GONE : View.VISIBLE);
//		holder.infoDateLayout.setVisibility(TextUtils.isEmpty(info_date) ? View.GONE : View.VISIBLE);
//		holder.infoLocationLayout.setVisibility(TextUtils.isEmpty(info_location) ? View.GONE : View.VISIBLE);

		// Set padding
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

		public TextView titleTextView;
		public TextView noteTextView;
		public LinearLayout infoDateLayout;
		public TextView infoDateTextView;
		public ImageView infoDateImageView;
		public LinearLayout infoLocationLayout;
		public TextView infoLocationTextView;
		public ImageView infoLocationImageView;

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
           // context.startActivity(new Intent(context, EventDetailActivity.class));
            if(clickListener !=null){
                clickListener.itemClicked(v, getAdapterPosition());
                Log.i("sdfsfadfa","sdfasdfasfasfda");
            }
        }
    }

    public interface  ClickListener{
        public void itemClicked(View view, int position);

    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

}
