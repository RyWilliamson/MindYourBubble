package com.example.mindyourbubble.Data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HealthData implements Serializable {
    private static final SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH );
    private Date startTime;
    private Date endTime;
    private long duration;
    private HeartData heartData;
    private RespiratoryData respiratoryData;
    private SkinData skinData;
    private SleepData sleepData;

    public HealthData( JSONObject data ) {
        try {
            this.startTime = format.parse( data.getString( "start_time" ) );
            this.endTime = format.parse( data.getString( "end_time" ) );
            this.duration = endTime.getTime() - startTime.getTime();
            this.heartData = new HeartData( data.getJSONObject( "heart_rate" ) );
            this.respiratoryData = new RespiratoryData( data.getJSONObject( "respiratory_rate" ) );
            this.skinData = new SkinData( data.getJSONObject( "skin_temp" ) );
            this.sleepData = new SleepData( data.getJSONObject( "sleep" ) );
        } catch ( JSONException e ) {
            Log.e( "Health Data Constructor",
                    "failed to construct from json: " + e.toString() );
        } catch ( ParseException e ) {
            Log.e( "Health Data Constructor",
                    "Failed to parse datetime: " + e.toString() );
        }
    }

    @Override
    public String toString() {
        return "HealthData{, startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                "\n" + heartData +
                "\n" + respiratoryData +
                "\n" + skinData +
                "\n" + sleepData +
                '}';
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public long getDuration() {
        return duration;
    }

    public HeartData getHeartData() {
        return heartData;
    }

    public RespiratoryData getRespiratoryData() {
        return respiratoryData;
    }

    public SkinData getSkinData() {
        return skinData;
    }

    public SleepData getSleepData() {
        return sleepData;
    }
}
