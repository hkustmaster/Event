package com.hkust.android.event.models;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.hkust.android.event.R;

/**
 * Created by Gordon Wong on 7/18/2015.
 * 
 * Note model.
 */
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
		int color = getRandomColor(context);
		return new Note(capitalize(title), note, info, color);
	}

	private static int getRandomColor(Context context) {
		int[] colors;
		if (Math.random() >= 0.6) {
			colors = context.getResources().getIntArray(R.array.note_accent_colors);
		} else {
			colors = context.getResources().getIntArray(R.array.note_neutral_colors);
		}
		return colors[((int) (Math.random() * colors.length))];
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
