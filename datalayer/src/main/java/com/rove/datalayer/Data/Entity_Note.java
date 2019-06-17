package com.rove.datalayer.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.rove.datalayer.Util.*;

@Entity
public class Entity_Note implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int NoteId;
    @TypeConverters({Converters.class})
    private Date Date;
    private String Title;
    private String Content;
    private String ImgUrl;


    protected Entity_Note(Parcel in) {
        Date = new Date(in.readLong());
        NoteId = in.readInt();
        Title = in.readString();
        Content = in.readString();
        ImgUrl = in.readString();
    }
    public Entity_Note() {
    }

    public static final Creator<Entity_Note> CREATOR = new Creator<Entity_Note>() {
        @Override
        public Entity_Note createFromParcel(Parcel in) {
            return new Entity_Note(in);
        }

        @Override
        public Entity_Note[] newArray(int size) {
            return new Entity_Note[size];
        }
    };

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public int getNoteId() {
        return NoteId;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(Date.getTime());
        parcel.writeInt(NoteId);
        parcel.writeString(Title);
        parcel.writeString(Content);
        parcel.writeString(ImgUrl);
    }
}
