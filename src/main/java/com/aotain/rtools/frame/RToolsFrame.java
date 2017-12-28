package com.aotain.rtools.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.aotain.rtools.common.JComboPairModel;
import com.aotain.rtools.common.Pair;
import com.aotain.rtools.common.RedisUtils;
import com.aotain.rtools.model.RedisConfig;
import com.aotain.rtools.utils.FrameUtils;

/**
 * 工具界面
 * 
 * @author liuz@aotian.com
 * @date 2017年12月28日 下午1:35:50
 */
public class RToolsFrame extends JFrame {
	private static final long serialVersionUID = 4012051726802344427L;

	private static final int D_SIZE_WIDTH = 1200;
	private static final int D_SIZE_HEIGHT = 650;
	private static final int D_SIZE_OP_PANEL = 70;
	private static final int D_SIZE_JL_WIDTH = 80;
	private static final int D_SIZE_JTF_WIDTH = 140;
	private static final int D_SIZE_KEY_WIDTH = 250;
	private static final int D_SIZE_LINE_HEIGHT = 28;

	// 集群选择框
	private JComboBox<Pair<Integer, String>> clusterSelect = null;

	private List<RedisConfig> rlist = null;

	private RedisUtils rutils = null;

	private JTextArea jtaResult = null;

	public RToolsFrame() {
		super();
		setTitle("RTools v1.0");
		setSize(new Dimension(D_SIZE_WIDTH, D_SIZE_HEIGHT));
		setLocation(FrameUtils.calcLoaction(getSize(), FrameUtils.getScreenDimension()));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 退出事件关闭进程
		initFrame();
		loadRedisConfig();
	}

	private void loadRedisConfig() {
		rlist = new ArrayList<RedisConfig>();
		String host1 = "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		String port1 = "7000,7000,7000,7000,7000,7000";
		RedisConfig rc1 = new RedisConfig(host1, port1);
		rc1.setId(1);
		rc1.setName("研发集群");
		String host2 = "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		String port2 = "8000,8000,8000,8000,8000,8000";
		RedisConfig rc2 = new RedisConfig(host2, port2);
		rc2.setId(2);
		rc2.setName("测试集群");
		rlist.add(rc1);
		rlist.add(rc2);
		rutils = new RedisUtils(rc1.getIpStrs(), rc1.getPortStrs());
	}

	private void initFrame() {
		// 1. 添加主界面
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel);

		// 2. 集群管理
		JPanel opPanel = new JPanel();
		mainPanel.add(opPanel, BorderLayout.NORTH);
		initOpPanel(opPanel);

		// 3. Key选择工具
		JPanel keysSelectPanel = new JPanel();
		keysSelectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		keysSelectPanel.setPreferredSize(new Dimension(D_SIZE_KEY_WIDTH, D_SIZE_LINE_HEIGHT));
		mainPanel.add(keysSelectPanel, BorderLayout.WEST);

		JLabel jlKeyFilter = createLabel("Key过滤：", 0);
		keysSelectPanel.add(jlKeyFilter);
		JTextField jtfKeyInput = new JTextField();
		jtfKeyInput.setPreferredSize(new Dimension(D_SIZE_JTF_WIDTH, D_SIZE_LINE_HEIGHT));
		keysSelectPanel.add(jtfKeyInput);
		Document document = jtfKeyInput.getDocument();
		document.addDocumentListener(new KeyFilterListener());

		// 4. 添加结果栏
		JPanel rsPanel = new JPanel();
		rsPanel.setLayout(new BorderLayout());
		mainPanel.add(rsPanel, BorderLayout.CENTER);
		initRsPanel(rsPanel);
	}

	/**
	 * 包括集群选择，key选择
	 * 
	 * @param panel
	 */
	private void initOpPanel(JPanel panel) {
		BoxLayout bLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(bLayout);
		panel.setPreferredSize(new Dimension(D_SIZE_WIDTH, D_SIZE_OP_PANEL));

		// 第一行，放置集群选择工具
		JPanel clusterSelectPanel = new JPanel();
		clusterSelectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		clusterSelectPanel.setBorder(BorderFactory.createTitledBorder("集群选择与管理"));
		panel.add(clusterSelectPanel);

		clusterSelect = new JComboBox<Pair<Integer, String>>();
		resetClusterSelect(clusterSelect);
		JLabel jlSelectCluster = createLabel("Redis集群：", 0);
		clusterSelectPanel.add(jlSelectCluster);
		clusterSelectPanel.add(clusterSelect);
		JButton jbClusterMgr = new JButton();
		jbClusterMgr.setText("集群管理");
		clusterSelectPanel.add(jbClusterMgr);
	}

	private JLabel createLabel(String text, int width) {
		JLabel jlKeyFilter = new JLabel(text);
		if (width > 0) {
			jlKeyFilter.setPreferredSize(new Dimension(width, D_SIZE_LINE_HEIGHT));
		} else {
			jlKeyFilter.setPreferredSize(new Dimension(D_SIZE_JL_WIDTH, D_SIZE_LINE_HEIGHT));
		}
		jlKeyFilter.setHorizontalAlignment(JLabel.RIGHT);
		return jlKeyFilter;
	}

	// 重置下拉框
	private void resetClusterSelect(JComboBox<Pair<Integer, String>> clusterSelect) {
		List<Pair<Integer, String>> clusterList = new ArrayList<Pair<Integer, String>>();
		clusterList.add(new Pair<Integer, String>(1, "研发集群"));
		clusterList.add(new Pair<Integer, String>(2, "测试集群"));
		clusterSelect.setModel(new JComboPairModel<>(clusterList));
		clusterSelect.setSelectedItem(clusterList.get(0));
	}

	private void initRsPanel(JPanel rsPanel) {
		jtaResult = new JTextArea();
		jtaResult.setEditable(false);
		rsPanel.add(jtaResult);
	}

	public static void main(String[] args) {
		new RToolsFrame().setVisible(true);
	}
	
	private class KeyFilterListener implements DocumentListener{

		@Override
		public void insertUpdate(DocumentEvent e) {
			changedUpdate(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			changedUpdate(e);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			Document doc = e.getDocument();
			String text = null;
			try {
				text = doc.getText(doc.getStartPosition().getOffset(), doc.getLength());
				List<String> keys = rutils.getKeys(text);
				jtaResult.setText(null);
				for(String key : keys) {
					jtaResult.append(key+"\r\n");
				}
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}
}
