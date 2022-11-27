import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Work {
    private static String startWeek = "";      //开始排班的周数
    private static String endWeek = "";        //结束排班的周数

    public static void main(String[] args) throws IOException{
        JFrame frame = new JFrame("小牛马排班");
        frame.setLayout(new BorderLayout());
        frame.setSize(500, 340);
        frame.setLocation(300, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea showArea = new JTextArea(12, 34);
        JScrollPane scrollPane = new JScrollPane(showArea);
        showArea.setEditable(false);

        JTextField startField = new JTextField(15);
        JTextField endField = new JTextField(15);

        JButton btnCreate = new JButton("生成");
        showArea.append("开发者: 20计网 @ 湫\n");
        showArea.append("追忆:   19锋   19乐   19贺   19晗   19祯   19熊   19爽\n");
        showArea.append("追忆:   18肖   20秋   20曾   20袁   20岱   20城\n");
        showArea.append("追忆:   21炎   21银   21方   21何   21珂   21翔\n");
        showArea.append("追忆:   21玲   21捷   21伦   21毅   21坤   21涵   21辉\n");

        showArea.append("开源地址: https://github.com/inkXu/WorkSchedule\n");
        showArea.append("使用前请查看说明! 谢谢配合!\n");
        btnCreate.addActionListener(e -> {
            FileOperation.clearInfoForFile("./error.log");
            startWeek = startField.getText();
            endWeek = endField.getText();
            if (startWeek == null || endWeek == null || startWeek.equals("") || endWeek.equals("")) {
                showArea.append("起始周和结束周不能为空!\n");
            } else if (Integer.parseInt(startWeek) < 0 || Integer.parseInt(endWeek) < 0
                    || Integer.parseInt(startWeek) > 19 || Integer.parseInt(endWeek) > 19) {
                showArea.append("起始周和结束周为1-18周范围!\n");
            } else if (Integer.parseInt(startWeek) >= Integer.parseInt(endWeek)) {
                showArea.append("起始周必须小于结束周!\n");
            }else {
                ArrayList<Employee> employees = new ArrayList<Employee>();
                String path = "./table";
                ArrayList<String> fileNameArr = FileOperation.queryFileName(path);
                for(int i=0;i<fileNameArr.size();i++){
                    String filepath = "./table/" + fileNameArr.get(i);

                    //读取txt文件
//                    ArrayList<String> list = FileOperation.readTxtFile(filepath);

                    //读取xls文件
                    ArrayList<String> list = null;
                    try {
                        list = FileOperation.readFile(filepath);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    String name = list.get(0);
                    name = name.substring(name.indexOf("姓名：")+3,name.indexOf("姓名：")+8).trim();
                    Employee employee = new Employee();
                    employee.name = name;
                    employee = GetWorkTime.DaytimeAndNight(list, employee, startWeek, endWeek);
                    employees.add(employee);
                }

                //读取txt文件
//                FileOperation.daytimeWorkTable(WorkTable.DaytimeTable(employees));
                //读取xls文件
//                FileOperation.outputDaytimeXlsFile(WorkTable.DaytimeTable(employees));

                //输出至txt文件
//                FileOperation.nightWorkTable(WorkTable.NightTable(employees));
                //输出至xls文件
//                FileOperation.outputNightXlsFile(WorkTable.NightTable(employees));

                FileOperation.outputDaytimeXlsxFile(OnWorkTable.onDaytimeTable(employees), FileOperation.queryFileName("./schedule"));
                FileOperation.outputNightXlsxFile(OnWorkTable.onNightTable(employees), FileOperation.queryFileName("./schedule"));
            }
            showArea.append("已完成排班，请查看该程序所在目录的error.log文件，检查程序是否报错!\n");
            startField.setText("");
            endField.setText("");
        });

        JButton btn_clear = new JButton("清除");
        btn_clear.addActionListener(e -> {
            showArea.setText("");
            showArea.append("开发者: 20计网 @ 湫\n");
            showArea.append("追忆:   19锋   19乐   19贺   19晗   19祯   19熊   19爽\n");
            showArea.append("追忆:   18肖   20秋   20曾   20袁   20岱   20城\n");
            showArea.append("追忆:   21炎   21银   21方   21何   21珂   21翔\n");
            showArea.append("追忆:   21玲   21捷   21伦   21毅   21坤   21涵   21辉\n");
            showArea.append("开源地址: https://github.com/inkXu/WorkSchedule\n");
        });

        JPanel panel_start = new JPanel();

        JLabel label_start = new JLabel("起始周");
        panel_start.add(label_start);
        panel_start.add(startField);

        JLabel label_end = new JLabel("结束周");
        panel_start.add(label_end);
        panel_start.add(endField);

        JPanel panel_btn = new JPanel();
        panel_btn.add(btnCreate);
        panel_btn.add(btn_clear);

        frame.add(scrollPane, BorderLayout.NORTH);
        frame.add(panel_start, BorderLayout.CENTER);
        frame.add(panel_btn, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

}


