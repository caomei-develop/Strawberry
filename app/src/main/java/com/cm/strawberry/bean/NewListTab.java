package com.cm.strawberry.bean;

import java.util.List;

/**
 * Created by zhouwei on 17-7-28.
 */

public class NewListTab {
    public String ctgId;
    public String name;
    public String parentId;

    public NewListTab(String ctgId, String name, String parentId) {
        this.ctgId = ctgId;
        this.name = name;
        this.parentId = parentId;
    }

    public String getCtgId() {
        return ctgId;
    }

    public void setCtgId(String ctgId) {
        this.ctgId = ctgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
