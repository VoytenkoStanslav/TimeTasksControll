package com.stanislav.admin.timetaskscontroll;

/**
 * Created by Admin on 05.09.2018.
 */

public class Task {

    private String nameTask;
    private String uid;
    private String fullNameTask;
    private String anyText;
    private boolean checkSumTask;
    private float sum;
    private long timeTask;
    private long allTimeTask;
    private long nowTimeTask;
    private String specificTask;
    private int spinPos;
    private long dateCreateTask;

    public Task(){

    }

    public Task (String uid, String nameTask, String fullNameTask, String anyText,
                 boolean checkSumTask, float sum, long timeTask, int spinPos, long dateCreateTask){
        this.uid = uid;
        this.nameTask = nameTask;
        this.fullNameTask = fullNameTask;
        this.anyText = anyText;
        this.checkSumTask = checkSumTask;
        this.sum = sum;
        this.timeTask = timeTask;
        this.spinPos = spinPos;
        this.dateCreateTask = dateCreateTask;
    }

    public Task (String uid, long allTimeTask, long nowTimeTask){
        this.uid = uid;
        this.allTimeTask = allTimeTask;
        this.nowTimeTask = nowTimeTask;
    }

    public long getDateCreateTask() {
        return dateCreateTask;
    }

    public void setDateCreateTask(long dateCreateTask) {
        this.dateCreateTask = dateCreateTask;
    }

    public int getSpinPos() {
        return spinPos;
    }

    public void setSpinPos(int spinPos) {
        this.spinPos = spinPos;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullNameTask() {
        return fullNameTask;
    }

    public void setFullNameTask(String fullNameTask) {
        this.fullNameTask = fullNameTask;
    }

    public String getAnyText() {
        return anyText;
    }

    public void setAnyText(String anyText) {
        this.anyText = anyText;
    }

    public boolean isCheckSumTask() {
        return checkSumTask;
    }

    public void setCheckSumTask(boolean checkSumTask) {
        this.checkSumTask = checkSumTask;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public long getTimeTask() {
        return timeTask;
    }

    public void setTimeTask(long timeTask) {
        this.timeTask = timeTask;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public long getAllTimeTask() {
        return allTimeTask;
    }

    public void setAllTimeTask(long allTimeTask) {
        this.allTimeTask = allTimeTask;
    }

    public long getNowTimeTask() {
        return nowTimeTask;
    }

    public void setNowTimeTask(long nowTimeTask) {
        this.nowTimeTask = nowTimeTask;
    }

    public String getSpecificTask() {
        return specificTask;
    }

    public void setSpecificTask(String specificTask) {
        this.specificTask = specificTask;
    }
}
