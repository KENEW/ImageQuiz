public class GameManager
{
    private String userName;
    
    public static GameManager Instance;
    private static InGameFrame inGameFrame;


    public static void main(String[] args) 
	{
        inGameFrame = new InGameFrame();
        inGameFrame.GameStart(5, 200);
	}
}