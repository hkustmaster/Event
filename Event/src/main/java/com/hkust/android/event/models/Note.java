package com.hkust.android.event.models;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.hkust.android.event.R;

public class Note {
	private String title;
	private String note;
	private String info_date;
    private String info_location;
	@DrawableRes
	private int info_date_Image;
    private int info_location_Image;
	private int color;

	private Note(String title, String note, NoteInfo info, int color) {
		this.title = title;
		this.note = note;
		this.info_date = info.info_date;
        this.info_date_Image = info.info_date_Image;
        this.info_location = info.info_location;
        this.info_location_Image = info.info_location_Image;
		this.color = color;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}


    public String getInfo_date() {
        return info_date;
    }

    public int getInfo_date_Image() {
        return info_date_Image;
    }

    public String getInfo_location() {
        return info_location;
    }

    public int getInfo_location_Image() {
        return info_location_Image;
    }

    public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public static Note randomNote(Context context) {
		String title = "title: Music Alive!";
		String note = "Music live show ";

		NoteInfo info = new NoteInfo("31 Oct Sat", R.drawable.ic_event_white_24dp, "HKUST", R.drawable.ic_place_white_24dp);
		int color = getColor(context);
		return new Note(capitalize(title), note, info, color);
	}

	public static Note randomOwnNote(Context context) {
		String title = "Tour to Sai Kung";
		String note = "Sea food, BBQ";

		NoteInfo info = new NoteInfo("To be determine", R.drawable.ic_event_white_24dp, "Sai Kong", R.drawable.ic_place_white_24dp);
		int color = getColor(context);
		return new Note(capitalize(title), note, info, color);
	}



	private static int getColor(Context context) {
		//0  3橙
		int[] colorsAccent = context.getResources().getIntArray(R.array.note_accent_colors);
		int[] colorsNeutral = context.getResources().getIntArray(R.array.note_neutral_colors);
		//2

		return colorsNeutral[2];
	}

	private static String capitalize(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	private static class NoteInfo {
		private String info_date;
        private String info_location;
		private int info_date_Image;
        private int info_location_Image;

		private NoteInfo(String info_date, int info_date_Image, String info_location, int info_location_Image) {
			this.info_date = info_date;
			this.info_date_Image = info_date_Image;

            this.info_location = info_location;
            this.info_location_Image = info_location_Image;
		}

	}
}
