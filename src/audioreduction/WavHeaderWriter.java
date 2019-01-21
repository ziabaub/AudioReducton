/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioreduction;

import com.sun.media.sound.WaveFileWriter;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.Math.round;
import java.nio.ByteBuffer;

/**
 *
 * @author ziad
 */
public class WavHeaderWriter {
    private static final int HEADER_SIZE = 44;

    /**
     * Buffer which contain bytes of the wave header
     */
    private final  byte[] buf = new byte[HEADER_SIZE];
    private byte[] bufother,ba,wa ;
    /**
     * Wave header
     */
    private WavHeader header = new WavHeader();

    /**
     * InputStream to the wave file
     */
    public OutputStream OutputStream;
    public BufferedOutputStream bos;
 
    /**
     * Empty constructor
     */
    public WavHeaderWriter() {

    }


    public WavHeaderWriter(WavHeader waveheader) {
        this.header=waveheader;
    }
    
    /**
     * Read wave header of the file
     *
     * @throws IOException
     * @see WavHeader
     */
    public void write() throws IOException {
   byte[] bytes,bytes1 ;
        OutputStream = new FileOutputStream(header.getFileNmae());
        bos = new BufferedOutputStream(OutputStream);
        bos.write(header.getChunkID());
        bytes = ByteBuffer.allocate(4).putInt(header.getChunkSize()).array();ReverseArr(bytes); 
        bos.write(bytes);
        bos.write(header.getFormat());
        bos.write(header.getSubChunk1ID());
        bytes = ByteBuffer.allocate(4).putInt(header.getSubChunk1Size()).array();ReverseArr(bytes);
        bos.write(bytes);
        bytes1 = ByteBuffer.allocate(2).putShort(header.getAudioFormat()).array();ReverseArr(bytes1);
        bos.write(bytes1);
        bytes1 = ByteBuffer.allocate(2).putShort(header.getNumChannels()).array();ReverseArr(bytes1);
        bos.write(bytes1);
        bytes = ByteBuffer.allocate(4).putInt(header.getSampleRate()).array();ReverseArr(bytes);
        bos.write(bytes);
        bytes = ByteBuffer.allocate(4).putInt(header.getByteRate()).array();ReverseArr(bytes);
        bos.write(bytes);
        bytes1 = ByteBuffer.allocate(2).putShort(header.getBlockAlign()).array();ReverseArr(bytes1);
        bos.write(bytes1);
        bytes1 = ByteBuffer.allocate(2).putShort(header.getBitsPerSample()).array();ReverseArr(bytes1);
        bos.write(bytes1);
        bos.write(header.getSubChunk2ID());
        bytes = ByteBuffer.allocate(4).putInt(header.getSubChunk2Size()).array();ReverseArr(bytes);
        bos.write(bytes);
        bos.write(header.getother());
        //bos.write(header.getDataId());
        bytes = ByteBuffer.allocate(4).putInt(header.getDataIdSize()).array();ReverseArr(bytes);
        bos.write(bytes);
        Double2ByteArrayTest();

        bos.flush();
        OutputStream.flush();
	bos.close();
	OutputStream.close();
    }

void ReverseArr(byte[] array){
    for(int i=0; i<array.length/2; i++){
  byte temp = array[i];
  array[i] = array[array.length -i -1];
  array[array.length -i -1] = temp;
}

}

    private void Double2ByteArrayTest() throws IOException {
        
        
                if (header.getNumChannels() == 1) {
                    if (header.getBitsPerSample() == 8) {
                        ba = new byte[header.data1.length];
                        for (int i = 0; i < header.data1.length; i++) {
                             ba[i]= (byte) round(header.data1[i]) ;
                        }
                        bos.write(ba);
                    } else {
                        wa = new byte[header.data1.length];
                        for (int i = 0; i < header.data1.length; i++) {
                            wa[i] =(byte) round (header.data1[i]) ;
                        }
                        bos.write(wa);
                    }
                } else { //8 bytes
                    if (header.getBitsPerSample() == 8) {
                        ba = new byte[header.data1.length*2];
                        for (int i = 0; i < header.data1.length; i++) {
                             ba[i * 2]=(byte) round(header.data1[i]) ;
                             ba[i * 2 + 1]= (byte) round(header.data2[i]);
                        }
                        bos.write(ba);
                    } else {//16 bytes
                        wa = new byte[header.data1.length*2];
                        for (int i = 0; i < header.data1.length; i++) {
                            wa[i * 2] = (byte) round(header.data1[i]);
                            wa[i * 2 + 1] =(byte) round(header.data2[i]);
                        }
                       bos.write(wa);
                    }
                }

    }




 



}
