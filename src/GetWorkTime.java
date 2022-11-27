import java.util.ArrayList;

//用来获取可轮班和值班时间的方法
//将数据转换成可操作形式
public class GetWorkTime {
    //用于判断某一个星期能否来值班
    static ArrayList<EmptyLesson> IsWork(ArrayList<String> table, int week){
        ArrayList<EmptyLesson> haveWork = new ArrayList<EmptyLesson>();
        //逐行遍历传入的ArrayList类型的无课表
        for(int i=2;i<table.size();i++){
//            System.out.println(table.get(i));
            //第一二节课的无课时间在同一行，每天的一二节课用两个制表符隔开，所以将它们分割开，分割到temp数组内
            String[] temp = table.get(i).split("\t\t");
            //temp数组内存储的时一个星期某一大节课的无可数据
            for(int j=0;j<temp.length;j++){
                // System.out.print(temp[j]+'\t');
                EmptyLesson zbiao = new EmptyLesson();
                if(temp[j].equals("0")){
                    continue;
                }
                else if(temp[j].indexOf("，") < 0 && temp[j].indexOf(",") < 0 && temp[j].indexOf("-") < 0){
                    //else if(temp[j].indexOf(",") == -1 && temp[j].indexOf("-") == -1){
                    if( Integer.parseInt(temp[j]) == week ){
                        zbiao.x = i-1;                           //课程节数，一二节课为1，三四节课为2，以此类推
                        zbiao.y = j+1;                           //星期数，星期一为1，星期二为2，以此类推
                        haveWork.add(zbiao);
//                        System.out.println(i+","+j);
                    }
                }
                else if(temp[j].indexOf("，") > 0 || temp[j].indexOf(",") > 0 || temp[j].indexOf("-") > 0){
                    //else if(temp[j].indexOf(",") > 0 || temp[j].indexOf("-") > 0){
                    String[] oneWork;
                    if(temp[j].indexOf("，") > 0){
                        oneWork = temp[j].split("，");
                    }else{
                        oneWork = temp[j].split(",");
                    }
                    for(int ii=0;ii<oneWork.length;ii++){
                        if(oneWork[ii].indexOf("-") > 0){
                            String start = oneWork[ii].substring(0,oneWork[ii].indexOf("-"));
                            String end   = oneWork[ii].substring(oneWork[ii].indexOf("-")+1);
                            if( Integer.parseInt(start) <= week  &&  Integer.parseInt(end) >= week ){
                                zbiao.x = i-1;
                                zbiao.y = j+1;
                                haveWork.add(zbiao);
                            }
                        }else if(Integer.parseInt(oneWork[ii]) == week){
                            zbiao.x = i-1;
                            zbiao.y = j+1;
                            haveWork.add(zbiao);
                        }
                    }
                }
            }
            // System.out.println();
        }
        return haveWork;
    }
    static ArrayList<String> WorkTime(ArrayList<String> list, String startWeek, String endWeek){
        int shi = Integer.parseInt(startWeek);
        int mo = Integer.parseInt(endWeek);
        String weekend = null;
        String weekendArr[] = null;
        String workArr[] = null;
        ArrayList<String> worklist = null;
        while( shi <= mo ){
            ArrayList<EmptyLesson> read = IsWork(list,shi);
            weekend = new String();
            for(int i=0;i<read.size();i++){
                EmptyLesson per = read.get(i);
                // System.out.println("("+per.y+","+per.x+")");
                weekend += "("+per.y+","+per.x+")-";                       //y值为星期数，x值为课程节数
            }
            // System.out.println(weekend);
            //用下面的循环判断出startWeek到endWeek之间重复的值班时间，也就是将要值班的这几周内都能值班的时间筛选出来
            weekendArr = weekend.split("-");
            if(workArr == null){
                workArr = weekendArr;
            }else{
                worklist = new ArrayList<String>();
                for(int i=0;i<workArr.length;i++){
                    if(workArr[i] == null){
                        break;
                    }
                    for(int j=0;j<weekendArr.length;j++){
                        if(workArr[i].equals(weekendArr[j])){
                            worklist.add(workArr[i]);
                        }
                    }
                }
                worklist.toArray(workArr);
                // for(int i=0;i<workArr.length;i++){
                //     System.out.print(workArr[i]);
                // }
                // System.out.println();
            }
            shi++;
        }
//         for(int i=0;i<worklist.size();i++){
//             System.out.println(worklist.get(i));
//         }
        return worklist;
    }
    static Employee DaytimeAndNight(ArrayList<String> list,Employee employee, String startWeek, String endWeek){
        ArrayList<String> workTime = WorkTime(list, startWeek, endWeek);
        // for(int i=0;i<workTime.size();i++){
        //     System.out.println(workTime.indexOf(workTime.get(i)));
        // }
        //(1,5)表示星期一的晚上两节课
        //星期一的第五节，也就是晚上，没课就可以机动和维护
        if(workTime.indexOf("(1,5)") >= 0){
            employee.jdnum += "1";
            employee.whnum += "1";
        }
        if(workTime.indexOf("(2,5)") >= 0){
            employee.jdnum += "2";
            employee.whnum += "2";
        }
        if(workTime.indexOf("(3,5)") >= 0){
            employee.jdnum += "3";
            employee.whnum += "3";
        }
        if(workTime.indexOf("(4,5)") >= 0){
            employee.jdnum += "4";
            employee.whnum += "4";
        }
        if(workTime.indexOf("(5,5)") >= 0){
            employee.jdnum += "5";
            employee.whnum += "5";
        }
        //双休日的机动是一下午和一晚上的班，所以要下午五六七八节课有时间值班，条件如下
        if(workTime.indexOf("(6,3)") >= 0 && workTime.indexOf("(6,4)") >= 0 && workTime.indexOf("(6,5)") >= 0){
            employee.jdnum += "6";
        }
        if(workTime.indexOf("(7,3)") >= 0 && workTime.indexOf("(7,4)") >= 0 && workTime.indexOf("(7,5)") >= 0){
            employee.jdnum += "7";
        }
        if(workTime.indexOf("(6,5)") >= 0){
            employee.whnum += "6";
        }
        if(workTime.indexOf("(7,5)") >= 0){
            employee.whnum += "7";
        }
        for(int i=0;i<workTime.size();i++){
            if(workTime.get(i).equals("(1,1)") || workTime.get(i).equals("(1,2)") || workTime.get(i).equals("(1,3)") || workTime.get(i).equals("(1,4)")
                    || workTime.get(i).equals("(2,1)") || workTime.get(i).equals("(2,2)") || workTime.get(i).equals("(2,3)") || workTime.get(i).equals("(2,4)")
                    || workTime.get(i).equals("(3,1)") || workTime.get(i).equals("(3,2)") || workTime.get(i).equals("(3,3)") || workTime.get(i).equals("(3,4)")
                    || workTime.get(i).equals("(4,1)") || workTime.get(i).equals("(4,2)") || workTime.get(i).equals("(4,3)") || workTime.get(i).equals("(4,4)")
                    || workTime.get(i).equals("(5,1)") || workTime.get(i).equals("(5,2)") || workTime.get(i).equals("(5,3)") || workTime.get(i).equals("(5,4)")){
                employee.lbnum += workTime.get(i)+'-';
            }
        }
        return employee;
    }
}
