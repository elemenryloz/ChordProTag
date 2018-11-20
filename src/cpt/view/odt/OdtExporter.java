package cpt.view.odt;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.SortedSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.odftoolkit.odfdom.dom.attribute.fo.FoMinWidthAttribute;
import org.odftoolkit.odfdom.dom.element.OdfStylePropertiesBase;
import org.odftoolkit.odfdom.dom.element.draw.DrawCustomShapeElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawEnhancedGeometryElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawEquationElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawFillImageElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawHandleElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawImageElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawTextBoxElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeEventListenersElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeFontFaceDeclsElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeScriptsElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.odftoolkit.odfdom.dom.element.script.ScriptEventListenerElement;
import org.odftoolkit.odfdom.dom.element.style.StyleColumnElement;
import org.odftoolkit.odfdom.dom.element.style.StyleColumnsElement;
import org.odftoolkit.odfdom.dom.element.style.StyleFontFaceElement;
import org.odftoolkit.odfdom.dom.element.style.StyleGraphicPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleMasterPageElement;
import org.odftoolkit.odfdom.dom.element.style.StyleParagraphPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleSectionPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTabStopElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTabStopsElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTableRowPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTextPropertiesElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableColumnElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableRowElement;
import org.odftoolkit.odfdom.dom.element.text.TextBookmarkRefElement;
import org.odftoolkit.odfdom.dom.element.text.TextHElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSectionElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.dom.style.props.OdfPageLayoutProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfStylePropertiesSet;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeStyles;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStylePageLayout;
import org.odftoolkit.odfdom.pkg.OdfFileDom;
import org.odftoolkit.odfdom.pkg.OdfXMLFactory;
import org.odftoolkit.simple.TextDocument;
import org.w3c.dom.Node;

import view.filedialog.UdoFileDialog;
import cpt.control.Control;
import cpt.model.Chord;
import cpt.model.ChordList;
import cpt.model.ModelSong;
import cpt.model.ModelToken;


public class OdtExporter extends JDialog implements ListSelectionListener {
		
		static int cnt=0;
		static int zIx=0;
		public static OdtExporter inst;
		
		
		private OdfStyle sTitle, sArtist;
		private OdfStyle sChorus;
		private OdfStyle sLyrics, sLyricsChorus, sLyricsTab, sLyricsChorusTab;
		private OdfStyle sChords, sChordsChorus, sChordsTab, sChordsChorusTab;
		private OdfStyle sComment, sCommentItalic, sCommentBox;
		private OdfStyle sIndexHeading,sIndexSeparator,sIndexEntry;

		private boolean incomment;
		private boolean inchorus;
		private boolean intab;
		private int oVoicingLoc = 1;
		private int oVoicingCols = 1;
		private String oVoicingSize = "2.0";
		private String oMarginLeft = "2.0";
		private String oMarginRight = "2.0";
		private String oMarginTop = "2.0";
		private String oMarginBottom = "2.0";
		
		JTextField songBookTitle;
		private JList<ModelSong> lCols;
		private DefaultListModel<ModelSong> lm;
		private JButton upButton;
		private JButton downButton;
		private String upString="Move up";
		private String downString="Move down";
		private TextSectionElement sectionSongs;
		private ChordList chordList;
		OdfStyle sChordFrame;

		String oChordTitleColor;
		private String oChordFretColor;

	    private Control listener;
		protected String oTitleDefault;
		
		public OdtExporter(){
			super();
	        this.setTitle("ODT Exporter");
	        this.setSize(200,200);
	        this.setModal(true);
	        this.setLayout(new GridBagLayout());
	        int b;
	        
	        GridBagConstraints c=new GridBagConstraints();
			c.fill=GridBagConstraints.HORIZONTAL;
	        c.weightx=0;
			c.insets=new Insets(5,5,5,5);
			c.gridx=0;
			c.gridy=0;
			c.gridheight=1;
			c.gridwidth=5;		
	        this.add(new JLabel("sort songs before export"),c);
	        
	        lm = new DefaultListModel<ModelSong>();
	        lCols = new JList<ModelSong>(lm);
	        lCols.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	        lCols.setCellRenderer(new DefaultListCellRenderer(){

	        	JLabel component = new JLabel();
	        	
				@Override
				public Component getListCellRendererComponent(JList list, Object o, int row, boolean isSelected, boolean isEditing) {
					component=(JLabel) super.getListCellRendererComponent(list,o,row,isSelected,isEditing);
					ModelSong col = (ModelSong) o; 
					component.setText(col.meta.get("title")+" - ("+col.meta.get("artist")+")");
					return component;
				}
	        	
	        });
			c.fill=GridBagConstraints.BOTH;
			c.gridx=0;
			c.gridy=1;
			c.gridheight=(b=7);
			c.gridwidth=2;		
	        this.add(new JScrollPane(lCols),c);

			c.gridy=++b;
			c.gridheight=1;
			c.gridwidth=5;		
	        this.add(new JSeparator(JSeparator.HORIZONTAL),c);

	        JButton button=new JButton("Utils");
	        button.setMnemonic(KeyEvent.VK_U);
			c.gridy=++b;
			c.gridheight=1;
			c.gridwidth=1;		
	        this.add(button,c);
	        c.weightx=1;
			c.gridx=1;
	        this.add(new JLabel("               "),c);
	        button=new JButton("OK");
	        button.setMnemonic(KeyEvent.VK_O);
	        button.addActionListener(new OKButtonListener());
	        c.weightx=0;
			c.gridx=2;
	        this.add(button,c);
	        button=new JButton("Cancel");
	        button.setMnemonic(KeyEvent.VK_C);
	        button.addActionListener(new CancelButtonListener());
			c.gridx=3;
	        this.add(button,c);
	        button=new JButton("Help");
	        button.setMnemonic(KeyEvent.VK_H);
			c.gridx=4;
	        this.add(button,c);			     
	        
	        
	        int a=2;
	        b=2;
			c.fill=GridBagConstraints.HORIZONTAL;
	        c.gridx=a;
			c.gridy=++b;
			c.gridwidth=3;
	        this.add(new JLabel("Song Book Title"),c);
			c.gridy=++b;
			Border border = BorderFactory.createEtchedBorder();
			songBookTitle = new JTextField(oTitleDefault);
			songBookTitle.setBorder(border);
	        this.add(songBookTitle,c);
			c.gridy=++b;
			c.gridwidth=1;		
//	        this.add(new JLabel(""),c);
	        upButton=new JButton(upString);
	        upButton.setActionCommand(upString);
	        upButton.setMnemonic(KeyEvent.VK_U);
	        upButton.addActionListener(new UpDownListener());
			c.gridy=++b;
	        this.add(upButton,c);
	        downButton=new JButton(downString);
	        downButton.setActionCommand(downString);
	        downButton.setMnemonic(KeyEvent.VK_D);
	        downButton.addActionListener(new UpDownListener());
			c.gridy=++b;
	        this.add(downButton,c);

	        lCols.addListSelectionListener(this);
	        lCols.setSelectedIndex(0);
	        
	        this.pack();
//	        this.setLocationRelativeTo(Control.frame);
	        
	        OdtExporter.inst=this;
		}
		
		public void run(String output, String songBookTitle){  
			try {
				TextDocument odt = TextDocument.newTextDocument();
				createScript(odt);
				createProlog(odt,songBookTitle);
		        createTitlePage(odt,songBookTitle);
		        Item items[]=createSongs(odt);
		        createIndexes(odt,items);
		        odt.save(output);
				listener.getModel().getOptions().put("odt.lastfile", output);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}

		private void createScript(TextDocument odt) throws Exception {
		    String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		            + "\n<!DOCTYPE library:libraries PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\" \"libraries.dtd\">"
		            + "\n<library:libraries xmlns:library=\"http://openoffice.org/2000/library\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">"
		            + "\n <library:library library:name=\"Standard\" library:link=\"false\"/>"
		            + "\n</library:libraries>";
		    odt.getPackage().insert(xml.getBytes(), "Basic/script-lc.xml", "text:xml");
		    
		    xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		            +"\n<!DOCTYPE script:module PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\" \"module.dtd\">"
		            +"\n<script:module xmlns:script=\"http://openoffice.org/2000/script\" script:name=\"Module1\" script:language=\"StarBasic\">"
		            +"\nSub RunAtLoad"

					+"\n\tvCurs = thisComponent.currentController.ViewCursor"
					+"\n\ttext = thisComponent.Text"
		        	+"\n\ttCurs = text.createTextCursor()"
		            +"\n\tparas = text.createEnumeration"
		            +"\n\toldP = 0"
		            +"\n\twhile paras.hasmoreelements"
		            +"\n\t\tpara = paras.nextelement"
		        	+"\n\t\tif para.supportsService(\"com.sun.star.text.Paragraph\") Then"
		        	+"\n\t\t\tstyle = para.ParaStyleName"
		        	+"\n\t\t\toutline = para.OutlineLevel"
		        	+"\n\t\t\tif outline = 1 then"
		        	+"\n\t\t\t\tvCurs.gotoRange(para,false)"
		        	+"\n\t\t\t\tp = vCurs.Page"
		        	+"\n\t\t\t\tif oldP > 0 then" 
		        	+"\n\t\t\t\t\tn = p - oldP"
		        	+"\n\t\t\t\t\tif n>1 AND ((oldP MOD 2)=1) then" 
		        	+"\n\t\t\t\t\t\tvCurs.gotoRange(oldPara,false)"
		        	+"\n\t\t\t\t\t\ttCurs.gotoRange(vCurs, False)"
		        	+"\n\t\t\t\t\t\ttext.insertString(tCurs,chr(13),false)"
		        	+"\n\t\t\t\t\t\tp=p+1"
		        	+"\n\t\t\t\t\tendif"
		        	+"\n\t\t\t\t\tif style &lt;&gt; \"CPT_Song_Title\" then"
		            +"\n\t\t\t\t\t\tThisComponent.TextFields.refresh"
		        	+"\n\t\t\t\t\t\texit Sub"
		        	+"\n\t\t\t\t\tendif"
		        	+"\n\t\t\t\tendif"
		        	+"\n\t\t\t\toldP = p"
		        	+"\n\t\t\t\toldPara = para"
		        	+"\n\t\t\tendif"
		            +"\n\t\tendif"
		            +"\n\twend"		            
		            +"\nEnd Sub"
		            +"\n</script:module>";
		    odt.getPackage().insert(xml.getBytes(), "Basic/Standard/Module1.xml", "text:xml");
		    xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		            +"\n<!DOCTYPE library:library PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\" \"library.dtd\">"
		            +"\n<library:library xmlns:library=\"http://openoffice.org/2000/library\" library:name=\"Standard\" library:readonly=\"false\" library:passwordprotected=\"false\">"
		            +"\n <library:element library:name=\"Module1\"/>"
		            +"\n</library:library>";
		    odt.getPackage().insert(xml.getBytes(), "Basic/Standard/script-lb.xml", "text:xml");
		    
		    InputStream input = getClass().getResourceAsStream("/cpt/view/images/title_page.png");
		    odt.getPackage().insert(input, "Pictures/title_page.png", "image/png");
		}

		private void createIndexes(TextDocument odt, Item item[]) throws Exception {
			
		    TextSectionElement eSection;
		    TextHElement eHeader;
			TextPElement eParagraph;
			TextBookmarkRefElement eBookmark;
		    
		    eSection = odt.getContentRoot().newTextSectionElement("Indexes", "Indexes");
		    
		    eSection.setProperty(StyleSectionPropertiesElement.DontBalanceTextColumns, "true");
		    
		    
		    StyleColumnsElement cc = eSection.getOrCreateUnqiueAutomaticStyle().newStyleSectionPropertiesElement().newStyleColumnsElement(2);
		    StyleColumnElement ce = cc.newStyleColumnElement("4818*");
		    ce.setFoStartIndentAttribute("0cm");
		    ce.setFoEndIndentAttribute("0.25cm");
		    cc.newStyleColumnElement("4819*");
		    ce.setFoStartIndentAttribute("0.25cm");
		    ce.setFoEndIndentAttribute("0cm");
		    eHeader = eSection.newTextHElement(1);
		    eHeader.setTextContent("By Artist");
		    eHeader.setDocumentStyle(sIndexHeading);
		    
		    for(int i=0;i<item.length-1;i++){
		        for(int j=i+1;j<item.length; j++){
		            if ( item[i].artist.compareTo(item[j].artist)>0 ||
		                    (item[i].artist.compareTo(item[j].artist)==0 && item[i].title.compareTo(item[j].title)>0)) {
		                Item h=item[i];
		                item[i]=item[j];
		                item[j]=h;
		            }
		        }
		    }
		    
		    String oldArtist="";
		    for(int i=0;i<item.length;i++) {
		        String artist=item[i].artist;
		        if(!artist.equals(oldArtist)) {
		            eParagraph=eSection.newTextPElement();
		            eParagraph.setTextContent(artist);    
		            eParagraph.setDocumentStyle(sIndexSeparator);
		            oldArtist=artist;
		        }
		        eParagraph = eSection.newTextPElement();
		        eParagraph.setDocumentStyle(sIndexEntry);
		        eParagraph.setTextContent(item[i].title);
		        eParagraph.newTextTabElement();
		        eBookmark = eParagraph.newTextBookmarkRefElement();
		        eBookmark.setTextRefNameAttribute("title-"+item[i].ref);
		        eBookmark.setTextReferenceFormatAttribute("page");
		    }
		    
		    
		    eHeader = eSection.newTextHElement(1);
		    eHeader.setTextContent("By Title");
		    eHeader.setDocumentStyle(sIndexHeading);

		    for(int i=0;i<item.length-1;i++){
		        for(int j=i+1;j<item.length; j++){
		            if ( item[i].title.compareTo(item[j].title)>0 ||
		                    (item[i].title.compareTo(item[j].title)==0 && item[i].artist.compareTo(item[j].artist)>0)) {
		                Item h=item[i];
		                item[i]=item[j];
		                item[j]=h;
		            }
		        }
		    }
		    
		    char oldChar=' ';
		    for(int i=0;i<item.length;i++) {
		        char chr=item[i].title.charAt(0);
		        if(chr!=oldChar) {
		            eParagraph=eSection.newTextPElement();
		            eParagraph.setTextContent(""+chr);    
		            eParagraph.setDocumentStyle(sIndexSeparator);
		            oldChar=chr;
		        }
		        eParagraph=eSection.newTextPElement();
		        eParagraph.setDocumentStyle(sIndexEntry);
		        eParagraph.setTextContent(item[i].title + " ("+item[i].artist+")");
		        eParagraph.newTextTabElement();
		        eBookmark = eParagraph.newTextBookmarkRefElement();
		        eBookmark.setTextRefNameAttribute("title-"+item[i].ref);
		        eBookmark.setTextReferenceFormatAttribute("page");
		    }
		            
		}

		private Item[] createSongs(TextDocument odt) throws Exception {
			
			chordList = new ChordList();

		    sectionSongs = odt.getContentRoot().newTextSectionElement("Songs", "Songs");
		    Item items[] = new Item[lm.getSize()];
	    	for(int i=0; i<lm.getSize(); i++) {
				ModelSong it=lm.get(i);
				items[i]=new Item(it, it.meta.get("artist"), it.meta.get("title"),it.allText.split("\n"));
				writeODT(odt, items[i], i==0);
			}
	    	
			if(oVoicingLoc==2) {
				TextHElement xx = odt.getContentRoot().newTextHElement(1);
			    xx.setDocumentStyle(sIndexHeading);
			    xx.newTextNode("Chords");
				TextSectionElement sectionChords = odt.getContentRoot().newTextSectionElement("Chords", "Chords");
			    StyleColumnsElement cc = sectionChords.getOrCreateUnqiueAutomaticStyle().newStyleSectionPropertiesElement().newStyleColumnsElement(oVoicingCols);
			    for(int i=0; i<=oVoicingCols; i++){
				    StyleColumnElement ce = cc.newStyleColumnElement((9637/oVoicingCols)+"*");
				    ce.setFoStartIndentAttribute("0cm");
				    ce.setFoEndIndentAttribute("0.25cm");
			    }
			    TableTableElement table = sectionChords.newTableTableElement();
			    TableTableColumnElement column = table.newTableTableColumnElement();
			    column.setStyleName("songCol");
				TableTableRowElement row = table.newTableTableRowElement();
				TableTableCellElement cell = row.newTableTableCellElement(1.0, "string");
				chordListToODT(chordList, odt,cell,0);
			}

		    return items;
		}

		private void createTitlePage(TextDocument odt, String title) throws Exception {
			
			Node childNode; 
			OfficeTextElement root = odt.getContentRoot();
			childNode = root.getFirstChild(); 
			while (childNode != null) { 
				root.removeChild(childNode); 
				childNode = root.getFirstChild(); 
			}
			
			TextSectionElement x = root.newTextSectionElement("Title", "Title");
			
		   	TextPElement xx = x.newTextPElement();
		   	xx.setStyleName("PT");
			DrawCustomShapeElement xxx = xx.newDrawCustomShapeElement();
			xxx.setDrawTextStyleNameAttribute("P2");
			xxx.setDrawZIndexAttribute(0);
		    xxx.setStyleName("P2");
		    xxx.setTextAnchorTypeAttribute("page");
		    xxx.setTextAnchorPageNumberAttribute(1);
		    xxx.setDrawStyleNameAttribute("gr1");
		    xxx.setSvgWidthAttribute("17cm");
		    xxx.setSvgHeightAttribute("8.82cm");
		    xxx.setSvgXAttribute("0cm");
		    xxx.setSvgYAttribute("8.438cm");
		    TextPElement xxxx = xxx.newTextPElement();
		    xxxx.setStyleName("P1");
		    TextSpanElement xxxxx = xxxx.newTextSpanElement();
		    xxxxx.setStyleName("T1");
		    xxxxx.setTextContent(title);
		    DrawEnhancedGeometryElement x6 = xxx.newDrawEnhancedGeometryElement();
		    x6.setSvgViewBoxAttribute("0 0 21600 21600");
		    x6.setDrawTextAreasAttribute("0 0 21600 21600");
		    x6.setDrawTextPathAttribute(true);
		    x6.setDrawTextPathModeAttribute("shape");
		    x6.setDrawTextPathScaleAttribute("path");
		    x6.setDrawTextPathSameLetterHeightsAttribute(false);
		    x6.setDrawTypeAttribute("fontwork-slant-up");
		    x6.setDrawModifiersAttribute("12000");
		    x6.setDrawEnhancedPathAttribute("M 0 ?f0 L 21600 0 N M 0 21600 L 21600 ?f1 N");
		    DrawEquationElement x7 = x6.newDrawEquationElement();
		    x7.setDrawNameAttribute("f0");
		    x7.setDrawFormulaAttribute("$0");
		    x7 = x6.newDrawEquationElement();
		    x7.setDrawNameAttribute("f1");
		    x7.setDrawFormulaAttribute("21600-$0 ");
		    DrawHandleElement x8 = x6.newDrawHandleElement("left $0");
		    x8.setDrawHandleRangeYMinimumAttribute("0");
		    x8.setDrawHandleRangeYMaximumAttribute("15400");
		    
		    xx = x.newTextPElement();
		   	xx.setStyleName("PT");		    
		}

		private void createProlog(TextDocument odt, String title) throws Exception{
			
		    OfficeScriptsElement x = odt.getContentDom().getRootElement().newOfficeScriptsElement();
		    OfficeEventListenersElement xx = x.newOfficeEventListenersElement();
		    ScriptEventListenerElement xxx = xx.newScriptEventListenerElement("office:view-created", "ooo:script");
		    xxx.setScriptMacroNameAttribute("RunAtLoad");
		    xxx.setXlinkHrefAttribute("vnd.sun.star.script:Standard.Module1.RunAtLoad?language=Basic&location=document");
		    xxx.setXlinkTypeAttribute("simple");

		    StyleMasterPageElement defaultPage = odt.getOfficeMasterStyles ().getMasterPage ( "Standard" );
		    TextPElement footer = defaultPage.newStyleFooterElement().newTextPElement();
		    footer.setStyleName("footer");
		    footer.newTextDateElement();
		    footer.newTextTabElement();
		    footer.newTextNode("Page ");
		    footer.newTextPageNumberElement("current");
		    footer.newTextTabElement();
		    footer.newTextNode(title);
		    
		    String pageLayoutName = defaultPage.getStylePageLayoutNameAttribute ();
		    OdfStylePageLayout pageLayout = defaultPage.getAutomaticStyles ().getPageLayout ( pageLayoutName );
		    pageLayout.setStylePageUsageAttribute("mirrored");
		    pageLayout.setProperty ( OdfPageLayoutProperties.MarginLeft, oMarginLeft+"cm" );
		    pageLayout.setProperty ( OdfPageLayoutProperties.MarginRight, oMarginRight+"cm" );
		    pageLayout.setProperty ( OdfPageLayoutProperties.MarginTop, oMarginTop+"cm" );
		    pageLayout.setProperty ( OdfPageLayoutProperties.MarginBottom, oMarginBottom+"cm" );

		    OdfOfficeStyles styles = odt.getOrCreateDocumentStyles();
		    
			sTitle = styles.newStyle("CPT_5f_Song_5f_Title",OdfStyleFamily.Paragraph);
		    sTitle.setStyleDisplayNameAttribute("CPT_Song_Title");
		    sTitle.setStyleParentStyleNameAttribute("Heading_20_1");
		    sTitle.setProperty(StyleParagraphPropertiesElement.BreakBefore, "page");
		    sTitle.setProperty(StyleParagraphPropertiesElement.MarginTop, "0.0cm");
		    sTitle.setProperty(StyleParagraphPropertiesElement.TextAlign, "center");
		    
		    sArtist = styles.newStyle("CPT_5f_Song_5f_Artist",OdfStyleFamily.Paragraph);
		    sArtist.setStyleDisplayNameAttribute("CPT_Song_Artist");
		    sArtist.setStyleParentStyleNameAttribute("Heading_20_2");
		    sArtist.setProperty(StyleParagraphPropertiesElement.MarginTop, "0.0cm");
		    sArtist.setProperty(StyleParagraphPropertiesElement.TextAlign, "center");

		    sChorus = styles.newStyle("CPT_5f_Song_5f_Chorus",OdfStyleFamily.Paragraph);
		    sChorus.setStyleDisplayNameAttribute("CPT_Song_Chorus");
		    sChorus.setProperty(StyleParagraphPropertiesElement.BackgroundColor, "#ffffbb" );
		    sChorus.setProperty(StyleParagraphPropertiesElement.MarginTop,"0.2cm");
		    sChorus.setProperty(StyleParagraphPropertiesElement.MarginBottom,"0.2cm");
		    
		    sComment = styles.newStyle("CPT_5f_Song_5f_Comment",OdfStyleFamily.Paragraph);
		    sComment.setStyleDisplayNameAttribute("CPT_Song_Comment");
		    sComment.setProperty(StyleTextPropertiesElement.FontName, "Arial");
		    sComment.setProperty(StyleTextPropertiesElement.FontSize, "12pt");
		    sComment.setProperty(StyleParagraphPropertiesElement.BackgroundColor, "#eeeeee" );
		    sComment.setProperty(StyleParagraphPropertiesElement.MarginTop,"0.4cm");
		    sComment.setProperty(StyleParagraphPropertiesElement.MarginBottom,"0.2cm");
		    
		    sCommentItalic = styles.newStyle("CPT_Song_5f_Comment_5f_Italic",OdfStyleFamily.Paragraph);
		    sCommentItalic.setStyleDisplayNameAttribute("CPT_Song_Comment_Italic");
		    sCommentItalic.setStyleParentStyleNameAttribute("CPT_5f_Song_5f_Comment");
		    sCommentItalic.setProperty(StyleTextPropertiesElement.FontStyle, "italic");
		    
		    sCommentBox = styles.newStyle("CPT_5f_Song_5f_Comment_5f_Box",OdfStyleFamily.Paragraph);
		    sCommentBox.setStyleDisplayNameAttribute("CPT_Song_Comment_Box");
		    sCommentBox.setStyleParentStyleNameAttribute("CPT_5f_Song_5f_Comment");
		    sCommentBox.setProperty(StyleParagraphPropertiesElement.Border, "0.002cm solid #000000");
		    
		    sChords = styles.newStyle("CPT_5f_Song_5f_Chord",OdfStyleFamily.Paragraph);
		    sChords.setStyleDisplayNameAttribute("CPT_Song_Chord");
		    sChords.setProperty(StyleTextPropertiesElement.FontName, "Arial");
		    sChords.setProperty(StyleTextPropertiesElement.FontWeight, "bold");
		    sChords.setProperty(StyleTextPropertiesElement.FontSize, "13pt");

		    sChordsChorus = styles.newStyle("CPT_5f_Song_5f_Chord_5f_Chorus",OdfStyleFamily.Paragraph);
		    sChordsChorus.setStyleDisplayNameAttribute("CPT_Song_Chord_Chorus");
		    sChordsChorus.setStyleParentStyleNameAttribute("CPT_5f_Song_5f_Chord");
		    
		    sChordsTab = styles.newStyle("CPT_5f_Song_5f_Chord_5f_Tab",OdfStyleFamily.Paragraph);
		    sChordsTab.setStyleDisplayNameAttribute("CPT_Song_Chord_Tab");
		    sChordsTab.setStyleParentStyleNameAttribute("CPT_5f_Song_5f_Chord");
		    sChordsTab.setProperty(StyleTextPropertiesElement.FontName, "Courier New");

		    sChordsChorusTab = styles.newStyle("CPT_5f_Song_5f_Chord_5f_Chorus_5f_Tab",OdfStyleFamily.Paragraph);
		    sChordsChorusTab.setStyleDisplayNameAttribute("CPT_Song_Chord_Chorus_Tab");
		    sChordsChorusTab.setStyleParentStyleNameAttribute("CPT_5f_Song_5f_Chord_5f_Chorus");
		    sChordsChorusTab.setProperty(StyleTextPropertiesElement.FontName, "Courier New");
		    
		    sLyrics = styles.newStyle("CPT_5f_Song_5f_Lyrics",OdfStyleFamily.Paragraph);
		    sLyrics.setStyleDisplayNameAttribute("CPT_Song_Lyrics");
		    sLyrics.setProperty(StyleTextPropertiesElement.FontName, "Arial");
		    sLyrics.setProperty(StyleTextPropertiesElement.FontSize, "11pt");
		    
		    sLyricsChorus = styles.newStyle("CPT_5f_Song_5f_Lyrics_5f_Chorus",OdfStyleFamily.Paragraph);
		    sLyricsChorus.setStyleDisplayNameAttribute("CPT_Song_Lyrics_Chorus");
		    sLyricsChorus.setStyleParentStyleNameAttribute("CPT_5f_Song_5f_Lyrics");
		    
		    sLyricsTab = styles.newStyle("CPT_5f_Song_5f_Lyrics_5f_Tab",OdfStyleFamily.Paragraph);
		    sLyricsTab.setStyleDisplayNameAttribute("CPT_Song_Lyrics_Tab");
		    sLyricsTab.setStyleParentStyleNameAttribute("CPT_5f_Song_5f_Lyrics");
		    sLyricsTab.setProperty(StyleTextPropertiesElement.FontName, "Courier New");
		    
		    sLyricsChorusTab = styles.newStyle("CPT_5f_Song_5f_Lyrics_5f_Chorus_Tab",OdfStyleFamily.Paragraph);
		    sLyricsChorusTab.setStyleDisplayNameAttribute("CPT_Song_Lyrics_Chorus_Tab");
		    sLyricsChorusTab.setStyleParentStyleNameAttribute("CPT_5f_Song_5f_Lyrics_5f_Chorus");
		    sLyricsChorusTab.setProperty(StyleTextPropertiesElement.FontName, "Courier New");
		    
		    
		    sIndexSeparator = styles.newStyle("CPT_5f_Index_5f_Separator",OdfStyleFamily.Paragraph);
		    sIndexSeparator.setStyleDisplayNameAttribute("CPT_Index_Separator");
		    sIndexSeparator.setStyleParentStyleNameAttribute("Standard");
		    sIndexSeparator.setProperty(StyleParagraphPropertiesElement.TextAlign,"center");
		    sIndexSeparator.setProperty(StyleParagraphPropertiesElement.MarginTop,"0.2cm");
		    sIndexSeparator.setProperty(StyleTextPropertiesElement.FontWeight,"bold");

		    
		    sIndexHeading = styles.newStyle("CPT_5f_Index_5f_Heading",OdfStyleFamily.Paragraph);
		    sIndexHeading.setStyleDisplayNameAttribute("CPT_Index_Heading");
		    sIndexHeading.setStyleParentStyleNameAttribute("Heading_20_1");
		    sIndexHeading.setProperty(StyleParagraphPropertiesElement.BreakBefore, "page");
		
		    sIndexEntry = styles.newStyle("CPT_5f_Index_5f_Entry",OdfStyleFamily.Paragraph);
		    sIndexEntry.setStyleDisplayNameAttribute("CPT_Index_Entry");
		    sIndexEntry.setStyleMasterPageNameAttribute("");
		    sIndexEntry.setProperty(StyleParagraphPropertiesElement.MarginLeft, "0.5cm");
		    StyleTabStopsElement tabStops = (StyleTabStopsElement) OdfXMLFactory.newOdfElement(odt.getStylesDom(), StyleTabStopsElement.ELEMENT_NAME);
		    StyleTabStopElement tabStop1 = (StyleTabStopElement) OdfXMLFactory.newOdfElement(odt.getStylesDom(), StyleTabStopElement.ELEMENT_NAME);
		    tabStop1.setStylePositionAttribute("16.5cm");
		    tabStop1.setStyleTypeAttribute("right");
		    tabStop1.setStyleLeaderStyleAttribute("dotted");
		    tabStop1.setStyleLeaderTextAttribute(".");
		    OdfStylePropertiesBase propElement = sIndexEntry.getPropertiesElement(OdfStylePropertiesSet.ParagraphProperties);
		    propElement.appendChild(tabStops);
		    tabStops.appendChild(tabStop1);
		    
		    OdfStyle p1 = odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.Paragraph);
		    p1.setStyleParentStyleNameAttribute("Standard");
		    p1.setStyleNameAttribute("PT");
		    p1.setStyleMasterPageNameAttribute("First_20_Page");
		    p1.setProperty(StyleParagraphPropertiesElement.PageNumber, "auto");

		    p1 = odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.Paragraph);
		    p1.setStyleParentStyleNameAttribute("CPT_Song_Title");
		    p1.setStyleNameAttribute("PX");
		    p1.setStyleMasterPageNameAttribute("Standard");
		    p1.setProperty(StyleParagraphPropertiesElement.PageNumber, "1");

		    p1 = odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.Graphic);
		    p1.setStyleParentStyleNameAttribute("Frame");
		    p1.setStyleNameAttribute("fr1");
		    p1.setProperty(StyleGraphicPropertiesElement.RunThrough, "foreground");
		    p1.setProperty(StyleGraphicPropertiesElement.Wrap, "run-through");
		    p1.setProperty(StyleGraphicPropertiesElement.NumberWrappedParagraphs, "no-limit");
		    p1.setProperty(StyleGraphicPropertiesElement.VerticalPos, "top");
		    p1.setProperty(StyleGraphicPropertiesElement.VerticalRel, "baseline");
		    p1.setProperty(StyleGraphicPropertiesElement.HorizontalPos, "from-left");
		    p1.setProperty(StyleGraphicPropertiesElement.HorizontalRel, "char");
		    p1.setProperty(StyleGraphicPropertiesElement.MarginBottom, "0cm");
		    p1.setProperty(StyleGraphicPropertiesElement.MarginTop, "0cm");
		    p1.setProperty(StyleGraphicPropertiesElement.MarginLeft, "0cm");
		    p1.setProperty(StyleGraphicPropertiesElement.MarginRight, "0cm");
		    p1.setProperty(StyleGraphicPropertiesElement.Padding, "0cm");
		    p1.setProperty(StyleGraphicPropertiesElement.Border, "none");
		    p1.setProperty(StyleGraphicPropertiesElement.FlowWithText, "false");

		    
		    p1=odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.Text);
		    p1.setStyleNameAttribute("T1");
		    p1.setProperty(StyleTextPropertiesElement.FontName, "Arial Black");
		    
		    OfficeFontFaceDeclsElement ff = (OfficeFontFaceDeclsElement) odt.getContentDom().getRootElement().getElementsByTagName("office:font-face-decls").item(0);
		    StyleFontFaceElement fe = ff.newStyleFontFaceElement("Arial Black");
		    fe.setSvgFontFamilyAttribute("Arial Black");
		    fe.setStyleFontFamilyGenericAttribute("roman");
		    
		    p1=odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.Paragraph);
		    p1.setStyleNameAttribute("P1");
		    p1.setProperty(StyleParagraphPropertiesElement.MarginLeft, "0cm");
		    p1.setProperty(StyleParagraphPropertiesElement.MarginRight, "0cm");
		    p1.setProperty(StyleParagraphPropertiesElement.TextIndent, "0cm");
		    p1.setProperty(StyleParagraphPropertiesElement.WritingMode, "lr-tb");
		    ((StyleParagraphPropertiesElement)p1.getPropertiesElement(OdfStylePropertiesSet.ParagraphProperties)).newStyleTabStopsElement();
		    
		    p1=odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.Paragraph);
		    p1.setStyleNameAttribute("P2");
		    p1.setProperty(StyleParagraphPropertiesElement.MarginLeft, "0cm");
		    p1.setProperty(StyleParagraphPropertiesElement.MarginRight, "0cm");
		    p1.setProperty(StyleParagraphPropertiesElement.MarginTop, "0cm");
		    p1.setProperty(StyleParagraphPropertiesElement.MarginBottom, "0cm");
		    p1.setProperty(StyleParagraphPropertiesElement.LineHeight, "100%");
		    p1.setProperty(StyleParagraphPropertiesElement.TextAlign, "start");
		    p1.setProperty(StyleParagraphPropertiesElement.TextIndent, "0cm");
		    p1.setProperty(StyleParagraphPropertiesElement.PunctuationWrap, "simple");
		    p1.setProperty(StyleParagraphPropertiesElement.LineBreak, "normal");
		    p1.setProperty(StyleParagraphPropertiesElement.WritingMode, "lr-tb");
		    p1.setProperty(StyleTextPropertiesElement.Color, "#000000");
		    p1.setProperty(StyleTextPropertiesElement.TextOutline, "false");
		    p1.setProperty(StyleTextPropertiesElement.TextLineThroughStyle, "none");
		    p1.setProperty(StyleTextPropertiesElement.TextPosition, "0% 100%");
		    p1.setProperty(StyleTextPropertiesElement.FontName, "Arial Black");
		    p1.setProperty(StyleTextPropertiesElement.FontSize, "24pt");
		    p1.setProperty(StyleTextPropertiesElement.FontStyle, "normal");
		    p1.setProperty(StyleTextPropertiesElement.FontWeight, "normal");
		    p1.setProperty(StyleTextPropertiesElement.FontRelief, "none");
		    p1.setProperty(StyleTextPropertiesElement.TextShadow, "none");
		    p1.setProperty(StyleTextPropertiesElement.TextUnderlineStyle, "none");
		    p1.setProperty(StyleTextPropertiesElement.TextEmphasize, "none");
		    ((StyleParagraphPropertiesElement)p1.getPropertiesElement(OdfStylePropertiesSet.ParagraphProperties)).newStyleTabStopsElement();
	
		    
		    p1=odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.Graphic);
		    p1.setStyleNameAttribute("gr1");
		    p1.setProperty(StyleGraphicPropertiesElement.Stroke, "solid");
		    p1.setProperty(StyleGraphicPropertiesElement.StrokeLinejoin, "miter");
		    p1.setProperty(StyleGraphicPropertiesElement.StrokeWidth, "0.026cm");
		    p1.setProperty(StyleGraphicPropertiesElement.StrokeColor, "#000000");
		    p1.setProperty(StyleGraphicPropertiesElement.MarkerStartWidth, "0.3cm");
		    p1.setProperty(StyleGraphicPropertiesElement.MarkerStartCenter, "false");
		    p1.setProperty(StyleGraphicPropertiesElement.MarkerEndWidth, "0.3cm");
		    p1.setProperty(StyleGraphicPropertiesElement.MarkerEndCenter, "false");
		    p1.setProperty(StyleGraphicPropertiesElement.Fill, "bitmap");
		    p1.setProperty(StyleGraphicPropertiesElement.FillColor, "#ffffff");
		    p1.setProperty(StyleGraphicPropertiesElement.FillImageName, "Bitmape_20_2");
		    p1.setProperty(StyleGraphicPropertiesElement.FillImageWidth, "0cm");
		    p1.setProperty(StyleGraphicPropertiesElement.FillImageHeight, "0cm");
		    p1.setProperty(StyleGraphicPropertiesElement.Repeat, "repeat");
		    p1.setProperty(StyleGraphicPropertiesElement.TextareaHorizontalAlign, "center");
		    p1.setProperty(StyleGraphicPropertiesElement.TextareaVerticalAlign, "middle");
		    p1.setProperty(StyleGraphicPropertiesElement.AutoGrowHeight, "false");
		    p1.setProperty(StyleGraphicPropertiesElement.FitToSize, "false");
		    p1.setProperty(StyleGraphicPropertiesElement.MinHeight, "0cm");
		    p1.setProperty(StyleGraphicPropertiesElement.MinWidth, "0cm");
		    p1.setProperty(StyleGraphicPropertiesElement.PaddingTop, "0.131cm");
		    p1.setProperty(StyleGraphicPropertiesElement.PaddingBottom, "0.131cm");
		    p1.setProperty(StyleGraphicPropertiesElement.PaddingLeft, "0.25cm");
		    p1.setProperty(StyleGraphicPropertiesElement.PaddingRight, "0.25m");
		    p1.setProperty(StyleGraphicPropertiesElement.WrapOption, "wrap");
		    p1.setProperty(StyleGraphicPropertiesElement.DrawShadow, "visible");
		    p1.setProperty(StyleGraphicPropertiesElement.ShadowOffsetX, "0.3cm");
		    p1.setProperty(StyleGraphicPropertiesElement.ShadowOffsetY, "0.3cm");
		    p1.setProperty(StyleGraphicPropertiesElement.ShadowColor, "#868686");
		    p1.setProperty(StyleGraphicPropertiesElement.ShadowOpacity, "100%");
		    p1.setProperty(StyleGraphicPropertiesElement.RunThrough, "forground");
		    p1.setProperty(StyleGraphicPropertiesElement.VerticalPos, "from-top");
		    p1.setProperty(StyleGraphicPropertiesElement.VerticalRel, "paragraph-content");
		    p1.setProperty(StyleGraphicPropertiesElement.HorizontalPos, "from-left");
		    p1.setProperty(StyleGraphicPropertiesElement.HorizontalRel, "paragraph-content");
		    
		    /* chord fingering styles */
		    
			sChordFrame = styles.newStyle("CPT_5f_Chord_5f_Frame",OdfStyleFamily.Graphic);
		    sChordFrame.setStyleDisplayNameAttribute("CPT_Chord_Frame");
		    sChordFrame.setStyleParentStyleNameAttribute("Graphics");
		    sChordFrame.setProperty(StyleGraphicPropertiesElement.Width, "2.4cm");
		    sChordFrame.setProperty(StyleGraphicPropertiesElement.Height, "2.4cm");
		    sChordFrame.setProperty(StyleGraphicPropertiesElement.AnchorType, "as-char");
		    sChordFrame.setProperty(StyleGraphicPropertiesElement.FlowWithText, "true");
		   // style:wrap="none" fo:background-color="transparent" style:background-transparency="100%" style:shadow="none"
		    
		    p1 = odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.Graphic);
		    p1.setStyleNameAttribute("chordFingering");
		    p1.setStyleParentStyleNameAttribute("CPT_5f_Chord_5f_Frame");
		    p1.setProperty(StyleGraphicPropertiesElement.VerticalPos, "middle");
		    p1.setProperty(StyleGraphicPropertiesElement.VerticalRel, "baseline");
		    p1.setProperty(StyleGraphicPropertiesElement.HorizontalPos, "from-left");
		    p1.setProperty(StyleGraphicPropertiesElement.HorizontalRel, "paragraph");
		    p1.setProperty(StyleGraphicPropertiesElement.MarginBottom, "0.1cm");
		    p1.setProperty(StyleGraphicPropertiesElement.MarginTop, "0.1cm");
		    p1.setProperty(StyleGraphicPropertiesElement.MarginLeft, "0.1cm");
		    p1.setProperty(StyleGraphicPropertiesElement.MarginRight, "0.1cm");
		    p1.setProperty(StyleGraphicPropertiesElement.Padding, "0cm");
		    p1.setProperty(StyleGraphicPropertiesElement.Border, "none");
//		    style:mirror="none" fo:clip="rect(0cm, 0cm, 0cm, 0cm)" draw:luminance="0%" draw:contrast="0%" draw:red="0%" draw:green="0%" draw:blue="0%" draw:gamma="100%" draw:color-inversion="false" draw:image-opacity="100%" draw:color-mode="standard"/>
		    
		    p1=odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.Paragraph);
		    p1.setStyleParentStyleNameAttribute("Caption");
		    p1.setStyleNameAttribute("chordCaption");
		    p1.setProperty(StyleParagraphPropertiesElement.MarginTop, "0cm");
		    p1.setProperty(StyleParagraphPropertiesElement.MarginBottom, "0.2cm");
		    p1.setProperty(StyleParagraphPropertiesElement.TextAlign, "center");
		    p1.setProperty(StyleTextPropertiesElement.FontWeight, "bold");
		    
		    p1 = odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.Graphic);
		    p1.setStyleNameAttribute("chordFret");
		    p1.setProperty(StyleGraphicPropertiesElement.RunThrough, "foreground");
		    p1.setProperty(StyleGraphicPropertiesElement.Wrap, "run-through");
		    p1.setProperty(StyleGraphicPropertiesElement.NumberWrappedParagraphs, "no-limit");
		    p1.setProperty(StyleGraphicPropertiesElement.VerticalPos, "from-top");
		    p1.setProperty(StyleGraphicPropertiesElement.VerticalRel, "paragraph");
		    p1.setProperty(StyleGraphicPropertiesElement.HorizontalPos, "frem-left");
		    p1.setProperty(StyleGraphicPropertiesElement.HorizontalRel, "paragraph");
		    p1.setProperty(StyleGraphicPropertiesElement.Padding, "0.025cm");
		    p1.setProperty(StyleGraphicPropertiesElement.Stroke, "solid");
		    p1.setProperty(StyleGraphicPropertiesElement.StrokeWidth, "0.053cm");
		    p1.setProperty(StyleGraphicPropertiesElement.StrokeColor, "#d00028");
		    p1.setProperty(StyleGraphicPropertiesElement.MarkerStartWidth, "0.432cm");
		    p1.setProperty(StyleGraphicPropertiesElement.MarkerEndWidth, "0.432cm");
		    p1.setProperty(StyleGraphicPropertiesElement.TextareaHorizontalAlign, "center");
		    p1.setProperty(StyleGraphicPropertiesElement.TextareaVerticalAlign, "middle");
 
		    p1 = odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.Graphic);
		    p1.setStyleNameAttribute("chordString");
		    p1.setProperty(StyleGraphicPropertiesElement.RunThrough, "foreground");
		    p1.setProperty(StyleGraphicPropertiesElement.Wrap, "run-through");
		    p1.setProperty(StyleGraphicPropertiesElement.NumberWrappedParagraphs, "no-limit");
		    p1.setProperty(StyleGraphicPropertiesElement.VerticalPos, "from-top");
		    p1.setProperty(StyleGraphicPropertiesElement.VerticalRel, "paragraph");
		    p1.setProperty(StyleGraphicPropertiesElement.HorizontalPos, "frem-left");
		    p1.setProperty(StyleGraphicPropertiesElement.HorizontalRel, "paragraph");
		    p1.setProperty(StyleGraphicPropertiesElement.Padding, "0.025cm");
		    p1.setProperty(StyleGraphicPropertiesElement.Stroke, "solid");
		    p1.setProperty(StyleGraphicPropertiesElement.StrokeWidth, "0.025cm");
		    p1.setProperty(StyleGraphicPropertiesElement.StrokeColor, "#2800d0");
		    p1.setProperty(StyleGraphicPropertiesElement.MarkerStartWidth, "0.432cm");
		    p1.setProperty(StyleGraphicPropertiesElement.MarkerEndWidth, "0.432cm");
		    p1.setProperty(StyleGraphicPropertiesElement.TextareaHorizontalAlign, "center");
		    p1.setProperty(StyleGraphicPropertiesElement.TextareaVerticalAlign, "middle");
 
		    p1 = odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.TableColumn);
		    p1.setStyleNameAttribute("songCol");
		    p1.newStyleTableColumnPropertiesElement().setStyleColumnWidthAttribute("14.6cm");
		    
		    p1 = odt.getContentDom().getAutomaticStyles().newStyle(OdfStyleFamily.TableColumn);
		    p1.setStyleNameAttribute("chordCol");
		    p1.newStyleTableColumnPropertiesElement().setStyleColumnWidthAttribute(oVoicingSize+"cm");
		    
		    DrawFillImageElement fi = odt.getDocumentStyles().newDrawFillImageElement("Bitmape_20_2", "Pictures/title_page.png", "simple");
		    fi.setDrawDisplayNameAttribute("Bitmape 2");
		    fi.setXlinkShowAttribute("embed");
		    fi.setXlinkActuateAttribute("onLoad");
		}
		
		class ODTSong {

			TableTableElement table, tableSong;
			TableTableColumnElement columnSong, columnChords;
			TableTableRowElement row, rowSong;
			TableTableCellElement cell, cellSong, cellChords;
			
			public ODTSong(boolean first, Item item) {
				// create heading
			    TextHElement heSong = sectionSongs.newTextHElement(1);
			    if(first) heSong.setStyleName("PX");
			    else heSong.setDocumentStyle(sTitle);
			    heSong.newTextBookmarkStartElement("title-"+item.ref);
			    heSong.newTextNode(item.title);
			    heSong.newTextBookmarkEndElement("title-"+item.ref);
			    heSong = sectionSongs.newTextHElement(2);
			    heSong.setDocumentStyle(sArtist);
			    heSong.newTextBookmarkStartElement("artist-"+item.ref);
			    heSong.newTextNode(item.artist);
			    heSong.newTextBookmarkEndElement("artist-"+item.ref);
			    
			    //create song table: one row for every line
			    table = sectionSongs.newTableTableElement();
			    columnSong = table.newTableTableColumnElement();
			    columnSong.setStyleName("songCol");
				if(oVoicingLoc==1) {
					chordList.clear();
				    columnChords = table.newTableTableColumnElement();
					columnChords.setStyleName("chordCol");
				}
				row = table.newTableTableRowElement();
				cell = row.newTableTableCellElement(0.5, "string");
				if(oVoicingLoc==1) {
					cellChords = row.newTableTableCellElement(0.5, "string");
				}
				tableSong = cell.newTableTableElement();

			}
			
			private void clearRow(){
				rowSong = null;
			}
			
			private void makeRow(OdfStyle style){
				rowSong = tableSong.newTableTableRowElement();
				rowSong.setProperty(StyleTableRowPropertiesElement.UseOptimalRowHeight, "true");
				cellSong = rowSong.newTableTableCellElement(1.0, "string");
			    if(style!=null) cellSong.setDocumentStyle(style);
			}
			
			private void newSongLine(){
			}
			
		}
		

		private void writeODT(TextDocument odt, Item item, boolean first) throws Exception{

			incomment = false;
			inchorus = false;
			intab = false;
			
			ODTSong odtSong = new ODTSong(first,item);
			odtSong.newSongLine();

		    DrawTextBoxElement tb = null;
		    TextPElement p = null;
		    TextPElement cmtPara = null;
		    
		    Vector<ModelToken> tokens = item.song.parseText();
		    
			for ( int i = 0; i < tokens.size(); i++) {
				ModelToken token = tokens.get(i);
				switch(token.name) {
					case "C":
					case "CI":
					case "CB":
						incomment = true;
						if(token.name.equals("C")) odtSong.makeRow(sComment);
						else if(token.name.equals("CI")) odtSong.makeRow(sCommentItalic);
						else if(token.name.equals("CB")) odtSong.makeRow(sCommentBox);
						cmtPara=odtSong.cellSong.newTextPElement();
					    break;
					case "/C":
						incomment=false;
						odtSong.clearRow();
						break;
					case "SOC":
						inchorus=true;
						odtSong.makeRow(sChorus);
					    break;
					case "EOC":
						inchorus = false;
						odtSong.clearRow();
					    break;
					case "SOT":
						intab = true;
						break;
					case "EOT":
						intab = false;
						break;
					case "DEFINE":
						chordList.insert(Chord.transpose(token.value.split(" ")[1], 0),token.value,true);
						break;
					case "NL":
						if(odtSong.rowSong==null) odtSong.makeRow(null);
			        	p=odtSong.cellSong.newTextPElement();
						break;
					case "CHORD":
						String crd = token.value;
						if (chordList.get(crd)==null) chordList.add(crd,null,false);
						if(incomment){
							cmtPara.newTextNode(crd.trim());
						} else  {
			        		DrawFrameElement df=p.newDrawFrameElement();
				            df.setDrawStyleNameAttribute("fr1");
				            df.setDrawNameAttribute("Frame"+(++zIx));
				            df.setTextAnchorTypeAttribute("as-char");
				            df.setSvgYAttribute("0cm");
				            FoMinWidthAttribute a = new FoMinWidthAttribute(odt.getContentDom());
				            a.setValue("0.041cm");
				            df.setOdfAttribute(a);
				            df.setDrawZIndexAttribute(zIx);
				            tb = df.newDrawTextBoxElement();
				            tb.setAttribute("fo:min-height", "0cm");
				        	if(!intab || crd.length()>0){
				        		TextPElement pe=tb.newTextPElement();
				        		pe.setDocumentStyle(inchorus ? ( intab ? sChordsChorusTab : sChordsChorus) : ( intab ? sChordsTab : sChords));
					            pe.setTextContent(crd+" ");
				        	}
						}
						break;
					case "TEXT":
						String txt=token.value;
						if(incomment){
							cmtPara.newTextNode(txt);
						} else {
							TextPElement pe=tb.newTextPElement();
							pe.setDocumentStyle(inchorus ? ( intab ? sLyricsChorusTab : sLyricsChorus) : ( intab ? sLyricsTab : sLyrics));
							pe.setTextContent(txt.replaceAll("\\G ", "\u00a0"));
						}
						break;
					default:
				}
			}
			
			if(oVoicingLoc==1) {
				chordListToODT(chordList, odt,odtSong.cellChords,item.ref);
			}
		}

		class Item {
			ModelSong song;
		    String artist;
		    String title;
		    String lines[];
		    int ref;
		    public Item(ModelSong song, String artist, String title, String lines[]) {
		    	this.song = song;
		        this.artist=artist;
		        this.title=title;
		        this.lines=lines;
		        this.ref=cnt;
		        cnt++;
		    }
		}
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			 if (e.getValueIsAdjusting() == false) {
				 
	            if (lCols.getSelectedIndex() == -1) {
	            //No selection: disable delete, up, and down buttons.
	                upButton.setEnabled(false);
	                downButton.setEnabled(false);
	 
	            } else if (lCols.getSelectedIndices().length > 1) {
	            //Multiple selection: disable up and down buttons.
	                upButton.setEnabled(false);
	                downButton.setEnabled(false);
	 
	            } else {
	            //Single selection: permit all operations.
	                upButton.setEnabled(true);
	                downButton.setEnabled(true);
	            }
	        }
		 }	
		
	/** A listener shared by the text field and add button. */
	class OKButtonListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	UdoFileDialog chooser = new UdoFileDialog("OpenOffice Writer Document (*.odt)", "odt");
	    	String last = (String) listener.getModel().getOptions().get("odt.lastfile");
	    	if(last!=null) chooser.setSelectedFile(new File(last));
	        int rc = chooser.showDialog(null, "Export");
	        if (rc == UdoFileDialog.APPROVE_OPTION) {
	        	OdtExporter.inst.run(chooser.getSelectedFile().getAbsolutePath(), OdtExporter.inst.songBookTitle.getText());
	        	OdtExporter.inst.setVisible(false);
	        }
	    }
	}
	
	/** A listener shared by the text field and add button. */
	class CancelButtonListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	OdtExporter.inst.setVisible(false);
	    }
	}
	
	//Listen for clicks on the up and down arrow buttons.
	class UpDownListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	        //This method can be called only when
	        //there's a valid selection,
	        //so go ahead and move the list item.
	        int moveMe = lCols.getSelectedIndex();
	
	        if (e.getActionCommand().equals(upString)) {
	        //UP ARROW BUTTON
	            if (moveMe != 0) {    
	            //not already at top
	                swap(moveMe, moveMe-1);
	                lCols.setSelectedIndex(moveMe-1);
	                lCols.ensureIndexIsVisible(moveMe-1);
	            }
	        } else {
	        //DOWN ARROW BUTTON
	            if (moveMe != lm.getSize()-1) {
	            //not already at bottom
	                swap(moveMe, moveMe+1);
	                lCols.setSelectedIndex(moveMe+1);
	                lCols.ensureIndexIsVisible(moveMe+1);
	            }
	        }
	    }
	}
	
	//Swap two elements in the list.
	private void swap(int a, int b) {
		ModelSong colA = lm.getElementAt(a);
		ModelSong colB = lm.getElementAt(b);
	    lm.set(a, colB);
	    lm.set(b, colA);
	}
	
	/**
	 * setText
	 *
	 * updates the HTML element "chords" with the chords in the list
	 * 
	 * @param chordList list of chords to be rendered
	 */
	public void chordListToODT(ChordList chordList, TextDocument odt, TableTableCellElement cell, int song) {
		TextPElement para = cell.newTextPElement();
		int n=0;
		SortedSet<String> keys = new java.util.TreeSet<String>(chordList.keySet());
		for (String key : keys) { 
			chordToODT(chordList.get(key), odt, para, song, n);
			n++;
		}
	}
	
	
	public void chordToODT(Chord chord, TextDocument odt, TextPElement para, int song, int n){
		
		if (chord.getVoicing()>chord.getDefinitionList().size()-1) return;
		String def = chord.getDefinitionList().get(chord.getVoicing()).replaceAll("\\{|\\}", "");
		String fret[] = def.split(" ");
	
		boolean drawNut = (fret[3].equals("1"));
		
		String xml=	"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
					"<svg xmlns:dc=\"http://purl.org/dc/elements/1.1/\" "
						+ "xmlns:cc=\"http://web.resource.org/cc/\" "
						+ "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" "
						+ "xmlns:svg=\"http://www.w3.org/2000/svg\" "
						+ "xmlns=\"http://www.w3.org/2000/svg\" "
						+ "width=\"80\" "
						+ "height=\"80\">"
						+ "<g id=\"frets\" style=\"stroke:#000000;stroke-width:1;stroke-linecap:round;stroke-opacity:1\">";
		if(fret[0].equals("udefine")) 
							xml+="<rect x=\"0\" y=\"0\" width=\"80\" height=\"80\" fill=\"#eeee88\" stroke=\"none\" />";
		for (int f=0; f<6; f++){
							xml+="<path d=\"M 5 "+(f+2)+"5 H 55\" ";
							if(f==0 && drawNut) xml+=" style=\"stroke-width:2;\" ";
							xml+="/>";
		}
		xml+=				"</g>"
						+ "<g id=\"frets\" style=\"stroke:#000000;stroke-width:1;stroke-linecap:round;stroke-opacity:1\">";
		for (int s=0; s<6; s++){
							xml+="<path d=\"M "+s+"5 25 V 75\"/>";
		}
		xml+=				"</g>"
						+ "<g id=\"annotaions\" style=\"font-size:8; font-family:sans-serif; text-anchor:middle;\">"
							+ "<text x=\"30\" y=\"12\" style=\"font-size:12; font-weight:bold; font-style:italic; fill:"+ oChordTitleColor+";\">"+fret[1]+"</text>";
		if(!drawNut){
							int f=new Integer(fret[3]);
							xml+="<text style=\"fill:"+oChordFretColor+";\" x=\"67\" y=\"38\">"+f+(f>3?"th":new String[]{"","st","nd","rd"}[f])+"</text>";
		}
		for(int s=0; s<6; s++){
			if (fret[s+5].equals("X") || fret[s+5].equals("0")) {
							xml+="<text x=\""+s+"5\" y=\"22\">"+fret[s+5]+"</text>";			
			}
		}
		xml+=				"</g>"
						+ "<g id=\"fingers\" style=\"stroke:#000000;stroke-width:1;fill:#ff0000;\">";
		for(int s=0; s<6; s++){
			if (!fret[s+5].equals("X") &&  !fret[s+5].equals("0")) {
					int f=new Integer(fret[s+5]);
					xml+="<circle cx=\""+s+"5\" cy=\""+(f+2)+"2\" r=\"3\"/>";			
			}
		}
		xml+=				"</g>";
		xml+=		"</svg>";
		String pic = "Pictures/"+def.replace("/", "_")+".svg";
	    odt.getPackage().insert(xml.getBytes(), pic, "image/svg+xml");
	
	    /* make frame */ 
		DrawFrameElement fingering = para.newDrawFrameElement();
		fingering.setDocumentStyle(sChordFrame);
		fingering.setDrawNameAttribute("fingering-"+song+"-"+n);
		fingering.setTextAnchorTypeAttribute("as-char");
		fingering.setStyleRelWidthAttribute("100%");
		fingering.setStyleRelHeightAttribute("scale");
		fingering.setDrawZIndexAttribute(0);
			
		/* insert the SVG */
		DrawImageElement img = fingering.newDrawImageElement();
		img.setXlinkHrefAttribute(pic);
		img.setXlinkShowAttribute("embed");
		img.setXlinkTypeAttribute("simple");
		img.setXlinkActuateAttribute("onLoad");
	}

	public void setListener(Control listener) {
		this.listener=listener;
	}

	public DefaultListModel<ModelSong> getModel(){
		return lm;
	}

	public void setOptions(Properties props) {
		
		oTitleDefault = props.get(OdtOptionsContentDlg.OPT_TITLE_DEFAULT).toString();
		songBookTitle.setText(oTitleDefault);
		
		oVoicingSize = props.get(OdtOptionsContentDlg.OPT_VOICINGS_SIZE).toString();
		oVoicingLoc =new Integer(props.get(OdtOptionsContentDlg.OPT_VOICINGS_LOC).toString());
		oVoicingCols =new Integer(props.get(OdtOptionsContentDlg.OPT_VOICINGS_COLS).toString());

		oMarginLeft = props.get(OdtOptionsContentDlg.OPT_MARGIN_LEFT).toString();
		oMarginRight = props.get(OdtOptionsContentDlg.OPT_MARGIN_RIGHT).toString();
		oMarginTop = props.get(OdtOptionsContentDlg.OPT_MARGIN_TOP).toString();
		oMarginBottom = props.get(OdtOptionsContentDlg.OPT_MARGIN_BOTTOM).toString();
		
		oChordTitleColor = props.get(OdtOptionsColorDlg.OPT_CHORD_TITLE_COLOR).toString();
		oChordFretColor = props.get(OdtOptionsColorDlg.OPT_CHORD_FRET_COLOR).toString();
		
	}
	
}
