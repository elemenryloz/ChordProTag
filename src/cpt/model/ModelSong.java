package cpt.model;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.odftoolkit.odfdom.dom.attribute.fo.FoMinWidthAttribute;
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawTextBoxElement;
import org.odftoolkit.odfdom.dom.element.style.StyleParagraphPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTableRowPropertiesElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;

/**
 * Represents a chordpro file
 * @author Udo
 *
 */
public class ModelSong {

	private static final long serialVersionUID = 1L;

	public HashMap<String, String> meta;
	public String allText;
	public String noMeta;
	private File file;

	public String tmpstr="";

	private boolean modified;
	private boolean open;
	private boolean added;

	/**
	 * Constructor from an existing file. Reads the file and tries to find artist and title.
	 * @param file the file to be used
	 */
	public ModelSong(File file) {
		this.meta = new HashMap<String, String>();
		this.file=file;
	}

	public void read( ) {
		try {
			
			// read the file
			BufferedReader br = new BufferedReader(new FileReader(file));

			this.meta = new HashMap<String, String>();
			String path = file.getAbsolutePath();
			meta.put("_file_path", path);
			String name = file.getName();
			meta.put("_file_name_ex", name);
			meta.put("_file_name", name.replaceFirst("[.][^.]+$", ""));
			meta.put("_file_extension", name.replaceAll("[^.]+[.]", ""));

			
			FileTime time = (FileTime) Files.getAttribute(Paths.get(path), "basic:creationTime");
			
			String ts = time.toString().split("\\.")[0].replaceAll("T"," ").replaceAll("Z","");
			meta.put("_file_created", ts);
			time = (FileTime) Files.getAttribute(Paths.get(path), "basic:lastModifiedTime");
			ts = time.toString().split("\\.")[0].replaceAll("T"," ").replaceAll("Z","");
			meta.put("_file_modified", ts);
			time = (FileTime) Files.getAttribute(Paths.get(path), "basic:lastAccessTime");
			ts = time.toString().split("\\.")[0].replaceAll("T"," ").replaceAll("Z","");
			meta.put("_file_accessed", ts);
			Long size = (Long) Files.getAttribute(Paths.get(path), "basic:size");
			meta.put("_file_size", size.toString());
			
			File parent = file.getParentFile();
			if(parent!=null){
				meta.put("_file_directory_path", parent.getAbsolutePath());
				meta.put("_file_directory", parent.getName());
			}
			
			allText = "";
			String line;
			while( (line = br.readLine()) != null ) {
				allText+=line+"\n";
			}
			br.close();
			
			parseMeta();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void parseMeta(){
		boolean hasMeta = allText.indexOf("#{start_of_meta")!=-1;
		boolean isInMeta = false;
		noMeta = "";
		for( String input: allText.split("\n") ) {
			boolean cptitle = input.startsWith("{t:") || input.startsWith("{title:");
			boolean cpsubtitle = input.startsWith("{st:") || input.startsWith("{subtitle:");
			boolean cptag = cptitle || cpsubtitle;
			if (input.startsWith("#{start_of_meta}")) {
				isInMeta = true;
			} else if (input.startsWith("#{end_of_meta}")) {
				isInMeta = false;
			} else if (cptag || isInMeta || !hasMeta) {
				if (input.startsWith("#{") || cptag) {
					int p=input.indexOf('}');
					if(p!=-1) input=input.substring(0,p);
					int p1=input.indexOf(':');
					if(p1!=-1){
						String key = cptag ? "§"+ (cptitle ? "title" : "subtitle") : input.substring(2,p1); 
						String ns = input.substring(p1+1);
						String os = meta.get(key);
						if(os!=null) ns=os+"\n"+ns;
						meta.put(key, ns);
					}
				} else {
					noMeta+=input+"\n";
				}
			}  else {
				noMeta+=input+"\n";
			}
		}
	}

	public Vector<ModelToken> parseText() throws ModelSongException{
		String ex = "";
		Vector<ModelToken> tokens = new Vector<>();
		String lines[] = this.noMeta.split("\n");
		for ( int i = 0; i < lines.length; i++) {
			String line = lines[i].replaceAll ("\\s*$","");
			if (line.length()==0 || line.startsWith("{c}")) line = " ";
			if (line.startsWith("#")) continue;
			if (line.startsWith("{")) {
				String tag = "";
				String val = "";
				int p = line.indexOf(":");
				if(p==-1) {
					tag=line.substring(1);
					if(tag.endsWith("}")) tag=tag.substring(0,tag.length()-1);
				} else {
					int p2 = line.indexOf("}",p);
					if(p2==-1) p2=line.length();
					tag=line.substring(1,p);
					val=line.substring(p+1,p2);
				}
				
				switch(tag) {
					case "title":
					case "t":
						tokens.add(new ModelToken("T",val));
						break;
					case "st":
					case "subtitle":
						tokens.add(new ModelToken("ST",val));
						break;
					case "c":
					case "comment":
					case "ci":
					case "comment_italic":
					case "cb":
					case "comment_box":
						if (tag.equals("c") || tag.equals("comment")) tokens.add(new ModelToken("C",""));
						else if (tag.equals("ci") || tag.equals("comment_italic")) tokens.add(new ModelToken("CI",""));
						else if (tag.equals("cb") || tag.equals("comment_box")) tokens.add(new ModelToken("CB",""));
						parseLine(val,tokens);
						tokens.add(new ModelToken("/C",""));
						break;
					case "soc":
					case "start_of_chorus":
						tokens.add(new ModelToken("SOC",""));
					    break;
					case "eoc":
					case "end_of_chorus":
						tokens.add(new ModelToken("EOC",""));
					    break;
					case "sot":
					case "start_of_tab":
						tokens.add(new ModelToken("SOT",""));
					    break;
					case "eot":
					case "end_of_tab":
						tokens.add(new ModelToken("EOT",""));
					    break;
					default:
						if (tag.length()>7 && tag.substring(0, 7).equals("define ")) {
							// check syntax 
							String def[] = tag.split(" ");
							String chordName = "";
							String baseFret = "";
							String frets[] = null;
							if (def.length > 1 && def[0].equals("define")) chordName = def[1];
							if (def.length > 3 && def[2].equals("base-fret")) baseFret = def[3];
							if (def.length >= 11 && def[4].equals("frets")) frets = Arrays.copyOfRange(def,5, 11);
							if (chordName.equals("") || baseFret.equals("") || frets==null || frets.length != 6) {
								ex = ex + "invalid define in " + meta.get("title")+" ("+meta.get("artist")+") line "+i+": " + line + "\n";
							} else {
								tokens.add(new ModelToken("DEFINE",tag));
							}
						} else {
							ex = ex + "unsupported directive in "+meta.get("title")+" ("+meta.get("artist")+") line " + i+": " + line + "\n";
						}
				}
			} else {
				tokens.add(new ModelToken("NL",""));
				parseLine(line,tokens);
				tokens.add(new ModelToken("EOL",""));
			}
		}
		if(ex.length()>1) {
			ModelSongException e=new ModelSongException(ex);
			e.setTokens(tokens);
			throw e;
		}
		return tokens;
	}
	
	private void parseLine(String val, Vector<ModelToken> tokens){
		String[] c = val.split("\\[");
		for ( int i1=0; i1<c.length; i1++) {
			// ignore leading [
			if (i1 == 0 && c[i1].length()==0) continue;
			String cc[] = c[i1].split("\\]");
			String crd = null, txt = null;
			if (cc.length>1) {
				crd = cc[0];
				txt = cc[1];
			} else if (c[i1].endsWith("]")) {
				crd = cc[0];
				txt = " ";
			} else {
				crd = "";
				txt = cc[0];
			}
			tokens.add(new ModelToken("CHORD",crd.trim()));
			tokens.add(new ModelToken("TEXT",txt));
		}
	}
	
	public void write() {
		try {
			
			List<String> sortedKeys=new ArrayList<String>(meta.keySet());
			Collections.sort(sortedKeys);
			BufferedWriter br = new BufferedWriter(new FileWriter(file));
			br.write("#{start_of_meta}\n");
			for( Object k: sortedKeys.toArray()){
				String kk=(String)k;
				if(kk.startsWith("_")) continue;
				if(kk.startsWith("§")) continue;
				for(String t: meta.get(kk).split("\n")) br.write("#{"+kk+":"+t+"}\n");
			}
			br.write("#{end_of_meta}\n");
			for( Object k: sortedKeys.toArray()){
				String kk=(String)k;
				if(kk.startsWith("§")) {
					for(String t: meta.get(kk).split("\n")) br.write("{"+kk.substring(1)+":"+ t +"}\n");
				}
			}
			br.write(noMeta);
			br.close();
			setModified(false);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void rename(Path target){
		Path source = file.toPath();
		if(!target.equals(source)){
			try {
				if(target.getParent()!=null && !Files.exists(target.getParent())) Files.createDirectory(target.getParent());
				Path nf = Files.move(source, target);
				file = nf.toFile();
				read();
			} catch (FileAlreadyExistsException e) {
        		JOptionPane.showMessageDialog(null, "rename of "+file.toPath().getFileName()+ " failed:\n"+target+" already exists");
        		meta.put("_file_name",source.getFileName().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void delete(){
		try {
			Files.delete(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isAdded() {
		return added;
	}
	
	public void setAdded(boolean added) {
		this.added = added;
	}

	public void setText(String text) {
		allText=text;
		for (Object m: meta.keySet().toArray()){
			String k=(String)m;
			if(!k.startsWith("_")) meta.remove(k);
		}
		parseMeta();
	}

}
