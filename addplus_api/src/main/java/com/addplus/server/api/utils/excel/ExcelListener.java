package com.addplus.server.api.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fuyq
 * @date 2019/2/27
 */
public class ExcelListener<T> extends AnalysisEventListener<T> {

    private List<T> dataList = new ArrayList<>();

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        String jsonStr = JSON.toJSONString(t);
        dataList.add(t);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//        dataList.clear();
    }

    public List<T> getDataList() {
        return dataList;
    }
}
