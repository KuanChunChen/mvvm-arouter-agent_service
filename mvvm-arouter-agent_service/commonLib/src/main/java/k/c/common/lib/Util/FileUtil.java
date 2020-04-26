package k.c.common.lib.Util;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import k.c.common.lib.AppSingle;
import k.c.common.lib.constants.Constants;
import k.c.common.lib.logTool.LogTool;

public class FileUtil {

    /**
     * @param path path
     * @return is exists
     */
    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static File fileInit(String rootDirPath, String fileName){
        String mVideoPath;
        File dirFile = new File(rootDirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        mVideoPath = rootDirPath + fileName;
        File mFile;
        mFile = new File(mVideoPath);
        return mFile;
    }

    public static boolean appendFileContent(File mFile, byte[] data){
        RandomAccessFile raf = null;
        try {
            File fileParent = mFile.getParentFile();
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }

            if(!mFile.exists()){
                mFile.createNewFile();
            }

            raf = new RandomAccessFile(mFile, "rw");
            raf.seek(mFile.length());
            raf.write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * unZipFolder
     * @param archive zip file path
     * @param decompressDir decompress dir
     * @return The path of the execution file.
     */
    public static int unZipFolder(String archive, String decompressDir) {
        try {
            String fileName = archive.substring(archive.lastIndexOf(".") + 1);
            LogTool.d("file suffix == " + fileName);
            if (!"zip".equals(fileName)){
                return Constants.File.FILE_NOT_NEED_UNZIP;
            }
            File dirFile = new File(decompressDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

            BufferedInputStream bi;
            ZipFile zf = new ZipFile(archive);
            Enumeration e = zf.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze2 = (ZipEntry) e.nextElement();
                String entryName = ze2.getName();
                String path = decompressDir + "/" + entryName;
                if (ze2.isDirectory()) {
                    File decompressDirFile = new File(path);
                    if (!decompressDirFile.exists()) {
                        decompressDirFile.mkdirs();
                    }
                } else {
                    String fileDir = path.substring(0, path.lastIndexOf("/"));
                    File fileDirFile = new File(fileDir);
                    if (!fileDirFile.exists()) {
                        fileDirFile.mkdirs();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(decompressDir + "/" + entryName));
                    bi = new BufferedInputStream(zf.getInputStream(ze2));
                    byte[] readContent = new byte[1024];
                    int readCount = bi.read(readContent);
                    while (readCount != -1) {
                        bos.write(readContent, 0, readCount);
                        readCount = bi.read(readContent);
                    }
                    bos.close();
                }
            }
            zf.close();
            return Constants.File.FILE_UNZIP_SUCCESS;
        } catch (IOException e) {
            Log.d("","faile to unzip file");
            return Constants.File.FILE_UNZIP_FAIL;
        }
    }

    public static long getFileSizes(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        if(flist != null){
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + getFileSize(flist[i]);
                }
            }
        }
        return size;
    }

    /**
     * @param zipFilePathName save path of the zip
     * @param fileToZip origin file path
     */
    public static boolean createZIP(String zipFilePathName, String fileToZip) {
        return createZIP(zipFilePathName, fileToZip, -1);
    }

    /**
     * @param zipFilePathName save path of the zip
     * @param fileToZip origin file path
     * @param splitLength split length of the each file
     */
    public static boolean createZIP(String zipFilePathName, String fileToZip, long splitLength) {
        boolean isSuccess = false;
        try {
            // Initiate ZipFile object with the path/name of the zip file.
            net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(zipFilePathName);

            // Build the list of files to be added in the array list
            // Objects of type File have to be added to the ArrayList
//            ArrayList filesToAdd = new ArrayList();
            File file = new File(fileToZip);
//            filesToAdd.add(new File("c:\\ZipTest\\myvideo.avi"));
//            filesToAdd.add(new File("c:\\ZipTest\\mysong.mp3"));

            // Initiate Zip Parameters which define various properties such
            // as compression method, etc.
            ZipParameters parameters = new ZipParameters();

            // set compression method to store compression
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

            // Set the compression level. This value has to be in between 0 to 9
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            // Create a split file by setting splitArchive parameter to true
            // and specifying the splitLength. SplitLenth has to be greater than
            // 65536 bytes
            // Please note: If the zip file already exists, then this method throws an
            // exception
            if(file.isDirectory()){
                zipFile.createZipFileFromFolder(file, parameters, splitLength != -1, splitLength);
            }else{
                zipFile.createZipFile(file, parameters, splitLength != -1, splitLength);
            }
            isSuccess = true;
        } catch (ZipException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    private static long getFileSize(File file) {
        return file.length();
    }

    /**
     * 复制文件目录
     * @param srcDir 要复制的源目录
     * @param destDir 复制到的目标目录
     * @return
     */
    public static boolean copyDir(String srcDir, String destDir){
        File sourceDir = new File(srcDir);
        //判断文件目录是否存在
        if(!sourceDir.exists()){
            return false;
        }
        //判断是否是目录
        if (sourceDir.isDirectory()) {
            File[] fileList = sourceDir.listFiles();
            File targetDir = new File(destDir);
            //创建目标目录
            if(!targetDir.exists()){
                targetDir.mkdirs();
            }
            //遍历要复制该目录下的全部文件
            for(int i= 0;i<fileList.length;i++){
                if(fileList[i].isDirectory()){
                    //如果如果是子目录进行递归
                    copyDir(fileList[i].getPath()+ "/", destDir + fileList[i].getName() + "/");
                }else{
                    //如果是文件则进行文件拷贝
                    copyFile(fileList[i].getPath(), destDir +fileList[i].getName());
                }
            }
            return true;
        }else {
            copyFileToDir(srcDir,destDir);
            return true;
        }
    }

    /**
     * 复制文件（非目录）
     * @param srcFile 要复制的源文件
     * @param destFile 复制到的目标文件
     * @return
     */
    private static boolean copyFile(String srcFile, String destFile){
        try{
            InputStream streamFrom = new FileInputStream(srcFile);
            OutputStream streamTo = new FileOutputStream(destFile);
            byte buffer[]=new byte[1024];
            int len;
            while ((len= streamFrom.read(buffer)) > 0){
                streamTo.write(buffer, 0, len);
            }
            streamFrom.close();
            streamTo.close();
            return true;
        } catch(Exception ex){
            return false;
        }
    }

    /**
     * 把文件拷贝到某一目录下
     * @param srcFile
     * @param destDir
     * @return
     */
    public static boolean copyFileToDir(String srcFile, String destDir){
        File fileDir = new File(destDir);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String destFile = destDir +"/" + new File(srcFile).getName();
        try{
            InputStream streamFrom = new FileInputStream(srcFile);
            OutputStream streamTo = new FileOutputStream(destFile);
            byte buffer[]=new byte[1024];
            int len;
            while ((len= streamFrom.read(buffer)) > 0){
                streamTo.write(buffer, 0, len);
            }
            streamFrom.close();
            streamTo.close();
            return true;
        } catch(Exception ex){
            return false;
        }
    }

    /**
     * 移动文件目录到某一路径下
     * @param srcFile
     * @param destDir
     * @return
     */
    public static boolean moveFile(String srcFile, String destDir) {
        //复制后删除原目录
        if (copyDir(srcFile, destDir)) {
            deleteFile(new File(srcFile));
            return true;
        }
        return false;
    }

    /**
     * 删除文件（包括目录）
     * @param delFile
     */
    public static void deleteFile(File delFile) {
        //如果是目录递归删除
        if (delFile.isDirectory()) {
            File[] files = delFile.listFiles();
            for (File file : files) {
                deleteFile(file);
            }
        }else{
            delFile.delete();
        }
        //刪除子目录空文件夹
        delFile.delete();
    }

    public static String getDefaultFolder(Context mContext) {
        String retDefalutPath = mContext.getFilesDir().getPath();
        Log.d("getDefaultFolder", retDefalutPath);
        return retDefalutPath;
    }

    public static boolean isDirectoryExist(String strPath) {
        File mFile = new File(strPath);
        if (mFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean makeDefaultFile(String strFilePath) {
        File makeFile = new File(strFilePath);
        try {
            makeFile.createNewFile();
        } catch (IOException eIOException) {
            eIOException.printStackTrace();
        }

        if (makeFile.exists()) {
            Log.d("makeDefaultFile : ", "File exist!");
            return true;
        } else {
            Log.d("makeDefaultFile : ", "File not exist!");
            return false;
        }

    }

    public static boolean makeDefaultFile(String strMakeDir, String strFile) {
        File makeFile = new File(strMakeDir + strFile);
        try {
            makeFile.createNewFile();
        } catch (IOException eIOException) {
            eIOException.printStackTrace();
        }
        if (makeFile.exists()) {
            Log.d("makeDefaultFile : ", "File exist!");
            return true;
        } else {
            Log.d("makeDefaultFile : ", "File not exist!");
            return false;
        }
    }

    public static void writeFile(String strFilePath, String strWrite) {
        FileWriter fw = null;
        File mFile = new File(strFilePath);
        try {
            fw = new FileWriter(mFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(strWrite);
            bw.flush();
            bw.close();
        } catch (IOException eIOException) {
            eIOException.printStackTrace();
        }
    }

    public static void writeFileSync(String strFilePath, String strWrite) {
        FileWriter fw = null;
        FileOutputStream fos = null;
        FileDescriptor fd = null;

        try {
            fos = new FileOutputStream(strFilePath);
            fd = fos.getFD();
            fos.write(strWrite.getBytes());
            fos.flush();
            fd.sync();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void fileWrite(String path, String str) throws IOException {
        byte[] b = str.getBytes(Constants.FILE_WRITE_READER_CHARSET);
        FileOutputStream fos = null;
        FileChannel foc = null;
        try {
            File writeFile = new File(path);
            if (writeFile.exists()) {
                writeFile.delete();
            }
            File fileParent = writeFile.getParentFile();
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }
            writeFile.createNewFile();
            fos = new FileOutputStream(writeFile);
            foc = fos.getChannel();
            ByteBuffer writeBuffer = ByteBuffer.allocate(b.length);
            writeBuffer.put(b);
            writeBuffer.flip();
            foc.write(writeBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (foc != null) {
                foc.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }


    public static boolean deletlFile(String strPath) {
        File mFile = new File(strPath);
        boolean success = mFile.delete();
        if (success) {
            Log.d("deleteAllFile", "del all success");
            return true;
        } else {
            Log.d("deleteAllFile", "del all fail");
            return false;
        }
    }

    public static boolean isFileEmpty(String strPath) {

        File mFile = new File(strPath);
        boolean success = false;
        if (mFile.exists() && mFile.length() == 0) {
            success = true;
        }
        return success;
    }

    public static boolean mkdir(String strPath) {

        File mFile = new File(strPath);
        mFile.mkdir();
        boolean success = false;
        if (mFile.exists() && mFile.length() == 0) {
            success = true;
        }
        return success;
    }

    public static void zip(String strPath) throws IOException {
        String ZIP_OUTPUT_PATH = "/Output";
        String strOutputPath = AppSingle.getInstance().getDefalutPath() + ZIP_OUTPUT_PATH;
        File file = new File(strPath);
        if (!FileUtil.isDirectoryExist(strOutputPath)) {
            mkdir(strOutputPath);
        }
        File zipFile = new File(AppSingle.getInstance().getDefalutPath() + ZIP_OUTPUT_PATH + "/CTMS/Upload/log_" + TimeUtil.getCurrentSysTime() + ".zip");
        InputStream input ;
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        zipOut.setComment("hello");
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; ++i) {
                input = new FileInputStream(files[i]);
                zipOut.putNextEntry(new ZipEntry(file.getName()
                        + File.separator + files[i].getName()));
                int temp = 0;
                while ((temp = input.read()) != -1) {
                    zipOut.write(temp);
                }
                input.close();
            }
        }
        zipOut.close();
    }


    public static String readFile(String strFilePath) {

        BufferedReader buffReader = null;
        String strRespone = "";

        try {
            StringBuffer strBuffOutput = new StringBuffer();

            buffReader = new BufferedReader(new FileReader(strFilePath));
            String line = "";
            while ((line = buffReader.readLine()) != null) {
                strBuffOutput.append(line + "\n");

                if (strBuffOutput.capacity() > 102400) {
                    strRespone += strBuffOutput.toString();
                    strBuffOutput.setLength(0);
                }
            }
            strRespone += strBuffOutput.toString();
            buffReader.close();

        } catch (FileNotFoundException eIOException) {
            eIOException.printStackTrace();
            Log.d(eIOException.toString(), eIOException.toString());
            strRespone = "FileNotFoundException";
        } catch (IOException eIOException) {
            eIOException.printStackTrace();
            strRespone = "IOExecption";

        }
        return strRespone;
    }
}
