import com.tyaer.database.mysql.MySQLHelper;
import com.tyaer.database.mysql.MySQLHelperSingleton;
import com.tyaer.net.httpclient.app.SourceObtainer;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Twin on 2017/7/2.
 */
public class CrawlDomainSource {
    private static final Logger logger = Logger.getLogger(CrawlDomainSource.class);

    public static void main(String[] args) {
        MySQLHelper mysqlHelper = new MySQLHelperSingleton("crawler", "Cr@wler1357", "jdbc:mysql://10.248.161.2:3306/data_collect_center?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&useSSL=true");
//        MySQLHelper mysqlHelper = new MySQLHelperSingleton("root", "izhonghong@2016root123", "jdbc:mysql://192.168.2.115:3306/data_collect_center?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&useSSL=true");

        List<String> pagingQuery = mysqlHelper.pagingQuery("t_domain", "select id,domain_id,domain from t_domain where domain_id>=2000000", 100);
        logger.info("总页数："+pagingQuery.size());
        for (int i = 0; i < pagingQuery.size(); i++) {
            String pagesql=pagingQuery.get(i);
            logger.info(pagesql);
            List<Map<String, Object>> modeResult = mysqlHelper.findModeResult(pagesql);
            logger.info(modeResult.size());
            SourceObtainer sourceObtainer = new SourceObtainer();
            ArrayList<String> list = new ArrayList<>();
            for (int j = 0; j < modeResult.size(); j++) {
                String domain = (String) modeResult.get(j).get("domain");
                list.add(domain);
            }
            ConcurrentHashMap<String, String> sources = sourceObtainer.getSources(list, 10);
//            logger.info(sources);
            StringBuilder stringBuilder = new StringBuilder(0);
            String sql = "update t_domain set domain_name=? where domain=?";
            ArrayList<ArrayList<Object>> arrayLists = new ArrayList<>();
            for (String domain : sources.keySet()) {
                String source = sources.get(domain);
                stringBuilder.append(domain).append("\t").append(source).append("\t").append("\n");
                ArrayList<Object> grams = new ArrayList<>();
                if ("".equals(source)) {
                    source = null;
                }
                grams.add(source);
                grams.add(domain);
                arrayLists.add(grams);
            }
            logger.info("开始更新... "+i);
            mysqlHelper.batchUpdateByPreparedStatement(sql, arrayLists);
            try {
                FileUtils.writeStringToFile(new File("./file/WebSite.txt"), stringBuilder.toString(), "utf-8", true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
