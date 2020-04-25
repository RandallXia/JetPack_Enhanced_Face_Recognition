package com.randolltest.facerecognition.util;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.randolltest.facerecognition.data.Constants;

import java.util.List;

/**
 * @author randoll.
 * @Date 4/23/20.
 * @Time 16:43.
 */
public class CameraUtils {

    public static Camera openCamera() {
        int cameraId = 0;

        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras == 0) {
            ToastUtils.showShort("无可用相机！");
            return null;
        } else {
            while (cameraId < numberOfCameras) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(cameraId, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    break;
                }
                cameraId++;
            }
        }
        return Camera.open(cameraId);
    }

    public static void startPreview(Camera camera, SurfaceHolder holder, Camera.PreviewCallback callback) {
        try {
            camera.setPreviewDisplay(holder);
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

            int w = Constants.CAMERA_PREVIEW_WIDTH;
            int h = Constants.CAMERA_PREVIEW_HEIGHT;
            for (Camera.Size size : previewSizes) {
                if (size.width - w <= 100) {
                    w = size.width;
                    h = size.height;
                    LogUtils.i("previewSizes width:" + size.width + " | height:" + size.height);
                    break;
                }
            }

            parameters.setPreviewSize(w, h);
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setJpegQuality(Constants.JPEG_QUALITY);
            parameters.setPictureSize(w, h);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(Constants.DISPLAY_ORIENTATION);

            camera.startPreview();
            camera.setPreviewCallback(callback);
        } catch (Exception e) {
            LogUtils.e("相机预览出错：" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void stopCamera(Camera camera) {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
        } else {
            LogUtils.e("关闭相机出错： parameter 'camera' is null！");
        }
    }
}