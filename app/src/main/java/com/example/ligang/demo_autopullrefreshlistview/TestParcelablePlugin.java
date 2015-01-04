package com.example.ligang.demo_autopullrefreshlistview;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ligang on 15/1/3.
 */
public class TestParcelablePlugin implements Parcelable {
    private int id;
    private String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public TestParcelablePlugin() {
    }

    private TestParcelablePlugin(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<TestParcelablePlugin> CREATOR = new Parcelable.Creator<TestParcelablePlugin>() {
        public TestParcelablePlugin createFromParcel(Parcel source) {
            return new TestParcelablePlugin(source);
        }

        public TestParcelablePlugin[] newArray(int size) {
            return new TestParcelablePlugin[size];
        }
    };
}
