package nagaseiori.http;

/**
 * 提供将时间或者硬盘空间大小转化成人们方便阅读格式的方法
 */
public class HumanReadableUtil {

    /**
     * 
     */
    private static final String NA = "N/A";
    /**
     * 每种时间单位对应的毫秒数
     */
    private static final long[] TIME_SPAN = { 1, 1000, 1000 * 60, 1000 * 60 * 60, 1000 * 60 * 60 * 24, 1000l * 60 * 60 * 24 * 365 };
    /**
     * 时间单位(中文)
     */
    private static final String[] TIME_SPAN_FORMAT_CH = { "毫秒", "秒", "分", "时", "天", "年" };
    /**
     * 时间单位(英文)
     */
    private static final String[] TIME_SPAN_FORMAT_EN = { "ms ", "sec ", "min ", "hour ", "day ", "year " };

    /**
     * 将用毫秒表示的时间转换成用年月日表示的时间
     * 
     * @param span 时间的毫秒数表示
     * @return 时间的年月日的格式
     */
    public static String timeSpan(long span) {
        return timeSpan(span, 0, false);
    }

    /**
     * 将毫秒格式的时间转化为**年**月**日**时**分**秒**毫秒的格式
     * 
     * @param span 时间的毫秒数表示
     * @param max_len 最多显示的时间单位个数
     * @param chinese 是否为中文
     * @return
     */
    public static String timeSpan(long span, int max_len, boolean chinese) {
        long sp = span;
        int maxlen = max_len;
        if (sp < 0) {
            return NA;
        }
        String[] format = chinese ? TIME_SPAN_FORMAT_CH : TIME_SPAN_FORMAT_EN;
        if (maxlen <= 0) {
            maxlen = 3;
        }
        long tmp = 0;
        int index = 6;
        StringBuilder sb = new StringBuilder("");
        while (--index >= 0) {
            if ((tmp = sp / TIME_SPAN[index]) > 0) {
                sp = sp % TIME_SPAN[index];
                sb.append(tmp);
                sb.append(format[index]);
                if (--maxlen <= 0) {
                    break;
                }
            }
        }
        return sb.toString();
    }

}
