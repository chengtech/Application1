package com.chengtech.imooc_festsms.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class FestivalLab {

    public static FestivalLab festivalLab;

    private List<Festival> festivalList = new ArrayList<Festival>();
    private List<Msg> msgList = new ArrayList<Msg>();
    private FestivalLab () {
        festivalList.add(new Festival("001","儿童节"));
        festivalList.add(new Festival("002","国庆节"));
        festivalList.add(new Festival("003","中秋节"));
        festivalList.add(new Festival("004","青年节"));
        festivalList.add(new Festival("005","春节"));

        msgList.add(new Msg("01","001","我是儿童节1"));
        msgList.add(new Msg("02","001","我是儿童节2"));
        msgList.add(new Msg("03","001","我是儿童节3"));
        msgList.add(new Msg("04","002","我是国庆节1"));
        msgList.add(new Msg("05","002","我是国庆节2"));
        msgList.add(new Msg("06","002","我是国庆节3"));
    }


    public List<Msg> getFestivalMsg(String fesId) {
        List<Msg> list = new ArrayList<Msg>();
        for (Msg m :msgList) {
            if (m.getFesId().equals(fesId)) {
                list.add(m);
            }
        }
        return list;
    }

    public Msg getMsgById(String id) {
        for (Msg m : msgList) {
            if (m.getId().equals(id))
                return m;
        }

        return null;
    }
    public Festival getFestivalById (String id) {
        for (Festival festival: festivalList) {
            if (festival.getId().equals(id)) {
                return festival;
            }
        }
        return null;
    }
    public List<Festival> getFestivalList () {
        return new ArrayList<>(festivalList);
    }

    public static FestivalLab getFestivalLab() {
        if (festivalLab==null) {
            synchronized (FestivalLab.class) {
                if (festivalLab==null) {
                    festivalLab = new FestivalLab();
                }
            }
        }

        return festivalLab;
    }
}
