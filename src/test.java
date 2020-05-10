import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class test {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("test");
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        JButton jButton = new JButton("URL");

        jButton.addActionListener(e -> {
            try {
                URL url = new URL("http://openapi.youdao.com/ttsapi?q=good&langType=en&sign=BED8B3A5C217862AC786E3EA74C4EEBC&salt=1588599127531&voice=6&format=mp3&appKey=2f298534dea096b2");
                URLConnection conn;
                conn = url.openConnection();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Player player = new Player(bis);
                player.play();
            } catch (IOException | JavaLayerException ex) {
                ex.printStackTrace();
            }

        });
        jPanel.add(jButton);
        jFrame.add(jPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
