import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class Frame {

    String q = "";
    String from = "en";     //默认英文
    String to = "en";       //默认英文
    String out = "";
    String out_1 = "";
    String json = "";
    JSONObject jsonObject;

    public static void main(String[] args) {
        new Frame().surface();
    }

    public void surface() {

        JFrame jframe = new JFrame("translation");
        JFrame.setDefaultLookAndFeelDecorated(true);
        jframe.setBounds(300, 200, 400, 80);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      //关闭窗口

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        JPanel jPanel_1 = new JPanel();
        jPanel_1.setLayout(new FlowLayout());
        JPanel jPanel_2 = new JPanel();
        jPanel_2.setLayout(new FlowLayout());

        JButton trans = new JButton("翻译");
        JButton voice = new JButton("发音");

        JTextArea input = new JTextArea(2, 40);
        JTextArea display = new JTextArea(2, 40);
        JTextArea web = new JTextArea(8, 50);
        JTextArea basic = new JTextArea(8, 50);

        JLabel jLabel = new JLabel("请选择互相转换的语言：");
        JLabel jLabel_1 = new JLabel("网络释义");
        JLabel jLabel_2 = new JLabel("基本词典");
        JLabel jLabel_3 = new JLabel("输入区域");
        JLabel jLabel_4 = new JLabel("输出区域");

        String[] str = {"英文", "中文", "日文", "韩文", "法文", "西班牙文", "俄文", "葡萄牙文", "德文", "意大利文"};

        JComboBox<String> languageChoose = new JComboBox<>(str);
        JComboBox<String> languageChoose_1 = new JComboBox<>(str);
        languageChoose.addActionListener(e -> {
            out = languageChoose.getSelectedItem().toString();
            to = (String) new Frame().buildType().get(out);
        });
        languageChoose_1.addActionListener(e -> {
            out_1 = languageChoose_1.getSelectedItem().toString();
            from = (String) new Frame().buildType().get(out_1);
        });

        trans.addActionListener(e -> {
            q = input.getText();
            if (q.equals("")) {
                JOptionPane.showMessageDialog(null, "输入不能为空！");
            } else {
                try {
                    json = new TransApi().buildParams(q, from, to);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (json == null) {
                    JOptionPane.showMessageDialog(null, "程序错误！");
                } else {
                    jsonObject = JSONObject.fromObject(json);
                    JSONArray translation = jsonObject.getJSONArray("translation");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < translation.size(); ++i) {
                        sb.append(translation.getString(i) + "\n");
                    }
                    display.setText(sb.toString());
                }
                //英译中
                if (from.equals("en") && to.equals("zh-CHS")) {
                    //基本词典
                    JSONObject basic_1 = jsonObject.getJSONObject("basic");
                    JSONArray explains = basic_1.getJSONArray("explains");
                    StringBuilder sb = new StringBuilder();
                    sb.append("美式音标：" + basic_1.getString("us-phonetic") + "\n");
                    sb.append("英式音标：" + basic_1.getString("uk-phonetic") + "\n");
                    for (int i = 0; i < explains.size(); ++i) {
                        sb.append(explains.getString(i) + "\n");
                    }
                    basic.setText(sb.toString());
                    //网络释义
                    JSONArray webArray = jsonObject.getJSONArray("web");
                    StringBuilder sb_1 = new StringBuilder();
                    sb_1.append("短语：" + "\n");
                    for (int j = 0; j < webArray.size(); ++j) {
                        JSONObject web_1 = webArray.getJSONObject(j);
                        JSONArray value = web_1.getJSONArray("value");
                        String key = web_1.getString("key");
                        sb_1.append(key + "：");
                        for (int k = 0; k < value.size(); ++k) {
                            sb_1.append(value.getString(k) + " ");
                        }
                        sb_1.append("\n");
                    }
                    web.setText(sb_1.toString());
                } else {
                    web.setText("不是英译中，没有网络释义");
                    basic.setText("不是英译中，没有基本词典");
                }
            }
        });

        voice.addActionListener(e -> {
            try {
                URL url = new URL(jsonObject.getString("tSpeakUrl"));
                URLConnection conn;
                conn = url.openConnection();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Player player = new Player(bis);
                player.play();
                player.close();
            } catch (IOException | JavaLayerException ex) {
                ex.printStackTrace();
            }
        });

        jPanel.add(jLabel);
        jPanel.add(languageChoose_1);
        jPanel.add(languageChoose);

        jPanel_1.add(jLabel_3);
        jPanel_1.add(input);
        jPanel_1.add(jLabel_4);
        jPanel_1.add(display);
        jPanel_1.add(trans);
        jPanel_1.add(voice);

        jPanel_2.add(jLabel_1);
        jPanel_2.add(web);
        jPanel_2.add(jLabel_2);
        jPanel_2.add(basic);

        Container p = jframe.getContentPane();
        jframe.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        jframe.add(jPanel);
        jframe.add(jPanel_1);
        jframe.add(jPanel_2);
        jframe.pack();
        jframe.setVisible(true);
    }

    public Map buildType() {
        Map type = new HashMap();
        type.put("中文", "zh-CHS");
        type.put("英文", "en");
        type.put("日文", "ja");
        type.put("韩文", "ko");
        type.put("法文", "fr");
        type.put("西班牙文", "es");
        type.put("俄文", "ru");
        type.put("葡萄牙文", "pt");
        type.put("德文", "de");
        type.put("意大利文", "it");
        return type;
    }
}
