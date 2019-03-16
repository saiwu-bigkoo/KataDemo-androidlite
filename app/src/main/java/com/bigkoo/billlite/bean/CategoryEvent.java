package com.bigkoo.billlite.bean;

public class CategoryEvent {

    public static final int ADD = 0;
    public static final int DELETE = -1;

    private int eventType;
    private CategoryBean categoryBean;

    public CategoryEvent(int eventType, CategoryBean categoryBean) {
        this.eventType = eventType;
        this.categoryBean = categoryBean;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public CategoryBean getCategoryBean() {
        return categoryBean;
    }

    public void setCategoryBean(CategoryBean categoryBean) {
        this.categoryBean = categoryBean;
    }
}
