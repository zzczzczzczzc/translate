import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

class JSON {
    public static void analysisJson(String json) {
        //翻译
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONArray translation = jsonObject.getJSONArray("translation");
        for (int i = 0; i < translation.size(); ++i) {
            System.out.println(translation.getString(i));
        }

        //音标+词义
        JSONObject basic = jsonObject.getJSONObject("basic");
        JSONArray explains = basic.getJSONArray("explains");
        for (int i = 0; i < explains.size(); ++i) {
            System.out.println(explains.getString(i));
        }
        //tSpeakUrl 翻译结果发音地址

        //英语
        System.out.println(basic.getString("us-phonetic"));
        System.out.println(basic.getString("us-speech"));
        System.out.println(basic.getString("uk-phonetic"));
        System.out.println(basic.getString("uk-speech"));
/*
        basic.getString("us-phonetic");                                 //美式音标
        basic.getString("us-speech");                                   //美式发音
        basic.getString("uk-phonetic");                                 //英式音标
        basic.getString("uk-speech");                                   //英式发音
*/
        //短语
        JSONArray webArray = jsonObject.getJSONArray("web");
        for (int i = 0; i < webArray.size(); ++i) {
            JSONObject web = webArray.getJSONObject(i);
            JSONArray value = web.getJSONArray("value");
            String key = web.getString("key");
            System.out.print(key + "：");
            for (int j = 0; j < value.size(); ++j) {
                System.out.print(value.getString(j));
            }
            System.out.println();
        }
    }
}
