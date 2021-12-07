package com.tw;

public class HelloVO {

    private String name;
    private boolean shouldShow;
    private SubPage1VO subPage1VO = new SubPage1VO();

    public HelloVO(String name, boolean shouldShow) {
        this.name = name;
        this.shouldShow = shouldShow;
    }

    public String getName() {
        return name;
    }

    public boolean isShouldShow() {
        return shouldShow;
    }

    public SubPage1VO getSubPage1VO() {
        return subPage1VO;
    }

    public void setSubPage1VO(SubPage1VO subPage1VO) {
        this.subPage1VO = subPage1VO;
    }
}
