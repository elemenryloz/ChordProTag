package cpt.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import cpt.view.browser.options.content.BrowserOptionsContentDlg;
import view.action.UdoMvcViewAction;
import model.UdoMvcModel;
import model.UdoMvcModelEvent;

public class Model extends UdoMvcModel {

	public enum LibraryEvent {LOAD,SAVE};
	
	private Vector<ModelSong> lib = new Vector<ModelSong>();

	public void openDir(File dir){
		lib.clear();
		open( new String[] {dir.getPath()},new String[] {".chopro",".crd"} );
		getOptions().setProperty("lib.directories", dir.getPath());
		getOptions().setProperty("lib.lastdir", dir.getPath());
	}
	
	public void addDir(File dir){
		String dirs[] = getOptions().getProperty("lib.directories").split(",");
		String o="";
		boolean found=false;
		for(String d : dirs){
			o+=d+",";
			if(d.equals(dir.getPath())) found=true;
		}
		if(!found){
			open( new String[] {dir.getPath()},new String[] {".chopro",".crd"} );
			getOptions().setProperty("lib.directories", o+dir.getPath());
		}
		getOptions().setProperty("lib.lastdir", dir.getPath());
	}
	
	public Vector<ModelSong> getLib() {
		return lib;
	}

	public void setOptionsDefaults() {
		String dirs = getOptions().getProperty(BrowserOptionsContentDlg.OPT_DIRS);
		if(dirs!=null){
			String d[] = dirs.split(",");
			lib.clear();
			open(d,new String[] {".chopro",".crd"});
		}
	}
	
	/**
	 * Scans all directories specified in the path for chordpro files. All files are read and kept in storage
	 * @param path path of directory to scan
	 * @param ext file extensions to look for
	 */
	public void open(final String[] path, String [] ext) {
		
		// scan directories recursively
		for(int p=0; p<path.length; p++){
			File file=new File(path[p]);
			if (file.isDirectory()) listDir(file,ext);
		}
		
		// read all files
		final UdoMvcModelEvent e= new UdoMvcModelEvent(lib);
		e.setType(UdoMvcModelEvent.LOAD);
        new Thread(){  
            public void run(){  
            	final int size = lib.size();
        		for(int i=0; i<size; i++){
            		if(size<200) {
            			try{Thread.sleep(1000/size);}catch(InterruptedException ie){ie.printStackTrace();}
            		}
            		final int j=i;
            		SwingUtilities.invokeLater(new Runnable(){  
            			public void run(){  
            				ModelSong f=lib.elementAt(j);
            				f.read();
            				e.setData(new Integer((100*j)/size));
            				fireEvent(e);
            			}  
            		});  
            	}
        		SwingUtilities.invokeLater(new Runnable(){  
        			public void run(){
        				e.setData(null);
        				fireEvent(e);
        			}  
        		});  
            }
        }.start();  
	}
	
	/**
	 * Scans a single directory recursively for chordpro files and adds those to the library
	 * @param dir directory to scan
	 * @param ext file extensions to look for
	 */
	public void listDir(File dir, String[] ext){
		
		// get all files
		File[] files = dir.listFiles();
		if (files!=null) {
			
			// inspect each file
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					
					// scan directory recursively
					listDir(files[i],ext);
				}
				else {
					
					// check all specified extensions
					String n=files[i].getName();
					for(int e=0; e<ext.length; e++){
						if(n.endsWith(ext[e])){
							
							// read chordpro file and add to vector
							ModelSong mf=(new ModelSong(files[i]));
							lib.add(mf);
							break;
						}
					}
				}
			}
		}
	}
	
	public String[] getUsedKeys(int[] items){
    	HashMap<String, Boolean> tag=new HashMap<String, Boolean>();
    	if(items==null){
    		int size=lib.size();
    		items=new int[size];
    		for (int i=0; i<size; i++){
    			items[i]=i;
    		};
    	}
    	for (int i: items){
			ModelSong item = lib.get(i);
			for (String t: item.meta.keySet()) {
				if(tag.get(t)==null) tag.put((String)t, true);
			}
    	}
    	List<String> sortedKeys=new ArrayList<String>(tag.keySet());
   		Collections.sort(sortedKeys);
		return (String[])sortedKeys.toArray(new String[0]);
	}
	
	
	
	public int countChanged() {
    	int size = 0;
    	for (ModelSong item: lib){
    		if (item.isModified()) size++;
    	};
    	return size;
	}
	
	
	
	public void saveChanged() {
		
		final UdoMvcModelEvent e= new UdoMvcModelEvent(lib);
		e.setType(UdoMvcModelEvent.SAVE);
    	final int ss = countChanged();
        new Thread(){  
            public void run(){
            	for (final ModelSong item: lib){
            		if(!item.isModified()) continue;
            		if(ss<200) {
            			try{Thread.sleep(200/ss);}catch(InterruptedException ie){ie.printStackTrace();}
            		}
            		SwingUtilities.invokeLater(new Runnable(){  
            			public void run(){  
            				e.setData(item);
            				fireEvent(e);
    	    		        item.write();
            			}  
            		});  
            	}
				e.setData(null);
				fireEvent(e);
            }
        }.start();  
	}

	public ModelSong addFile( File file) {
		
		ModelSong i = find(file);
		if(i==null) {
			i=new ModelSong(file);
			i.read();
			lib.add(i);
		} else {
			i.read();
		}
		i.setAdded(true);
		return i;
	}

	public void delFile( File file) {
		
		ModelSong i = find(file);
		if(i!=null) {
			i.delete();
			lib.remove(i);
		} 
	}

	public void delFiles( int[] items) {
		Arrays.sort(items);
		for(int i=items.length-1; i>=0; i--){
			delFile(new File(lib.get(i).meta.get("_file_path")));
		}
	}

	private ModelSong find(File file) {
		for(ModelSong i: lib){
			if (i.meta.get("_file_path").equals(file.getAbsolutePath())) return i;
		}
		return null;
	}
	
	

}
