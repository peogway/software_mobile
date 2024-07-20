package com.example.project_software_mobile;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Random;


public class FragmentQuiz extends Fragment{

    ArrayList<MunicipalityData>  currentMuniDataList = Storage.currentMunicipalityData;
    private static final String ARG_CITY_NAME = "cityName";
    private TextView txtDes, txtTitle, txtStatus, txtNumber, txtPoint, finalDesText;
    private Button buttonReady, checkButton, nextButton, tryAgainButton;
    private RadioButton ans1, ans2, ans3;
    private LinearLayout questionLayout, resultLayout, finalResult;
    private int i=1, point =0;
    private QuizData question;
    private RadioGroup option;
    private RadioButton selectedRadioButton;
    private ImageView imageStatus;
    private ImageView finalImage;


    public static FragmentQuiz newInstance(String cityName) {
        FragmentQuiz fragment = new FragmentQuiz();
        Bundle args = new Bundle();
        args.putString(ARG_CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String cityName = getArguments().getString(ARG_CITY_NAME);
        ArrayList<QuizData> questionList = Storage.getQuestionList(cityName);


        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        txtTitle = view.findViewById(R.id.txtTitle);
        txtStatus = view.findViewById(R.id.txtStatus);
        txtNumber = view.findViewById(R.id.quesNumber);
        txtPoint = view.findViewById(R.id.txtPoint);

        finalDesText = view.findViewById(R.id.finalDesText);
        finalResult = view.findViewById(R.id.finalResult);
        finalImage = view.findViewById(R.id.finalImage);
        tryAgainButton = view.findViewById(R.id.tryAgainButton);

        checkButton = view.findViewById(R.id.checkButton);
        nextButton = view.findViewById(R.id.nextQuestion);

        questionLayout = view.findViewById(R.id.layoutQuestion);
        resultLayout = view.findViewById(R.id.resultLayout);

        txtDes = view.findViewById(R.id.txtReady);

        ans1 = view.findViewById(R.id.ans1);
        ans2 = view.findViewById(R.id.ans2);
        ans3 = view.findViewById(R.id.ans3);


        imageStatus = view.findViewById(R.id.imageStatus);


        option = view.findViewById(R.id.options);

        option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkButton.setVisibility(View.VISIBLE);
                selectedRadioButton = view.findViewById(checkedId);
            }
        });




        buttonReady = view.findViewById(R.id.buttonReady);
        buttonReady.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                txtDes.setVisibility(View.GONE);
                buttonReady.setVisibility(View.GONE);
                questionLayout.setVisibility(View.VISIBLE);

                String quesNumberString ="Question: "+ i + "/10";
                String pointString ="Point: "+ point;

                txtNumber.setText(quesNumberString);
                txtPoint.setText(pointString);

                Random rand = new Random();
                int quesNum = rand.nextInt(questionList.size());

                question = questionList.get(quesNum);
                txtTitle.setText(question.getTitle());

                if (question.getTfQues()){

                    ans3.setVisibility(View.GONE);

                    ans1.setText(question.getQues1());
                    ans2.setText(question.getQues2());
                    ans3.setText(question.getQues3());


                }else{
                    ans3.setVisibility(View.VISIBLE);

                    ans1.setText(question.getQues1());
                    ans2.setText(question.getQues2());
                    ans3.setText(question.getQues3());


                }
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkButton.setVisibility(View.GONE);
                boolean check = (selectedRadioButton.getText().toString().equals(question.getAnswer()));
                option.clearCheck();
                if (check){
                    point++;
                    imageStatus.setImageResource(R.drawable.firework);
                    txtStatus.setText("Correct!");
                    ViewCompat.setBackgroundTintList(resultLayout, ColorStateList.valueOf(Color.parseColor("#81C784")));
                    ViewCompat.setBackgroundTintList(nextButton, ColorStateList.valueOf(Color.parseColor("#FF5722")));
                }else{
                    imageStatus.setImageResource(R.drawable.sad);
                    String statusString = "Incorrect! The answer is "+question.getAnswer() +".";
                    txtStatus.setText(statusString);
                    ViewCompat.setBackgroundTintList(resultLayout, ColorStateList.valueOf(Color.parseColor("#DDEF5350")));
                    ViewCompat.setBackgroundTintList(nextButton, ColorStateList.valueOf(Color.parseColor("#FFCA28")));

                }

                questionLayout.setVisibility(View.GONE);
                resultLayout.setVisibility(View.VISIBLE);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                i++;

                if (i>10){
                    resultLayout.setVisibility(view.GONE);
                    String txtFinal = "You got " +point+ " correct answer!";
                    finalDesText.setText(txtFinal);
                    if (point>5){
                        finalImage.setImageResource(R.drawable.clapping);
                    }else{
                        finalImage.setImageResource(R.drawable.hate);
                    }
                    finalResult.setVisibility(view.VISIBLE);


                    return;
                }
                checkButton.setVisibility(View.GONE);
                Random rand = new Random();
                int quesNum = rand.nextInt(questionList.size());

                question = questionList.get(quesNum);
                txtTitle.setText(question.getTitle());

                if (question.getTfQues()){

                    ans3.setVisibility(View.GONE);

                    ans1.setText(question.getQues1());
                    ans2.setText(question.getQues2());
                    ans3.setText(question.getQues3());


                }else{
                    ans3.setVisibility(View.VISIBLE);

                    ans1.setText(question.getQues1());
                    ans2.setText(question.getQues2());
                    ans3.setText(question.getQues3());


                }

                String quesNumberString ="Question: "+ i + "/10";
                String pointString ="Point: "+ point;

                txtNumber.setText(quesNumberString);
                txtPoint.setText(pointString);

                resultLayout.setVisibility(View.GONE);
                questionLayout.setVisibility(View.VISIBLE);

            }
        });



        tryAgainButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                i=1;
                point=0;
                Random rand = new Random();
                int quesNum = rand.nextInt(questionList.size());

                question = questionList.get(quesNum);
                txtTitle.setText(question.getTitle());

                if (question.getTfQues()){

                    ans3.setVisibility(View.GONE);

                    ans1.setText(question.getQues1());
                    ans2.setText(question.getQues2());
                    ans3.setText(question.getQues3());


                }else{
                    ans3.setVisibility(View.VISIBLE);

                    ans1.setText(question.getQues1());
                    ans2.setText(question.getQues2());
                    ans3.setText(question.getQues3());


                }

                String quesNumberString ="Question: "+ i + "/10";
                String pointString ="Point: "+ point;

                txtNumber.setText(quesNumberString);
                txtPoint.setText(pointString);
                checkButton.setVisibility(View.GONE);
                finalResult.setVisibility(View.GONE);
                questionLayout.setVisibility(View.VISIBLE);
            }
        });


        ImageView homeImage = view.findViewById(R.id.homeImage);

        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                // Optionally, if you want to clear all previous activities on the stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Start MainActivity
                startActivity(intent);
                // Optionally, if you want to finish the current activity (e.g. if it's also an activity)
                if (getActivity() != null) {
                    getActivity().finish();
                }

                // Do something in response to the image view click
            }
        });



        ImageView resetImage = view.findViewById(R.id.resetImage);
        resetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i=1;
                point =0;

                txtDes.setVisibility(View.VISIBLE);
                buttonReady.setVisibility(View.VISIBLE);

                option.clearCheck();
                checkButton.setVisibility(View.GONE);
                questionLayout.setVisibility(View.GONE);
                resultLayout.setVisibility(View.GONE);
                finalResult.setVisibility(View.GONE);

                Animation blinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);

                // Apply the blink animation to the entire view or specific elements
                v.startAnimation(blinkAnimation);


            }
        });




        // Inflate the layout for this fragment
        return view;
    }
}
