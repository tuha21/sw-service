package com.example.demo.common.util;

import com.example.demo.common.consts.TimeZoneConst;
import com.example.demo.model.PrintReportOrderData;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.time.*;
import java.util.Date;
import java.util.List;

public class Utils {

    public static long getUTCTimestamp() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        return Date.from(now.toInstant()).getTime() / 1000;
    }

    public static int getDateKey(long time) {
        Instant instant = Instant.ofEpochMilli(time * 1000);
        ZonedDateTime zonedDateTimeUTC = instant.atZone(ZoneId.of(TimeZoneConst.TimeOffset.UTC));
        return getDateTimeKey(zonedDateTimeUTC, "yyyyMMdd");
    }

    public static int getDateTimeKey(ZonedDateTime zonedDateTime, String pattern) {
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        String timeKeyString = DateFormatUtils.format(
                Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant()), pattern
        );
        return Integer.valueOf(timeKeyString);
    }

    public static String getStringDateFromDateKey(int dateKey) {
        var stringKey = String.valueOf(dateKey);
        var year = stringKey.substring(0, 4);
        var month = stringKey.substring(4, 6);
        var date = stringKey.substring(6, 8);
        return date + "/" + month + "/" + year;
    }

    public static String getReportHtml(
            List<PrintReportOrderData> data,
            String connectionName,
            String fromText,
            String toText
    ) {
        int totalOrder = data.stream().mapToInt(PrintReportOrderData::getTotal).sum();
        int totalOrderCancelled = data.stream().mapToInt(PrintReportOrderData::getTotalCancelled).sum();
        BigDecimal revenue = BigDecimal.ZERO;
        var header = "<div class='info'>" +
                "                <div class='connection'>Gian hàng:&nbsp;<strong> "+ connectionName +"</strong></div>" +
                "                <div class='times'>Từ ngày: "+ fromText +" - Đến ngày: "+ toText +"</div>" +
                "            </div>";
        var body = new StringBuilder();
        for (PrintReportOrderData item : data) {
            revenue = revenue.add(item.getRevenue());
            body.append("<tr>" + "                            <td>").append(data.indexOf(item) + 1).append("</td>").append("                            <td>").append(item.getDateKey()).append("</td>").append("                            <td>").append(item.getTotal()).append("</td>").append("                            <td>").append(item.getTotalCancelled()).append("</td>").append("                            <td>").append(item.getRevenue()).append("</td>").append("                        </tr>");
        }
        var total = "<tr>" +
                "                            <td>Tổng</td>" +
                "                            <td></td>" +
                "                            <td>"+totalOrder+"</td>" +
                "                            <td>"+totalOrderCancelled+"</td>" +
                "                            <td>"+revenue+"</td>" +
                "                        </tr>";
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Document</title>" +
                "    <link" +
                "    href=\"https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap\"" +
                "    rel=\"stylesheet\">" +
                "    <style>" +
                "        .pdf-wrapper {" +
                "            display: flex;" +
                "            justify-content: center;" +
                "        }" +
                "" +
                "        .print-wrapper {" +
                "            width: 800px;" +
                "        }" +
                "" +
                "        .print-wrapper .header {" +
                "            width: 100%;" +
                "            display: flex;" +
                "            justify-content: center;" +
                "            font-weight: bold;" +
                "            font-size: 18px;" +
                "            height: 70px;" +
                "            align-items: center;" +
                "        }" +
                "" +
                "        .connection {" +
                "            width: 100%;" +
                "            display: flex;" +
                "            justify-content: center;" +
                "        }" +
                "" +
                "        .times {" +
                "            padding: 20px;" +
                "            width: 100%;" +
                "            display: flex;" +
                "            justify-content: center;" +
                "        }" +
                "" +
                "        table {" +
                "            width: 800px;" +
                "        }" +
                "" +
                "        table," +
                "        th," +
                "        td {" +
                "            border-collapse: collapse;" +
                "            border: 1px solid black;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "" +
                "<body style='font-family: 'Roboto', sans-serif;'>" +
                "    <div class='pdf-wrapper'>" +
                "        <div class='print-wrapper'>" +
                "            <div class='header'>BÁO CÁO HIỆU QUẢ KINH DOANH</div>" + header +
                "            <div class='table-data'>" +
                "                <table>" +
                "                    <thead>" +
                "                        <tr>" +
                "                            <th>STT</th>" +
                "                            <th>Ngày</th>" +
                "                            <th>Số lượng đơn hàng</th>" +
                "                            <th>Số lượng đơn hủy</th>" +
                "                            <th>Doanh thu</th>" +
                "                        </tr>" +
                "                    </thead>" +
                "                    <thead>" + total +
                "                    </thead>" +
                "                    <tbody>" + body.toString() +
                "                    </tbody>" +
                "                </table>" +
                "            </div>" +
                "            <div class='note'>" +
                "                <p>" +
                "                <div>" +
                "                    <strong>Ghi chú:</strong>" +
                "                </div>" +
                "                <div>" +
                "                    Thời điểm tính đơn hàng vào báo cáo: sau khi xác nhận vận chuyển;" +
                "                    Loại bỏ những đơn hàng đã huỷ" +
                "                </div>" +
                "                <div>" +
                "                    Số lượng đơn hàng: Số đơn bán trong ngày (chỉ tính những đơn đã xác nhận vận chuyển)" +
                "                </div>" +
                "                <div>" +
                "                    Chi phí: tổng giá vốn của sản phẩm trong đơn (đơn đã xác nhận vận chuyển, trừ dơn huỷ)" +
                "                </div>" +
                "                <div>" +
                "                    Doanh thu = doanh thu các đơn hàng (đã xác nhận vận chuyển) - các đơn hàng huỷ" +
                "                </div>" +
                "                <div>" +
                "                    Lợi nhuận = doanh thu - chi phí" +
                "                </div>" +
                "                </p>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "" +
                "</html>";
    }

}
