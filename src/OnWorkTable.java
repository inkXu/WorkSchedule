import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//轮班和值班的正式排班
public class OnWorkTable {
    static NightTable onNightTable(ArrayList<Employee> list){

        ArrayList<Employee> employees = randomOrder(list);

        NightTable table = new NightTable();
        int maxJobsOfzb = 1;
        int maxJobsOfjd = 1;                                                               //一个班最大机动加值班的人数
        if (employees.size() > 20) {
            maxJobsOfjd = 2;
        }
        int maxJobsOfwh = ((employees.size() * 3 - (maxJobsOfjd + maxJobsOfzb - 1) * 7) / 7) + 2;
        if (maxJobsOfwh < 4) {
            maxJobsOfwh = 3;
        }

        for (Employee employee : employees) {
            String[] jd = employee.jdnum.split("");
            String[] wh = employee.whnum.split("");
            if (jd[0].length() < 1 || wh[0].length() < 1) {
                try {
                    String msg = employee.name + "不存在晚班时间，请查看值班表是否有误!";
                    throw new Exception(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            int flag = blackInsert(table, employee, maxJobsOfzb, maxJobsOfjd, maxJobsOfwh, 0);
            blackCallback(flag, table, employee, employees, maxJobsOfzb, maxJobsOfjd, maxJobsOfwh);
        }
        return table;
    }
    static DaytimeTable onDaytimeTable(ArrayList<Employee> list){

        ArrayList<Employee> employees = randomOrder(list);

        //计算一个轮班的最大人数为多少
        int employeeNum = employees.size();
        int maxJobsOfLb = 2;
        if(employeeNum > 6)
            maxJobsOfLb = (employeeNum*3)/(4*5)+1;
        DaytimeTable table = new DaytimeTable();
        for(int i=0;i<employees.size();i++){
            Employee employee = employees.get(i);
            int flag = whiteInsert(table, employee, maxJobsOfLb, 0);
            whiteCallback(flag, table, employee, employees, maxJobsOfLb);
        }
        return table;
    }
    private static ArrayList<Employee> randomOrder(ArrayList<Employee> employees) {
        Random rand = new Random();
        ArrayList<Employee> list = new ArrayList<>();
        do {
            int index = rand.nextInt(employees.size());
            Employee employee = employees.get(index);
            if (!list.contains(employee)) {
                list.add(employee);
            }
        } while (list.size() < employees.size());
        return list;
    }
    private static String whiteReplace(DaytimeTable table, Employee employee) {
        String[] lb = employee.lbnum.split("-");
        Random rand = new Random();
        ArrayList<String> exist = new ArrayList<>();
        boolean is = false;
        HasUseful temp = null;
        while (!is && exist.size() < lb.length) {
            String lb_time = lb[rand.nextInt(lb.length)];
            int week = Integer.parseInt(lb_time.substring(1, 2));
            int courseNum = Integer.parseInt(lb_time.substring(3, 4)) - 1;
            if (exist.contains(lb_time)) {
                continue;
            }
            exist.add(lb_time);
            switch (week) {
                case 1:
                    if (!Arrays.asList(table.monday[courseNum]).contains(employee.name)) {
                        temp = remove(table.monday[courseNum]);
                        table.monday[courseNum] = temp.course;
                        table.monday[courseNum][table.monday[courseNum].length - 1] = employee.name;
                        is = true;
                    }
                    break;
                case 2:
                    if (!Arrays.asList(table.tuesday[courseNum]).contains(employee.name)) {
                        temp = remove(table.tuesday[courseNum]);
                        table.tuesday[courseNum] = temp.course;
                        table.tuesday[courseNum][table.tuesday[courseNum].length - 1] = employee.name;
                        is = true;
                    }
                    break;
                case 3:
                    if (!Arrays.asList(table.wednesday[courseNum]).contains(employee.name)) {
                        temp = remove(table.wednesday[courseNum]);
                        table.wednesday[courseNum] = temp.course;
                        table.wednesday[courseNum][table.wednesday[courseNum].length - 1] = employee.name;
                        is = true;
                    }
                    break;
                case 4:
                    if (!Arrays.asList(table.thursday[courseNum]).contains(employee.name)) {
                        temp = remove(table.thursday[courseNum]);
                        table.thursday[courseNum] = temp.course;
                        table.thursday[courseNum][table.thursday[courseNum].length - 1] = employee.name;
                        is = true;
                    }
                    break;
                case 5:
                    if (!Arrays.asList(table.friday[courseNum]).contains(employee.name)) {
                        temp = remove(table.friday[courseNum]);
                        table.friday[courseNum] = temp.course;
                        table.friday[courseNum][table.friday[courseNum].length - 1] = employee.name;
                        is = true;
                    }
                    break;
                default:
                    break;
            }
        }
        assert temp != null;
        return temp.employee_name;
    }
    private static HasUseful remove(String[] arr) {
        HasUseful temp = new HasUseful(arr.length);
        boolean is = false;
        for(int i = 0;i < arr.length ;i++) {
            if(i == 1) {
                temp.employee_name = arr[i];
                is = true;
            }
            else {
                if (is) {
                    temp.course[i - 1] = arr[i];
                } else {
                    temp.course[i] = arr[i];
                }
            }
        }
        return temp;
    }
    private static int whiteInsert(DaytimeTable table, Employee employee, int maxJobsOfLb, int flag) {
        String[] lb = employee.lbnum.split("-");
        ArrayList<String> exist = new ArrayList<>();
        while(true){
            if(flag >= 3 || exist.size() >= lb.length){
                break;
            }
            Random rand = new Random();
            String lbtime = lb[rand.nextInt(lb.length)];
            if (lbtime.equals("")) {
                try {
                    String msg = employee.name + " 不存在轮班时间，请查看无课表是否有误!";
                    throw new Exception(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
            int week = Integer.parseInt(lbtime.substring(1,2));
            int courseNum = Integer.parseInt(lbtime.substring(3, 4))-1;
            if (exist.contains(lbtime)) {
                continue;
            }
            exist.add(lbtime);
            switch(week){
                case 1:
                    if(Arrays.asList(table.monday[courseNum]).contains(employee.name) || table.monday[courseNum].length > maxJobsOfLb){
                        continue;
                    }else{
                        // 将数组的长度加1,用来存储新添加的成员
                        table.monday[courseNum] = Arrays.copyOf(table.monday[courseNum], table.monday[courseNum].length+1);
                        // 前一行代码对数组的长度加了1,所以可以存储在该数组的最后
                        table.monday[courseNum][table.monday[courseNum].length-1] = employee.name;
                        ++flag;
                    }
                    break;
                case 2:
                    // System.out.println(Arrays.asList(table.baiTwo[courseNum]).contains(employee.name));
                    if(Arrays.asList(table.tuesday[courseNum]).contains(employee.name) || table.tuesday[courseNum].length > maxJobsOfLb){
                        continue;
                    }else{
                        table.tuesday[courseNum] = Arrays.copyOf(table.tuesday[courseNum], table.tuesday[courseNum].length+1);
                        table.tuesday[courseNum][table.tuesday[courseNum].length-1] = employee.name;
                        ++flag;
                    }
                    break;
                case 3:
                    if(Arrays.asList(table.wednesday[courseNum]).contains(employee.name) || table.wednesday[courseNum].length > maxJobsOfLb){
                        continue;
                    }else{
                        table.wednesday[courseNum] = Arrays.copyOf(table.wednesday[courseNum], table.wednesday[courseNum].length+1);
                        table.wednesday[courseNum][table.wednesday[courseNum].length-1] = employee.name;
                        ++flag;
                    }
                    break;
                case 4:
                    if(Arrays.asList(table.thursday[courseNum]).contains(employee.name) || table.thursday[courseNum].length > maxJobsOfLb){
                        continue;
                    }else{
                        table.thursday[courseNum] = Arrays.copyOf(table.thursday[courseNum], table.thursday[courseNum].length+1);
                        table.thursday[courseNum][table.thursday[courseNum].length-1] = employee.name;
                        ++flag;
                    }
                    break;
                case 5:
                    if(Arrays.asList(table.friday[courseNum]).contains(employee.name) || table.friday[courseNum].length > maxJobsOfLb){
                        continue;
                    }else{
                        table.friday[courseNum] = Arrays.copyOf(table.friday[courseNum], table.friday[courseNum].length+1);
                        table.friday[courseNum][table.friday[courseNum].length-1] = employee.name;
                        ++flag;
                    }
                    break;
                default:break;
            }
        }
        return 3 - flag;
    }
    private static void whiteCallback(int flag, DaytimeTable table, Employee employee, ArrayList<Employee> employees, int maxJobsOfLb) {
        switch (flag) {
            case 3:
                String employee_name = whiteReplace(table, employee);
                for(Employee _employee:employees){
                    if (_employee.name.equals(employee_name)) {
                        int i = whiteInsert(table, _employee, maxJobsOfLb, 2);
                        if (i == 1) {
                            if (employee.lbnum.split("").length < 3) {
                                try {
                                    String msg = employee.name + " 不存在轮班时间，请查看无课表是否有误!";
                                    throw new Exception(msg);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                return;
                            }
                            whiteCallback(i, table, _employee, employees, maxJobsOfLb);
                        }
                        break;
                    }
                }
            case 2:
                employee_name = whiteReplace(table, employee);
                for(Employee _employee:employees){
                    if (_employee.name.equals(employee_name)) {
                        int i = whiteInsert(table, _employee, maxJobsOfLb, 2);
                        if (i == 1) {
                            if (employee.lbnum.split("").length < 3) {
                                try {
                                    String msg = employee.name + " 不存在轮班时间，请查看无课表是否有误!";
                                    throw new Exception(msg);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                return;
                            }
                            whiteCallback(i, table, _employee, employees, maxJobsOfLb);
                        }
                        break;
                    }
                }
            case 1:
                employee_name = whiteReplace(table, employee);
                for(Employee _employee:employees){
                    if (_employee.name.equals(employee_name)) {
                        int i = whiteInsert(table, _employee, maxJobsOfLb, 2);
                        if (i == 1) {
                            if (employee.lbnum.split("").length < 3) {
                                try {
                                    String msg = employee.name + " 不存在轮班时间，请查看无课表是否有误!";
                                    throw new Exception(msg);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                return;
                            }
                            whiteCallback(i, table, _employee, employees, maxJobsOfLb);
                        }
                        break;
                    }
                }
            default:break;
        }
    }
    private static int blackInsert(NightTable table, Employee employee, int maxJobsOfzb, int maxJobsOfjd, int maxJobsOfwh, int flag) {
        String[] jd = employee.jdnum.split("");
        String[] wh = employee.whnum.split("");
        boolean hasZb = false, hasJd = false;
        ArrayList<Integer> selected_zj = new ArrayList<>();
        Random rand;
        ArrayList<Integer> selected = new ArrayList<>();
        while (true) {
            rand = new Random();
            int index = rand.nextInt(jd.length);
            int zbtime = Integer.parseInt(jd[index]) - 1;
            if (selected.size() >= jd.length || flag >= 3) {
                break;
            }
            if (selected_zj.contains(zbtime)) {
                continue;
            }
            selected.add(zbtime);
            if (!hasZb && table.zb[zbtime].length - 1 < maxJobsOfzb) {
                table.zb[zbtime] = Arrays.copyOf(table.zb[zbtime], table.zb[zbtime].length + 1);
                table.zb[zbtime][table.zb[zbtime].length - 1] = employee.name;
                selected_zj.add(zbtime);
                ++flag;
                hasZb = true;
                continue;
            }
            if (rand.nextInt(10) > 3) {
                if (!hasJd && table.jd[zbtime].length - 1 < maxJobsOfjd) {
                    table.jd[zbtime] = Arrays.copyOf(table.jd[zbtime], table.jd[zbtime].length + 1);
                    table.jd[zbtime][table.jd[zbtime].length - 1] = employee.name;
                    selected_zj.add(zbtime);
                    ++flag;
                    hasJd = true;
                }
            }
        }
        selected.clear();
        ArrayList<Integer> selected_wh = new ArrayList<>();
        while (true) {
            rand = new Random();
            int index = rand.nextInt(wh.length);
            int whtime = Integer.parseInt(wh[index]) - 1;
            if (selected.size() >= wh.length || flag >= 3) {
                break;
            }
            if (selected_wh.contains(whtime) || selected_zj.contains(whtime)) {
                continue;
            }
            selected.add(whtime);
            if (table.wh[whtime].length < maxJobsOfwh) {
                table.wh[whtime] = Arrays.copyOf(table.wh[whtime], table.wh[whtime].length + 1);
                table.wh[whtime][table.wh[whtime].length - 1] = employee.name;
                selected_wh.add(whtime);
                ++flag;
            }
        }
        return 3 - flag;
    }
    private static void blackCallback(int flag, NightTable table, Employee employee, ArrayList<Employee> employees, int maxJobsOfzb, int maxJobsOfjd, int maxJobsOfwh) {
        String employee_name = "";
        switch (flag) {
            case 3:
                employee_name = blackReplace(table, employee);
                for(Employee _employee:employees){
                    if (_employee.name.equals(employee_name)) {
                        int i = blackInsert(table, employee, maxJobsOfzb, maxJobsOfjd, maxJobsOfwh, 2);
                        if (i == 1) {
                            if (employee.whnum.split("").length < 3) {
                                try {
                                    String msg = employee.name + " 不存在值班时间，请查看无课表是否有误!";
                                    throw new Exception(msg);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                return;
                            }
                            blackCallback(i, table, _employee, employees, maxJobsOfzb, maxJobsOfjd, maxJobsOfwh);
                        }
                        break;
                    }
                }
            case 2:
                employee_name = blackReplace(table, employee);
                for(Employee _employee:employees){
                    if (_employee.name.equals(employee_name)) {
                        int i = blackInsert(table, employee, maxJobsOfzb, maxJobsOfjd, maxJobsOfwh, 2);
                        if (i == 1) {
                            if (employee.whnum.split("").length < 3) {
                                try {
                                    String msg = employee.name + " 不存在值班时间，请查看无课表是否有误!";
                                    throw new Exception(msg);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                return;
                            }
                            blackCallback(i, table, _employee, employees, maxJobsOfzb, maxJobsOfjd, maxJobsOfwh);
                        }
                        break;
                    }
                }
            case 1:
                employee_name = blackReplace(table, employee);
                for(Employee _employee:employees){
                    if (_employee.name.equals(employee_name)) {
                        int i = blackInsert(table, employee, maxJobsOfzb, maxJobsOfjd, maxJobsOfwh, 2);
                        if (i == 1) {
                            if (employee.whnum.split("").length < 3) {
                                try {
                                    String msg = employee.name + " 不存在值班时间，请查看无课表是否有误!";
                                    throw new Exception(msg);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                return;
                            }
                            blackCallback(i, table, _employee, employees, maxJobsOfzb, maxJobsOfjd, maxJobsOfwh);
                        }
                        break;
                    }
                }
            default:break;
        }
    }
    private static String blackReplace(NightTable table, Employee employee) {
        String[] jd = employee.jdnum.split("");
        String[] wh = employee.whnum.split("");
        Random rand;
        boolean is = false;
        ArrayList<Integer> selected = new ArrayList<>();
        HasUseful temp = null;
        while (true) {
            rand = new Random();
            int whtime = Integer.parseInt(wh[rand.nextInt(wh.length)]) - 1;
            if (selected.size() >= wh.length) {
                break;
            }
            selected.add(whtime);
            if (Arrays.asList(table.zb[whtime]).contains(whtime) || Arrays.asList(table.jd[whtime]).contains(whtime)
                    || Arrays.asList(table.wh[whtime]).contains(whtime)) {
                continue;
            } else {
                temp = remove(table.wh[whtime]);
                table.wh[whtime] = temp.course;
                table.wh[whtime][table.wh[whtime].length - 1]  = employee.name;
                is = true;
                break;
            }
        }
        selected.clear();
        while (true) {
            int zbtime = Integer.parseInt(jd[rand.nextInt(jd.length)]) - 1;
            if (is || selected.size() >= jd.length) {
                break;
            }
            selected.add(zbtime);
            if (Arrays.binarySearch(table.wh[zbtime], zbtime) > 0) {
                continue;
            }
            if (Arrays.binarySearch(table.zb[zbtime], zbtime) > 0) {
                temp = remove(table.zb[zbtime]);
                table.zb[zbtime] = temp.course;
                table.zb[zbtime][table.zb[zbtime].length - 1] = employee.name;
                break;
            }
            if (rand.nextInt(10) > 5) {
                if (Arrays.binarySearch(table.jd[zbtime], zbtime) > 0) {
                    temp = remove(table.jd[zbtime]);
                    table.jd[zbtime] = temp.course;
                    table.jd[zbtime][table.jd[zbtime].length - 1] = employee.name;
                    break;
                }
            }
        }
        return temp.employee_name;
    }
}
