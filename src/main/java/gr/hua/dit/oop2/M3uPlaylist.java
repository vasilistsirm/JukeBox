package gr.hua.dit.oop2;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;

//Class for parsing a m3u list.
public class M3uPlaylist {
  ArrayList<String> mp3;
  private String column[] = {"File","Title","Artists","Genre","Album"};//Headers of Jtable

  private Metadata metadata = new Metadata();
  private JTable table;//Jtable for display metadata
  private JFrame frame;



  public File M3uPlaylist(File f) {

    mp3 = new ArrayList<>();//We create an ArrayList with Strings
    frame = new JFrame(); //Create new Jframe object for display a Jtable with metadata from mp3 files
    frame.setSize(600,600);//set size

    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
      String line;
      while ((line = br.readLine()) != null) {
        //check for extended m3u file which contains comments and the path is absolute
        if (line.startsWith("#") == false && line.startsWith("\\")) {
          addMP3(line.replace("\\","/"));
        }
        if(line.startsWith("\\")==false && line.startsWith("#") == false){//if is relative path
          addMP3(f.getParent()+ "/" +line.replace("\\","/"));
        }

      }
    } catch (IOException ex) {
      System.out.println("error is reading the file");
    }

    DefaultTableModel defaultTableModel  = new DefaultTableModel(getColumn(), 0);//set in Jtable the headers and the size of rows

    for(int i=0; i<getMp3().size(); i++){
      try {
        InputStream input = new FileInputStream(getMp3().get(i));
        Parser parser = new AutoDetectParser();
        ContentHandler contentHandler = new BodyContentHandler();
        ParseContext context = new ParseContext();

        parser.parse(input, contentHandler, metadata, context);//Save metadata in metadata attribute

      } catch (IOException | SAXException | TikaException e) {
        throw new RuntimeException(e);
      }
      defaultTableModel.insertRow(i,new Object[]{songsNames(getMp3().get(i)), metadata.get("title"), metadata.get("xmpDM:artist"), metadata.get("xmpDM:genre"), metadata.get("xmpDM:album")});
    }

    table = new JTable(defaultTableModel);
    table.setBounds(300,700,30,100);
    JScrollPane js = new JScrollPane(table);
    frame.add(js);
    frame.setVisible(true);
    frame.setSize(600,600);//set size in frame
    frame.setTitle("Metadata from mp3 files");//set title in frame

    return f;
  }

  private void addMP3(String line) {

    if (line.endsWith(".mp3")) {//We check if line ends with .mp3 because we want to add only mp3 files in our list
      mp3.add(line);  //We insert in the ArrayList only the lines that contains mp3 files
    }else{//Print in output files which are not mp3
      System.out.println("This file is not mp3: " + line);
    }

  }


  //Method for take only the name of the song
  public String songsNames(String args){
    //We find where is the last index of char '/'
    //return the name of the song
    return (String) args.subSequence(args.lastIndexOf("/")+1,args.length());
  }

  //getters
  public String[] getColumn() {
    return column;
  }

  public ArrayList<String> getMp3() {
    return mp3;
  }

  public JFrame getFrame() {
    return frame;
  }
}


