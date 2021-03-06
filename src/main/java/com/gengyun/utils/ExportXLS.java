package com.gengyun.utils;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/6/26.
 */
public class ExportXLS {
    private static FileOutputStream fos;
    private static WritableSheet sheet;

    public WritableWorkbook createExcel(String xlsName,WritableWorkbook workbook) {
//        String path = "/home/hbase/excels/error_words_" + xlsName + ".xls";
        String path = "/Users/macbookpro/IdeaProjects/apiTest/data/" + xlsName + ".xls";
        try {
            fos = new FileOutputStream(path);
            //创建工作薄
            workbook = Workbook.createWorkbook(fos);
            //创建新的一页
            sheet = workbook.createSheet("First Sheet", 0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return workbook;
    }

    public void addRecord(int index, String url, String error_words) {
        try {
            //设置字体为Arial，30号，加粗
            CellView cellView = new CellView();
            cellView.setAutosize(true);
            sheet.setColumnView(0,cellView);
            sheet.setColumnView(1,cellView);
            //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
            Label title_url = new Label(0, 0, "URL");
            sheet.addCell(title_url);
            Label title_error_words = new Label(1, 0, "ERROR_WORDS");
            sheet.addCell(title_error_words);
            //set row
            int row = index + 1;
            Label label_url = new Label(0, row, url);
            sheet.addCell(label_url);
            Label label_error_words = new Label(1, row, error_words);
            sheet.addCell(label_error_words);
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }

    public void close(WritableWorkbook workbook) {
        //把创建的内容写入到输出流中，并关闭输出流
        try {
            workbook.write();
            workbook.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }




}
