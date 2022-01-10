package main;

import javax.swing.*;

public class BubbleFrame extends JFrame {
	private JLabel backgroundMap;
	private Player player;

	public BubbleFrame() {
		initObject();
		initSetting();
		setVisible(true); // 패널에 그림을 그릴수 있게됨
	}

	private void initObject(){
		backgroundMap = new JLabel(new ImageIcon("image/backgroundMap.png"));
		setContentPane(backgroundMap); // JPanel 자체를 JLabel로 변경

		player = new Player();
		add(player);

//		backgroundMap.setSize(1000, 600);
//		backgroundMap.setLocation(300, 300);
//		add(backgroundMap); // JFrame에 JLabel을 그린다.
	}

	private void initSetting(){
		setSize(1000, 640); // 창 크기 설정
		setLayout(null); // absolute layout으로 자유롭게 그림을 그릴수 있다.
		setLocationRelativeTo(null); // 창이 우측 상단에서 생기지 않도록 함.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 창을 닫으면 JVM도 같이 종료
	}

	public static void main(String[] args) {
		new BubbleFrame();
	}
}