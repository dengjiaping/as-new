package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/9/12.
 */

public class LocationBaiDuBean {

    /**
     * status : 0
     * result : {"location":{"lng":117.13116999999993,"lat":36.67503591439761},"formatted_address":"山东省济南市历下区","business":"","addressComponent":{"country":"中国","country_code":0,"province":"山东省","city":"济南市","district":"历下区","adcode":"370102","street":"","street_number":"","direction":"","distance":""},"pois":[{"addr":"历下区","cp":" ","direction":"西南","distance":"215","name":"雅居园小区4区2号楼","poiType":"房地产","point":{"x":117.1322652577601,"y":36.676325684330166},"tag":"房地产;住宅区","tel":"","uid":"2b9b19b96913ebbbbf4842e0","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"历下区","cp":" ","direction":"西","distance":"317","name":"雅居园小区4区7号楼","poiType":"房地产","point":{"x":117.13399898739435,"y":36.675341555574214},"tag":"房地产;住宅区","tel":"","uid":"c9964951d9e62006a3acc489","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"历下区高新技术开发区伯乐路998号","cp":" ","direction":"西北","distance":"487","name":"山东省干部学院","poiType":"教育培训","point":{"x":117.13486136068393,"y":36.6731416924391},"tag":"教育培训;高等院校","tel":"","uid":"50f7d146621b85dd72210bff","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"济南市历下区新泺大街2222号","cp":" ","direction":"南","distance":"403","name":"雅居园2区3号楼","poiType":"房地产","point":{"x":117.1316364439031,"y":36.67793210251718},"tag":"房地产;住宅区","tel":"","uid":"52cad26dca64728eb9df8f88","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"济南市历下区新泺大街2222号","cp":" ","direction":"南","distance":"540","name":"雅居园","poiType":"房地产","point":{"x":117.1320855966581,"y":36.678880018095576},"tag":"房地产;住宅区","tel":"","uid":"be54cde92de6375979f79683","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"山东省济南市历下区浪潮路","cp":" ","direction":"西南","distance":"418","name":"雅居园三区1栋","poiType":"房地产","point":{"x":117.13411576711064,"y":36.67691181380548},"tag":"房地产;住宅区","tel":"","uid":"f11659cb15402850f942b2c8","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"山东省济南市历下区伯乐路998","cp":" ","direction":"西北","distance":"518","name":"名士轩","poiType":"酒店","point":{"x":117.13498712345533,"y":36.6728811781294},"tag":"酒店","tel":"","uid":"bcaebff8c51d9e4dc9e33c58","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"浪潮路99号","cp":" ","direction":"西南","distance":"497","name":"山东师范大学附属小学雅居园校区","poiType":"教育培训","point":{"x":117.13483441151863,"y":36.677099953175876},"tag":"教育培训;小学","tel":"","uid":"56249586471ee010a8f7298e","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"济南市历下区崇华路1111号","cp":" ","direction":"西南","distance":"583","name":"顺鑫缘超市","poiType":"购物","point":{"x":117.13457390292074,"y":36.67824325167777},"tag":"购物;超市","tel":"","uid":"d8e0b1e4108ade8c2a11d183","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"新泺大街2566","cp":" ","direction":"东南","distance":"597","name":"济南市公安局高新区分局侦查大队","poiType":"政府机构","point":{"x":117.12851932378348,"y":36.678793186624404},"tag":"政府机构;公检法机构","tel":"","uid":"9a2511230697b9db2ce67be9","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}}],"roads":[],"poiRegions":[],"sematic_description":"雅居园小区4区2号楼西南215米","cityCode":288}
     */

    public int status;
    public ResultBean result;

    public static class ResultBean {
        /**
         * location : {"lng":117.13116999999993,"lat":36.67503591439761}
         * formatted_address : 山东省济南市历下区
         * business :
         * addressComponent : {"country":"中国","country_code":0,"province":"山东省","city":"济南市","district":"历下区","adcode":"370102","street":"","street_number":"","direction":"","distance":""}
         * pois : [{"addr":"历下区","cp":" ","direction":"西南","distance":"215","name":"雅居园小区4区2号楼","poiType":"房地产","point":{"x":117.1322652577601,"y":36.676325684330166},"tag":"房地产;住宅区","tel":"","uid":"2b9b19b96913ebbbbf4842e0","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"历下区","cp":" ","direction":"西","distance":"317","name":"雅居园小区4区7号楼","poiType":"房地产","point":{"x":117.13399898739435,"y":36.675341555574214},"tag":"房地产;住宅区","tel":"","uid":"c9964951d9e62006a3acc489","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"历下区高新技术开发区伯乐路998号","cp":" ","direction":"西北","distance":"487","name":"山东省干部学院","poiType":"教育培训","point":{"x":117.13486136068393,"y":36.6731416924391},"tag":"教育培训;高等院校","tel":"","uid":"50f7d146621b85dd72210bff","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"济南市历下区新泺大街2222号","cp":" ","direction":"南","distance":"403","name":"雅居园2区3号楼","poiType":"房地产","point":{"x":117.1316364439031,"y":36.67793210251718},"tag":"房地产;住宅区","tel":"","uid":"52cad26dca64728eb9df8f88","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"济南市历下区新泺大街2222号","cp":" ","direction":"南","distance":"540","name":"雅居园","poiType":"房地产","point":{"x":117.1320855966581,"y":36.678880018095576},"tag":"房地产;住宅区","tel":"","uid":"be54cde92de6375979f79683","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"山东省济南市历下区浪潮路","cp":" ","direction":"西南","distance":"418","name":"雅居园三区1栋","poiType":"房地产","point":{"x":117.13411576711064,"y":36.67691181380548},"tag":"房地产;住宅区","tel":"","uid":"f11659cb15402850f942b2c8","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"山东省济南市历下区伯乐路998","cp":" ","direction":"西北","distance":"518","name":"名士轩","poiType":"酒店","point":{"x":117.13498712345533,"y":36.6728811781294},"tag":"酒店","tel":"","uid":"bcaebff8c51d9e4dc9e33c58","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"浪潮路99号","cp":" ","direction":"西南","distance":"497","name":"山东师范大学附属小学雅居园校区","poiType":"教育培训","point":{"x":117.13483441151863,"y":36.677099953175876},"tag":"教育培训;小学","tel":"","uid":"56249586471ee010a8f7298e","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"济南市历下区崇华路1111号","cp":" ","direction":"西南","distance":"583","name":"顺鑫缘超市","poiType":"购物","point":{"x":117.13457390292074,"y":36.67824325167777},"tag":"购物;超市","tel":"","uid":"d8e0b1e4108ade8c2a11d183","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}},{"addr":"新泺大街2566","cp":" ","direction":"东南","distance":"597","name":"济南市公安局高新区分局侦查大队","poiType":"政府机构","point":{"x":117.12851932378348,"y":36.678793186624404},"tag":"政府机构;公检法机构","tel":"","uid":"9a2511230697b9db2ce67be9","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}}]
         * roads : []
         * poiRegions : []
         * sematic_description : 雅居园小区4区2号楼西南215米
         * cityCode : 288
         */

        public LocationBean location;
        public String formatted_address;
        public String business;
        public AddressComponentBean addressComponent;
        public String sematic_description;
        public int cityCode;
        public List<PoisBean> pois;
        public List<?> roads;
        public List<?> poiRegions;

        public static class LocationBean {
            /**
             * lng : 117.13116999999993
             * lat : 36.67503591439761
             */

            public double lng;
            public double lat;
        }

        public static class AddressComponentBean {
            /**
             * country : 中国
             * country_code : 0
             * province : 山东省
             * city : 济南市
             * district : 历下区
             * adcode : 370102
             * street :
             * street_number :
             * direction :
             * distance :
             */

            public String country;
            public int country_code;
            public String province;
            public String city;
            public String district;
            public String adcode;
            public String street;
            public String street_number;
            public String direction;
            public String distance;
        }

        public static class PoisBean {
            /**
             * addr : 历下区
             * cp :
             * direction : 西南
             * distance : 215
             * name : 雅居园小区4区2号楼
             * poiType : 房地产
             * point : {"x":117.1322652577601,"y":36.676325684330166}
             * tag : 房地产;住宅区
             * tel :
             * uid : 2b9b19b96913ebbbbf4842e0
             * zip :
             * parent_poi : {"name":"","tag":"","addr":"","point":{"x":0,"y":0},"direction":"","distance":"","uid":""}
             */

            public String addr;
            public String cp;
            public String direction;
            public String distance;
            public String name;
            public String poiType;
            public PointBean point;
            public String tag;
            public String tel;
            public String uid;
            public String zip;
            public ParentPoiBean parent_poi;

            public static class PointBean {
                /**
                 * x : 117.1322652577601
                 * y : 36.676325684330166
                 */

                public double x;
                public double y;
            }

            public static class ParentPoiBean {
                /**
                 * name :
                 * tag :
                 * addr :
                 * point : {"x":0,"y":0}
                 * direction :
                 * distance :
                 * uid :
                 */

                public String name;
                public String tag;
                public String addr;
                public PointBeanX point;
                public String direction;
                public String distance;
                public String uid;

                public static class PointBeanX {
                    /**
                     * x : 0
                     * y : 0
                     */

                    public int x;
                    public int y;
                }
            }
        }
    }
}
