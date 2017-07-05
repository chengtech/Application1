package com.chengtech.chengtechmt.presenter;

import android.content.Context;

import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.entity.MaintainRegister;
import com.chengtech.chengtechmt.entity.MaintainTaskItem;
import com.chengtech.chengtechmt.entity.MediumPlanprogress;
import com.chengtech.chengtechmt.entity.ProjectManagement;
import com.chengtech.chengtechmt.entity.TaskDetail;
import com.chengtech.chengtechmt.entity.gson.DeptG;
import com.chengtech.chengtechmt.entity.MaintainTask;

/**
 * 作者: LiuFuYingWang on 2016/6/28 15:04.
 */
public class ListPagePre implements Presenter<IView, Object> {

    public IView view;
    public DeptG deptG;
    public MediumPlanprogress mediumPlanprogress;
    public ProjectManagement projectManagement;
    public MaintainRegister maintainRegister;
    public MaintainTask maintainTask;
    public MaintainTaskItem maintainTaskItem;
    public TaskDetail taskDetail;

    public ListPagePre(IView view, String type) {
        attachView(view);
        switch (type) {
            case "MediumPlanprogress":
                mediumPlanprogress = new MediumPlanprogress(this);
                break;
            case "ProjectManagement" :
                projectManagement = new ProjectManagement(this);
                break;
            case "MaintainRegister" :
                maintainRegister = new MaintainRegister(this);
                break;
            case "MaintainTask":
            case "MinorRepair":
                maintainTask = new MaintainTask(this);
                maintainTaskItem = new MaintainTaskItem(this);
                taskDetail = new TaskDetail(this);
                break;

        }

        deptG = new DeptG(this);

    }

    @Override
    public void attachView(IView iView) {
        this.view = iView;
    }

    @Override
    public void detchView(IView iView) {
        iView = null;
    }

    @Override
    public void loadDataSuccess(Object o) {

    }

    @Override
    public void loadDataSuccess(Object o, int type) {

    }

    @Override
    public void loadDataSuccess(Object o, String className) {
        view.dismssDialog();
        view.loadDataSuccess(o, className);
    }

    @Override
    public void loadDataFailed() {
        view.dismssDialog();
        view.loadDataFailure();
    }

    @Override
    public void hasError() {
        view.dismssDialog();
        view.hasError();
    }

    public void getData(Context context, String url, String className,int pageNo) {
        view.showDialog();
        switch (className) {
            case "DeptG":
                deptG.getData(context,url,className);
                break;
            case "MediumPlanprogress" :
                mediumPlanprogress.getData(context,url,className,pageNo);
                break;
            case "ProjectManagement" :
                projectManagement.getData(context,url,className,pageNo);
                break;
            case "MaintainRegister" :
                maintainRegister.getData(context,url,className,pageNo);
                break;
            case "MaintainTask" :
            case "MinorRepair":
                maintainTask.getData(context,url,className,pageNo);
                break;
            case "MaintainTaskItem" :
                maintainTaskItem.getData(context,url,className,pageNo);
                break;
            case "TaskDetail" :
                taskDetail.getData(context,url,className,pageNo);
                break;
        }
    }
}
