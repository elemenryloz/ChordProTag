package cpt.control;

import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import view.UdoMvcViewEvent;
import view.action.UdoMvcViewAction;
import view.filedialog.UdoFileDialog;
import view.menu.UdoMvcViewMenuBar;
import controller.UdoMvcController;
import cpt.model.ModelSong;
import cpt.model.Model;
import cpt.view.View;
import cpt.view.browser.Browser;


/**
 * Controller of the chordpro tagger application
 * @author Udo
 *
 */
public class Control extends UdoMvcController {

	private final static String OPT_PROPERTIES_FILE = System.getProperty("user.home")+"/.chordProTagger.properties";
	
	Model model;
	View view;
	
	public Control(View view) {
		this.view=view;
		model=new Model();
		model.setController(this);
		try {
			model.addOptions(OPT_PROPERTIES_FILE);
		} catch (Exception e) {
		}
		addView(view);
		view.setListener(this);
        view.setOptionsDefaults(model.getOptions());
        model.setOptionsDefaults();

		view.setVisible(true);
		
		model.open(model.getOptions().getProperty("lib.directories").split(","),new String[] {".chopro",".crd"});
	}
	
	@Override
	public void handleViewEvent(UdoMvcViewEvent e) {
		if(e.type==UdoMvcViewEvent.ACTION){
			
			String cmd=(String)e.getData();
			
			// File
			if(cmd.equals(Browser.MENU_FILE_NEW)) {
				File file = view.newFile();
		        if (file!=null) {
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			        	bw.write(model.getOptions().getProperty("lib.new.text"));
			        	bw.close();
			    		ModelSong i = model.addFile(file);
			    		view.addFile(i);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
		        }
			} else if(cmd.equals(Browser.MENU_FILE_EDIT)) {
                final int items[] = view.getSelectedItems();
               	view.editItems(items);
			} else if(cmd.equals(Browser.MENU_FILE_DELETE)) {
                final int items[] = view.getSelectedItems();
            	if(view.deleteItems(items)){
            		model.delFiles(items);
            	}
			} else if(cmd.equals(Browser.MENU_FILE_OPEN)) {
				File dir=view.openDirectory(model.getOptions().getProperty("lib.lastdir"));
				if(dir!=null){
					model.openDir(dir);
				}
			} else if(cmd.equals(Browser.MENU_FILE_ADD)) {
				File dir=view.openDirectory(model.getOptions().getProperty("lib.lastdir"));
				if(dir!=null){
					model.addDir(dir);
				}
			} else if(cmd.equals(Browser.MENU_FILE_SAVE_SELECTED)) {
				
				final int items[] = view.getSelectedItems();
            	final int size = items.length;
		        new Thread(){  
		            public void run(){  
		            	for (final int row: items){
		            		if(size<200) {
		            			try{Thread.sleep(200/size);}catch(InterruptedException ie){ie.printStackTrace();}
		            		}
		            		SwingUtilities.invokeLater(new Runnable(){  
		            			public void run(){  
		            				ModelSong item = view.getBrowserTable().getModel().getLib().get(row);
		            				view.addStatus("saving "+item.meta.get("_file_path"), true);  
		    	    		        item.write();
		            			}  
		            		});  
		            	}
		            	view.update();
		            }
		        }.start(); 
			} else if(cmd.equals(Browser.MENU_FILE_SAVE_CHANGED)) {
				view.getBrowserTable().editingCanceled(null);
		    	model.saveChanged();
			} else if(cmd.equals(Browser.MENU_FILE_EXPORT_ODT)) {
				view.exportODT();
			} else if(cmd.equals(Browser.MENU_FILE_EXIT)) {
		    	System.exit(0);
			}  
			
			// Edit
			else if(cmd.equals(Browser.MENU_EDIT_UNDO)) {
			} else if(cmd.equals(Browser.MENU_EDIT_REDO)) {
			} else if(cmd.equals(Browser.MENU_EDIT_CUT)) {
			} else if(cmd.equals(Browser.MENU_EDIT_COPY)) {
			} else if(cmd.equals(Browser.MENU_EDIT_PASTE)) {
			} else if(cmd.equals(Browser.MENU_EDIT_SELECT_ALL)) {
				view.getBrowserTable().selectAll();
			}  else if(cmd.equals(Browser.MENU_EDIT_SELECT_NONE)) {
				view.getBrowserTable().clearSelection();
			}  
			
			// Tools
			else if(cmd.equals(Browser.MENU_TOOLS_RENAME)) {
				view.renameFiles();
			} else if(cmd.equals(Browser.MENU_TOOLS_TAG_TO_TAG)) {
				view.changeTags();
			} else if(cmd.equals(Browser.MENU_TOOLS_OPTIONS)) {
				view.showOptionsDlg();
			}  
			
			// Help
			else if(cmd.equals(Browser.MENU_HELP_HELP)) {
				view.showHelpDlg();
			} else if(cmd.equals(Browser.MENU_HELP_ABOUT)) {
				view.showAboutDlg();
			} 			
		} else if(e.type==UdoMvcViewEvent.OPTIONS){
			model.setOptions((Properties) e.source);
		} else if(e.type==UdoMvcViewEvent.CLOSING){
			try {
				int changed = model.countChanged();
				if( changed == 0 
				|| JOptionPane.showConfirmDialog(view, changed+" unsafed song(s).\nSure to cancel?","Warning",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
					Rectangle r=(Rectangle)e.getData();
					model.getOptions().put(View.OPT_BOUNDS, r.x+","+r.y+","+r.width+","+r.height);
					model.saveOptions();
					System.exit(0);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void updateMenuStatus() {
		UdoMvcViewMenuBar menubar = view.getMenubar();
		menubar.setAllMenuItemsEnabled(false);
		for(UdoMvcViewAction m: view.getBrowser().alwaysEnabled) {
			m.setEnabled(true);
		}
		
		int s=view.getBrowserTable().getRowCount();
		if (s>0) {
			for(UdoMvcViewAction m: view.getBrowser().whenLoadedEnabled) {
				m.setEnabled(true);
			}
		}		
		
		s=view.getBrowserTable().getSelectedRows().length;
		if (s==0) {
			for(UdoMvcViewAction m: view.getBrowser().zeroSelectedEnabled) {
				m.setEnabled(true);
			}
		} else if (s==1) {
			for(UdoMvcViewAction m: view.getBrowser().oneSelectedEnabled) {
				m.setEnabled(true);
			}
		} else {
			for(UdoMvcViewAction m: view.getBrowser().multipleSelectedEnabled) {
				m.setEnabled(true);
			}
		}
	}
	
	public Model getModel(){
		return model;
	}

	public View getView(){
		return view;
	}

}




