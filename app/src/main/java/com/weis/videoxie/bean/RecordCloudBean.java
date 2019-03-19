package com.weis.videoxie.bean;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.videogo.openapi.bean.EZCloudRecordFile;

import java.util.List;

public class RecordCloudBean extends SectionEntity {

    public RecordCloudBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public List<EZCloudRecordFile> data;

    public List<EZCloudRecordFile> getData() {
        return data;
    }

    public void setData(List<EZCloudRecordFile> data) {
        this.data = data;
    }
}
