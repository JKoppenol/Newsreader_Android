package com.example.jeroe.inhollandnewsreader_student550395;

import android.os.Parcel;
import android.os.Parcelable;

public class AuthToken implements Parcelable {
    public String AuthToken;

    protected AuthToken(Parcel in) {
        AuthToken = in.readString();
    }

    public static final Creator<AuthToken> CREATOR = new Creator<AuthToken>() {
        @Override
        public AuthToken createFromParcel(Parcel in) {
            return new AuthToken(in);
        }

        @Override
        public AuthToken[] newArray(int size) {
            return new AuthToken[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(AuthToken);
    }
}
