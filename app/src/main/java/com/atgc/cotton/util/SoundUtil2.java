package com.atgc.cotton.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc:声音工具类，包括录音，播放录音等
 * @author: pangzf
 * @date: 2014年11月5日 下午2:50:35
 * @blog:http://blog.csdn.net/pangzaifei/article/details/43023625
 * @github:https://github.com/pangzaifei/zfIMDemo
 * @qq:1660380990
 * @email:pzfpang451@163.com
 */
public class SoundUtil2 {
    private static final double EMA_FILTER = 0.6;
    private static SoundUtil2 INSTANCE;
    private static MediaRecorder mMediaRecorder;
    private double mEMA = 0.0;
    private MediaPlayer mMediaPlayer;

    public SoundUtil2() {
    }

    public static SoundUtil2 getInstance() {
        if (INSTANCE == null) {
            synchronized (SoundUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SoundUtil2();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * 初始化
     */
    private static void initMedia() throws Exception {
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder
                    .setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        }
    }

    /**
     * 开始录音
     *
     * @param name 声音存储的路径
     */
    public void startRecord(Context context, String name) {
        try {
            initMedia();
        } catch (Exception e1) {
            e1.printStackTrace();
            Toast.makeText(context, "麦克风不可用", Toast.LENGTH_SHORT).show();
        }
        StringBuilder sb = getFilePath(context, name);
        mMediaRecorder.setOutputFile(sb.toString());
        Log.e("fff", "录音路径:" + sb.toString());
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            mEMA = 0.0;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    public void stopPlay() throws IllegalStateException {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    // public StringBuilder getFilePath(Context context, String name) {
    // StringBuilder sb = new StringBuilder();
    // sb.append("/storage/emulated/0");
    // sb.append("/");
    // sb.append(name);
    // return sb;
    // }

    public StringBuilder getFilePath(Context context, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(getDiskFielsDir(context));
        sb.append(File.separator);
        sb.append(name);
        return sb;
    }

    /**
     * 获得录音的文件名
     *
     * @return
     */
//    public String getRecordFileName() {
//        return App.getInstance().getSpUtil().getUserId() + "_"
//                + System.currentTimeMillis() + "record.amr";
//        // return System.currentTimeMillis() + ".amr";
//    }

    /**
     * 停止录音
     */
    public void stopRecord() throws IllegalStateException {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * 获得缓存路径
     *
     * @param name
     * @return
     */
    // public String getDiskCacheDir(Context context) {
    // String cachePath;
    // if (Environment.MEDIA_MOUNTED.equals(Environment
    // .getExternalStorageState())
    // || !Environment.isExternalStorageRemovable()) {
    // cachePath = context.getExternalCacheDir().getPath();
    // } else {
    // cachePath = context.getCacheDir().getPath();
    // }
    // return cachePath;
    // }

    /**
     * 获取录音地址
     *
     * @param context
     * @return
     */
    public String getDiskFielsDir(Context context) {
        String cachePath;
        if (Build.VERSION.SDK_INT == 19) {
            cachePath = context.getFilesDir().getPath();
        } else {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = context.getExternalFilesDir(
                        Environment.DIRECTORY_MUSIC).getPath();
            } else {
                cachePath = context.getFilesDir().getPath();
            }
        }
        return cachePath;
    }

    /**
     * 获得缓存路径
     *
     * @return
     */
    // public String getDiskFielsDir(Context context) {
    // String cachePath;
    // if (Environment.MEDIA_MOUNTED.equals(Environment
    // .getExternalStorageState())
    // || !Environment.isExternalStorageRemovable()) {
    // File file = null;
    // if (file == null) {
    // File musicFile = context.getExternalFilesDir(
    // Environment.DIRECTORY_MUSIC);
    // if(musicFile==null){
    //
    // }
    // String path = context.getExternalFilesDir(
    // Environment.DIRECTORY_MUSIC).getPath();
    // file = new File(path);
    // }
    // if (!file.exists()) {
    // file.mkdirs();
    // }
    // cachePath = file.getPath();
    // } else {
    // cachePath = context.getFilesDir().getPath();
    // }
    // return cachePath;
    // }
    public double getAmplitude() {
        if (mMediaRecorder != null)
            return (mMediaRecorder.getMaxAmplitude() / 2700.0);
        else
            return 0;

    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    /**
     * @Description
     */
//    public void playRecorder(Context context, String name) {
//        if (mMediaPlayer == null) {
//            mMediaPlayer = new MediaPlayer();
//        }
//
//        try {
//            if (mMediaPlayer.isPlaying()) {
//                mMediaPlayer.stop();
//            }
//            mMediaPlayer.reset();
//            // mMediaPlayer.setDataSource(getFilePath(context,
//            // name).toString());
//            Log.e("fff", "播放路径:" + getFilePath(context, name).toString());
//            // String filePath = "/storage/emulated/0/1415255297852.amr";
//            // String filePath = getFilePath(context, "1415255297852.amr")
//            // .toString();
//            String filePath = getFilePath(context, name).toString();
//            File file = new File(filePath);
//            if (file.exists()) {
//                mMediaPlayer.setDataSource(filePath);
//                mMediaPlayer.prepare();
//                mMediaPlayer.start();
//                mMediaPlayer
//                        .setOnCompletionListener(new OnCompletionListener() {
//                            public void onCompletion(MediaPlayer mp) {
//                                Log.e("fff", "播放方程");
//                                mMediaPlayer.release();
//                                mMediaPlayer = null;
//                            }
//                        });
//            } else {
//                // 不存在语音文件
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public synchronized void playRecorder(Context context, String audioUrl, final CompletionListener completionListener) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            if (!audioUrl.equals("")) {
                String url = getEncodeUrl(audioUrl);
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mMediaPlayer
                        .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                Log.e("fff", "播放方程");
                                if (completionListener != null) {
                                    completionListener.onCompletion();
                                }
                                mMediaPlayer.release();
                                mMediaPlayer = null;
                            }
                        });
            } else {
                // 不存在语音文件
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getEncodeUrl(String str) {
        String url = "";
        int startIndex = str.indexOf("music/") + 6;
        String str1 = str.substring(0, startIndex);
        String str2 = str.substring(startIndex, str.length());
//        if (isContainChinese(str2)) {
        url = str1 + toURLEncoded(str2);
//        } else {
//            url = str;
//        }
        return url;
    }


    public boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    //    public String toURLEncoded(String paramString) {
//        if (paramString == null || paramString.equals("")) {
//            Log.i("info", "toURLEncoded error:" + paramString);
//            return "";
//        }
//
//        try {
//            String str = new String(paramString.getBytes(), "UTF-8");
//            str = URLEncoder.encode(str, "UTF-8");
//            return str;
//        } catch (Exception localException) {
//            Log.e("toURLEncoded error:" + paramString, localException.getMessage());
//        }
//        return "";
//    }
    public static String toURLEncoded(String str) {
        String url = "";
        try {
            url = URLEncoder.encode(str, "utf-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public interface CompletionListener {
        void onCompletion();
    }
}