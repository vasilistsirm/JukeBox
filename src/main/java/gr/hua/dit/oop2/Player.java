package gr.hua.dit.oop2;

import gr.hua.dit.oop2.musicplayer.*;


import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;



public class Player extends JFrame implements ActionListener {
  //Encapsulation with keyword private
  private JButton play,stop,resume,selectMp3,close,randomSong,closeProgram,selectFolder,nextSong,selectM3u;//Buttons
  private gr.hua.dit.oop2.musicplayer.Player player = PlayerFactory.getPlayer();

  private JList list ;
  private InputStream song ;
  private JFileChooser fileChooser;//For choose files.
  private JLabel label;//for labels
  private File myFile=null;
  private File myFolder=null;

  long totalLength;
  long pause;
  int isFolderOrM3u;//1 for folder, 0 for M3u file
  Object item;


  private Audio audio = new Audio();//Class Audio.

  private M3uPlaylist m3u = new M3uPlaylist();//Class M3uPlaylist

  //Constructor
  public Player(){
    //Initializing the buttons and label
    play = new JButton("Play");
    stop = new JButton("Stop");
    resume = new JButton("Resume");
    selectMp3 = new JButton("Select Mp3");
    close = new JButton("Close");
    randomSong = new JButton("Select next random song");
    nextSong = new JButton("Select next song");
    closeProgram = new JButton("Terminate Program");
    selectFolder = new JButton("Select Folder");
    selectM3u = new JButton("Select M3u file");
    label = new JLabel("Please select a folder or a M3u file");

    //set bounds on the buttons and label
    label.setBounds(100,10,300,30);
    selectFolder.setBounds(100,50,300,30);
    selectM3u.setBounds(100,100,300,30);
    play.setBounds(100,150,300,30);
    stop.setBounds(100,200,300,30);
    resume.setBounds(100,250,300,30);
    selectMp3.setBounds(100,300,300,30);
    close.setBounds(100,350,300,30);
    randomSong.setBounds(100,400,300,30);
    nextSong.setBounds(100,450,300,30);
    closeProgram.setBounds(100,500,300,30);

    play.addActionListener(this);
    stop.addActionListener(this);
    resume.addActionListener(this);
    selectMp3.addActionListener(this);
    close.addActionListener(this);
    randomSong.addActionListener(this);
    closeProgram.addActionListener(this);
    selectFolder.addActionListener(this);
    nextSong.addActionListener(this);
    selectM3u.addActionListener(this);

    //add all in Jframe
    add(selectMp3);
    add(selectFolder);
    add(play);
    add(stop);
    add(resume);
    add(close);
    add(randomSong);
    add(nextSong);
    add(closeProgram);
    add(selectM3u);
    add(label);
    setLayout(null);

  }

  //getters and setters
  public JButton getPlay() {
    return play;
  }

  public void setPlay(JButton play) {
    this.play = play;
  }

  public JButton getResume() {
    return resume;
  }

  public void setResume(JButton resume) {
    this.resume = resume;
  }

  public JButton getStop() {
    return stop;
  }

  public void setStop(JButton stop) {
    this.stop = stop;
  }

  public JLabel getLabel() {
    return label;
  }

  public void setLabel(JLabel label) {
    this.label = label;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    label.setText("Please select a mp3 file if you want to listen music");//label text for select a mp3 file

    if(actionEvent.getSource()==selectFolder){ //Select the folder that contains mp3 files
      fileChooser=new JFileChooser();
      fileChooser.setDialogTitle("Select folder");
      fileChooser.setAcceptAllFileFilterUsed(false);
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
        myFolder=fileChooser.getSelectedFile();
      }
      isFolderOrM3u = 1;
    }
    if(actionEvent.getSource()==selectM3u) { //Select the m3u file that contains mp3 files
      fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Select M3u file");
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fileChooser.setFileFilter(new FileNameExtensionFilter("M3u files","m3u"));
      if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        myFolder = fileChooser.getSelectedFile();
      }
      isFolderOrM3u = 0;
    }
    if(actionEvent.getSource()==selectMp3){//Select mp3 files from this folder(myFolder)
      if(isFolderOrM3u == 1) {//From directory
        fileChooser=new JFileChooser();
        fileChooser.setCurrentDirectory(new File(audio.ListOfMp3(myFolder).toURI()));

        fileChooser.setDialogTitle("Select Mp3");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3 files","mp3"));
        if(fileChooser.showOpenDialog(selectMp3)==JFileChooser.APPROVE_OPTION){
          myFile=fileChooser.getSelectedFile();
        }
      }else if(isFolderOrM3u == 0){//From m3u file
        JFrame frame = new JFrame(); //Create new Jframe object for display songs from m3u playlist
        frame.setSize(800,800);
        DefaultListModel model = new DefaultListModel();

        m3u.M3uPlaylist(myFolder);

        for(int i=0; i<m3u.mp3.size(); i++){
          model.addElement(m3u.songsNames(m3u.mp3.get(i)));
        }

        list = new JList(model);//add the default list model in Jlist

        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setBackground(Color.LIGHT_GRAY);//set gray color in Jlist

        frame.add(list);
        frame.setVisible(true);
        frame.setTitle("Mp3 files from M3u file");

        list.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
              JList target = (JList) e.getSource();
              int index = target.locationToIndex(e.getPoint());
              if (index >= 0) {
                item = target.getModel().getElementAt(index);
                JOptionPane.showMessageDialog(null, item.toString());

                for(int i=0; i<m3u.mp3.size(); i++){
                  if(item.toString().equals(m3u.songsNames(m3u.mp3.get(i)))){
                    myFile = new File(m3u.mp3.get(i));
                  }
                }

              }
            }
            frame.setVisible(false); //set visible false
            frame.dispose();//destroy this Jframe object

            m3u.getFrame().dispose();//Also, destroy Jframe object which contains metadata details
          }
        });
      }

    }

    if(actionEvent.getSource()==play){

      //code for play button
      try {
        if(player.getStatus()== gr.hua.dit.oop2.musicplayer.Player.Status.PLAYING){
          player.stop();
        }
        song = new FileInputStream(myFile);
        player.startPlaying(song);
        System.out.println("Player just begin");

      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      } catch (PlayerException e) {
        throw new RuntimeException(e);
      }
      label.setText("Now we are listening "+m3u.songsNames(myFile.toString()));//label text

    }
    if(actionEvent.getSource()==randomSong) {//Button for play random mp3 files
      if (isFolderOrM3u == 1) {
        //We create a table with Integers and set size audio.getAudio().size().
        Integer[] arr = new Integer[audio.getAudio().size()];
        //Insert in table numbers from 0 to arr.length that is equal with audio.getAudio().size().
        for (int j = 0; j < arr.length; j++) {
          arr[j] = j;
        }
        //The shuffle() method is used to work by randomly reorders the specified list elements using a default randomness.
        Collections.shuffle(Arrays.asList(arr));
        //Now, the numbers in array is mixed up, so we can play random mp3 files from a specific list without duplicates.
        String path;
        for (int i = 0; i < arr.length; i++) {
          path = myFolder + "/" + audio.getAudio().get(arr[i]);

          if (!path.equals(myFile.toString())) { //if path is not equal with song that already play
            player.stop();
            System.out.println("Player just stopped");

            try {
              song = new FileInputStream(path);
              player.startPlaying(song);
              System.out.println("Player just begin, because you selected to listen random song from playlist");

              label.setText("Now we are listening "+m3u.songsNames(path));//label
              myFile = new File(path);
              break;
            } catch (FileNotFoundException | PlayerException e) {
              throw new RuntimeException(e);
            }
          }


        }
      }else if(isFolderOrM3u==0){
        //We create a table with Integers and set size m3u.mp3.size()].
        Integer[] arr = new Integer[m3u.mp3.size()];
        //Insert in table numbers from 0 to arr.length that is equal with m3u.mp3.size()].
        for (int j = 0; j < arr.length; j++) {
          arr[j] = j;
        }
        //The shuffle() method is used to work by randomly reorders the specified list elements using a default randomness.
        Collections.shuffle(Arrays.asList(arr));
        //Now, the numbers in array is mixed up, so we can play random mp3 files from a specific list without duplicates.
        String path;
        for (int i = 0; i < arr.length; i++) {
          path =  m3u.mp3.get(arr[i]);

          if (!path.equals(myFile.toString())) {
            try {
              player.stop();
              song = new FileInputStream(path);
              player.startPlaying(song);

              label.setText("Now we are listening "+m3u.songsNames(path));//label
              myFile = new File(path);
              break;
            } catch (FileNotFoundException | PlayerException e) {
              throw new RuntimeException(e);
            }
          }

        }
      }
    }
    if(actionEvent.getSource()==nextSong){//Play the next song,after the song that just ended
      String path;
     if(isFolderOrM3u==1) {
       for (int i = 0; i < audio.getAudio().size(); i++) {
         path = myFolder + "/" + audio.getAudio().get(i);

         if (path.equals(myFile.toString()) && i < audio.getAudio().size() - 1) {//If path is the same with selected file
           player.stop();//First stop player
           System.out.println("Player just stopped");

           try {
             song = new FileInputStream(myFolder + "/" + audio.getAudio().get(i + 1));//Play next song from list
             player.startPlaying(song);
             System.out.println("Player just begin because you selected to go to the next song");

             myFile = new File(myFolder + "/" + audio.getAudio().get(i + 1));//update myFile to next song
             label.setText("Now we are listening "+m3u.songsNames(myFile.toString()));//label
             break;
           } catch (FileNotFoundException | PlayerException e) {
             throw new RuntimeException(e);
           }
         }

         if (i == audio.getAudio().size() - 1) { // If there is no other song in this list
           label.setText("There is no other song in the list");
           System.out.println("There is no other song in the list");
         }

       }
     }else if(isFolderOrM3u==0){
       for (int i = 0; i < m3u.mp3.size(); i++) {
         path = m3u.mp3.get(i);
         if (path.equals(myFile.toString()) && i < m3u.mp3.size() - 1) {//If path is the same with selected file
           player.stop();//First stop player
           System.out.println("Player just stopped");

           try {
             song = new FileInputStream( m3u.mp3.get(i+1));//Play next song from list
             player.startPlaying(song);
             System.out.println("Player just begin because you selected to go to the next song");

             myFile = new File(m3u.mp3.get(i+1));//update myFile to next song
             label.setText("Now we are listening "+m3u.songsNames(myFile.toString()));//label
             break;
           } catch (FileNotFoundException | PlayerException e) {
             throw new RuntimeException(e);
           }
         }

         if (i == m3u.mp3.size() - 1) { // If there is no other song in this list
           label.setText("There is no other song in the list");//label
           System.out.println("There is no other song in the list");
         }

       }
     }
    }

    if(actionEvent.getSource()==stop){//Stop Player
      //code for stop button
      if(player!=null){
        try {
          pause = song.available();
          player.stop();
          System.out.println("Player just stopped");//for output in console
          label.setText("You stopped "+m3u.songsNames(myFile.toString())+" song");//label text for stop button
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }


    }
    if(actionEvent.getSource()==close){//Close PLayer
      player.close();
    }
    if(actionEvent.getSource()==resume){
      try {
        //code for resume button
        song=new FileInputStream(myFile);
        totalLength = song.available();
        player=PlayerFactory.getPlayer();

        song.skip(totalLength-pause);
        player.startPlaying(song);;

        System.out.println("Player just resume");//for output in console
        label.setText("Resume again "+m3u.songsNames(myFile.toString())+" song");//label text for resume button
      } catch (FileNotFoundException | PlayerException e) {
        e.printStackTrace();

      } catch (IOException e) {
        e.printStackTrace();
      }

    }

    if(actionEvent.getSource()==closeProgram){//terminate program
      System.exit(0);
    }

  }

}
