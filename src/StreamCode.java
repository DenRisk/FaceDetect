import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;


public class StreamCode extends JFrame {

    private BufferedImagePanel imgPanel1;
    Mat frame;
    private FaceDetection fd = new FaceDetection();

    public StreamCode(){
        creatLayout();

    }

    public void creatLayout() {

        setTitle("Video stream");

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new FlowLayout());

        imgPanel1 = new BufferedImagePanel();
        contentPane.add(imgPanel1);
        ActionEvent event = null;
        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        videoProcess();
    }

    public void videoProcess() {

        String filenameFaceCascade = "C:\\Users\\Denis\\Desktop\\Haar Classifier\\haarcascade_frontalface_alt.xml";
        String filenameEyesCascade = "C:\\Users\\Denis\\Desktop\\Haar Classifier\\haarcascade_eye_tree_eyeglasses.xml";

        CascadeClassifier faceCascade = new CascadeClassifier();
        CascadeClassifier eyesCascade = new CascadeClassifier();

        if (!faceCascade.load(filenameFaceCascade)) {
            System.err.println("--(!)Error loading face cascade: " + filenameFaceCascade);
            System.exit(0);
        }
        if (!eyesCascade.load(filenameEyesCascade)) {
            System.err.println("--(!)Error loading eyes cascade: " + filenameEyesCascade);
            System.exit(0);

        }
        VideoCapture capture = new VideoCapture(0);
        frame = new Mat();


        if (!capture.isOpened())
            throw new CvException("The Video File or the Camera could not be opened!");
        capture.read(frame);

        while (capture.read(frame)) {
            fd.detectAndDisplay(frame, faceCascade, eyesCascade);
        imgPanel1.setImage(Mat2BufferedImage(frame));
        pack();
        if (!capture.read(frame)) {
            break;
        }
        }
        capture.release();


    }


    public BufferedImage Mat2BufferedImage(Mat imgMat){
        int bufferedImageType = 0;
        switch (imgMat.channels()) {
            case 1:
                bufferedImageType = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                bufferedImageType = BufferedImage.TYPE_3BYTE_BGR;
                break;
            default:
                throw new IllegalArgumentException("Unknown matrix type. Only one byte per pixel (one channel) or three bytes pre pixel (three channels) are allowed.");
        }
        BufferedImage bufferedImage = new BufferedImage(imgMat.cols(), imgMat.rows(), bufferedImageType);
        final byte[] bufferedImageBuffer = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        imgMat.get(0, 0, bufferedImageBuffer);
        return bufferedImage;

    }



}
