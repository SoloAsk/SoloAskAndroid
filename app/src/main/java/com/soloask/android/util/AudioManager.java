package com.soloask.android.util;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Lebron on 2016/6/20.
 */
public class AudioManager {
    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;

    private static AudioManager mAudioInstance; // 单例

    public boolean isPrepared = false;

    private AudioManager() {
        this.mDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public interface AudioStateChangeListener {
        void wellPrepared();

        void Preparedfailed();
    }

    public AudioStateChangeListener mAudioStateChangeListener;

    public void setOnAudioStateChangeListener(AudioStateChangeListener listener) {
        mAudioStateChangeListener = listener;
    }

    public static AudioManager getInstance() {
        if (mAudioInstance == null) {
            synchronized (AudioManager.class) {
                if (mAudioInstance == null) {
                    mAudioInstance = new AudioManager();
                }
            }
        }
        return mAudioInstance;
    }

    public void prepareAudio() {
        try {
            isPrepared = false;
            File fileDir = new File(mDir + "/SoloAsk");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String fileName = generateFileName();
            File file = new File(fileDir, fileName);

            mCurrentFilePath = file.getAbsolutePath();
            mMediaRecorder = new MediaRecorder();
            // 设置音频源
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            // 设置音频编码
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            // 设置输出文件
            mMediaRecorder.setOutputFile(file.getAbsolutePath());

            mMediaRecorder.prepare();
            mMediaRecorder.start();
            // 准备结束
            isPrepared = true;
            if (mAudioStateChangeListener != null) {
                mAudioStateChangeListener.wellPrepared();
            }
        } catch (IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (mAudioStateChangeListener != null) {
                mAudioStateChangeListener.Preparedfailed();
            }
        }
    }

    private String generateFileName() {
        return Constant.FILE_NAME_VOICE;
    }

    public void stopAudio() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void reRecording() {
        stopAudio();
        if (mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }
    }

    public String getCurrentPath() {
        return mCurrentFilePath;
    }
}
