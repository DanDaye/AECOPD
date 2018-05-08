package com.example.copd.Web;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.ByteArrayOutputStream;

import static okio.ByteString.read;

/**
 * Created by asus on 2018/4/2.
 */

public class WebService {
    private static String IP="123.207.20.100:8080";
    private static String pathip = "http://"+IP+"/aecopdDB/";
    //通过get方式获取HTTP服务器数据
    public static String executeHttpGet(String username,String password){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/login";
            path = path+"?username="+username+"&password="+password;
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * GetLatest
     * @param path
     * @return
     */
    public static String executeHttpGetLatest(String path){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
//            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Register
     * @param username
     * @param machine_id
     * @param password
     * @return
     */

    public static String executeHttpRegister(String username,String machine_id,String password){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/RegisterServlet";
            path = path+"?username="+username+"&password="+password+"&machine_id="+machine_id;
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }else if(conn.getResponseCode() == 500){
                return "false";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Change Password
     * @param username
     * @param password
     * @return
     */
    public static String executeHttpChangePwd(String username,String password){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/ChangePwdServlet";
            path = path+"?username="+username+"&password="+password;
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }else if(conn.getResponseCode() == 500){
                return "false";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Check password
     * @param username
     * @param password
     * @return
     */

    public static String executeHttpCheckPwd(String username,String password){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/CheckPwdServlet";
            path = path+"?username="+username+"&password="+password;
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }else if(conn.getResponseCode() == 500){
                return "false";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Check Machine
     * @param machine
     * @return
     */

    public static String executeHttpCheckMachine(String machine){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/CheckMachineServlet";
            path = path+"?machine_id="+machine;
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }else if(conn.getResponseCode() == 500){
                return "false";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Change Machine
     * @param username
     * @param machine_id
     * @return
     */


    public static String executeHttpChangeMachine(String username,String machine_id){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/ChangeMachineServlet";
            path = path+"?username="+username+"&machine_id="+machine_id;
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }else if(conn.getResponseCode() == 500){
                return "false";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Change Birth
     * @param birthdate
     * @return
     */

    public static String executeHttpChangeBirth(String machine_id,String birthdate){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/ChangeBirth";
            path = path+"?machine_id="+machine_id+"&birthdate="+birthdate;
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }else if(conn.getResponseCode() == 500){
                return "false";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Change Disease
     * @param disease_history
     * @return
     */


    public static String executeHttpChangeDisease(String machine_id,String disease_history){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/ChangeDiseaseServlet";
            path = path+"?machine_id="+machine_id+"&disease_history="+disease_history;
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }else if(conn.getResponseCode() == 500){
                return "false";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    /**
     * executeHttpChangeCough_Level
     * @param machine_id
     * @param cough_level
     * @return
     */

    public static String executeHttpChangeCough_Level(String machine_id,String cough_level){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/ChangeCoughLevelServlet";
            path = path+"?machine_id="+machine_id+"&cough_level="+cough_level;
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }else if(conn.getResponseCode() == 500){
                return "false";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * GetNormal
     * @param machine_id
     * @return
     */



    public static String executeHttpGetNormal(String machine_id){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/GetNormalServlet";
            path = path+"?machine_id="+machine_id;
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }else if(conn.getResponseCode() == 500){
                return "false";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * changeNormal
     * @param machine_id
     * @param type
     * @param content
     * @return
     */

    public static String executeHttpChangeNormal(String machine_id,String type,String content){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/ChangeNormalServlet";
            path = path+"?machine_id="+machine_id+"&type="+type+"&content="+content;
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }else if(conn.getResponseCode() == 500){
                return "false";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * AddNormal
     * @param machine_id
     * @param fev1
     * @param breath
     * @param heart
     * @param boold
     * @param bmi
     * @return
     */

    public static String executeHttpAddNormal(String machine_id,String fev1,String breath,String heart,String boold,String bmi,String temperature,String relivate,String birthdate,String desease_history){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://"+IP+"/aecopdDB/AddNormalServlet";
            path = path+"?machine_id="+machine_id+"&fev1="+fev1+"&heart="+heart+"&breath="+breath+"&bmi="+bmi+"&boold="+boold+"&temperature="+temperature+"&relivate="+relivate+"&birthdate="+birthdate+"&desease_history="+desease_history;
            System.out.println(path);
            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            System.out.print(conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }else if(conn.getResponseCode() == 500){
                return "false";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn !=null){
                conn.disconnect();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String parseInfo(InputStream inStream) throws IOException {
        byte [] data = read(inStream);
        return new String(data,"UTF-8");

    }

    static byte[] read(InputStream inStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte [] buffer = new byte[1024];
        int len =0;
        while ((len = inStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }
}
