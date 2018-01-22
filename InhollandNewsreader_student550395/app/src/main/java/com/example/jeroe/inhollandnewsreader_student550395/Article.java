package com.example.jeroe.inhollandnewsreader_student550395;

import android.os.Parcelable;
import android.os.Parcel;
import java.util.ArrayList;

public class Article implements Parcelable {

    public int Id;
    public int Feed;
    public String Title;
    public String Summary;
    public String PublishDate;
    public String Image;
    public String Url;
    public String[] Related;
    public ArrayList<Category> Categories;
    public boolean IsLiked;

    private Article(Parcel in) {
        Id = in.readInt();
        Feed = in.readInt();
        Title = in.readString();
        Summary = in.readString();
        PublishDate = in.readString();
        Image = in.readString();
        Url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeInt(Feed);
        dest.writeString(Title);
        dest.writeString(Summary);
        dest.writeString(PublishDate);
        dest.writeString(Image);
        dest.writeString(Url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
