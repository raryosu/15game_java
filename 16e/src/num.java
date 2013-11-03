import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;


public class num extends Applet implements MouseListener
{
	private final int GAME_WAIT = 0;	//ゲーム状態フラグ定数（タイトル画面時）
	private final int GAME_ING = 1;		//ゲーム状態フラグ定数（ゲーム中）
	private final int GRID_X = 4;		//ボードの横マス数
	private final int GRID_Y = 4;		//ボードの縦マス数
	private final int GRID_WIDTH = 100;	//マスの横幅
	private final int GRID_HEIGHT = 100;	//マスの縦幅
	private int WIDTH;			//アプレット画面横幅
	private int HEIGHT;			//アプレット画面縦幅
	private int gameFlg;			//ゲーム状態フラグ
	private GridInfo GInfo;			//グリッドクラス
	private Image offImage;			//画面バッファ
	private Image tileImage[];		//コマイメージ（1～15）
	
	public void init(){
		// WIDTH = getSize().width;
		// HEIGHT = getSize().height;
		WIDTH = 400;
		HEIGHT = 400;
		setBackground(Color.white);
		tileImage = new Image[GRID_X * GRID_Y + 1];
		DecimalFormat decimalFormat = new DecimalFormat("00");
		for(int i = 1; i < GRID_X * GRID_Y; i++){
			tileImage[i] = getImage(getDocumentBase(),
							 "images/" + decimalFormat.format(i) + ".png");
			
		}
		offImage = createImage(WIDTH, HEIGHT);
	}
	public void start(){
		addMouseListener(this);
		GInfo = new GridInfo(GRID_X, GRID_Y);
	}
	public void gameInit(){
		GInfo.shfleTile();
		gameFlg = GAME_ING;
	}
	public void mouseClicked(MouseEvent e){

	}
	public void mousePressed(MouseEvent e){
		int clickTileX;
		int clickTileY;
		boolean blnRet;
		
		switch(gameFlg){
			case GAME_WAIT:
				gameInit();
				break;
			case GAME_ING:
				/* クリック座標からクリックされたコマが置いてあるマスを取得 */
				clickTileX = (int)(e.getX() / GRID_WIDTH);
				clickTileY = (int)(e.getY() / GRID_HEIGHT);
				/*コマを移動させる。移動できない場合は何もしない */
				blnRet = GInfo.moveTile(clickTileX, clickTileY);
				/* コマが整列した場合はゲーム終了 */
				blnRet = GInfo.getGameClearFlg();
				if(blnRet == true){
					/* ゲーム状態フラグを変更 */
					gameFlg = GAME_WAIT;
				}
				break;
		}
		/*描画 */
		repaint();
	}
	public void mouseReleased(MouseEvent e){
	}
	public void mouseExited(MouseEvent e){
	}
	public void mouseEntered(MouseEvent e){
	}
  	public void update(Graphics g){
  		paint(g);
  	}
	public void paint(Graphics g){
		Graphics gv = offImage.getGraphics();
		gv.clearRect(0, 0, WIDTH, HEIGHT);
		
		for(int y = 0; y < GRID_Y; y++){
			for(int x = 0; x < GRID_X; x++){
				if(GInfo.getTileNum(x, y) != 0){
					gv.drawImage(tileImage[GInfo.getTileNum(x, y)],
					 x * GRID_WIDTH, y * GRID_HEIGHT, GRID_WIDTH, GRID_HEIGHT, this);
				}
			}
		}
		switch(gameFlg){
			case GAME_WAIT:
				gv.drawString("Game Over", 100, 100);
				gv.drawString("Click Start", 130, 130);
				break;
			case GAME_ING:
				break;
		}
		g.drawImage(offImage, 0, 0, WIDTH, HEIGHT, this);
	}
}
class GridInfo
{
	private int intGridXNum;	//マスの横数
	private int intGridYNum;	//マスの縦数
	private int intGridFlg[][];	//マス情報
	
	/* コンストラクタ */
	GridInfo(int xNum, int yNum){
		/* 引数から渡されたマスの横幅、縦幅を保存 */
		intGridXNum = xNum;
		intGridYNum = yNum;
		
		/* 各マスに置かれているコマを保持する2次元配列を定義 */
		intGridFlg = new int[intGridXNum][intGridYNum];
		
		/* 1～15までの数を格納 */
		for(int y = 0; y < intGridYNum; y++){
			for(int x = 0; x < intGridXNum; x++){
				intGridFlg[x][y] = (y * intGridYNum) + x + 1;
			}
		}
		/* 右下のマスにはコマがないことを意味する0を格納 */
		intGridFlg[intGridXNum - 1][intGridYNum - 1] = 0;
	}
	public void shfleTile(){
		int intClickX = 0;
		int intClickY = 0;
		int intClickedX = 0;
		int intClickedY = 0;
		int randNum;
		boolean blnRet = false;
		
		/* 500回コマを移動させる */
		for(int i = 0; i < 500; i++){
			/* コマが置かれていない空いているマスを取得 */
			intClickX = getEmpGridXNum();	//横位置取得
			intClickY = getEmpGridYNum();	//縦位置取得
			
			/* 0～3までの数値をランダムに取得 */
			randNum = (int)(Math.random() * 4);
			
			/* ランダムに得た0～3までの数値を上下左右に対応させて場合分け */
			
			switch(randNum){
				/* 右 */
				case 0:
					/* 空いているマスの右にあるコマを移動させる */
					blnRet = moveTile(intClickX + 1, intClickY);
					intClickedX = intClickX + 1;
					intClickedY = intClickY;
					break;
				
				/* 左 */
				case 1:
					/* 空いているマスの左にあるコマを移動させる */
					blnRet = moveTile(intClickX - 1, intClickY);
					intClickedX = intClickX - 1;
					intClickedY = intClickY;
					break;
				
				/* 下 */
				case 2:
					/* 空いているマスの下にあるコマを移動させる */
					blnRet = moveTile(intClickX, intClickY + 1);
					intClickedX = intClickX;
					intClickedY = intClickY + 1;
					break;
				
				/* 上 */
				case 3:
					/* 空いているマスの上にあるコマを移動させる */
					blnRet = moveTile(intClickX, intClickY - 1);
					intClickedX = intClickX;
					intClickedY = intClickY - 1;
					break;
			}
			if(blnRet == true){
				intClickX = intClickedX;
				intClickY = intClickedY;
				moveTile(intClickX, intClickY);
			}
		}
	}
	public int getEmpGridXNum(){
		int rx = 0;
		for(int y = 0; y < intGridYNum; y++){
			for(int x = 0; x < intGridXNum; x++){
				if(intGridFlg[x][y] == 0){
					rx = x;
				}
			}
		}
		return rx;	//コマが置かれていない空いているX座標（論理値）を返す
	}
	public int getEmpGridYNum(){
		int ry = 0;
		for(int y = 0; y < intGridYNum; y++){
			for(int x = 0; x < intGridXNum; x++){
				if(intGridFlg[x][y] == 0){
					ry = y;
				}
			}
		}
		return ry;	//コマが置かれていない空いているY座標（論理値）を返す
	}
	public boolean moveTile(int clickX, int clickY){
		boolean blnRet;
		boolean blnExist;
		blnRet = true;
		blnExist = false;
		while(true){
			/* 右に移動できるか判別 */
			if(clickX + 1 < intGridXNum && clickX >= 0 
								&& clickY >= 0 && clickY < intGridYNum){
				if(intGridFlg[clickX + 1][clickY] == 0){
					intGridFlg[clickX + 1][clickY] = intGridFlg[clickX][clickY];
					blnExist = true;
					break;
				}
			}
			/* 左に移動できるか判別 */
			if(clickX - 1 >= 0 && clickX < intGridXNum 
								&& clickY >= 0 && clickY < intGridYNum){
				if(intGridFlg[clickX - 1][clickY] == 0){
					intGridFlg[clickX - 1][clickY] = intGridFlg[clickX][clickY];
					blnExist = true;
					break;
				}
			}
			/* 下に移動できるか判別 */
			if(clickY + 1 < intGridYNum && clickY >= 0 
								&& clickX >= 0 && clickX < intGridXNum){
				if(intGridFlg[clickX][clickY + 1] == 0){
					intGridFlg[clickX][clickY + 1] = intGridFlg[clickX][clickY];
					blnExist = true;
					break;
				}
			}
			/* 上に移動できるか判別 */
			if(clickY - 1 >= 0 && clickY < intGridYNum 
								&& clickX >= 0 && clickX < intGridXNum){
				if(intGridFlg[clickX][clickY - 1] == 0){
					intGridFlg[clickX][clickY - 1] = intGridFlg[clickX][clickY];
					blnExist = true;
					break;
				}
			}
			break;
		}
		/* クリックされたマスからコマを移動させたら、そのマスを空にする */
		if(blnExist == true){
			intGridFlg[clickX][clickY] = 0;
		}
		return blnRet;
	}
	public int getTileNum(int x, int y){
		return intGridFlg[x][y];
	}
	public boolean getGameClearFlg(){
		boolean blnRet;
		blnRet = true;
		
		for(int y = 0; y < intGridYNum; y++){
			for(int x = 0; x < intGridXNum; x++){
				/* 右下のマスが空いているかどうかを判断 */
				if(y == intGridYNum - 1 && x == intGridXNum - 1){
					if(intGridFlg[x][y] != 0){
						blnRet = false;	//右下のマスが空いていなかったらfalseを代入
	
					}
				}
				/* 数字が整列されているかどうかを判断 */
				else if(intGridFlg[x][y] != (y * intGridYNum) + x + 1){
					blnRet = false;	//数字が整列されていなかったらfalseを代入
				}
			}
		}
	
		return blnRet;
	}
}