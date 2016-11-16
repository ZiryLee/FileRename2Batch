package me.ziry.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import me.ziry.util.AlertPhotoUtil;

import org.jvnet.substance.skin.SubstanceAutumnLookAndFeel;

public class IndexUi extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String VERSION = "V1.0";

	JFileChooser jFileChooser = new JFileChooser();

	JLabel welcome_JL = new JLabel("欢迎使用  《Ziry作品：批量修改图片名为拍摄日期》");
	JLabel welcome_JL2 = new JLabel("请选择文件夹目录，程序会将目录及子目录下的所有图片文件重命名为拍摄日期。");
	JLabel welcome_JL3 = new JLabel("使用中如果发现有问题欢迎邮箱反馈：lee@ziry.me");
	JPanel welcome_JP = new JPanel();

	// 选择按钮
	JButton chooser_JB = new JButton("选择目录");

	// 设置用户界面居中
	public static final int WIDTH = 500; // 界面宽度
	public static final int HEIGHT = 300; // 界面高度
	private Toolkit tk = Toolkit.getDefaultToolkit();
	private Dimension d = tk.getScreenSize();
	private int y = d.height / 2 - HEIGHT / 2; // 居中坐标X
	private int x = d.width / 2 - WIDTH / 2; // 居中坐标y

	public IndexUi() {
		this.setTitle("Ziry作品：批量修改图片名为拍摄日期" + VERSION); // 界面标题
		this.setIconImage(new ImageIcon("http://ziry.me/favicon.ico")
				.getImage());
		this.setBounds(x, y, WIDTH, HEIGHT); // 设置界面位置及大小，这里使用居中位置x,y
		this.setLayout(new GridLayout(2, 1)); // 设置界面布局为边界布局，这里只使用NORTH、CENTER、SOUTH

		jFileChooser.setDialogTitle("请选择目录"); // 设置文件选择器标题
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jFileChooser.setMultiSelectionEnabled(false);
		welcome_JP.setLayout(new GridLayout(3, 1, 100, 0));
		welcome_JP.add(welcome_JL);
		welcome_JP.add(welcome_JL2);
		welcome_JP.add(welcome_JL3);
		this.add(welcome_JP);

		this.add(chooser_JB);

		// 设置按钮监听
		chooser_JB.addActionListener(this);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); // 设置屏幕关闭事件
		this.setVisible(true); // 显示界面
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		chooser_JB.setText("请稍后，正在处理中...");
		chooser_JB.setEnabled(false);
		
		// 选择目录
		if (e.getSource() == chooser_JB) {
			
			int returnVal = jFileChooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File imgFile = jFileChooser.getSelectedFile();
				AlertPhotoUtil main = new AlertPhotoUtil();
				main.start(imgFile);
				String massage = "操作成功! 一共有:" + main.sum +"张, 其中修改了:"+main.alterCount+"张, 错误："+main.errorCount+"张, 错误信息："+main.errorNameList;
				JOptionPane.showMessageDialog(this, massage,
						"提示", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		chooser_JB.setText("选择目录");
		chooser_JB.setEnabled(true);
	}

	public static void main(String[] args) {
		// 使标题栏的风格也跟着一起改变
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		// 设置皮肤
		try {
			UIManager.setLookAndFeel(new SubstanceAutumnLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new IndexUi();
	}

}
