package com.weis.videoxie.view;

import com.weis.videoxie.base.BaseView;
import com.weis.videoxie.bean.DepartmentBean;

public interface RegisterView extends BaseView{
    void showMessage(int i, String s);

    void getDepartmentNext(DepartmentBean info);

    void registerNext();
}
