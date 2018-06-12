package com.eeesns.tshow.common.json;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import sparknet.starshine.common.json.JsonDateValueProcessor;


public class AbstractJsonBean
{

    public AbstractJsonBean()
    {
        dateFormat = "yyyy-MM-dd";
    }

    public String getDateFormat()
    {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat)
    {
        this.dateFormat = dateFormat;
    }

    public String toJsonString()
    {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());
        return toJsonString(jsonConfig);
    }

    public String toJsonString(JsonConfig jsonConfig)
    {
        return JSONObject.fromObject(this, jsonConfig).toString();
    }

    private String dateFormat;
}
