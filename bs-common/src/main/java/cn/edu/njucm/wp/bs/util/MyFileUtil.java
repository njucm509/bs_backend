package cn.edu.njucm.wp.bs.util;

import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class MyFileUtil {
    public static String[] readHeaderFromCsv(File file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = br.readLine();
        String[] split = line.split(",");
        return split;
    }

    public static String[] readHeaderFromExcel(File file) throws IOException {
        FileOutputStream fos = null;
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new HSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        List<String> list = new ArrayList<>();
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            list.add(cellIterator.next().getStringCellValue());
        }
        return list.toArray(new String[0]);
    }

    public static File convertExcelToCsv(File file) throws IOException {
        File csv = File.createTempFile(file.getName().split("\\.")[0], "csv");
        Workbook wb = null;
        if (file.getName().endsWith("xlsx")) {
            wb = StreamingReader.builder().bufferSize(4096).rowCacheSize(200).open(file);
        }
        if (file.getName().endsWith("xls")) {
            wb = new HSSFWorkbook(new FileInputStream(file));
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv)));
        Sheet sheetAt = wb.getSheetAt(0);
        for (Row row : sheetAt) {
            for (Cell cell : row) {
                if (cell.getCellTypeEnum() == CellType.STRING) {
                    bw.write(cell.getStringCellValue() + ",");
                }
                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    bw.write(String.valueOf(cell.getNumericCellValue()).split("\\.")[0] + ",");
                }
                if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
                    bw.write(String.valueOf(cell.getBooleanCellValue()).split("\\.")[0] + ",");
                }
            }
            bw.newLine();
        }

        if (wb != null) {
            wb.close();
        }
        if (bw != null) {
            bw.close();
        }
        return csv;
    }

    public static void main(String[] args) throws IOException {
        File file = MyFileUtil.convertExcelToCsv(new File("/Users/weilin/Downloads/test_data.xlsx"));
        System.out.println(file.getName());
        System.out.println(file.getAbsolutePath());
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}
