package method.qr.kiarelemb.utils.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class QRRestDayData implements Serializable {
    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int min;
    private final int sec;

    public QRRestDayData(int year, int month, int day, int hour, int min, int sec) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public static QRRestDayData getNullData() {
        int zero = 0;
        return new QRRestDayData(zero, zero, zero, zero, zero, zero);
    }

    public static QRRestDayData getRestDayData(String dateAndTime) {
        StringBuilder sb = new StringBuilder();
        String[] mark = {"年", "月", "天", "时", "分", "秒"};
        String[] res = new String[6];
        int index;
        int preIndex = 0;
        for (int j = 0, markLength = mark.length; j < markLength; j++) {
            String s = mark[j];
            index = dateAndTime.indexOf(s);
            if (index != -1) {
                res[j] = dateAndTime.substring(preIndex, index);
                preIndex = index + 1;
            }
        }
        int[] data = new int[6];
        for (int i = 0, resLength = res.length; i < resLength; i++) {
            String re = res[i];
            data[i] = re == null ? 0 : Integer.parseInt(re);
        }
        return new QRRestDayData(data[0], data[1], data[2], data[3], data[4], data[5]);
    }

    public LocalDateTime getLocalAndTime() {
        return LocalDateTime.of(year, month, day, hour, min, sec);
    }

    public int getDays() {
        return year * 365 + month * 30 + day;
    }

    @Override
    public String toString() {
        if (year + month + day + hour + min + sec <= 0) {
            return "\u5DF2\u8FC7\u671F\uFF01";
        }
        return (year == 0 ? "" : year + "年") + (month == 0 ? "" : month + "月") + (day > 0 ? day + "天" : "") +
                (hour > 0 ? hour + "时" : "") + min + "分" + sec + "秒";
    }

    public int year() {
        return year;
    }

    public int month() {
        return month;
    }

    public int day() {
        return day;
    }

    public int hour() {
        return hour;
    }

    public int min() {
        return min;
    }

    public int sec() {
        return sec;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        QRRestDayData that = (QRRestDayData) obj;
        return this.year == that.year &&
                this.month == that.month &&
                this.day == that.day &&
                this.hour == that.hour &&
                this.min == that.min &&
                this.sec == that.sec;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day, hour, min, sec);
    }

}