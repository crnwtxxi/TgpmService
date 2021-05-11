package com.example.tgpmsystem.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Slf4j
public class ExcelUtils {

    /**
     * 获取并解析excel文件，返回一个二维集合
     * @param file 上传的文件
     * @return 二维集合（第一重集合为行，第二重集合为列，每一行包含该行的列集合，列集合包含该行的全部单元格的值）
     */
    public static ArrayList<ArrayList<String>> analysisExcel(@RequestParam(value = "file",required = false) MultipartFile file) {
        ArrayList<ArrayList<String>> row = new ArrayList<>();
        //获取文件名称
        String fileName = file.getOriginalFilename();
        System.out.println("fileName == > " + fileName);
        boolean version2007;
        try {
            //获取输入流
            InputStream in = file.getInputStream();
            //判断excel版本
            Workbook workbook = null;
            version2007 = judegExcelEdition(fileName);
            if (version2007) {
                //2007+
                workbook = new XSSFWorkbook(in);
            } else {
                //2007-
                workbook = new HSSFWorkbook(in);
            }
            //获取第一张工作表
            Sheet sheet = workbook.getSheetAt(0);
            //从第二行开始获取
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                //循环获取工作表的每一行，一行就是一条用户信息
                Row sheetRow = sheet.getRow(i);
                //循环获取每一列
                ArrayList<String> cell = new ArrayList<>();
                for (int j = 0; j < sheetRow.getPhysicalNumberOfCells(); j++) {
                    //将每一个单元的值装入集合
                    Cell numCell = sheetRow.getCell(j);
                    numCell.setCellType(CellType.STRING);
                    cell.add(numCell.getStringCellValue());
                }
                //将装有每一列的集合装入大集合
                row.add(cell);
                //关闭资源
                workbook.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.info("----------未找到文件---------");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("----------上传失败---------");
        }
        return row;
    }

    /**
     * 判断上传的excel文件版本（xls为2003，xlsx为2017）
     * @param fileName 文件路径
     * @return excel2007及以上版本返回true，excel2007以下版本返回false
     */
    private static boolean judegExcelEdition(String fileName) {
        if (fileName.matches("^.+\\.(?i)(xls)$")) {
            return false;
        } else {
            return true;
        }
    }

}
