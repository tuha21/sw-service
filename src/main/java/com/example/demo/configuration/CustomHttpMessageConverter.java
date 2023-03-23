package com.example.demo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Custom Http Message converter
 */
public class CustomHttpMessageConverter implements HttpMessageConverter<Object> {
    private final List<String> METRICS = Arrays.asList("metrics", "env", "jolokia", "dump", "configprops", "heapdump", "refresh", "trace");
    @Autowired
    private HttpMessageConverter<Object> defaultMessageConverter;

    public CustomHttpMessageConverter() {
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.http.converter.HttpMessageConverter#canRead(java.lang
     * .Class, org.springframework.http.MediaType)
     */
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return defaultMessageConverter.canRead(clazz, mediaType);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.http.converter.HttpMessageConverter#canWrite(java.
     * lang.Class, org.springframework.http.MediaType)
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return defaultMessageConverter.canWrite(clazz, mediaType);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.http.converter.HttpMessageConverter#
     * getSupportedMediaTypes()
     */
    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return defaultMessageConverter.getSupportedMediaTypes();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.http.converter.HttpMessageConverter#read(java.lang.
     * Class, org.springframework.http.HttpInputMessage)
     */
    @Override
    public Object read(Class<? extends Object> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return defaultMessageConverter.read(clazz, inputMessage);
		/*String data = IOUtils.toString(inputMessage.getBody(), "UTF-8");
		try {
			return objectMapper.readValue(data, clazz);
		} catch (JsonMappingException e) {
			return json.readValue(data, clazz);
		}*/
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.http.converter.HttpMessageConverter#write(java.lang.
     * Object, org.springframework.http.MediaType,
     * org.springframework.http.HttpOutputMessage)
     */
    @Override
    public void write(Object object, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        HttpMessageConverter<Object> httpMessageConverter = getMessageConverter(object);
        httpMessageConverter.write(object, contentType, outputMessage);
    }

    /**
     * Used to find message converter that will be used for serialization
     *
     * @return Message converter
     */
    private HttpMessageConverter<Object> getMessageConverter(Object object) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        val fieldsString = request.getParameter("fields");
        if (!StringUtils.isBlank(fieldsString)) {
            Set<String> fields = new HashSet<>(Arrays.asList(fieldsString.split(",")));
            if (!fields.isEmpty()) {
                ObjectMapper objectMapper = ObjectMapperUtil.initSimpleObjectMapper();

                SimpleFilterProvider filters = new SimpleFilterProvider();
                filters.setFailOnUnknownId(false);
                filters.addFilter("field", SimpleBeanPropertyFilter.filterOutAllExcept(fields));
                filters.addFilter("empty", SimpleBeanPropertyFilter.serializeAllExcept(new HashSet<>()));
                objectMapper.setFilterProvider(filters);
                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                converter.setObjectMapper(objectMapper);
                return converter;
            }
        }
        return defaultMessageConverter;
    }
}