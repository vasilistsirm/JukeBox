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
import java.util.*;


public class Audio {

  //encapsulation with private declaration
  private List<String> Audio;
  
  private String column[] = {"File","Title","Artists","Genre","Album"};//Headers of Jtable

  private Metadata metadata = new Metadata();
  private JTable table;//Jtable for display metadata


  //default constructor
  public Audio(){

  }


  //getters
  public List<String> getAudio() {
    return Audio;
  }

  public String[] getColumn() {
    return column;
  }



  public File ListOfMp3(File args){
    //Create a List
     Audio = new ArrayList<>();

    //give the path of folder
    File f = new File(args.toURI());
    File[] listOfFiles = f.listFiles();

    JFrame frame = new JFrame(); //Create new Jframe object for display a Jtable with metadata from mp3 files
    frame.setSize(600,600);//set size

    //We run all list to find files which are mp3
    for (File file : listOfFiles) {
      //if ends with .mp3 then add file in Audio list
      if (file.getName().endsWith(".mp3")) {
        Audio.add(file.getName());
      }else{//Print in output files which are not mp3
        System.out.println("This file is not mp3: " + file.getName());
      }
    }

    DefaultTableModel defaultTableModel  = new DefaultTableModel(getColumn(), 0);//set in Jtable the headers and the size of rows

    for(int i=0; i<getAudio().size(); i++){
      try {
        InputStream input = new FileInputStream(args+"/"+getAudio().get(i));
        Parser parser = new AutoDetectParser();
        ContentHandler contentHandler = new BodyContentHandler();
        ParseContext context = new ParseContext();

        parser.parse(input, contentHandler, metadata, context);//Save metadata from InputStream in metadata attribute

      } catch (IOException | SAXException | TikaException e) {
        throw new RuntimeException(e);
      }
      defaultTableModel.insertRow(i,new Object[]{getAudio().get(i), metadata.get("title"), metadata.get("xmpDM:artist"), metadata.get("xmpDM:genre"), metadata.get("xmpDM:album")});
    }

    table = new JTable(defaultTableModel);
    table.setBounds(300,700,30,100);
    JScrollPane js = new JScrollPane(table);
    frame.add(js);//add table in frame
    frame.setVisible(true);
    frame.setSize(600,600);//set size in frame
    frame.setTitle("Metadata from mp3 files");//set title in frame

    return f;
  }


  }





