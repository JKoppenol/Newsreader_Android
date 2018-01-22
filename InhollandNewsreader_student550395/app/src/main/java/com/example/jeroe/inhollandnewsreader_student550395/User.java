package com.example.jeroe.inhollandnewsreader_student550395;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String Username;
    private String Password;
    private AuthToken AuthToken;

    public User(Parcel in) {
        Username = in.readString();
        Password = in.readString();
        AuthToken = in.readParcelable(com.example.jeroe.inhollandnewsreader_student550395.AuthToken.class.getClassLoader());
    }

    public User() {

    }

    public String getUsername() {
        return this.Username;
    }

    public void setUsername(String username)
    {
        this.Username = username;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String password)
    {
        this.Password = password;
    }

    public AuthToken getAuthToken()
    {
        return this.AuthToken;
    }

    public void setAuthToken(AuthToken authToken)
    {
        this.AuthToken = authToken;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Username);
        parcel.writeString(Password);
        parcel.writeParcelable(AuthToken, i);
    }
}
