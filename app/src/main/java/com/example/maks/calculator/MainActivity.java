package com.example.maks.calculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.support.v4.view.GestureDetectorCompat;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements GestureDetector.OnGestureListener {

    private GestureDetectorCompat gestureDetectorCompat;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private EditText cellingText;
    private EditText bottomText;
    //  private TextView displayOperation;
    private EditText firstTextVar;
    private EditText secondTextVar;

    private Double firstResDoub;
    private Double secondResDoub;

    private ArrayList<Button> buttons;

    private Button buttonDot;

    private Button buttonNeg;
    private Button buttonVar1;
    private Button buttonVar2;
    private Button buttonSave;
    private Button buttonClear;
    private Button buttonClear1;
    private Button buttonClear2;
    private Button buttonUp;

    //Variables to hold operands and type of calculations
    private Double operand1 = null;
    private String pendingOperation = "";

    private static final String STATE_PENDING_OPERATION = "PendingOperation";
    private static final String STATE_OPERAND1 = "Operand1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gestureDetectorCompat = new GestureDetectorCompat(this, this);

        cellingText = findViewById(R.id.ET_cellingText);
        bottomText = findViewById(R.id.ET_bottomText);
        //  displayOperation = (TextView) findViewById(R.id.operation);
        firstTextVar = findViewById(R.id.textVar1);
        secondTextVar = findViewById(R.id.textVar2);

        buttonsFindViews();

        setNumListeners();

    }


    private void buttonsFindViews() {
        buttons = new ArrayList<>();
        buttons.add(findViewById(R.id.button0));
        buttons.add(findViewById(R.id.button1));
        buttons.add(findViewById(R.id.button2));
        buttons.add(findViewById(R.id.button3));
        buttons.add(findViewById(R.id.button4));
        buttons.add(findViewById(R.id.button5));
        buttons.add(findViewById(R.id.button6));
        buttons.add(findViewById(R.id.button7));
        buttons.add(findViewById(R.id.button8));
        buttons.add(findViewById(R.id.button9));

        buttonDot = findViewById(R.id.buttonDot);

        buttonNeg = findViewById(R.id.buttonNeg);
        buttonVar1 = findViewById(R.id.buttonVar1);
        buttonVar2 = findViewById(R.id.buttonVar2);
        buttonSave = findViewById(R.id.buttonSave);
        buttonClear = findViewById(R.id.buttonClear);
        buttonClear1 = findViewById(R.id.buttonClear1);
        buttonClear2 = findViewById(R.id.buttonClear2);
        buttonUp = findViewById(R.id.buttonUp);
    }

    private void setNumListeners() {
        for(Button button : buttons) {
            button.setOnClickListener(listener);
        }
        buttonDot.setOnClickListener(listener);


        //Raises text from newText to cellingText
        buttonUp.setOnClickListener(v -> {
            String newNum = bottomText.getText().toString();
            cellingText.setText(newNum);
            bottomText.setText("");
        });


        buttonVar1.setOnClickListener(view -> {
            try {
                bottomText.append(firstTextVar.getText().toString());
            } catch (Exception ex) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Nothing", Toast.LENGTH_SHORT).show();
            }

        });

        buttonVar2.setOnClickListener(view ->
                bottomText.append(secondTextVar.getText().toString()));


        buttonSave.setOnClickListener(view -> {
            String res = cellingText.getText().toString();

            if (!res.equals("")) {
                try {
                    String frstStr = firstTextVar.getText().toString();
                    String scndStr = secondTextVar.getText().toString();
                    if (frstStr.equals("")) {
                        firstTextVar.setText(res);
                    } else if (!frstStr.equals("") && scndStr.equals("")) {
                        secondTextVar.setText(res);
                    } else if (!scndStr.equals("") && frstStr.equals("")) {
                        firstTextVar.setText(res);
                    } else {
                        Toast.makeText(this, "Clear one variable", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(this, "Nothing to store", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //General clear button
        buttonClear.setOnClickListener(view -> {
            // operand1 = 0.0;
            cellingText.setText("");
            bottomText.setText("");
            //     displayOperation.setText("");
            // pendingOperation = "";
        });

        //Clean First Variable
        buttonClear1.setOnClickListener(view -> {
            firstResDoub = 0.0;
            firstTextVar.setText("");
        });

        //Clean Second Variable
        buttonClear2.setOnClickListener(view -> {
            secondResDoub = 0.0;
            secondTextVar.setText("");
        });
        //Button Neg Listener
        buttonNeg.setOnClickListener(v -> {
            String value = bottomText.getText().toString();
            if (value.length() == 0) {
                bottomText.setText("-");
            } else {
                try {
                    Double doubleValue = Double.valueOf(value);
                    doubleValue *= -1;
                    bottomText.setText(String.format(Locale.ENGLISH, "%1$,.2f", doubleValue));
                } catch (NumberFormatException ex) {
                    bottomText.setText("");
                }
            }
        });
    }

    //Listener

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button b = (Button) view;
            bottomText.append(b.getText().toString());
        }
    };


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_PENDING_OPERATION, pendingOperation);
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION);
        operand1 = savedInstanceState.getDouble(STATE_OPERAND1);
        // displayOperation.setText(pendingOperation);
    }

    //**PERFORM OPERATION FUNCTIONS**\\
    private void performAddition() {
        Double numRes = Double.valueOf(cellingText.getText().toString()) + Double.valueOf(bottomText.getText().toString());
        cellingText.setText(String.format(Locale.ENGLISH, "%1$,.2f", numRes));
        bottomText.setText("");
    }

    private void performSubtraction() {
        Double numRes = Double.valueOf(cellingText.getText().toString()) - Double.valueOf(bottomText.getText().toString());
        cellingText.setText(String.format(Locale.ENGLISH, "%1$,.2f", numRes));
        bottomText.setText("");
    }

    private void performMultiplying() {
        Double numRes = Double.valueOf(cellingText.getText().toString()) * Double.valueOf(bottomText.getText().toString());
        cellingText.setText(String.format(Locale.ENGLISH, "%1$,.2f", numRes));
        bottomText.setText("");
    }

    private void performDivision() {
        Double newNum = Double.valueOf(bottomText.getText().toString());
        if (newNum != 0) {
            Double numRes = Double.valueOf(cellingText.getText().toString()) / newNum;
            cellingText.setText(String.format(Locale.ENGLISH, "%1$,.2f", numRes));
            bottomText.setText("");
        } else
            Toast.makeText(this, "You're dividing by 0", Toast.LENGTH_SHORT).show();
    }
    //**END PERFORM OPERATION FUNCTIONS**\\


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {


    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(this, "It is a LEFT", Toast.LENGTH_SHORT).show();

                try {
                    performDivision();
                } catch (Exception ex) {
                    Toast.makeText(this, " DIVISION Exception Catched", Toast.LENGTH_SHORT).show();
                }
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(this, "It is a RIGHT", Toast.LENGTH_SHORT).show();

                try {
                    performMultiplying();
                } catch (Exception ex) {
                    Toast.makeText(this, " MULTIPLICATION Exception Catched", Toast.LENGTH_SHORT).show();
                }
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(this, "It is a DOWN", Toast.LENGTH_SHORT).show();

                try {
                    performSubtraction();
                } catch (Exception ex) {
                    Toast.makeText(this, " SUBTRACTION Exception Catched", Toast.LENGTH_SHORT).show();
                }

            } else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(this, "It is a UP", Toast.LENGTH_SHORT).show();
                //Addition Functionality
                try {
                    performAddition();
                } catch (Exception ex) {
                    Toast.makeText(this, " ADDING Exception Catched", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    private static class OnTouchRunnable implements Runnable {
        @Override
        public void run() {


        }
    }
}

