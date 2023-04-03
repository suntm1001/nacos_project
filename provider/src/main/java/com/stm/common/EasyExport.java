package com.stm.common;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.stm.Interceptor.ExcelWidthStyleStrategy;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class EasyExport {
    /**
     * easyExcel导出案例
     * @param request
     * @param response
     * @param templateFileName
     * @param fileName
     */
    public void doExcel(HttpServletRequest request, HttpServletResponse response, String templateFileName, String fileName){
        String userAgent=request.getHeader("USER-AGENT");
        String finalFileName="";
        try {
            if(StringUtils.contains(userAgent,"Mozilla")){//google 火狐浏览器
                finalFileName = new String(fileName.getBytes(),"ISO8859-1");
            }else if(StringUtils.contains(userAgent,"MSIE") ||
                    StringUtils.contains(userAgent,"Trident")){ // IE其他浏览器

            }
        }catch (Exception e){
            log.info("流编码异常"+e.getMessage());
        }
        response.addHeader("Content-Disposition","attachment;filename=\""+finalFileName+"\"");//下载文件名称
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        log.info("文件：{}缓存完成，开始写出",fileName);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control","no-store");
        response.addHeader("Cache-Control","max-age=0");

        //查询总页数
        long totalPage=0;
        //总页数=0则不导出
        if(totalPage<=0){
            log.info("无导出数据");
        }
        //获取导出模板
        ClassPathResource classPathResource = new ClassPathResource("/templates/"+templateFileName);
        //输入流
        InputStream inputStream = null;
        //输出流
        ServletOutputStream outputStream=null;
        //excel对象
        ExcelWriter excelWriter = null;
        log.info("导出中...");

        try{
            inputStream = classPathResource.getInputStream();
            log.info("写入数据中...");
            outputStream = response.getOutputStream();
            //设置输出流和模板信息
            excelWriter= EasyExcel.write(outputStream).withTemplate(inputStream).build();

            //头部样式
            WriteCellStyle head = new WriteCellStyle();
            head.setFillBackgroundColor(IndexedColors.PALE_BLUE.getIndex());
            //内容样式
            WriteCellStyle contentStyle = new WriteCellStyle();
            //自动换行
            contentStyle.setWrapped(true);
            //单元格边框变为黑色实线
            contentStyle.setBorderBottom(BorderStyle.THIN);
            contentStyle.setBorderTop(BorderStyle.THIN);
            contentStyle.setBorderLeft(BorderStyle.THIN);
            contentStyle.setBorderRight(BorderStyle.THIN);
            //将样式添加到策略中
            HorizontalCellStyleStrategy strategy = new HorizontalCellStyleStrategy(head,contentStyle);
            //自定义样式-设置单元格宽度
            ExcelWidthStyleStrategy excelWidthStyleStrategy = new ExcelWidthStyleStrategy();
            //如果需要设置单元格样式则使用此方法
            //WriteSheet writeSheet = EasyExcel.writerSheet().registerWriteHandler(strategy)
            // .registerWriteHandler(excelWidthStyleStrategy).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            //开启横向填充
            //FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            //如果集合是横向填充使用此表达式‘
            //List<HashMap<String,Object>> horizontalList = new ArrayList<>();
            //一个模板中存在多个集合，需要使用new FillWrapper("data1",horizontalList)方法
            //excelWriter.fill(new FillWrapper("data1",horizontalList),fillConfig,writeSheet);
            if(totalPage>=1){
                int realPageNo=1;
                while (realPageNo<=totalPage){
                    List<HashMap<String,Object>> list = new ArrayList<>();
                    if(null == list){
                        return;
                    }
                    //填充数据
                    excelWriter.fill(list,writeSheet);
                    realPageNo ++;
                    list.clear();
                }
            }
            //完成
            excelWriter.finish();
        }catch (Exception e){
            log.error("导出失败："+e.getMessage());
        }finally {
            if(null != excelWriter){
                excelWriter.finish();
            }
            if(null != outputStream){
                try {
                    outputStream.close();
                }catch (IOException e){
                    log.error("流关闭异常");
                }

            }
            if(null != inputStream){
                try {
                    inputStream.close();
                }catch (IOException e){
                    log.error("流关闭异常");
                }

            }
        }
    }
}
