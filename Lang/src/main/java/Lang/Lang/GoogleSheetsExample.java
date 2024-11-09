package Lang.Lang;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

class WordInfo {
	private String word;
	private String meaning;

	public WordInfo(String word, String meaning) {
		this.word = word;
		this.meaning = meaning;
	}

	public String getWord() {
		return word;
	}

	public String getMeaning() {
		return meaning;
	}

}

public class GoogleSheetsExample extends JFrame {
	public String newlang = "";
	private JPanel contentPane;
	public List<String> strValuesEN = new ArrayList<>();
	public List<String> strValuesEN_N = new ArrayList<>();
	public List<String> strValuesKR = new ArrayList<>();
	public List<String> strValuesKR_N = new ArrayList<>();
	public List<String> strValuesJP = new ArrayList<>();
	public List<String> strValuesJP_N = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		Map<Character, List<WordInfo>> categorizedMap = new HashMap<Character, List<WordInfo>>();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GoogleSheetsExample frame = new GoogleSheetsExample(categorizedMap);
					frame.setTitle("Title");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	// 建立Google Sheets服務
	private static Sheets getSheetsService() throws Exception {
		return new Sheets.Builder(
				GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(),
				getCredentials())
				.setApplicationName("Your Application Name")
				.build();
	}

	// 獲取憑證
	private static Credential getCredentials() throws Exception {
		return GoogleCredential.fromStream(
				new FileInputStream("./credentials.json"))
				.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
	}

	public GoogleSheetsExample(Map<Character, List<WordInfo>> categorizedMap) throws Exception {
		String languages[] = { "# 英文", "# 韓文", "# 日文" };

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0,0,550, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JList list = new JList(languages);	
		list.setBackground(getBackground());

		panel(contentPane,list);

		// 指定試算表ID和工作表名稱
		String spreadsheetId = "";
		// 指定文件路径
		String filePath = "excelID.txt";

		// 檢查文件是否存在
		Path path = Paths.get(filePath);

		if (Files.exists(path)) {
			// 文件存在，讀取文件内容
			try {
				List<String> lines = Files.readAllLines(path);
				for (String line : lines) {
					if(!line.isEmpty() && line !="")
						spreadsheetId = line;
				}
			} catch (IOException e) {
				System.out.println("讀取文件時發生錯誤：" + e.getMessage());
			}
		} else {
			// 文件不存在則新增文件
			System.out.println("文件不存在，正在新增");

			try {
				Files.createFile(path);
				System.out.println("文件已新增：" + filePath);
			} catch (IOException ex) {
				System.out.println("新增錯誤：" + ex.getMessage());
			}
		}

		Sheets sheetsService = getSheetsService();
		for(String sheetName : languages) {
			if (!isSheetExist(sheetsService, spreadsheetId, sheetName.replace("# ", ""))) {
				createSheet(sheetsService, spreadsheetId, sheetName.replace("# ", ""));
			}
		}


		DefaultListModel<String> listModel_name = new DefaultListModel<>();
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
				int index = theList.locationToIndex(mouseEvent.getPoint());
				if (index >= 0) {
					Object o = theList.getModel().getElementAt(index);

					if(o.toString().contains("回首頁")) {
						DefaultListModel<String> listModel = new DefaultListModel<>();
						list.setModel(new DefaultListModel<>());
						for (String line : languages) {
							listModel.addElement(line);
						}
						list.setModel(listModel);
					}
					else {
						// 檢查文件是否存在
						if(o.toString().contains("# ")) {
							readsheet(o,categorizedMap,list);
							newlang = o.toString();
						}
						else {
							EN(o,newlang,categorizedMap,list,listModel_name,index);	
						}

					}
				}
			}
		};
		list.addMouseListener(mouseListener);
	}

	private static void panel(JPanel contentPane,JList list) {
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(200, 200));


		contentPane.setLayout(new GridBagLayout());

		JComboBox jComboBox = new JComboBox();
		jComboBox.addItem("英文");
		jComboBox.addItem("韓文");
		jComboBox.addItem("日文");

		GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
		scrollPaneConstraints.gridx = 0;
		scrollPaneConstraints.gridy = 0;
		contentPane.add(scrollPane, scrollPaneConstraints);

		JPanel textFieldsPanel = new JPanel();
		textFieldsPanel.setLayout(new GridBagLayout());

		// 添加三個 JLabel 和 JTextField
		JLabel label1 = new JLabel("分類 :");
		JLabel label2 = new JLabel("外文 :");
		JLabel label3 = new JLabel("中文 :");
		JLabel label4 = new JLabel("資料讀取中...");
		label4.setVisible(false);

		JTextField textField2 = new JTextField(15);
		JTextField textField3 = new JTextField(15);

		
		textField2.setPreferredSize(new Dimension(150, 25));
		textField3.setPreferredSize(new Dimension(150, 25));

		
		Insets Insets = new Insets(10, 10, 10, 10);

		// 将 JLabel 和 JTextField 放在 textFieldsPanel 中
		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;
		labelConstraints.insets = Insets;
		textFieldsPanel.add(label1, labelConstraints);

		labelConstraints.gridx = 0;
		labelConstraints.gridy = 1;
		labelConstraints.insets = Insets;
		textFieldsPanel.add(label2, labelConstraints);

		labelConstraints.gridx = 0;
		labelConstraints.gridy = 2;
		labelConstraints.insets = Insets;
		textFieldsPanel.add(label3, labelConstraints);

		GridBagConstraints textFieldConstraints = new GridBagConstraints();
		textFieldConstraints.gridx = 1;
		textFieldConstraints.gridy = 0;
		textFieldConstraints.insets = Insets;
		textFieldsPanel.add(jComboBox, textFieldConstraints);

		textFieldConstraints.gridx = 1;
		textFieldConstraints.gridy = 1;
		textFieldConstraints.insets = Insets;
		textFieldsPanel.add(textField2, textFieldConstraints);

		textFieldConstraints.gridx = 1;
		textFieldConstraints.gridy = 2;
		textFieldConstraints.insets = Insets;
		textFieldsPanel.add(textField3, textFieldConstraints);

		// 將 textFieldsPanel 放在 GridBagLayout 的下一個位置
		GridBagConstraints textFieldsPanelConstraints = new GridBagConstraints();
		textFieldsPanelConstraints.gridx = 0;
		textFieldsPanelConstraints.gridy = 1;
		textFieldsPanelConstraints.insets = Insets;
		contentPane.add(textFieldsPanel, textFieldsPanelConstraints);

		JButton btnconcelButton = new JButton("取消");
		btnconcelButton.setVisible(false);
		btnconcelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textFieldsPanel.isVisible()) {
					scrollPane.setVisible(true);
					textFieldsPanel.setVisible(false);
					btnconcelButton.setVisible(false);
				}
				else {
					scrollPane.setVisible(false);
					textFieldsPanel.setVisible(true);
//					updateSheet(jComboBox.getSelectedItem().toString(),textField2.getText(),textField3.getText());
				}
			}
		});

		JButton btnNewButton = new JButton("新增");

		
		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = 2;
		contentPane.add(btnNewButton, buttonConstraints);

		buttonConstraints.gridx = 1;
		buttonConstraints.gridy = 2;
		buttonConstraints.insets = new Insets(0,-110,0,0);
		contentPane.add(btnconcelButton, buttonConstraints);

		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = 3;
		buttonConstraints.insets = Insets;
		contentPane.add(label4, buttonConstraints);

		
		textFieldsPanel.setVisible(false);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(scrollPane.isVisible()) {
					scrollPane.setVisible(false);
					textFieldsPanel.setVisible(true);	
					btnconcelButton.setVisible(true);
					buttonConstraints.insets = new Insets(0,-20,0,0);
				}
				else {
					scrollPane.setVisible(true);
					textFieldsPanel.setVisible(false);
					updateSheet(jComboBox.getSelectedItem().toString(),textField2.getText(),textField3.getText());
					btnconcelButton.setVisible(false);
					
					String languages[] = { "# 英文", "# 韓文", "# 日文" };
					DefaultListModel<String> listModel = new DefaultListModel<>();
					list.setModel(new DefaultListModel<>());
					for (String line : languages) {
						listModel.addElement(line);
					}
					list.setModel(listModel);
				}
			}
		});	

	}

	private  void readsheet(Object o ,Map<Character, List<WordInfo>> categorizedMap,JList list ) {

		SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
			private String status;

			@Override
			protected Void doInBackground() throws Exception {
				if((o.toString()=="# 英文" && (strValuesEN.isEmpty() || strValuesEN_N.isEmpty())) ||
						(o.toString()=="# 韓文" &&(strValuesKR.isEmpty() || strValuesKR_N.isEmpty())) ||
						(o.toString()=="# 日文" && (strValuesJP.isEmpty() || strValuesJP_N.isEmpty()))		
						) {
					contentPane.getComponent(4).setVisible(true);
				}
				else {
					contentPane.getComponent(4).setVisible(false);	
				}
				return null;
			}

			@Override
			protected void done() {
				if(o.toString()=="# 英文") {
					if(categorizedMap != null) {
						categorizedMap.clear();
					}
					if(strValuesEN.isEmpty() || strValuesEN_N.isEmpty()) {
						try {
							EnSheet(o.toString().replace("# ", ""));
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
					int i=0;
					for (String value : strValuesEN) {
						String emp = "";
						if(!strValuesEN_N.get(i).isEmpty()) {
							emp = strValuesEN_N.get(i).toString();
						}
						else {
							emp = "";
						}
						char firstChar = value.charAt(0);
						addWord(categorizedMap, firstChar, value, emp);
						i++;
					}
				}

				if(o.toString()=="# 韓文") {
					if(categorizedMap != null) {
						categorizedMap.clear();
					}
					if(strValuesKR.isEmpty() || strValuesKR_N.isEmpty()) {
						try {
							EnSheet(o.toString().replace("# ", ""));
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
					int i=0;
					for (String value : strValuesKR) {
						String emp = "";
						if(!strValuesKR_N.get(i).isEmpty()) {
							emp = strValuesKR_N.get(i).toString();
						}
						else {
							emp = "";
						}
						char firstChar = value.charAt(0);
						addWord(categorizedMap, firstChar, value, emp);
						i++;
					}
				}

				if(o.toString() == "# 日文") {
					if(categorizedMap != null) {
						categorizedMap.clear();
					}
					if(strValuesJP.isEmpty() || strValuesJP_N.isEmpty()) {
						try {
							EnSheet(o.toString().replace("# ", ""));

						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
					int i=0;
					for (String value : strValuesJP) {
						String emp = "";
						if(!strValuesJP_N.get(i).isEmpty()) {
							emp = strValuesJP_N.get(i).toString();
						}
						else {
							emp = "";
						}
						char firstChar = value.charAt(0);
						addWord(categorizedMap, firstChar, value, emp);
						i++;
					}
				}
				List<String> fileContent = new ArrayList<>();
				// 顯示分類結果
				for (Map.Entry<Character, List<WordInfo>> entry : categorizedMap.entrySet()) {
					fileContent.add("$ " + entry.getKey().toString());
				}

				DefaultListModel<String> listModel = new DefaultListModel<>();

				// 將文件內容設置給JList
				listModel.addElement("回首頁");
				for (String line : fileContent) {
					listModel.addElement(line);
				}
				list.setModel(listModel);
				contentPane.getComponent(4).setVisible(false);

			}
		};

		worker.execute();



	}

	private void EnSheet(String lang) throws Exception{
		String spreadsheetId = "";
		
		String filePath = "excelID.txt";

		
		Path path = Paths.get(filePath);

		if (Files.exists(path)) {
			
			try {
				List<String> lines = Files.readAllLines(path);
				for (String line : lines) {
					if(!line.isEmpty() && line != null)
						spreadsheetId = line;
				}
			} catch (IOException e) {
				System.out.println("讀取錯誤：" + e.getMessage());
			}
		} else {
			
			System.out.println("新增文件...");

			try {
				Files.createFile(path);
				System.out.println("文件已新增：" + filePath);
			} catch (IOException ex) {
				System.out.println("新增錯誤：" + ex.getMessage());
			}
		}

		System.out.println(spreadsheetId);

		// 使用憑證檔案建立Google Sheets服務
		Sheets sheetsService = getSheetsService();

		for (char c = 'A'; c <= 'Z'; c+=3) {
			String range = lang + "!" + c + "1:" + c + "36";
			String range_N = lang + "!" + (char)c+1 + "1:" + (char)c+1 + "36";

			// 使用 Sheets 服務來讀取試算表數據
			ValueRange response = sheetsService.spreadsheets().values()
					.get(spreadsheetId, range)
					.execute();

			ValueRange response_N = sheetsService.spreadsheets().values()
					.get(spreadsheetId, range_N)
					.execute();

			// 取得數據
			List<List<Object>> values = response.getValues();
			if (values == null || values.isEmpty()) {
				System.out.println("No data found.");
			} else {
				String word = "";
				for (List<Object> row : values) {
					if(!row.isEmpty() && row.toString().trim() != null && row.toString().trim() != "") {
						word = row.toString().replace("[", "").replace("]", "");
						if(lang.equals("英文"))
							strValuesEN.add(word);
						if(lang.equals("韓文"))
							strValuesKR.add(word);
						if(lang.equals("日文"))
							strValuesJP.add(word);
					}
					else {
						if(lang.equals("英文"))
							strValuesEN.add("-");
						if(lang.equals("韓文"))
							strValuesKR.add("-");
						if(lang.equals("日文"))
							strValuesJP.add("-");
					}
				}
			}

			List<List<Object>> values_N = response_N.getValues();
			if (values == null || values.isEmpty()) {
				System.out.println("No data found.");
			} else {
				String word = "";
				for (List<Object> row : values) {
					if(!row.isEmpty() && row.toString().trim() != null && row.toString().trim() != "") {
						word = row.toString().replace("[", "").replace("]", "");
						if(lang.equals("英文"))
							strValuesEN_N.add(word);
						if(lang.equals("韓文"))
							strValuesKR_N.add(word);
						if(lang.equals("日文"))
							strValuesJP_N.add(word);
					}
					else {
						if(lang.equals("英文"))
							strValuesEN_N.add("-");
						if(lang.equals("韓文"))
							strValuesKR_N.add("-");
						if(lang.equals("日文"))
							strValuesJP_N.add("-");
					}
				}
			}
		}

	}

	private static void addWord(Map<Character, List<WordInfo>> categorizedMap,
			char key, String word, String meaning) {
		WordInfo wordInfo = new WordInfo(word, meaning);
		categorizedMap.computeIfAbsent(key, k -> new ArrayList<>()).add(wordInfo);
	}

	// 確認工作表是否存在
	private static boolean isSheetExist(Sheets sheetsService, String spreadsheetId, String sheetName) throws Exception {
		Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();
		for (Sheet sheet : spreadsheet.getSheets()) {
			if (sheet.getProperties().getTitle().equals(sheetName)) {
				return true;
			}
		}
		return false;
	}

	// 創建新的工作表
	private static void createSheet(Sheets sheetsService, String spreadsheetId, String sheetName) throws Exception {
		AddSheetRequest addSheetRequest = new AddSheetRequest();
		addSheetRequest.setProperties(new SheetProperties().setTitle(sheetName));
		BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
		batchUpdateSpreadsheetRequest.setRequests(Arrays.asList(new Request().setAddSheet(addSheetRequest)));
		sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateSpreadsheetRequest).execute();
	}

	private static void updateSheet(String sheetName,String lang,String CN) {
		try {
			// 指定試算表ID和工作表名稱
			String spreadsheetId = "";
			// 指定文件路径
			String filePath = "excelID.txt";

			
			Path path = Paths.get(filePath);

			if (Files.exists(path)) {
				
				try {
					List<String> lines = Files.readAllLines(path);
					for (String line : lines) {
						if(!line.isEmpty() && line !="")
							spreadsheetId = line;
					}
				} catch (IOException e) {
					System.out.println("新增錯誤：" + e.getMessage());
				}
			} else {
				// 文件不存在，创建文件
				System.out.println("新增中...");

				try {
					Files.createFile(path);
					System.out.println("文件已新增：" + filePath);
				} catch (IOException ex) {
					System.out.println("新增錯誤：" + ex.getMessage());
				}
			}


			// 使用憑證檔案初始化 Google Sheets 服務
			Sheets sheetsService = getSheetsService();

			// 檢查工作表是否存在，不存在則創建
			if (!isSheetExist(sheetsService, spreadsheetId, sheetName)) {
				createSheet(sheetsService, spreadsheetId, sheetName);
			}

			List<String> strValues = new ArrayList<>();
			for(char c = 'A'; c <= 'Z'; c+=3) {
					// 指定 range
					// String range = sheetName + "!" + c + i;
					String range = sheetName + "!" + c + "1:" + c + "36";
					ValueRange response = sheetsService.spreadsheets().values()
							.get(spreadsheetId, range)
							.execute();

					// 取得數據
					List<List<Object>> values = response.getValues();
					if (values == null || values.isEmpty()) {
						System.out.println("No data found.");
					} else {
						String word = "";
						for (List<Object> row : values) {
							if(!row.isEmpty() && row.toString().trim() != null && row.toString().trim() != "") {
								word = row.toString().replace("[", "").replace("]", "");
								strValues.add(word);
							}
						}
					} 
			}
			int line = 0;
			if(strValues.size()/16 >= 9)
				line = strValues.size()/16%9;
			else
				line = strValues.size()/16;
			
			
			String next =(char)('A'+3*(line)) + "" + (strValues.size()%16+1+(strValues.size()/16/9*20));

			String range = sheetName + "!" + next;
			ValueRange body = new ValueRange()
					.setValues(Arrays.asList(
							Arrays.asList(lang , CN)
							));

//			// 使用 Sheets 服務將數據寫入試算表
			UpdateValuesResponse result = sheetsService.spreadsheets().values()
					.update(spreadsheetId, range, body)
					.setValueInputOption("RAW")
					.execute();

			JFrame jFrame = new JFrame();
			JOptionPane.showMessageDialog(jFrame, "成功 !");
			System.out.println("Updated " + result.getUpdatedCells() + " cells." + next + " is null");

			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private static void EN(Object o,String newlang,Map<Character, List<WordInfo>> categorizedMap,JList list,DefaultListModel<String> listModel_name,int index) {
		if (o.toString().contains("$ ")) {
			DefaultListModel<String> listModel = new DefaultListModel<>();

			listModel.addElement(newlang);

			String bool = "";
			List<String> fileContent = new ArrayList<>();

			for (Map.Entry<Character, List<WordInfo>> entry : categorizedMap.entrySet()) {			            		
				List<WordInfo> wordInfoList = entry.getValue();
				if(entry.getKey().toString().trim().equals(o.toString().replace("$ ", "").trim())) {
					listModel_name.clear();
					listModel_name.addElement(newlang);
					for (WordInfo wordInfo : wordInfoList) {
						// 获取单词和含义
						listModel.addElement("@ " + wordInfo.getWord());
						listModel_name.addElement(wordInfo.getMeaning());
					}
					list.setModel(listModel);
				}
			}
		}
		else if (o.toString().contains("@ ")) {
			DefaultListModel<String> listModel = new DefaultListModel<>();
			listModel.addElement(newlang);
			listModel.addElement(listModel_name.get(index));
			list.setModel(listModel);
		}
	}
}

