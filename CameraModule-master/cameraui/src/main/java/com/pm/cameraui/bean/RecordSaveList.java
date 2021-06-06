package com.pm.cameraui.bean;

import java.util.ArrayList;

public class RecordSaveList {
    public ArrayList<RecordSave> recordSaves = new ArrayList<>();

    public void removeReocrd(RecordSave recordSave){
        for(int i =0;i<recordSaves.size();i++){
            if(recordSave.equals(recordSaves.get(i))){
                recordSaves.remove(i);
                return;
            }
        }
    }

}
