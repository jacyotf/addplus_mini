package com.addplus.server.api.service.web.commonmodule;

import com.alibaba.excel.metadata.BaseRowModel;

import java.util.List;

/**
 * 类名: ExcelService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019/2/25 5:15 PM
 * @description 类描述: Excel处理Service
 */
public interface ExcelService {

    /**
     * 方法描述：生成excel文件并上传到oss服务器
     *
     * @param list BaseRowModel的数据list集合
     * @param sheetName excel文件的sheet表名 ( 传null默认为"sheet1")
     * @param c 数据模型class ( 继承BaseRowModel，与数据list泛型保持一致)
     * @return List<String> excel文件列表
     * @throws Exception
     * @author zhangjiehang
     * @date 2019/02/25 下午5:21
     * @throws Exception
     */
    List<String> makeExcelAndUpload(List<? extends BaseRowModel> list, String sheetName, Class c) throws Exception;
}
