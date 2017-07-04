package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/6/16.
 */

public class AddressBean {

    /**
     * areaId : 110000
     * areaName : 北京市
     * cities : [{"areaId":"110000","areaName":"北京市","counties":[{"areaId":"110101","areaName":"东城区"},{"areaId":"110102","areaName":"西城区"},{"areaId":"110105","areaName":"朝阳区"},{"areaId":"110106","areaName":"丰台区"},{"areaId":"110107","areaName":"石景山区"},{"areaId":"110108","areaName":"海淀区"},{"areaId":"110109","areaName":"门头沟区"},{"areaId":"110111","areaName":"房山区"},{"areaId":"110112","areaName":"通州区"},{"areaId":"110113","areaName":"顺义区"},{"areaId":"110114","areaName":"昌平区"},{"areaId":"110115","areaName":"大兴区"},{"areaId":"110116","areaName":"怀柔区"},{"areaId":"110117","areaName":"平谷区"},{"areaId":"110228","areaName":"密云县"},{"areaId":"110229","areaName":"延庆县"}]}]
     */

    public String areaId;
    public String areaName;
    public List<CitiesBean> cities;

    public static class CitiesBean {
        /**
         * areaId : 110000
         * areaName : 北京市
         * counties : [{"areaId":"110101","areaName":"东城区"},{"areaId":"110102","areaName":"西城区"},{"areaId":"110105","areaName":"朝阳区"},{"areaId":"110106","areaName":"丰台区"},{"areaId":"110107","areaName":"石景山区"},{"areaId":"110108","areaName":"海淀区"},{"areaId":"110109","areaName":"门头沟区"},{"areaId":"110111","areaName":"房山区"},{"areaId":"110112","areaName":"通州区"},{"areaId":"110113","areaName":"顺义区"},{"areaId":"110114","areaName":"昌平区"},{"areaId":"110115","areaName":"大兴区"},{"areaId":"110116","areaName":"怀柔区"},{"areaId":"110117","areaName":"平谷区"},{"areaId":"110228","areaName":"密云县"},{"areaId":"110229","areaName":"延庆县"}]
         */

        public String areaId;
        public String areaName;
        public List<CountiesBean> counties;

        public static class CountiesBean {
            /**
             * areaId : 110101
             * areaName : 东城区
             */

            public String areaId;
            public String areaName;
        }
    }
}
