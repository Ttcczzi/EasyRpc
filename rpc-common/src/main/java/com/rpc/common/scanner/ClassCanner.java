package com.rpc.common.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author xcx
 * @date
 */
public class ClassCanner {
    /**
     * 文件
     */
    private static final String PROTOCOL_FILE = "file";
    /**
     * jar包
     */
    private static final String PROTOCOL_JAR = "jar";
    /**
     * class文件的后缀
     */
    private static final String CLASS_FILE_SUFFIX = ".class";
    private static List<String> classNames;
    public static List<String> getClassNames(String packageName) throws IOException {
        if(classNames == null){
            classNames = new ArrayList<>();
            classNames = getClassNames0(packageName);
        }
        return classNames;
    }

    private static List<String> getClassNames0(String packageName) throws IOException {

        //将包名转换为文件名
        String dirName = packageName.replace('.', '/');
        //获得资源
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(dirName);

        while (dirs.hasMoreElements()){
            //资源路径
            URL url = dirs.nextElement();
            //是文件 还是 jar包
            String protocol = url.getProtocol();
            if(PROTOCOL_FILE.equals(protocol)){
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                //获得路径下所有的class文件
                findAndAddClassesByFile(packageName, filePath, classNames);
            }
        }

        return classNames;
    }

    public static void findAndAddClassesByFile(String packageName, String packagePath, List<String> classNames){
        File dirfile = new File(packagePath);
        //不是文件 或者不是文件夹 就退出
        if(!dirfile.exists() || !dirfile.isDirectory()){
            return;
        }
        //对文件过滤
        File[] files = dirfile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isDirectory() || file.getName().endsWith(CLASS_FILE_SUFFIX));
            }
        });
        for(File file :files){
            if(file.isDirectory()){
                findAndAddClassesByFile(packageName + "." + file.getName(), file.getPath(), classNames);
            }else{
                String className = file.getName().substring(0, file.getName().length() - CLASS_FILE_SUFFIX.length());
                classNames.add(packageName + "." + className);
            }
        }
    }
}
