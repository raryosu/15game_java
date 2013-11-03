import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;


public class num extends Applet implements MouseListener
{
	private final int GAME_WAIT = 0;	//�Q�[����ԃt���O�萔�i�^�C�g����ʎ��j
	private final int GAME_ING = 1;		//�Q�[����ԃt���O�萔�i�Q�[�����j
	private final int GRID_X = 4;		//�{�[�h�̉��}�X��
	private final int GRID_Y = 4;		//�{�[�h�̏c�}�X��
	private final int GRID_WIDTH = 100;	//�}�X�̉���
	private final int GRID_HEIGHT = 100;	//�}�X�̏c��
	private int WIDTH;			//�A�v���b�g��ʉ���
	private int HEIGHT;			//�A�v���b�g��ʏc��
	private int gameFlg;			//�Q�[����ԃt���O
	private GridInfo GInfo;			//�O���b�h�N���X
	private Image offImage;			//��ʃo�b�t�@
	private Image tileImage[];		//�R�}�C���[�W�i1�`15�j
	
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
				/* �N���b�N���W����N���b�N���ꂽ�R�}���u���Ă���}�X���擾 */
				clickTileX = (int)(e.getX() / GRID_WIDTH);
				clickTileY = (int)(e.getY() / GRID_HEIGHT);
				/*�R�}���ړ�������B�ړ��ł��Ȃ��ꍇ�͉������Ȃ� */
				blnRet = GInfo.moveTile(clickTileX, clickTileY);
				/* �R�}�����񂵂��ꍇ�̓Q�[���I�� */
				blnRet = GInfo.getGameClearFlg();
				if(blnRet == true){
					/* �Q�[����ԃt���O��ύX */
					gameFlg = GAME_WAIT;
				}
				break;
		}
		/*�`�� */
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
	private int intGridXNum;	//�}�X�̉���
	private int intGridYNum;	//�}�X�̏c��
	private int intGridFlg[][];	//�}�X���
	
	/* �R���X�g���N�^ */
	GridInfo(int xNum, int yNum){
		/* ��������n���ꂽ�}�X�̉����A�c����ۑ� */
		intGridXNum = xNum;
		intGridYNum = yNum;
		
		/* �e�}�X�ɒu����Ă���R�}��ێ�����2�����z����` */
		intGridFlg = new int[intGridXNum][intGridYNum];
		
		/* 1�`15�܂ł̐����i�[ */
		for(int y = 0; y < intGridYNum; y++){
			for(int x = 0; x < intGridXNum; x++){
				intGridFlg[x][y] = (y * intGridYNum) + x + 1;
			}
		}
		/* �E���̃}�X�ɂ̓R�}���Ȃ����Ƃ��Ӗ�����0���i�[ */
		intGridFlg[intGridXNum - 1][intGridYNum - 1] = 0;
	}
	public void shfleTile(){
		int intClickX = 0;
		int intClickY = 0;
		int intClickedX = 0;
		int intClickedY = 0;
		int randNum;
		boolean blnRet = false;
		
		/* 500��R�}���ړ������� */
		for(int i = 0; i < 500; i++){
			/* �R�}���u����Ă��Ȃ��󂢂Ă���}�X���擾 */
			intClickX = getEmpGridXNum();	//���ʒu�擾
			intClickY = getEmpGridYNum();	//�c�ʒu�擾
			
			/* 0�`3�܂ł̐��l�������_���Ɏ擾 */
			randNum = (int)(Math.random() * 4);
			
			/* �����_���ɓ���0�`3�܂ł̐��l���㉺���E�ɑΉ������ďꍇ���� */
			
			switch(randNum){
				/* �E */
				case 0:
					/* �󂢂Ă���}�X�̉E�ɂ���R�}���ړ������� */
					blnRet = moveTile(intClickX + 1, intClickY);
					intClickedX = intClickX + 1;
					intClickedY = intClickY;
					break;
				
				/* �� */
				case 1:
					/* �󂢂Ă���}�X�̍��ɂ���R�}���ړ������� */
					blnRet = moveTile(intClickX - 1, intClickY);
					intClickedX = intClickX - 1;
					intClickedY = intClickY;
					break;
				
				/* �� */
				case 2:
					/* �󂢂Ă���}�X�̉��ɂ���R�}���ړ������� */
					blnRet = moveTile(intClickX, intClickY + 1);
					intClickedX = intClickX;
					intClickedY = intClickY + 1;
					break;
				
				/* �� */
				case 3:
					/* �󂢂Ă���}�X�̏�ɂ���R�}���ړ������� */
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
		return rx;	//�R�}���u����Ă��Ȃ��󂢂Ă���X���W�i�_���l�j��Ԃ�
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
		return ry;	//�R�}���u����Ă��Ȃ��󂢂Ă���Y���W�i�_���l�j��Ԃ�
	}
	public boolean moveTile(int clickX, int clickY){
		boolean blnRet;
		boolean blnExist;
		blnRet = true;
		blnExist = false;
		while(true){
			/* �E�Ɉړ��ł��邩���� */
			if(clickX + 1 < intGridXNum && clickX >= 0 
								&& clickY >= 0 && clickY < intGridYNum){
				if(intGridFlg[clickX + 1][clickY] == 0){
					intGridFlg[clickX + 1][clickY] = intGridFlg[clickX][clickY];
					blnExist = true;
					break;
				}
			}
			/* ���Ɉړ��ł��邩���� */
			if(clickX - 1 >= 0 && clickX < intGridXNum 
								&& clickY >= 0 && clickY < intGridYNum){
				if(intGridFlg[clickX - 1][clickY] == 0){
					intGridFlg[clickX - 1][clickY] = intGridFlg[clickX][clickY];
					blnExist = true;
					break;
				}
			}
			/* ���Ɉړ��ł��邩���� */
			if(clickY + 1 < intGridYNum && clickY >= 0 
								&& clickX >= 0 && clickX < intGridXNum){
				if(intGridFlg[clickX][clickY + 1] == 0){
					intGridFlg[clickX][clickY + 1] = intGridFlg[clickX][clickY];
					blnExist = true;
					break;
				}
			}
			/* ��Ɉړ��ł��邩���� */
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
		/* �N���b�N���ꂽ�}�X����R�}���ړ���������A���̃}�X����ɂ��� */
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
				/* �E���̃}�X���󂢂Ă��邩�ǂ����𔻒f */
				if(y == intGridYNum - 1 && x == intGridXNum - 1){
					if(intGridFlg[x][y] != 0){
						blnRet = false;	//�E���̃}�X���󂢂Ă��Ȃ�������false����
	
					}
				}
				/* ���������񂳂�Ă��邩�ǂ����𔻒f */
				else if(intGridFlg[x][y] != (y * intGridYNum) + x + 1){
					blnRet = false;	//���������񂳂�Ă��Ȃ�������false����
				}
			}
		}
	
		return blnRet;
	}
}