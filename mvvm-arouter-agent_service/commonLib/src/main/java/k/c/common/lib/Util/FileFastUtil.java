package k.c.common.lib.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class FileFastUtil {
    public final static Charset FILE_WRITE_READER_CHARSET = StandardCharsets.UTF_8;
    /**
     * read the content from file
     *
     * @param path file path
     * @return content
     * @throws IOException ioException
     */
    public static String fileReader(String path) throws IOException {
        return fileReader(path, FILE_WRITE_READER_CHARSET);
    }

    /**
     * read the content from file
     *
     * @param path file path
     * @param charset charset UTF-8 or ISO-8859-1
     * @return content
     * @throws IOException ioException
     */
    public static String fileReader(String path, Charset charset) throws IOException {
        StringBuilder content = new StringBuilder();
        FileInputStream fis = null;
        FileChannel fic = null;
        try {
            fis = new FileInputStream(new File(path));
            fic = fis.getChannel();
            ByteBuffer readBuffer = ByteBuffer.allocate(2 * 1024);
            while (true) {
                int readBytes = fic.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    readBuffer.clear();
                    content.append(new String(bytes, charset));
                } else {
                    break;
                }
            }
        } finally {
            if (fic != null) {
                fic.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return content.toString();
    }

    /**
     * write the str to file
     *
     * @param path file path
     * @param str  content
     * @throws IOException ioException
     */
    public static void fileWrite(String path, String str) throws IOException {
        byte[] b = str.getBytes(FILE_WRITE_READER_CHARSET);
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
            fos.flush();
            FileDescriptor fd = fos.getFD();
            fd.sync();
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

    /**
     * write the inputStream to file
     *
     * @param path file path
     * @param is   inputStream
     * @throws IOException ioException
     */
    public static void fileWrite(String path, InputStream is) throws IOException {
        fileWrite(path, readStreamToString(is));
    }

    /**
     *
     */
    public static void fileWriteBig(String path, InputStream inputStream) throws IOException {
        byte[] buffer = new byte[100 * 1024];
        int len = 0;
        FileOutputStream fos = null;
        FileChannel foc = null;
        File writeFile = new File(path);
        if (writeFile.exists()) {
            writeFile.delete();
        }
        File fileParent = writeFile.getParentFile();
        if(!fileParent.exists()){
            fileParent.mkdirs();
        }
        writeFile.createNewFile();

        fos = new FileOutputStream(writeFile, true);
        foc = fos.getChannel();

        ByteBuffer writeBuffer = ByteBuffer.allocate(buffer.length);
        while ((len = inputStream.read(buffer)) != -1) {
            writeBuffer.rewind();
            writeBuffer.put(buffer);
            writeBuffer.flip();
            writeBuffer.limit(len);
            foc.write(writeBuffer);
        }
        fos.flush();
        FileDescriptor fd = fos.getFD();
        fd.sync();
        inputStream.close();
        foc.close();
        fos.close();
    }

    /**
     * nio copy file
     *
     * @param fromPath from file path
     * @param toPath   to file path
     * @throws IOException IoException
     */
    public static void fileCopy(String fromPath, String toPath) throws IOException {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        FileChannel fic = null;
        FileChannel foc = null;
        try {
            fin = new FileInputStream(new File(fromPath));
            fout = new FileOutputStream(new File(toPath));
            fic = fin.getChannel();
            foc = fout.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(1024 << 4);
            while (fic.read(bb) > 0) {
                bb.flip();
                foc.write(bb);
                bb.clear();
            }
            fout.flush();
            FileDescriptor fd = fout.getFD();
            fd.sync();
        } finally {
            if (null != fic) {
                fic.close();
            }
            if (foc != null) {
                foc.close();
            }
            if (null != fin) {
                fin.close();
            }
            if (null != fout) {
                fout.close();
            }
        }
    }


    /**
     * read the stream to string
     *
     * @param inputStream inputStream
     * @return string
     * @throws IOException ioException
     */
    public static String readStreamToString(InputStream inputStream) throws IOException {
        return readStreamToString(inputStream, "ISO-8859-1");
    }

    /**
     * read the stream to string
     *
     * @param inputStream inputStream
     * @return string
     * @throws IOException ioException
     */
    public static String readStreamToString(InputStream inputStream, String charsetName) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        String result = byteArrayOutputStream.toString(charsetName);

        inputStream.close();
        byteArrayOutputStream.close();
        return result;
    }

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
     * @return false means MD5 is equal.
     * true means MD5 is unequal.
     */
    public static boolean checkMD5(String checksum, String savePath, String fileName) {

        File file = new File(savePath + fileName);
        String localFileMD5 = "";
        if (file.isFile() && file.exists()) {
            localFileMD5 = MD5.getMD5(file);
        }
        return localFileMD5.equals(checksum);
    }

    /**
     *
     * @param file
     * @return
     */
    private static long getFileSize(File file) {
        return file.length();
    }

    /**
     *
     * @param f
     * @return
     */
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
     * delete file，can file or folder
     *
     * @param fileName delete file name
     *
     * @return delete return true，other return false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("delete:" + fileName + "no exist！");
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * delete one file
     *
     * @param fileName delete file name
     *
     * @return delete return true，other return false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("delete one file" + fileName + "success！");
                return true;
            } else {
                System.out.println("delete one file" + fileName + "fail！");
                return false;
            }
        } else {
            System.out.println("delete one file fail：" + fileName + "no exist！");
            return false;
        }
    }

    /**
     * delete folder and child folder
     *
     * @param dir delete dir
     * @return delete return true，other return false
     */
    public static boolean deleteDirectory(String dir) {
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("delete dir fail：" + dir + "no exist！");
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        if(files != null){
            for (int i = 0; i < files.length; i++) {
                // delete child file
                if (files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                }
                // delete child dir
                else if (files[i].isDirectory()) {
                    flag =deleteDirectory(files[i]
                            .getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                }
            }
        }

        if (!flag) {
            System.out.println("delete dir fail！");
            return false;
        }
        // delete current dir
        if (dirFile.delete()) {
            System.out.println("delete dir:" + dir + "success！");
            return true;
        } else {
            return false;
        }
    }

}
