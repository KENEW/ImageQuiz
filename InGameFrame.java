
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.awt.Image;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;

public class InGameFrame extends JFrame implements Runnable
{
    public class ImageItem 
    {
        public ImageItem(ImageIcon image, String imageName) 
        {
            this.image = image;
            this.imageName = imageName;
        }

        public ImageIcon image;
        public String imageName;
    }

    private class Panel extends JPanel
    {
        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
    
            g.setColor(Color.GRAY);
            g.drawRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        }
    }

    // 오디오를 출력합니다.
    public static void audio(String name, int loopNum) 
    {
        try 
        {
            File file = new File("./SFX/" + name + ".wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.loop(loopNum);
            clip.start();
        } 
        catch (Exception e) 
        {
            System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
        }
    }

    // 퀴즈를 생성합니다.
    private void CreateQuiz(String name, int imageType)
    {
        System.out.println(name);

        String typeStr = imageType == 0 ? ".png" : ".jpg";
        quizItems.add(new ImageItem(new ImageIcon("./QuizImages/" + name + typeStr), name));
    }

    // UI객체를 생성합니다.
    public void CreateUIItem(String name, int imageType)
    {
        System.out.println(name);

        String typeStr = imageType == 0 ? ".png" : ".jpg";
        uiItems.add(new ImageItem(new ImageIcon("./UIImages/" + name + typeStr), name));
    }

    private static final int START_DEFAULT_RATIO = 1200;
    private static final int SCALE_RATIO_OFFSET = 100;

    private static final int WINDOW_WIDTH = 550;
    private static final int WINDOW_HEIGHT = 900;

    private static final int TIME_OFFSET = 100;

    private String timeStr;
    private JLabel timeLabel;
    private JLabel scoreLabel;

    private int currentScore;

    private String currentInputName;
    private int defaultScoreAdd = 5;
    private int remainQuizNumber;
    private JTextField tf;
    private Thread thread;

    private int currenWidth, currentHeight;
    private Image currentQuizImage;
    private String currentQuizName;
    private int currentImageRatio;

    private int closeUPCount = 10;
    private JLabel imageQuizLabel;

    private int fullTime;;
    private int curTime;

    private ArrayList<ImageItem> quizItems = new ArrayList<ImageItem>();
    private ArrayList<ImageItem> uiItems = new ArrayList<ImageItem>();

    private JLabel resultLabel;
    private JLabel resultScoreLabel;

    // 이미지를 생성합니다.
    private void SetImage(int listID)
    { 
        currentQuizImage = quizItems.get(listID).image.getImage();
    }

    // 이미지를 그립니다.
    private void DrawImage()
    {
        currenWidth = currentQuizImage.getWidth(null) * (int)(closeUPCount);
        currentHeight = currentQuizImage.getHeight(null) * (int)(closeUPCount);

        System.out.println(currenWidth + ", " + currentHeight + ", " + closeUPCount);
        
        Image resizeImage = currentQuizImage.getScaledInstance(currenWidth, currentHeight, currentQuizImage.SCALE_SMOOTH);
        ImageIcon convertImgIcon = new ImageIcon(resizeImage);
        
        imageQuizLabel.setIcon(convertImgIcon);
        add(imageQuizLabel);
        imageQuizLabel.setBounds(0, WINDOW_HEIGHT / 2, WINDOW_WIDTH, WINDOW_WIDTH);

    }

    // 정답을 체크하고 기능을 실행합니다.
    private boolean CorrectCheck(String inputName)
    {
        // 정답이면
        if(currentQuizName.equals(inputName))
        {
            remainQuizNumber--;

            audio("CorrectSFX", 0);
            successLabel.setVisible(true);
            isCorrect = true;
            currentScore = currentScore + defaultScoreAdd + (curTime / 10);
            scoreLabel.setText(currentScore + "");
            closeUPCount = 10;

            if(remainQuizNumber > 0)
            {
                ExecuteQuestion();
            }
            else
            {
                isResult = true;
                System.out.println("모든 퀴즈가 종료되었습니다.");
            }
            
            return true;
        }
        else
        {
            audio("ErrorSFX", 0);

            return false;
        }
    }

    public void GameStart(int quizNumber, int timeOut)
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(true);
        setResizable(false);

        thread = new Thread(this);    
        random = new Random();
        random.setSeed(System.currentTimeMillis());

        imageQuizLabel = new JLabel();

        CreateUIItem("BottomUI", 0);
        CreateUIItem("UpUI", 0);
        CreateUIItem("SuccessUI", 0);
        CreateUIItem("GameResultUI", 0);

        JLabel bottomUI = new JLabel();
        bottomUI.setIcon(uiItems.get(0).image);
        bottomUI.setBounds(-20, WINDOW_HEIGHT - 370, 570, 350);
        bottomUI.setLayout(new FlowLayout());

        JLabel upUI = new JLabel();
        upUI.setIcon(uiItems.get(1).image);
        upUI.setBounds(0, 0, 550, 70);
        upUI.setLayout(new FlowLayout());

        // 0 : png / 1 : jpg
        CreateQuiz("사과", 1);
        CreateQuiz("기타", 1);
        CreateQuiz("토마토", 1);
        CreateQuiz("수박", 0);
        CreateQuiz("딸기", 1);
        CreateQuiz("파인애플", 1);
        CreateQuiz("김치", 1);
        CreateQuiz("피아노", 1);
        CreateQuiz("아보카도", 1);
        CreateQuiz("귤", 1);
        CreateQuiz("치킨", 1);
        CreateQuiz("피자", 1);
        CreateQuiz("토마토", 1);
        CreateQuiz("말", 1);
        CreateQuiz("고슴도치", 1);

        //CreateQuiz("포도", 1);
        //CreateQuiz("모니터", 1);
        //CreateQuiz("에어팟", 1);
        //CreateQuiz("말", 1);
   
        // 시간 초 UI
        timeLabel = new JLabel((timeOut / 10) + "");
        timeLabel.setFont(new Font("TimesRoman", Font.BOLD, 45));
        timeLabel.setBounds(380, 700, 100, 50);
        timeLabel.setLayout(new FlowLayout());
        timeLabel.setForeground(new Color(157, 96, 99));
  
        scoreLabel = new JLabel(currentScore + "");
        scoreLabel.setFont(new Font("TimesRoman", Font.BOLD, 45));
        scoreLabel.setBounds(90, 700, 100, 50);
        scoreLabel.setForeground(new Color(157, 96, 99));
        scoreLabel.setLayout(new FlowLayout());
 
        Action TextFieldEnter = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                if(tf.getText() != null)
                {
                    if(CorrectCheck(tf.getText()))
                    {
                        System.out.println(tf.getText());
                        System.out.println(currentQuizName);
                    }

                    tf.setText("");
                }
            }
        };

        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        tf = new JTextField(20);
        tf.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enterKey, "ENTER");
        tf.getActionMap().put("ENTER", TextFieldEnter);
		tf.setLocation(165,800);
		tf.setSize(200,35);

        resultScoreLabel = new JLabel();
        resultScoreLabel.setFont(new Font("TimesRoman", Font.BOLD, 45));
        resultScoreLabel.setBounds(250, 550, 100, 50);
        resultScoreLabel.setForeground(new Color(157, 96, 99));
        resultScoreLabel.setLayout(new FlowLayout());
        add(resultScoreLabel);
        resultScoreLabel.setVisible(false);

        resultLabel = new JLabel();
        resultLabel.setIcon(uiItems.get(3).image);
        resultLabel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        resultLabel.setLayout(new FlowLayout());
        add(resultLabel);
        resultLabel.setVisible(false);
	
        add(scoreLabel);
        add(timeLabel);
        add(tf);
        add(bottomUI);
        add(upUI);
        ViewSuccessPanel();

        this.remainQuizNumber = quizNumber;
        this.fullTime = timeOut;

        ExecuteQuestion(); 
        
        thread.start();
    }

    private Random random;
    public void ExecuteQuestion()
    {
        curTime = fullTime;
        currentImageRatio = 0;

        int randomID = random.nextInt(quizItems.size());
        currentQuizName = quizItems.get(randomID).imageName;

        // 테스트 케이스
        // int randomID = remainQuizNumber;
        // currentQuizName = quizItems.get(randomID).imageName;

        System.out.println("퀴즈 정답 : " + currentQuizName);

        SetImage(randomID);
        DrawImage();
    }

    private String GetCurrentTime()
    {
        Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		
	    String time = hour + ":" + min + ":" + sec;
        return time;
    }

    private boolean TimeCheck(int time)
    {
        return curTime % time == 0;
    }

    private boolean isCorrect = false;
    private boolean isResult = false;

    public void run() 
    {        
        // 게임이 진행중이면
        while (!isResult) 
        {
            try 
            {
                // 문제가 정답이면
                if(isCorrect)
                { 
                    Thread.sleep(2000);
                    successLabel.setVisible(false);

                    isCorrect = false;
                }
                else
                {
                    // 시간이 남아있으면
                    if(curTime > 0)
                    {
                        curTime--;

                        Thread.sleep(TIME_OFFSET);
                        
                        // 1초가 지났을 경우
                        if(TimeCheck(10))
                        {
                            timeLabel.setText((curTime / 10) + "");
                        }

                        if(TimeCheck(20))
                        {
                            // 남은 확대 개수가 1이상이면 
                            if(closeUPCount > 1)
                            {
                                // 확대하고, 이미지를 다시 렌더링한다.
                                closeUPCount--;
                                DrawImage();
                            }
                  
                        }
                    }
                }
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }              
        }   

        // 게임 결과 화면 출력
        ViewGameResult();
    }

    private void ViewGameResult()
    {
        tf.setVisible(false);
        resultLabel.setVisible(true);
        resultScoreLabel.setText(currentScore + "");
        resultScoreLabel.setVisible(true);
    }

    JLabel successLabel;
    private void ViewSuccessPanel()
    {
        successLabel = new JLabel();
        successLabel.setIcon(uiItems.get(2).image);
        successLabel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        successLabel.setLayout(new FlowLayout());
        add(successLabel);

        successLabel.setVisible(false);
    }
}

// label.setHorizontalAlignment(JLabel.CENTER);