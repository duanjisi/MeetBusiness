package com.atgc.cotton.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;

/**
 * Created by GMARUnity on 2017/6/20.
 */
public class FileUtils {
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    public static File createTmpFile(Context context) throws IOException {
        File dir = null;
        if (TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if (!dir.exists()) {
                dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera");
                if (!dir.exists()) {
                    dir = getCacheDirectory(context, true);
                }
            }
        } else {
            dir = getCacheDirectory(context, true);
        }
        return File.createTempFile(JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, dir);
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    // 将模板View的图片转化为Bitmap
    public static Bitmap getBitmapByView(View v) {
        if (v.getWidth() > 0 && v.getHeight() > 0) {
            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            v.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
//        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
//                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        v.draw(canvas);
//        return bitmap;
    }

    public static String getFileNameFromPath(String url) {
        int index = url.lastIndexOf('/');
        return url.substring(index + 1);
    }

    public static String compressImage(String filePath, String targetPath, int quality) {
        Bitmap bm = getSmallBitmap(filePath);
        //旋转照片角度，省略
        /*int degree = readPictureDegree(filePath);
        if(degree!=0){
            bm=rotateBitmap(bm,degree);
        }*/
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
        }
        return outputFile.getPath();
    }

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static boolean deleteTempFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() && file.delete();
    }

    public static final String filePath = Environment.getExternalStorageDirectory() + "/meetBus/";

    // 将生成的图片保存到内存中
    public static String saveBitmap(Bitmap bitmap, String name) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(filePath);
            if (!dir.exists())
                dir.mkdir();
//            File file = new File(filePath + name + ".jpg");
            File file = new File(filePath + name);
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                    out.flush();
                    out.close();
                }
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String DATA_PATH = Environment.getDataDirectory().getPath();
    private static final String SD_STATE = Environment.getExternalStorageState();
    public static final String NAME = "audioWave";

    public static String getAppPath() {
        StringBuilder sb = new StringBuilder();
        if (SD_STATE.equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            sb.append(SD_PATH);
        } else {
            sb.append(DATA_PATH);
        }
        sb.append(File.separator);
        sb.append(NAME);
        sb.append(File.separator);
        return sb.toString();
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                String[] filePaths = file.list();
                for (String path : filePaths) {
                    deleteFile(filePath + File.separator + path);
                }
                file.delete();
            }
        }
    }

    /**
     * 按尺寸压缩图片
     *
     * @param srcPath  图片路径
     * @param desWidth 压缩的图片宽度
     * @return Bitmap 对象
     */

    public static Bitmap compressImageFromFile(String srcPath, float desWidth) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float desHeight = desWidth * h / w;
        int be = 1;
        if (w > h && w > desWidth) {
            be = (int) (newOpts.outWidth / desWidth);
        } else if (w < h && h > desHeight) {
            be = (int) (newOpts.outHeight / desHeight);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

//        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
        newOpts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        int degree = readPictureDegree(srcPath);
        Bitmap bitmap1 = rotaingImageView(degree, bitmap);
        if (bitmap != null)
            bitmap = null;
        return bitmap1;
    }

    private static Bitmap rotaingImageView(int degree, Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 获得图片的旋转角度
     *
     * @param filePath
     * @return
     */
    public static int readPictureDegree(String filePath) {
        // 获得图片的角度
        int degreen = 0;
        ExifInterface ef;
        try {
            ef = new ExifInterface(filePath);
            int orientation = ef.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degreen = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degreen = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degreen = 270;
                    break;

                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degreen;
    }

    /**
     * 压缩图片（质量压缩）
     *
     * @param image
     */

    public static File compressImage(Bitmap image, String path) {
        File bf = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;

        while (baos.toByteArray().length / 1024 > 2048) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            //long length = baos.toByteArray().length;
        }
//        long length = baos.toByteArray().length;
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片

        String imageName = System.currentTimeMillis() + ".jpg";
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.mkdirs();//如果路径不存在就先创建路径
            }
            bf = new File(file, imageName);
            FileOutputStream fos = new FileOutputStream(bf);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bf;
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        // File appDir = new File(Environment.getExternalStorageDirectory(), "boss66Im");
        File appDir = new File(context.getFilesDir().getPath(), "boss66Im");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String path = file.getAbsolutePath();
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }
}
