package com.eeesns.tshow.common.json;


public class JsonBean extends AbstractJsonBean
{

    public JsonBean()
    {
    }

    public Object getBean()
    {
        return bean;
    }

    public void setBean(Object bean)
    {
        this.bean = bean;
    }

    private Object bean;
}

