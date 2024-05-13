package bit;


import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class FileType {


    public static void main(String[] args) throws Exception {
        long zipType = 0x06054b50;
        System.out.println(zipType);
        System.out.println(Long.toHexString(zipType));


        byte[] intBuff = new byte[4];
        writeIntLittleEndian(intBuff, 0, (int) zipType);
//        File file = getFileFromResources();
//        System.out.println(file.getAbsolutePath());
//        FileUtils.writeByteArrayToFile(new File(file.getAbsoluteFile() + "/testzip.zip"), intBuff);

//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(file.getAbsoluteFile() + "/testzipObject.zip")));
//        objectOutputStream.writeInt((int) zipType);
//        objectOutputStream.close();
    }


    /**
     * int 4个字节，分别取出4个字节
     * 以0x06054b50为例
     *
     * @param array
     * @param pos
     * @param value
     */
    public static void writeIntLittleEndian(byte[] array, int pos, int value) {
        array[pos + 3] = (byte) (value >>> 24);//右移动3个字节
        array[pos + 2] = (byte) (value >>> 16);//右移两个字节
        array[pos + 1] = (byte) (value >>> 8);//右移一个字节
        array[pos] = (byte) (value & 0xFF);//取出最低位50

    }

    private static final String TEST_FILES_FOLDER_NAME = "test-files";

//    public static File getFileFromResources(String fileName) {
//        final String path = "/" + TEST_FILES_FOLDER_NAME + "/" + fileName;
//        return new File(bit.opration.FileType.class.getResource(path).getFile());
//    }
//
//    public static File getFileFromResources() {
//        final String path = "/" + TEST_FILES_FOLDER_NAME + "/";
//        return new File(bit.opration.FileType.class.getResource(path).getFile());
//    }

    public int readIntLittleEndian(byte[] b) {
        return ((b[0] & 0xff) | (b[1] & 0xff) << 8)
                | ((b[2] & 0xff) | (b[3] & 0xff) << 8) << 16;
    }
}
