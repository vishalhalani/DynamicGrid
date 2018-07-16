package com.vishal.vishaltest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etRow)
    EditText etRow;
    @BindView(R.id.etColumn)
    EditText etColumn;
    @BindView(R.id.btnGenrateGrid)
    Button btnGenrateGrid;

    @BindView(R.id.btnRandom)
    Button btnRandom;
    @BindView(R.id.btnCalculate)
    Button btnCalculate;
    @BindView(R.id.etInputNumberBox)
    EditText etInputNumberBox;
    @BindView(R.id.btnResult)
    Button btnResult;
    @BindView(R.id.table_main)
    TableLayout tableMain;
    @BindView(R.id.countoutput)
    TextView countoutput;
    private ArrayList<String> numbers = new ArrayList<>();
    private boolean isGridCreated = false;
    private int row = 0, column = 0;
    final int min = 1;
    final int max = 9;
    private Random random;
    private boolean isResultGenerated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        random = new Random();
    }


    @OnClick({R.id.btnGenrateGrid, R.id.btnRandom, R.id.btnCalculate, R.id.btnResult})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGenrateGrid:
                boolean isValid = checkAllValidation();

                if (isValid) {
                    row = Integer.parseInt(etRow.getText().toString());
                    column = Integer.parseInt(etColumn.getText().toString());
                    generateGrid(row, column);
                }

                break;
            case R.id.btnRandom:
                addRandomNumberToGrid();
                break;
            case R.id.btnCalculate:
                countDuplicateOccurance();
                break;
            case R.id.btnResult:
                if (!TextUtils.isEmpty(etInputNumberBox.getText().toString())) {
                    sumOfnumber(Integer.parseInt(etInputNumberBox.getText().toString()));
                } else {
                    etInputNumberBox.setError("Enter number");
                    etInputNumberBox.requestFocus();
                }

                break;
        }
    }

    /**
     * Add random number to grid
     */
    private void addRandomNumberToGrid() {

        if (isGridCreated) {
            isResultGenerated=false;
            numbers.clear();
            int count = tableMain.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = tableMain.getChildAt(i);
                if (child instanceof TableRow) {
                    TableRow tbrow = (TableRow) child;
                    int colCount = tbrow.getChildCount();
                    if (colCount != -1 && colCount > 0) {
                        for (int j = 0; j < colCount; j++) {
                            View item = tbrow.getChildAt(j);
                            if (item instanceof TextView) {
                                TextView textView = (TextView) (item);
                                String num = String.valueOf(generateRandomNumber(max, min));
                                numbers.add(num);
                                textView.setTag(num);
                                textView.setText(num);
                            }


                        }
                    }
                }
            }

        }

    }

    /**
     * count duplicate occurence of number
     */
    private void countDuplicateOccurance() {

        if(!isResultGenerated)
        {
            isResultGenerated=false;
            if (numbers.size() > 0) {
                countoutput.setText("");
                Set<String> unique = new HashSet<String>(numbers);
                StringBuilder stringBuilder = new StringBuilder();
                for (String num : unique) {
                    stringBuilder.append("Count of ").append(num).append(" is ").append(Collections.frequency(numbers, num)).append("<br>");


                }
                countoutput.setText(Html.fromHtml(stringBuilder.toString()));
            }
        }


    }

    /**
     * make sum for particular number with there occurences in grid
     * @param num input number
     */
    private void sumOfnumber(int num) {
        if (numbers.size() > 0 && numbers.contains(String.valueOf(num))) {
            Set<String> unique = new HashSet<String>(numbers);
            for (String m : unique) {
                if (Integer.parseInt(m) == num) {
                    int counter = Collections.frequency(numbers, m);
                    int sum = counter * num;
                    if (sum != -1) {
                        setSumAtFirstOccurenceOfGrid(num, sum);

                        Toast.makeText(this, "Sum of number " + num + " is " + sum, Toast.LENGTH_SHORT).show();
                        break;
                    }

                }


            }
        } else {
            Toast.makeText(this, "Number not available in grid", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * set sum at first occurence of grid
     * @param num inputed number
     * @param sum sum of inputed number
     */
    private void setSumAtFirstOccurenceOfGrid(int num, int sum) {

        if(!isResultGenerated)
        {
            if (isGridCreated && tableMain != null) {
                isResultGenerated = true;
                boolean isFirstOccurence = false;

                int count = tableMain.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = tableMain.getChildAt(i);
                    if (child instanceof TableRow) {
                        TableRow tbrow = (TableRow) child;
                        int colCount = tbrow.getChildCount();
                        if (colCount != -1 && colCount > 0) {
                            for (int j = 0; j < colCount; j++) {
                                View item = tbrow.getChildAt(j);
                                if (item instanceof TextView) {
                                    TextView textView = (TextView) (item);
                                    try {
                                        if (!textView.getText().equals("") && Integer.parseInt(textView.getText().toString()) == num) {
                                            if (!isFirstOccurence) {
                                                textView.setText(String.valueOf(sum));
                                                isFirstOccurence = true;
//
                                            } else {
                                                textView.setText("");
                                            }

                                        } else {
                                            textView.setText("");
                                        }
                                    } catch (ClassCastException e) {
                                        e.printStackTrace();
                                    }


                                }


                            }
                        }
                    }
                }


            }
        }

    }

    /**
     * method to generate random number
     * @param max max number range
     * @param min min number range
     * @return random number
     */
    private int generateRandomNumber(int max, int min) {


        int num = random.nextInt((max - min) + 1) + min;
        return num;
    }

    /**
     * check validation for generating grid
     * @return true if all validation is valid
     */
    private boolean checkAllValidation() {

        if (TextUtils.isEmpty(etRow.getText().toString().trim())) {
            etRow.requestFocus();
            etRow.setError("Enter number of rows");
            return false;
        } else if (TextUtils.isEmpty(etColumn.getText().toString().trim())) {
            etColumn.requestFocus();
            etColumn.setError("Enter number of column");
            return false;
        } else if (TextUtils.isEmpty(etRow.getText().toString().trim()) && TextUtils.isEmpty(etColumn.getText().toString().trim())) {
            Toast.makeText(this, "Please enter grid row and column number", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * generate dyamic grid
     * @param row number of row
     * @param column number of columns
     */
    public void generateGrid(int row, int column) {

        if (isGridCreated) {
            clearTableLayout();

        }

        for (int i = 0; i < row; i++) {
            TableRow tbrow = new TableRow(this);
            for (int j = 0; j < column; j++) {
                TextView textView = new TextView(this);
                textView.setText("x");
                textView.setPadding(15, 15, 15, 15);
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER);
                tbrow.addView(textView);
            }


            tableMain.addView(tbrow);
        }
        isGridCreated = true;

    }

    /**
     * clear grid
     */
    private void clearTableLayout() {
        int count = tableMain.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = tableMain.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
    }
}
