package cpt.view.help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import view.UdoMvcView;

public class Help extends JPanel  {
	private static final long serialVersionUID = 1L;
	
    private JEditorPane menu;
    private JEditorPane page;
    private JLabel title;

	public Help(UdoMvcView view) {
		super();

    	URL url=this.getClass().getResource("/cpt/view/images/tagger_title.png");
    	title=new JLabel(new ImageIcon(url),SwingConstants.LEFT);
    	title.setBackground(Color.WHITE);
    	title.setOpaque(true);
        
	     // add some styles to the html
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.importStyleSheet(getClass().getResource("/cpt/view/help/tagger.css"));
        
        menu=new JEditorPane();
        menu.setEditable(false);
        menu.setEditorKit(kit);
        try {
        	url=this.getClass().getResource("/cpt/view/help/tagger_menu.html");
			menu.setPage(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
        menu.setMinimumSize(menu.getPreferredSize());
        menu.setMaximumSize(menu.getPreferredSize());
        menu.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                   loadPage(page, e.getURL());
                }
            }
        });
        
        page=new JEditorPane();
        page.setEditable(false);
        page.setEditorKit(kit);
        loadPage(page, getClass().getResource("/cpt/view/help/tagger_start.html"));
        
        setLayout(new BorderLayout());
        add(title,BorderLayout.NORTH);
        add(menu,BorderLayout.WEST);
        add(new JScrollPane(page),BorderLayout.CENTER);
	}
	
	private void loadPage(JEditorPane page, URL url){
        try {
			page.setPage(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
