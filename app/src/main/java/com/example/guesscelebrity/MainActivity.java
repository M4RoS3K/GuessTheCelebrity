/*
* 1. define HtmlDownloader class and getHtml() method and call it in onCreate method to get the web content in html format
* 2. define getCelebrityNames() and getCelebritiesImageUrls() methods using regex to get name and image url of particular celebrity
* 3. define two ArrayList global variables which will hold name and image url and initialize them in onCreate method using getCelebrityNames() and getCelebritiesImageUrls() methods
* 4. define ImageDownloader class and downloadImg() method to download images
* 5. define generateNumber() method to get random number
* 6. define logic of every new round in newRound() method - initialize correct answer and another ArrayList method with possible answers
* 7. define logic of checkCelebrity() method, which is onClick method of UI buttons and will check if the tapped answer is the correct answer
*/

package com.example.guesscelebrity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private String initUrl = "http://www.posh24.se/kandisar";
    private ArrayList<String> celebrities;
    private ArrayList<String> celebritiesImageUrls;
    private ArrayList<String> answers = new ArrayList<String>();
    private ImageView img_celebrity;
    private Button btn_0;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private int locationOfCorrectAnswer;

    public void checkCelebrity(View view){
        String message;

        if(locationOfCorrectAnswer == Integer.parseInt(view.getTag().toString())){
            message = "Correct";
            newRound();
        } else {
            message = "Wrong";
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void newRound(){
        int locationOfCelebrity = generateNumber(celebrities.size());
        locationOfCorrectAnswer = generateNumber(4);

        answers.clear();

        Bitmap celebrityImage = downloadImg(celebritiesImageUrls.get(locationOfCelebrity));
        String celebrityName = celebrities.get(locationOfCelebrity);

        for(int i = 0; i < 4; i++){
            if(i == locationOfCorrectAnswer){
                answers.add(celebrityName);
            } else {
                int wrongAnswer = generateNumber(celebrities.size());

                while(wrongAnswer == locationOfCelebrity){
                    wrongAnswer = generateNumber(celebrities.size());
                }

                answers.add(celebrities.get(wrongAnswer));
            }
        }

        img_celebrity.setImageBitmap(celebrityImage);
        btn_0.setText(answers.get(0));
        btn_1.setText(answers.get(1));
        btn_2.setText(answers.get(2));
        btn_3.setText(answers.get(3));
    }

    private ArrayList<String> getCelebrityNames(String html){
        ArrayList<String> celebritiesNames = new ArrayList<String>();

        Pattern p = Pattern.compile("alt=\"(.*?)\"");
        Matcher m = p.matcher(html);

        while(m.find()){
            celebritiesNames.add(m.group(1));
        }

        return celebritiesNames;
    }

    private Bitmap downloadImg(String url){
        ImageDownloader imageDownloader = new ImageDownloader();
        Bitmap img;

        try{
            img = imageDownloader.execute(url).get();
            return img;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<String> getCelebritiesImageUrls(String html){
        ArrayList<String> celebritiesImageUrls = new ArrayList<String>();

        Pattern p = Pattern.compile("img src=\"(.*?)\"");
        Matcher m = p.matcher(html);

        while(m.find()){
            celebritiesImageUrls.add(m.group(1));
        }

        return celebritiesImageUrls;
    }

    private String getHtml(String url){
        String html;

        HtmlDownloader downloadHtml = new HtmlDownloader();

        try {
            html = downloadHtml.execute(url).get();
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int generateNumber(int max){
        Random rand = new Random();
        return rand.nextInt(max);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_celebrity = (ImageView) findViewById(R.id.img_celebrity);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);

        String html = getHtml(initUrl);
        celebrities = getCelebrityNames(html);
        celebritiesImageUrls = getCelebritiesImageUrls(html);

        newRound();
    }
}