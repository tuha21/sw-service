package dev.app.configuration;

import com.google.common.hash.Hashing;
import dev.app.common.consts.TikTokAppConst;
import dev.app.common.util.Utils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TiktokFeignConfig {

    public TiktokFeignConfig() {
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return template -> {
            val timestamp = Utils.getUTCTimestamp();
            template.query("timestamp", String.valueOf(timestamp));

            var sign = generateSignParam(template, timestamp);
            if (StringUtils.isNotBlank(sign)) {
                template.query("sign", sign);
            }
            template.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        };
    }

    private String generateSignParam(RequestTemplate template, long timestamp) {
        if (template.queries().isEmpty()) return "";
        var keys = template.queries().keySet().toArray(new String[0]);
        Arrays.sort(keys);

        StringBuilder params = new StringBuilder();
        for (String key : keys) {
            if (!key.equalsIgnoreCase("access_token")) {
                var values = template.queries().get(key);
                for (String value : values) {
                    params.append(key).append(value);
                }
            }
        }

        String baseString = String.format("%s%s%s%s", TikTokAppConst.APP_SECRET, template.path(), params, TikTokAppConst.APP_SECRET);
        String sign = Hashing.hmacSha256(TikTokAppConst.APP_SECRET.getBytes(StandardCharsets.UTF_8))
                .hashString(baseString, StandardCharsets.UTF_8).toString();
        return sign;
    }

}
