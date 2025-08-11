package com.lky.mytoolbox;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
    private long id;
    private String title;
    private String content;
    private Date date;
    private boolean isFavorite;
    private String reminderDate;
    private String alarmTime;
    private byte[] drawingData;
    private byte[] photoData;
    private int requestCode;

    // 构造器1：用于新笔记
    public Note(String title, String content, Date date, boolean isFavorite, String reminderDate, String alarmTime, byte[] drawingData, byte[] photoData) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.isFavorite = isFavorite;
        this.reminderDate = reminderDate;
        this.alarmTime = alarmTime;
        this.drawingData = drawingData;
        this.photoData = photoData;
    }

    // 构造器2：用于从数据库中读取的笔记
    public Note(long id, String title, String content, Date date, boolean isFavorite, String reminderDate, String alarmTime, byte[] drawingData, byte[] photoData) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.isFavorite = isFavorite;
        this.reminderDate = reminderDate;
        this.alarmTime = alarmTime;
        this.drawingData = drawingData;
        this.photoData = photoData;
    }

    // 构造器3：用于初始化新笔记时允许 reminderDate 和 alarmTime 为 null
    public Note(String title, String content, Date date, boolean isFavorite) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.isFavorite = isFavorite;
        this.reminderDate = null;
        this.alarmTime = null;
        this.drawingData = null;
        this.photoData = null;
    }
    public Note(long id, String title, String content, Date date, boolean isFavorite, String reminderDate, String alarmTime, byte[] photoData, byte[] drawingData, int requestCode) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.isFavorite = isFavorite;
        this.reminderDate = reminderDate;
        this.alarmTime = alarmTime;
        this.photoData = photoData;
        this.drawingData = drawingData;
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public byte[] getDrawingData() {
        return drawingData;
    }

    public void setDrawingData(byte[] drawingData) {
        this.drawingData = drawingData;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }
}
