package kr.co.hoon.a180615surfaceview;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // 인스턴스 변수
    SurfaceHolder holder;
    Camera camera;

    // 서피스뷰의 너비와 높이를 저장할 변수
    int surfaceWidth;
    int surfaceHeight;

    // 이미지의 너비와 높이를 저장할 변수
    int bitmapWidth;
    int bitmapHeight;

    // 생성자 - SurfaceView는 default 생성자가 없기 때문에 반드시 만들어서 super를 이용해 호출해줘야함
    public CameraSurfaceView(Context context) {
        super(context);
        // 초기화메소드 호출
        init();
    }
    public CameraSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    // 초기화 메소드
    public void init(){
        // SurfaceHolder 가져오기
        holder = getHolder();
        // 콜백함수 위치설정
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    // 서피스뷰가 만들어질 때 호출되는 메소드
    // 명시적으로 오버라이드 된 메소드라는 것을 알려주는 어노테이션
    // 이 어노테이션이 있는데 상위클래스나 인터페이스에 메소드가 없으면 에러가 발생함
    // 메소드 이름이 틀리지 않았는지 확인할 수 있음
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        // 카메라 열기
        camera = Camera.open();
        try{
            // 카메라를 화면에 출력
            camera.setPreviewDisplay(this.holder);
        }catch(Exception e){
            Log.e("카메라 예외", e.getMessage());
        }
    }

    // 서피스뷰의 크기가 변경될 때 호출되는 메소드
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        surfaceHeight = height;
        surfaceWidth = width;

        // 카메라를 회전하면 뷰에 출력할 때도 회전해서 출력
        try{
            // 운영체제 버전 확인
            // FROYO 이전 버전이면 바로 돌리고 이후 버전이면 파라미터설정을 이용해서 회전
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO){
                camera.setDisplayOrientation(90);
            }else{
                Camera.Parameters parameters = camera.getParameters();
                parameters.setRotation(90);
                camera.setParameters(parameters);
            }
        }catch(Exception e){
            Log.e("회전 예외", e.getMessage());
        }
        // 미리보기 시작
        camera.startPreview();
    }

    // 서피스뷰가 소멸될 때 호출되는 메소드
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 미리보기 중지 - 콜백 중지 - 메모리해제 - null 대입
        camera.stopPreview();
        camera.setPreviewCallback(null);
        camera.release();
        camera = null;
    }
}
