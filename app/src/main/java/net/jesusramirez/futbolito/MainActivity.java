package net.jesusramirez.futbolito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Variable para buscar el sensor en el dispositivo
    private SensorManager mSensorManager;
    //El acelerometro
    private Sensor mAccelerometer;
    //Vista personalizada
    private AnimatedView mAnimatedView = null;
    //Marcador de los dos jugadores
    private int jug1, jug2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Se inicializan con 0 al inicio del juego
        jug1=0;
        jug2=0;
        //Se añaden configuraciones a la vista personalizada
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Se inicializa el SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Se obtiene del dispositivo el acelerometro
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Se inicializa la vista y se coloca en pantalla
        mAnimatedView = new AnimatedView(this);
        setContentView(mAnimatedView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) { }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAnimatedView.onSensorEvent(event);
        }
    }

    //La siguiente clase define la vista personalizada
    public class AnimatedView extends View {
    // Radio de la pelota
        private static final int CIRCLE_RADIUS = 25;
    // Objetos Paint para dibujar en pantalla los elementos con sus atributos
        private Paint mPaint;
        private Paint mPaint2;
        private Paint mPaint3;
    //Coordenadas iniciales de la pelotita
        private int x = 360;
        private int y = 685;
        private int viewWidth;
        private int viewHeight;

        public AnimatedView(Context context) {
            super(context);
            //Se colocan las propiedades que tendran los elementos que se van a dibujar
            mPaint = new Paint();
            mPaint.setColor(Color.YELLOW);
            mPaint2= new Paint();
            mPaint2.setColor(Color.RED);
            mPaint3 = new Paint();
            mPaint3.setColor(Color.BLACK);
            mPaint3.setTextSize(16);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            viewWidth = w;
            viewHeight = h;
        }

     // SensorEvent es el que escucha los valores del sensor de el parametro que recibe
     // se obtienen las coordenadas para dibujar la pelotita y simular un movimiento
        public void onSensorEvent (SensorEvent event) {
     //Datos que contiene el sensor
            x = x - (int) event.values[0];
            y = y + (int) event.values[1];
     //Limites de la pantalla
            if (x <= 0 + CIRCLE_RADIUS) {
                x = 0 + CIRCLE_RADIUS;
            }
            if (x >= viewWidth - CIRCLE_RADIUS) {
                x = viewWidth - CIRCLE_RADIUS;
            }
            if (y <= 0 + CIRCLE_RADIUS) {
                y = 0 + CIRCLE_RADIUS;
            }
            if (y >= viewHeight - CIRCLE_RADIUS) {
                y = viewHeight - CIRCLE_RADIUS;
            }
      //Limites de las porterias
            if(((x > 218 & x < 495) & (y > 0 & y < 60))){
                jug1++;
                x = 360;
                y = 685;
            }


            if(((x > 217 & x < 495) & (y > 1210 & y < 1324))){
                jug2++;
                x = 360;
                y = 685;
            }
        }

    //onDraw se usa para dibujar las formas de los elementos en este caso la pelotita,
    //las porterias y el marcador de cada jugador
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(x, y, CIRCLE_RADIUS, mPaint);
            canvas.drawRect(200,0, 500,75,mPaint2);
            canvas.drawRect(200,viewHeight-75, 500,viewHeight,mPaint2);
            float textSize = mPaint3.getTextSize();
            mPaint3.setTextSize(textSize * 10);
            canvas.drawText(jug1+"", 10, 150, mPaint3);
            canvas.drawText(jug2+"", 10, viewHeight-75, mPaint3);
            mPaint3.setTextSize(textSize);
            invalidate();
        }
    }
}