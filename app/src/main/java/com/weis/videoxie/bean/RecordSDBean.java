package com.weis.videoxie.bean;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.videogo.openapi.bean.EZDeviceRecordFile;

import java.util.List;

public class RecordSDBean extends SectionEntity {

    public RecordSDBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public List<EZDeviceRecordFile> data;

    public List<EZDeviceRecordFile> getData() {
        return data;
    }

    public void setData(List<EZDeviceRecordFile> data) {
        this.data = data;
    }
}
