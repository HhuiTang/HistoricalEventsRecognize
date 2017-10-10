import java.io.*;
import java.util.ArrayList;

/**
 * Created by tang on 17-9-4.
 */
public class kmp {
    //Éú³ÉÎÄŒþÂ·Ÿ¶
    private static String path = "/home/tang/Downloads/";

    //ÎÄŒþÂ·Ÿ¶+Ãû³Æ
    private static String filenameTemp;
    /**
     * ŽŽœšÎÄŒþ
     * @param fileName  ÎÄŒþÃû³Æ
     * @return  ÊÇ·ñŽŽœš³É¹Š£¬³É¹ŠÔò·µ»Øtrue
     */
    public static String createFile(String fileName){
        Boolean bool = false;
        filenameTemp = path+fileName+".txt";
        File file = new File(filenameTemp);
        try {
            //Èç¹ûÎÄŒþ²»ŽæÔÚ£¬ÔòŽŽœšÐÂµÄÎÄŒþ
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                //System.out.println("success create file,the file is "+filenameTemp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filenameTemp;
    }

    /**
     * ÏòÎÄŒþÖÐÐŽÈëÄÚÈÝ
     * @param filepath ÎÄŒþÂ·Ÿ¶ÓëÃû³Æ
     * @param newstr  ÐŽÈëµÄÄÚÈÝ
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";
        String temp  = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //ÎÄŒþÔ­ÓÐÄÚÈÝ
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }

    /**
     * ÉŸ³ýÎÄŒþ
     * @param fileName ÎÄŒþÃû³Æ
     * @return
     */
    public static boolean delFile(String fileName){
        Boolean bool = false;
        filenameTemp = path+fileName+".txt";
        File file  = new File(filenameTemp);
        try {
            if(file.exists()){
                file.delete();
                bool = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bool;
    }
    /**
     * 判断是否匹配
     * @param target 目标文本串
     * @param mode 模式串
     * @return 匹配结果
     */
    public static boolean matchString(String target, String mode) {
        if (target.length()>mode.length()) {
            //为了和算法保持一致，使index从1开始，增加一前缀
            String newTarget = "x" + target;
            String newMode = 'x' + mode;
            //System.out.println(newTarget);
            //System.out.println(newMode);

            int[] K = calculateK(mode);

            int i = 1;
            int j = 1;
            while (i <= target.length() && j <= mode.length()) {
                if (j == 0 || newTarget.charAt(i) == newMode.charAt(j)) {
                    i++;
                    j++;
                } else {
                    j = K[j];
                }
            }

            if (j > mode.length()) {
                return true;
            }
        }
        return false;
    }

    /*
     * 计算K值
     */
    private static int[] calculateK(String mode) {
        //为了和算法保持一致，使index从1开始，增加一前缀
        String newMode = "x" + mode;
        int[] K = new int[newMode.length()];
        int i = 1;
        K[1] = 0;
        int j = 0;

        while(i < mode.length()) {
            if (j == 0 || newMode.charAt(i) == newMode.charAt(j)){
                i++;
                j++;
                K[i] = j;
            } else {
                j = K[j];
            }
        }

        return K;
    }
    private void labelSentence(String strList, String strText, int record,String f){

        //标注
        char[] charList=strList.toCharArray();
        char[] charText=strText.toCharArray();
        System.out.println(strList.length());
        System.out.println(strText.length());
        String[] annotation=new String[strText.length()+1];
        int k =0;
        int count =0;
        for(int p=0;p<strText.length();p++){
            boolean conti=true;
            annotation[p]=String.valueOf(record)+"\t"+charText[p]+"\t"+"A";
            if(k<strList.length()&&charList[k]==charText[p]&&conti){
                count++;
                if (count==strList.length()&&conti){
                    annotation[p]=String.valueOf(record)+"\t"+charText[p]+"\t"+"E";
                    for (int t=p-1;t>p-strList.length()+1;t--)
                    {
                        annotation[t]=String.valueOf(record)+"\t"+charText[t]+"\t"+"M";
                    }
                    annotation[p-strList.length()+1]=String.valueOf(record)+"\t"+charText[p-strList.length()+1]+"\t"+"B";
                    k=0;
                    count=0;
                    conti=false;
                }
                k++;
            }
        }
        for(int p=0;p<strText.length();p++){
            try {
                writeFileContent(f,annotation[p]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        annotation[strText.length()]=String.valueOf(0)+"\t"+" "+"\t"+" ";
        try {
            writeFileContent(f,annotation[strText.length()]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileInputStream fisText = null;
        FileInputStream fisList = null;
        InputStreamReader isrText = null;
        InputStreamReader isrList = null;
        BufferedReader brText = null;
        BufferedReader brList = null;

        boolean del = delFile("HisRec_WikiWord");
        if(del){
            System.out.println("Delete the file successfully!");
        }
        else {
            System.out.println("Failed to delete the file!");
        }
        /*
        boolean del1 = delFile("HisAban");
        if(del1){
            System.out.println("Delete the file successfully!");
        }
        else {
            System.out.println("Failed to delete the file!");
        }
*/
        String  f=createFile("HisRec_WikiWord");//存放匹配成功的记录
        //String  f1=createFile("HisAban");//存放匹配失败的断句
        try {
            String str = "";
            String strTextTemp="";
            //String str1 = "";
            fisText = new FileInputStream("/home/tang/Downloads/segment_WikiWord.txt");// FileInputStream
            fisList = new FileInputStream("/home/tang/Downloads/HisList2.txt");// FileInputStream
            //fisText = new FileInputStream("C:\Users\wzx\Desktop\HisTextTest.txt");// FileInputStream
            //fisList = new FileInputStream("C:\Users\wzx\DesktopHisListTest.txt");// FileInputStream

            isrText = new InputStreamReader(fisText);// InputStreamReader
            isrList = new InputStreamReader(fisList);
            brText = new BufferedReader(isrText);//
            brList = new BufferedReader(isrList);
            int i =0;
            ArrayList<String> myList=new ArrayList<String>();

            while ((str = brList.readLine()) != null) {
                String str2=str.trim();//去匹配模式末尾的空格
                //writeFileContent(f1,str2);
                if (str.length()>0)
                {
                    myList.add(i,str2);//得到历史时间列表
                    i++;
                }
            }

            int num=i;//历史事件个数
            int record=0;//挑选出的断句和历史事件组合的个数
//TODO:回收匹配不成功的断句
            for (i=0;(strTextTemp=brText.readLine())!=null;i++){//读历史断句文本
                String strText=strTextTemp.replaceAll("　| ", "");
                if (strText.length()>0){
                    //System.out.println(strText);
                    for (int j=0;j<num;j++){
                        String strList=myList.get(j);//依次获取每个事件
                        //System.out.println(strList);
                        boolean flag=matchString(strText,strList);
                        //System.out.println(flag);
                        if (flag){//匹配成功，加入记录
                            record++;//序号增一
                            String content = String.valueOf(record) + "\t" + strList + "\t" + strText;
                            System.out.println(content);
                            writeFileContent(f, content);
                            flag=false;
                        }
                    }
                }
            }

        }catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
        } catch (IOException e) {
            System.out.println("IOException");
        } finally {
            try {
                brText.close();
                brList.close();
                isrText.close();
                isrList.close();
                fisText.close();
                fisList.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


