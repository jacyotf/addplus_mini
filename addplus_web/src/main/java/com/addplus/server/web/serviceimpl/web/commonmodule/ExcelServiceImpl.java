package com.addplus.server.web.serviceimpl.web.commonmodule;

import com.addplus.server.api.service.web.commonmodule.ExcelService;
import com.addplus.server.web.oss.service.OssService;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    private Logger logger = LoggerFactory.getLogger(ExcelService.class);

    @Autowired
    private OssService ossService;

    @Override
    public List<String> makeExcelAndUpload(List<? extends BaseRowModel> list, String sheetName, Class clazz) throws Exception {
        File file = null;
        try {
            ExcelTypeEnum xlsx = ExcelTypeEnum.XLSX;
            // 创建临时file文件
            file = File.createTempFile("excel", xlsx.getValue());
            OutputStream out = new FileOutputStream(file);
            // 生成excel文件
            ExcelWriter writer = new ExcelWriter(out, xlsx);
            Sheet sheet = new Sheet(1, 0, clazz);
            if (null == sheetName) {
                sheetName = "sheet1";
            }
            sheet.setSheetName(sheetName);
            writer.write(list, sheet);
            writer.finish();
            out.close();
            // File对象转换MultipartFile对象
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            // 上传oss服务器
            List<MultipartFile> files = new ArrayList<MultipartFile>();
            files.add(multipartFile);
            List<String> strings = sendExcelToOss(files);
            fileInputStream.close();
            logger.info("----------导出并上传成功，下载地址为： " + strings.get(0));
            return strings;
        } catch (IOException e) {
            logger.info("----------导出失败，原因：");
            logger.info(e.getMessage());
        } finally {
            // 删除临时file文件
            File deleteFile = new File(file.toURI());
            deleteFile.delete();
            logger.info("----------临时文件删除成功----------");
        }
        return null;
    }

    /**
     * 方法描述： 上传oss服务器
     *
     * @param files
     * @return
     */
    private List<String> sendExcelToOss(List<MultipartFile> files) {
        return ossService.uploadFile(files, true, false);
    }
}
