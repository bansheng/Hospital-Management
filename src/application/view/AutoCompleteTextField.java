package application.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Window;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 根据输入的内容和提示列表来匹配提示信息
 * 
 *
 */
public class AutoCompleteTextField
{
	private TextField textField;
	private final static int LIST_MAX_SIZE = 10;
	private final static int LIST_CELL_HEIGHT = 24;
	
	/** pinyin4j 工具类 用来匹配输入内容 */
	private HanyuPinyinOutputFormat pinyinFormat = new HanyuPinyinOutputFormat();

	/** 用来存储显示 出来的信息列表 */
	private ObservableList<String> showCacheDataList = FXCollections.<String> observableArrayList();

	/** 用来存储缓存的信息列表 重写indexOf方法来获取匹配到的数据 */
	private List<String> cacheDataList = new ArrayList<String>()
	{
		private static final long serialVersionUID = 281687373227150590L;

		@Override
		public int indexOf(Object searchContext)
		{
			showCacheDataList.clear();
			int size = cacheDataList.size();
			
			if(null == searchContext || searchContext.toString().equals("")) {
				removeDuplicate(cacheDataList);
				int size1 = cacheDataList.size();
				for (int i = 0; i < size1; i++) {
					showCacheDataList.add(cacheDataList.get(i));
					
				}
				return 0;
			}
			
			for (int i = 0; i < size; i++)
			{
				char[] charArry = cacheDataList.get(i).toCharArray();
				StringBuilder sbPinyin = new StringBuilder();
				String indexPinyin = new String();
				for (char ch : charArry)
				{
					// 将搜索内容转换为拼音 方便搜索
					try
					{
						String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(ch, pinyinFormat);
						sbPinyin.append(null != pinyin ? pinyin[0] : ch);
						if(null != pinyin) {
							indexPinyin = indexPinyin + pinyin[0].charAt(0);
						}
					} catch (BadHanyuPinyinOutputFormatCombination e)
					{
						sbPinyin.append(ch);
					}
				}
				if (cacheDataList.get(i).contains(searchContext.toString().toLowerCase())
						|| sbPinyin.toString().contains(searchContext.toString().toLowerCase()) || 
						indexPinyin.contains(searchContext.toString().toLowerCase()))
				{
					showCacheDataList.add(cacheDataList.get(i));
				}
			}
			return -1;
		};
	};

	/** 监听输入框的内容 */
	private SimpleStringProperty inputContent = new SimpleStringProperty();

	/** 输入内容后显示的pop */
	private Popup popShowList = new Popup();

	/** 输入内容后显示的提示信息列表 */
	private ListView<String> autoTipList = new ListView<String>();

	AutoCompleteTextField(TextField textField, List<String> cacheDataList)
	{
		if (null == textField)
		{
			throw new RuntimeException("textField 不能为空");
		}
		this.textField = textField;
		if (null != cacheDataList)
		{
			this.cacheDataList.addAll(cacheDataList);
		}
		configure();
		confListnenr();
	}

	AutoCompleteTextField(TextField textField)
	{
		this(textField, null);
	}

	public void setCacheDataList(List<String> cacheDataList)
	{
		this.cacheDataList.clear();
		this.cacheDataList.addAll(cacheDataList);
	}

	/**
	 * 
	 * 添加监听事件
	 * 
	 */
	private void confListnenr()
	{
		textField.textProperty().bindBidirectional(inputContent);

//		textField.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>()
//		{
//			@Override
//			public void handle(ActionEvent event)
//			{
//				cacheDataList.add(inputContent.get()); // setOnAction事件后才会生效，此处是点击按钮时将文本框中数据存入到cacheDataList中
//				removeDuplicate(cacheDataList);
//			}
//			
//		});
		
		textField.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				configureListContext(inputContent.get());
			}
			
		});
		
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getCode() == KeyCode.ALT) 
				{
					configureListContext(inputContent.get());
				}
			}
			
		});

		inputContent.addListener(new ChangeListener<String>()
		{

			@Override
			public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue)
			{
				if(newValue != "" && newValue!=null)
					configureListContext(newValue);    //当文本框中内容发生变化时会触发此事件，对文本框中内容进行匹配
			}
		});

		autoTipList.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				selectedItem();
			}
		});

		autoTipList.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event)
			{
				if(event.getCode() == KeyCode.ENTER) 
				{
					selectedItem();
				}
			}
		});
	}
	
	/**
	 * 获取选中的list内容到输入框
	 */
	private void selectedItem() {
		inputContent.set(autoTipList.getSelectionModel().getSelectedItem());
		textField.end();
		popShowList.hide();
	}

	/**
	 * 根据输入的内容来配置提示信息
	 */
	public void configureListContext(String tipContent)
	{
		cacheDataList.indexOf(tipContent);
		if(!showCacheDataList.isEmpty() && textField.isFocused()) {
			showTipPop();
		} else {
			popShowList.hide();
		}
	}

	/**
	 * 配置组建
	 */
	private void configure()
	{
		pinyinFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		pinyinFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		pinyinFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

		popShowList.setAutoHide(true);
		popShowList.getContent().add(autoTipList);
  		autoTipList.setItems(showCacheDataList);
	}
	
	public void removeDuplicate(List<String> cacheDataList2) {
		HashSet<String> h  =   new  HashSet<String>(cacheDataList2); 
	    cacheDataList2.clear(); 
	    cacheDataList2.addAll(h); 
	}
	

	/**
	 * 获取pop显示的窗口
	 */
	public final Window getWindow()
	{
		return getScene().getWindow();
	}

	/**
	 * 获取textField Scene
	 */
	public final Scene getScene()
	{
		return textField.getScene();
	}

	/**
	 * 显示pop
	 */
	public final void showTipPop()
	{
		autoTipList.setPrefWidth(textField.getWidth() - 3);
		if(showCacheDataList.size() < LIST_MAX_SIZE) {
			autoTipList.setPrefHeight(showCacheDataList.size() * LIST_CELL_HEIGHT + 3);
		} else {
			autoTipList.setPrefHeight(LIST_MAX_SIZE * LIST_CELL_HEIGHT + 3);
		}
		Window window = getWindow();
		Scene scene = getScene();
		Point2D fieldPosition = textField.localToScene(0, 0);
		popShowList.show(window, window.getX() + fieldPosition.getX() + scene.getX(), window.getY() + fieldPosition.getY() + scene.getY() + textField.getHeight());
		autoTipList.getSelectionModel().clearSelection();
		autoTipList.getFocusModel().focus(-1);
	}
}
