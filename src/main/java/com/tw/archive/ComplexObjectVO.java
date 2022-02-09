package com.tw.archive;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplexObjectVO {
    private List<HelloVO> helloVOList = new ArrayList<>();
    private HelloVO currentHelloVO;
    private Map<String, HelloVO> aliasToHelloMap = new HashMap<>();

    public static ComplexObjectVO getInstance() {
        ComplexObjectVO complexObjectVO = new ComplexObjectVO();
        HelloVO helloVO = getHelloVO("alice", new SubPage1VO());

        complexObjectVO.setCurrentHelloVO(helloVO);

        complexObjectVO.helloVOList.add(getHelloVO("bob", new SubPage1VO("bobSub")));
        HelloVO charlie = getHelloVO("Charlie", new SubPage1VO("cSub"));
        complexObjectVO.helloVOList.add(charlie);
        complexObjectVO.helloVOList.add(getHelloVO("dave", new SubPage1VO("dSub")));

        complexObjectVO.aliasToHelloMap.put("Carol", charlie);
        complexObjectVO.aliasToHelloMap.put("eve", getHelloVO("eavesdropper", new SubPage1VO("eSub")));

        return complexObjectVO;
    }

    private static HelloVO getHelloVO(String name, SubPage1VO subPage1VO) {
        HelloVO helloVO = new HelloVO(name, true);
        helloVO.setSubPage1VO(subPage1VO);
        return helloVO;
    }

    public List<HelloVO> getHelloVOList() {
        return helloVOList;
    }

    public void setHelloVOList(List<HelloVO> helloVOList) {
        this.helloVOList = helloVOList;
    }

    public HelloVO getCurrentHelloVO() {
        return currentHelloVO;
    }

    public void setCurrentHelloVO(HelloVO currentHelloVO) {
        this.currentHelloVO = currentHelloVO;
    }

    public Map<String, HelloVO> getAliasToHelloMap() {
        return aliasToHelloMap;
    }

    public void setAliasToHelloMap(Map<String, HelloVO> aliasToHelloMap) {
        this.aliasToHelloMap = aliasToHelloMap;
    }


    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
