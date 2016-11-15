package com.gaokao366.gaokao366touser.model.version.parser;

import com.alibaba.fastjson.JSONObject;
import com.gaokao366.gaokao366touser.model.framework.parser.BaseParser;
import com.gaokao366.gaokao366touser.model.version.bean.VersionBean;

/**
 * Created by hh on 2016/5/11.
 */
public class VersionParser extends BaseParser<VersionBean> {
    @Override
    public VersionBean parse(String paramString) {
        VersionBean result = JSONObject.parseObject(paramString, VersionBean.class);
        return result;
    }
}
