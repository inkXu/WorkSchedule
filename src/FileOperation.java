import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

//文件操作类，有xls文件的读写方法和txt文件的读取方法
public class FileOperation {
    //获取path下的文件名
    static ArrayList<String> queryFileName(String path){
        ArrayList<String> filelist = new ArrayList<String>();
        try {
            File file = new File(path);
            if(!file.exists()){
                System.out.println("路径" + path + "不存在");
                throw new FileNotFoundException();
            }
            File fileArr[] = file.listFiles();
            for(int i=0;i<fileArr.length;i++){
                // File fs = fileArr[i];
                if(fileArr[i].isFile()){
                    // System.out.println(fileArr[i].getName());
                    filelist.add(fileArr[i].getName());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filelist;
    }
    //读取xls文件，Excel文件
    static ArrayList<String> readFile(String fName) throws IOException {
        //new code
        ArrayList<String> list = new ArrayList<String>();
        Sheet sheet;
        Workbook workbook;
        Cell cell;
        try{
            workbook = Workbook.getWorkbook(new File(fName));
            sheet = workbook.getSheet(0);
            int i=1;
            String row = "";
            while(true){
                row = "";
                for(int j=1;j<8;j++){
                    cell = sheet.getCell(j,i);//(列,行)
                    //如果读取的数据为空
                    if("".equals(cell.getContents())){
                        continue;
                    }
                    //System.out.println(cell.getContents() + "\t\t");
                    row += cell.getContents() + "\t\t";
                }
//                System.out.println(row.substring(0,row.length()-2));
                list.add(row);
//                System.out.println("");
//                if("".equals(name)){break;}
                i++;
                if("".equals(sheet.getCell(2,i).getContents())){break;}
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    //读取txt文档
    static ArrayList<String> readTxtFile(String fName) throws IOException {
        //old code
        //读取txt文件
        File file = new File(fName);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("utf-8")));
        ArrayList<String> list = new ArrayList<String>();
        String readline = null;
        while( (readline = br.readLine()) != null){
            list.add(readline);
        }
        br.close();
        return list;
    }
    static void nightWorkTable(NightTable table){
        //将数据写入文件
        File writefile;
        BufferedWriter bw;
        boolean append = true;  //  是否追加
        String path = "./workTable/值班表.txt";
        writefile = new File(path);
        if (writefile.exists() == false)
        {
            try {
                writefile.createNewFile();
                writefile = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {        // 存在先删除，再创建
            writefile.delete();
            try {
                writefile.createNewFile();
                writefile = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fw = new FileWriter(writefile, append);
            bw = new BufferedWriter(fw);
            fw.write("值班与机动表\n");
            for(int i=0;i<table.jd.length;i++){
                for(int j=0;j<table.jd[i].length;j++){
                    fw.write(table.jd[i][j]+"、");
                }
                fw.write("\n");
                fw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (writefile.exists() == false)
        {
            try {
                writefile.createNewFile();
                writefile = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {        // 存在先删除，再创建
            writefile.delete();
            try {
                writefile.createNewFile();
                writefile = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fw = new FileWriter(writefile, append);
            bw = new BufferedWriter(fw);
            fw.write("\n维护表\n");
            for(int i=0;i<table.wh.length;i++){
                for(int j=0;j<table.wh[i].length;j++){
                    fw.write(table.wh[i][j]+"、");
                }
                fw.write("\n");
                fw.flush();
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void daytimeWorkTable(DaytimeTable table){
        //写入文本文档
        File writefile;
        BufferedWriter bw;
        boolean append = true;  //  是否追加
        String path = "./workTable/轮班表.txt";
        writefile = new File(path);
        if (writefile.exists() == false)
        {
            try {
                writefile.createNewFile();
                writefile = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {        // 存在先删除，再创建
            writefile.delete();
            try {
                writefile.createNewFile();
                writefile = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fw = new FileWriter(writefile, append);
            bw = new BufferedWriter(fw);
            fw.write("轮班表\n");
            fw.write("星期一：\n");
            for(int i = 0; i<table.monday.length; i++){
                fw.write("\t");
                for(int j = 0; j<table.monday[i].length; j++){
                    fw.write(table.monday[i][j]+"、");
                }
                fw.write("\n");
            }
            fw.write("星期二：\n");
            for(int i = 0; i<table.tuesday.length; i++){
                fw.write("\t");
                for(int j = 0; j<table.tuesday[i].length; j++){
                    fw.write(table.tuesday[i][j]+"、");
                }
                fw.write("\n");
            }
            fw.write("星期三：\n");
            for(int i = 0; i<table.wednesday.length; i++){
                fw.write("\t");
                for(int j = 0; j<table.wednesday[i].length; j++){
                    fw.write(table.wednesday[i][j]+"、");
                }
                fw.write("\n");
            }
            fw.write("星期四：\n");
            for(int i = 0; i<table.thursday.length; i++){
                fw.write("\t");
                for(int j = 0; j<table.thursday[i].length; j++){
                    fw.write(table.thursday[i][j]+"、");
                }
                fw.write("\n");
            }
            fw.write("星期五：\n");
            for(int i = 0; i<table.friday.length; i++){
                fw.write("\t");
                for(int j = 0; j<table.friday[i].length; j++){
                    fw.write(table.friday[i][j]+"、");
                }
                fw.write("\n");
            }
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //向xls文件写入数据
    static void outputDaytimeXlsFile(DaytimeTable table){
        try{
            File file=new File("./workTable/14-17周轮班表.xls");
            if(!file.exists())
                file.createNewFile();
            Workbook inputWorkbook = Workbook.getWorkbook(new File("workTable/10-13周轮班表.xls"));
            Sheet inputSheet = inputWorkbook.getSheet(0);
            WritableWorkbook outWorkbook = Workbook.createWorkbook(file);
            WritableSheet outSheet = outWorkbook.createSheet("轮班表",0); //sheet名字,第几页
            //System.out.println(label.getContents());
            outSheet.addCell(new Label(0,0,inputSheet.getCell(0,0).getContents()));
            for(int i=0;i<6;i++){
                Label label = new Label(i,1,inputSheet.getCell(i,1).getContents());  //列 行 内容
                outSheet.addCell(label);
//                System.out.println(label.getContents());
//                System.out.println(data);
            }
            for(int i=2;i<6;i++) {
                Label label = new Label(0, i, inputSheet.getCell(0, i).getContents().substring(0, inputSheet.getCell(0, i).getContents().length() - 1));  //列 行 内容
                outSheet.addCell(label);
            }
            for(int i = 0; i<table.monday.length; i++){
                String temp = "";
                if(table.monday[i].length <= 1)
                    continue;
                for(int ii = 1; ii<table.monday[i].length; ii++){
                    temp += table.monday[i][ii]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                outSheet.addCell(new Label(1,i+2,temp));
            }
            for(int i = 0; i<table.tuesday.length; i++){
                String temp = "";
                if(table.tuesday[i].length <= 1)
                    continue;
                for(int ii = 1; ii<table.tuesday[i].length; ii++){
                    temp += table.tuesday[i][ii]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                outSheet.addCell(new Label(2,i+2,temp));
            }
            for(int i = 0; i<table.wednesday.length; i++){
                String temp = "";
                if(table.wednesday[i].length <= 1)
                    continue;
                for(int ii = 1; ii<table.wednesday[i].length; ii++){
                    temp += table.wednesday[i][ii]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                outSheet.addCell(new Label(3,i+2,temp));
            }
            for(int i = 0; i<table.thursday.length; i++){
                String temp = "";
                if(table.thursday[i].length <= 1)
                    continue;
                for(int ii = 1; ii<table.thursday[i].length; ii++){
                    temp += table.thursday[i][ii]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                outSheet.addCell(new Label(4,i+2,temp));
            }
            for(int i = 0; i<table.friday.length; i++){
                String temp = "";
                if(table.friday[i].length <= 1)
                    continue;
                for(int ii = 1; ii<table.friday[i].length; ii++){
                    temp += table.friday[i][ii]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                outSheet.addCell(new Label(5,i+2,temp));
            }
            outWorkbook.write();  //最后添加完数据调用
            outWorkbook.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    static void outputNightXlsFile(NightTable table){
        try{
            File file=new File("./workTable/14-17周值班表.xls");
            if(!file.exists())
                file.createNewFile();
//            Workbook inputWorkbook = Workbook.getWorkbook(new File("workTable/10-13周值班表.xls"));
//            Sheet inputSheet = inputWorkbook.getSheet(0);
            WritableWorkbook outWorkbook = Workbook.createWorkbook(file);
            WritableSheet outSheet = outWorkbook.createSheet("值班表",0); //sheet名字,第几页
            //System.out.println(label.getContents());
            int index = 0;
            for(int i=0;i<table.jd.length;i++){
                outSheet.addCell(new Label(index,0,table.jd[i][0]));
                outSheet.addCell(new Label(index+1,0,""));
                index += 2;
            }
            index = 0;
            for(int i=0;i<table.zb.length;i++){
                if(table.zb[i].length <= 1){
                    outSheet.addCell(new Label(index,1,""));
                    outSheet.addCell(new Label(index+1,1,""));
                    index += 2;
                    continue;
                }
                String temp = table.zb[i][1];
                outSheet.addCell(new Label(index,1,temp));
                outSheet.addCell(new Label(index+1,1,""));
                index += 2;
            }
            index = 0;
            for(int i=0;i<table.jd.length;i++){
                if(table.jd[i].length <= 1){
                    outSheet.addCell(new Label(index,2,""));
                    outSheet.addCell(new Label(index+1,2,""));
                    index += 2;
                    continue;
                }
                String temp = "";
                for(int j=1;j<table.jd[i].length;j++){
                    temp += table.jd[i][j]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                outSheet.addCell(new Label(index,2,temp));
                outSheet.addCell(new Label(index+1,2,""));
                index += 2;
            }
            index = 0;
            for(int i=0;i<table.wh.length;i++){
                if(table.wh[i].length <= 1){
                    outSheet.addCell(new Label(index,3,""));
                    outSheet.addCell(new Label(index+1,3,""));
                    index += 2;
                    continue;
                }
                String temp = "";
                for(int j=1;j<table.wh[i].length;j++){
                    temp += table.wh[i][j]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                outSheet.addCell(new Label(index,3,temp));
                outSheet.addCell(new Label(index+1,3,""));
                index += 2;
            }
//            int i=0;
//            while(true){
//                if("".equals(inputSheet.getCell(0,i).getContents()) && "".equals(inputSheet.getCell(1,i).getContents()) && "".equals(inputSheet.getCell(2,i).getContents()))
//                    break;
//                int j=0;
//                while(true){
//                    if("".equals(inputSheet.getCell(j,i).getContents())     && "".equals(inputSheet.getCell(j+1,i).getContents())
//                    && "".equals(inputSheet.getCell(j+2,i).getContents()) && "".equals(inputSheet.getCell(j+3,i).getContents()) )
//                        break;
//                    if("".equals(inputSheet.getCell(j,i).getContents())){
//                        j++;
//                        continue;
//                    }
//                    outSheet.addCell(new Label(j,i,inputSheet.getCell(j,i).getContents()));
//                    j++;
//                }
//                i++;
//            }
            outWorkbook.write();
            outWorkbook.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static void outputDaytimeXlsxFile(DaytimeTable table, ArrayList<String> worktable_template){
        String template_path = "./schedule/" + worktable_template.get(0);
        try {
            FileInputStream fis = new FileInputStream(template_path);
            XSSFWorkbook sheets = new XSSFWorkbook(fis);
            XSSFSheet lb_sheet = sheets.getSheet("轮班表");
            FileOutputStream fos = new FileOutputStream(template_path);
            String temp = "";
            for(int i = 0; i<table.monday.length; i++){
                temp = "";
                if(table.monday[i].length <= 1)
                    continue;
                for(int ii = 1; ii<table.monday[i].length; ii++){
                    temp += table.monday[i][ii]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                XSSFCell cell = lb_sheet.getRow(4+i).getCell(1);
                cell.setCellValue(temp);
            }
            for(int i = 0; i<table.tuesday.length; i++){
                temp = "";
                if(table.tuesday[i].length <= 1)
                    continue;
                for(int ii = 1; ii<table.tuesday[i].length; ii++){
                    temp += table.tuesday[i][ii]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                XSSFCell cell = lb_sheet.getRow(4+i).getCell(2);
                cell.setCellValue(temp);
            }
            for(int i = 0; i<table.wednesday.length; i++){
                temp = "";
                if(table.wednesday[i].length <= 1)
                    continue;
                for(int ii = 1; ii<table.wednesday[i].length; ii++){
                    temp += table.wednesday[i][ii]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                XSSFCell cell = lb_sheet.getRow(4+i).getCell(3);
                cell.setCellValue(temp);
            }
            for(int i = 0; i<table.thursday.length; i++){
                temp = "";
                if(table.thursday[i].length <= 1)
                    continue;
                for(int ii = 1; ii<table.thursday[i].length; ii++){
                    temp += table.thursday[i][ii]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                XSSFCell cell = lb_sheet.getRow(4+i).getCell(4);
                cell.setCellValue(temp);
            }
            for(int i = 0; i<table.friday.length; i++){
                temp = "";
                if(table.friday[i].length <= 1)
                    continue;
                for(int ii = 1; ii<table.friday[i].length; ii++){
                    temp += table.friday[i][ii]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                XSSFCell cell = lb_sheet.getRow(4+i).getCell(5);
                cell.setCellValue(temp);
            }
            sheets.write(fos);
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void outputNightXlsxFile(NightTable table, ArrayList<String> worktable_template){
        String template_path = "./schedule/" + worktable_template.get(0);
        try {
            FileInputStream fis = new FileInputStream(template_path);
            XSSFWorkbook sheets = new XSSFWorkbook(fis);
            XSSFSheet lb_sheet = sheets.getSheet("值班表");
            FileOutputStream fos = new FileOutputStream(template_path);
            int index = 0;
            for(int i=0;i<table.zb.length;i++){
                XSSFCell cell = lb_sheet.getRow(4).getCell(index + 2);
                if(table.zb[i].length <= 1){
                    cell.setCellValue("");
                    index += 2;
                    continue;
                }
                String temp = table.zb[i][1];
                cell.setCellValue(temp);
                index += 2;
            }
            index = 0;
            for(int i=0;i<table.jd.length;i++){
                XSSFCell cell = lb_sheet.getRow(5).getCell(index + 2);
                if(table.jd[i].length <= 1){
                    cell.setCellValue("");
                    index += 2;
                    continue;
                }
                String temp = "";
                for(int j=1;j<table.jd[i].length;j++){
                    temp += table.jd[i][j]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                cell.setCellValue(temp);
                index += 2;
            }
            index = 0;
            for(int i=0;i<table.wh.length;i++){
                XSSFCell cell = lb_sheet.getRow(6).getCell(index + 2);
                if(table.wh[i].length <= 1){
                    cell.setCellValue("");
                    index += 2;
                    continue;
                }
                String temp = "";
                for(int j=1;j<table.wh[i].length;j++){
                    temp += table.wh[i][j]+"、";
                }
                temp = temp.substring(0,temp.length()-1);
                cell.setCellValue(temp);
                index += 2;
            }
            sheets.write(fos);
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
