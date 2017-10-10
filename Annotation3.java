import java.io.*;
import java.util.ArrayList;

public class Annotation3 {
    //生成文件路径
    private static String path = "/home/tang/Downloads/";
    public static ArrayList<String> anno=new ArrayList<String>();
    //文件路径+名称
    private static String filenameTemp;
    /**
     * 创建文件
     * @param fileName  文件名称
     * @return  是否创建成功，成功则返回true
     */
    public static String createFile(String fileName){
        Boolean bool = false;
        filenameTemp = path+fileName+".txt";//文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
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
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
        String temp  = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
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
            //不要忘记关闭
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
     * 删除文件
     * @param fileName 文件名称
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
    private boolean isChineseByScript(char c) {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        if (sc == Character.UnicodeScript.HAN) {
            return true;
        }
        return false;
    }

    public static ArrayList<String> annotationWord(String strList,String strText,int times){
        Annotation3 a =new Annotation3();
        char[] charText = strText.toCharArray();
        String punctuation="";
        if (times==0) {
            anno.clear();
            for (int i = 0; i < strText.length(); i++) {
                if (a.isChineseByScript(charText[i])) {
                    anno.add(i, String.valueOf(charText[i]) + '\t' + 'A');
                }else {
                    punctuation+=charText[i];
                    anno.add(i,"extra");
                    if (i<charText.length-1&&a.isChineseByScript(charText[i+1])) {
                        anno.add(i, punctuation + '\t' + 'P');
                        punctuation="";
                    }
                }
            }
        }
        int index = strText.indexOf(strList);
        if (index >= 0) {
            System.out.println("times:"+times+",index:"+index);
            anno.set(index, String.valueOf(charText[index]) + '\t' + 'B');
            //System.out.println("word:"+anno.get(index));
            for (int j = index+1; j < index + strList.length(); j++) {
                anno.set(j, String.valueOf(charText[j]) + '\t' + 'M');
                //System.out.println("word:" + anno.get(j));
            }
            anno.set(index+strList.length() - 1, String.valueOf(charText[index+strList.length() - 1]) + '\t' + 'E');

            //System.out.println("word:"+anno.get(index+strList.length()-1));
        }
        return anno;
    }


    public static void main(String[] args) {
        FileInputStream fisText = null;
        FileInputStream fisList = null;
        InputStreamReader isrText = null;
        InputStreamReader isrList = null;
        BufferedReader brText = null;
        BufferedReader brList = null;

        boolean del = delFile("Annotation");
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
        String  f=createFile("Annotation");//存放匹配成功的记录
        //String  f1=createFile("HisAban");//存放匹配失败的断句
        try {
            String str = "";
            String strTextTemp="";
            //String str1 = "";
            fisText = new FileInputStream("/home/tang/Downloads/His_Seg.txt");// FileInputStream
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
            int record=1;//挑选出的断句和历史事件组合的个数
//TODO:回收匹配不成功的断句
            for (i=0;(strTextTemp=brText.readLine())!=null;i++){//读历史断句文本
                String strText=strTextTemp.replaceAll("　| ", "");
                if (strText.length()>0){
                    System.out.println(i);
                    //System.out.println(strText);
                    for (int j=0;j<num;j++){
                        String strList=myList.get(j);//依次获取每个事件
                        ArrayList<String> anno=annotationWord(strList,strText,j);
                    }
                    for (int k=0;k<anno.size();k++)
                    {
                        if (anno.get(k)!="extra") {
                            String st = String.valueOf(record) + '\t' + anno.get(k);
                            writeFileContent(f, st);
                        }
                    }
                    writeFileContent(f,String.valueOf(0));
                    record++;

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
