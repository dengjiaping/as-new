package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/8/1.
 */

public class StartTrainBean {


    /**
     * programme : [{"ishavezero":"0","num":"11","time":"10","timearr":[1,2,4,5,7,8],"arr":[[0,1,2,3,4,5,6,7,8,9,10],[0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2"]},{"ishavezero":"0","num":"11","time":"10","timearr":[1,2,4,5,7,8],"arr":[[0,1,2,3,4,5,6,7,8,9,10],[0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2"]},{"ishavezero":"1","num":"2","time":"6","timearr":[0],"arr":[[0,6],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"13","time":"18","timearr":[0,1,3,6,7,9,12,13,15],"arr":[[0,1,3,4,6,7,9,10,12,13,15,16,18],[0,5,5,0,0,5,5,0,0,5,5,0,0]],"videoarr":["y1","y3","y2","y1","y3","y2","y1","y3","y2"]},{"ishavezero":"0","num":"13","time":"18","timearr":[0,1,3,6,7,9,12,13,15],"arr":[[0,1,3,4,6,7,9,10,12,13,15,16,18],[0,5,5,0,0,5,5,0,0,5,5,0,0]],"videoarr":["y1","y3","y2","y1","y3","y2","y1","y3","y2"]},{"ishavezero":"1","num":"2","time":"8","timearr":[0],"arr":[[0,8],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"5","time":"11","timearr":[0,1,2,6],"arr":[[0,1,6,7,11],[0,5,5,0,0]],"videoarr":["y1","y3","y4","y2"]},{"ishavezero":"0","num":"5","time":"11","timearr":[0,1,2,6],"arr":[[0,1,6,7,11],[0,5,5,0,0]],"videoarr":["y1","y3","y4","y2"]},{"ishavezero":"0","num":"5","time":"11","timearr":[0,1,2,6],"arr":[[0,1,6,7,11],[0,5,5,0,0]],"videoarr":["y1","y3","y4","y2"]},{"ishavezero":"1","num":"2","time":"8","timearr":[0],"arr":[[0,8],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"13","time":"12","timearr":[0,1,3,4,6,7,9,10],"arr":[[0,1,2,3,4,5,6,7,8,9,10,11,12],[0,5,0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2","y1","y2"]},{"ishavezero":"0","num":"13","time":"12","timearr":[0,1,3,4,6,7,9,10],"arr":[[0,1,2,3,4,5,6,7,8,9,10,11,12],[0,5,0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2","y1","y2"]},{"ishavezero":"1","num":"2","time":"8","timearr":[0],"arr":[[0,8],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"5","time":"12","timearr":[0,1,2,7],"arr":[[0,1,7,8,12],[0,5,5,0,0]],"videoarr":["y1","y3","y5","y2"]},{"ishavezero":"0","num":"5","time":"12","timearr":[0,1,2,7],"arr":[[0,1,7,8,12],[0,5,5,0,0]],"videoarr":["y1","y3","y5","y2"]},{"ishavezero":"1","num":"2","time":"10","timearr":[0],"arr":[[0,10],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"11","time":"10","timearr":[1,2,4,5,7,8],"arr":[[0,1,2,3,4,5,6,7,8,9,10],[0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2"]},{"ishavezero":"0","num":"11","time":"10","timearr":[1,2,4,5,7,8],"arr":[[0,1,2,3,4,5,6,7,8,9,10],[0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2"]},{"ishavezero":"1","num":"2","time":"6","timearr":[0],"arr":[[0,6],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"13","time":"18","timearr":[0,1,3,6,7,9,12,13,15],"arr":[[0,1,3,4,6,7,9,10,12,13,15,16,18],[0,5,5,0,0,5,5,0,0,5,5,0,0]],"videoarr":["y1","y3","y2","y1","y3","y2","y1","y3","y2"]},{"ishavezero":"0","num":"13","time":"18","timearr":[0,1,3,6,7,9,12,13,15],"arr":[[0,1,3,4,6,7,9,10,12,13,15,16,18],[0,5,5,0,0,5,5,0,0,5,5,0,0]],"videoarr":["y1","y3","y2","y1","y3","y2","y1","y3","y2"]},{"ishavezero":"1","num":"2","time":"8","timearr":[0],"arr":[[0,8],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"5","time":"11","timearr":[0,1,2,6],"arr":[[0,1,6,7,11],[0,5,5,0,0]],"videoarr":["y1","y3","y4","y2"]},{"ishavezero":"0","num":"5","time":"11","timearr":[0,1,2,6],"arr":[[0,1,6,7,11],[0,5,5,0,0]],"videoarr":["y1","y3","y4","y2"]},{"ishavezero":"0","num":"5","time":"11","timearr":[0,1,2,6],"arr":[[0,1,6,7,11],[0,5,5,0,0]],"videoarr":["y1","y3","y4","y2"]},{"ishavezero":"1","num":"2","time":"8","timearr":[0],"arr":[[0,8],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"13","time":"12","timearr":[0,1,3,4,6,7,9,10],"arr":[[0,1,2,3,4,5,6,7,8,9,10,11,12],[0,5,0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2","y1","y2"]},{"ishavezero":"0","num":"13","time":"12","timearr":[0,1,3,4,6,7,9,10],"arr":[[0,1,2,3,4,5,6,7,8,9,10,11,12],[0,5,0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2","y1","y2"]},{"ishavezero":"1","num":"2","time":"8","timearr":[0],"arr":[[0,8],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"5","time":"12","timearr":[0,1,2,7],"arr":[[0,1,7,8,12],[0,5,5,0,0]],"videoarr":["y1","y3","y5","y2"]},{"ishavezero":"0","num":"5","time":"12","timearr":[0,1,2,7],"arr":[[0,1,7,8,12],[0,5,5,0,0]],"videoarr":["y1","y3","y5","y2"]},{"ishavezero":"1","num":"2","time":"10","timearr":[0],"arr":[[0,10],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"11","time":"10","timearr":[1,2,4,5,7,8],"arr":[[0,1,2,3,4,5,6,7,8,9,10],[0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2"]},{"ishavezero":"0","num":"11","time":"10","timearr":[1,2,4,5,7,8],"arr":[[0,1,2,3,4,5,6,7,8,9,10],[0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2"]},{"ishavezero":"1","num":"2","time":"6","timearr":[0],"arr":[[0,6],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"13","time":"18","timearr":[0,1,3,6,7,9,12,13,15],"arr":[[0,1,3,4,6,7,9,10,12,13,15,16,18],[0,5,5,0,0,5,5,0,0,5,5,0,0]],"videoarr":["y1","y3","y2","y1","y3","y2","y1","y3","y2"]},{"ishavezero":"0","num":"13","time":"18","timearr":[0,1,3,6,7,9,12,13,15],"arr":[[0,1,3,4,6,7,9,10,12,13,15,16,18],[0,5,5,0,0,5,5,0,0,5,5,0,0]],"videoarr":["y1","y3","y2","y1","y3","y2","y1","y3","y2"]},{"ishavezero":"1","num":"2","time":"8","timearr":[0],"arr":[[0,8],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"5","time":"11","timearr":[0,1,2,6],"arr":[[0,1,6,7,11],[0,5,5,0,0]],"videoarr":["y1","y3","y4","y2"]},{"ishavezero":"0","num":"5","time":"11","timearr":[0,1,2,6],"arr":[[0,1,6,7,11],[0,5,5,0,0]],"videoarr":["y1","y3","y4","y2"]},{"ishavezero":"0","num":"5","time":"11","timearr":[0,1,2,6],"arr":[[0,1,6,7,11],[0,5,5,0,0]],"videoarr":["y1","y3","y4","y2"]},{"ishavezero":"1","num":"2","time":"8","timearr":[0],"arr":[[0,8],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"13","time":"12","timearr":[0,1,3,4,6,7,9,10],"arr":[[0,1,2,3,4,5,6,7,8,9,10,11,12],[0,5,0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2","y1","y2"]},{"ishavezero":"0","num":"13","time":"12","timearr":[0,1,3,4,6,7,9,10],"arr":[[0,1,2,3,4,5,6,7,8,9,10,11,12],[0,5,0,0,5,0,0,5,0,0,5,0,0]],"videoarr":["y1","y2","y1","y2","y1","y2","y1","y2"]},{"ishavezero":"1","num":"2","time":"8","timearr":[0],"arr":[[0,8],[0,0]],"videoarr":["y13"]},{"ishavezero":"0","num":"5","time":"12","timearr":[0,1,2,7],"arr":[[0,1,7,8,12],[0,5,5,0,0]],"videoarr":["y1","y3","y5","y2"]},{"ishavezero":"0","num":"5","time":"12","timearr":[0,1,2,7],"arr":[[0,1,7,8,12],[0,5,5,0,0]],"videoarr":["y1","y3","y5","y2"]},{"ishavezero":"1","num":"2","time":"10","timearr":[0],"arr":[[0,10],[0,0]],"videoarr":["y13"]}]
     * level : Level-1
     * strength : 0~20
     * state : 200
     * experience : 20
     * mess : 请求成功
     * totaltime : 531
     * level_2 : A
     * excisedays : 3
     * integral : 20
     */

    public String level;
    public String strength;
    public int state;
    public String experience;
    public String mess;
    public String totaltime;
    public String level_2;
    public String excisedays;
    public String integral;
    public List<ProgrammeBean> programme;

    public static class ProgrammeBean {
        /**
         * ishavezero : 0
         * num : 11
         * time : 10
         * timearr : [1,2,4,5,7,8]
         * arr : [[0,1,2,3,4,5,6,7,8,9,10],[0,0,5,0,0,5,0,0,5,0,0]]
         * videoarr : ["y1","y2","y1","y2","y1","y2"]
         */

        public String ishavezero;
        public String num;
        public String time;
        public List<Float> timearr;
        public List<float[]> arr;
        public List<String> videoarr;
    }
}
