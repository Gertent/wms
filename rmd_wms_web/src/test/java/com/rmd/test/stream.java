package com.rmd.test;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * Created by wyf on 2017/8/2.
 */
public class stream {

    //流释放模式
    public void disposePattern(){
        OutputStream out=null;
        try{
            out=new FileOutputStream("d:/test.txt");
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }finally {
            if(out!=null){
                try{
                    out.close();
                }catch (IOException ex1){

                }
            }
        }
    }
    //带资源的try,会对try块参数表中声明的所有AutoCloseable对象自动调用close()
    public void tryWithResource(){
        try(OutputStream out=new FileOutputStream("d:/test.txt")){
            //处理输出流
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static String getM(InputStream in) throws IOException {
        Reader r=new InputStreamReader(in,"Maccyrillic");
        r=new BufferedReader(r,1024);
        StringBuilder sb=new StringBuilder();
        int c;
        while ((c=r.read())!=-1){
            sb.append((char)c);
        }

        return sb.toString();
    }

    public void testFuture(){

    }
   private static class Task implements Callable<Integer>{

        @Override
        public Integer call() throws Exception {
            System.out.println("子线程正在计算");
            Thread.sleep(3000);
            int sum=0;
            for(int i=0;i<300;i++){
                sum+=i;
            }
            return sum;
        }
    }
    public static void main1(String[] args){
        ExecutorService executor= Executors.newCachedThreadPool();
        Task tk=new Task();
        Future<Integer> result=executor.submit(tk);
        executor.shutdown();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("主线程在执行任务");

        try {
            System.out.println("task运行结果"+result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");

    }
    public static void main2(String[] args){
        try {
            InetAddress address=InetAddress.getByName("www.baidu.com");
            System.out.println(address);

            InetAddress me=InetAddress.getLocalHost();
            System.out.println(me.getHostName());
            System.out.println(me.getAddress());
            System.out.println(me.getAddress().length);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main4(String[] args){
        try{
            URL url=new URL("http://www.baidu.com");
            URLConnection connection=url.openConnection();
            try(InputStream in=connection.getInputStream()) {
                int c;
                while ((c = in.read()) != -1) {
                    System.out.print((char)c);
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args){
        InputStream in=null;
        try{
            URL url=new URL("http://www.baidu.com");
            URLConnection connection=url.openConnection();

            connection.setDoOutput(true);
            OutputStreamWriter out=new OutputStreamWriter(connection.getOutputStream(),"utf-8");
            out.write("name=csdn");
            out.write("\r\n");
            out.flush();


            in=connection.getInputStream();//url.openStream();
//            in=url.openStream();
//            in=new BufferedInputStream(in);
            Reader r=new InputStreamReader(in);
            int c;
            while ((c=r.read())!=-1){
                System.out.print((char)c);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }finally {
            if(in!=null){
                try {
                    in.close();
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    public static void  saveBinaryData(URL u){
        URLConnection uc= null;
        try {
            uc = u.openConnection();
            String contentType=uc.getContentType();
            int contentLength=uc.getContentLength();
            if(contentType.startsWith("text/")||contentLength==-1){
                throw new IOException("ioEx");
            }
            try(InputStream raw=uc.getInputStream()){
                InputStream in=new BufferedInputStream(raw);
                byte[] data=new byte[contentLength];
                int offset=0;
                while(offset<contentLength){
                    int bytesRead=in.read(data,offset,data.length-offset);
                    if(bytesRead==-1) break;
                    offset+=bytesRead;
                }
                if(offset!=contentLength){

                }
                String fileName=u.getFile();
                fileName=fileName.substring(fileName.lastIndexOf("/")+1);
                FileOutputStream out=new FileOutputStream(fileName);
                out.write(data);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static void sockTest(){
        Socket socket=null;
        try{
            socket=new Socket("dict.org",2628);
            socket.setSoTimeout(15000);
            OutputStream out=socket.getOutputStream();
            OutputStreamWriter writer=new OutputStreamWriter(out);
            try (BufferedWriter writer1= new BufferedWriter(writer)) {

            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }finally {
            if(socket!=null){

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
