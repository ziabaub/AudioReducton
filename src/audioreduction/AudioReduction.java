/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioreduction;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Math.round;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import static jdk.nashorn.internal.objects.NativeMath.random;

/**
 *
 * @author ziad
 */
public class AudioReduction extends Application {

    MediaPlayer MediaP;
    static File FSource;
    String Name, Location;
    Media MusicFile;
    WavHeader wavHeader;
    final int Header_size = 44;
    byte[] buf = new byte[Header_size];
    byte[] ba, wa;
    double[] startup;
    WavHeaderReader wavHeaderReader;
    InputStream inputStream;
    Canvas canvas,canvas2;
    Slider slstrength,slstrength1;
    Label lbStrenght,lbStrenght1;
    int OldSquValue =16,OldNoiValue=3;
    //.................................

    //.................................
    // panelChartFreqSound = new PanelChart();
    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        init(primaryStage);
    }

    private void init(Stage primaryStage) {
        Pane MainP = new Pane();
        Scene MainScene = new Scene(MainP, 1100, 650);

        HBox HControleBox = new HBox(15);
        VBox VControleBox = new VBox(10);

        VControleBox.setAlignment(Pos.BASELINE_LEFT);

        HControleBox.setPrefHeight(50);
        HControleBox.setPrefWidth(300);
        HControleBox.setAlignment(Pos.CENTER);
        HControleBox.setStyle("-fxpadding:10;" + "-fx-border-style:solid inside;"
                + "-fx-border-width: 1;" + "-fx-border-insets:5;"
                + "-fx-border-radius:5;" + "-fx-border-color: green;");
        

        Button Starts, Pauses, Stops, BtnBrowser;
        Label LControl = new Label(" Control");
        BtnBrowser = new Button("Browse");
        Starts = new Button("Play");
        Pauses = new Button("Pause");
        Stops = new Button("Stop");
        
            Label lbType = new Label("Noise");
            lbStrenght = new Label("3");
            
            HBox hbStrenght = new HBox(10);
            hbStrenght.setLayoutY(290);
            hbStrenght.setLayoutX(230);
            hbStrenght.getChildren().addAll(lbType,lbStrenght);
            
            slstrength = new Slider(1, 16, 3);
            slstrength.setLayoutY(315);
            slstrength.setLayoutX(173);
            slstrength.setValue(3);
            slstrength.setShowTickLabels(true);
            slstrength.setOnMouseReleased((MouseEvent event) -> {
                lbStrenght.setText((int)slstrength.getValue()+"");
           });

            
            Label lbType1 = new Label("Squelch");
            lbStrenght1 = new Label("16");
            
            HBox hbStrenght1 = new HBox(10);
            hbStrenght1.setLayoutY(290);
            hbStrenght1.setLayoutX(230);
            hbStrenght1.getChildren().addAll(lbType1,lbStrenght1);
            
            slstrength1 = new Slider(1, 16, 16);
            slstrength1.setLayoutY(315);
            slstrength1.setLayoutX(173);
            slstrength1.setValue(16);
            slstrength1.setShowTickLabels(true);
            slstrength1.setOnMouseReleased((MouseEvent event) -> {
                lbStrenght1.setText((int)slstrength1.getValue()+"");
           });
            
            VBox SqAndNois = new VBox();
            VBox SqAndNois1 = new VBox();
            VBox SqAndNois2 = new VBox(1);
            
            SqAndNois.getChildren().addAll(hbStrenght,slstrength);
            SqAndNois1.getChildren().addAll(hbStrenght1,slstrength1);
            SqAndNois2.getChildren().addAll(SqAndNois,SqAndNois1);
            SqAndNois2.setLayoutX(300);
            SqAndNois2.setLayoutY(26);
            SqAndNois2.setStyle("-fxpadding:20;" + "-fx-border-style:solid inside;"
                + "-fx-border-width: 1;" + "-fx-border-insets:5;"
                + "-fx-border-radius:5;" + "-fx-border-color: green;");
            SqAndNois2.setAlignment(Pos.CENTER);
            
        
        Button Shows = new Button("Edit");
        Shows.setLayoutX(217);
        Shows.setLayoutY(80);
        Shows.setOnAction((ActionEvent even) -> {
            if (OldNoiValue!=(Integer.valueOf(lbStrenght.getText()))){
                OldNoiValue=(Integer.valueOf(lbStrenght.getText()));
                try {       
                    Applynoise();
                } catch (IOException ex) {
                    Logger.getLogger(AudioReduction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(OldSquValue!=(Integer.valueOf(lbStrenght1.getText()))){
                OldSquValue=(Integer.valueOf(lbStrenght1.getText()));
                try {       
                    ApplySquelsh();
                } catch (IOException ex) {
                    Logger.getLogger(AudioReduction.class.getName()).log(Level.SEVERE, null, ex);
                }
                 
            }else {
                if (wavHeader!=null){
                wavHeader.data1=Arrays.copyOf(startup, startup.length);
                try {
                    printWaveSpec();
                } catch (IOException ex) {
                    Logger.getLogger(AudioReduction.class.getName()).log(Level.SEVERE, null, ex);
                }
                    Location = "file:///" + FSource.getAbsolutePath();
            MusicFile = new Media(Location);
            MediaP = new MediaPlayer(MusicFile);
            MediaP.setVolume(0.10);
            }
            }

        });
        //chart
//******************************************************************************        
        CategoryAxis XAxis = new CategoryAxis();
        XAxis.setLabel("Msec");
        NumberAxis YAxis = new NumberAxis();
        XAxis.setLabel("Div");
        LineChart<String, Number> LineChart = new LineChart<>(XAxis, YAxis);
        LineChart.setTitle("Frequency");
        XYChart.Series<String, Number> data = new XYChart.Series<>();

        LineChart.getData().add(data);

        CategoryAxis XAxis1 = new CategoryAxis();
        XAxis1.setLabel("Msec");
        NumberAxis YAxis1 = new NumberAxis();
        XAxis1.setLabel("Div");
        LineChart<String, Number> LineChart1 = new LineChart<>(XAxis1, YAxis1);
        LineChart1.setTitle("Frequency1");
        XYChart.Series<String, Number> data1 = new XYChart.Series<>();

        LineChart1.getData().add(data1);

        canvas = new Canvas(1000, 150);
        canvas2 = new Canvas(1000, 150);
        
        VBox Can1 = new VBox();
        VBox Can2 = new VBox();
        
        VBox Can11 = new VBox(2);
        VBox Can22 = new VBox(2);
        
        Label can1 = new Label("Frequencies");
        Label can2 = new Label("FFT");
       
        Can1.getChildren().add(canvas);
        Can2.getChildren().add(canvas2);

        Can11.getChildren().addAll(can1,Can1);
        Can22.getChildren().addAll(can2,Can2);
        
        
                    Can1.setStyle("-fxpadding:20;" + "-fx-border-style:solid inside;"
                + "-fx-border-width: 1;" + "-fx-border-insets:5;"
                + "-fx-border-radius:5;" + "-fx-border-color: green;");
            SqAndNois2.setAlignment(Pos.CENTER);
            
                        Can2.setStyle("-fxpadding:20;" + "-fx-border-style:solid inside;"
                + "-fx-border-width: 1;" + "-fx-border-insets:5;"
                + "-fx-border-radius:5;" + "-fx-border-color: green;");
            SqAndNois2.setAlignment(Pos.CENTER);

        VBox ChartBox = new VBox(100);
        ChartBox.getChildren().addAll(Can11,Can22);
        ChartBox.setLayoutY(150);
        ChartBox.setPrefHeight(400);
        ChartBox.setPrefWidth(1000);

//******************************************************************************
//endchart
        VControleBox.getChildren().addAll(LControl, HControleBox);
        HControleBox.getChildren().addAll(Starts, Pauses, Stops, BtnBrowser);

        Starts.setOnAction((ActionEvent t) -> {
            MediaP.play();
        }); 

        Pauses.setOnAction((ActionEvent t) -> {
            MediaP.pause();
        });

        Stops.setOnAction((ActionEvent t) -> {
            MediaP.stop();
        });

        BtnBrowser.setOnAction((ActionEvent event) -> {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            FSource = chooser.getSelectedFile();
            Location = "file:///" + FSource.getAbsolutePath();
            MusicFile = new Media(Location);
            MediaP = new MediaPlayer(MusicFile);
            MediaP.setVolume(0.10);
            OldNoiValue= 3;
            OldSquValue=16;
            slstrength.setValue(3);
            lbStrenght.setText("3");
            slstrength1.setValue(16);
            lbStrenght1.setText("16");
            LoadVav();
            
            try {
                checkaudio();
                startup = Arrays.copyOf(wavHeader.data1, wavHeader.data1.length);
                Applynoise();
            } catch (IOException ex) {
                Logger.getLogger(AudioReduction.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        MainP.getChildren().addAll(VControleBox, ChartBox,SqAndNois2,Shows);
        primaryStage.setTitle("Audio Reduction");
        primaryStage.setScene(MainScene);
        primaryStage.show();

    }

    public void checkaudio() throws FileNotFoundException, IOException {
      long ux;
      String s;
      int len ;
      byte[] other = wavHeader.getother();
      byte[] other2 = ByteBuffer.allocate(4).putInt(wavHeader.getDataIdSize()).array();ReverseArr(other2);
      len = wavHeader.getother().length+4;
        if (wavHeader == null) {
            JOptionPane.showMessageDialog(null, "Load File !!");
        } else {
            if (wavHeader.getAudioFormat() != 1) {
                JOptionPane.showMessageDialog(null, "Can't open the file  ");
            } else {
                wavHeader.setFileNmae(FSource.getName());
                if (wavHeader.getSubChunk1Size() != 16) {
                    wavHeader.setSubChunk1Size(16);
                }
                if (wavHeader.getNumChannels() == 1) {
                    if (wavHeader.getBitsPerSample() == 8) {
                        ba = new byte[wavHeader.getDataIdSize()];
                        wavHeaderReader.inputStream.read(ba);
                        wavHeader.data1 = new double[wavHeader.getDataIdSize()+len];
                        for (int i =0 ;i<len-4 ;i++){
                          wavHeader.data1[i] = other[i];
                        }
                        for (int i =len-4 ;i<len ;i++){
                          s = getbinaryMethod(other2[i-(len-4)]);  
                          ux = Integer.parseInt(s, 2);  
                          wavHeader.data1[i] = ux;
                        }
                        for (int i = len; i < wavHeader.data1.length; i++) {
                            s = getbinaryMethod(ba[i-len]);  
                            ux = Integer.parseInt(s, 2);
                            wavHeader.data1[i]=ux;
                        }
                        
                        ba = new byte[0];
                    } else {
                        wa = new byte[wavHeader.getDataIdSize()/2];
                        wavHeaderReader.inputStream.read(wa);
                        wavHeader.data1 = new double[wavHeader.getDataIdSize()/2+len];
                        
                        for (int i =0 ;i<len-4 ;i++){
                          wavHeader.data1[i] = other[i];
                        }
                        for (int i =len-4 ;i<len ;i++){
                          s = getbinaryMethod(other2[i-(len-4)]);  
                          ux = Integer.parseInt(s, 2);  
                          wavHeader.data1[i] = ux;
                        }
                        
                        for (int i = len; i < wavHeader.data1.length; i++) {
                            s = getbinaryMethod(wa[i-len]);  
                            ux = Integer.parseInt(s, 2);
                            wavHeader.data1[i]=ux;
                        }

                        wa = new byte[0];
                    }
                } else { //8 bytes
                    if (wavHeader.getBitsPerSample() == 8) {
                        ba = new byte[wavHeader.getDataIdSize()];
                        wavHeaderReader.inputStream.read(ba);
                        wavHeader.data1 = new double[wavHeader.getDataIdSize() / 2+len];
                        wavHeader.data2 = new double[wavHeader.getDataIdSize() / 2+len];
                        
                        for (int i =0 ;i<len-4 ;i++){
                          wavHeader.data1[i] = other[i];
                        }
                        for (int i =len-4 ;i<len ;i++){
                          s = getbinaryMethod(other2[i-(len-4)]);  
                          ux = Integer.parseInt(s, 2);  
                          wavHeader.data1[i] = ux;
                        }
                        
                        for (int i =0 ;i<len-4 ;i++){
                          wavHeader.data2[i] = other[i];
                        }
                        for (int i =len-4 ;i<len ;i++){
                          s = getbinaryMethod(other2[i-(len-4)]);  
                          ux = Integer.parseInt(s, 2);  
                          wavHeader.data2[i] = ux;
                        }
                        
                        for (int i = len; i < wavHeader.data1.length; i++) {
                            s = getbinaryMethod(ba[i*2-len]);  
                            ux = Integer.parseInt(s, 2);
                            wavHeader.data1[i]=ux;
                            
                            s = getbinaryMethod(ba[(i*2+1)-len]);  
                            ux = Integer.parseInt(s, 2);
                            wavHeader.data2[i]=ux;
                        }

                        
                        ba = new byte[0];
                    } else {//16 bytes
                        wa = new byte[wavHeader.getDataIdSize() / 2];
                        wavHeaderReader.inputStream.read(wa);
                        wavHeader.data1 = new double[wavHeader.getDataIdSize() / 4+len];
                        wavHeader.data2 = new double[wavHeader.getDataIdSize() / 4+len];
                        
                        for (int i =0 ;i<len-4 ;i++){
                          wavHeader.data1[i] = other[i];
                        }
                        for (int i =len-4 ;i<len ;i++){
                          s = getbinaryMethod(other2[i-(len-4)]);  
                          ux = Integer.parseInt(s, 2);  
                          wavHeader.data1[i] = ux;
                        }
                        
                        for (int i =0 ;i<len-4 ;i++){
                          wavHeader.data2[i] = other[i];
                        }
                        for (int i =len-4 ;i<len ;i++){
                          s = getbinaryMethod(other2[i-(len-4)]);  
                          ux = Integer.parseInt(s, 2);  
                          wavHeader.data2[i] = ux;
                        }
                        
                        for (int i = 0; i < wavHeader.data1.length; i++) {
                            s = getbinaryMethod(wa[i*2-len]);  
                            ux = Integer.parseInt(s, 2);
                            wavHeader.data1[i]=ux;
                            
                            s = getbinaryMethod(wa[(i*2+1)-len]);  
                            ux = Integer.parseInt(s, 2);
                            wavHeader.data2[i]=ux;
                        }

                        wa = new byte[0];
                    }
                }
            }
        }

        printWaveSpec();
    }

    public void LoadVav() {

        try {
            wavHeaderReader = new WavHeaderReader(FSource.getPath());
            wavHeader = wavHeaderReader.read();
            System.out.println(wavHeader.toString());
        } catch (FileNotFoundException e) {
            System.out.println("Error: File " + FSource.getName() + " not found!");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void play() throws UnsupportedAudioFileException, IOException {
        File file = null;
        AudioInputStream AuioInStr = null;
        AuioInStr = AudioSystem.getAudioInputStream(file);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

    private void printWaveSpec() throws IOException {
        
      String s = getbinaryMethod2(ByteBuffer.allocate(4).putInt(wavHeader.getSampleRate()).array());  
      long ux = Integer.parseInt(s, 2);
      
      wavHeader.setSampleRate((int)ux);
      wavHeader.setByteRate((int)ux);


        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.ALICEBLUE);
        gc.fillRect(0, 0, 1000, 150);
        GraphicsContext gc1 = canvas2.getGraphicsContext2D();
        gc1.setFill(Color.ALICEBLUE);
        gc1.fillRect(0, 0, 1000, 150);
        
        int j, readngwindowswidth, pow,rpos;
        long cnt, max;
        double sc,x,y;
        //FourierTransform1 tfftbase =new FourierTransform1();
        //FourierTransform tfftbase =new FourierTransform();
        FFT tfftbase = new FFT();
        double[] psd;

        if (wavHeader.getBitsPerSample() == 8) {
            int l = ((int) wavHeader.data1[1]/70)-1;
            
            j = 177+(l*50);
            sc = 0.69140625;
        } else {
            j = 88;
            sc = 0.3;
        }
        int pre = 1;
        GraphicsContext context = canvas.getGraphicsContext2D();
        for (int i = 1; i < wavHeader.data1.length; i++) {
            if (wavHeader.getBitsPerSample() == 8) {
               x= i/50;
               y = round(j-(wavHeader.data1[i]*sc));
                context.strokeLine(x, y,x,y);
//               y = j-(wavHeader.data1[i])*sc;
//               context.strokeLine(x, y,x,y);
 
            } else {
               x= i/50;
               y = j-(wavHeader.data1[i])/256*sc;
                context.strokeLine(x, y,x,y);
//               y = j-(wavHeader.data1[i])/256*sc;
//               context.strokeLine(x, y,x,y);
            }
        }

      readngwindowswidth = 256;

      pow = 8;
      if (ux>1000){readngwindowswidth=512 ; pow =9;}
      if (ux>2000){readngwindowswidth=1024 ; pow =10;}
      if (ux>5000){readngwindowswidth=2048 ; pow =11;}
      if (ux>10000){readngwindowswidth=4096 ; pow =12;}
      if (ux>20000){readngwindowswidth=8192 ; pow =13;}
      if (ux>40000){readngwindowswidth=16384 ; pow =14;}
      
      max = wavHeader.data1.length/(readngwindowswidth/2)-1;// reading windows count
      psd = new double[readngwindowswidth/2];
      Arrays.fill(psd, 0);
      Complex[] fFFtComplBuf= new Complex[readngwindowswidth];
      rpos = 0;
      for (cnt = 1 ;cnt<=max;cnt++){
          for (int i = 0; i<readngwindowswidth;i++){
              if ((rpos+i)< wavHeader.data1.length){
                  fFFtComplBuf[i] = new Complex(wavHeader.data1[rpos+i], 0.0);    
              }else{ 
                  fFFtComplBuf[i] = new Complex(wavHeader.data1[wavHeader.data1.length-1], 0.0);
    
              }
          }   
            //  tfftbase.FFT(fFFtComplBuf, FourierTransform.Direction.Forward);
           // tfftbase.FFT(fFFtComplBuf, readngwindowswidth,pow,false);
             fFFtComplBuf=tfftbase.fft(fFFtComplBuf);
              for ( int i =1;i<readngwindowswidth/2-1;i++){
                  psd[i]+=  Math.sqrt(Math.pow(fFFtComplBuf[i].real(),2)+Math.pow(fFFtComplBuf[i].imag(),2));
              }
              rpos += readngwindowswidth/2;
          
      }
      
        GraphicsContext context2 = canvas2.getGraphicsContext2D();
        for (int i = 1; i < readngwindowswidth/2-1; i++) {   
               y =100-psd[i]*0.0001;
                context2.strokeLine(i, y,i,y);
        }
      

        
    }

   
    
    private byte[] doubleToByteArray ( final double i ) throws IOException {
     ByteArrayOutputStream bos = new ByteArrayOutputStream();
     DataOutputStream dos = new DataOutputStream(bos);
     dos.writeDouble(i);
     dos.flush();
     return bos.toByteArray();
    }
    
     
    public String getbinaryMethod2(byte [] bn) {
        String Binary = new String ();    
        StringBuilder sb = new StringBuilder("00000000");
    for(int i =0;i<bn.length;i++){ 
     if ((bn[i]!=0)&&(bn[i] != -1))   
     for (int bit = 0; bit < 8; bit++) {
         if (((bn[i] >> bit) & 1) > 0) {
             sb.setCharAt(7 - bit, '1');
         }
     }
     if(i==0){
          Binary=sb.toString();
          }else{
         if (!("00000000".equals(sb.toString())))
          Binary+=sb.toString();
     }  if ((bn[i]!=0)&&(bn[i]!=-1))
        sb = new StringBuilder("00000000");      
    }
    
     return Binary;
    }
    
    
        public String getbinaryMethod(byte bn) {
            
        StringBuilder sb = new StringBuilder("00000000");
     if ((bn!=0)&&(bn != -1))   
     for (int bit = 0; bit < 8; bit++) {
         if (((bn >> bit) & 1) > 0) {
             sb.setCharAt(7 - bit, '1');
         }
            
    }
     return sb.toString();
        }

    private void ApplySquelsh() throws IOException {
    long max;
    Double[] dsc1,dsc2;
    int i,j,i1,i2,readngwindowswidth,pow,rpos,cnt;
    //FourierTransform1  tfftbase= new FourierTransform1();
     //FourierTransform  tfftbase= new FourierTransform();
     FFT tfftbase = new FFT();
    Complex[] fFFtComplBuf;
    dsc2=new Double[wavHeader.data1.length]; 
     dsc1 = new Double[wavHeader.data1.length];
    readngwindowswidth=256;
      String s = getbinaryMethod2(ByteBuffer.allocate(4).putInt(wavHeader.getSampleRate()).array());  
      long ux = Integer.parseInt(s, 2);
      pow = 8;
      if (ux>1000){readngwindowswidth=512 ; pow =9;}
      if (ux>2000){readngwindowswidth=1024 ; pow =10;}
      if (ux>5000){readngwindowswidth=2048 ; pow =11;}
      if (ux>10000){readngwindowswidth=4096 ; pow =12;}
      if (ux>20000){readngwindowswidth=8192 ; pow =13;}
      if (ux>40000){readngwindowswidth=16384 ; pow =14;}
      if (wavHeader.getFileNmae()==null){
          JOptionPane.showMessageDialog(null, "File Not Open ");
      }
      else{
          max = wavHeader.data1.length/(readngwindowswidth/2)-1;  
 
              fFFtComplBuf = new Complex[readngwindowswidth];
              rpos = 0 ;
          for (cnt =1;cnt <=max ;cnt++){
                  for (i=0;i<readngwindowswidth ;i++){
              if ((rpos +i)<wavHeader.data1.length){
                  fFFtComplBuf[i] = new Complex(wavHeader.data1[rpos+i], 0.0);    
              }else{ 
                  fFFtComplBuf[i] = new Complex(wavHeader.data1[wavHeader.data1.length-1], 0.0);
    
              }
                  }
                  
                //tfftbase.FFT(fFFtComplBuf, readngwindowswidth,pow,false);
                //tfftbase.FFT(fFFtComplBuf, FourierTransform.Direction.Forward);
                fFFtComplBuf=tfftbase.fft(fFFtComplBuf);
                i1 = Integer.valueOf(lbStrenght1.getText())*10;
                i2 = readngwindowswidth-i1;
                
                for (i = i1 ;i<=i2 ;i++){
                   fFFtComplBuf[i] = new Complex(fFFtComplBuf[i].real()*0.001, fFFtComplBuf[i].imag()*0.001); 
                }
                //tfftbase.FFT(fFFtComplBuf, readngwindowswidth,pow,true);
                //tfftbase.FFT(fFFtComplBuf, FourierTransform.Direction.Backward);
                fFFtComplBuf=tfftbase.fft(fFFtComplBuf);
                if (cnt ==1 ){
                    for (i=0;i<(int) 3*readngwindowswidth/4+4;i++){
                        dsc1[i]=fFFtComplBuf[i].real()*0.0001;
                    }
                }else if (cnt==max){
                    for (i=(int) readngwindowswidth/4-4;i<readngwindowswidth;i++){
                        if ((rpos+i)<dsc1.length)
                        dsc1[rpos+i]=fFFtComplBuf[i].real()*0.0001;
                    }
                }else{ 
                    for (i=(int) readngwindowswidth/4-4;i<(int) 3*readngwindowswidth/4+4;i++){
                        dsc1[rpos+i]=fFFtComplBuf[i].real()*0.0001;
                    }
                }
                
             if (wavHeader.getNumChannels()==2){
                  
                 
                for (i=0;i<readngwindowswidth ;i++){
              if ((rpos +i)<wavHeader.data2.length){
                  fFFtComplBuf[i] = new Complex(wavHeader.data2[rpos+i], 0.0);    
              }else{  
                  fFFtComplBuf[i] = new Complex(wavHeader.data2[wavHeader.data2.length-1], 0.0);
    
              }
                  }
                  
               // tfftbase.FFT(fFFtComplBuf, readngwindowswidth,pow,false);
                //tfftbase.FFT(fFFtComplBuf, FourierTransform.Direction.Forward);
                fFFtComplBuf=tfftbase.fft(fFFtComplBuf);
                i1 = Integer.valueOf(lbStrenght1.getText())*10;
                i2 = readngwindowswidth-i1;
                
                for (i = i1 ;i<=i2 ;i++){
                   fFFtComplBuf[i] = new Complex(fFFtComplBuf[i].real()*0.0001, fFFtComplBuf[i].imag()*0.0001); 
                }
              //  tfftbase.FFT(fFFtComplBuf, readngwindowswidth,pow,true);
               // tfftbase.FFT(fFFtComplBuf, FourierTransform.Direction.Forward);
               fFFtComplBuf=tfftbase.fft(fFFtComplBuf);
               if (cnt ==1 ){
                    for (i=0;i<(int) 3*readngwindowswidth/4+4;i++){
                        dsc2[i]=fFFtComplBuf[i].real()*0.0001;
                    }
                }else if (cnt==max){
                    for (i=(int) readngwindowswidth/4-4;i<readngwindowswidth;i++){
                        if ((rpos+i)<dsc2.length)
                        dsc2[rpos+i]=fFFtComplBuf[i].real()*0.0001;
                    }
                }else{
                    for (i=(int) readngwindowswidth/4-4;i<(int) 3*readngwindowswidth/4+4;i++){
                        dsc2[rpos+i]=fFFtComplBuf[i].real()*0.0001;
                    }
                }    
                }
                
             rpos+=readngwindowswidth/2;
              }
           for (cnt =0 ; cnt<wavHeader.data1.length-1;cnt++){
               if (dsc1[cnt]!=null)
               wavHeader.data1[cnt]=dsc1[cnt];
               else
               wavHeader.data1[cnt]=0.0;    
           } 
           if (wavHeader.getNumChannels()==2){
             for (cnt =0 ; cnt<wavHeader.data1.length-1;cnt++){
               if (dsc2[cnt]!=null)  
               wavHeader.data2[cnt]=dsc2[cnt]; 
               else
               wavHeader.data2[cnt]=0.0;    
               }  
          }
           
          WavHeaderWriter wavHeaderWriter = new WavHeaderWriter(wavHeader);
          wavHeaderWriter.write();
          }
      
    printWaveSpec();
    MusicFile = new Media("file:///"+"/home/ziad/workspace/NetBeans/AudioRedu/AudioReduction/"+FSource.getName());
    MediaP = new MediaPlayer(MusicFile);
    }
    
    
    private void Applynoise() throws IOException {
    Random randomNum = new Random();    
    if (wavHeader.getNumChannels()==1){
        if (wavHeader.getBitsPerSample()==8){
            
            for(int i =0;i<wavHeader.data1.length;i++){
                
                if (randomNum.nextInt(100)< (Integer.valueOf(lbStrenght.getText()))) {
                   wavHeader.data1[i]= wavHeader.data1[i]+(-50+randomNum.nextInt(100));
                }
               
                if (wavHeader.data1[i]<0){
                    wavHeader.data1[i] =1;
                }
                if(wavHeader.data1[i]>255){
                    wavHeader.data1[i]=254;
                }
            }
            
        }else {//16 bits 
            
            for(int i =0;i<wavHeader.data1.length;i++){
                
                if (randomNum.nextInt(100)< (Integer.valueOf(lbStrenght.getText()))) {
                   wavHeader.data1[i]= wavHeader.data1[i]+(-10000+randomNum.nextInt(20000));
                }
               
                if (wavHeader.data1[i]<-32768){
                    wavHeader.data1[i] =-32767;
                }
                if(wavHeader.data1[i]>32767){
                    wavHeader.data1[i]=32766;
                }
            }
            
        }
        
    }else { // else stereo 
        
        if (wavHeader.getBitsPerSample()==8){
            
            for(int i =0;i<wavHeader.data1.length;i++){
                // channel 1 
                if (randomNum.nextInt(100)< (Integer.valueOf(lbStrenght.getText()))) {
                   wavHeader.data1[i]= wavHeader.data1[i]+(-50+randomNum.nextInt(100));
                }
               
                if (wavHeader.data1[i]<0){
                    wavHeader.data1[i] =1;
                }
                if(wavHeader.data1[i]>255){
                    wavHeader.data1[i]=254;
                }         
                
                //channel 2 
                if (randomNum.nextInt(100)< (Integer.valueOf(lbStrenght.getText()))) {
                   wavHeader.data2[i]= wavHeader.data2[i]+(-50+randomNum.nextInt(100));
                }
               
                if (wavHeader.data2[i]<0){
                    wavHeader.data2[i] =1;
                }
                if(wavHeader.data2[i]>255){
                    wavHeader.data2[i]=254;
                } 
                
                
            }
            
        }else {//16 bits 
            
            for(int i =0;i<wavHeader.data1.length;i++){
                //channel 1 
                if (randomNum.nextInt(100)< (Integer.valueOf(lbStrenght.getText()))) {
                   wavHeader.data1[i]= wavHeader.data1[i]+(-10000+randomNum.nextInt(20000));
                }
               
                if (wavHeader.data1[i]<-32768){
                    wavHeader.data1[i] =-32767;
                }
                if(wavHeader.data1[i]>32767){
                    wavHeader.data1[i]=32766;
                }
                
                //channel 2 ;
                
               if (randomNum.nextInt(100)< (Integer.valueOf(lbStrenght.getText()))) {
                   wavHeader.data2[i]= wavHeader.data2[i]+(-10000+randomNum.nextInt(20000));
                }
               
                if (wavHeader.data2[i]<-32768){
                    wavHeader.data2[i] =-32767;
                }
                if(wavHeader.data2[i]>32767){
                    wavHeader.data2[i]=32766;
                }
            }
            
        }
        
        
    }
    
     WavHeaderWriter wavHeaderWriter = new WavHeaderWriter(wavHeader);
     wavHeaderWriter.write();  
    printWaveSpec();
    MusicFile = new Media("file:///"+"/home/ziad/workspace/NetBeans/AudioRedu/AudioReduction/"+FSource.getName());
    MediaP = new MediaPlayer(MusicFile);
    }
    


void ReverseArr(byte[] array){
    for(int i=0; i<array.length/2; i++){
  byte temp = array[i];
  array[i] = array[array.length -i -1];
  array[array.length -i -1] = temp;
}

}
    
    
}
