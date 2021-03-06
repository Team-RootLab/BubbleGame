package main.component;

import lombok.Getter;
import lombok.Setter;
import main.service.BackgroundBubbleService;
import main.BubbleFrame;
import main.Moveable;

import javax.swing.*;
import java.util.List;

@Getter
@Setter
public class Bubble extends JLabel implements Moveable {

	private BubbleFrame mContext;
	private Player player;
	private List<Enemy> enemyList;
	private Enemy target = null; // 제거할 적을 저장
	private BackgroundBubbleService bubbleService;

	// 위치 상태
	private int x;
	private int y;

	// 동작 상태
	private boolean up;
	private boolean left;
	private boolean right;

	// 적을 맞춘 상태
	private int state; // 0(물방울), 1(적을 가둔 물방울)

	private ImageIcon bubble; // 물방울
	private ImageIcon bubbled; // 적을 가둔 물방울
	private ImageIcon bomb; // 물방울이 터진 상태

	public Bubble(BubbleFrame mContext) {
		this.mContext = mContext;
		this.player = mContext.getPlayer();
		this.enemyList = mContext.getEnemyList();
		initObject();
		initSetting();
	}

	private void initSetting() {
		left = false;
		right = false;
		up = false;
		state = 0;

		this.x = player.getX();
		this.y = player.getY();

		setIcon(bubble);
		setSize(50, 50);
	}

	private void initObject() {
		bubble = new ImageIcon("image/bubble.png");
		bubbled = new ImageIcon("image/bubbled.png");
		bomb = new ImageIcon("image/bomb.png");
		bubbleService = new BackgroundBubbleService(this);
	}


	@Override
	public void left() {
		left = true;
		Stop:for (int i = 0; i < 400; i++) {
			x -= 1;
			setLocation(x, y);
			if (bubbleService.leftWall()) {
				left = false;
				break;
			}

			for (Enemy enemy : enemyList) {
				if ((Math.abs(x - enemy.getX()) < 10)
						&& (Math.abs(y - enemy.getY()) >= 0 && Math.abs(y - enemy.getY()) < 50)
				) {
//				System.out.println("물방울이 적과 충돌");
					if (enemy.getState() == 0) {
						attack(enemy);
						break Stop;
					}
				}
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		up();
	}

	@Override
	public void right() {
		right = true;
		Stop:for (int i = 0; i < 400; i++) {
			x += 1;
			setLocation(x, y);
			if (bubbleService.rightWall()) {
				right = false;
				break;
			}

			for (Enemy enemy : enemyList) {
				if ((Math.abs(x - enemy.getX()) < 10)
						&& (Math.abs(y - enemy.getY()) >= 0 && Math.abs(y - enemy.getY()) < 50)
				) {
//				System.out.println("물방울이 적과 충돌");
					if (enemy.getState() == 0) {
						attack(enemy);
						break Stop;
					}
				}
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		up();
	}

	@Override
	public void up() {
		up = true;
		while (up) {
			y--;
			setLocation(x, y);
			if (bubbleService.topWall()) {
				up = false;
				break;
			}
			try {
				if (state == 0) {
					Thread.sleep(1);
				} else {
					Thread.sleep(10);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (state == 0) clearBubble(); // 천장에 물방울이 도착하고 나서 3초후 메모리에서 소멸
	}

	@Override
	public void attack(Enemy enemy) {
		state = 1;
		enemy.setState(1);
		setIcon(bubbled);
		target = enemy;
		mContext.remove(enemy); // GC가 즉시 동작하지 않는다.
		mContext.repaint();
	}

	private void clearBubble() {
		try {
			Thread.sleep(3000);
			setIcon(bomb);
			Thread.sleep(500);
			mContext.getPlayer().getBubbles().remove(this);
			mContext.remove(this); // BubbleFrame의 bubble이 메모리에서 소멸된다.
			mContext.repaint(); // BubbleFrame을 다시 그린다. 메모리에 없는 객체는 그리지 않는다.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void clearBubbled() {
		new Thread(() -> {
			try {
				up = false;
				setIcon(bomb);
				Thread.sleep(1000);
				mContext.getPlayer().getBubbles().remove(this);
				mContext.getEnemyList().remove(target);
				mContext.remove(this); // BubbleFrame의 bubble이 메모리에서 소멸된다.
				mContext.repaint(); // BubbleFrame을 다시 그린다. 메모리에 없는 객체는 그리지 않는다.
				if(mContext.getEnemyList().size() == 0){
					mContext.endGame();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
}
