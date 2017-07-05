package com.chengtech.chengtechmt.entity.routequery;

import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.MyConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/8/12 15:40.
 * 路况病害登记表
 */
public class MUnitPatrol {

    public MunitPatrolKey munitPatrolKey;

    public List<PatrolRecord> records;

    public class MunitPatrolKey {

        public String sectionId;        // 区间

        public String thirdDeptId;     // 巡查单位

        public Date patrolDate;       // 巡查日期

        public String patrolMileage;  // 巡查里程

        public String patrolType;     // 巡查类型

        public String patrolMan;      // 巡查人

        public Date patrolEndDate;    // 巡查结束日期

        public List<String> getArray() {
            List<String> values = new ArrayList<>();

            values.add(DateUtils.convertDate2(patrolDate));
            values.add(MyConstants.deptTree.get(thirdDeptId));
            values.add(patrolMileage);
            values.add(patrolType);
            values.add(patrolMan);
            return values;
        }
    }
}
