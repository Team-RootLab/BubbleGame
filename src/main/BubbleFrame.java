package main;

import lombok.Getter;
import lombok.Setter;
import main.component.Enemy;
import main.component.GameOver;
import main.component.Player;
import main.music.BGM;
import main.state.EnemyDirection;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BubbleFrame extends JFrame {
	private BubbleFrame mContext = this;
	private JLabel backgroundMap;
	private Player player;
	private List<Enemy> enemyList;
	private BGM bgm;

	public BubbleFrame() {
		initObject();
		initSetting();
		initListener();
		setVisible(true); // 패널에 그림을 그릴수 있게됨
	}

	private void initListener() {
		addKeyListener(new KeyAdapter() {

			// 1. 그림의 변경 시점: 이벤트 루프의 모든 임무가 완료되어야 repaint가 된다.
			// 2. 메인스레드만 존재하는 경우 이벤트 루프로의 key의 전달은 동시에 이루어질 수 없다.
			// 두개의 키를 동시에 입력시 둘 중 하나만 전달할 수 있다.

			// 키보드 입력 이밴트 핸들러
			@Override
			public void keyPressed(KeyEvent e) {
//				System.out.println(e.getKeyCode());
				switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						if (!player.isLeft() && !player.isLeftWallCrash() && player.getState() == 0) {
							player.left();
						}
						break;
					case KeyEvent.VK_RIGHT:
						if (!player.isRight() && !player.isRightWallCrash() && player.getState() == 0) {
							player.right();
						}
						break;
					case KeyEvent.VK_UP:
						if (!player.isUp() && !player.isDown() && player.getState() == 0) {
							player.up();
						}
						break;
					case KeyEvent.VK_SPACE:
						if (player.getState() == 0) {
							player.attack();
						}
						break;
				}
			}

			// 키보드 해제 이벤트 핸들러
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						player.setLeft(false);
						break;
					case KeyEvent.VK_RIGHT:
						player.setRight(false);
						break;
				}
			}
		});
	}

	private void initObject() {
		backgroundMap = new JLabel(new ImageIcon("image/backgroundMap.png"));
//		backgroundMap = new JLabel(new ImageIcon("image/backgroundMapService.png"));
//		backgroundMap = new JLabel(new ImageIcon("image/test.png"));
		setContentPane(backgroundMap); // JPanel 자체를 JLabel로 변경
		player = new Player(mContext);
		add(player);
		enemyList = new ArrayList<Enemy>();
		enemyList.add(new Enemy(mContext, EnemyDirection.RIGHT));
		enemyList.add(new Enemy(mContext, EnemyDirection.LEFT));
		for (Enemy e : enemyList) add(e);
		bgm = new BGM();
	}

	private void initSetting() {
		setSize(1000, 640); // 창 크기 설정
		setLayout(null); // absolute layout으로 자유롭게 그림을 그릴수 있다.
		setLocationRelativeTo(null); // 창이 우측 상단에서 생기지 않도록 함.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 창을 닫으면 JVM도 같이 종료
	}

	public void endGame(){
		bgm.stopBGM();
		add(new GameOver());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		new BubbleFrame();
	}
}
