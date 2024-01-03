package com.example.agenda.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.agenda.utils.DateConverter;
import java.util.Date;
import java.util.Objects;

@Entity(tableName="events", foreignKeys={@ForeignKey(
        entity = User.class,
        parentColumns = {"uid"},
        childColumns = {"user_id"},
        onDelete = ForeignKey.CASCADE
)})
public class Event implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private long id_event;
    @ColumnInfo
    private Date event_date;
    @ColumnInfo
    private int begin_time;
    @ColumnInfo
    private int end_time;
    @ColumnInfo
    @NonNull
    private String title;
    @ColumnInfo
    String details;
    @ColumnInfo
    private EventType eventType;
    @ColumnInfo(index = true)
    private String user_id;

    public Event(long id_event, Date event_date, int begin_time, int end_time, String title, String details, EventType eventType, String user_id) {
        this.id_event = id_event;
        this.event_date = event_date;
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.title = title;
        this.details = details;
        this.eventType = eventType;
        this.user_id = user_id;
    }


    @Ignore
    public Event(Date event_date, int begin_time, int end_time, String title, String details, EventType eventType) {
        this.event_date = event_date;
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.title = title;
        this.details = details;
        this.eventType = eventType;
    }
    @Ignore
    public Event(Date event_date, int begin_time) {
        this.event_date = event_date;
        this.begin_time = begin_time;
        this.end_time = begin_time+1;
        this.title = "";
        this.details = "";
        this.eventType = EventType.MEETING;
    }
    @Ignore
    public Event(Parcel source) {
        this.id_event = source.readLong();
        this.event_date = DateConverter.toDate(source.readString());
        this.begin_time = source.readInt();
        this.end_time = source.readInt();
        this.title = source.readString();
        this.details = source.readString();
        this.eventType = EventType.valueOf(source.readString());
        this.user_id = source.readString();
    }

    @Override
    public String toString() {
        return title;
    }

    @NonNull
    public Date getEvent_date() {
        return event_date;
    }

    public void setEvent_date(Date event_date) {
        this.event_date = event_date;
    }

    public int getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(int begin_time) {
        this.begin_time = begin_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
    public long getId_event() {
        return id_event;
    }

    public void setId_event(long id_event) {
        this.id_event = id_event;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id_event == event.id_event ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_event);
    }

    public static Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }

    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id_event);
        dest.writeString(DateConverter.fromDate(event_date));
        dest.writeInt(begin_time);
        dest.writeInt(end_time);
        dest.writeString(title);
        dest.writeString(details);
        dest.writeString(eventType.toString());
        dest.writeString(user_id);
    }
}
