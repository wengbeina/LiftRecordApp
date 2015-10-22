package cn.edu.zucc.TPF.Bean;

import java.util.ArrayList;
import java.util.List;

public class AccRecordBean {
    private List<Float> accX = new ArrayList<Float>();
    private List<Float> accY = new ArrayList<Float>();
    private List<Float> accZ = new ArrayList<Float>();
    private float rotateX;
    private float rotateY;
    private float rotateZ;
    private boolean saveOn = false;
    private boolean transportOn = false;
    private final int limitedSize = 50;

    public AccRecordBean() {
        // TODO Auto-generated constructor stub
    }

    public float getCurrentAccX() {
        if (accX.size() == 0)
            return 0;
        else
            return accX.get(accX.size() - 1);
    }

    public float getCurrentAccY() {
        if (accY.size() == 0)
            return 0;
        else
            return accY.get(accY.size() - 1);
    }

    public float getCurrentAccZ() {
        if (accZ.size() == 0)
            return 0;
        else
            return accZ.get(accZ.size() - 1);
    }

    public void addDataToAccX(float x) {
        if (accX.size() == limitedSize) {
            accX.remove(0);
        }
        accX.add(x);
    }

    public void addDataToAccY(float y) {
        if (accY.size() == limitedSize)
            accY.remove(0);
        accY.add(y);
    }

    public void addDataToAccZ(float z) {
        if (accZ.size() == limitedSize)
            accZ.remove(0);
        accZ.add(z);
    }

    public List<Float> getAccX() {
        return accX;
    }

    public void setAccX(List<Float> accX) {
        this.accX = accX;
    }

    public List<Float> getAccY() {
        return accY;
    }

    public void setAccY(List<Float> accY) {
        this.accY = accY;
    }

    public List<Float> getAccZ() {
        return accZ;
    }

    public void setAccZ(List<Float> accZ) {
        this.accZ = accZ;
    }

    public float getRotateX() {
        return rotateX;
    }

    public void setRotateX(float rotateX) {
        this.rotateX = rotateX;
    }

    public float getRotateY() {
        return rotateY;
    }

    public void setRotateY(float rotateY) {
        this.rotateY = rotateY;
    }

    public float getRotateZ() {
        return rotateZ;
    }

    public void setRotateZ(float rotateZ) {
        this.rotateZ = rotateZ;
    }

    public boolean isSaveOn() {
        return saveOn;
    }

    public void setSaveOn(boolean saveOn) {
        this.saveOn = saveOn;
    }

    public boolean isTransportOn() {
        return transportOn;
    }

    public void setTransportOn(boolean transportOn) {
        this.transportOn = transportOn;
    }
}
